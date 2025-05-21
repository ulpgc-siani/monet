package org.monet.federation.accountoffice.core.components.certificatecomponent.impl;

import com.google.inject.Inject;
import org.apache.commons.codec.binary.Base64;
import org.monet.encrypt.CertificateVerifier;
import org.monet.encrypt.CertificateVerifier.TYPE_PKCS;
import org.monet.encrypt.extractor.CertificateExtractor;
import org.monet.encrypt.extractor.ExtractorUser;
import org.monet.federation.accountoffice.core.components.certificatecomponent.CertificateComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.BusinessUnit;
import org.monet.federation.accountoffice.core.model.Federation;
import org.monet.federation.accountoffice.core.model.MonetCertificateInfo;
import org.monet.federation.accountoffice.core.model.User;

import java.security.Security;
import java.security.cert.X509Certificate;

public class CertificateComponentImpl implements CertificateComponent {
  private Logger               logger;
  private Configuration        configuration;
  private CertificateExtractor certificateExtractor;
  private DataRepository       dataRepository;

  @Inject
  public CertificateComponentImpl(Logger logger, Configuration configuration, CertificateExtractor certificateExtractor, DataRepository dataRepository) {
    this.logger = logger;
    this.configuration = configuration;
    this.certificateExtractor = certificateExtractor;
    this.dataRepository = dataRepository;

    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  private X509Certificate getCertificateFromSignature(String signature) {
    byte[] signatureBytes = Base64.decodeBase64(signature);

    X509Certificate cert = null;
    try {
      cert = CertificateVerifier.getCertificateFromSignature(signatureBytes);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    if (cert == null) {
      logger.error("Can't get certificate from signature");
      throw new RuntimeException(ERROR_EXTRACT_CERTIFICATE_FROM_SIGNATURE);
    }

    return cert;
  }

  private boolean checkSignature(String queryString, String signature, boolean verifyTimestamp) {
    byte[] signatureBytes = Base64.decodeBase64(signature);
    String originalContent = null;

    try {
      originalContent = CertificateVerifier.getOriginalContentFromSignature(signatureBytes);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    if (originalContent == null) {
      logger.error("Can't get original content from signature");
      throw new RuntimeException(ERROR_EXTRACT_ORIGINAL_CONTENT_FROM_SIGNATURE);
    }

    if (!QueryStringComparator.equals(queryString, originalContent)) {
      logger.error("sended queryString != signed content");
      throw new RuntimeException(ERROR_INVALID_HASH);
    }

    if (verifyTimestamp) {
      int indexTime = originalContent.indexOf("&timeSign=");
      long iTime = 0;
      if (indexTime > -1)
        iTime = Long.parseLong(originalContent.substring(indexTime + 10));

      long diff = System.currentTimeMillis() - iTime;
      if (diff > configuration.getSignTimestampInterval()) {
        logger.error("Timestamp don't found or too old");
        throw new RuntimeException(ERROR_VERIFYING_TIMESTAMP);
      }
    }

    String caPath = this.configuration.getP7B();
    String caPassword = this.configuration.getCertificateAuthorityPassword();
    String caIdentificator = this.configuration.getCertificateAuthorityIdentifier();

    return CertificateVerifier.checkRootCertificate(signatureBytes, caPath, TYPE_PKCS.PKCS7, caPassword, caIdentificator);
  }

  private void logError(String error) {
    logger.error(error);
    System.out.println(error);
  }

  @Override
  public boolean checkRootCertificate(String hash, String signature, boolean verifyTimestamp) {

    if (!this.checkSignature(hash, signature, verifyTimestamp)) {
      logger.error("No Root Certificate for this certificate");
      return false;
    }

    return true;
  }

  @Override
  public boolean isValidCertificate(String hash, String signature, boolean verifyTimestamp) {
    X509Certificate certificate = this.getCertificateFromSignature(signature);

    if (!this.checkSignature(hash, signature, verifyTimestamp)) {
      logger.error("No Root Certificate for this certificate");
      throw new RuntimeException(ERROR_UNKNOWN_ROOT_CERTIFICATE);
    }

    org.monet.encrypt.extractor.MonetCertificateInfo extractorInfo = this.certificateExtractor.extractMonetInfo(certificate);
    if (extractorInfo == null) {
      this.logError("No Monet certificate found in signature");
      return false;
    }

    MonetCertificateInfo info = new MonetCertificateInfo();
    info.fromExtractor(extractorInfo);

    if (info.getFederation() == null) {
      this.logError("No Federation Certificate found in signature");
      return false;
    }

    if (info.getBusinessUnit() == null) {
      this.logError("No Business Unit Certificate found in signature");
      return false;
    }

    String federationName = info.getFederation();
    if (!this.dataRepository.existsFederation(federationName)) {
      this.logError(federationName + " is not registered in this federation. Check configuration file and friend federations.");
      return false;
    }

    Federation federation = this.dataRepository.loadFederation(federationName);
    if (!federation.isTrusted()) {
      this.logError(federationName + " is not a trust federation");
      return false;
    }

    String businessUnitName = info.getBusinessUnit();
    if (this.dataRepository.existsBusinessUnit(federationName, businessUnitName)) {
      BusinessUnit businessUnit = this.dataRepository.loadBusinessUnit(federationName, businessUnitName);
      if (!businessUnit.isPartner() && !businessUnit.isMember()) {
        this.logError(businessUnitName + " is not a partner neither member business unit in this federation");
        return false;
      }
    }

    return true;
  }

  @Override
  public User getUserFromSignature(String hash, String signature, boolean verifyTimestamp) {

    if (!this.checkSignature(hash, signature, verifyTimestamp)) {
      logger.error("No Root Certificate for this certificate");
      throw new RuntimeException(ERROR_UNKNOWN_ROOT_CERTIFICATE);
    }

    User user = new User();
    ExtractorUser extractorUser = this.certificateExtractor.extractUser(this.getCertificateFromSignature(signature));
    user.setUsername(extractorUser.getUsername());
    user.setEmail(extractorUser.getEmail());
    user.setFullname(extractorUser.getFullname());

    return user;
  }

}
