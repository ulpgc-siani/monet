package org.monet.space.mobile.net;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.monet.space.mobile.events.UntrustedServerEvent;
import org.monet.space.mobile.mvp.BusProvider;

import android.content.Context;

public class MonetTrustManager implements X509TrustManager {

  private X509TrustManager origTrustmanager;

  public MonetTrustManager() {
  }  
  
  public void loadKeyStore(Context context) {
    try {
      
      KeyStore trusted = null;
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init(trusted);
      TrustManager[] trustManagers = tmf.getTrustManagers();
      this.origTrustmanager = (X509TrustManager) trustManagers[0];
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public void checkClientTrusted(X509Certificate[] cert, String authType) throws CertificateException {
    try {
      origTrustmanager.checkClientTrusted(cert, authType);
    } catch (CertificateException e) {
    }
  }

  public void checkServerTrusted(X509Certificate[] cert, String authType) throws CertificateException {
    try {
      origTrustmanager.checkServerTrusted(cert, authType);
    } catch (CertificateException e) {
      BusProvider.get().post(new UntrustedServerEvent());
    }
  }
  
  public X509Certificate[] getAcceptedIssuers() {
    return origTrustmanager.getAcceptedIssuers();
  }
}
