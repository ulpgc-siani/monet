package org.monet.bpi;

import org.monet.bpi.java.PostImpl;

public abstract class Post {

	public static Post createInfo() {
		return new PostImpl(org.monet.space.kernel.model.news.Post.INFO);
	}

	public static Post createRequest() {
		return new PostImpl(org.monet.space.kernel.model.news.Post.SERVICE_REQUEST);
	}

	public static Post createResponse() {
		return new PostImpl(org.monet.space.kernel.model.news.Post.SERVICE_RESPONSE);
	}

	public abstract String getId();

	public abstract void setTitle(String title);

	public abstract void setBody(String body);

	public abstract void setTitleLink(MonetLink link);

	public abstract void setBodyLink(MonetLink link);

}
