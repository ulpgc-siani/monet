package org.monet.space.mobile.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;


public class MonetSSLSocketFactory extends SSLSocketFactory {
  private static MonetSSLSocketFactory instance;
  
  private javax.net.ssl.SSLSocketFactory factory;
  private MonetTrustManager trustManager;

  public MonetSSLSocketFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
          super(null);
          this.trustManager = new MonetTrustManager();
          try {
                  SSLContext sslcontext = SSLContext.getInstance("TLS");
                  sslcontext.init(null, new TrustManager[] { this.trustManager }, null);
                  factory = sslcontext.getSocketFactory();
                  setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
                  
          } catch (Exception ex) {
          }
  }
  
  public void setContext(Context context) {
    this.trustManager.loadKeyStore(context);
  }

  public static SocketFactory getDefault() throws KeyManagementException,NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
          return new MonetSSLSocketFactory();
  }

  public synchronized static SSLSocketFactory getSocketFactory(Context context)  {
    if (instance == null) {
      try {
        instance = new MonetSSLSocketFactory();
        instance.setContext(context);
      } catch (Exception e) {}
    }
    return instance;
    
  }
  
  @Override
  public Socket createSocket() throws IOException {
          return factory.createSocket();
  }

  @Override
  public Socket createSocket(Socket socket, String s, int i, boolean flag) throws IOException {
          return factory.createSocket(socket, s, i, flag);
  }

  public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr1, int j) throws IOException {
          return factory.createSocket(inaddr, i, inaddr1, j);
  }

  public Socket createSocket(InetAddress inaddr, int i) throws IOException {
          return factory.createSocket(inaddr, i);
  }

  public Socket createSocket(String s, int i, InetAddress inaddr, int j) throws IOException {
          return factory.createSocket(s, i, inaddr, j);
  }

  public Socket createSocket(String s, int i) throws IOException {
          return factory.createSocket(s, i);
  }

  public String[] getDefaultCipherSuites() {
          return factory.getDefaultCipherSuites();
  }

  public String[] getSupportedCipherSuites() {
          return factory.getSupportedCipherSuites();
  }
}
