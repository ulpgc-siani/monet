package org.monet.editor.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileHelper {

  public static String readFile(File file) throws IOException {
    return new String(Files.readAllBytes(file.toPath()));
  }

  public static void writeFile(File file, String content) throws IOException {
    Files.write(file.toPath(), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
  }

}
