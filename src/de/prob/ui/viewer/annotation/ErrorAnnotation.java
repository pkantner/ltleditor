package de.prob.ui.viewer.annotation;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.jface.text.source.AnnotationPainter.ITextStyleStrategy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ErrorAnnotation extends AbstractAnnotation {

	public static final String ERROR_ANNOTATION_TYPE = "__error_annotation_type__";
	public static final RGB ERROR_ANNOTATION_COLOR = new RGB(255, 0, 0);
	public static final String ERROR_ANNOTATION_IMAGE = ISharedImages.IMG_OBJS_ERROR_TSK;

	public ErrorAnnotation(String text, Position position) {
		super(ERROR_ANNOTATION_TYPE, text, position);
	}

	public ErrorAnnotation(String text, int offset, int length) {
		this(text, new Position(offset, length));
	}

	@Override
	public Color getColor() {
		return new Color(Display.getCurrent(), ERROR_ANNOTATION_COLOR);
	}

	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(ERROR_ANNOTATION_IMAGE);
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IDrawingStrategy getDrawingStrategy() {
		return new AnnotationPainter.SquigglesStrategy();
	}

	@Override
	public ITextStyleStrategy getTextStyleStrategy() {
		return null;
	}

}
