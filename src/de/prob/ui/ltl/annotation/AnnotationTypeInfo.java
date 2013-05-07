package de.prob.ui.ltl.annotation;

import org.eclipse.jface.text.source.AnnotationPainter.IDrawingStrategy;
import org.eclipse.jface.text.source.AnnotationPainter.ITextStyleStrategy;
import org.eclipse.swt.graphics.Color;


public class AnnotationTypeInfo {

	private String type;
	private Color color;

	private final String drawingStrategyId;
	private final String textStyleStrategyId;
	private IDrawingStrategy drawingStrategy;
	private ITextStyleStrategy textStyleStrategy;

	public AnnotationTypeInfo(String type, Color color) {
		this.type = type;
		this.color = color;

		drawingStrategyId = type + "_drawing_strategy";
		textStyleStrategyId = type + "_text_style_strategy";
	}

	public String getType() {
		return type;
	}

	public Color getColor() {
		return color;
	}

	public String getDrawingStrategyId() {
		return drawingStrategyId;
	}

	public String getTextStyleStrategyId() {
		return textStyleStrategyId;
	}

	public IDrawingStrategy getDrawingStrategy() {
		return drawingStrategy;
	}

	public void setDrawingStrategy(IDrawingStrategy drawingStrategy) {
		this.drawingStrategy = drawingStrategy;
	}

	public ITextStyleStrategy getTextStyleStrategy() {
		return textStyleStrategy;
	}

	public void setTextStyleStrategy(ITextStyleStrategy textStyleStrategy) {
		this.textStyleStrategy = textStyleStrategy;
	}

}
