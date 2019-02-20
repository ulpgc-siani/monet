package org.monet.v3.model;

import java.io.*;

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

	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

	public static void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public static final void copyData(InputStream input, OutputStream output) throws IOException {
		int len;
		byte[] buff = new byte[16384];
		while ((len = input.read(buff)) > 0)
			output.write(buff, 0, len);
	}

}
