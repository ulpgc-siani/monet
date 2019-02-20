package org.monet.editor.preview.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PackageManifestHelper {

  public static HashMap<String, String> getPackageManifest(File modelDirectory) throws IOException {
    HashMap<String, String> map = new HashMap<String, String>();
    
    File manifestFile = new File(modelDirectory, "MANIFEST");
    if(!manifestFile.exists())
      return null;
    
    String content = FilesystemHelper.readFile(manifestFile.getAbsolutePath());
    for(String line : content.split("\\n")) {
      String[] entry = line.trim().split(":");
      if(entry.length == 2) {
        map.put(entry[0].trim().toLowerCase(), entry[1].trim());
      }
    }
    
    return map;
  }
  
}
