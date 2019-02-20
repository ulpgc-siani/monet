package org.monet.editor.core.generators;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.monet.editor.MonetLog;
import org.monet.editor.core.util.ResourceManager;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;

public abstract class FileGenerator extends Render {

  public static class Logger implements CanvasLogger {
    @Override
    public void debug(String message, Object... args) {
      MonetLog.print(String.format(message, args));
    }
  }
  
  public FileGenerator() {
    super(new Logger(), ResourceManager.getInstance().getTemplatesPath());
  }
  
  public FileGenerator(String templatePath) {
    super(new Logger(), templatePath);
  }
  
  protected void formatFile(final IFile outputFile, final boolean keepOpenAfterCreate) {
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        if(workbench.getWorkbenchWindowCount() == 0)
          return;
        
        IWorkbenchPage page = workbench.getWorkbenchWindows()[0].getActivePage();
        IEditorPart editor = null;
        try {
          editor = org.eclipse.ui.ide.IDE.openEditor(page, outputFile, true);
          XtextEditor xed = (XtextEditor)editor;
          ((SourceViewer)xed.getInternalSourceViewer()).doOperation(ISourceViewer.FORMAT);
          xed.doSave(null);
          if(!keepOpenAfterCreate)
            xed.close(true);
        } catch (PartInitException e) {
          MonetLog.print(e);
        }
      }
    });
  }

}
