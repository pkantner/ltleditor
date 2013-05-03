package de.prob.ui.ltl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractPartitionScanner extends RuleBasedPartitionScanner {

	private PresentationReconciler reconciler;
	private List<IPredicateRule> rules = new LinkedList<IPredicateRule>();

	/**
	 * Returns the content types of the viewer.
	 * 
	 * @return Array with content types
	 */
	public abstract String[] getTypes();
	/**
	 * Returns the type name of the used partition.
	 * 
	 * @return partition type name
	 */
	public abstract String getPartitionTypeName();

	public AbstractPartitionScanner(PresentationReconciler reconciler) {
		this.reconciler = reconciler;
		this.reconciler.setDocumentPartitioning(getPartitionTypeName());
	}

	/**
	 * Returns the presentation reconciler ready to be used with the given source viewer.
	 *
	 * @param sourceViewer the source viewer
	 * @return the presentation reconciler or <code>null</code> if presentation reconciling should not be supported
	 */
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		return reconciler;
	}

	/**
	 * Adds a rule to the scanner.
	 * The rule must implement the IPredicateRule interface
	 * 
	 * @param rule Rule to be added
	 * @see IPredicateRule
	 */
	public void addRule(IPredicateRule rule) {
		rules.add(rule);
		setPredicateRules(rules.toArray(new IPredicateRule[0]));
	}

	/**
	 * Creates a simple token scanner using the RuleBasedScanner.
	 * 
	 * @param foreground Foreground color for the text
	 * @param background Background color for the text
	 * @param textStyle Text style (i.e. <code>SWT.BOLD, SWT.ITALIC</code>)
	 * @return a RuleBasedScanner object
	 * 
	 * @see RuleBasedScanner
	 * @see ITokenScanner
	 */
	protected ITokenScanner createSimpleTokenScanner(RGB foreground, RGB background, int textStyle) {
		Color fColor = null;
		Color bColor = null;
		if (foreground != null) {
			// TODO use sharedtextcolors
			fColor = new Color(Display.getCurrent(), foreground);
		}
		if (background != null) {
			// TODO use sharedtextcolors
			bColor = new Color(Display.getCurrent(), background);
		}

		RuleBasedScanner scanner = new RuleBasedScanner();
		scanner.setDefaultReturnToken(new Token(new TextAttribute(fColor, bColor, textStyle)));

		return scanner;
	}

	/**
	 * Adds a default syntax driven presentation damager and presentation repairer to the given presentation reconciler.
	 * 
	 * @param contentType The content type under which to register
	 * @param foreground Foreground color for the text
	 */
	protected void addDefaultDamagerRepairer(String contentType, RGB foreground) {
		addDefaultDamagerRepairer(contentType, foreground, null);
	}

	/**
	 * Adds a default syntax driven presentation damager and presentation repairer to the given presentation reconciler.
	 * 
	 * @param contentType The content type under which to register
	 * @param foreground Foreground color for the text
	 * @param background Background color for the text
	 */
	protected void addDefaultDamagerRepairer(String contentType, RGB foreground, RGB background) {
		addDefaultDamagerRepairer(contentType, foreground, background, SWT.NORMAL);
	}

	/**
	 * Adds a default syntax driven presentation damager and presentation repairer to the given presentation reconciler.
	 * 
	 * @param contentType The content type under which to register
	 * @param foreground Foreground color for the text
	 * @param background Background color for the text
	 * @param textStyle Text style (i.e. <code>SWT.BOLD, SWT.ITALIC</code>)
	 */
	protected void addDefaultDamagerRepairer(String contentType, RGB foreground, RGB background, int textStyle) {
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(createSimpleTokenScanner(foreground, background, textStyle));
		reconciler.setDamager(dr, contentType);
		reconciler.setRepairer(dr, contentType);
	}

}
