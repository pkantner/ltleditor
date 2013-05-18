package de.prob.ui.viewer.annotation;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.jface.text.source.AnnotationPainter.ITextStyleStrategy;
import org.eclipse.jface.text.source.IAnnotationPresentation;
import org.eclipse.jface.text.source.ImageUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public abstract class AbstractAnnotation extends Annotation implements IAnnotationPresentation {

	private Position position;

	private final String drawingStrategyId;
	private final String textStyleStrategyId;

	public AbstractAnnotation(String type, String text, Position position) {
		super(type, false, text);
		this.position = position;

		drawingStrategyId = type + "_drawing_strategy__";
		textStyleStrategyId = type + "_text_style_strategy__";
	}

	public abstract Color getColor();
	public abstract Image getImage();
	@Override
	public abstract int getLayer();
	public abstract IDrawingStrategy getDrawingStrategy();
	public abstract ITextStyleStrategy getTextStyleStrategy();

	public Position getPosition() {
		return position;
	}

	public String getDrawingStrategyId() {
		return drawingStrategyId;
	}

	public String getTextStyleStrategyId() {
		return textStyleStrategyId;
	}

	@Override
	public void paint(GC gc, Canvas canvas, Rectangle bounds) {
		Image image = getImage();
		if (image != null) {
			ImageUtilities.drawImage(image, gc, canvas, bounds, SWT.CENTER, SWT.TOP);
		}
	}

}
