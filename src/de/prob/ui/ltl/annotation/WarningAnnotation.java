package de.prob.ui.ltl.annotation;

import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class WarningAnnotation extends AbstractAnnotation {

	public static final String WARNING_ANNOTATION_TYPE = "annotation.type.warning";
	public static final RGB WARNING_ANNOTATION_COLOR = new RGB(244, 200, 45);
	public static final String WARNING_ANNOTATION_IMAGE = ISharedImages.IMG_OBJS_WARN_TSK;
	public static final String WARNING_ANNOTATION_GROUP_LABEL = "Warnings";

	public WarningAnnotation(String text) {
		super(WARNING_ANNOTATION_TYPE, text);
		setGroupLabel(WARNING_ANNOTATION_GROUP_LABEL);
		setImage(PlatformUI.getWorkbench().getSharedImages().getImage(WARNING_ANNOTATION_IMAGE));
		setLayer(3);
	}

	@SuppressWarnings("deprecation")
	public static AnnotationTypeInfo getAnnotationTypeInfo() {
		// TODO: use shared colors
		AnnotationTypeInfo info = new AnnotationTypeInfo(WARNING_ANNOTATION_TYPE, new Color(Display.getCurrent(), WARNING_ANNOTATION_COLOR));
		info.setDrawingStrategy(new AnnotationPainter.SquigglesStrategy());

		return info;
	}

}
