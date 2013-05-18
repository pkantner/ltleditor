package de.prob.ui.viewer.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorProvider implements ISharedTextColors {

	private Map<RGB, Color> colors = new HashMap<RGB, Color>();

	@Override
	public Color getColor(RGB rgb) {
		Color color = colors.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colors.put(rgb, color);
		}
		return color;
	}

	@Override
	public void dispose() {
		for (Color color : colors.values()) {
			color.dispose();
		}
		colors.clear();
	}

}
