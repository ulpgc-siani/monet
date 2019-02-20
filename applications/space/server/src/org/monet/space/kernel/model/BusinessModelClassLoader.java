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

package org.monet.space.kernel.model;

import edu.emory.mathcs.backport.java.util.Collections;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class BusinessModelClassLoader extends ClassLoader {
	protected String modelPathBase;
	protected String modelLibrariesPathBase;
	private AgentLogger agentException = AgentLogger.getInstance();

	private static HashMap<String, Class<?>> modelClasses = new HashMap<String, Class<?>>();
	private static HashMap<String, Class<?>> libraryClasses = new HashMap<String, Class<?>>();

	public BusinessModelClassLoader() {
		super();
		this.modelPathBase = Configuration.getInstance().getBusinessModelClassesDir();
		this.modelLibrariesPathBase = Configuration.getInstance().getBusinessModelLibrariesClassesDir();
	}

	byte[] getClassBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		boolean eof = false;
		while (!eof) {
			try {
				int i = bis.read();
				if (i == -1)
					eof = true;
				else baos.write(i);
			} catch (IOException e) {
				agentException.error(e);
				return null;
			}
		}
		return baos.toByteArray();
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		byte buf[];
		Class<?> resultClass;
		File file;
		InputStream is = null;

		try {

			if (BusinessModelClassLoader.modelClasses.containsKey(name))
				return BusinessModelClassLoader.modelClasses.get(name);

			if (BusinessModelClassLoader.libraryClasses.containsKey(name))
				return BusinessModelClassLoader.libraryClasses.get(name);

			file = new File(this.modelPathBase, name.replace(".", "/") + ".class");

			if (!file.exists())
				file = new File(this.modelLibrariesPathBase, name.replace(".", "/") + ".class");

			if (!file.exists())
				return this.getClass().getClassLoader().loadClass(name);

			is = new FileInputStream(file);
			buf = getClassBytes(is);
			resultClass = defineClass(name, buf, 0, buf.length, null);

			if (isLibraryClass(name))
				BusinessModelClassLoader.libraryClasses.put(name, resultClass);
			else
				BusinessModelClassLoader.modelClasses.put(name, resultClass);

			return resultClass;
		} catch (FileNotFoundException e) {
			agentException.errorInModel(e);
		} finally {
			StreamHelper.close(is);
		}
		return null;
	}

	@Override
	protected URL findResource(String name) {

		try {
			File file = new File(this.modelPathBase, name);

			if (!file.exists())
				file = new File(this.modelLibrariesPathBase, name);

			if (!file.exists())
				return super.findResource(name);

			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		Enumeration<URL> superResources = super.findResources(name);
		ArrayList<URL> result = new ArrayList<URL>();

		while (superResources.hasMoreElements())
			result.add(superResources.nextElement());

		File file = new File(this.modelPathBase, name);
		if (file.exists())
			result.add(file.toURI().toURL());

		file = new File(this.modelLibrariesPathBase, name);
		if (file.exists())
			result.add(file.toURI().toURL());

		return Collections.enumeration(result);
	}

	private boolean isLibraryClass(String name) {
		return new File(this.modelLibrariesPathBase, name.replace(".", "/") + ".class").exists();
	}

	public static void reset() {
		BusinessModelClassLoader.modelClasses.clear();
	}
}