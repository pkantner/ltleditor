package de.prob.ui.ltl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import de.prob.ltl.parser.LtlLexer;

public class LtlScanner extends AbstractPartitionScanner {

	// Colors
	public static final	RGB COLOR_LTL_KEYWORDS_TEXT = new RGB(20, 50, 200);
	public static final RGB COLOR_LTL_BOOLEAN_OPERATORS_TEXT = new RGB(163, 73, 164);

	public static final	RGB COLOR_DEFAULT_TEXT = new RGB(0, 0, 0);

	// Types
	public final static String LTL_PARTITION_TYPE = "__LTL_PARTITION_TYPE";

	public final static String LTL_KEYWORDS = "__LTL_KEYWORDS";
	public final static String LTL_BOOLEAN_OPERATOR = "__LTL_BOOLEAN_OPERATOR";

	public LtlScanner() {
		super(new PresentationReconciler());

		addDefaultDamagerRepairer(IDocument.DEFAULT_CONTENT_TYPE, COLOR_DEFAULT_TEXT);

		List<String> allowedCharacters = new LinkedList<String>();
		for (String symbol : LtlLexer.getBooleanOperatorsSymbols()) {
			allowedCharacters.add(symbol);
		}
		for (String symbol : new String[]{" ", "\n", "\t", "\r"}) {
			allowedCharacters.add(symbol);
		}

		addKeywordsRule(allowedCharacters);
		addBooleanOperatorRule(allowedCharacters);
	}

	@Override
	public String[] getTypes() {
		return new String[] {
				LTL_BOOLEAN_OPERATOR,
				LTL_KEYWORDS};
	}

	@Override
	public String getPartitionTypeName() {
		return LTL_PARTITION_TYPE;
	}

	private void addKeywordsRule(List<String> allowedCharacters) {
		for (String word : LtlLexer.getKeywords()) {
			addRule(new ReservedWordRule(word, new Token(LTL_KEYWORDS), allowedCharacters));
		}

		addDefaultDamagerRepairer(LTL_KEYWORDS, COLOR_LTL_KEYWORDS_TEXT);
	}

	private void addBooleanOperatorRule(List<String> allowedCharacters) {
		for (String word : LtlLexer.getBooleanOperators()) {
			addRule(new ReservedWordRule(word, new Token(LTL_BOOLEAN_OPERATOR), allowedCharacters));
		}
		for (String word : LtlLexer.getBooleanOperatorsSymbols()) {
			addRule(new ReservedWordRule(word, new Token(LTL_BOOLEAN_OPERATOR), null));
		}

		addDefaultDamagerRepairer(LTL_BOOLEAN_OPERATOR, COLOR_LTL_BOOLEAN_OPERATORS_TEXT, null, SWT.BOLD);
	}

}
