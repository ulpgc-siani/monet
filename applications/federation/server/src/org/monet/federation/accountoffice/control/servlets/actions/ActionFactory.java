package org.monet.federation.accountoffice.control.servlets.actions;

public interface ActionFactory {
  public static final String LOGIN = "login";
  public static final String LOGOUT = "logout";
  public static final String CHANGE_LANGUAGE = "changelanguage";
  public static final String RESET_PASSWORD = "resetpassword";
  public static final String REGISTER = "register";
  public static final String CHANGE_PASSWORD = "changepassword";
  Action getAction(String actionKey);
}
