package de.prob.ui.ltl.annotation;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

public class DefaultInformationControlCreator implements IInformationControlCreator {

	@Override
	public IInformationControl createInformationControl(Shell shell) {
		return new DefaultInformationControl(shell);
	}

}
