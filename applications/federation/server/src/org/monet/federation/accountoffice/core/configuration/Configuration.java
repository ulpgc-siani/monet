package org.monet.federation.accountoffice.core.configuration;

import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration;

import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public interface Configuration {

  String CERTIFICATE_AUTHORITY_IDENTIFIER = "CertificateAuthority.Identifier";
  String CERTIFICATE_AUTHORITY_PASSWORD = "CertificateAuthority.Password";
  String CERTIFICATE_AUTHORITY_FILE = "CertificateAuthority.File";

  String CERTIFICATE_FILE = "Certificate.File";
  String CERTIFICATE_PASSWORD = "Certificate.Password";

  String JDBC_DATABASE = "Jdbc.Database";
  String JDBC_DATASOURCE = "Jdbc.DataSource";

  String SOCKET_HOST = "Socket.Host";
  String SOCKET_PORT = "Socket.Port";
  String SOCKET_MAX_SESSIONS = "Socket.MaxSessions";
  String SOCKET_ALLOWED_ADDRESSES = "Socket.AllowedAddresses";

  String SMTP_HOSTNAME = "Smtp.Hostname";
  String SMTP_PORT = "Smtp.Port";
  String SMTP_USER = "Smtp.User";
  String SMTP_PASS = "Smtp.Password";
  String SMTP_USE_TLS = "Smtp.UseTLS";
  String SMTP_USE_SSL = "Smtp.UseSSL";
  String SMTP_EMAIL_FROM = "Smtp.Email.From";
  String SMTP_EMAIL_TO = "Smtp.Email.To";

  String NAME = "Name";
  String VERSION = "Version";
  String AUTOSTART = "Autostart";
  String INACTIVITY_TIME = "InactivityTime";
  String MAX_REMEMBER_TIME = "MaxRememberTime";
  String MAX_SESSIONS = "MaxSessions";
  String SUSPEND_TIME = "SuspendTime";
  String REMOVE_SUSPEND_TIME = "RemoveSuspendTime";
  String SIGN_TIMESTAMP_INTERVAL = "SignTimestampInterval";

  String IMAGES_FOLDER = "/images";
  String CSS_FOLDER = "/css";
  String LOGO = "logo.png";
  String DEFAULT_LOGO = "default.png";
  String DEFAULT_SPACE_LOGO = "default.space.png";
  String CONFIGURATION_FILENAME = "federation.config";
  String SERVICE_CONFIGURATION_FILENAME = "federation.xml";
  String MOBILE_PUSH_API_KEY = "Mobile.PushApi.Key";

  String getCertificateAuthorityIdentifier();

  String getCertificateAuthorityPassword();

  String getCertificateAuthorityPath();

  String getCertificatePassword();

  String getCertificatePath();

  String getSocketHost();

  int getSocketPort();

  int getSocketMaxSessions();

  Set<String> getSocketAllowedAddresses();

  String getDatabaseSource();

  Properties getQueries();

  String getName();

  void setLabel(String label);

  String getLabel();

  long getInactivityTime();

  String getLogoPath();

  String getLogoFilename();

  String getDefaultSpaceLogoPath();

  String getDefaultSpaceLogoFilename();

  long getMaxRememberTime();

  int getMaxSessions();

  boolean isRegisterEnable();

  int getSuspendTime();

  int getRemoveSuspendTime();

  FederationConfiguration.Authentication.UserPassword getUserPasswordAuth();
  FederationConfiguration.Authentication.Micv getMicvAuth();

  long getSignTimestampInterval();

  String getConfigurationFile();

  String getBackservicePath();

  Properties getLanguage(String lang);

  String isLanguageSupport(String lang);

  Map<String, String> getSelectableLanguages();

  String getTemplatePath();

  String getResourcePath();

  String getP7B();

  boolean isUserPasswordActive();

  boolean isCertificateActive();

  boolean isOpenIDActive();

  boolean isMicvActive();

  String getProperty(String propertyName);

  String getProperty(String propertyName, String defaultValue);

  void reload();

  String getMobilePushAPIKey();

  String getSpaceBackserviceServletPath(URI spaceUri);
}
