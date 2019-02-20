package org.monet.editor.ui;

import java.util.ArrayList;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Display;

public class WorkspaceManager {
	private static WorkspaceManager instance;
	private ArrayList<IWorkspaceListener> listenerList;

	public interface IWorkspaceListener {
		public void changed(IResourceChangeEvent event);
	}
	
	private WorkspaceManager() {
		this.listenerList = new ArrayList<IWorkspaceListener>();
	}
	
	public static WorkspaceManager instance() {
		if (instance == null)
			instance = new WorkspaceManager();
		return instance;	
	}

	public void init() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new IResourceChangeListener() {
			public void resourceChanged(final IResourceChangeEvent event) {
				if (event.getType() != IResourceChangeEvent.POST_CHANGE) return;
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						notifyWorkspaceChanged(event);
					}
				});
			}
		});
	}
	
	public void addListener(IWorkspaceListener listener) {
		listenerList.add(listener);
	}

	public void removeListener(IWorkspaceListener listener) {
		listenerList.remove(listener);
	}	

	public void notifyWorkspaceChanged(IResourceChangeEvent event) {
		for (IWorkspaceListener listener : listenerList)
			listener.changed(event);
	}	

}
