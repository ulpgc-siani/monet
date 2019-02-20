package org.monet.space.setup.library;

import org.apache.commons.fileupload.FileItem;

import java.io.File;

public class LibraryFileUploader {

	public File uploadFile(FileItem item, String directoryName, String fileName) throws Exception {
		File file;
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		file = new File(directory.getAbsolutePath(), fileName);
		item.write(file);
		return file;
	}
}
