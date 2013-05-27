package de.prob.ui.ltleditor;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);

		IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, 0.35f, layout.getEditorArea());
		folder.addView("de.prob.ui.ltl.LtlCheckingView");
		folder.addView("de.prob.ui.ltl.pattern.PatternManagerView");
	}

}
