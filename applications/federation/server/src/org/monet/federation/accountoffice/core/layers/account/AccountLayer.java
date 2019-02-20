package org.monet.federation.accountoffice.core.layers.account;

import org.monet.federation.accountoffice.core.model.User;

import javax.servlet.http.HttpServletRequest;

public interface AccountLayer {

  public static enum LoginMode {
    PASSWORD    { public String toString() { return "password"; }},
    CERTIFICATE { public String toString() { return "certificate"; }},
    OPEN_ID     { public String toString() { return "openid"; }},
    MICV        { public String toString() { return "micv"; }};

    public static LoginMode from(String mode) {
      if(mode != null && mode.equals("certificate"))
        return CERTIFICATE;
      else if(mode != null && mode.equals("openid"))
        return OPEN_ID;
      else if(mode != null && mode.equals("micv"))
        return MICV;
      else
        return PASSWORD;
    }
  }
  
  public User login(HttpServletRequest request, LoginMode model);

  public User load(String id);
  public User loadFromUsername(String username);
  public User loadFromEmail(String email);

  public boolean existsUser(String username);
  public void createUser(User user, String password);
  public void save(User user);

  public boolean changePassword(String username, String oldPassword, String newPassword);
  public boolean resetPassword(String userId, String newPassword);


}
