package de.prob.ui.ltl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class LtlCheckingView extends ViewPart {

	private Composite container;

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, true));
	}

	@Override
	public void setFocus() {
		container.setFocus();
	}

}
