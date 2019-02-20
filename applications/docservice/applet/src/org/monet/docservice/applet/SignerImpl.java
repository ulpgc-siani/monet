package org.monet.docservice.applet;

import es.uji.security.crypto.SupportedKeystore;
import es.uji.security.keystore.IKeyStore;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.monet.docservice.applet.util.Base64;
import org.monet.docservice.applet.util.ExternalSignatureCMSSignedDataGenerator;
import org.monet.docservice.applet.util.ExternalSignatureSignerInfoGenerator;

import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class SignerImpl {

  private CustomKeyStoreManager keyStoreManager;
  private SupportedBrowser browser;
  private Logger log = Logger.getLogger(SignerImpl.class);


  private SupportedBrowser getBrowser(String userAgent) {
    if (userAgent != null)
    {
      userAgent = userAgent.toLowerCase();

      if(userAgent.contains("macintosh")) {
        return SupportedBrowser.SAFARI;
      }

      if (userAgent.contains("explorer") ||
              userAgent.contains("like gecko") ||
              userAgent.contains("msie") ||
              userAgent.contains("chrome") ||
              (userAgent.contains("safari") && userAgent.contains("windows"))) {
        return SupportedBrowser.IEXPLORER;
      }

      if (userAgent.contains("firefox") ||
              userAgent.contains("iceweasel") ||
              userAgent.contains("seamonkey") ||
              userAgent.contains("gecko") ||
              userAgent.contains("netscape")) {
        return SupportedBrowser.MOZILLA;
      }
    }
    return SupportedBrowser.OTHERS;
  }

  public void install(String userAgent) throws OperationException {
    log.debug("OS detected: " + System.getProperty("os.name"));
    log.debug("OS architecture: " + System.getProperty("os.arch"));

    String javaVersion = System.getProperty("java.version");
    log.debug("Java version: " + javaVersion);

    if (javaVersion.startsWith("1.8"))
      System.setProperty("java.version", "1.7");

    log.debug(String.format("install(%s)", userAgent));
    
    try {
      this.browser = getBrowser(userAgent);
      log.debug(String.format("browser: %s", this.browser));

      keyStoreManager = new CustomKeyStoreManager(this.browser);
      keyStoreManager.initKeyStoresTable();
      
    } catch (Exception e) {
      throw new OperationException("Install", e);
    }
  }
  
  public void destroy() {
    log.debug(String.format("destroy()"));
    this.keyStoreManager.deactivate();
  }
  
  private byte[] sign(byte[] dataToSign, String sKeyStore, String certificateAlias) throws OperationException {
    log.debug(String.format("sign(%s, %s, %s)", dataToSign, sKeyStore, certificateAlias));

    try {
      SupportedKeystore supportedKeystore = SupportedKeystore.valueOf(sKeyStore);
      IKeyStore keyStore = keyStoreManager.getKeyStore(supportedKeystore);

      X509Certificate[] chain = new X509Certificate[1];

      chain[0] = (X509Certificate) keyStore.getCertificate(certificateAlias);
      return createSignature(dataToSign, certificateAlias, keyStore, chain);
    } catch (Exception e) {
      throw new OperationException("sign", e);
    }
  }
  
  private byte[] createSignature(byte[] hash, String privateKeyAlias, IKeyStore keyStore, X509Certificate[] chain) throws OperationException {
    log.debug(String.format("createSignature(%s, %s, %s, %s)", hash, privateKeyAlias, keyStore, chain));

    try {
      ExternalSignatureCMSSignedDataGenerator generator = new ExternalSignatureCMSSignedDataGenerator();
      ExternalSignatureSignerInfoGenerator si = new ExternalSignatureSignerInfoGenerator(CMSSignedDataGenerator.DIGEST_SHA1, CMSSignedDataGenerator.ENCRYPTION_RSA);

      byte[] signedBytes = keyStore.signMessage(hash, privateKeyAlias);

      log.debug(String.format("Signed data: %s", signedBytes));

      si.setCertificate(chain[0]);
      si.setSignedBytes(signedBytes);

      generator.addSignerInf(si);
      generator.addCertificatesAndCRLs(getCertStore(chain));
      log.debug("Pre-generate Signed Data");
      CMSSignedData signedData = generator.generate(new CMSProcessableByteArray(hash), true);
      log.debug("Signed Data Generated");
      return signedData.getEncoded();
    } catch (Exception e) {
      throw new OperationException("createSignature", e);
    }
  }
    
  private CertStore getCertStore(Certificate[] certs) throws OperationException {
    log.debug(String.format("getCertStore(%s)", certs.length));

    try {
      ArrayList<Certificate> list = new ArrayList<Certificate>();
      for (int i = 0, length = certs == null ? 0 : certs.length; i < length; i++) {
        list.add(certs[i]);
      }
      return CertStore.getInstance("Collection", new CollectionCertStoreParameters(list), "BC");
    } catch (Exception e) {
      throw new OperationException("getCertStore", e);
    }
  }
  
  public String getCertificate(String sKeyStore, String alias) throws OperationException {
    log.debug(String.format("getCertificate(%s, %s)", sKeyStore, alias));
    
    try {
      SupportedKeystore supportedKeystore = SupportedKeystore.valueOf(sKeyStore);
      IKeyStore keyStore = keyStoreManager.getKeyStore(supportedKeystore);
      byte[] binaryCert = keyStore.getCertificate(alias).getEncoded();
      return Base64.encodeBytes(binaryCert);
    } catch (Exception e) {
      throw new OperationException("GetCertificate", e);
    }
  }
  
  public String signDocument(String hash, String sKeyStore, String certificateAlias) throws OperationException {
    log.debug(String.format("signDocument(%s, %s, %s)", hash, sKeyStore, certificateAlias));
    
    try {
      byte[] data = sign(Base64.decode(hash), sKeyStore, certificateAlias);
      String sData = Base64.encodeBytes(data);
      return sData;
    } catch (Exception e) {
      throw new OperationException("SignDocument", e);
    }
    
  }
  
  public String signText(String text, String sKeyStore, String certificateAlias) throws OperationException {
    log.debug(String.format("signText(%s, %s, %s)", text, sKeyStore, certificateAlias));
    
    try {
      byte[] data = sign(text.getBytes(), sKeyStore, certificateAlias);
      String sData = Base64.encodeBytes(data);
      return sData;
    } catch (Exception e) {
      throw new OperationException("SignText", e);
    }
    
  }
  
  public String getAllCertificatesAliases() throws OperationException {
    log.debug(String.format("getAllCertificatesAliases()"));
    
    try {
      StringBuilder builder = new StringBuilder();
      
      builder.append("[ ");
       
      for(IKeyStore store : keyStoreManager.keystores.values()) {
        try {
          Certificate[] userCertificates = store.getUserCertificates();
          store.load(new char[0]);
          
          if(userCertificates != null) {
            //store.getKey(alias)
      
            for(int i=0;i<userCertificates.length;i++)
            	
            	
              builder.append(String.format("{ KeyStore : '%s', SubjectDN :'%s', Alias : '%s' }, ", 
                                           scapeJSONString(store.getName().toString()), 
                                           scapeJSONString(((X509Certificate)userCertificates[i]).getSubjectDN().getName()),
                                           scapeJSONString(store.getAliasFromCertificate(userCertificates[i]))));
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      builder.append(" ]");
      return builder.toString();
    } catch (Exception e) {
      throw new OperationException("GetAllCertificates", e);
    }
    
  }
  
  private String scapeJSONString(String origin) {
    return origin.replaceAll("\\\\", "\\\\\\\\")
                 .replaceAll("'", "\\\\'")
                 .replaceAll("\\\"", "\\\\\"");
  }
  
}
