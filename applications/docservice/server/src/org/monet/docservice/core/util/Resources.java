package org.monet.docservice.core.util;

import java.io.InputStream;

public class Resources {

  public static InputStream getAsStream(String name) {
    return Resources.class.getResourceAsStream(name);
  }

	public static String getFullPath(String name) {
		String directory = Resources.class.getResource(name).getPath();

		if (directory.substring(directory.length()-1, directory.length()).equals("/"))
			directory = directory.substring(0, directory.length()-1);

		return directory;
	}
}
