package org.monet.federation.accountoffice.control.servlets.actions.impl;

import com.google.inject.Inject;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.mail.HtmlEmail;
import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.control.servlets.actions.ActionFactory;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ActionRegister implements Action {

  private Logger            logger;
  private Configuration     configuration;
  private TemplateComponent templateComponent;
  private AccountLayer      accountLayer;
  private ActionFactory     actionFactory;

  @Inject
  public ActionRegister(Logger logger, AccountLayer accountLayer, Configuration configuration, TemplateComponent templateComponent) {
    this.logger = logger;
    this.templateComponent = templateComponent;
    this.configuration = configuration;
    this.accountLayer = accountLayer;
  }

  @Inject
  public void injectActionFactory(ActionFactory actinFactory) {
    this.actionFactory = actinFactory;
  }

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response, String path) {
    this.logger.info("execute()");
    if (this.configuration.isRegisterEnable()) {
      String lang = (String) request.getSession().getAttribute("lang");
      String baseUrl = Utils.getBaseUrl(request);

      try {
        String username = StringEscapeUtils.unescapeHtml(request.getParameter("username"));
        String fullname = StringEscapeUtils.unescapeHtml(request.getParameter("fullname"));
        String email = StringEscapeUtils.unescapeHtml(request.getParameter("email"));
        String password = StringEscapeUtils.unescapeHtml(request.getParameter("password"));

        if (username == null || username.isEmpty()) {
          this.templateComponent.createRegisterTemplate(response, path, true, "", lang, baseUrl, "");
          return;
        }

        if (this.accountLayer.existsUser(username)) {
          this.templateComponent.createRegisterTemplate(response, path, true, "", lang, baseUrl, "errorExists");
          return;
        }

        User user = new User();
        user.setUsername(username);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setLang(lang);
        user.setMode(LoginMode.PASSWORD);
        this.accountLayer.createUser(user, password);
        this.actionFactory.getAction(ActionFactory.LOGIN).execute(request, response, path);
        sendMailAdmin(user, request.getRequestURL().toString());
      } catch (Exception e) {
        this.logger.error(e.getMessage(), e);
        this.templateComponent.createRegisterTemplate(response, path, false, "", lang, baseUrl, "");
      }
    } else {
      this.logger.error("execute(); The federation does not allow user registration. Attempt at hacking!!!");
    }
  }

  private static String styles;

  private void sendMailAdmin(User user, String url) {
    try {
      if (styles == null)
        readStyles();

      HtmlEmail httpMail = new HtmlEmail();

      String userDetails = "<div><h3>Account Details:</h3>" + "<table>" + "<tr><td>User:</td><td>" + user.getUsername() + "</td></tr>" + "<tr><td>Email:</td><td>" + user.getEmail() + "</td></tr>" + "<tr><td>Fullname:</td><td>" + user.getFullname() + "</td></tr>" + "</table></div>";

      String form = "<form  style=\"height: 40%;  border:0px;\" class=\"box\" action=\"" + url + "/?action=enableaccount\" method=\"post\">";
      form += String.format("<input type=\"hidden\" name=\"userId\" value=\"%s\" /> <div class=\"tab_content\">", user.getId());

      httpMail.setHostName(this.configuration.getProperty(Configuration.SMTP_HOSTNAME));
      httpMail.setSmtpPort(Integer.valueOf(this.configuration.getProperty(Configuration.SMTP_PORT)));
      httpMail.setAuthentication(this.configuration.getProperty(Configuration.SMTP_USER), this.configuration.getProperty(Configuration.SMTP_PASS));
      httpMail.setTLS(Boolean.valueOf(this.configuration.getProperty(Configuration.SMTP_USE_TLS)));

      httpMail.setFrom(this.configuration.getProperty(Configuration.SMTP_EMAIL_FROM));
      httpMail.addTo(this.configuration.getProperty(Configuration.SMTP_EMAIL_TO));
      httpMail.setSubject("Enable Account");

      httpMail.setHtmlMsg("<html><head>" + styles + "</head><body><div class=\"layout\" style=\"width: 300px;\"><h2>Enable Account</h2> " + userDetails + "<table class=\"dialogs\" style=\"height:40%; padding:0;\"><tr><td class=\"login dialog\" style=\"width:70%;\">" + form + "<button style=\"float:right;\" type=\"submit\" name=\"cancel\" >Accept</button>" + "</table></form>" + "<span class=\"copyright\">© 2013 Todos los derechos reservados</span>" + "</div>" + "</body></html>");
      httpMail.send();
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }

  }

  private void readStyles() throws Exception {
    FileInputStream fstream = new FileInputStream(this.configuration.getResourcePath() + Configuration.CSS_FOLDER + File.separator + "styles.css");
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    StringBuilder strStyles = new StringBuilder();
    while ((strLine = br.readLine()) != null)
      strStyles.append(strLine);
    in.close();
    styles = "<style type=\"text/css\">" + strStyles.toString() + "</style>";
  }

}
