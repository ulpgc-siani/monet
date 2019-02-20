package org.monet.editor;

public class Constants {

  public static enum RESOURCE {
    data, help, images, templates, layouts
  };

  public static final String   CURRENT_VERSION           = "3.1";
  public static final String   DEFAULT_HOME_PATH         = ".monet.editor";

  public static final String   MML_CONTENT_TYPE          = "org.monet.editor.mml";
  public static final String   PROJECT_CONTENT_TYPE      = "org.monet.editor.mml.project";
  public static final String   DISTRIBUTION_CONTENT_TYPE = "org.monet.editor.mml.distribution";
  public static final String   LANGUAGE_CONTENT_TYPE     = "org.monet.editor.lang";

  public static final String   HOME                      = "HOME";
  public static final String   HOME_LABEL                = "&Home path:";
  public static final String   TEMPLATES                 = "templates";
  public static final String   DISTRIBUTIONS             = "distributions";
  public static final String   DEFINITIONS               = "definitions";
  public static final String   DOCUMENTS                 = "documents";
  public static final String   LAYOUTS                   = "layouts";
  public static final String   LOCALIZATION              = "localization";
  public static final String   PROJECTS                  = "projects";

  public static final String   SOURCE_FOLDER             = "src";
  public static final String   GEN_FOLDER                = "gen";
  public static final String   OUT_FOLDER                = "out";
  public static final String   RES_FOLDER                = "res";
  public static final String   RES_LAYOUTS_FOLDER        = "res/layouts";
  public static final String   BIN_FOLDER                = "bin";
  public static final String   LIBS_FOLDER               = "libs";
  public static final String   JAVA_OUT_FOLDER           = "bin/classes";
  public static final String   DIST_FOLDER               = "dist";
  public static final String   PREVIEW_FOLDER            = "preview";
  public static final String   TEMP_FOLDER               = "tmp";

  public static final String   PROJECT_FILE              = "project.mml";
  public static final String   MANIFEST_PACKAGE          = "manifest";
  public static final String   DISTRIBUTION_FILE         = "distribution.mml";
  public static final String   DISTRIBUTION_DISABLED_FILE= "disabled";
  public static final String   MML_FILE_EXTENSION        = "mml";

  public static final String   MSMANAGER_MAC             = "%s/launcher.mac.sh";
  public static final String   MSMANAGER_LINUX           = "%s/launcher.linux.sh";
  public static final String   MSMANAGER_WIN             = "%s/msmanager.exe";

  public static final String   MSMANAGER_PATH            = "/resources/msmanager";
  public static final String   TEMPLATES_PATH            = "/resources/templates";
  public static final String   PREVIEWER_EXECUTABLE_PATH = "/resources/previewer/org.monet.editor.preview.jar";
  public static final String   WIZARD_TEMPLATES_PATH     = "/resources/wizard_templates.zip";
  public static final String   PRIVATE_KEY_TEMPLATE_PATH = "/resources/ms.p12";

  public static final String   PRIVATE_KEY_TEMPLATE_NAME = "ms.p12";

  public static final String   RESOURCE_BUILDER_ID       = "org.monet.editor.builder.resourceBuilder";
  public static final String   MONET_NATURE_ID           = "org.monet.editor.Nature";

  public static final String   XTEXT_BUILDER_ID          = "org.eclipse.xtext.ui.shared.xtextBuilder";
  public static final String   XTEXT_NATURE_ID           = "org.eclipse.xtext.ui.shared.xtextNature";

  public static final String   ID_MODEL_EXPLORER_VIEW    = "MonetEditor.views.explorer";
  public static final String   ID_DEPLOY_VIEW            = "MonetEditor.views.deploy";

  public static final String   PREFERENCES_FILENAME      = ".preferences.properties";
  public static final String   USER_PREFERENCES_FILENAME = ".preferences.user.properties";

  public static final String[] PROJECT_FOLDERS           = new String[] { SOURCE_FOLDER, GEN_FOLDER, OUT_FOLDER, RES_FOLDER, BIN_FOLDER, DIST_FOLDER, LIBS_FOLDER };

  public static final String[] MANDATORY_MODEL_IMAGES    = new String[] { "model.logo.png", "model.splash.png" };
  public static final String[] MANDATORY_SPACE_IMAGES    = new String[] { "organization.logo.png", "organization.splash.png" };

}
