package org.monet.docservice.applet;

import java.applet.Applet;
import java.security.Security;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;

import org.apache.log4j.PropertyConfigurator;

public class Signer extends Applet {
 
  private static final long serialVersionUID = 914149438831352669L;
  private static final int INIT_OPERATION = 0;
  private static final int GET_ALL_CERTIFICATE_OPERATION = 1;
  private static final int GET_CERTIFICATE_OPERATION = 2;
  private static final int SIGN_DOCUMENT_OPERATION = 3;
  private static final int SIGN_TEXT_OPERATION = 4;
  
  private Semaphore sincro;
  private Semaphore startThread;
  private Semaphore waitThread;
  private SignerImpl signerImpl;
  private int currentOperation;
  private String result;
  private String sKeyStore;
  private String alias;
  private String hash;
  private String text;
  private String certificateAlias;
  private String userAgent;
  
  Thread initializer = new Thread(new Runnable(){

    public void run() {
      String currentOperationTxt = null;
      while(true) {
        try {
          startThread.acquire();
          
          switch (currentOperation) {
            case INIT_OPERATION:
              currentOperationTxt = "INIT_OPERATION";
              signerImpl.install(userAgent);
              break;
            case GET_ALL_CERTIFICATE_OPERATION:
              currentOperationTxt = "GET_ALL_CERTIFICATE_OPERATION";
              result = signerImpl.getAllCertificatesAliases();
              break;
            case GET_CERTIFICATE_OPERATION:
              currentOperationTxt = "GET_CERTIFICATE_OPERATION";
              result = signerImpl.getCertificate(sKeyStore, alias);
              break;
            case SIGN_DOCUMENT_OPERATION:
              currentOperationTxt = "SIGN_DOCUMENT_OPERATION";
              result = signerImpl.signDocument(hash, sKeyStore, certificateAlias);
              break;
            case SIGN_TEXT_OPERATION:
              currentOperationTxt = "SIGN_TEXT_OPERATION";
              result = signerImpl.signText(text, sKeyStore, certificateAlias);
              break;
            default:
              //NOOP
              break;
          }
        } catch (Exception e) {
          Logger.getLogger(SignerImpl.class).error(String.format("Error executing operation %s", currentOperationTxt), e);
          result = String.format("{ isError: true, message: \"%s\"}", e.getMessage());
        } finally {
          waitThread.release();
        }
      }
    }
    
  });  
  
  public Signer() {
    PropertyConfigurator.configure(this.getClass().getResource("/log4j.properties"));
    
    signerImpl = new SignerImpl();
    sincro = new Semaphore(1);
    startThread = new Semaphore(0);
    waitThread = new Semaphore(0);
    initializer.start();
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }
  
  @Override
  public void destroy() {
    super.destroy();
    signerImpl.destroy();
  }
  
  public void init(String userAgent) throws InterruptedException {
    try {
      this.sincro.acquire();
      this.userAgent = userAgent;
      this.currentOperation = INIT_OPERATION;
      startThread.release();
      waitThread.acquire();
    } finally {
      this.sincro.release();
    }
  }

  public String getCertificate(String sKeyStore, String alias) throws InterruptedException {
    String localResult;
    try {
      this.sincro.acquire();
      this.sKeyStore = sKeyStore;
      this.alias = alias;
      this.currentOperation = GET_CERTIFICATE_OPERATION;
      startThread.release();
      waitThread.acquire();
    } finally {
      localResult = result;
      this.sincro.release();
    }
    return localResult;
  }
  
  public String signDocument(String hash, String sKeyStore, String certificateAlias) throws InterruptedException {
    String localResult;
    try {
      this.sincro.acquire();
      this.hash = hash;
      this.sKeyStore = sKeyStore;
      this.certificateAlias = certificateAlias;
      this.currentOperation = SIGN_DOCUMENT_OPERATION;
      startThread.release();
      waitThread.acquire();
    } finally {
      localResult = result;
      this.sincro.release();
    }
    return localResult;
  }
  
  public String signText(String text, String sKeyStore, String certificateAlias) throws InterruptedException {
    String localResult;
    try {
      this.sincro.acquire();
      this.text = text;
      this.sKeyStore = sKeyStore;
      this.certificateAlias = certificateAlias;
      this.currentOperation = SIGN_TEXT_OPERATION;
      startThread.release();
      waitThread.acquire();
    } finally {
      localResult = result;
      this.sincro.release();
    }
    return localResult;
  }
  
  public String getAllCertificatesAliases() throws InterruptedException {
    String localResult;
    try {
      this.sincro.acquire();
      this.currentOperation = GET_ALL_CERTIFICATE_OPERATION;
      startThread.release();
      waitThread.acquire();
    } finally {
      localResult = result;
      this.sincro.release();
    }
    return localResult;
  }
  
}
