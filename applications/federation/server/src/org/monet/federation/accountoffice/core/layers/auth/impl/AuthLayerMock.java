package org.monet.federation.accountoffice.core.layers.auth.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.layers.auth.AuthLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;

import javax.servlet.http.HttpServletRequest;

public class AuthLayerMock extends FederationAuthLayer implements AuthLayer {

    private Logger logger;

    @Inject
    public AuthLayerMock(Logger logger) {
        this.logger = logger;
    }

    private User createMockUser(String userId) {
        return this.createMockUser(userId, userId + "@mock.com");
    }

    private User createMockUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setFullname(username);
        user.setEmail(email);
        user.setLang(null);
        user.setMode(LoginMode.PASSWORD);
        return user;
    }

    @Override
    public User login(HttpServletRequest request) {
        logger.debug("login(%s)", request);

        String user = requestParam(request, "username");
        return this.createMockUser(user);
    }

    @Override
    public User loadUser(String userId) {
        logger.debug("loadUser(%s)", userId);

        return this.createMockUser(userId);
    }

    @Override
    public User loadUserFromEmail(String email) {
        logger.debug("loadUserByEmail(%s)", email);

        return this.createMockUser(email, email);
    }

    @Override
    public void createUser(User user, String password) {
        logger.debug("updateUser(%s)", user);

        //createUser() not implemented in Certificate Auth
    }

    @Override
    public void saveUser(User user) {
        logger.debug("updateUser(%s)", user);

        //updateUser() not implemented in Certificate Auth
    }

    @Override
    public boolean updateUserPassword(User user, String newPassword) {
        logger.debug("updateUserPassword(%s, %s)", user.getId(), "****");

        return true;
    }

    @Override
    public boolean isValidLogin(String userId, String password) {
        logger.debug("isValidLogin(%s, %s)", userId, "****");

        return true;
    }

}
