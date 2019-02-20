package org.monet.federation.accountoffice.core.components.accountcomponent;



public interface SessionComponent {

  public String addUser(String username, String remoteAddr, String remoteUA, String lang, boolean rememberMe, boolean isMobile, String space);

  public boolean deleteUser(String username);

  public boolean deleteUserFromToken(String token);

  public String getAccessToken(String token);

  public boolean isUserOnline(String token);
  
  public boolean hasAvailableSessions();

  public boolean hasSessions(String username);

  public String getVerifier(String token);
  
  public String getUsername(String token);
  
  public int addAuthTryToUser(String username);

  public void resetUserTries(String user);

  boolean isUserSuspended(String username);

  int getAuthTriesOfUser(String username);

  public void setCaptchaAnswerToUser(String username, String captchaAnswer);

  public boolean isUserCaptchaAnswerCorrect(String username, String answer);

}
