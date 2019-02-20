package org.monet.editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.monet.editor.core.listeners.PreferencesFileListener;
import org.monet.editor.core.listeners.UserPreferencesFileListener;
import org.monet.editor.core.util.ResourceManager;
import org.monet.editor.core.util.Resources;
import org.monet.editor.library.LibraryZip;
import org.monet.editor.library.StreamHelper;
import org.monet.editor.ui.WorkspaceManager;
import org.monet.editor.ui.hooks.MoveFileHook;
import org.monet.templation.Canvas;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "MonetEditor"; //$NON-NLS-1$
	private static Activator plugin;
	
	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		Canvas.disableCache();
		initResourceManager();
    MonetLog.init(this.getLog());
    WorkspaceManager.instance().init();
    MoveFileHook.instance().init();
    PreferencesFileListener.init();
    UserPreferencesFileListener.init();
    TrayDialog.setDialogHelpAvailable(false);
    initTemplates();
    
    
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	private void initResourceManager() {
    try {
      File internalFile = FileLocator.getBundleFile(getBundle());

      ResourceManager.init(internalFile.getAbsolutePath());
    } catch (IOException e) {
      MonetLog.print(e);
    }
  }
	
	private void initTemplates() {
	  File path = new File(MonetPreferences.getTemplatePath());
	  if(!path.exists()) {
	    LibraryZip.decompress(Resources.getAsStream(Constants.WIZARD_TEMPLATES_PATH), MonetPreferences.getHomePath());
	  }
	  
	  FileOutputStream stream = null;
	  try {
	    stream = new FileOutputStream(new File(MonetPreferences.getHomePath(), Constants.PRIVATE_KEY_TEMPLATE_NAME));
	    StreamHelper.copyData(Resources.getAsStream(Constants.PRIVATE_KEY_TEMPLATE_PATH), stream);
	  } catch(Exception ex) {
	    MonetLog.print(ex);
	  } finally {
	    StreamHelper.close(stream);
	  }
	}
}
