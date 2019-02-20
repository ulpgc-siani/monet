package org.monet.editor.core;

import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.monet.editor.MonetLog;
import org.monet.editor.library.StreamHelper;

public class ProjectHelper {

  public static final String                   PROJECT_PACKAGE_BASE              = "package";

  public static String getPackageBase(IProject project) {
    Properties properties = loadProperties(project);
    return properties.getProperty(PROJECT_PACKAGE_BASE);
  }

  private static Properties loadProperties(IProject project) {
    Properties properties = new Properties();
    InputStream is = null;
    try {
      IFile propertiesFile = project.getFile(".preferences.properties");

      if (!propertiesFile.isSynchronized(IFile.DEPTH_ZERO))
        propertiesFile.refreshLocal(IFile.DEPTH_ZERO, null);
      is = propertiesFile.getContents();
      properties.loadFromXML(is);
      return properties;

    } catch (Exception e) {
      MonetLog.print(e);
    } finally {
      StreamHelper.close(is);
    }
    return null;
  }

}
