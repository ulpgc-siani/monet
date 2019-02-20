package org.monet.metacompiler.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileUtils {

  public static final void writeToFileAsUTF8(String filename, String contents) throws IOException {
    writeToFile(filename, contents, "UTF-8");
  }
  
  public static final void writeToFile(String filename, String contents, String enc) throws IOException {
    File file = new File(filename);
    if(!file.exists()) {
      file.getParentFile().mkdirs();
      file.createNewFile();
    }
    OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(file), enc);
    String bom = getBOM(enc);
    output.write(bom + contents);
    output.close();
  }

  private static final String getBOM(String enc) throws UnsupportedEncodingException {
    if ("UTF-8".equals(enc)) {
      byte[] bom = new byte[3];
      bom[0] = (byte) 0xEF;
      bom[1] = (byte) 0xBB;
      bom[2] = (byte) 0xBF;
      return new String(bom, enc);
    } else if ("UTF-16BE".equals(enc)) {
      byte[] bom = new byte[2];
      bom[0] = (byte) 0xFE;
      bom[1] = (byte) 0xFF;
      return new String(bom, enc);
    } else if ("UTF-16LE".equals(enc)) {
      byte[] bom = new byte[2];
      bom[0] = (byte) 0xFF;
      bom[1] = (byte) 0xFE;
      return new String(bom, enc);
    } else if ("UTF-32BE".equals(enc)) {
      byte[] bom = new byte[4];
      bom[0] = (byte) 0x00;
      bom[1] = (byte) 0x00;
      bom[2] = (byte) 0xFE;
      bom[3] = (byte) 0xFF;
      return new String(bom, enc);
    } else if ("UTF-32LE".equals(enc)) {
      byte[] bom = new byte[4];
      bom[0] = (byte) 0x00;
      bom[1] = (byte) 0x00;
      bom[2] = (byte) 0xFF;
      bom[3] = (byte) 0xFE;
      return new String(bom, enc);
    } else {
      return null;
    }
  }
}
