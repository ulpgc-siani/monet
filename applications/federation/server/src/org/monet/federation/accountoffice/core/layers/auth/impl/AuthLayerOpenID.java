package org.monet.federation.accountoffice.core.layers.auth.impl;

import com.google.inject.Inject;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.auth.AuthLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.utils.Utils;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ax.FetchRequest;

import javax.servlet.http.HttpServletRequest;

public class AuthLayerOpenID extends FederationAuthLayer implements AuthLayer {

    private Logger logger;
    private Configuration configuration;

    @Inject
    public AuthLayerOpenID(Logger logger, Configuration configuration) {
        this.logger = logger;
        this.configuration = configuration;
    }

    @Override
    public User login(HttpServletRequest request) {
        this.logger.debug("login(%s)", request);

        try {
            String requestToken = StringEscapeUtils.escapeHtml(requestParam(request, "oauth_token"));
            String server = StringEscapeUtils.escapeHtml(requestParam(request, "server"));

            request.getSession().setAttribute("requestToken", requestToken);

            ConsumerManager manager = new ConsumerManager();
            String returnURL = Utils.getBaseUrl(request) + "/accounts/authorization/callback/openid/";

            DiscoveryInformation discovered = manager.associate(manager.discover(server));
            request.getSession().setAttribute("discovered", discovered);
            request.getSession().setAttribute("manager", manager);

            AuthRequest authRequest = manager.authenticate(discovered, returnURL);
            addAttributeExchangeToAuthRequest(authRequest);
            return new User(authRequest.getDestinationUrl(true));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException("Error login OpenID request: " + e.getMessage(), e);
        }
    }

    private void addAttributeExchangeToAuthRequest(AuthRequest authRequest) throws Exception {
        this.logger.debug("addAttributeExchangeToAuthRequest(%s)", authRequest);

        FetchRequest fetch = FetchRequest.createFetchRequest();

        fetch.addAttribute("Friendly", "http://axschema.org/namePerson/first", true);
        fetch.addAttribute("Language", "http://axschema.org/pref/language", true);
        fetch.addAttribute("Email", "http://axschema.org/contact/email", true);
        fetch.addAttribute("Alias", "http://axschema.org/namePerson/friendly", true);
        fetch.addAttribute("Prefix", "http://axschema.org/namePerson/prefix", true);

        authRequest.addExtension(fetch);
    }

    @Override
    public User loadUser(String userId) {
        throw new NotImplementedException("loadUser() not implemented in OpenID Auth");
    }

    @Override
    public User loadUserFromEmail(String email) {
        throw new NotImplementedException("loadUserByEmail() not implemented in OpenID Auth");
    }

    @Override
    public void createUser(User user, String password) {
        //createUser() not implemented in OpenID Auth
    }

    @Override
    public void saveUser(User user) {
        //updateUser() not implemented in OpenID Auth
    }

    @Override
    public boolean updateUserPassword(User user, String newPassword) {
        return false;
    }

    @Override
    public boolean isValidLogin(String userId, String password) {
        throw new NotImplementedException("isValidLogin() not implemented in OpenID Auth");
    }

}
