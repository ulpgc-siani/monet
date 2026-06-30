package org.monet.federation.accountoffice.core.components.totpcomponent;

import org.monet.federation.accountoffice.core.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface TotpComponent {

  enum VerificationStatus {
    SUCCESS,
    INVALID,
    REUSED,
    EXPIRED,
    NOT_FOUND
  }

  final class PendingChallenge {
    private final String requestToken;
    private final String secret;
    private final String otpAuthUri;
    private final boolean setupMode;

    public PendingChallenge(String requestToken, String secret, String otpAuthUri, boolean setupMode) {
      this.requestToken = requestToken;
      this.secret = secret;
      this.otpAuthUri = otpAuthUri;
      this.setupMode = setupMode;
    }

    public String getRequestToken() {
      return requestToken;
    }

    public String getSecret() {
      return secret;
    }

    public String getOtpAuthUri() {
      return otpAuthUri;
    }

    public boolean isSetupMode() {
      return setupMode;
    }
  }

  boolean isActive();

  boolean hasPendingChallenge(HttpSession session);

  PendingChallenge getPendingChallenge(HttpSession session);

  PendingChallenge beginChallenge(HttpServletRequest request, User user, String requestToken, boolean rememberMe);

  VerificationStatus verifyChallenge(HttpServletRequest request);

  void clearPendingChallenge(HttpSession session);
}
