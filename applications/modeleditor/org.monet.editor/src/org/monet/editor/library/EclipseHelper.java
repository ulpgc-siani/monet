package org.monet.editor.library;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.monet.editor.MonetLog;
import org.monet.editor.dsl.ui.internal.MonetLocalizationLanguageActivator;

import com.google.inject.Injector;

public class EclipseHelper {

  public static void showErrorMessage(final String message) {
    showErrorMessage(message, message);
  }
  
  public static void showErrorMessage(final String title, final String message) {
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        Shell shell = Display.getCurrent().getActiveShell();
        if(shell == null)
          shell = Display.getCurrent().getShells()[0];
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
        messageBox.setMessage(message);
        messageBox.setText(title);
        messageBox.open();
      }
    });
  }
  
  public static void createFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
    IContainer parent = folder.getParent();
    
    if (parent instanceof IFolder) {
      createFolder((IFolder) parent, monitor);
    }
    
    if (!folder.exists()) {
      folder.create(false, true, monitor);
    }
  }
  
  public static void createFile(IFile file, String content, IProgressMonitor monitor) throws Exception {
    InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
    createFile(file, is, monitor);
  }
  
  public static void createFile(IFile file, InputStream contentStream, IProgressMonitor monitor) throws Exception {
    if(file.exists()) {
      file.setCharset("UTF-8", monitor);
      file.setContents(contentStream, true, false, null);
    } else {
      IContainer parent = file.getParent();
      if (parent instanceof IFolder)  
        createFolder((IFolder) parent, monitor);
      file.create(contentStream, true, null);
    }
  }
  
  public static EObject getModelForResource(IResource resource) {
    Injector injector = MonetLocalizationLanguageActivator.getInstance().getInjector(MonetLocalizationLanguageActivator.ORG_MONET_EDITOR_DSL_MONETMODELINGLANGUAGE);
    IResourceSetProvider resourceSetProvider = injector.getInstance(IResourceSetProvider.class);
    
    URI uri = URI.createURI("platform:/resource/" + resource.getProject().getName() + "/" + resource.getProjectRelativePath());
    ResourceSet set = resourceSetProvider.get(resource.getProject());
    try {
      Resource res = set.getResource(uri, true);
      if (res != null) {
        return res.getContents().get(0);
      }
    } catch (Exception e) {
    }
    return null;
  }

  public static void saveModelToResource(EObject model) {
    try {
      SaveOptions options = SaveOptions.newBuilder().noValidation().getOptions();
      model.eResource().save(options.toOptionsMap());
    } catch(Exception e) {
      MonetLog.print(e);
    }
  }
}
