package org.monet.editor.ui.hooks;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.monet.editor.MonetLog;
import org.monet.editor.core.commands.ICommand;
import org.monet.editor.core.commands.RefactorCommand;
import org.monet.editor.ui.WorkspaceManager;
import org.monet.editor.ui.WorkspaceManager.IWorkspaceListener;

public class MoveFileHook {
	private static MoveFileHook instance;

	private MoveFileHook() {

	}

	public static MoveFileHook instance() {
		if (instance == null)
			instance = new MoveFileHook();
		return instance;
	}

	public void init() {
		IWorkspaceListener listener = new IWorkspaceListener() {

			@Override
			public void changed(IResourceChangeEvent event) {
				IResourceDelta delta = event.getDelta();
				try {
					delta.accept(new IResourceDeltaVisitor() {
						public boolean visit(IResourceDelta delta) {
						  try {
  							if (delta.getKind() == IResourceDelta.ADDED) {
  								if (delta.getMovedFromPath() == null) return true;
  								ICommand command = new RefactorCommand(delta.getResource(), delta.getMovedFromPath());
  								command.execute();
  							}
  						} catch (Exception e) {
  		          MonetLog.print(e);
  		        }
							return true;
						}
					});
				} catch (Exception e) {
			    MonetLog.print(e);
				}
			}
		};
		WorkspaceManager.instance().addListener(listener);
	}

}
