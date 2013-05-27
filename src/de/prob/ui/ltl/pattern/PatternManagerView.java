package de.prob.ui.ltl.pattern;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;

import de.prob.ui.ltleditor.Activator;

public class PatternManagerView extends ViewPart implements ISelectionChangedListener {

	public static final Image IMAGE_PATTERN = Activator.getImageDescriptor("icons/pattern.png").createImage();
	public static final Image IMAGE_BUILTIN_PATTERN = Activator.getImageDescriptor("icons/builtin_pattern.png").createImage();
	public static final Image IMAGE_NEW_PATTERN = Activator.getImageDescriptor("icons/new_pattern.png").createImage();
	public static final Image IMAGE_EDIT_PATTERN = Activator.getImageDescriptor("icons/edit_pattern.png").createImage();
	public static final Image IMAGE_REMOVE_PATTERN = Activator.getImageDescriptor("icons/remove_pattern.png").createImage();
	public static final Image IMAGE_COPY_PATTERN = Activator.getImageDescriptor("icons/copy_pattern.png").createImage();

	private PatternManager patternManager;

	private Composite tableComposite;
	private TableViewer patternTableViewer;
	private Text descriptionText;
	private Text codeText;

	private ToolItem newPatternItem;
	private ToolItem editPatternItem;
	private ToolItem removePatternItem;
	private ToolItem copyPatternItem;

	public PatternManagerView() {
		patternManager = new PatternManager();
		PatternInfo pattern1 = new PatternInfo("absence", 1);
		pattern1.setDescription("Example description");
		pattern1.setCode("def absence<global>(x):\nnot x");
		pattern1.setLocked(true);
		patternManager.add(pattern1);
		PatternInfo pattern2 = new PatternInfo("exists", 2);
		pattern2.setDescription("Example 2");
		pattern2.setCode("def exists(x): x");
		patternManager.add(pattern2);
	}

	@Override
	public void createPartControl(final Composite parent) {
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		parent.setLayout(layout);

		// Patterns
		createPatternTable(parent);

		// Sash
		final int limit = 20;
		final Sash sash = new Sash(parent, SWT.VERTICAL);
		sash.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		sash.addListener (SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event e) {
				Rectangle sashRect = sash.getBounds ();
				Rectangle parentRect = parent.getClientArea ();
				int right = parentRect.width - sashRect.width - limit;
				e.x = Math.max (Math.min (e.x, right), limit);
				if (e.x != sashRect.x)  {
					((GridData) tableComposite.getLayoutData()).widthHint = e.x;
					parent.layout();
				}
			}
		});

		// Description and code
		createDescriptionAndCode(parent);

		// Add items
		patternTableViewer.setInput(patternManager.getPatterns());
		if (patternTableViewer.getTable().getItemCount() > 0) {
			patternTableViewer.getTable().select(0);
		}
		selectionChanged(null);
	}

	protected void createPatternTable(Composite parent) {
		tableComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		tableComposite.setLayout(layout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		patternTableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = 183;
		Table table = patternTableViewer.getTable();
		table.setLayoutData(data);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		patternTableViewer.setContentProvider(new ArrayContentProvider());
		patternTableViewer.addSelectionChangedListener(this);

		// Add columns
		TableViewerColumn nameColumn = createTableViewerColumn("Name", 120);
		nameColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				PatternInfo pattern = (PatternInfo) element;
				return pattern.getName();
			}

			@Override
			public Image getImage(Object element) {
				PatternInfo pattern = (PatternInfo) element;
				if (pattern.isLocked()) {
					return IMAGE_BUILTIN_PATTERN;
				}
				return IMAGE_PATTERN;
			}

		});
		TableViewerColumn argColumn = createTableViewerColumn("Arguments", 80);
		argColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				PatternInfo pattern = (PatternInfo) element;
				return pattern.getArgs() + "";
			}

		});

		// Right separator
		Label separator = new Label(tableComposite, SWT.VERTICAL | SWT.SEPARATOR);
		data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.FILL_VERTICAL);
		data.verticalSpan = 3;
		separator.setLayoutData(data);

		// Bottom separator
		separator = new Label(tableComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Toolbar
		ToolBar toolbar = new ToolBar(tableComposite, SWT.HORIZONTAL);
		toolbar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		newPatternItem = new ToolItem(toolbar, SWT.PUSH);
		newPatternItem.setImage(IMAGE_NEW_PATTERN);
		newPatternItem.setToolTipText("Create new pattern");
		editPatternItem = new ToolItem(toolbar, SWT.PUSH);
		editPatternItem.setImage(IMAGE_EDIT_PATTERN);
		editPatternItem.setToolTipText("Edit pattern");
		removePatternItem = new ToolItem(toolbar, SWT.PUSH);
		removePatternItem.setImage(IMAGE_REMOVE_PATTERN);
		removePatternItem.setToolTipText("Remove pattern");
		copyPatternItem = new ToolItem(toolbar, SWT.PUSH);
		copyPatternItem.setImage(IMAGE_COPY_PATTERN);
		copyPatternItem.setToolTipText("Copy pattern");
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(patternTableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		return viewerColumn;
	}

	protected void createDescriptionAndCode(Composite parent) {
		// Description and code
		Composite contentComposite = new Composite(parent, SWT.NONE);
		contentComposite.setLayout(new GridLayout(1, false));
		contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label descriptionLabel = new Label(contentComposite, SWT.NONE);
		descriptionLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		descriptionLabel.setText("Description:");

		descriptionText = new Text(contentComposite, SWT.BORDER | SWT.MULTI);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label codeLabel = new Label(contentComposite, SWT.NONE);
		codeLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		codeLabel.setText("Code:");

		codeText = new Text(contentComposite, SWT.BORDER | SWT.MULTI);
		codeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	public void setFocus() {
		patternTableViewer.getControl().setFocus();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) patternTableViewer.getSelection();
		if (selection.size() == 0) {
			descriptionText.setText("");
			codeText.setText("");

			editPatternItem.setEnabled(false);
			removePatternItem.setEnabled(false);
			copyPatternItem.setEnabled(false);
			descriptionText.setEnabled(false);
			codeText.setEnabled(false);
		} else if (selection.size() == 1) {
			PatternInfo pattern = (PatternInfo) selection.getFirstElement();

			descriptionText.setText(pattern.getDescription());
			codeText.setText(pattern.getCode());

			editPatternItem.setEnabled(!pattern.isLocked());
			removePatternItem.setEnabled(!pattern.isLocked());
			copyPatternItem.setEnabled(true);
			descriptionText.setEnabled(true);
			codeText.setEnabled(true);
		} else {
			descriptionText.setText("");
			codeText.setText("");

			editPatternItem.setEnabled(false);
			removePatternItem.setEnabled(true);
			copyPatternItem.setEnabled(false);
			descriptionText.setEnabled(false);
			codeText.setEnabled(false);
		}
	}

}
