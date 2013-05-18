package de.prob.ui.viewer.annotation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.source.AnnotationBarHoverManager;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.jface.text.source.AnnotationPainter.ITextStyleStrategy;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;


public class AnnotationManager {

	private SourceViewer sourceViewer;
	private CompositeRuler verticalRuler;

	private boolean enabled = false;
	private AnnotationModel annotationModel;
	private IAnnotationAccess annotationAccess;
	private AnnotationRulerColumn annotationRulerColumn;
	private AnnotationPainter annotationPainter;
	private AnnotationBarHoverManager annotationHoverManager;
	private List<String> registeredAnnotations;

	public AnnotationManager(SourceViewer sourceViewer, CompositeRuler verticalRuler) {
		this.sourceViewer = sourceViewer;
		this.verticalRuler = verticalRuler;
	}

	public void setEnabled(boolean value) {
		if (enabled == value) {
			return; // No change
		}
		enabled = value;
		if (enabled) {
			if (annotationModel == null) {
				annotationModel = new AnnotationModel();
				annotationAccess = new DefaultMarkerAnnotationAccess();
				annotationRulerColumn = new AnnotationRulerColumn(annotationModel, 12, annotationAccess); // TODO: remove magic number
				annotationPainter = new AnnotationPainter(sourceViewer, annotationAccess);
				annotationHoverManager = new AnnotationBarHoverManager(verticalRuler, sourceViewer, new DefaultAnnotationHover(), new DefaultInformationControlCreator());
				registeredAnnotations = new LinkedList<String>();
			}
			verticalRuler.setModel(annotationModel);
			sourceViewer.setDocument(sourceViewer.getDocument(), annotationModel);
			verticalRuler.addDecorator(0, annotationRulerColumn);
			sourceViewer.addPainter(annotationPainter);
			annotationHoverManager.install(annotationRulerColumn.getControl());
		} else {
			verticalRuler.setModel(null);
			sourceViewer.setDocument(sourceViewer.getDocument());
			verticalRuler.removeDecorator(0);
			sourceViewer.removePainter(annotationPainter);
		}
	}

	public void addAnnotation(AbstractAnnotation annotation) {
		if (!registeredAnnotations.contains(annotation.getType())) {
			// Automatic registering of annotation types
			registerAnnotationType(annotation);
			registeredAnnotations.add(annotation.getType());
		}
		annotationModel.addAnnotation(annotation, annotation.getPosition());
	}

	public void removeAllAnnotations() {
		annotationModel.removeAllAnnotations();
	}

	protected void registerAnnotationType(AbstractAnnotation annotation) {
		annotationRulerColumn.addAnnotationType(annotation.getType());

		annotationPainter.setAnnotationTypeColor(annotation.getType(), annotation.getColor());

		IDrawingStrategy drawingStrategy = annotation.getDrawingStrategy();
		ITextStyleStrategy textStyleStrategy = annotation.getTextStyleStrategy();
		if (drawingStrategy != null) {
			annotationPainter.addAnnotationType(annotation.getType(), annotation.getDrawingStrategyId());
			annotationPainter.addDrawingStrategy(annotation.getDrawingStrategyId(), drawingStrategy);
		}
		if (textStyleStrategy != null) {
			annotationPainter.addAnnotationType(annotation.getType(), annotation.getTextStyleStrategyId());
			annotationPainter.addTextStyleStrategy(annotation.getTextStyleStrategyId(), textStyleStrategy);
		}
	}

}
