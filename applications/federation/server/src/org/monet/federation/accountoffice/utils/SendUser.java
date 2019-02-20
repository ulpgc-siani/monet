package org.monet.federation.accountoffice.utils;

import org.monet.federation.accountoffice.core.components.requesttokencomponent.RequestTokenComponent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendUser {
  
  public static void sendUser(HttpServletRequest request, 
                              HttpServletResponse response, 
                              RequestTokenComponent requestTokenComponent, 
                              String requestToken) throws IOException {
    
    String callback = requestTokenComponent.getUrlCallback(requestToken);
    if ((callback != null) && (!callback.equals(""))) {
      String verifier = requestTokenComponent.generateVerifier(requestToken);
      if (callback.indexOf("?") != -1)
        callback += "&oauth_token=" + requestToken + "&oauth_verifier=" + verifier;
      else
        callback += "/?oauth_token=" + requestToken + "&oauth_verifier=" + verifier;
      Utils.sendRedirect(response, callback);
    } else {
      String baseUrl = Utils.getBaseUrl(request);
      Utils.sendRedirect(response, baseUrl + "/home/");
    }
    
  }

}
