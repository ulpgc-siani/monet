package org.monet.encrypt;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

public class CertificateVerifier {

  public static enum TYPE_PKCS {PKCS7, PKCS12};
  
  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  /**
   * Get the original content from signature
   * @param signedBytes
   * @return
   * @throws Exception
   */
  public static String getOriginalContentFromSignature(byte[] signedBytes) throws Exception{
    CMSSignedData s = new CMSSignedData(signedBytes);
    CMSProcessable signedContent = s.getSignedContent() ;
    return new String((byte[]) signedContent.getContent());
  }
  
  /**
   * Get certificate from bytes
   * @param signedBytes
   * @return
   * @throws Exception
   */
  public static X509Certificate getCertificateFromBytes(byte[] certificateBytes) throws Exception{
    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
    InputStream in = new ByteArrayInputStream(certificateBytes);
    return (X509Certificate)certFactory.generateCertificate(in);
  }

  /**
   * Get certificate from signature
   * @param signedBytes
   * @return
   * @throws Exception
   */
  @SuppressWarnings({ "deprecation", "rawtypes" })
  public static X509Certificate getCertificateFromSignature(byte[] signedBytes) throws Exception{
      CMSSignedData s = new CMSSignedData(signedBytes);
      CertStore certs = s.getCertificatesAndCRLs("Collection", "BC");
      SignerInformationStore signers = s.getSignerInfos();

      for (Iterator i = signers.getSigners().iterator(); i.hasNext(); ) {
        SignerInformation signer = (SignerInformation) i.next();
        Collection<? extends Certificate> certCollection = certs.getCertificates(signer.getSID());
        if (!certCollection.isEmpty()) {
          X509Certificate cert = (X509Certificate) certCollection.iterator().next();

          if (signer.verify(cert.getPublicKey(), "BC"))  return  cert;
        }
      }
    
      return null;
  }

  /**
   * Get Certificate chain from signature
   * @param signature
   * @return
   */
  @SuppressWarnings({ "resource", "unchecked", "rawtypes" })
  public static CertPath getCertificatePathFromSignature(byte[] signature) {
    CertPath certPath = null;
    
    try{
      java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");

      // Get ContentInfo
      //byte[] signature = ... // PKCS#7 signature bytes
      InputStream signatureIn = new ByteArrayInputStream(signature);
      DERObject obj = new ASN1InputStream(signatureIn).readObject();
      ContentInfo contentInfo = ContentInfo.getInstance(obj);

      // Extract certificates
      SignedData signedData = SignedData.getInstance(contentInfo.getContent());
      Enumeration certificates = signedData.getCertificates().getObjects();

      // Build certificate path
      List certList = new ArrayList();
      while (certificates.hasMoreElements()) {
        DERObject certObj = (DERObject) certificates.nextElement();
        InputStream in = new ByteArrayInputStream(certObj.getDEREncoded());
        certList.add(cf.generateCertificate(in));
      }
      certPath = cf.generateCertPath(certList);
    }
    catch (Exception e) {
      e.printStackTrace();
      return certPath;
    }
    
    return certPath;
  }

  /**
   * Check root Certificate for signature
   * @param signature
   * @param storeCertFilePath File with root certificates 
   * @return
   */
  @SuppressWarnings({ "resource", "unchecked", "rawtypes" })
  public static boolean checkRootCertificate(byte[] signature, String storeCertFilePath, TYPE_PKCS typePKCSFile, String password, String certificateIndentifier){
    try{
      java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");

      // Get ContentInfo
      //byte[] signature = ... // PKCS#7 signature bytes
      InputStream signatureIn = new ByteArrayInputStream(signature);
      DERObject obj = new ASN1InputStream(signatureIn).readObject();
      ContentInfo contentInfo = ContentInfo.getInstance(obj);

      // Extract certificates
      SignedData signedData = SignedData.getInstance(contentInfo.getContent());
      Enumeration certificates = signedData.getCertificates().getObjects();

      // Build certificate path
      List certList = new ArrayList();
      while (certificates.hasMoreElements()) {
        DERObject certObj = (DERObject) certificates.nextElement();
        InputStream in = new ByteArrayInputStream(certObj.getDEREncoded());
        certList.add(cf.generateCertificate(in));
      }
      CertPath certPath = cf.generateCertPath(certList);

      KeyStore keyStore;
      if(typePKCSFile == TYPE_PKCS.PKCS7){
    	InputStream stream = null;
    	Collection col;
    	try {  
            stream = new FileInputStream(storeCertFilePath);
    		col = cf.generateCertificates(stream);
    	}
    	finally {
    		if (stream != null) stream.close();
    	}

        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        for (Iterator it = col.iterator(); it.hasNext();) {
          X509Certificate cert = (X509Certificate)it.next();
          keyStore.setCertificateEntry(cert.getSerialNumber().toString(Character.MAX_RADIX), cert);
        }
      }
      else{
    	InputStream stream = null;
    	try {    		
    		stream = new FileInputStream(storeCertFilePath);
	        keyStore = KeyStore.getInstance("PKCS12");
	        keyStore.load(stream, null);
    	}
    	finally {
    		if (stream != null) stream.close();
    	}
      }
      // Set validation parameters
      PKIXParameters params = new PKIXParameters(keyStore);
      params.setRevocationEnabled(false); // to avoid exception on empty CRL
      
      // Validate certificate path
      CertPathValidator validator = CertPathValidator.getInstance("PKIX");
      validator.validate(certPath, params);
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;

  }

}
