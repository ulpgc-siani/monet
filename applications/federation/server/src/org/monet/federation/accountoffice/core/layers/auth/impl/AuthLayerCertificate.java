package org.monet.federation.accountoffice.core.layers.auth.impl;

import com.google.inject.Inject;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.federation.accountoffice.core.components.certificatecomponent.CertificateComponent;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.layers.auth.AuthLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;

import javax.servlet.http.HttpServletRequest;
import java.security.Security;

public class AuthLayerCertificate implements AuthLayer {
    private Logger logger;
    private CertificateComponent verificationComponent;
    private DataRepository dataRepository;

    @Inject
    public AuthLayerCertificate(Logger logger, CertificateComponent verificationComponent, DataRepository dataRepository) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        this.logger = logger;
        this.verificationComponent = verificationComponent;
        this.dataRepository = dataRepository;
    }

    @Override
    public User login(HttpServletRequest request) {
        logger.debug("login(%s)", request);

        String signature = StringEscapeUtils.escapeHtml(request.getParameter("signature"));
        signature = signature.replace("%2B", "+").replace("%24", "$").replace("%26", "&")
                .replace("%2C", ",").replace("%3A", ":").replace("%3B", ";")
                .replace("%3D", "=").replace("%3F", "@");
        String timeSign = StringEscapeUtils.escapeHtml(request.getParameter("timeSign"));
        String queryString = "&mode=certificate&timeSign=" + timeSign;
        String lang = (String) request.getSession().getAttribute("lang");

        User user = verificationComponent.getUserFromSignature(queryString, signature, true);
        user.setId(this.dataRepository.generateIdForUser(user));
        if (user != null)
            user.setMode(LoginMode.CERTIFICATE);
        user.setLang(lang != null ? lang : "en");
        return user;
    }

    @Override
    public User loadUser(String id) {
        throw new NotImplementedException("loadUser() not implemented in Certificate Auth");
    }

    @Override
    public User loadUserFromEmail(String email) {
        throw new NotImplementedException("loadUserByEmail() not implemented in Certificate Auth");
    }

    @Override
    public void createUser(User user, String password) {
        //createUser() not implemented in Certificate Auth
    }

    @Override
    public void saveUser(User user) {
        //updateUser() not implemented in Certificate Auth
    }

    @Override
    public boolean updateUserPassword(User user, String newPassword) {
        return false;
    }

    @Override
    public boolean isValidLogin(String userId, String password) {
        throw new NotImplementedException("isValidLogin() not implemented in Certificate Auth");
    }

}
