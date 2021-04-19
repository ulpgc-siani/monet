package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.model.OAuth;
import org.monet.federation.accountoffice.utils.SignatureService;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class AccessTokenServlet extends FederationServlet {
    private static final long serialVersionUID = -3288173277189127810L;

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (!this.isFederationRunning(response))
            return;

        this.initialize(response);

        String header = request.getHeader("authorization").substring(6);
        Map<String, String> authorizationElements = extractAuthElements(new StringBuilder(header));
        String requestToken = authorizationElements.get(OAuth.OAUTH_TOKEN);
        String verifier = authorizationElements.get(OAuth.OAUTH_VERIFIER);
        String clientSecret = this.businessUnitComponent.getBusinessUnitSecret(authorizationElements.get(OAuth.OAUTH_CONSUMER_KEY));

        if (!Utils.containsHeader(request, "X-Forwarded-Proto"))
            logger.warn("X-Forwarded-Proto header not found");

        String url = Utils.getRequestURL(request);
        boolean check = SignatureService.checkSignature(url, authorizationElements, clientSecret, this.requestTokenComponent.getSecret(requestToken));

        String accessToken = null;
        if (check)
            accessToken = this.requestTokenComponent.getAccessToken(requestToken, verifier);

        String secret = "";
        if (accessToken != null) {
            secret = this.requestTokenComponent.getSecret(requestToken);
            this.requestTokenComponent.deleteToken(requestToken);

            response.setContentType("text/plain");
            OutputStream out = response.getOutputStream();
            out.write(("oauth_token=" + accessToken + "&oauth_token_secret=" + secret).getBytes());
        }
    }

    private Map<String, String> extractAuthElements(StringBuilder sbOAuthRequest) {
        logger.debug("extractAuthElements(%s)", sbOAuthRequest.toString());

        Map<String, String> elements = new HashMap<String, String>();
        while (!sbOAuthRequest.toString().equals("")) {
            int commaIndex = sbOAuthRequest.indexOf(",");
            if (commaIndex == -1)
                commaIndex = sbOAuthRequest.length();

            String element = sbOAuthRequest.substring(0, commaIndex);

            int equalsIndex = element.indexOf("=");
            if (equalsIndex == -1)
                equalsIndex = element.length();

            String key = element.substring(0, equalsIndex).trim();
            String value = element.substring(equalsIndex + 2, element.length() - 1);

            sbOAuthRequest.delete(0, commaIndex + 1);

            elements.put(key, value);
        }
        return elements;
    }


}
