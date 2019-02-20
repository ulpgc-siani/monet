package org.monet.api.space.backservice.impl.model;

import org.simpleframework.xml.Root;

import java.util.regex.Pattern;

@Root(name="string")
public abstract class Uri {
	protected @org.simpleframework.xml.Attribute(required=true)
	String header;
	protected @org.simpleframework.xml.Attribute(required=true)
	String partner;
	protected @org.simpleframework.xml.Attribute(required=true)
	String id;
	protected @org.simpleframework.xml.Attribute(required=false)
	String row = null;

	protected static final String MAILBOX_URI_HEADER = "mmu://";
	protected static final String SERVICE_URI_HEADER = "msu://";
	protected static final String FEEDER_URI_HEADER  = "mfu://";

	protected static final String URI_REGULAR_EXPRESSION = "m.u://";
	private static final Pattern URI_PATTERN = Pattern.compile(URI_REGULAR_EXPRESSION);

	public static Uri build(String uri) {
        Uri instance = null;

        if (uri == null)
            return instance;

        if (uri.contains(MAILBOX_URI_HEADER)) instance = MailBoxUri.build(uri);
        else if (uri.contains(SERVICE_URI_HEADER)) instance = ServiceUri.build(uri);
        else if (uri.contains(FEEDER_URI_HEADER)) instance = FeederUri.build(uri);

        return instance;
    }

    public static boolean isMonetUri(String uri) {
        return uri != null && URI_PATTERN.matcher(uri).find();
    }

	public String getPartner() {
		return partner;
	}

	public String getId() {
		return id;
	}

	public boolean isToService() {
		return this instanceof ServiceUri;
	}

	public boolean isToFeeder() {
		return this instanceof FeederUri;
	}

	public boolean isToMailbox() {
		return this instanceof MailBoxUri;
	}

	protected Uri(@org.simpleframework.xml.Attribute String header, @org.simpleframework.xml.Attribute String partner, @org.simpleframework.xml.Attribute String id) {
		this.header = header;
		this.partner = partner;
		this.id = id;
	}

	protected Uri(@org.simpleframework.xml.Attribute String header) {
		this(header, "", "");
	}

	protected static <T extends Uri> T build(String uri, Class<T> clazz) {
		T instance = null;

		try {
			if (uri == null)
				return instance;

			instance = clazz.newInstance();
			instance.row = uri;

			if (!isMonetUri(uri))
				return instance;

			uri = uri.replaceAll(URI_REGULAR_EXPRESSION, "");

			String[] uriArray = uri.split(":");
			if (uriArray.length < 2)
				return instance;

			instance.partner = uriArray[0];
			instance.id = uriArray[1];

			return instance;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	protected String toString(String header) {
		if (row != null && !isMonetUri(row))
			return row;
		return String.format(header + "%s:%s", partner, id);
	}


}
