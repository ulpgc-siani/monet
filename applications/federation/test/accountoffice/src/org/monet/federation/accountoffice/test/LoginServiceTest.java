package org.monet.federation.accountoffice.test;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.federation.accountoffice.client.AccountOfficeClient;
import org.monet.federation.accountoffice.test.models.SingleSignOnApi;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

@SuppressWarnings("serial")
public class LoginServiceTest extends HttpServlet {


  public static Token requestToken;
  public static AccountOfficeClient client;
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      
      String xml = "<info><label lang=\"ES\">Esto es un etiqueta</label><label lang=\"EN\">This is a label</label><description lang=\"ES\">Esto es una descripción</description><description lang=\"EN\">This is a description</description> </info>";
      client = new AccountOfficeClient("localhost", 5348, "unit1", xml);
      client.init();
      OAuthService service = new ServiceBuilder().provider(SingleSignOnApi.class).apiKey("unit1")
      .apiSecret("1234").callback("http://localhost:8081/monet.federationservice.test/servlet/returnUrlTest/").build();
      requestToken = service.getRequestToken();
      System.out.println(requestToken.getToken());
      System.out.println(requestToken.getSecret());

      String authUrl = service.getAuthorizationUrl(requestToken);

      resp.sendRedirect(authUrl);
    } catch (Exception e) {

      e.printStackTrace();
    }

  }
}
