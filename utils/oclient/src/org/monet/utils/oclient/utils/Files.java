package org.monet.utils.oclient.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class Files {
  public static String readFile(String path) throws IOException {
    byte[] encoded = java.nio.file.Files.readAllBytes(Paths.get(path));
    return new String(encoded, Charset.defaultCharset());
  }

}
