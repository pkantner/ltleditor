package de.prob.ui.viewer;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.prob.ui.viewer.annotation.AnnotationManager;
import de.prob.ui.viewer.syntax.AbstractPartitionScanner;

public class StyledTextViewer {

	public static final String DEFAULT_FONT_NAME = "Consolas";
	public static final int DEFAULT_FONT_SIZE = 10;

	private IDocument document;

	private SourceViewer sourceViewer;
	private SourceViewerConfiguration config;
	private CompositeRuler verticalRuler;

	private AnnotationManager annotationManager;

	private boolean lineNumbersVisible = false;
	private LineNumberRulerColumn lineNumberRulerColumn;

	public StyledTextViewer(Composite parent, SourceViewerConfiguration config) {
		this(parent, config,  SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	}

	public StyledTextViewer(Composite parent, SourceViewerConfiguration config, int styles) {
		this.config = config;
		document = new Document();

		verticalRuler = new CompositeRuler();
		sourceViewer = new SourceViewer(parent, verticalRuler, null, true, styles);

		sourceViewer.setDocument(document);
		sourceViewer.configure(this.config);
		setFont(new Font(Display.getCurrent(), DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, SWT.NORMAL));

		annotationManager = new AnnotationManager(sourceViewer, verticalRuler);
	}

	public void setAnnotationsVisible(boolean visible) {
		annotationManager.setEnabled(visible);
	}

	public void setLineNumbersVisible(boolean visible) {
		if (lineNumbersVisible == visible) {
			return;	// No change
		}
		lineNumbersVisible = visible;
		if (lineNumbersVisible) {
			if (lineNumberRulerColumn == null){
				lineNumberRulerColumn = new LineNumberRulerColumn();
			}
			verticalRuler.addDecorator(1, lineNumberRulerColumn);
		} else {
			verticalRuler.removeDecorator(1);
		}
	}

	public void setPartitionScanner(AbstractPartitionScanner scanner) {
		IDocumentExtension3 documentExtension = (IDocumentExtension3) document;
		IDocumentPartitioner documentPartitioner = documentExtension.getDocumentPartitioner(scanner.getPartitionType());
		if (documentPartitioner != null) {
			// First remove the old scanner
			documentPartitioner.disconnect();
		}

		IDocumentPartitioner partitioner = new FastPartitioner(scanner, scanner.getLegalContantTypes());
		documentExtension.setDocumentPartitioner(scanner.getPartitionType(), partitioner);
		partitioner.connect(document);
	}

	public void setLayoutData(Object layoutData) {
		sourceViewer.getControl().setLayoutData(layoutData);
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

	public SourceViewerConfiguration getSourceViewerConfiguration() {
		return config;
	}

	public AnnotationManager getAnnotationManager() {
		return annotationManager;
	}

}
