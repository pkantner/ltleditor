package de.prob.ui.ltl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class LtlCheckingView extends ViewPart {

	private Composite container;
	private FormulaTextViewer formulaTextViewer;

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, true));

		formulaTextViewer = new FormulaTextViewer(container);
		formulaTextViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		formulaTextViewer.setPartitionScanner(new LtlScanner());
		formulaTextViewer.setText("true and false");
	}

	@Override
	public void setFocus() {
		container.setFocus();
	}

}
