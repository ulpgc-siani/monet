package org.monet.docservice.core.library.impl;

import com.google.inject.Inject;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.monet.docservice.core.library.LibraryFile;
import org.monet.docservice.core.log.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

public class LibraryFileImpl implements LibraryFile {

	public static final String DOT = ".";
	public static final String BAR45 = "/";
	public static final String BAR135 = "\\";

	private Logger logger;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	public String getBasename(String sFilename) {
		logger.debug("getBasename(%s)", sFilename);

		Integer iPos = sFilename.lastIndexOf(DOT);
		if (iPos == -1)
			return null;
		return sFilename.substring(0, iPos);
	}

	public String getDirname(String sFilename) {
		logger.debug("getDirname(%s)", sFilename);

		sFilename = sFilename.replaceAll(BAR135 + BAR135, BAR45);
		Integer iPos = sFilename.lastIndexOf(BAR45);
		if (iPos == -1)
			return null;
		return sFilename.substring(0, iPos);
	}

	public String getFilename(String sFilename) {
		logger.debug("getFilename(%s)", sFilename);

		sFilename = sFilename.replaceAll(BAR135 + BAR135, BAR45);
		Integer iPos = sFilename.lastIndexOf(BAR45);
		if (iPos == -1)
			return sFilename;
		return sFilename.substring(iPos + 1);
	}

	public String getFilenameWithoutExtension(String sFilename) {
		logger.debug("getFilenameWithoutExtension(%s)", sFilename);

		sFilename = sFilename.replaceAll(BAR135 + BAR135, BAR45);
		Integer iPos = sFilename.lastIndexOf(BAR45);
		int iDotPos = sFilename.lastIndexOf(DOT);
		if (iPos == -1)
			return sFilename;
		if (iDotPos == -1)
			return sFilename.substring(iPos + 1);
		return sFilename.substring(iPos + 1, iDotPos);
	}

	public String getExtension(String sFilename) {
		logger.debug("getExtension(%s)", sFilename);

		Integer iPos = sFilename.lastIndexOf(DOT);
		if (iPos == -1)
			return null;
		return sFilename.substring(iPos + 1);
	}

	public String getContentType(File file) {
		try {
			return getContentType(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return "application/octet-stream";
		}
	}

	public String getContentType(InputStream inputStream) {
		String contentType = "application/octet-stream";
		ContentHandler contentHandler = new BodyContentHandler();
		Metadata metadata = new Metadata();

		try {
			Parser parser = new AutoDetectParser();
			parser.parse(inputStream, contentHandler, metadata);
			contentType = metadata.get(Metadata.CONTENT_TYPE);
		} catch (IOException e) {
		} catch (SAXException e) {
		} catch (TikaException e) {
		}

		return contentType;
	}

}