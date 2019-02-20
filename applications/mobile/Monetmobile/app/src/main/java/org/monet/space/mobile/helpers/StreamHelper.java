package org.monet.space.mobile.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;

public class StreamHelper {

  public static void close(InputStream stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (Exception e) {
      }
    }
  }

  public static void close(OutputStream stream) {
    if (stream != null) {
      try {
        stream.flush();
        stream.close();
      } catch (Exception e) {
      }
    }
  }

  public static void close(Writer out) {
    if (out != null) {
      try {
        out.flush();
        out.close();
      } catch (Exception e) {
      }
    }
  }

  public static void copyStream(InputStream inputStream, OutputStream outputStream) {
    byte[] buffer = new byte[4096];
    int read = 0;
    try {
      while ((read = inputStream.read(buffer, 0, buffer.length)) > 0) {
        outputStream.write(buffer, 0, read);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public static final String toString(InputStream input) throws IOException {
    if (input == null)
      return null;

    InputStreamReader reader = new InputStreamReader(input, "UTF-8");
    StringBuffer content = new StringBuffer();
    BufferedReader bufferedReader;
    String line;

    bufferedReader = new BufferedReader(reader);
    while ((line = bufferedReader.readLine()) != null) {
      content.append(line + "\n");
    }

    return content.toString();
  }

}
