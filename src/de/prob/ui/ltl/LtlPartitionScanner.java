package de.prob.ui.ltl;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

import de.prob.ui.viewer.syntax.AbstractPartitionScanner;

public class LtlPartitionScanner extends AbstractPartitionScanner {

	public static final String PARTITION_TYPE = "__ltl_partition__";

	public static final String SINGLE_LINE_COMMENT = "__ltl_single_line_comment__";
	public static final String MULTI_LINE_COMMENT = "__ltl_multi_line_comment__";
	public static final String[] CONTENT_TYPES = { SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT };
	// Only comments, everything else is default content

	public LtlPartitionScanner() {
		IToken singleLineComment = new Token(SINGLE_LINE_COMMENT);
		IToken multiLineComment = new Token(MULTI_LINE_COMMENT);

		addRule(new EndOfLineRule("//", singleLineComment));
		addRule(new MultiLineRule("/*", "*/", multiLineComment, (char) 0, true));
	}

	@Override
	public String[] getLegalContantTypes() {
		return CONTENT_TYPES;
	}

	@Override
	public String getPartitionType() {
		return PARTITION_TYPE;
	}

}
