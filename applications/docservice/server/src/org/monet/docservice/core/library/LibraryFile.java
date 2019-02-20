package org.monet.docservice.core.library;

import java.io.File;
import java.io.InputStream;

public interface LibraryFile {

	String getBasename(String sFilename);

	String getDirname(String sFilename);

	String getFilename(String sFilename);

	String getFilenameWithoutExtension(String sFilename);

	String getExtension(String sFilename);

	String getContentType(File file);

	String getContentType(InputStream stream);

}