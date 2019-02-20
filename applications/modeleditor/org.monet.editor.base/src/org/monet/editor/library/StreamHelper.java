package org.monet.editor.library;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StreamHelper {

	public static final void copyData(InputStream input, OutputStream output) throws IOException {
		int len;
		byte[] buff = new byte[16384];
		while ((len = input.read(buff, 0, input.available() > buff.length ? buff.length : input.available())) > 0)
			output.write(buff, 0, len);
	}

	public static void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static void close(OutputStream stream) {
		if (stream != null) {
			try {
				stream.flush();
				stream.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			}
			catch (Exception e) {
			}
		}
	}
	
  public static String calculateHashToHexString(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
    return org.apache.commons.codec.binary.Hex.encodeHexString(calculateHash(inputStream));
  }

	public static byte[] calculateHash(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA1");
		byte[] buffer = new byte[4096];
		int readed = 0;
		while ((readed = inputStream.read(buffer, 0, buffer.length)) > 0) {
			digest.update(buffer, 0, readed);
		}
		return digest.digest();
	}

	public static final String toString(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input, "UTF-8");
		StringBuffer sbContent = new StringBuffer();
		BufferedReader oBufferedReader;
		String sLine;

		try {
			oBufferedReader = new BufferedReader(reader);
			while ((sLine = oBufferedReader.readLine()) != null) {
				sbContent.append(sLine + "\n");
			}
		}
		catch (IOException oException) {
			throw new IOException("Could not get content from reader", oException);
		}

		return sbContent.toString();
	}

	public static final byte[] toByteArray(InputStream input) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int next = input.read();
		while (next > -1) {
			bos.write(next);
			next = input.read();
		}
		bos.flush();
		return bos.toByteArray();
	}
}
