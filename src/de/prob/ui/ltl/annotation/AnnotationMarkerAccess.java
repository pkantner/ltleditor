package de.prob.ui.ltl.annotation;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;

public class AnnotationMarkerAccess extends DefaultMarkerAnnotationAccess {

	@Override
	public String getTypeLabel(Annotation annotation) {
		if (annotation instanceof AbstractAnnotation) {
			return ((AbstractAnnotation) annotation).getGroupLabel();
		}

		return null;
	}

}
