package org.monet.federation.accountoffice.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.monet.federation.accountoffice.core.configuration.Configuration;

public abstract class EmailService {

  public static void sendMail(Configuration configuration, String subject, String message) throws EmailException {
    HtmlEmail mailer = new HtmlEmail();
    mailer.setHostName(configuration.getProperty(Configuration.SMTP_HOSTNAME));
    mailer.setSmtpPort(Integer.valueOf(configuration.getProperty(Configuration.SMTP_PORT)));
    mailer.setAuthentication(configuration.getProperty(Configuration.SMTP_USER), configuration.getProperty(Configuration.SMTP_PASS));
    mailer.setTLS(Boolean.valueOf(configuration.getProperty(Configuration.SMTP_USE_TLS)));
    mailer.setSSL(Boolean.valueOf(configuration.getProperty(Configuration.SMTP_USE_SSL)));

    mailer.setFrom(configuration.getProperty(Configuration.SMTP_EMAIL_FROM));
    mailer.addTo(configuration.getProperty(Configuration.SMTP_EMAIL_TO));

    mailer.setSubject(subject);
    mailer.setHtmlMsg(message);
    mailer.send();
  }

}
