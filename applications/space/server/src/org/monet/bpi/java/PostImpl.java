package org.monet.bpi.java;

import org.monet.bpi.MonetLink;
import org.monet.bpi.Post;

import java.util.Date;

public class PostImpl extends Post {

	org.monet.space.kernel.model.news.Post post;

	public PostImpl() {
		this.post = new org.monet.space.kernel.model.news.Post();
		this.post.setCreateDate(new Date());
	}

	public PostImpl(int type) {
		this();
		this.post.setType(type);
	}

	@Override
	public String getId() {
		return this.post.getId();
	}

	public void setTitle(String title) {
		this.post.setTitle(title);
	}

	public void setBody(String body) {
		this.post.setBody(body);
	}

	public void setTitleLink(MonetLink link) {
		if (link == null)
			this.post.setTarget(null);
		else
			this.post.setTarget(((MonetLinkImpl) link).getLink());
	}

	public void setBodyLink(MonetLink link) {
		if (link == null)
			this.post.setBodyTarget(null);
		else
			this.post.setBodyTarget(((MonetLinkImpl) link).getLink());
	}

}
