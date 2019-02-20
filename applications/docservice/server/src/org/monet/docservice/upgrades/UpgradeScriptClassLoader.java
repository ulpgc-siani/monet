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

package org.monet.docservice.upgrades;

import com.google.inject.Inject;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

public class UpgradeScriptClassLoader extends ClassLoader {
	private final String pathBase;
	private Configuration configuration;
	private Logger logger;

	private static HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();

	@Inject
	public UpgradeScriptClassLoader(Configuration configuration, Logger logger) {
		super();
		this.configuration = configuration;
		this.pathBase = configuration.getApplicationDir();
		this.logger = logger;
	}

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
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
				logger.error(e.getMessage());
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

			if (UpgradeScriptClassLoader.classes.containsKey(name))
				return UpgradeScriptClassLoader.classes.get(name);

			if (!name.contains("upgrades.v"))
				return this.getClass().getClassLoader().loadClass(name);

			file = new File(this.pathBase, name.replace(".", "/") + ".class");
			if (!file.exists())
				return this.getClass().getClassLoader().loadClass(name);

			is = new FileInputStream(file);
			buf = getClassBytes(is);
			resultClass = defineClass(name, buf, 0, buf.length, null);
			UpgradeScriptClassLoader.classes.put(name, resultClass);

			return resultClass;
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} finally {
			StreamHelper.close(is);
		}
		return null;
	}

	@Override
	protected URL findResource(String name) {

		try {
			File file = new File(this.pathBase, name);

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

		File file = new File(this.pathBase, name);
		if (file.exists())
			result.add(file.toURI().toURL());

		return Collections.enumeration(result);
	}

	public static void reset() {
		UpgradeScriptClassLoader.classes.clear();
	}
}