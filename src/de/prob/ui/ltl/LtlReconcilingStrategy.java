package de.prob.ui.ltl;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;

import de.prob.ltl.parser.LtlLexer;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.symbolcheck.SymbolChecker;
import de.prob.ltl.parser.symbolcheck.SymbolCollector;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.warning.ErrorManager;

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
		System.out.println("reconcile/1: " + input);
		try {
			parse(input);
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
		}
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
		final LtlParser parser = createParser(input);
		ParseTree result = parser.start();

		// Check symbols
		ErrorManager errorManager = new ErrorManager();
		SymbolTable symbolTable = new SymbolTable(errorManager);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new SymbolCollector(symbolTable), result);
		walker.walk(new SymbolChecker(symbolTable), result);
	}

	protected LtlLexer createLexer(String input) {
		ANTLRInputStream inputStream = new ANTLRInputStream(input);
		LtlLexer lexer = new LtlLexer(inputStream);
		return lexer;
	}

	protected LtlParser createParser(String input) {
		LtlParser parser = new LtlParser(new CommonTokenStream(createLexer(input)));
		return parser;
	}

}
