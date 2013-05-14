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

	protected Position getAnnotationPosition(Token token, int line, int posInLine) {
		int offset = 0;
		int length = 0;
		if (token != null) {
			offset = token.getStartIndex();
			length = token.getStopIndex() - offset + 1;
		} else {
			try {
				offset = textViewer.getSourceViewer().getDocument().getLineOffset(line - 1) + posInLine;
				length = 1;
			} catch (BadLocationException ex) {
			}
		}

		if (length < 1) {
			length = 1;
			offset--;
		}

		return new Position(Math.max(0, offset), length);
	}

	protected void parse(String input) {
		LtlLexer lexer = ParserFactory.createLtlLexer(input);
		LtlParser parser = ParserFactory.createLtlParser(lexer);
		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		BaseErrorListener listener = new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer,
					Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				System.out.println("Error: " + msg);

				Token token = null;
				if (offendingSymbol != null && offendingSymbol instanceof Token) {
					token = (Token) offendingSymbol;
				}
				Position pos = getAnnotationPosition(token, line, charPositionInLine);
				textViewer.getAnnotationModel().addAnnotation(new ErrorAnnotation(msg), pos);
			}

		};
		lexer.addErrorListener(listener);
		parser.addErrorListener(listener);
		parser.addWarningListener(new WarningListener() {

			@Override
			public void warning(String msg, Symbol... symbols) {
				System.out.println("Warning: " + msg);
				for (Symbol symbol : symbols) {
					Token token = symbol.getToken();

					Position pos = getAnnotationPosition(token, 0, 0);
					textViewer.getAnnotationModel().addAnnotation(new WarningAnnotation(msg), pos);
				}
			}

		});

		ParseTree ast = parser.start();
		parser.semanticCheck(ast);
		System.out.println("--------------------------------------------------------------");
	}

	@Override
	public void setDocument(IDocument document) {
		// IGNORE
	}

	@Override
	public void setProgressMonitor(IProgressMonitor arg0) {
		// IGNORE
	}

}
