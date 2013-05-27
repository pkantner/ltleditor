package de.prob.ui.ltl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.prob.ui.ltl.pattern.PatternManagerDialog;
import de.prob.ui.viewer.StyledTextViewer;

public class LtlCheckingView extends ViewPart {

	private Composite container;
	private ToolBar toolbar;
	private StyledTextViewer textViewer;

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, true));

		toolbar = new ToolBar(container, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		ToolItem patternItem = new ToolItem(toolbar, SWT.PUSH);
		patternItem.setText("Pattern manager");
		Image image = AbstractUIPlugin.imageDescriptorFromPlugin("de.prob.ui.ltleditor", "icons/pattern.png").createImage();
		patternItem.setImage(image);
		patternItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				PatternManagerDialog patternManager = new PatternManagerDialog(Display.getCurrent().getActiveShell());
				patternManager.open();
			}
		});

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
