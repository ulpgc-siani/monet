package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Singleton;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.utils.Strings;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class AuthorizationServlet extends FederationServlet {
  private static final long serialVersionUID = 5942024161860017967L;

  @Override
  protected void doProcess(HttpServletRequest request, HttpServletResponse response) {
    try {

      if (!this.isFederationRunning(response))
        return;

      this.initialize(response);
      
      String user = (String) request.getSession().getAttribute("user");
      String lang = (String) request.getSession().getAttribute("lang");
      String token = StringEscapeUtils.escapeHtml(request.getParameter("oauth_token"));
      String url = "";
      String space = "";

      if (token != null && !token.equals("") && !token.equals("null")) {
        url = this.requestTokenComponent.getUrlCallback(token);

        try {
          String expression = ".*://.*/(.*)/.*";
          Strings strings = new Strings();
          space = strings.getRegularExpression(expression, url, 0)[0];
        } catch (Exception e) {
          logger.error("I can't get space name. Url: " + url);
          space = "";
        }
      }
      String path = this.getServletContext().getRealPath("");
      String action = StringEscapeUtils.escapeHtml(request.getParameter("action"));
      String view = StringEscapeUtils.escapeHtml(request.getParameter("view"));
      if (lang == null) {
        String acceptLanguages = request.getHeader("Accept-Language");
        if (acceptLanguages == null) {
          lang = this.configuration.isLanguageSupport("");
          request.getSession().setAttribute("lang", lang);
        } else {
          String prefLanguage  = acceptLanguages.split("-")[0].split(",")[0];
          if ( (prefLanguage == null)) {
            logger.info("Accept-Language(%s)", prefLanguage);
            prefLanguage = "en";
          }

          lang = this.configuration.isLanguageSupport(prefLanguage);
          request.getSession().setAttribute("lang", lang);
        }
      }

      if (view != null) {
        if (view.equals("register"))
          showRegister(request, response, path, token, lang);
        else if (view.equals("resetPassword"))
          showResetPassword(request, response, path, token, lang);
        else if (view.equals("changePassword"))
          showChangePassword(request, response, path, token, lang);
      } else if (action != null)
        this.actionFactory.getAction(action).execute(request, response, this.getServletContext().getRealPath(""));
      else {
        if (user != null && user != "") {
          logger.info("Continue url: " + url);

          String accessToken = this.sessionComponent.getAccessToken(token);

          if (accessToken == null && this.sessionComponent.hasSessions(user)) {
            String remoteAddr = Utils.getAddress(request);
            String remoteUA = request.getHeader("User-Agent").toLowerCase();
            accessToken = this.sessionComponent.addUser(user, remoteAddr, remoteUA, lang, true, false, space);
          } else {
            this.actionFactory.getAction("logout").execute(request, response, this.getServletContext().getRealPath(""));
            String baseUrl = Utils.getBaseUrl(request);
            this.templateComponent.createLoginTemplate(response, false, token, lang, baseUrl, -1);
            return;
          }

          this.requestTokenComponent.setAccessToken(token, accessToken);
          if ((url != null) && (!url.equals(""))) {
            String verifier = this.requestTokenComponent.generateVerifier(token);
            if (url.indexOf("?") != -1)
              url += "&oauth_token=" + token + "&oauth_verifier=" + verifier;
            else
              url += "/?oauth_token=" + token + "&oauth_verifier=" + verifier;
            Utils.sendRedirect(response, url);
          } else {
            String baseUrl = Utils.getBaseUrl(request);
            Utils.sendRedirect(response, baseUrl + "/home/");

          }
        } else {
          if ((request.getParameter("idsession") != null) && (request.getParameter("name") != null)) {
            //Login micv
            this.actionFactory.getAction("login").execute(request, response, this.getServletContext().getRealPath(""));
          } else {
            if (configuration.getUserPasswordAuth() != null) {
              String baseUrl = Utils.getBaseUrl(request);
              this.templateComponent.createLoginTemplate(response, false, token, lang, baseUrl, -1);
            } else if (configuration.getMicvAuth() != null) {
              Utils.sendRedirect(response, configuration.getMicvAuth().getMicvAuth().getErrorUrl());
            }
          }
        }
      }
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }
  }

  private void showRegister(HttpServletRequest request, HttpServletResponse response, String path, String token, String lang) {
    String baseUrl = Utils.getBaseUrl(request);
    this.templateComponent.createRegisterTemplate(response, path, false, token, lang, baseUrl, "");
  }

  private void showResetPassword(HttpServletRequest request, HttpServletResponse response, String path, String token, String lang) {
    String baseUrl = Utils.getBaseUrl(request);
    this.templateComponent.createResetPasswordTemplate(response, path, false, token, lang, baseUrl, null);
  }

  private void showChangePassword(HttpServletRequest request, HttpServletResponse response, String path, String token, String lang) {
    String baseUrl = Utils.getBaseUrl(request);
    this.templateComponent.createChangePasswordTemplate(response, path, false, token, lang, baseUrl, null);
  }
}
