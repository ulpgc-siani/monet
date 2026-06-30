package org.monet.federation.accountoffice.core.components.totpcomponent.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.components.totpcomponent.TotpComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.core.model.UserTotpInfo;
import org.monet.federation.accountoffice.utils.TotpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class TotpComponentImpl implements TotpComponent {
  private static final String ATTR_PENDING_TOTP = "totp.pending.challenge";

  private final Logger logger;
  private final Configuration configuration;
  private final DataRepository dataRepository;

  @Inject
  public TotpComponentImpl(Logger logger, Configuration configuration, DataRepository dataRepository) {
    this.logger = logger;
    this.configuration = configuration;
    this.dataRepository = dataRepository;
  }

  @Override
  public boolean isActive() {
    return this.configuration.isPasswordTotpActive();
  }

  @Override
  public boolean hasPendingChallenge(HttpSession session) {
    return this.getState(session) != null;
  }

  @Override
  public PendingChallenge getPendingChallenge(HttpSession session) {
    State state = this.getState(session);
    if (state == null)
      return null;
    return new PendingChallenge(state.requestToken, state.secret, state.otpAuthUri, state.setupMode);
  }

  @Override
  public PendingChallenge beginChallenge(HttpServletRequest request, User user, String requestToken, boolean rememberMe) {
    HttpSession session = request.getSession();
    UserTotpInfo userTotpInfo = this.dataRepository.loadUserTotpInfo(user.getId());
    boolean setupMode = userTotpInfo == null || !userTotpInfo.isEnabled() || userTotpInfo.getSecret() == null || userTotpInfo.getSecret().isEmpty();
    String secret = setupMode ? TotpUtils.generateSecret() : userTotpInfo.getSecret();
    String otpAuthUri = TotpUtils.buildOtpAuthUri(this.configuration.getPasswordTotpIssuer(), user.getUsername(), secret, this.configuration.getPasswordTotpDigits(), this.configuration.getPasswordTotpPeriod());

    State state = new State();
    state.userId = user.getId();
    state.username = user.getUsername();
    state.requestToken = requestToken;
    state.rememberMe = rememberMe;
    state.setupMode = setupMode;
    state.secret = secret;
    state.otpAuthUri = otpAuthUri;
    state.attempts = 0;

    session.setAttribute(ATTR_PENDING_TOTP, state);
    return new PendingChallenge(requestToken, secret, otpAuthUri, setupMode);
  }

  @Override
  public VerificationStatus verifyChallenge(HttpServletRequest request) {
    State state = this.getState(request.getSession());
    if (state == null)
      return VerificationStatus.NOT_FOUND;

    String code = request.getParameter("totpCode");
    if (code == null || code.trim().isEmpty())
      return VerificationStatus.INVALID;

    UserTotpInfo userTotpInfo = this.dataRepository.loadUserTotpInfo(state.userId);
    Long lastUsedCounter = userTotpInfo != null ? userTotpInfo.getLastUsedCounter() : null;
    Long validatedCounter = TotpUtils.validateCode(state.secret, code, this.configuration.getPasswordTotpDigits(), this.configuration.getPasswordTotpPeriod(), this.configuration.getPasswordTotpValidationWindow(), lastUsedCounter);
    if (validatedCounter == null) {
      state.attempts += 1;
      if (state.attempts >= this.configuration.getPasswordTotpMaxAttempts()) {
        this.clearPendingChallenge(request.getSession());
        return VerificationStatus.EXPIRED;
      }
      return VerificationStatus.INVALID;
    }

    if (validatedCounter.longValue() < 0L)
      return VerificationStatus.REUSED;

    if (userTotpInfo == null)
      userTotpInfo = new UserTotpInfo();

    userTotpInfo.setUserId(state.userId);
    userTotpInfo.setSecret(state.secret);
    userTotpInfo.setEnabled(true);
    userTotpInfo.setLastUsedCounter(validatedCounter);

    if (this.dataRepository.existsUserTotpInfo(state.userId))
      this.dataRepository.saveUserTotpInfo(userTotpInfo);
    else
      this.dataRepository.createUserTotpInfo(userTotpInfo);

    this.logger.info("TOTP verification completed for user %s", state.username);
    return VerificationStatus.SUCCESS;
  }

  @Override
  public void clearPendingChallenge(HttpSession session) {
    if (session != null)
      session.removeAttribute(ATTR_PENDING_TOTP);
  }

  public String getPendingUserId(HttpSession session) {
    State state = this.getState(session);
    return state != null ? state.userId : null;
  }

  public boolean getPendingRememberMe(HttpSession session) {
    State state = this.getState(session);
    return state != null && state.rememberMe;
  }

  private State getState(HttpSession session) {
    if (session == null)
      return null;
    return (State) session.getAttribute(ATTR_PENDING_TOTP);
  }

  private static class State implements Serializable {
    String userId;
    String username;
    String requestToken;
    boolean rememberMe;
    boolean setupMode;
    String secret;
    String otpAuthUri;
    int attempts;
  }
}
