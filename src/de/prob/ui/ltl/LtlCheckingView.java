package de.prob.ui.ltl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.prob.ui.viewer.StyledTextViewer;

public class LtlCheckingView extends ViewPart {

	private Composite container;
	private StyledTextViewer textViewer;

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		textViewer = new StyledTextViewer(container);
		textViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textViewer.setAnnotationsVisible(true);
		textViewer.setLineNumbersVisible(true);
		LtlSourceViewerConfiguration config = new LtlSourceViewerConfiguration(textViewer.getAnnotationManager());
		textViewer.setSourceViewerConfiguration(config);
		textViewer.setPartitionScanner(config.getPartitionScanner());

		// Examples: Can be removed
		textViewer.setText("/* This is\n   a multiple line\n   comment.\n*/\nGF(true) =>// Single line comment\ntrue or false");
	}

	@Override
	public void setFocus() {
		container.setFocus();
	}

}
