package de.prob.ui.ltl.pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class PatternManagerDialog extends Dialog {

	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 400;

	private Label titleLabel;
	private TableViewer patternTableViewer;
	private Text descriptionText;
	private Text codeText;

	public PatternManagerDialog(Shell parentShell) {
		super(parentShell);
	}

	protected void createTitle(Composite parent) {
		Composite titleComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		titleComposite.setLayout(layout);
		titleComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Title label
		titleLabel = new Label(titleComposite, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalIndent = IDialogConstants.VERTICAL_MARGIN;
		data.horizontalIndent = IDialogConstants.HORIZONTAL_MARGIN;
		titleLabel.setLayoutData(data);
		titleLabel.setFont(JFaceResources.getHeaderFont());
		titleLabel.setText("Pattern manager");

		// Bottom separator
		Label separator = new Label(titleComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalIndent = IDialogConstants.VERTICAL_MARGIN;
		separator.setLayoutData(data);
	}

	protected void createPatternTable(Composite parent) {
		patternTableViewer = new TableViewer(parent, SWT.MULTI);
		Table table = patternTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	protected void createDescriptionAndCode(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Left separator
		Label separator = new Label(composite, SWT.VERTICAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_VERTICAL));

		// Description and code
		Composite contentComposite = new Composite(composite, SWT.NONE);
		contentComposite.setLayout(new GridLayout(1, false));
		contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label descriptionLabel = new Label(contentComposite, SWT.NONE);
		descriptionLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		descriptionLabel.setText("Description:");

		descriptionText = new Text(contentComposite, SWT.BORDER);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label codeLabel = new Label(contentComposite, SWT.NONE);
		codeLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		codeLabel.setText("Code:");

		codeText = new Text(contentComposite, SWT.BORDER);
		codeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// Dialog area
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		control.setLayout(layout);

		// Title
		createTitle(control);

		// Content
		SashForm sashForm = new SashForm(control, SWT.HORIZONTAL);
		sashForm.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = DEFAULT_WIDTH;
		data.heightHint = DEFAULT_HEIGHT;
		sashForm.setLayoutData(data);

		// Content
		createPatternTable(sashForm);
		createDescriptionAndCode(sashForm);

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




}
