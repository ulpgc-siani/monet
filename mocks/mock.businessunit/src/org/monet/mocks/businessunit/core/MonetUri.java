package org.monet.mocks.businessunit.core;

public class MonetUri {
    protected String header;
    protected String partner;
    protected String id;

    private static final String URI_REGULAR_EXPRESSION = "m.u://";

	public String getId() {
		return id;
	}

    public static MonetUri build(String uri) {
	    MonetUri instance = null;

	    if (uri == null)
		    return instance;

	    instance = new MonetUri();
	    uri = uri.replaceAll(URI_REGULAR_EXPRESSION, "");

	    String[] uriArray = uri.split(":");
	    if (uriArray.length < 2)
		    return instance;

	    instance.partner = uriArray[0];
	    instance.id = uriArray[1];

	    return instance;
    }

}
