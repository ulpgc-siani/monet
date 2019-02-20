package org.monet.federation.accountoffice.core.layers.auth.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.layers.auth.AuthLayer;
import org.monet.federation.accountoffice.core.layers.auth.impl.micv.BCLDAPwsdl.LDAP_BCwsdlPortTypeProxy;
import org.monet.federation.accountoffice.core.layers.auth.impl.micv.sessioninternal.Sesion_WebServicePortType;
import org.monet.federation.accountoffice.core.layers.auth.impl.micv.sessioninternal.Sesion_WebService_Impl;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration;

import javax.servlet.http.HttpServletRequest;

public class AuthLayerMicv extends FederationAuthLayer implements AuthLayer {
  private Logger logger;
  private final DataRepository dataRepository;

  @Inject
  public AuthLayerMicv(Logger logger, Configuration configuration, DataRepository dataRepository) {
    this.logger = logger;
    this.dataRepository = dataRepository;
    try {
      FederationConfiguration.Authentication.MicvAuth authConfig = configuration.getMicvAuth().getMicvAuth();

      this.wsSession = new Sesion_WebService_Impl().getSesion_WebServicePort(authConfig.getUrl());
      this.wsPermissions = new LDAP_BCwsdlPortTypeProxy(authConfig.getLdapUrl());

    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }

  }

  private Sesion_WebServicePortType wsSession;
  private LDAP_BCwsdlPortTypeProxy wsPermissions;

  private User createMicvUser(String userId) {
    return this.createMicvUser(userId, userId + "@micv");
  }

  private User createMicvUser(String username, String email) {
    User user;

    if (!this.dataRepository.existsUser(username)) {
      user = new User();
      user.setUsername(username);
      user.setFullname(username);
      user.setEmail(email);
      user.setLang(null);
      user.setMode(LoginMode.PASSWORD);

      this.dataRepository.createUser(user);
    }
    else {
      user = this.dataRepository.loadUserFromUsername(username);
    }

    return user;
  }

  @Override
  public User login(HttpServletRequest request) {
    logger.debug("login(%s)", request);

    String username = requestParam(request, "name");
    String idSession = requestParam(request, "idsession");

    return this.login(username, idSession);
  }

  private User login(String username, String password) {
    User userInfo = null;
    try {
      String result;
      result = this.wsSession.comprobar_validez_sesion(password, username);
      if (!result.equals("1")) return null;
      result = this.wsPermissions.servicioMICV(username);
      if (!result.toLowerCase().equals("s")) return null;
      userInfo = this.createMicvUser(username);
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }

    return userInfo;
  }

  @Override
  public User loadUser(String userId) {
    logger.debug("loadUser(%s)", userId);

    return this.dataRepository.loadUser(userId);
  }

  @Override
  public User loadUserFromEmail(String email) {
    logger.debug("loadUserByEmail(%s)", email);

    return this.dataRepository.loadUserFromEmail(email);
  }

  @Override
  public void createUser(User user, String password) {
    logger.debug("createUser(%s)", user);

    this.dataRepository.createUser(user);
  }

  @Override
  public void saveUser(User user) {
    logger.debug("saveUser(%s)", user);

    this.dataRepository.createUser(user);
  }

  @Override
  public boolean updateUserPassword(User user, String newPassword) {
    logger.debug("updateUserPassword(%s, %s)", user.getId(), "****");

    return true;
  }

  @Override
  public boolean isValidLogin(String userId, String password) {
    logger.debug("isValidLogin(%s, %s)", userId, "****");
    return this.login(userId, password) != null;
  }

}
