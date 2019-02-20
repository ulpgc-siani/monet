package org.monet.federation.accountoffice.test;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.monet.federation.accountoffice.test.models.SingleSignOnApi;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

@SuppressWarnings("serial")
public class ReturnLoginServiceTest extends HttpServlet {
  
  public static String accessToken;
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
  throws IOException {
    
    System.out.println(req.getParameter("oauth_token"));
    System.out.println(req.getParameter("oauth_verifier"));
    
    String verifier;
    verifier = req.getParameter("oauth_verifier");
    
    OAuthService service = new ServiceBuilder().provider(SingleSignOnApi.class).apiKey("unit1").apiSecret("1234").build();
    
    
    Verifier v = new Verifier(verifier);
    Token accessOToken = service.getAccessToken(LoginServiceTest.requestToken, v);
    accessToken = accessOToken.getToken();
    System.out.println("Access token=" +accessToken);
  }
}
