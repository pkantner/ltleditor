package de.prob.ui.ltl;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.AnnotationBarHoverManager;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.prob.ui.ltl.annotation.AnnotationMarkerAccess;
import de.prob.ui.ltl.annotation.AnnotationTypeInfo;
import de.prob.ui.ltl.annotation.DefaultInformationControlCreator;

public class FormulaTextViewer {

	public static final String DEFAULT_FONT_NAME = "Consolas";
	public static final int DEFAULT_FONT_SIZE = 10;

	private IDocument document;
	private BaseSourceViewerConfiguration config;
	private SourceViewer sourceViewer;

	private AnnotationModel annotationModel;
	private IAnnotationAccess annotationAccess;

	private CompositeRuler verticalRuler;
	private AnnotationRulerColumn annotationRulerColumn;
	private LineNumberRulerColumn lineNumberRulerColumn;
	private AnnotationPainter annotationPainter;
	private AnnotationBarHoverManager annotationHoverManager;

	public FormulaTextViewer(Composite parent) {
		this(parent,  SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
	}

	public FormulaTextViewer(Composite parent, int styles) {
		document = new Document();
		config = new BaseSourceViewerConfiguration();

		initAnnotations();
		sourceViewer = new SourceViewer(parent, verticalRuler, null, true, styles);

		sourceViewer.configure(config);
		sourceViewer.setDocument(document, annotationModel);
		setFont(new Font(Display.getCurrent(), DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, SWT.NORMAL));
		initAnnotationPainterAndHover();
	}

	protected void initAnnotations() {
		annotationModel = new AnnotationModel();
		annotationAccess = new AnnotationMarkerAccess();

		annotationRulerColumn = new AnnotationRulerColumn(annotationModel, 12, annotationAccess);
		verticalRuler = new CompositeRuler();
		verticalRuler.setModel(annotationModel);
		verticalRuler.addDecorator(0, annotationRulerColumn);
	}

	protected void initAnnotationPainterAndHover() {
		annotationPainter = new AnnotationPainter(sourceViewer, annotationAccess);
		sourceViewer.addPainter(annotationPainter);
		annotationHoverManager = new AnnotationBarHoverManager(verticalRuler, sourceViewer, new DefaultAnnotationHover(), new DefaultInformationControlCreator());
		annotationHoverManager.install(annotationRulerColumn.getControl());
	}

	public void setLineNumbersVisible(boolean visible) {
		if (visible) {
			if (lineNumberRulerColumn == null) {
				lineNumberRulerColumn = new LineNumberRulerColumn();
			}
			verticalRuler.addDecorator(1, lineNumberRulerColumn);
		} else if (lineNumberRulerColumn != null) {
			verticalRuler.removeDecorator(lineNumberRulerColumn);
			lineNumberRulerColumn = null;
		}
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

	public AnnotationModel getAnnotationModel() {
		return annotationModel;
	}

	public void registerAnnotationType(AnnotationTypeInfo info) {
		annotationRulerColumn.addAnnotationType(info.getType());

		annotationPainter.setAnnotationTypeColor(info.getType(), info.getColor());

		if (info.getDrawingStrategy() != null) {
			annotationPainter.addAnnotationType(info.getType(), info.getDrawingStrategyId());
			annotationPainter.addDrawingStrategy(info.getDrawingStrategyId(), info.getDrawingStrategy());
		}
		if (info.getTextStyleStrategy() != null) {
			annotationPainter.addAnnotationType(info.getType(), info.getTextStyleStrategyId());
			annotationPainter.addTextStyleStrategy(info.getTextStyleStrategyId(), info.getTextStyleStrategy());
		}
	}

}
