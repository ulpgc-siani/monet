package org.monet.bpi;

public abstract class NewsService {

  protected static NewsService instance;
  
  public static void postToUser(Post post, String userId) {
    instance.postToUserImpl(post, userId);
  }
  
  public static void postToRole(Post post, String role) {
    instance.postToRoleImpl(post, role);
  }
  
  public static void postToTaskTeam(Post post, Task task) {
    instance.postToTaskTeamImpl(post, task);
  }
  
  public static void postToAll(Post post) {
    instance.postToAllImpl(post);
  }
  
  protected abstract void postToUserImpl(Post post, String userId);
  protected abstract void postToRoleImpl(Post post, String role);
  protected abstract void postToTaskTeamImpl(Post post, Task task);
  protected abstract void postToAllImpl(Post post);
  
}
