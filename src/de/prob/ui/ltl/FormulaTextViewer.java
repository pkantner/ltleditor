package de.prob.ui.ltl;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class FormulaTextViewer {

	public static final String DEFAULT_FONT_NAME = "Consolas";
	public static final int DEFAULT_FONT_SIZE = 10;

	private IDocument document;
	private BaseSourceViewerConfiguration config;
	private SourceViewer sourceViewer;

	public FormulaTextViewer(Composite parent) {
		this(parent,  SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
	}

	public FormulaTextViewer(Composite parent, int styles) {
		document = new Document();
		config = new BaseSourceViewerConfiguration();
		sourceViewer = new SourceViewer(parent, null, null, true, styles);

		sourceViewer.configure(config);
		sourceViewer.setDocument(document);
		setFont(new Font(Display.getCurrent(), DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, SWT.NORMAL));
	}

	public void setPartitionScanner(AbstractPartitionScanner scanner) {
		IDocumentExtension3 documentExtension = (IDocumentExtension3) document;
		IDocumentPartitioner documentPartitioner = documentExtension.getDocumentPartitioner(scanner.getPartitionTypeName());
		if (documentPartitioner != null) {
			documentPartitioner.disconnect();
		}
		FastPartitioner partitioner = new FastPartitioner(scanner, scanner.getTypes());
		partitioner.connect(document);
		documentExtension.setDocumentPartitioner(scanner.getPartitionTypeName(), partitioner);

		config.setScanner(scanner);
		sourceViewer.unconfigure();
		sourceViewer.configure(config);
	}

	public void setSourceViewerConfiguration(BaseSourceViewerConfiguration config) {
		AbstractPartitionScanner scanner = this.config.getScanner();
		this.config = config;

		setPartitionScanner(scanner);
	}

	public BaseSourceViewerConfiguration getSourceViewerConfiguration() {
		return config;
	}

	public void setReconciler(IReconciler reconciler) {
		config.setReconciler(reconciler);
		sourceViewer.unconfigure();
		sourceViewer.configure(config);
	}

	public void setLayoutData(Object layoutData) {
		sourceViewer.getTextWidget().setLayoutData(layoutData);
	}

	public void setFont(Font font) {
		sourceViewer.getTextWidget().setFont(font);
	}

	public void setText(String text) {
		document.set(text);
	}

	public String getText() {
		return document.get();
	}

	public IDocument getDocument() {
		return document;
	}

	public SourceViewer getSourceViewer() {
		return sourceViewer;
	}

}
