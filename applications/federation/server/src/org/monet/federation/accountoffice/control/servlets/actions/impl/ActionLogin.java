package org.monet.federation.accountoffice.control.servlets.actions.impl;

import com.google.inject.Inject;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.components.requesttokencomponent.RequestTokenComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.utils.SendUser;
import org.monet.federation.accountoffice.utils.Strings;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionLogin implements Action {

  private Logger logger;
  private AccountLayer accountLayer;
  private SessionComponent sessionComponent;
  private TemplateComponent templateComponent;
  private RequestTokenComponent requestTokenComponent;
  private Configuration configuration;

  public static final class Mode {
    public static final String PASSWORD = "password";
    public static final String CERTIFICATE = "certificate";
    public static final String OPEN_ID = "openid";
    public static final String MICV = "micv";
  }

  @Inject
  public ActionLogin(Logger logger, AccountLayer accountLayer, SessionComponent sessionComponent, TemplateComponent templateComponent, Configuration configuration, RequestTokenComponent requestTokenComponent) {
    this.logger = logger;
    this.accountLayer = accountLayer;
    this.sessionComponent = sessionComponent;
    this.templateComponent = templateComponent;
    this.requestTokenComponent = requestTokenComponent;
    this.configuration = configuration;
  }

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response, String path) {
    logger.debug("execute(%s, %s, %s)", request, response, path);

    try {
      String lang = (String) request.getSession().getAttribute("lang");
      String requestToken = StringEscapeUtils.escapeHtml(request.getParameter("oauth_token"));
      LoginMode mode = LoginMode.from((String) request.getParameter("mode"));
      boolean rememberMe = request.getParameter("rememberme") != null && !request.getParameter("rememberme").equals("false");
      String remoteAddress = Utils.getAddress(request);
      String remoteUserAgent = request.getHeader("User-Agent").toLowerCase();

      if ((request.getParameter("idsession") != null) && (!request.getParameter("idsession").equals("")) &&
              (request.getParameter("name") != null) && (!request.getParameter("name").equals("")))
        mode = LoginMode.MICV;

      User user = this.accountLayer.login(request, mode);

      if (mode == LoginMode.OPEN_ID) {
        Utils.sendRedirect(response, user.getId());
        return;
      }

      if ((mode == LoginMode.MICV) && (user == null)) {
        Utils.sendRedirect(response, configuration.getMicvAuth().getMicvAuth().getErrorUrl());
        return;
      }

      if (user == null) {
        String username = StringEscapeUtils.escapeHtml(request.getParameter("username"));
        if (mode == LoginMode.PASSWORD) {
          if (username != null) {
            int countFailed = this.sessionComponent.addAuthTryToUser(username);
            if (countFailed == 0)
              request.getSession().setAttribute("nvUser", username);
          }
        }
        showLogin(request, response, lang, requestToken, username);
        return;
      }

      if (this.sessionComponent.isUserSuspended(user.getUsername())) {
        this.showLogin(request, response, lang, requestToken, user.getUsername());
        return;
      }

      String answer = StringEscapeUtils.escapeHtml(request.getParameter("answerCaptcha"));
      if (this.sessionComponent.isUserCaptchaAnswerCorrect(user.getUsername(), answer))
        this.sessionComponent.resetUserTries(user.getUsername());
      else {
        this.showLogin(request, response, lang, requestToken, user.getUsername());
        return;
      }

      if (!this.sessionComponent.hasAvailableSessions()) {
        this.showLogin(request, response, lang, requestToken, user.getUsername());
        return;
      }

      request.getSession().setAttribute("user", user.getUsername());
      request.getSession().setAttribute("lang", user.getLang());

      if (rememberMe)
        request.getSession().setMaxInactiveInterval((int) this.configuration.getMaxRememberTime() / 1000);

      String token = StringEscapeUtils.escapeHtml(request.getParameter("oauth_token"));
      String url = this.requestTokenComponent.getUrlCallback(token);
      String space = "";
      if (url != null) {
          try {
            String expression = ".*://.*/(.*)/.*";
            space = new Strings().getRegularExpression(expression, url, 0)[0];
          }
          catch(Exception e) {
            this.logger.error("Could not obtain space name from url for login. Url: %s", url);
          }
      }

      logger.info("User %s logged", user.getUsername());
      String accessToken = this.sessionComponent.addUser(user.getUsername(), remoteAddress, remoteUserAgent, lang, false, false, space);
      this.requestTokenComponent.setAccessToken(requestToken, accessToken);
      SendUser.sendUser(request, response, this.requestTokenComponent, requestToken);

    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }
  }

  private void showLogin(HttpServletRequest request, HttpServletResponse response, String lang, String requestToken, String username) {
    String baseUrl = Utils.getBaseUrl(request);
    int tries = this.sessionComponent.getAuthTriesOfUser(username);
    this.templateComponent.createLoginTemplate(response, true, requestToken, lang, baseUrl, tries);
  }

}
