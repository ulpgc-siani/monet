package org.monet.federation.accountoffice.core.layers.auth.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.layers.auth.AuthLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.core.model.configurationmodels.FederationConfiguration.Authentication.LdapAuth;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Properties;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

public class AuthLayerLDAP extends FederationAuthLayer implements AuthLayer {

  public static final String ALIAS_APP = "app";
  public static final String ALIAS_DOMAIN = "domain";
  public static final String ALIAS_USERDOMAIN = "userdomain";
  public static final String ALIAS_LANG = "lang";
  public static final String ALIAS_EMAIL = "email";
  public static final String ALIAS_username = "uid";
  public static final String PARAM_CN = "cn";
  public static final String PARAM_SECURITY_PRINCIPAL_PREFIX = "security-principal-prefix";

  private Logger logger;
  private Properties properties;

  private String appUser;
  private String appPassword;
  private String schema;
  private Map<String, String> parameters;
  private boolean readOnly;
  private String domainName;
  private String userdomainName;
  private String securityPrincipalPrefix;

  @Inject
  public AuthLayerLDAP(Logger logger, Configuration configuration) {
    this.logger = logger;

    try {
      LdapAuth authConfig = configuration.getUserPasswordAuth().getLdapAuth();
      this.appUser = authConfig.getUser();
      this.appPassword = authConfig.getPassword();
      this.schema = authConfig.getSchema();
      this.parameters = authConfig.getAliasParamMap();
      this.readOnly = authConfig.isReadOnly();

      this.properties = new Properties();
      this.properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      this.properties.put(Context.PROVIDER_URL, authConfig.getUrl());
      this.properties.put(Context.REFERRAL, "ignore");

      this.domainName = this.parameters.get(ALIAS_DOMAIN);
      this.userdomainName = this.parameters.get(ALIAS_USERDOMAIN);
      if ((this.userdomainName == null) || "".equals(this.userdomainName)) this.userdomainName = this.domainName;

      this.securityPrincipalPrefix = this.parameters.get(PARAM_SECURITY_PRINCIPAL_PREFIX);
      if (this.securityPrincipalPrefix == null) this.securityPrincipalPrefix = "";
    } catch (Exception e) {
      logger.error("Error loading configuration file: " + e.getMessage(), e);
    }

  }

  @Override
  public User login(HttpServletRequest request) {
    logger.debug("login(%s)", request);

    String username = requestParam(request, "username");
    String password = requestParam(request, "password");

    return login(username, password);
  }

  @Override
  public boolean isValidLogin(String username, String password) {
    logger.debug("isValidLogin(%s, %s)", username, "****");

    return this.login(username, password) != null;
  }

  private User login(String username, String password) {
    logger.debug("login(%s, %s)", username, "****");

    User userInfo = null;
    String searchUserIDAtributte = "userPrincipalName";
    if (username != null && password != null) {
      String principalName = this.parameters.get(ALIAS_username) + "=" + username + "," + this.schema;
      String userPrincipalName = principalName;

      if (this.parameters.get(ALIAS_APP) != null && "activedirectory".equals(this.parameters.get(ALIAS_APP))) {
        principalName = username + "@" + userdomainName;
        userPrincipalName = principalName;
      }

      if (this.parameters.get(ALIAS_APP) != null && "ldap".equals(this.parameters.get(ALIAS_APP))) {
        principalName = username;
        userPrincipalName = username + "@" + userdomainName;
        if (this.parameters.get(ALIAS_username) != null && !this.parameters.get(ALIAS_username).equals("") ){
          searchUserIDAtributte = this.parameters.get(ALIAS_username);
          userPrincipalName = username;
          principalName = this.parameters.get(ALIAS_username) + "=" + username + "," + this.schema;
        }
      }

      properties.put(Context.SECURITY_PRINCIPAL, this.securityPrincipalPrefix + principalName);
      properties.put(Context.SECURITY_CREDENTIALS, password);
      InitialDirContext context = null;

      try {
        context = new InitialDirContext(properties);

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> renum = context.search(toDC(domainName),"(& ("+searchUserIDAtributte+"="+userPrincipalName+")(objectClass=*))", controls);
        if(!renum.hasMore()) {
          throw new Exception("Cannot locate user information for " + userPrincipalName);
        }
        userInfo = this.createLdapUser(username);
      } catch (Exception e) {
        logger.info("Domain context: " + domainName);
        logger.info("Filter: (& (userPrincipalName="+userPrincipalName+")(objectClass=user))");
        logger.error(e.getMessage(), e);
      } finally {
        close(context);
      }
    }
    return userInfo;
  }

  private User createLdapUser(String userId) {
    return this.createLdapUser(userId, userId + "@ldap");
  }

  private User createLdapUser(String username, String email) {
    User user = new User();
    user.setUsername(username);
    user.setFullname(username);
    user.setEmail(email);
    user.setLang(null);
    user.setMode(AccountLayer.LoginMode.PASSWORD);
    return user;
  }

  @Override
  public void createUser(User user, String password) {
    logger.debug("createUser(%s)", user);

    if (readOnly)
      throw new RuntimeException("LDAP is marked as read only.");
    InitialDirContext context = null;
    try {
      properties.put(Context.SECURITY_PRINCIPAL, this.appUser);
      properties.put(Context.SECURITY_CREDENTIALS, this.appPassword);
      context = new InitialDirContext(this.properties);

      if (this.exists(user.getId(), context))
        throw new RuntimeException("User already exists");

      Attribute classes = new BasicAttribute("objectclass");
      classes.add("person");
      classes.add("top");
      classes.add("inetOrgPerson");
      classes.add("organizationalPerson");
      Attributes attrs = new BasicAttributes();
      attrs.put("cn", user.getId());
      attrs.put("userPassword", password);
      attrs.put(classes);

      String securityPrincipal = this.parameters.get(PARAM_CN) + "=" + this.appUser;
      context.bind(securityPrincipal, null, attrs);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException("Error creating user: " + e.getMessage(), e);
    } finally {
      close(context);
    }
  }

  private boolean exists(String user, InitialDirContext context) throws Exception {
    SearchControls ctrls = new SearchControls();
    ctrls.setReturningAttributes(new String[]{"cn"});
    ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    NamingEnumeration<SearchResult> answer = context.search(this.schema, this.parameters.get(ALIAS_username) + "=" + user, ctrls);
    return answer.hasMore();
  }

  private User loadUser(String user, String password, String filter, String filterValue) {
    logger.debug("getAccount(%s)", user);

    String securityPrincipal = this.parameters.get(PARAM_CN) + "=" + this.appUser;

    properties.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
    properties.put(Context.SECURITY_CREDENTIALS, password);
    InitialDirContext context = null;
    User userAcc = null;
    try {
      context = new InitialDirContext(properties);
      NamingEnumeration<? extends Attribute> attrs;
      SearchControls ctrls = new SearchControls();
      ctrls.setReturningAttributes(null);
      ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      NamingEnumeration<SearchResult> result = context.search(this.schema, this.parameters.get(filter) + "=" + filterValue, ctrls);
      attrs = result.nextElement().getAttributes().getAll();

      if (attrs.hasMoreElements()) {
        userAcc = new User();
        while (attrs.hasMoreElements()) {
          Attribute attribute = attrs.nextElement();
          String alias = this.parameters.get(attribute.getID());
          if (alias == null)
            continue;
          switch (alias) {
            case ALIAS_username:
              userAcc.setId(attribute.get(0).toString());
              break;
            case ALIAS_LANG:
              userAcc.setLang(attribute.get(0).toString());
              break;
            case ALIAS_EMAIL:
              userAcc.setEmail(attribute.get(0).toString());
              break;
          }
        }
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      close(context);
    }
    return userAcc;
  }

  @Override
  public User loadUser(String username) {
    return this.loadUser(this.appUser, this.appPassword, ALIAS_username, username);
  }

  @Override
  public User loadUserFromEmail(String email) {
    return this.loadUser(this.appUser, this.appPassword, ALIAS_EMAIL, email);
  }

  @Override
  public void saveUser(User account) {
    //LDAP Auth Layer doesn't support this operation
  }

  @Override
  public boolean updateUserPassword(User user, String newPassword) {
    return false;
  }

  private void close(InitialDirContext context) {
    if (context != null) {
      try {
        context.close();
      } catch (NamingException e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

  private static String toDC(String domainName) {
    StringBuilder buf = new StringBuilder();
    for (String token : domainName.split("\\.")) {
      if(token.length()==0)   continue;   // defensive check
      if(buf.length()>0)  buf.append(",");
      buf.append("DC=").append(token);
    }
    return buf.toString();
  }

}
