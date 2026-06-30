package org.monet.federation.accountoffice.control.servlets.actions.impl;

import com.google.inject.Inject;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.components.requesttokencomponent.RequestTokenComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.totpcomponent.TotpComponent;
import org.monet.federation.accountoffice.core.components.totpcomponent.impl.TotpComponentImpl;
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
  private TotpComponent totpComponent;

  public static final class Mode {
    public static final String PASSWORD = "password";
    public static final String CERTIFICATE = "certificate";
    public static final String OPEN_ID = "openid";
    public static final String MICV = "micv";
  }

  @Inject
  public ActionLogin(Logger logger, AccountLayer accountLayer, SessionComponent sessionComponent, TemplateComponent templateComponent, Configuration configuration, RequestTokenComponent requestTokenComponent, TotpComponent totpComponent) {
    this.logger = logger;
    this.accountLayer = accountLayer;
    this.sessionComponent = sessionComponent;
    this.templateComponent = templateComponent;
    this.requestTokenComponent = requestTokenComponent;
    this.configuration = configuration;
    this.totpComponent = totpComponent;
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

      if (mode == LoginMode.PASSWORD && this.totpComponent.isActive()) {
        boolean hasPendingChallenge = this.totpComponent.hasPendingChallenge(request.getSession());
        boolean hasPrimaryCredentials = request.getParameter("username") != null || request.getParameter("password") != null;
        String totpCode = request.getParameter("totpCode");

        if (hasPendingChallenge && totpCode != null) {
          this.verifyTotpChallenge(request, response, lang, requestToken);
          return;
        }

        if (hasPendingChallenge && !hasPrimaryCredentials) {
          this.showTotpPage(request, response, lang, null);
          return;
        }

        if (hasPendingChallenge && hasPrimaryCredentials)
          this.totpComponent.clearPendingChallenge(request.getSession());
      }

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

      if (mode == LoginMode.PASSWORD && this.totpComponent.isActive()) {
        this.totpComponent.beginChallenge(request, user, requestToken, rememberMe);
        this.showTotpPage(request, response, lang, null);
        return;
      }

      this.completeLogin(request, response, user, rememberMe, requestToken, lang, remoteAddress, remoteUserAgent);
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }
  }

  private void verifyTotpChallenge(HttpServletRequest request, HttpServletResponse response, String lang, String requestToken) {
    TotpComponent.VerificationStatus status = this.totpComponent.verifyChallenge(request);
    if (status != TotpComponent.VerificationStatus.SUCCESS) {
      String message = TemplateComponent.TOTP_INVALID;
      if (status == TotpComponent.VerificationStatus.REUSED)
        message = TemplateComponent.TOTP_REUSED;
      else if (status == TotpComponent.VerificationStatus.EXPIRED)
        message = TemplateComponent.TOTP_EXPIRED;
      this.showTotpPage(request, response, lang, message);
      return;
    }

    TotpComponentImpl totpComponent = (TotpComponentImpl) this.totpComponent;
    String userId = totpComponent.getPendingUserId(request.getSession());
    boolean rememberMe = totpComponent.getPendingRememberMe(request.getSession());
    User user = this.accountLayer.load(userId);
    String remoteAddress = Utils.getAddress(request);
    String remoteUserAgent = request.getHeader("User-Agent").toLowerCase();
    String resolvedRequestToken = requestToken != null && !requestToken.isEmpty() ? requestToken : this.totpComponent.getPendingChallenge(request.getSession()).getRequestToken();
    this.totpComponent.clearPendingChallenge(request.getSession());
    this.completeLogin(request, response, user, rememberMe, resolvedRequestToken, lang, remoteAddress, remoteUserAgent);
  }

  private void completeLogin(HttpServletRequest request, HttpServletResponse response, User user, boolean rememberMe, String requestToken, String lang, String remoteAddress, String remoteUserAgent) {
    try {
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
      throw new RuntimeException("Could not complete TOTP login flow", e);
    }
  }

  private void showLogin(HttpServletRequest request, HttpServletResponse response, String lang, String requestToken, String username) {
    String baseUrl = Utils.getBaseUrl(request);
    int tries = this.sessionComponent.getAuthTriesOfUser(username);
    this.templateComponent.createLoginTemplate(response, true, requestToken, lang, baseUrl, tries);
  }

  private void showTotpPage(HttpServletRequest request, HttpServletResponse response, String lang, String additionalMessage) {
    TotpComponent.PendingChallenge challenge = this.totpComponent.getPendingChallenge(request.getSession());
    String baseUrl = Utils.getBaseUrl(request);
    this.templateComponent.createTotpLoginTemplate(response, challenge.getRequestToken(), lang, baseUrl, challenge.isSetupMode(), challenge.getSecret(), challenge.getOtpAuthUri(), additionalMessage);
  }

}
