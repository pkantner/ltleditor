package de.prob.ui.viewer.syntax;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public abstract class AbstractPartitionScanner extends RuleBasedPartitionScanner {

	public abstract String[] getLegalContantTypes();
	public abstract String getPartitionType();

	protected List<IPredicateRule> rules = new LinkedList<IPredicateRule>();

	public void addRule(IPredicateRule rule) {
		rules.add(rule);
		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
	}

}
