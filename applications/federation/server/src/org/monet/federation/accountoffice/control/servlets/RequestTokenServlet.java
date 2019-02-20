package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.model.OAuth;
import org.monet.federation.accountoffice.utils.SignatureService;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class  RequestTokenServlet extends FederationServlet {
    private static final long serialVersionUID = -8823454050042995367L;

    @Override
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (!isFederationRunning(response))
                return;

            initialize(response);

            String header = request.getHeader("authorization").substring(6);
            Map<String, String> authorizationElements = extractAuthElements(new StringBuilder(header));
            String clientSecret = businessUnitComponent.getBusinessUnitSecret(authorizationElements.get(OAuth.OAUTH_CONSUMER_KEY));

            if (!Utils.containsHeader(request, "X-Forwarded-Proto"))
                logger.warn("X-Forwarded-Proto header not found");

            String url = Utils.getRequestURL(request);
            boolean check = SignatureService.checkSignature(url, authorizationElements, clientSecret, "");

            String oauth_token = "";
            String oauth_token_secret = "";
            if (check) {
                oauth_token = requestTokenComponent.getNewToken(URLDecoder.decode(authorizationElements.get(OAuth.OAUTH_CALLBACK), "UTF-8"), URLDecoder.decode(authorizationElements.get(OAuth.OAUTH_CONSUMER_KEY), "UTF-8"));
                oauth_token_secret = requestTokenComponent.getSecret(oauth_token);
            }
            response.setContentType("text/plain");
            OutputStream out = response.getOutputStream();
            out.write(("oauth_token=" + oauth_token + "&oauth_token_secret=" + oauth_token_secret + "&oauth_callback_confirmed=true").getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Map<String, String> extractAuthElements(StringBuilder sbOAuthRequest) {
        Map<String, String> elements = new HashMap<>();
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
