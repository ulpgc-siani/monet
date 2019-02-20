package org.monet.bpi;

import org.apache.commons.lang.NotImplementedException;

public abstract class Post {

	public static Post createInfo() {
		throw new NotImplementedException();
	}

	public static Post createRequest() {
		throw new NotImplementedException();
	}

	public static Post createResponse() {
		throw new NotImplementedException();
	}

	public abstract String getId();

	public abstract void setTitle(String title);

	public abstract void setBody(String body);

	public abstract void setTitleLink(MonetLink link);

	public abstract void setBodyLink(MonetLink link);

}
