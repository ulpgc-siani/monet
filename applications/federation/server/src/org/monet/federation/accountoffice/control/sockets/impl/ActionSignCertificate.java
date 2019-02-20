package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.bouncycastle.util.encoders.Base64;
import org.monet.encrypt.Certificater;
import org.monet.encrypt.Certificater.RETURN_LOAD_PKCS12;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;

public class ActionSignCertificate implements ActionAccountSocket {

  private Logger        logger;
  private Configuration configuration;

  @Inject
  public ActionSignCertificate(Logger logger, Configuration configuration) {
    this.logger = logger;
    this.configuration = configuration;
  }

  @Override
  public SocketResponseMessage execute(SocketMessageModel socketMessage) {
    SocketResponseMessage response = new SocketResponseMessage();

    String cn = "emailAddress=%" + socketMessage.getSignCertificate().getEmail().getText();
    String publicKeyString = socketMessage.getSignCertificate().getPublicKey().getText();
    try {
      KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKeyString));
      PublicKey publicKey = rsaKeyFac.generatePublic(keySpec);
      X509Certificate certificate = registerWithCertificate(publicKey, cn);

      response.setResponse(Certificater.getCertBase64Encoded(certificate));
    } catch (Exception e) {
      response.setIsError(true);
      response.setResponse(SocketResponseMessage.RESPONSE_ERROR_UNKNOW);
      logger.error(e.getMessage(), e);
    }

    return response;
  }

  private X509Certificate registerWithCertificate(PublicKey publicKey, String cn) {

    String caPath = configuration.getCertificateAuthorityPath();
    String password = configuration.getCertificateAuthorityPassword();

    try {
      Map<RETURN_LOAD_PKCS12, Object> caAndPrivateKey = Certificater.loadPKCS12(caPath, Configuration.CERTIFICATE_AUTHORITY_IDENTIFIER, password);
      Certificate caCert = (Certificate) caAndPrivateKey.get(Certificater.RETURN_LOAD_PKCS12.CERTIFICATE);
      Key caPrivateKey = (Key) caAndPrivateKey.get(Certificater.RETURN_LOAD_PKCS12.PRIVATE_KEY);
      Date startDate = new Date(System.currentTimeMillis());
      Date expiryDate = new Date(System.currentTimeMillis() + 31536000000L);
      BigInteger serial = new BigInteger(String.valueOf(System.currentTimeMillis()));

      X509Certificate cert = Certificater.generateCertificateV3(startDate, expiryDate, serial, publicKey, Certificater.SIGNATURE_ALGORITHM_SHA1_RSA, (X509Certificate) caCert, (PrivateKey) caPrivateKey, cn);
      return cert;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return null;
  }

}
