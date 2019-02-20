package org.monet.editor.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.monet.editor.Constants;
import org.monet.editor.MonetLog;
import org.monet.editor.MonetPreferences;
import org.monet.editor.core.generators.ProjectPropertiesFileGenerator;
import org.monet.editor.core.generators.UserPropertiesFileGenerator;
import org.monet.editor.library.StreamHelper;

public final class ProjectHelper extends org.monet.editor.core.ProjectHelper {

  public static final String                 PROJECT_VERSION           = "version";
  public static final String                 PROJECT_PACKAGE_BASE      = "package";
  public static final String                 PROJECT_RELEASE_NUMBER    = "release";
  public static final String                 PROJECT_PARENT_PROJECT    = "parent";
  public static final String                 PROJECT_UUID              = "uuid";

  public static final String                 PROJECT_KEYSTORE_PATH     = "keystore-path";
  public static final String                 PROJECT_KEYSTORE_PASSWORD = "keystore-password";

  private static HashMap<String, Properties> preferencesMap            = new HashMap<String, Properties>();
  private static HashMap<String, Properties> userPreferencesMap        = new HashMap<String, Properties>();

  public static boolean hasError(IProject project, boolean includeReferencedProjects) {
    try {
      IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);

      if (markers != null && markers.length > 0) {
        for (IMarker m : markers) {
          int s = m.getAttribute(IMarker.SEVERITY, -1);
          if (s == IMarker.SEVERITY_ERROR) {
            return true;
          }
        }
      }
      if (includeReferencedProjects) {
        List<IProject> projects = getReferencedProjects(project);

        for (IProject p : projects) {
          if (hasError(p, false)) {
            return true;
          }
        }
      }
    } catch (CoreException e) {
      MonetLog.print(e);
      return true;
    }
    return false;
  }

  public static List<IProject> getReferencedProjects(IProject project) throws CoreException {
    IProject[] projects = project.getReferencedProjects();

    ArrayList<IProject> list = new ArrayList<IProject>();

    for (IProject p : projects) {
      if (p.isOpen() && p.hasNature(Constants.MONET_NATURE_ID)) {
        list.add(p);
      }
    }
    return list;
  }

  public static IProject getParentProject(IProject project) throws CoreException {
    Properties properties = loadPreferences(project);
    String parentName = properties.getProperty(PROJECT_PARENT_PROJECT);
    if ((parentName == null) || (parentName.isEmpty()))
    	return null;
    
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		return workspaceRoot.getProject(parentName);
  }

  // PROJECT FOLDERS

  public static IFolder getProjectFolder(IProject project) {
    return project.getFolder(project.getLocation());
  }

  public static IFolder getLibrariesFolder(IProject project) {
    return project.getFolder(Constants.LIBS_FOLDER);
  }
  
  public static IFolder getResourcesFolder(IProject project) {
    return project.getFolder(Constants.RES_FOLDER);
  }

  public static IFolder getDistFolder(IProject project) {
    return project.getFolder(Constants.DIST_FOLDER);
  }

  public static IFolder getSourceFolder(IProject project) {
    return project.getFolder(Constants.SOURCE_FOLDER);
  }

  public static IFolder getOutputFolder(IProject project) {
    return project.getFolder(Constants.OUT_FOLDER);
  }

  public static IFolder getBinFolder(IProject project) {
    return project.getFolder(Constants.BIN_FOLDER);
  }

  public static IFolder getPreviewFolder(IProject project) {
    return project.getFolder(Constants.PREVIEW_FOLDER);
  }

  public static IFolder getTempFolder(IProject project) {
    return project.getFolder(Constants.TEMP_FOLDER);
  }

  // PROJECT FILES

  public static IFile getProjectFile(IProject project) {
    return project.getFile(Constants.PROJECT_FILE);
  }

  public static IFile getPreferencesPath(IProject project) {
    return project.getFile(Constants.PREFERENCES_FILENAME);
  }

  public static IFile getUserPreferencesPath(IProject project) {
    return project.getFile(Constants.USER_PREFERENCES_FILENAME);
  }

  // PROJECT PROPERTIES

  public static String getProjectPackageBase(IProject project) {
    Properties properties = loadPreferences(project);
    return properties.getProperty(PROJECT_PACKAGE_BASE);
  }

  public static String getProjectVersion(IProject project) {
    Properties properties = loadPreferences(project);
    return properties.getProperty(PROJECT_VERSION);
  }

  public static String getProjectReleaseNumber(IProject project) {
    Properties properties = loadPreferences(project);
    String releaseNumber = properties.getProperty(PROJECT_RELEASE_NUMBER);
    if (releaseNumber == null)
      releaseNumber = incrementProjectReleaseNumber(project);
    return releaseNumber;
  }

  public static String getProjectUUID(IProject project) {
    Properties properties = loadPreferences(project);
    String uuid = properties.getProperty(PROJECT_UUID);
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
      setProjectUUID(project, uuid);
    }
    return uuid;
  }

  public static String getProjectKeyStorePath(IProject project) {
    Properties properties = loadUserPreferences(project);
    return properties.getProperty(PROJECT_KEYSTORE_PATH);
  }

  public static String getProjectKeyStorePassword(IProject project) {
    Properties properties = loadUserPreferences(project);
    return properties.getProperty(PROJECT_KEYSTORE_PASSWORD);
  }

  public static void setPackageBase(IProject project, String newValue) {
    Properties properties = loadPreferences(project);
    properties.setProperty(PROJECT_PACKAGE_BASE, newValue);
    savePreferences(project);
  }

  public static void setProjectVersion(IProject project, String newValue) {
    Properties properties = loadPreferences(project);
    properties.setProperty(PROJECT_VERSION, newValue);
    savePreferences(project);
  }

  public static void setProjectUUID(IProject project, String newValue) {
    Properties properties = loadPreferences(project);
    properties.setProperty(PROJECT_UUID, newValue);
    savePreferences(project);
  }

  public static String incrementProjectReleaseNumber(IProject project) {
    Properties properties = loadPreferences(project);
    String releaseNumber = "0";
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
      releaseNumber = formatter.format(new Date());
    } catch (NumberFormatException e) {
    }
    properties.setProperty(PROJECT_RELEASE_NUMBER, releaseNumber);
    savePreferences(project);
    return releaseNumber;
  }

  public static void setProjectKeyStorePath(IProject project, String keyStorePath) {
    Properties properties = loadUserPreferences(project);
    properties.setProperty(PROJECT_KEYSTORE_PATH, keyStorePath);
    saveUserPreferences(project);
  }

  public static void setProjectKeyStorePassword(IProject project, String keyStorePassword) {
    Properties properties = loadUserPreferences(project);
    properties.setProperty(PROJECT_KEYSTORE_PASSWORD, keyStorePassword);
    saveUserPreferences(project);
  }

  private static Properties loadProperties(IProject project, IFile propertiesFile, boolean isUserProperties) throws Exception {
    Properties properties = new Properties();
    InputStream is = null;
    try {
      if (!propertiesFile.exists()) {
        if (isUserProperties) {
          UserPropertiesFileGenerator.Parameters userPropertiesParams = new UserPropertiesFileGenerator.Parameters();
          userPropertiesParams.path = new File(MonetPreferences.getHomePath(), Constants.PRIVATE_KEY_TEMPLATE_NAME).getAbsolutePath();
          UserPropertiesFileGenerator upfg = new UserPropertiesFileGenerator(project, null, userPropertiesParams);
          upfg.create();
        } else {
          ProjectPropertiesFileGenerator.Parameters parameters = new ProjectPropertiesFileGenerator.Parameters();
          parameters.packageName = "nopackage";
          parameters.release = "1";
          ProjectPropertiesFileGenerator pfg = new ProjectPropertiesFileGenerator(project, null, parameters);
          pfg.create();
        }
      }
      if (!propertiesFile.isSynchronized(IFile.DEPTH_ZERO))
        propertiesFile.refreshLocal(IFile.DEPTH_ZERO, null);
      is = propertiesFile.getContents();
      properties.loadFromXML(is);
      return properties;
    } finally {
      StreamHelper.close(is);
    }
  }

  private static Properties loadPreferences(IProject project) {
    synchronized (preferencesMap) {
      Properties preferences = preferencesMap.get(project.getName());
      if (preferences == null) {
        try {
          IFile preferencesFile = ProjectHelper.getPreferencesPath(project);
          preferences = ProjectHelper.loadProperties(project, preferencesFile, false);
          preferencesMap.put(project.getName(), preferences);
        } catch (Exception e) {
          MonetLog.print(e);
        }
      }
      return preferences;
    }
  }

  private static Properties loadUserPreferences(IProject project) {
    synchronized (userPreferencesMap) {
      if (userPreferencesMap.containsKey(project.getName()))
        return userPreferencesMap.get(project.getName());
      else {
        try {
          IFile userPreferencesFile = ProjectHelper.getUserPreferencesPath(project);
          Properties userPreferences = ProjectHelper.loadProperties(project, userPreferencesFile, true);
          userPreferencesMap.put(project.getName(), userPreferences);
          return userPreferences;
        } catch (Exception e) {
          MonetLog.print(e);
        }
      }
      return null;
    }
  }

  public static void savePreferences(IProject project) {
    Properties preferences = (Properties) loadPreferences(project);

    FileOutputStream outputStream = null;
    try {
      IFile preferencesFile = ProjectHelper.getPreferencesPath(project);
      outputStream = new FileOutputStream(preferencesFile.getLocation().toFile());
      preferences.storeToXML(outputStream, null);
      preferencesFile.refreshLocal(IFile.DEPTH_ZERO, null);
    } catch (Exception e) {
      MonetLog.print(e);
    } finally {
      StreamHelper.close(outputStream);
    }
  }

  public static void saveUserPreferences(IProject project) {
    Properties userPreferences = (Properties) loadUserPreferences(project);

    FileOutputStream outputStream = null;
    try {
      IFile userPreferencesFile = ProjectHelper.getUserPreferencesPath(project);
      outputStream = new FileOutputStream(userPreferencesFile.getLocation().toFile());
      userPreferences.storeToXML(outputStream, null);
      userPreferencesFile.refreshLocal(IFile.DEPTH_ZERO, null);
    } catch (Exception e) {
      MonetLog.print(e);
    } finally {
      StreamHelper.close(outputStream);
    }
  }

  public static void invalidatePreferences(IProject project) {
    synchronized (preferencesMap) {
      preferencesMap.remove(project.getName());
    }
  }

  public static void invalidateUserPreferences(IProject project) {
    synchronized (userPreferencesMap) {
      userPreferencesMap.remove(project.getName());
    }
  }

}
