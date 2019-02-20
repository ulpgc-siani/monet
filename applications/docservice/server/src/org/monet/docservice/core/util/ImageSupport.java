package org.monet.docservice.core.util;

public class ImageSupport {
  private static String[] extensions = {"jpg","jpeg","png","bmp","tiff"};
  public static final String DOT              = ".";
  
  public static boolean isImage(String filename){
    Integer iPos = filename.lastIndexOf(DOT);
    if (iPos == -1) return false;
    String extension = filename.substring(iPos+1);
    
    for (String ext : extensions) {
      if(extension.equalsIgnoreCase(ext))
        return true;
    }
    return false;
  }
}
