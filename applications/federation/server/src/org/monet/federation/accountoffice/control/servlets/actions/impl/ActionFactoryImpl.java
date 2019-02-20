package org.monet.federation.accountoffice.control.servlets.actions.impl;

import java.util.HashMap;

import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.control.servlets.actions.ActionFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class ActionFactoryImpl implements ActionFactory {
  private HashMap<String,Action> actions;
  
  @Inject
  public ActionFactoryImpl(Injector injector){
    actions = new HashMap<String, Action>();

    actions.put(ActionFactory.LOGIN, injector.getInstance(ActionLogin.class));
    actions.put(ActionFactory.LOGOUT, injector.getInstance(ActionLogout.class));
    actions.put(ActionFactory.CHANGE_LANGUAGE,injector.getInstance(ActionChangeLanguage.class));
    actions.put(ActionFactory.RESET_PASSWORD,injector.getInstance(ActionResetPassword.class));
    actions.put(ActionFactory.REGISTER,injector.getInstance(ActionRegister.class));
    actions.put(ActionFactory.CHANGE_PASSWORD, injector.getInstance(ActionChangePassword.class));
  }

  @Override
  public Action getAction(String actionKey) {
    return actions.get(actionKey);
  }
}
