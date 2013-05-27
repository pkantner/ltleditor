package de.prob.ui.ltl.pattern;

import java.util.LinkedList;
import java.util.List;

public class PatternManager {

	private List<PatternInfo> patterns = new LinkedList<PatternInfo>();

	public void add(PatternInfo pattern) {
		patterns.add(pattern);
	}

	public void add(String name, int args) {
		add(new PatternInfo(name, args));
	}

	public void remove(PatternInfo pattern) {
		patterns.remove(pattern);
	}

	public void remove(int index) {
		patterns.remove(index);
	}

	public List<PatternInfo> getPatterns() {
		return patterns;
	}

}
