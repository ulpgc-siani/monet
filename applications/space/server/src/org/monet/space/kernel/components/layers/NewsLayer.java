package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.news.Post;
import org.monet.space.kernel.model.news.PostComment;
import org.monet.space.kernel.model.news.seed.NewsSeed;

import java.util.ArrayList;

public interface NewsLayer extends Layer {

	public void publish(Post post, boolean withNotification);

	public void publish(NewsSeed seed, ArrayList<String> idUsers, ArrayList<String> roles);

	public void publishToUser(Post post, String userId, boolean withNotification);

	public void publishToRole(Post post, String role, boolean withNotification);

	public void publishToTask(Post post, Task<?> task, boolean withNotification);

	public Post publishToSeed(NewsSeed seed);

	public Post loadPost(String postId);

	public void addCommentToPost(String postId, PostComment comment);

	public void addFilterPost(String postId);

	public void addFilterPostByAuthor(String authorId);

	public ArrayList<Post> getPosts(int startPos, int limit);

}
