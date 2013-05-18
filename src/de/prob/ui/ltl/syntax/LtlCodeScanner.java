package de.prob.ui.ltl.syntax;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import de.prob.ltl.parser.LtlLexer;

public class LtlCodeScanner extends RuleBasedScanner {

	public static final	RGB COLOR_LTL_KEYWORDS_TEXT = new RGB(20, 50, 200);
	public static final RGB COLOR_LTL_BOOLEAN_OPERATORS_TEXT = new RGB(163, 73, 164);

	public static final	RGB COLOR_DEFAULT_TEXT = new RGB(0, 0, 0);

	public LtlCodeScanner(ISharedTextColors colorProvider) {
		IToken keywordToken = new Token(new TextAttribute(colorProvider.getColor(COLOR_LTL_KEYWORDS_TEXT)));
		IToken booleanToken = new Token(new TextAttribute(colorProvider.getColor(COLOR_LTL_BOOLEAN_OPERATORS_TEXT), null, SWT.BOLD));
		IToken other = new Token(new TextAttribute(colorProvider.getColor(COLOR_DEFAULT_TEXT)));

		List<IRule> rules = new LinkedList<IRule>();

		// Whitespaces
		rules.add(new WhitespaceRule(new WhiteSpaceDetector()));

		// Rules for words like keywords, scopes and boolean operators
		WordRule wordRule = new WordRule(new WordDetector(), other);
		for (String keyword : LtlLexer.getKeywords()) {
			wordRule.addWord(keyword, keywordToken);
		}
		for (String operator : LtlLexer.getScopes()) {
			wordRule.addWord(operator, keywordToken);
		}
		for (String operator : LtlLexer.getBooleanOperators()) {
			wordRule.addWord(operator, booleanToken);
		}
		rules.add(wordRule);

		// Rule for the symbolic operators
		WordRule symbolRule = new WordRule(new SymbolDetector(LtlLexer.getBooleanOperatorsSymbols()), other);
		for (String operator : LtlLexer.getBooleanOperatorsSymbols()) {
			symbolRule.addWord(operator, booleanToken);
		}
		rules.add(symbolRule);

		setRules(rules.toArray(new IRule[rules.size()]));
	}

	protected class WordDetector implements IWordDetector {

		@Override
		public boolean isWordStart(char c) {
			return Character.isAlphabetic(c);
		}

		@Override
		public boolean isWordPart(char c) {
			return Character.isAlphabetic(c);
		}

	}

	protected class SymbolDetector implements IWordDetector {

		private String symbolString;

		public SymbolDetector(String[] symbols) {
			StringBuilder sb = new StringBuilder();
			for (String symbol : symbols) {
				sb.append(symbol);
			}
			symbolString = sb.toString();
		}

		@Override
		public boolean isWordStart(char c) {
			return symbolString.indexOf(c) != -1;
		}

		@Override
		public boolean isWordPart(char c) {
			return isWordStart(c);
		}

	}

	protected class WhiteSpaceDetector implements IWhitespaceDetector {

		@Override
		public boolean isWhitespace(char c) {
			return (c == ' ' | c == '\n' | c == '\r' | c == '\t');
		}

	}

}
