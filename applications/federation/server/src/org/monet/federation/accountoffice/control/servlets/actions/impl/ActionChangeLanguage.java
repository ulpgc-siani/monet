package org.monet.federation.accountoffice.control.servlets.actions.impl;

import com.google.inject.Inject;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ActionChangeLanguage implements Action {
  private Logger            logger;
  private TemplateComponent templateComponent;
  private SessionComponent  sessionComponent;
  private AccountLayer      accountLayer;
  private Configuration     configuration;

  @Inject
  public ActionChangeLanguage(Logger logger, AccountLayer accountLayer, SessionComponent sessionComponent, TemplateComponent templateComponent, Configuration configuration) {
    this.logger = logger;
    this.templateComponent = templateComponent;
    this.sessionComponent = sessionComponent;
    this.accountLayer = accountLayer;
    this.configuration = configuration;
  }

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response, String path) {
    this.logger.debug("execute()");

    HttpSession session = request.getSession();
    String username  = (String) session.getAttribute("user");

    String lang = StringEscapeUtils.escapeHtml(request.getParameter("language")).toLowerCase();
    String token = StringEscapeUtils.escapeHtml(request.getParameter("oauth_token"));
    String baseUrl = Utils.getBaseUrl(request);

    request.getSession().setAttribute("lang", lang);
    
    if ((username == null) || username.equals("")) {
      String userId = (String) request.getSession().getAttribute("nvUser");
      int tries = this.sessionComponent.getAuthTriesOfUser(userId);
      this.templateComponent.createLoginTemplate(response, false, token, lang, baseUrl, tries);
    } else {
      User user = this.accountLayer.loadFromUsername(username);
      user.setLang(lang);
      this.accountLayer.save(user);
      this.templateComponent.createLoggedTemplate(response, username, null, lang, baseUrl);
    }
  }
}
