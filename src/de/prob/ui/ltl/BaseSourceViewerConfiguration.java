package de.prob.ui.ltl;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class BaseSourceViewerConfiguration extends SourceViewerConfiguration {

	private AbstractPartitionScanner scanner;
	private IReconciler reconciler;

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		if (scanner == null) {
			return super.getConfiguredContentTypes(sourceViewer);
		}
		return scanner.getTypes();
	}

	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		if (scanner == null) {
			return super.getConfiguredDocumentPartitioning(sourceViewer);
		}
		return scanner.getPartitionTypeName();
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		if (scanner == null) {
			return null;
		}

		return scanner.getPresentationReconciler(sourceViewer);
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		return reconciler;
	}

	public AbstractPartitionScanner getScanner() {
		return scanner;
	}

	public void setScanner(AbstractPartitionScanner scanner) {
		this.scanner = scanner;
	}

	public void setReconciler(IReconciler reconciler) {
		this.reconciler = reconciler;
	}

}
