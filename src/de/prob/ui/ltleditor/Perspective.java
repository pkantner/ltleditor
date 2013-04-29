package de.prob.ui.ltleditor;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);

		layout.addView("de.prob.ui.ltl.LtlCheckingView", SWT.TOP, 1.0f, layout.getEditorArea());
	}

}
