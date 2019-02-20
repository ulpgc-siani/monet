package org.monet.modelling.compiler.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ResourcesManager {
  
  public HashMap<String, File> getDeclarations(String path) {
    HashMap<String, File> hmFiles = new HashMap<String, File>();

    File rootFile = new File(path);

    if (!rootFile.exists()) { return null; }

    File[] files = rootFile.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        if (!file.isHidden() && !file.getName().equals("images"))
          hmFiles.putAll(getDeclarations(file.getAbsolutePath()));
      } else {
        if (!file.getName().startsWith("."))
          hmFiles.put(file.getName(), file);
      }
    }
    return hmFiles;
  }
  
    
  public Collection<File> getFiles(String path) {
    Collection<File> hmFiles = new ArrayList<File>();

    File rootFile = new File(path);

    if (!rootFile.exists()) { return null; }

    File[] files = rootFile.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        if (!file.isHidden())
          hmFiles.addAll(getFiles(file.getAbsolutePath()));
      } else {
        if (!file.getName().startsWith("."))
          hmFiles.add(file);
      }
    }
    return hmFiles;
  }

}
