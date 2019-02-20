package org.monet.federation.accountoffice.core.layers.auth;

import org.monet.federation.accountoffice.core.model.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface AuthLayer {

    User login(HttpServletRequest request);

    boolean isValidLogin(String userId, String password);

    User loadUser(String id);

    User loadUserFromEmail(String email);

    void createUser(User user, String password);

    void saveUser(User user);

    boolean updateUserPassword(User user, String newPassword);

}
