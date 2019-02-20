package org.monet.editor;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class MonetPreferences {
	private static final Preferences preferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
	
	public static ScopedPreferenceStore getPreferenceStore() {
		return new ScopedPreferenceStore(InstanceScope.INSTANCE, "/instance/org.monet.editor");
	}

	public static void init() {
		setProperty(Constants.HOME, getDefaultHomePath());
	}	

	public static String getProperty(String name) {
		return preferences.get(name, null);
	}

	public static void setProperty(String name, String value) {
		preferences.put(name, value);	
		try {
			preferences.flush();
		}
		catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static String getHomePath() {
		String path = getProperty(Constants.HOME);
		if (path == null)
			path = getDefaultHomePath();
		return path;		
	}

	private static String getDefaultHomePath() {
		return System.getProperty("user.home") + "/" + Constants.DEFAULT_HOME_PATH;
	}
	
	public static String getTemplatePath() {
	  String path = getHomePath() + "/" + Constants.CURRENT_VERSION + "/" + Constants.TEMPLATES;
	  return path;
	}
	
	public static String getTemplateProjectPath() {
		String path = getHomePath() + "/" + Constants.CURRENT_VERSION + "/" + Constants.TEMPLATES + "/" + Constants.PROJECTS;
		return path;
	}
	
	public static String getTemplateDistributionsPath() {
	  String path = getHomePath() + "/" + Constants.CURRENT_VERSION + "/" + Constants.TEMPLATES + "/" + Constants.DISTRIBUTIONS;
	  return path;
	}
	
	public static String getTemplateDefinitionPath() {
		String path = getHomePath() + "/" + Constants.CURRENT_VERSION + "/" + Constants.TEMPLATES + "/" + Constants.DEFINITIONS;
		return path;
	}
	
	public static String getTemplateDocumentPath() {
		String path = getHomePath() + "/" + Constants.CURRENT_VERSION + "/" + Constants.TEMPLATES + "/" + Constants.DOCUMENTS;
		return path;
	}	

  public static String getTemplateLayoutPath() {
    String path = getHomePath() + "/" + Constants.CURRENT_VERSION + "/" + Constants.TEMPLATES + "/" + Constants.LAYOUTS;
    return path;
  } 

  public static String getTemplateLocalizationPath() {
    String path = getHomePath() + "/" + Constants.CURRENT_VERSION + "/" + Constants.TEMPLATES + "/" + Constants.LOCALIZATION;
    return path;
  } 

}
