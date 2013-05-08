package de.prob.ui.ltl;

import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.prob.ui.ltl.annotation.ErrorAnnotation;
import de.prob.ui.ltl.annotation.WarningAnnotation;

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
		formulaTextViewer.setReconciler(new MonoReconciler(new LtlReconcilingStrategy(formulaTextViewer), false));
		formulaTextViewer.setLineNumbersVisible(true);
		formulaTextViewer.setText("true and false");

		formulaTextViewer.registerAnnotationType(ErrorAnnotation.getAnnotationTypeInfo());
		formulaTextViewer.registerAnnotationType(WarningAnnotation.getAnnotationTypeInfo());
	}

	@Override
	public void setFocus() {
		container.setFocus();
	}

}
