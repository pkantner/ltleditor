package de.prob.ui.ltl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.prob.ui.viewer.StyledTextViewer;
import de.prob.ui.viewer.annotation.ErrorAnnotation;
import de.prob.ui.viewer.annotation.WarningAnnotation;

public class LtlCheckingView extends ViewPart {

	private Composite container;
	private StyledTextViewer textViewer;

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, true));

		LtlSourceViewerConfiguration config = new LtlSourceViewerConfiguration();

		textViewer = new StyledTextViewer(container, config);
		textViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textViewer.setPartitionScanner(config.getPartitionScanner());
		textViewer.setAnnotationsVisible(true);
		textViewer.setLineNumbersVisible(true);

		// Examples: Can be removed
		textViewer.setText("/* This is\n   a multiple line\n   comment.\n*/\nGF(true) =>// Single line comment\ntrue or false");
		textViewer.getAnnotationManager().addAnnotation(new ErrorAnnotation("Test", 5, 2));
		textViewer.getAnnotationManager().addAnnotation(new ErrorAnnotation("Test2", 10, 2));
		textViewer.getAnnotationManager().addAnnotation(new WarningAnnotation("W", 10, 2));
	}

	@Override
	public void setFocus() {
		container.setFocus();
	}

}
