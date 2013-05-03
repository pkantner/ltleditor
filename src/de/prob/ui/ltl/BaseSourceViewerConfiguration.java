package de.prob.ui.ltl;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class BaseSourceViewerConfiguration extends SourceViewerConfiguration {

	private AbstractPartitionScanner scanner;

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

	public AbstractPartitionScanner getScanner() {
		return scanner;
	}

	public void setScanner(AbstractPartitionScanner scanner) {
		this.scanner = scanner;
	}
}
