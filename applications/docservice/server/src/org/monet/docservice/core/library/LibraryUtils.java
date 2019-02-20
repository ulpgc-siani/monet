package org.monet.docservice.core.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface LibraryUtils {

	public void copyStream(InputStream input, OutputStream output) throws IOException;
	public byte[] copyStreamToByteArray(InputStream input) throws IOException;
}
