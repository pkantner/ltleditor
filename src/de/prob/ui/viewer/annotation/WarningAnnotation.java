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

public class WarningAnnotation extends AbstractAnnotation {

	public static final String WARNING_ANNOTATION_TYPE = "__warning_annotation_type__";
	public static final RGB WARNING_ANNOTATION_COLOR = new RGB(244, 200, 45);
	public static final String WARNING_ANNOTATION_IMAGE = ISharedImages.IMG_OBJS_WARN_TSK;

	public WarningAnnotation(String text, Position position) {
		super(WARNING_ANNOTATION_TYPE, text, position);
	}

	public WarningAnnotation(String text, int offset, int length) {
		this(text, new Position(offset, length));
	}

	@Override
	public Color getColor() {
		return new Color(Display.getCurrent(), WARNING_ANNOTATION_COLOR);
	}

	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(WARNING_ANNOTATION_IMAGE);
	}

	@Override
	public int getLayer() {
		return 2;
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
