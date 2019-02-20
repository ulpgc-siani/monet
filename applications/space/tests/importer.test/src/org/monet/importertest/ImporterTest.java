package org.monet.importertest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.monet.services.backservice.BackserviceProxy;
import org.monet.services.backservice.Session;


public class ImporterTest {

  @Test
  public void importLugaresActuacion() throws Exception {
    BackserviceProxy proxy = new BackserviceProxy();
    try {
      Session session = proxy.openSession();
      proxy.login(session, "user", "1234");
      
      proxy.importNode(session, "importer.lugar-actuacion", load());
    } catch (Exception e) {
      throw e;
    }
  }
 
  private String load() throws IOException {
    StringBuffer oContent = new StringBuffer();
    String sLine;
    InputStream inputStream = this.getClass().getResourceAsStream("/lugares.xml");
    InputStreamReader oInput = new InputStreamReader(inputStream, "UTF-8");
    BufferedReader oBufferedReader = new BufferedReader(oInput);
    while ((sLine = oBufferedReader.readLine()) != null) {
      oContent.append(sLine);
      oContent.append("\n");
    }
    oInput.close();
    return oContent.toString();
  }
}
