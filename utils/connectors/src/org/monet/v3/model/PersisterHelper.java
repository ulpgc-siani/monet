package org.monet.v3.model;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersisterHelper {
	private static Persister persister = null;
	private static RegistryMatcher matcher = null;

	private static RegistryMatcher getPersisterMatcher() {

		if (matcher != null)
			return matcher;

		matcher = new RegistryMatcher();
		matcher.bind(Date.class, new Transform<Date>() {
			private SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S Z");
			private SimpleDateFormat mobileFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

			@Override
			public Date read(String value) throws Exception {
				try {
					return defaultFormat.parse(value);
				} catch (ParseException exception) {
					return mobileFormat.parse(value);
				}
			}

			@Override
			public String write(Date value) throws Exception {
				return defaultFormat.format(value);
			}
		});

		return matcher;
	}

	private static Persister getPersister() {
		if (persister == null)
			persister = new Persister(getPersisterMatcher());
		return persister;
	}

	public static void save(File destination, Object object) throws Exception {
		FileOutputStream outputStream = null;
		try {
			if (!destination.exists()) {
				destination.getParentFile().mkdirs();
				destination.createNewFile();
			}

			outputStream = new FileOutputStream(destination);
			getPersister().write(object, outputStream);
		} finally {
			StreamHelper.close(outputStream);
		}
	}

	public static void save(Writer out, Object object) throws Exception {
		try {
			getPersister().write(object, out);
		} finally {
			StreamHelper.close(out);
		}
	}

	public static <T> T load(File source, Class<T> objectClazz) throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(source);
			return getPersister().read(objectClazz, inputStream);
		} finally {
			StreamHelper.close(inputStream);
		}
	}

	public static <T> T load(String content, Class<T> objectClazz) throws Exception {
		return getPersister().read(objectClazz, content);
	}

	public static <T> T load(InputStream stream, Class<T> objectClazz) throws Exception {
		return getPersister().read(objectClazz, stream);
	}

	public static String save(Object object) throws Exception {
		StringWriter writer = new StringWriter();
		try {
			save(writer, object);
			return writer.toString();
		} finally {
			writer.close();
		}
	}

}
