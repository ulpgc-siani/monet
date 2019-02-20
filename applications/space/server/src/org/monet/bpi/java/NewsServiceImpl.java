package org.monet.bpi.java;

import org.monet.api.federation.setupservice.impl.library.LibraryString;
import org.monet.bpi.NewsService;
import org.monet.bpi.Post;
import org.monet.bpi.Task;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.MonetLink;
import org.monet.space.kernel.model.Notification;
import org.monet.space.kernel.model.news.PostComment;

import java.util.Date;

public class NewsServiceImpl extends NewsService {

	public static void init() {
		instance = new NewsServiceImpl();
	}

	@Override
	protected void commentPostImpl(Post post, String text, String author) {
		PostComment comment = new PostComment();
		comment.setAuthorId("-1");
		comment.setAuthor(author);
		comment.setText(text);
		comment.setPostId(post.getId());
		comment.setCreateDate(new Date());
		ComponentPersistence.getInstance().getNewsLayer().addCommentToPost(post.getId(), comment);
	}

	@Override
	protected void postToUserImpl(Post bpiPost, String userId) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		ComponentPersistence.getInstance().getNewsLayer().publishToUser(post, userId, false);
	}

	@Override
	protected void postAndNotifyToUserImpl(Post bpiPost, String userId) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		ComponentPersistence.getInstance().getNewsLayer().publishToUser(post, userId, true);
	}

	@Override
	protected void postToRoleImpl(Post bpiPost, String role) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		ComponentPersistence.getInstance().getNewsLayer().publishToRole(post, role, false);
	}

	@Override
	protected void postAndNotifyToRoleImpl(Post bpiPost, String role) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		ComponentPersistence.getInstance().getNewsLayer().publishToRole(post, role, true);
	}

	@Override
	protected void postToTaskTeamImpl(Post bpiPost, Task bpiTask) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		org.monet.space.kernel.model.Task task = ((TaskImpl) bpiTask).task;
		ComponentPersistence.getInstance().getNewsLayer().publishToTask(post, task, false);
	}

	@Override
	protected void postAndNotifyToTaskTeamImpl(Post bpiPost, Task bpiTask) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		org.monet.space.kernel.model.Task task = ((TaskImpl) bpiTask).task;
		ComponentPersistence.getInstance().getNewsLayer().publishToTask(post, task, true);
	}

	@Override
	protected void postToAllImpl(Post bpiPost) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		post.setWallUserId(null);
		ComponentPersistence.getInstance().getNewsLayer().publish(post, false);
	}

	@Override
	protected void postAndNotifyToAllImpl(Post bpiPost) {
		org.monet.space.kernel.model.news.Post post = ((PostImpl) bpiPost).post;
		post.setWallUserId(null);
		ComponentPersistence.getInstance().getNewsLayer().publish(post, true);

	}

	private Notification createNotification(org.monet.space.kernel.model.news.Post post) {
        String title = post.getTitle();
        String body = post.getBody();
        MonetLink target = post.getTarget();

        String key = title!=null?LibraryString.cleanSpecialChars(title):"";
		key += "_" + (body!=null?LibraryString.cleanSpecialChars(body):"");
        key += "_" + (target!=null?LibraryString.cleanSpecialChars(target.toString()):"");

		Notification notification = new Notification();
		notification.setLabel(title);
		notification.setIcon(null);
		notification.setTarget(target != null?target.toString():null);
		notification.setPublicationId(org.monet.space.kernel.library.LibraryString.generateIdForKey(key));

		return notification;
	}

}