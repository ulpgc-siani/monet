package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.MonetLink;
import org.monet.space.kernel.model.news.Post;
import org.monet.space.kernel.model.news.PostComment;

import java.util.HashMap;
import java.util.List;

public class NewsViewRender extends ViewRender {

	public NewsViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	protected void initPosts(HashMap<String, Object> viewMap) {
		int start = 0;
		int limit = 10;

		if (this.getParameter("start") != null)
			start = (Integer) this.getParameter("start");

		if (this.getParameter("limit") != null)
			limit = (Integer) this.getParameter("limit");

		List<Post> posts = this.renderLink.loadNews(start, limit);

		StringBuilder postBuilder = new StringBuilder();
		for (Post p : posts)
			postBuilder.append(renderPost(p));

		viewMap.put("content", postBuilder.toString());
	}

	protected void initPost(HashMap<String, Object> viewMap) {
		Post post = (Post) this.getParameter("post");
		viewMap.put("content", this.renderPost(post));
	}

	protected void initComment(HashMap<String, Object> viewMap) {
		PostComment comment = (PostComment) this.getParameter("comment");
		viewMap.put("content", this.renderComment(comment));
	}

	protected String renderPost(Post post) {
		StringBuilder commentBuilder = new StringBuilder();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String messageTitle = post.getTitle();
		String messageBody = post.getBody() != null ? post.getBody() : "";
		String messageImage = "";

		if (post.getTarget() != null) {
			switch (post.getTarget().getType()) {
				case Node:
					messageTitle = renderNodeLink(post.getTarget(), messageTitle);
					break;
				case Task:
					messageTitle = renderTaskLink(post.getTarget(), messageTitle);
					break;
				case User:
					break;
				default:
					break;
			}
		}

		if (post.getBodyTarget() != null) {
			switch (post.getBodyTarget().getType()) {
				case Node:
					messageBody = renderNodeLink(post.getBodyTarget(), messageBody);
					break;
				case Task:
					messageBody = renderTaskLink(post.getBodyTarget(), messageBody);
					break;
				case User:
					break;
				default:
					break;
			}
		}

		switch (post.getType()) {
			case Post.BUSINESS_MODEL_UPDATED:
			case Post.BUSINESS_MODEL_INSTALLED:
				messageImage = "monet";
				break;
			case Post.INFO:
				messageImage = "info";
				break;
			case Post.SERVICE_REQUEST:
				messageImage = "order";
				break;
			case Post.SERVICE_RESPONSE:
				messageImage = "response";
				break;
			case Post.USER_POST:
				messageImage = "dialog";
				break;
		}

		map.clear();

		map.put("image", messageImage);
		map.put("messageTitle", messageTitle);
		map.put("messageBody", messageBody);
		map.put("createDate", post.getCreateDate());
		map.put("postId", post.getId());

		for (PostComment comment : post.getComments()) {
			commentBuilder.append(renderComment(comment));
		}
		map.put("comments", commentBuilder.toString());

		return block("content.news$post", map);
	}

	protected String renderTaskLink(MonetLink target, String title) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("target", target.getId());
		map.put("view", target.getView()!=null?target.getView():"");
		map.put("title", title);
		return block("content.news$post.titleLinkTask", map);
	}

	protected String renderNodeLink(MonetLink target, String title) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("target", target.getId());
		map.put("title", title);
		return block("content.news$post.titleLinkNode", map);
	}

	protected String renderComment(PostComment comment) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("author", comment.getAuthor());
		map.put("commentBody", comment.getText());
		map.put("createDate", comment.getCreateDate());

		return block("content.news$comment", map);
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (this.getParameter("comment") != null)
			this.initComment(map);
		else if (this.getParameter("post") != null)
			this.initPost(map);
		else
			this.initPosts(map);

		return block("content", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.news");
		super.init();
	}

}
