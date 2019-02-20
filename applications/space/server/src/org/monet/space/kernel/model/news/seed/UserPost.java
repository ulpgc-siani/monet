package org.monet.space.kernel.model.news.seed;

import org.monet.space.kernel.model.news.Post;

import java.util.Date;

public class UserPost implements NewsSeed {

	private String author;
	private String authorId;
	private String body;
	private String wallUserId;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getWallUserId() {
		return wallUserId;
	}

	public void setWallUserId(String wallUserId) {
		this.wallUserId = wallUserId;
	}

	@Override
	public Post toPost() {
		Post post = new Post();
		post.setTitle(this.author);
		post.setType(Post.USER_POST);
		post.setBody(this.body);
		post.setCreateDate(new Date());
		if (this.wallUserId != null)
			post.setWallUserId(this.wallUserId);
		return post;
	}

}
