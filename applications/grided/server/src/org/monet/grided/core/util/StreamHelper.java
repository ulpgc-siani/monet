package org.monet.grided.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;


public class StreamHelper {

  public static void close(InputStream stream) {
    if(stream != null) {
      try {
        stream.close();
      } catch(Exception e) {}
    }
  }
  
  public static void close(OutputStream stream) {
    if(stream != null) {
      try {
        stream.flush();
        stream.close();
      } catch(Exception e) {}
    }
  }

  public static void close(Reader reader) {
    if(reader != null) {
      try {
        reader.close();
      } catch(Exception e) {}
    }
  }
  
  public static final void copyData(InputStream input, OutputStream output) throws IOException {
    int len;
    byte[] buff = new byte[16384];
    while((len = input.read(buff)) > 0)
      output.write(buff, 0, len);
  }
  
//  public static final String toString(InputStream input) throws UnsupportedEncodingException {
//    if(input == null) 
//      return null;
//    InputStreamReader reader = new InputStreamReader(input, "UTF-8");
//    return AgentFilesystem.getReaderContent(reader);
//  }
//  
//  public static byte[] calculateHash(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
//    MessageDigest digest = MessageDigest.getInstance("SHA1");
//    byte[] buffer = new byte[4096];
//    int readed = 0;
//    while((readed = inputStream.read(buffer, 0, buffer.length)) > 0) {
//      digest.update(buffer, 0, readed);
//    }
//    return digest.digest();
//  }
  
}
