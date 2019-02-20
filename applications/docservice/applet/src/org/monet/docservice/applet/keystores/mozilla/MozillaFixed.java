package org.monet.docservice.applet.keystores.mozilla;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import es.uji.security.util.OS;
import org.apache.log4j.Logger;

public class MozillaFixed extends es.uji.security.keystore.mozilla.Mozilla {

  private String dllPath;
  private Logger log = Logger.getLogger(MozillaFixed.class);

  public MozillaFixed(String dllPath) {
    super();
    this.dllPath = dllPath;
  }
  
  private String copy(File src, File dst) {
    byte[] buffer = new byte[4096];
    try {
      FileInputStream input = new FileInputStream(src);
      FileOutputStream output = new FileOutputStream(dst);
      int size;
      while((size = input.read(buffer)) > -1) {
        output.write(buffer, 0, size);
      }
      output.close();
      input.close();
      return dst.getAbsolutePath();
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public String getPkcs11FilePath() {
      log.debug("GetPkcs11FilePath on mozilla");

      //if !(OS.isWindowsUpperEqualToNT()) {
//        log.debug("GetPkcs11FilePath on mozilla old windows");
//        return super.getPkcs11FilePath();
//      }

      log.debug("GetPkcs11FilePath on mozilla winnt");
      String sAbsoluteApplicationPath = getAbsoluteApplicationPath();
      log.debug("Mozilla application path: " + sAbsoluteApplicationPath);

      if(sAbsoluteApplicationPath.indexOf('(') > -1 || sAbsoluteApplicationPath.indexOf(')') > -1) {
        File file = new File(sAbsoluteApplicationPath + "\\softokn3.dll");
        return copy(file, new File(this.dllPath + "softokn3.dll"));
      }

      return sAbsoluteApplicationPath + "\\softokn3.dll";
  }
  
}
