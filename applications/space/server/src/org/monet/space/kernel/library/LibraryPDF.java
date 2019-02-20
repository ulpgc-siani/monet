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

package org.monet.space.kernel.library;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.utils.StreamHelper;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LibraryPDF {

	public static ByteArrayOutputStream create(InputStream stream) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		FopFactory fopFactory = FopFactory.newInstance();
		TransformerFactory factory = TransformerFactory.newInstance();
		Fop fop;
		Transformer transformer;
		Source source;
		Result result;

		try {
			fop = fopFactory.newFop(MimeConstants.MIME_PDF, output);
			transformer = factory.newTransformer();
			source = new StreamSource(stream);
			result = new SAXResult(fop.getDefaultHandler());
			transformer.transform(source, result);
		} catch (Exception exception) {
			throw new DataException("create pdf", null, exception);
		}

		return output;
	}

	public static ByteArrayOutputStream createFromFile(String fopFilename) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		FopFactory fopFactory = FopFactory.newInstance();
		TransformerFactory factory = TransformerFactory.newInstance();
		Fop fop;
		Transformer transformer;
		Source source;
		Result result;

		try {
			fop = fopFactory.newFop(MimeConstants.MIME_PDF, output);
			transformer = factory.newTransformer();
			source = new StreamSource(fopFilename);
			result = new SAXResult(fop.getDefaultHandler());
			transformer.transform(source, result);
		} catch (Exception exception) {
			throw new DataException("create pdf", fopFilename, exception);
		}

		return output;
	}

	public static String extractXmlData(File sourcePdfFile) throws IOException {
		return extractXmlData(new FileInputStream(sourcePdfFile));
	}

	public static String extractXmlData(InputStream sourcePdfFileStream) throws IOException {
		PdfReader pdfReader = null;
		String xmlData = null;

		try {
			pdfReader = new PdfReader(sourcePdfFileStream);
			xmlData = pdfReader.getAcroFields().getField("xmlData");
		} finally {
			if (pdfReader != null)
				pdfReader.close();
			StreamHelper.close(sourcePdfFileStream);
		}

		return xmlData;
	}

}