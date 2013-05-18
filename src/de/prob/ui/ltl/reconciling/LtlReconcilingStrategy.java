package de.prob.ui.ltl.reconciling;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import de.prob.ltl.parser.LtlLexer;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.ParserFactory;
import de.prob.ltl.parser.symboltable.Symbol;
import de.prob.ltl.parser.warning.WarningListener;
import de.prob.ui.viewer.annotation.AnnotationManager;
import de.prob.ui.viewer.annotation.ErrorAnnotation;
import de.prob.ui.viewer.annotation.WarningAnnotation;

public class LtlReconcilingStrategy implements IReconcilingStrategy {

	private IDocument document;
	private AnnotationManager annotationManager;

	public LtlReconcilingStrategy(AnnotationManager annotationManager) {
		this.annotationManager = annotationManager;
	}

	@Override
	public void reconcile(IRegion region) {
		annotationManager.removeAllAnnotations();
		String input = document.get();

		BaseErrorListener errorListener = new BaseErrorListener() {

			@Override
			public void syntaxError(Recognizer<?, ?> recognizer,
					Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				int offset = 0;
				int length = 1;
				if (offendingSymbol != null) {
					Token token = (Token) offendingSymbol;
					offset = token.getStartIndex();
					length = token.getStopIndex() - offset + 1;
				} else {
					try {
						offset = document.getLineOffset(line - 1) + charPositionInLine;
					} catch (BadLocationException e1) {
					}
				}

				if (length < 1) {
					length = 1;
					offset--;
				}
				annotationManager.addAnnotation(new ErrorAnnotation(msg, Math.max(0, offset), length));
			}

		};

		WarningListener warningListener = new WarningListener() {

			@Override
			public void warning(String message, Symbol... symbols) {
				for (Symbol symbol : symbols) {
					Token token = symbol.getToken();
					int offset = token.getStartIndex();
					int length = token.getText().length();
					annotationManager.addAnnotation(new WarningAnnotation(message, offset, length));
				}
			}

		};

		LtlLexer lexer = ParserFactory.createLtlLexer(input, errorListener);
		LtlParser parser = ParserFactory.createLtlParser(lexer, errorListener);
		parser.addWarningListener(warningListener);

		ParseTree ast = parser.start();
		parser.semanticCheck(ast);
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion region) {
		reconcile(region);
	}

	@Override
	public void setDocument(IDocument document) {
		this.document = document;
	}

}
