package org.monet.editor.preview.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.monet.editor.preview.utils.PackageManifestHelper;

/**
 * Manifest Una definición describe una entidad en el modelo de negocio
 */

public class Manifest {
  private static Manifest instance;
  private Object title;
  private Object subTitle;

  private Manifest() {
    this.title = "";
    this.subTitle = "";
  }
  
  public static Manifest getInstance() {
    if (instance == null)
      instance = new Manifest();
    return instance;
  }
  
  public void initialize(String modelDir) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
    HashMap<String, String> packageManifest = PackageManifestHelper.getPackageManifest(new File(modelDir));
    
    if (packageManifest == null)
      return;
    
    DictionaryClassLoader classLoader = DictionaryClassLoader.getInstance();
    classLoader.setPathBase(modelDir + "/classes");
    
    String setupManifestClassname = packageManifest.get("location");
    Class<?> setupManifestClass = Class.forName(setupManifestClassname, true, classLoader);
    org.monet.metamodel.Manifest manifest = (org.monet.metamodel.Manifest)setupManifestClass.newInstance();
    
    this.title = manifest.getTitle();
    this.subTitle = manifest.getSubtitle();
  }

  public Object getTitle() {
    return title;
  }

  public Object getSubTitle() {
    return subTitle;
  }

}
