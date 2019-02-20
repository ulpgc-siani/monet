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

import org.jdom.input.SAXBuilder;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.Strings;
import org.w3c.dom.Document;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class LibraryXSL {

	public static Reader compile(String sXslFilename, String sSource) {
		String sInputFilename = Kernel.getInstance().getConfiguration().getTempDir() + Strings.BAR45 + Thread.currentThread().getId() + "_input";
		Reader oReader;

		try {
			AgentFilesystem.writeFile(sInputFilename, sSource);
			oReader = LibraryXSL.compileFromFile(sXslFilename, sInputFilename, new HashMap<String, String>());
		}
		finally {
			AgentFilesystem.removeFile(sInputFilename);
		}

		return oReader;
	}

	public static Reader compile(String sXslFilename, String sSource, HashMap<String, String> hmParameters) {
		String sInputFilename = Kernel.getInstance().getConfiguration().getTempDir() + Strings.BAR45 + Thread.currentThread().getId() + "_input";
		Reader oReader;

		AgentFilesystem.writeFile(sInputFilename, sSource);
		oReader = LibraryXSL.compileFromFile(sXslFilename, sInputFilename, hmParameters);

		AgentFilesystem.removeFile(sInputFilename);

		return oReader;
	}

	public static Reader compile(Reader xslFileReader, String sXslFilename, String sSource) {
		String sInputFilename = Kernel.getInstance().getConfiguration().getTempDir() + Strings.BAR45 + Thread.currentThread().getId() + "_input";
		Reader oReader;

		AgentFilesystem.writeFile(sInputFilename, sSource);
		String sOutputFilename = Kernel.getInstance().getConfiguration().getTempDir() + Strings.BAR45 + Thread.currentThread().getId() + "_output";
		sOutputFilename = compileFromFileToPath(xslFileReader, sXslFilename, sInputFilename, sOutputFilename, new HashMap<String, String>());
		oReader = AgentFilesystem.getReader(sOutputFilename);

		AgentFilesystem.removeFile(sInputFilename);

		return oReader;
	}

	public static Reader compileFromFile(String sXslFilename, String sSourceFilename) {
		return compileFromFile(sXslFilename, sSourceFilename, new HashMap<String, String>());
	}

	public static Reader compileFromFile(String sXslFilename, String sSourceFilename, HashMap<String, String> hmParameters) {
		Reader oReader = null;
		String sOutputFilename = null;
		try {
			sOutputFilename = Kernel.getInstance().getConfiguration().getTempDir() + Strings.BAR45 + Thread.currentThread().getId() + "_output";
			sOutputFilename = compileFromFileToPath(new InputStreamReader(new FileInputStream(sXslFilename), "UTF-8"), sXslFilename, sSourceFilename, sOutputFilename, hmParameters);
			oReader = AgentFilesystem.getReader(sOutputFilename);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
		finally {
			if (sOutputFilename != null)
				AgentFilesystem.removeFile(sOutputFilename);
		}
		return oReader;
	}

	public static String compileFromFileToPath(Reader oXslFileStream, String sXslFilename, String sSourceFilename, String sOutputFilename) {
		return compileFromFileToPath(oXslFileStream, sXslFilename, sSourceFilename, sOutputFilename, new HashMap<String, String>());
	}

	public static String compileFromFileToPath(Reader oXslFileStream, String sXslFilename, String sSourceFilename, String sOutputFilename, HashMap<String, String> hmParameters) {
		TransformerFactory oFactory = TransformerFactory.newInstance();
		SAXBuilder oBuilder = new SAXBuilder();
		org.jdom.Document oDocument;
		Document oDOMDocument;
		Source oXmlSource;
		StreamSource oXsltSource;
		Transformer oTransformer;
		Writer oWriter;

		try {
			oDocument = oBuilder.build(sSourceFilename);
			oDOMDocument = new org.jdom.output.DOMOutputter().output(oDocument);
			oXmlSource = new DOMSource(oDOMDocument);

			oXsltSource = new StreamSource(oXslFileStream);
			oXsltSource.setSystemId(new File(sXslFilename));
			oTransformer = oFactory.newTransformer(oXsltSource);

			Iterator<String> oIter = hmParameters.keySet().iterator();

			while (oIter.hasNext()) {
				String sKey = oIter.next();
				String sValue = hmParameters.get(sKey);
				oTransformer.setParameter(sKey, sValue);
			}

			oWriter = AgentFilesystem.getWriter(sOutputFilename);
			oTransformer.transform(oXmlSource, new StreamResult(oWriter));
			oWriter.close();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			return null;
		}

		return sOutputFilename;
	}

	public static String compileFromFileToString(Reader xslReader, String sXslFilename, String sSourceFilename) {
		String content = "";
		try {
			String sOutputFilename = Kernel.getInstance().getConfiguration().getTempDir() + Strings.BAR45 + Thread.currentThread().getId() + "_output";
			sOutputFilename = compileFromFileToPath(xslReader, sXslFilename, sSourceFilename, sOutputFilename, new HashMap<String, String>());
			content = AgentFilesystem.readFile(sOutputFilename);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
		return content;
	}

	public static String compileFromFileToString(String sXslFilename, String sSourceFilename) {
		String content = "";
		try {
			String sOutputFilename = Kernel.getInstance().getConfiguration().getTempDir() + Strings.BAR45 + Thread.currentThread().getId() + "_output";
			sOutputFilename = compileFromFileToPath(new InputStreamReader(new FileInputStream(sXslFilename), "UTF-8"), sXslFilename, sSourceFilename, sOutputFilename, new HashMap<String, String>());
			content = AgentFilesystem.readFile(sOutputFilename);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
		return content;
	}

}