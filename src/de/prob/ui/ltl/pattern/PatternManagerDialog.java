package de.prob.ui.ltl.pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.prob.ui.ltleditor.Activator;

public class PatternManagerDialog extends Dialog implements ISelectionChangedListener {

	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 400;

	public static final Image IMAGE_PATTERN = Activator.getImageDescriptor("icons/pattern.png").createImage();
	public static final Image IMAGE_BUILTIN_PATTERN = Activator.getImageDescriptor("icons/builtin_pattern.png").createImage();
	public static final Image IMAGE_NEW_PATTERN = Activator.getImageDescriptor("icons/new_pattern.png").createImage();
	public static final Image IMAGE_EDIT_PATTERN = Activator.getImageDescriptor("icons/edit_pattern.png").createImage();
	public static final Image IMAGE_REMOVE_PATTERN = Activator.getImageDescriptor("icons/remove_pattern.png").createImage();
	public static final Image IMAGE_COPY_PATTERN = Activator.getImageDescriptor("icons/copy_pattern.png").createImage();

	private PatternManager patternManager;

	private Label titleLabel;
	private TableViewer patternTableViewer;
	private Text descriptionText;
	private Text codeText;

	private ToolItem newPatternItem;
	private ToolItem editPatternItem;
	private ToolItem removePatternItem;
	private ToolItem copyPatternItem;

	public PatternManagerDialog(Shell parentShell) {
		super(parentShell);

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

	protected void createTitle(Composite parent) {
		// Title label
		titleLabel = new Label(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalIndent = IDialogConstants.VERTICAL_MARGIN;
		data.horizontalIndent = IDialogConstants.HORIZONTAL_MARGIN;
		data.horizontalSpan = 3;
		data.widthHint = DEFAULT_WIDTH;
		titleLabel.setLayoutData(data);
		titleLabel.setFont(JFaceResources.getHeaderFont());
		titleLabel.setText("Pattern manager");

		// Bottom separator
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalIndent = IDialogConstants.VERTICAL_MARGIN;
		data.horizontalSpan = 3;
		separator.setLayoutData(data);
	}

	protected void createPatternTable(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		tableComposite.setLayout(layout);

		patternTableViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.widthHint = 200;
		data.heightHint = DEFAULT_HEIGHT;
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

		// Add items
		patternTableViewer.setInput(patternManager.getPatterns());

		// Separator
		Label separator = new Label(tableComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
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
		// Left separator
		Label separator = new Label(parent, SWT.VERTICAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_VERTICAL));

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
	protected Control createDialogArea(Composite parent) {
		// Dialog area
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		control.setLayout(layout);

		// Title
		createTitle(control);
		// Content
		createPatternTable(control);
		createDescriptionAndCode(control);

		selectionChanged(null);

		return control;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		control.setLayout(layout);
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Top separator
		Label separator = new Label(control, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Composite for buttons
		Composite buttonBarComposite = new Composite(control, SWT.NONE);
		buttonBarComposite.setLayout(new GridLayout(1, false));
		buttonBarComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));

		// Create buttons
		createButtonsForButtonBar(buttonBarComposite);

		return control;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) patternTableViewer.getSelection();
		if (selection.size() == 0) {
			titleLabel.setText("Pattern manager");
			descriptionText.setText("");
			codeText.setText("");

			editPatternItem.setEnabled(false);
			removePatternItem.setEnabled(false);
			copyPatternItem.setEnabled(false);
		} else if (selection.size() == 1) {
			PatternInfo pattern = (PatternInfo) selection.getFirstElement();

			titleLabel.setText(pattern.getName());
			descriptionText.setText(pattern.getDescription());
			codeText.setText(pattern.getCode());

			editPatternItem.setEnabled(!pattern.isLocked());
			removePatternItem.setEnabled(!pattern.isLocked());
			copyPatternItem.setEnabled(true);
		} else {
			titleLabel.setText("Pattern manager");
			descriptionText.setText("");
			codeText.setText("");

			editPatternItem.setEnabled(false);
			removePatternItem.setEnabled(true);
			copyPatternItem.setEnabled(false);
		}
	}

}
