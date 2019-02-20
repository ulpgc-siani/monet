package org.monet.space.mobile.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.simpleframework.xml.core.Persister;

public class PersisterHelper {

  private static Persister persister = new Persister();

  public static void save(File destination, Object object) throws Exception {
    FileOutputStream outputStream = null;
    try {
      if (!destination.exists()) {
        destination.getParentFile().mkdirs();
        destination.createNewFile();
      }

      outputStream = new FileOutputStream(destination);
      persister.write(object, outputStream);
    } finally {
      StreamHelper.close(outputStream);
    }
  }

  public static void save(Writer out, Object object) throws Exception {
    try {
      persister.write(object, out);
    } finally {
      StreamHelper.close(out);
    }
  }

  public static <T> T load(File source, Class<T> objectClazz) throws Exception {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(source);
      return persister.read(objectClazz, inputStream);
    } finally {
      StreamHelper.close(inputStream);
    }
  }

  public static <T> T load(String content, Class<T> objectClazz) throws Exception {
    return persister.read(objectClazz, content);
  }

  public static String save(Object object) throws Exception {
    StringWriter writer = new StringWriter();
    try {
      save(writer, object);
      return writer.toString();
    } finally {
      writer.close();
    }
  }

}
