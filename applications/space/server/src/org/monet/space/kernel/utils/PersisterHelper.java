package org.monet.space.kernel.utils;

import org.monet.space.kernel.model.FeederUri;
import org.monet.space.kernel.model.MailBoxUri;
import org.monet.space.kernel.model.ServiceUri;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

        matcher.bind(ServiceUri.class, new Transform<ServiceUri>() {
            @Override
            public ServiceUri read(String uri) throws Exception {
                return ServiceUri.build(uri);
            }

            @Override
            public String write(ServiceUri monetUri) throws Exception {
                return monetUri.toString();
            }
        });

        matcher.bind(FeederUri.class, new Transform<FeederUri>() {
            @Override
            public FeederUri read(String uri) throws Exception {
                return FeederUri.build(uri);
            }

            @Override
            public String write(FeederUri monetUri) throws Exception {
                return monetUri.toString();
            }
        });

        matcher.bind(MailBoxUri.class, new Transform<MailBoxUri>() {
            @Override
            public MailBoxUri read(String uri) throws Exception {
                return MailBoxUri.build(uri);
            }

            @Override
            public String write(MailBoxUri monetUri) throws Exception {
                return monetUri.toString();
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

	public static void save(OutputStream out, Object object) throws Exception {
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
