package de.prob.ui.ltl.annotation;

import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ErrorAnnotation extends AbstractAnnotation {

	public static final String ERROR_ANNOTATION_TYPE = "annotation.type.error";
	public static final RGB ERROR_ANNOTATION_COLOR = new RGB(255, 0, 0);
	public static final String ERROR_ANNOTATION_IMAGE = ISharedImages.IMG_OBJS_ERROR_TSK;
	public static final String ERROR_ANNOTATION_GROUP_LABEL = "Errors";

	public ErrorAnnotation(String text) {
		super(ERROR_ANNOTATION_TYPE, text);
		setGroupLabel(ERROR_ANNOTATION_GROUP_LABEL);
		setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ERROR_ANNOTATION_IMAGE));
		setLayer(3);
	}

	@SuppressWarnings("deprecation")
	public static AnnotationTypeInfo getAnnotationTypeInfo() {
		// TODO: use shared colors
		AnnotationTypeInfo info = new AnnotationTypeInfo(ERROR_ANNOTATION_TYPE, new Color(Display.getCurrent(), ERROR_ANNOTATION_COLOR));
		info.setDrawingStrategy(new AnnotationPainter.SquigglesStrategy());

		return info;
	}

}
