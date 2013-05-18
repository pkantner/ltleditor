package de.prob.ui.viewer;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import de.prob.ui.viewer.syntax.AbstractPartitionScanner;

public abstract class AbstractSourceViewerConfiguration extends SourceViewerConfiguration {

	protected AbstractPartitionScanner partitionScanner;
	protected ISharedTextColors colorProvider;
	protected PresentationReconciler presentationReconciler;
	protected IReconciler reconciler;

	public void addDamagerRepairer(ITokenScanner scanner, String contentType) {
		if (presentationReconciler != null) {
			DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
			presentationReconciler.setDamager(dr, contentType);
			presentationReconciler.setRepairer(dr, contentType);
		}
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		if (partitionScanner != null) {
			return partitionScanner.getLegalContantTypes();
		}
		return super.getConfiguredContentTypes(sourceViewer);
	}

	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		if (partitionScanner != null) {
			return partitionScanner.getPartitionType();
		}
		return super.getConfiguredDocumentPartitioning(sourceViewer);
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		if (presentationReconciler != null) {
			return presentationReconciler;
		}
		return super.getPresentationReconciler(sourceViewer);
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		if (reconciler != null) {
			reconciler.install(sourceViewer);
			return reconciler;
		}
		return super.getReconciler(sourceViewer);
	}


	public AbstractPartitionScanner getPartitionScanner() {
		return partitionScanner;
	}

	public void setPartitionScanner(AbstractPartitionScanner partitionScanner) {
		this.partitionScanner = partitionScanner;
		if (presentationReconciler != null) {
			presentationReconciler.setDocumentPartitioning(partitionScanner.getPartitionType());
		}
	}

	public ISharedTextColors getColorProvider() {
		return colorProvider;
	}

	public void setColorProvider(ISharedTextColors colorProvider) {
		this.colorProvider = colorProvider;
	}

	public PresentationReconciler getPresentationReconciler() {
		return presentationReconciler;
	}

	public void setPresentationReconciler(PresentationReconciler presentationReconciler) {
		this.presentationReconciler = presentationReconciler;
		if (partitionScanner != null) {
			presentationReconciler.setDocumentPartitioning(partitionScanner.getPartitionType());
		}
	}

	public IReconciler getReconciler() {
		return reconciler;
	}

	public void setReconciler(IReconciler reconciler) {
		this.reconciler = reconciler;
	}

}
