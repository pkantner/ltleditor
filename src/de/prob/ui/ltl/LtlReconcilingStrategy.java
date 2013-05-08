package de.prob.ui.ltl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;

import de.prob.ltl.parser.LtlLexer;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.ParserFactory;
import de.prob.ltl.parser.symboltable.Symbol;
import de.prob.ltl.parser.warning.WarningListener;
import de.prob.ui.ltl.annotation.ErrorAnnotation;
import de.prob.ui.ltl.annotation.WarningAnnotation;

public class LtlReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {

	private FormulaTextViewer textViewer;

	public LtlReconcilingStrategy(FormulaTextViewer textViewer) {
		this.textViewer = textViewer;
	}

	@Override
	public void setDocument(IDocument document) {
		// IGNORE
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion region) {
		reconcile(region);
	}

	@Override
	public void reconcile(IRegion region) {
		String input = textViewer.getText();
		textViewer.getAnnotationModel().removeAllAnnotations();
		parse(input);
	}

	@Override
	public void initialReconcile() {
		reconcile(null);
	}

	@Override
	public void setProgressMonitor(IProgressMonitor arg0) {
		// IGNORE
	}

	protected void parse(String input) {
		LtlLexer lexer = ParserFactory.createLtlLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(new BaseErrorListener() {

			@Override
			public void syntaxError(Recognizer<?, ?> recognizer,
					Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				System.out.println("Lexer error: " + msg);

				int offset = charPositionInLine;
				int length = 1;
				try {
					offset += textViewer.getSourceViewer().getDocument().getLineOffset(line);
				} catch (BadLocationException ex) {
				}
				if (offendingSymbol instanceof Token) {
					Token token = (Token) offendingSymbol;
					length = token.getStopIndex() - token.getStartIndex() + 1;
				}
				textViewer.getAnnotationModel().addAnnotation(new ErrorAnnotation(msg), new Position(offset, length));
			}

		});
		LtlParser parser = ParserFactory.createLtlParser(lexer);
		parser.removeErrorListeners();
		parser.addErrorListener(new BaseErrorListener() {

			@Override
			public void syntaxError(Recognizer<?, ?> recognizer,
					Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				System.out.println("Parser error: " + msg + " " + offendingSymbol);

				int offset = charPositionInLine;
				int length = 1;
				try {
					offset += textViewer.getSourceViewer().getDocument().getLineOffset(line);
				} catch (BadLocationException ex) {
				}
				if (offendingSymbol instanceof Token) {
					Token token = (Token) offendingSymbol;
					length = token.getStopIndex() - token.getStartIndex() + 1;
					if (length == 0) {
						offset = Math.max(0, --offset);
						length = 1;
					}
				}
				textViewer.getAnnotationModel().addAnnotation(new ErrorAnnotation(msg), new Position(offset, length));
			}

		});
		parser.addWarningListener(new WarningListener() {

			@Override
			public void warning(String msg, Symbol... symbols) {
				System.out.println("Parser warning: " + msg);
				for (Symbol symbol : symbols) {
					Token token = symbol.getToken();
					int offset = token.getStartIndex();
					int length = token.getStopIndex() - offset + 1;
					textViewer.getAnnotationModel().addAnnotation(new WarningAnnotation(msg), new Position(offset, Math.max(1, length)));
				}
			}

		});

		ParseTree ast = parser.start();
		parser.semanticCheck(ast);
		System.out.println("--------------------------------------------------------------");
	}

}
