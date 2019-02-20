package org.monet.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;


@SuppressWarnings("deprecation")
public class Certificater {

  public static enum RETURN_LOAD_PKCS12 {CERTIFICATE, PRIVATE_KEY};
  public static final String ALGORITHM_RSA = "RSA";
  public static final String SIGNATURE_ALGORITHM_SHA1_RSA = "SHA1WithRSAEncryption";

  private  static final String BEGIN_CERT     = "-----BEGIN CERTIFICATE-----";
  private  static final String END_CERT       = "-----END CERTIFICATE-----";
  
  private  static final String NEW_BEGIN_CERT_RQS     = "-----BEGIN NEW CERTIFICATE REQUEST-----";
  private  static final String NEW_END_CERT_RQS       = "-----END NEW CERTIFICATE REQUEST----";

  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  /**
   * 
   * @param algorithm Ej. RSA
   * @param keysize  Ej. 1024
   * @return
   * @throws Exception
   */
  public static KeyPair generateKeyPair(String algorithm, int keysize) throws Exception{
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
    keyGen.initialize(keysize);
    return keyGen.genKeyPair();
  }


  public static void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
    PrivateKey privateKey = keyPair.getPrivate();
    PublicKey publicKey = keyPair.getPublic();

    // Store Public Key.
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
    FileOutputStream fos = new FileOutputStream(path);
    fos.write(x509EncodedKeySpec.getEncoded());
    fos.close();

    // Store Private Key.
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
    fos = new FileOutputStream(path);
    fos.write(pkcs8EncodedKeySpec.getEncoded());
    fos.close();
  }

  
  public static KeyPair LoadKeyPair(String outPublicKeyFile,String outPrivateKeyFile, String algorithm) throws Exception {
    // Read Public Key.
    File filePublicKey = new File(outPublicKeyFile);
    FileInputStream fis = new FileInputStream(filePublicKey);
    byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
    fis.read(encodedPublicKey);
    fis.close();

    // Read Private Key.
    File filePrivateKey = new File(outPrivateKeyFile);
    fis = new FileInputStream(filePrivateKey);
    byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
    fis.read(encodedPrivateKey);
    fis.close();

    // Generate KeyPair.
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
    PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

    return new KeyPair(publicKey, privateKey);
  }


  public static void savePKCS12(String pathStorePKCS12, String certificateIndetifier, X509Certificate certificate,  PrivateKey privateKey, String password) throws Exception{
    KeyStore store = KeyStore.getInstance("PKCS12");
    store.load(null, password.toCharArray());

    store.setKeyEntry(certificateIndetifier, privateKey, password.toCharArray(), new Certificate[] { certificate });
    FileOutputStream fOut = new FileOutputStream(pathStorePKCS12);
    store.store(fOut, password.toCharArray());
  }
  
  public static void saveCertificatesInP7bFile(String outFile, PrivateKey key, Certificate certificate, List<Certificate> certificates) throws Exception{
    
    Store certs = new JcaCertStore(certificates);
    CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
    ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(key);

    gen.addSignerInfoGenerator(
              new JcaSignerInfoGeneratorBuilder(
                   new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
                   .build(sha1Signer, (X509Certificate)certificate));

    gen.addCertificates(certs);

    FileOutputStream output = new FileOutputStream(outFile);
    for (Certificate currentCertificate : certificates) {
      output.write(gen.generate(new CMSProcessableByteArray(currentCertificate.getEncoded()), false).getEncoded());
    }
    output.close();
  }


//  public static void saveCertificatesInP7bFile(String outFile, PrivateKey key, Certificate certificate, List<Certificate> certificateList) throws Exception{
//    CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
//    generator.addSigner(key, (X509Certificate) certificate, CMSSignedDataGenerator.DIGEST_SHA1);
//
//
//    CertStore certs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certificateList), "BC");
//    generator.addCertificatesAndCRLs(certs);
//
//    FileOutputStream output = new FileOutputStream(outFile);
//    for (Certificate currentCertificate : certificateList) {
//      output.write(generator.generate(new CMSProcessableByteArray(currentCertificate.getEncoded()), "BC").getEncoded());
//    }
//    output.close();
//  }

  /**
   * 
   * @param pathStorePKCS12 Path of keystore
   * @param certificateIndetifier Alias in certificate in keystore
   * @param password  Password of file and private key
   * @return Map: Key(RETURN_LOAD_PKCS12.CERTIFICATE)  = java.security.cert.Certificate 
   *              Key(RETURN_LOAD_PKCS12.PRIVATE_KEY)  = java.security.Key
   * @throws Exception
   */
  public static Map<RETURN_LOAD_PKCS12,Object> loadPKCS12(String pathStorePKCS12, String certificateIndetifier, String password) throws Exception{
    KeyStore store = KeyStore.getInstance("PKCS12");
    store.load(new FileInputStream(pathStorePKCS12), password.toCharArray());

    Certificate certificate = store.getCertificate(certificateIndetifier);
    Key key = store.getKey(certificateIndetifier, password.toCharArray());

    Map<RETURN_LOAD_PKCS12,Object> returnValues = new HashMap<RETURN_LOAD_PKCS12,Object>();
    returnValues.put(RETURN_LOAD_PKCS12.CERTIFICATE, certificate);
    returnValues.put(RETURN_LOAD_PKCS12.PRIVATE_KEY, key);

    return returnValues;
  }
  
    
  public static List<Certificate> loadCertificatesFromP7bFile(String p7bFile) throws Exception{
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Collection<? extends Certificate> col = cf.generateCertificates(new FileInputStream(p7bFile));
    return  new ArrayList<Certificate>(col);
  }


  public static X509Certificate generateCertificateV1(Date startDate, Date expiryDate, BigInteger serial, KeyPair keyPair, String signatureAlgorithm, String DNName) throws Exception{
    X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
    X500Principal              dnName = new X500Principal(DNName); //"CN=Test CA Certificate"

    certGen.setSerialNumber(serial);
    certGen.setIssuerDN(dnName);
    certGen.setNotBefore(startDate);
    certGen.setNotAfter(expiryDate);
    certGen.setSubjectDN(dnName);
    certGen.setPublicKey(keyPair.getPublic());
    certGen.setSignatureAlgorithm(signatureAlgorithm);

    return certGen.generate(keyPair.getPrivate(), "BC");
  }


  public static X509Certificate generateCertificateV3(Date startDate, Date expiryDate, BigInteger serial, PublicKey publicKey, String signatureAlgorithm,
      X509Certificate caCert, PrivateKey caKey, String SubjectName)throws Exception{

    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
    X500Principal              subjectName = new X500Principal(SubjectName); // "CN=Test V3 Certificate"

    certGen.setSerialNumber(serial);
    certGen.setIssuerDN(caCert.getSubjectX500Principal());
    certGen.setNotBefore(startDate);
    certGen.setNotAfter(expiryDate);
    certGen.setSubjectDN(subjectName);
    certGen.setPublicKey(publicKey);
    certGen.setSignatureAlgorithm(signatureAlgorithm);

    certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
    certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(publicKey));

    return certGen.generate(caKey, "BC");
  }


  public static void saveCertificateInPEMFile(File outFile, X509Certificate certificate) throws Exception{
    OutputStream outStream = new FileOutputStream(outFile);
    PEMWriter writer = new PEMWriter(new OutputStreamWriter(outStream)); 
    writer.writeObject(certificate);
    writer.close();
  }


  public static void saveX509toFile(String filename, X509Certificate cert) throws Exception {
    FileWriter output;
    output = new FileWriter(filename, false);
    output.write(getCertBase64Encoded(cert));
    output.flush();
    output.close();
  }

  public static PublicKey  getPublicKeyFromPkcs10(String csr) throws Exception{
    csr = csr.replace(NEW_BEGIN_CERT_RQS , "");
    csr = csr.replace(NEW_END_CERT_RQS, "");
    PKCS10CertificationRequest pkcs10CertificationRequest = new PKCS10CertificationRequest(Base64.decode(csr.trim()));
    return pkcs10CertificationRequest.getPublicKey();
  }
  
  public static PublicKey getPublicKeyFromSpkac(String spkac) throws Exception{
    ASN1InputStream asn1input = new ASN1InputStream(Base64.decode(spkac));
    ASN1Sequence asn1seq =  ASN1Sequence.getInstance(asn1input.readObject());
    PKCS10CertificationRequest pkcs10CertificationRequest = new PKCS10CertificationRequest(asn1seq);
    asn1input.close();
    return pkcs10CertificationRequest.getPublicKey();
  }

  public static String getCertBase64Encoded(X509Certificate cert) throws Exception {
    String sTmp = new String(Base64.encode(cert.getEncoded()));
    String sEncoded = BEGIN_CERT + "\r\n";
    for(int iCnt = 0; iCnt < sTmp.length(); iCnt += 64)
    {
      int iLineLength;
      if(iCnt + 64 > sTmp.length())
        iLineLength = sTmp.length() - iCnt;
      else
        iLineLength = 64;
      sEncoded = sEncoded + sTmp.substring(iCnt, iCnt + iLineLength) + "\r\n";
    }

    sEncoded = sEncoded + END_CERT + "\r\n";
    return sEncoded;
  }

}
