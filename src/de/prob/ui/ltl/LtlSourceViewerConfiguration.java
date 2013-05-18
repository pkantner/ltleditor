package de.prob.ui.ltl;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;

import de.prob.ui.ltl.reconciling.LtlReconcilingStrategy;
import de.prob.ui.ltl.syntax.LtlCodeScanner;
import de.prob.ui.ltl.syntax.LtlCommentScanner;
import de.prob.ui.viewer.AbstractSourceViewerConfiguration;
import de.prob.ui.viewer.annotation.AnnotationManager;
import de.prob.ui.viewer.util.ColorProvider;

public class LtlSourceViewerConfiguration extends AbstractSourceViewerConfiguration {

	public LtlSourceViewerConfiguration(AnnotationManager annotationManager) {
		setPartitionScanner(new LtlPartitionScanner()); // Determines partitions
		setColorProvider(new ColorProvider());
		setPresentationReconciler(new PresentationReconciler());
		setReconciler(new MonoReconciler(new LtlReconcilingStrategy(annotationManager), false));

		// Add damager and repairer for content types
		addDamagerRepairer(new LtlCommentScanner(colorProvider), LtlPartitionScanner.SINGLE_LINE_COMMENT);
		addDamagerRepairer(new LtlCommentScanner(colorProvider), LtlPartitionScanner.MULTI_LINE_COMMENT);
		addDamagerRepairer(new LtlCodeScanner(colorProvider), IDocument.DEFAULT_CONTENT_TYPE);
	}

}
