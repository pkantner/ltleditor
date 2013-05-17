package de.prob.ui.viewer;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.prob.ui.viewer.annotation.AnnotationManager;

public class StyledTextViewer {

	public static final String DEFAULT_FONT_NAME = "Consolas";
	public static final int DEFAULT_FONT_SIZE = 10;

	private IDocument document;

	private SourceViewer sourceViewer;
	private CompositeRuler verticalRuler;

	private AnnotationManager annotationManager;

	private boolean lineNumbersVisible = false;
	private LineNumberRulerColumn lineNumberRulerColumn;

	public StyledTextViewer(Composite parent) {
		this(parent,  SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	}

	public StyledTextViewer(Composite parent, int styles) {
		document = new Document();

		verticalRuler = new CompositeRuler();
		sourceViewer = new SourceViewer(parent, verticalRuler, null, true, styles);

		sourceViewer.setDocument(document);
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

	public AnnotationManager getAnnotationManager() {
		return annotationManager;
	}

}
