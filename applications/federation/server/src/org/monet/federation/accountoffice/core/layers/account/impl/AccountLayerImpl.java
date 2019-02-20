package org.monet.federation.accountoffice.core.layers.account.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.agents.AgentMobilePushService;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.layers.auth.AuthLayer;
import org.monet.federation.accountoffice.core.layers.auth.impl.*;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration.Authentication.UserPassword;
import org.monet.mobile.service.PushOperations;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Singleton
public class AccountLayerImpl implements AccountLayer {

  private Logger logger;
  private DataRepository dataRepository;
  private HashMap<LoginMode, AuthLayer> authLayers = new HashMap<AccountLayer.LoginMode, AuthLayer>();
  private AgentMobilePushService mobilePushService;

  @Inject
  public AccountLayerImpl(Logger logger, Configuration configuration, Injector injector, DataRepository dataRepository) {
    this.logger = logger;
    this.dataRepository = dataRepository;

    if (configuration.isCertificateActive())
      this.authLayers.put(LoginMode.CERTIFICATE, injector.getInstance(AuthLayerCertificate.class));
    if (configuration.isOpenIDActive())
      this.authLayers.put(LoginMode.OPEN_ID, injector.getInstance(AuthLayerCertificate.class));
    if (configuration.isMicvActive())
      this.authLayers.put(LoginMode.MICV, injector.getInstance(AuthLayerMicv.class));
    if (configuration.isUserPasswordActive()) {
      UserPassword userPasswordAuth = configuration.getUserPasswordAuth();

      if (userPasswordAuth.getLdapAuth() != null) {
        this.authLayers.put(LoginMode.PASSWORD, injector.getInstance(AuthLayerLDAP.class));
      } else if (userPasswordAuth.getDatabaseAuth() != null)
        this.authLayers.put(LoginMode.PASSWORD, injector.getInstance(AuthLayerDB.class));
      else if (userPasswordAuth.getMockAuth() != null)
        this.authLayers.put(LoginMode.PASSWORD, injector.getInstance(AuthLayerMock.class));
      else if (userPasswordAuth.getCustomAuth() != null) {
        String classname = userPasswordAuth.getCustomAuth().getClassname();
        try {
          Class<?> clazz = Class.forName(classname);
          this.authLayers.put(LoginMode.PASSWORD, (AuthLayer) clazz.newInstance());
        } catch (Exception e) {
          throw new RuntimeException("Can't load custom authentication method '" + classname + "': " + e.getMessage(), e);
        }
      }
    }
  }

  @Inject
  public void inject(AgentMobilePushService mobilePushService) {
    this.mobilePushService = mobilePushService;
  }

  @Override
  public User login(HttpServletRequest request, LoginMode mode) {
    this.logger.debug("login(%s, %s)", request, mode);

    AuthLayer authLayer = this.getAuthLayerForUser(mode);

    User user = authLayer.login(request);
    if (user != null)
      this.updateAndFill(user);

    return user;
  }


  @Override
  public User loadFromEmail(String email) {
    this.logger.debug("updateAndFill(%s)", email);

    return this.dataRepository.loadUserFromEmail(email);
  }

  private void updateAndFill(User user) {
    this.logger.debug("updateAndFill(%s)", user);

    String username = user.getUsername();
    boolean exists = this.dataRepository.existsUser(username);
    if (!exists) {
      //TODO: fill data that authlayer don't provide
      User localUser = this.dataRepository.createUser(user);
      user.fillFrom(localUser);
    } else {
      User localUser = this.dataRepository.loadUserFromUsername(username);
      user.fillFrom(localUser);
      this.dataRepository.saveUser(user);
    }
  }

  @Override
  public User load(String id) {
    this.logger.debug("load(%s)", id);

    User localUser = this.dataRepository.loadUser(id);
    if (localUser == null)
      throw new RuntimeException(String.format("Invalid user %s", id));

    return localUser;
  }

  @Override
  public User loadFromUsername(String username) {
    this.logger.debug("load(%s)", username);

    User localUser = this.dataRepository.loadUserFromUsername(username);
    if (localUser == null)
      throw new RuntimeException(String.format("Invalid user %s", username));

    return localUser;
  }

  @Override
  public boolean existsUser(String username) {
    this.logger.debug("existsUser(%s)", username);

    return this.dataRepository.existsUser(username);
  }

  @Override
  public void createUser(User user, String password) {
    this.logger.debug("createUser(%s)", user);

    this.dataRepository.createUser(user);

    AuthLayer authLayer = this.getAuthLayerForUser(user);
    authLayer.createUser(user, password);
  }

  @Override
  public void save(User user) {
    this.logger.debug("save(%s)", user);

    this.dataRepository.saveUser(user);

    AuthLayer authLayer = this.getAuthLayerForUser(user);
    authLayer.saveUser(user);
  }

  @Override
  public boolean changePassword(String username, String oldPassword, String newPassword) {
    this.logger.debug("changePassword(%s, %s, %s)", username, "****", "****");

    User user = this.loadFromUsername(username);
    AuthLayer authLayer = this.getAuthLayerForUser(user);

    if (!authLayer.isValidLogin(username, oldPassword))
      return false;

    boolean made = authLayer.updateUserPassword(user, newPassword);

    if (made)
      mobilePushService.push(user.getId(), PushOperations.PASSWORD_RESET, null);

    return made;
  }

  @Override
  public boolean resetPassword(String userId, String newPassword) {
    this.logger.debug("resetPassword(%s, %s)", userId, "****");

    User user = this.load(userId);
    AuthLayer authLayer = this.getAuthLayerForUser(user);

    boolean made = authLayer.updateUserPassword(user, newPassword);

    if (made)
      mobilePushService.push(user.getId(), PushOperations.PASSWORD_RESET, null);

    return made;
  }

  private AuthLayer getAuthLayerForUser(User user) {
    this.logger.debug("getAuthLayerForUser(%s)", user);

    return this.getAuthLayerForUser(user.getMode());
  }

  private AuthLayer getAuthLayerForUser(LoginMode mode) {
    this.logger.debug("getAuthLayerForUser(%s)", mode);

    AuthLayer authLayer = this.authLayers.get(mode);
    if (authLayer == null) {
      throw new RuntimeException(String.format("Authentication mode '%s' are disabled", mode));
    }
    return authLayer;
  }

}
