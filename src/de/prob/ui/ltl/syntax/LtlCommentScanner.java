package de.prob.ui.ltl.syntax;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.RGB;

public class LtlCommentScanner extends RuleBasedScanner {

	public static final RGB COLOR_LTL_COMMENTS_TEXT = new RGB(23, 164, 73);

	public LtlCommentScanner(ISharedTextColors colorProvider) {
		IToken comment = new Token(new TextAttribute(colorProvider.getColor(COLOR_LTL_COMMENTS_TEXT)));
		// No need to scan the comment partitions again, so just use the default return token
		setDefaultReturnToken(comment);
	}

}
