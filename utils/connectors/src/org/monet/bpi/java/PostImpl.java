package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.MonetLink;
import org.monet.bpi.Post;

public class PostImpl extends Post {

	public PostImpl() {
	}

	public PostImpl(int type) {
		this();
	}

	@Override
	public String getId() {
		throw new NotImplementedException();
	}

	public void setTitle(String title) {
		throw new NotImplementedException();
	}

	public void setBody(String body) {
		throw new NotImplementedException();
	}

	public void setTitleLink(MonetLink link) {
		throw new NotImplementedException();
	}

	public void setBodyLink(MonetLink link) {
		throw new NotImplementedException();
	}

}
