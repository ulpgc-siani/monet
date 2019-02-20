package org.monet.editor.core.framework;

import java.io.File;
import java.io.FilenameFilter;

public class FileSystemHelper {
	
	public final static FilenameFilter USER_FILES_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return !name.startsWith(".");
		}
	};


}
