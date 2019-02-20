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

package org.monet.v2.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class LibraryZip {

	private static final Integer BUFFER_SIZE = 8192;

	public static Boolean compress(ArrayList<String> filesList, String destinationFilename) {
		BufferedInputStream originStream = null;
		FileOutputStream destinationStream;
		ZipOutputStream outputStream;
		Iterator<String> iterator;
		byte[] dataArray;

		try {
			destinationStream = new FileOutputStream(destinationFilename);
			outputStream = new ZipOutputStream(new BufferedOutputStream(destinationStream));

			dataArray = new byte[BUFFER_SIZE];

			iterator = filesList.iterator();
			while (iterator.hasNext()) {
				String filename = (String) iterator.next();

				FileInputStream fisInput = new FileInputStream(filename);
				originStream = new BufferedInputStream(fisInput, BUFFER_SIZE);

				ZipEntry oEntry = new ZipEntry(filename);
				outputStream.putNextEntry(oEntry);

				int count;
				while ((count = originStream.read(dataArray, 0, BUFFER_SIZE)) != -1) {
					outputStream.write(dataArray, 0, count);
				}

				originStream.close();
			}

			outputStream.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static Boolean decompress(String filename, String destination) {
		return decompress(new File(filename), destination);
	}

	public static Boolean decompress(ZipInputStream inputStream, String destination) {
		BufferedOutputStream destinationStream = null;
		FileInputStream inputFileStream = null;
		FileOutputStream outputStream = null;
		boolean isValidZip = false;

		try {
			inputStream = new ZipInputStream(inputFileStream);

			int count;
			byte dataArray[] = new byte[BUFFER_SIZE];

			ZipEntry entry;
			while ((entry = inputStream.getNextEntry()) != null) {
				isValidZip = true;
				String entryFileName = entry.getName();

				try {
					if (entry.isDirectory())
						new File(destination + File.separator + entryFileName).mkdirs();
					else {
						String destDN = LibraryFile.getDirname(destination + File.separator + entryFileName);
						String destFN = destination + File.separator + entryFileName;

						new File(destDN).mkdirs();
						File outputFile = new File(destFN);
						outputStream = new FileOutputStream(outputFile);
						destinationStream = new BufferedOutputStream(outputStream, BUFFER_SIZE);

						while ((count = inputStream.read(dataArray, 0, BUFFER_SIZE)) != -1)
							destinationStream.write(dataArray, 0, count);
					}
				} finally {
					StreamHelper.close(destinationStream);
					StreamHelper.close(outputStream);
				}

			}
		} catch (Exception e) {
			return isValidZip;
		} finally {
			StreamHelper.close(inputFileStream);
			StreamHelper.close(inputStream);
		}

		return isValidZip;
	}

	public static Boolean decompress(File file, String destination) {
		FileInputStream inputFileStream = null;
		ZipInputStream inputStream = null;
		boolean isValidZip = false;

		try {
			inputFileStream = new FileInputStream(file);
			inputStream = new ZipInputStream(inputFileStream);
			isValidZip = LibraryZip.decompress(inputStream, destination);
		} catch (Exception e) {
			return isValidZip;
		} finally {
			StreamHelper.close(inputFileStream);
			StreamHelper.close(inputStream);
		}

		return isValidZip;
	}

	public static void addZipEntry(String name, String extra, InputStream inputStream, ZipOutputStream outputStream) throws IOException, UnsupportedEncodingException {
		try {
			ZipEntry entry = new ZipEntry(name);
			if (extra != null)
				entry.setExtra(extra.getBytes("UTF-8"));
			outputStream.putNextEntry(entry);
			StreamHelper.copyData(inputStream, outputStream);
		} finally {
			StreamHelper.close(inputStream);
		}
	}

}