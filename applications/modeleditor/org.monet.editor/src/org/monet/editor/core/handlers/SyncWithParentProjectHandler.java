package org.monet.editor.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.monet.editor.core.commands.SyncWithParentProjectCommand;
import org.monet.editor.core.commands.SyncWithParentProjectCommand.Parameters;
import org.monet.editor.ui.ProjectManager;
import org.monet.editor.ui.framework.MonetJob;

public class SyncWithParentProjectHandler extends AbstractHandler {

  
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
	  Parameters params = new Parameters();
	  params.project = ProjectManager.instance().getActiveProjectFromModelExplorer(); 
	  
	  MonetJob.execute("Sync with parent project", new SyncWithParentProjectCommand(params));
	  return null;
  }


}
