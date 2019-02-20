package org.monet.mocks.businessunit.utils;

import com.twmacinta.util.MD5;

import java.io.*;
import java.security.NoSuchAlgorithmException;

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

	public static final int copyData(InputStream input, OutputStream output) throws IOException {
		int size = 0;
		int len;
		byte[] buff = new byte[16384];
		while ((len = input.read(buff)) > 0) {
			output.write(buff, 0, len);
			size += len;
		}
		return size;
	}

	public static final String toString(InputStream input) throws IOException {
		if (input == null)
			return null;
		UnicodeBOMInputStream unicodeBOMInputStream = new UnicodeBOMInputStream(input);
		if (unicodeBOMInputStream.getBOM() != UnicodeBOMInputStream.BOM.NONE)
			unicodeBOMInputStream.skipBOM();
		InputStreamReader reader = new InputStreamReader(unicodeBOMInputStream, "UTF-8");
		return getReaderContent(reader);
	}

	public static String getReaderContent(Reader oReader) {
		StringBuffer sbContent = new StringBuffer();
		BufferedReader oBufferedReader;
		String sLine;

		try {
			oBufferedReader = new BufferedReader(oReader);
			while ((sLine = oBufferedReader.readLine()) != null) {
				sbContent.append(sLine + "\n");
			}
		} catch (IOException oException) {
			throw new RuntimeException("Could not get content from reader", oException);
		}

		return sbContent.toString();
	}

	public static String calculateHashToHexString(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
		MD5 md5Hasher = new MD5();
		md5Hasher.Init();

		byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = inputStream.read(buffer)) > 0) {
			md5Hasher.Update(buffer, 0, bytesRead);
		}

		md5Hasher.Final();
		return md5Hasher.asHex();
	}

}
