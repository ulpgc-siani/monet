/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.agents;

import org.apache.commons.io.filefilter.AgeFileFilter;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.FilesystemException;
import org.monet.space.kernel.utils.Resources;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AgentFilesystem {

	protected AgentFilesystem() {
	}

	public static String[] listDir(String dirname) {
		String result = dirname.contains("jar!") ? dirname.substring(dirname.indexOf("jar!") + "jar!".length()) : dirname;
		try {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return !name.startsWith(".");
				}
			};

			if (isJar(result)) return Objects.requireNonNull(jarPathOf(result)).toFile().list(filter);
			File file = new File(result);
			return file.exists() ? file.list(filter) : new File(AgentFilesystem.class.getResource(result).toURI()).list(filter);
		} catch (IOException | URISyntaxException e) {
			AgentLogger.getInstance().error(e);
			return null;
		}
	}

	public static String[] listFiles(String dirname) {
		List<String> alResult = new ArrayList<>();
		FilenameFilter oFilter;
		Integer iPos;

		oFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			}
		};

		File[] aFiles = new File(dirname).listFiles(oFilter);
		for (iPos = 0; iPos < aFiles.length; iPos++) {
			if (aFiles[iPos].isDirectory()) continue;
			alResult.add(aFiles[iPos].getName());
		}

		return alResult.toArray(new String[alResult.size()]);
	}

	public static Boolean createDir(String dirname) {
		return new File(dirname).mkdir();
	}

	public static Boolean renameDir(String source, String destination) {
		return new File(source).renameTo(new File(destination));
	}

	public static Boolean removeDir(String dirname) {
		return removeDir(new File(dirname));
	}

	public static Boolean removeDir(File file) {
		if (!file.exists())
			return true;

		removeDirContent(file);

		return (file.delete());
	}

	public static void removeDirContent(File directory) {
		for (File file : directory.listFiles()) {
			if (file.isDirectory())
				removeDir(file.getAbsolutePath());
			else
				file.delete();
		}
	}

	public static Boolean copyDir(String source, String destination) {
		return copyDir(new File(source), new File(destination));
	}

	public static Boolean copyDir(File source, File destination) {
		try {
			if (source.exists()) {
				if (source.isDirectory()) {
					if (!destination.exists()) {
						destination.mkdir();
					}

					for (String child : source.list())
						copyDir(new File(source, child), new File(destination, child));
				} else {

					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(destination);

					// Copy the bits from instream to outstream
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
				}
				return true;
			}
		} catch (IOException oException) {
			throw new FilesystemException(oException.getMessage(), source.getName(), oException);
		}

		return false;
	}

	public static Boolean moveDir(File source, File destination) {
		if (!destination.exists())
			return source.renameTo(destination);
		boolean result = true;
		for (String child : source.list()) {
			File sourceChild = new File(source, child);
			File destinationChild = new File(destination, child);
			if (sourceChild.isDirectory()) {
				if (!moveDir(sourceChild, destinationChild)) result = false;
			} else {
				if (destinationChild.exists()) destinationChild.delete();
				if (!sourceChild.renameTo(destinationChild)) result = false;
			}
		}
		return result;
	}

	public static Boolean forceDir(String dirname) {
		return new File(dirname).mkdirs();
	}

	public static Boolean existFile(String sFilename) {
		return new File(sFilename).exists();
	}

	public static Boolean createFile(String filename) {

		try {
			new File(filename).createNewFile();
		} catch (IOException ex) {
			AgentLogger.getInstance().error(ex);
			return false;
		}

		return true;
	}

	public static Boolean copyFile(String source, String destination) {
		return copyFile(new File(source), new File(destination));
	}

	public static Boolean copyFile(File source, File destination) {

		AgentFilesystem.forceDir(destination.getParentFile().getAbsolutePath());

		try {
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(destination);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (IOException ex) {
			AgentLogger.getInstance().error(ex);
			return false;
		}

		return true;
	}


	public static Boolean renameFile(String source, String destination) {
		return new File(source).renameTo(new File(destination));
	}

	public static Boolean removeFile(String sFilename) {
		return new File(sFilename).delete();
	}

	public static Reader getReader(String filename) {
		try {
			return new InputStreamReader(new FileInputStream(filename), "UTF-8");
		} catch (IOException oException) {
			throw new FilesystemException("Could not read file", filename, oException);
		}
	}

	public static Reader getReader(InputStream stream) {
		try {
			return new InputStreamReader(stream, "UTF-8");
		} catch (IOException oException) {
			throw new FilesystemException("Could not create reader", "", oException);
		}
	}

	public static InputStream getInputStream(String filename) {
		try {
			return new FileInputStream(filename);
		} catch (IOException oException) {
			throw new FilesystemException("Could not read file", filename, oException);
		}
	}

	public static byte[] getBytesFromFile(String filename) {
		File file = new File(filename);
		InputStream stream = null;
		long length;
		byte[] bytes;
		int offset, numRead;

		try {
			stream = new FileInputStream(file);

			length = file.length();
			if (length > Integer.MAX_VALUE) {
				throw new FilesystemException("File is too large", filename);
			}

			bytes = new byte[(int) length];

			offset = 0;
			numRead = 0;
			while (offset < bytes.length && (numRead = stream.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				throw new FilesystemException("Could not completely read file", filename);
			}

		} catch (IOException oException) {
			throw new FilesystemException("Could not get bytes from file", filename, oException);
		} finally {
			StreamHelper.close(stream);
		}

		return bytes;
	}

	public static String readFile(String filename, String mode) {
		char[] content = null;

		try {
			File file = new File(filename);
			InputStreamReader input = new InputStreamReader(new FileInputStream(file), "UTF-8");
			input.read(content);
			input.close();
		} catch (IOException oException) {
			throw new FilesystemException("Could not read file", filename, oException);
		}

		return new String(content);
	}

	public static String readStream(InputStream is) throws IOException {
		if (is == null) return "";
		Writer writer = new StringWriter();

		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1)
				writer.write(buffer, 0, n);
		} finally {
			is.close();
		}
		return writer.toString();
	}

	public static String readFile(String filename) {
		StringBuilder content = new StringBuilder();
		InputStreamReader streamReader;
		BufferedReader bufferedReader;
		String line;

		try {
			streamReader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
			bufferedReader = new BufferedReader(streamReader);
			while ((line = bufferedReader.readLine()) != null)
				content.append(line).append(Strings.CRLF);
			streamReader.close();
		} catch (IOException exception) {
			throw new FilesystemException("Could not read file", filename, exception);
		}

		return content.toString();
	}

	public static String getReaderContent(Reader reader) {
		StringBuilder sbContent = new StringBuilder();
		BufferedReader oBufferedReader;
		String sLine;

		try {
			oBufferedReader = new BufferedReader(reader);
			while ((sLine = oBufferedReader.readLine()) != null) {
				sbContent.append(sLine).append(Strings.CRLF);
			}
		} catch (IOException oException) {
			throw new FilesystemException("Could not get content from reader", null, oException);
		}

		return sbContent.toString();
	}

	public static Boolean writeFile(String filename, String content) {

		try {
			OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
			oWriter.write(content);
			oWriter.close();
		} catch (IOException oException) {
			throw new FilesystemException("Could not write file", filename, oException);
		}

		return true;
	}

	public static Boolean writeFile(File file, InputStream content) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			copyStream(content, outputStream);

		} catch (IOException oException) {
			throw new FilesystemException("Could not write file", file.getName(), oException);
		} finally {
			StreamHelper.close(outputStream);
		}

		return true;
	}

	public static Writer getWriter(String sFilename) {
		try {
			return new OutputStreamWriter(new FileOutputStream(sFilename), "UTF-8");
		} catch (IOException oException) {
			throw new FilesystemException("Could not read file", sFilename, oException);
		}
	}

	public static OutputStream getOutputStream(String filename) {
		try {
			return new FileOutputStream(filename);
		} catch (IOException oException) {
			throw new FilesystemException("Could not read file", filename, oException);
		}
	}

	public static Boolean appendToFile(String sFilename, String sContent) {

		try {
			FileWriter oFileWriter = new FileWriter(sFilename, true);
			oFileWriter.write(sContent);
			oFileWriter.close();
		} catch (IOException oException) {
			throw new FilesystemException("Could not write file", sFilename, oException);
		}

		return true;
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
		int readed = 0;
		while ((readed = input.read(buffer)) > 0)
			output.write(buffer, 0, readed);
	}

	private static boolean isJar(String dirname) throws URISyntaxException {
		URL resource = AgentFilesystem.class.getResource(dirname);
		return resource != null && resource.toURI().getScheme().equals("jar");
	}

	private static Path jarPathOf(String dirname) throws IOException, URISyntaxException {
		URI uri = AgentFilesystem.class.getResource(dirname).toURI();
		if (!uri.getScheme().equals("jar")) return null;
		FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
		return fileSystem.getPath(dirname);
	}

}
