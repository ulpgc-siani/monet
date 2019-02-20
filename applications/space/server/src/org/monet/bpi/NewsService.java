package org.monet.bpi;

public abstract class NewsService {

	protected static NewsService instance;

	public static void commentPost(Post post, String text, String author) {
		instance.commentPostImpl(post, text, author);
	}

	public static void postToUser(Post post, String user) {
		instance.postToUserImpl(post, user);
	}

	public static void postAndNotifyToUser(Post post, String user) {
		instance.postAndNotifyToUserImpl(post, user);
	}

	public static void postToRole(Post post, String role) {
		instance.postToRoleImpl(post, role);
	}

	public static void postAndNotifyToRole(Post post, String role) {
		instance.postAndNotifyToRoleImpl(post, role);
	}

	public static void postToTaskTeam(Post post, Task task) {
		instance.postToTaskTeamImpl(post, task);
	}

	public static void postAndNotifyToTaskTeam(Post post, Task task) {
		instance.postAndNotifyToTaskTeamImpl(post, task);
	}

	public static void postToAll(Post post) {
		instance.postToAllImpl(post);
	}

	public static void postAndNotifyToAll(Post post) {
		instance.postAndNotifyToAllImpl(post);
	}

	protected abstract void commentPostImpl(Post post, String text, String author);

	protected abstract void postToUserImpl(Post post, String userId);

	protected abstract void postAndNotifyToUserImpl(Post post, String userId);

	protected abstract void postToRoleImpl(Post post, String role);

	protected abstract void postAndNotifyToRoleImpl(Post post, String role);

	protected abstract void postToTaskTeamImpl(Post post, Task task);

	protected abstract void postAndNotifyToTaskTeamImpl(Post post, Task task);

	protected abstract void postToAllImpl(Post post);

	protected abstract void postAndNotifyToAllImpl(Post post);

}
