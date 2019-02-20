package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.utils.SendUser;
import org.monet.federation.accountoffice.utils.Utils;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class OpenIdCallbackServlet extends FederationServlet {
  private static final long serialVersionUID = -7273183900822340556L;

  @Override
  protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      if (!this.isFederationRunning(response))
        return;

      this.initialize(response);

      ParameterList openidResp = new ParameterList(request.getParameterMap());

      // retrieve the previously stored discovery information
      DiscoveryInformation discovered = (DiscoveryInformation) request.getSession().getAttribute("discovered");

      // extract the receiving URL from the HTTP request
      StringBuffer receivingURL = request.getRequestURL();
      String queryString = request.getQueryString();
      if (queryString != null && queryString.length() > 0)
        receivingURL.append("?").append(request.getQueryString());

      // verify the response
      ConsumerManager manager = (ConsumerManager) request.getSession().getAttribute("manager");
      VerificationResult verification;

      verification = manager.verify(receivingURL.toString(), openidResp, discovered);

      // examine the verification result and extract the verified identifier
      Identifier verified = verification.getVerifiedId();
      String lang = (String) request.getSession().getAttribute("lang");
      String requestToken = (String) request.getSession().getAttribute("requestToken");

      if (verified != null && sessionComponent.hasAvailableSessions()) {
        String user = verified.getIdentifier();
        receiveAttributeExchange((AuthSuccess) verification.getAuthResponse(), user);
        String remoteAddr = Utils.getAddress(request);
        String remoteUA = request.getHeader("User-Agent").toLowerCase();

        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("lang", lang);
        String accessToken = sessionComponent.addUser(user, remoteAddr, remoteUA, lang, false, false, "");

        this.requestTokenComponent.setAccessToken(requestToken, accessToken);

        SendUser.sendUser(request, response, requestTokenComponent, requestToken);
      } else {

        String userId = (String) request.getSession().getAttribute("user");
        String baseUrl = Utils.getBaseUrl(request);
        int tries = this.sessionComponent.getAuthTriesOfUser(userId);
        this.templateComponent.createLoginTemplate(response, true, requestToken, lang, baseUrl, tries);
      }
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
      e.printStackTrace();
    }
  }

  private void receiveAttributeExchange(AuthSuccess authSuccess, String id) throws Exception {
    if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
      MessageExtension ext = authSuccess.getExtension(AxMessage.OPENID_NS_AX);

      if (ext instanceof FetchResponse) {
        FetchResponse fetchResp = (FetchResponse) ext;

        String nickname = fetchResp.getAttributeValue("username");
        if (nickname == null)
          nickname = fetchResp.getAttributeValue("nickname");
        if (nickname == null)
          nickname = fetchResp.getAttributeValue("Friendly");

        String lang = fetchResp.getAttributeValue("language");
        if (lang == null)
          lang = fetchResp.getAttributeValue("Language");
        String email = fetchResp.getAttributeValue("email");
        if (email == null)
          email = fetchResp.getAttributeValue("Email");

        User newUser = new User();
        newUser.setUsername(id);
        newUser.setFullname(nickname);
        newUser.setEmail(email);
        newUser.setLang(lang);
        newUser.setMode(LoginMode.OPEN_ID);

        this.accountLayer.createUser(newUser, null);
      }
    }
  }
}
