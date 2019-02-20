package org.monet.federation.accountoffice.test;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.cert.Certificate;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.federation.accountoffice.client.AccountOfficeClient;
import org.monet.federation.accountoffice.client.models.FederationAccount;

@SuppressWarnings("serial")
public class TestWSLoginService extends HttpServlet {
  
  @SuppressWarnings("unused")
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
  throws IOException {
    AccountOfficeClient client = null;
    try {
      client = LoginServiceTest.client;

      String token = ReturnLoginServiceTest.accessToken;
      
      boolean flag = client.isLogged(token,req);
      if(flag) System.out.println("User online");
      else System.out.println("User offline");
      
      
      
      FederationAccount fAccount =client.getAccount(token, req);
      
      List<String> roles = fAccount.getRoles();
      List<String> aa = client.checkRoles(token, roles);
      
      
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      keyGen.initialize(1024);
      Certificate cert = client.certificateSigningRequest(keyGen.genKeyPair().getPublic(), "fabio@gmail.com");
      
      
      flag = client.logout(token,req);
      if(flag) System.out.println("User logout");
      
      client.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
//   String token = ReturnLoginServiceTest.token;
//   
//   AccountserviceProxy proxy = new AccountserviceProxy("http://localhost:8080/monet.loginservice/servlet/accountservice");
//   System.out.println("Yo estoy conectado? = " + proxy.isLogin(token));
  }
}
