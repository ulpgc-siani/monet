package org.monet.editor.dsl.ui.outline.tasks;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.xtext.nodemodel.BidiIterator;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.IXtextEditorCallback;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import com.google.inject.Inject;

public class XtextTaskCalculator extends IXtextEditorCallback.NullImpl {
	public static String taskMarkerType = "";

	@Inject
	private UpdateTaskMarkerJob updateTaskMarkerJob;

	@Override
	public void afterCreatePartControl(XtextEditor editor) {
		updateTaskMarkers(editor);
	}
	
	@Override
	public void afterSave(XtextEditor argEditor) {
		updateTaskMarkers(argEditor);
	}

	private void updateTaskMarkers(XtextEditor editor) {
		updateTaskMarkerJob.cancel();
		updateTaskMarkerJob.setEditor(editor);
		if (updateTaskMarkerJob.isSystem()) {
			updateTaskMarkerJob.setSystem(false);
		}
		updateTaskMarkerJob.setPriority(Job.DECORATE);
		updateTaskMarkerJob.schedule();
	}

	public static class UpdateTaskMarkerJob extends Job {
		@Inject
		private ITaskElementChecker taskElementChecker;
		XtextEditor editor;

		public UpdateTaskMarkerJob() {
			super("Xtext-Task-Marker-Update-Job");
		}

		void setEditor(XtextEditor editor) {
			this.editor = editor;
		}

		@Override
		public IStatus run(final IProgressMonitor monitor) {
			if (!monitor.isCanceled()) {
				try {
					new UpdateTaskMarkersOperation(editor, taskElementChecker).run(SubMonitor.convert(monitor));
				} catch (Exception e) {
				}
			}
			return monitor.isCanceled() ? Status.CANCEL_STATUS : Status.OK_STATUS;
		}
	}

	static class UpdateTaskMarkersOperation extends WorkspaceModifyOperation {
		private XtextEditor editor;
		private ITaskElementChecker taskElementChecker;

		UpdateTaskMarkersOperation(XtextEditor editor, ITaskElementChecker taskElementChecker) {
			this.editor = editor;
			this.taskElementChecker = taskElementChecker;
		}

		@Override
		protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
			IResource resource = this.editor.getResource();
			resource.deleteMarkers(getMarkerType(), true, IResource.DEPTH_INFINITE);
			if (!monitor.isCanceled()) {
				createNewMarkers(this.editor, monitor);
			}
		}

		private void createNewMarkers(final XtextEditor editor, final IProgressMonitor monitor) throws CoreException {
			final IResource resourceInEditor = editor.getResource();
			editor.getDocument().readOnly(
					new IUnitOfWork<Void, XtextResource>() {
						@Override
						public java.lang.Void exec(XtextResource state) throws Exception {
							if (state != null && !state.getContents().isEmpty()) {
								EObject model = state.getContents().get(0);
								ICompositeNode root = NodeModelUtils.getNode(model);
								visit(root, resourceInEditor, monitor);
							}
							return null;
						}
					});
		}

		private void visit(ICompositeNode node, final IResource resource, final IProgressMonitor monitor) throws CoreException {
			BidiIterator<INode> varAllContents = node.getChildren().iterator();
			while (varAllContents.hasNext() && !monitor.isCanceled()) {
				INode nodeInt = varAllContents.next();
				internalCreateMarker(nodeInt, resource);
				if (nodeInt instanceof ICompositeNode) {
					visit((ICompositeNode) nodeInt, resource, monitor);
				}
			}
		}

		private void internalCreateMarker(INode node, IResource resource) throws CoreException {
			String ignorePrefix = taskElementChecker.getPrefixToIgnore(node);
			if (ignorePrefix != null) {
				String markerType = getMarkerType();
				IMarker marker = resource.createMarker(markerType);
				
				String text = node.getText().substring(2);
				marker.setAttribute(IMarker.MESSAGE, text.trim());
				marker.setAttribute(IMarker.LOCATION, "line " + node.getStartLine());
				marker.setAttribute(IMarker.CHAR_START, node.getOffset());
				marker.setAttribute(IMarker.CHAR_END, node.getOffset() + node.getLength());
				marker.setAttribute(IMarker.USER_EDITABLE, false);
			}
		}
	}

	public static String getMarkerType() {
		if (!taskMarkerType.isEmpty())
			return taskMarkerType;
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.ui.editors.annotationTypes");
		
		for (IConfigurationElement e : config) {
			String markerType = e.getAttribute("markerType");
			if (markerType == null) continue;
				
			if (markerType.endsWith(TaskConstants.XTEXT_MARKER_SIMPLE_NAME)) {
				taskMarkerType = markerType;
				break;
			}
		}
		return taskMarkerType;
	}
}
