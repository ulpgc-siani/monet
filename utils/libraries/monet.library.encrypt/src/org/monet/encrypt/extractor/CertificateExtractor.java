package org.monet.encrypt.extractor;

import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import org.monet.encrypt.extractor.extractors.CamerFirma;
import org.monet.encrypt.extractor.extractors.DNIe;
import org.monet.encrypt.extractor.extractors.FNMT;
import org.monet.encrypt.extractor.extractors.Monet;
import org.monet.encrypt.extractor.extractors.OpenSSLCertificate;
import org.monet.encrypt.library.X509CertificateHelper;

public class CertificateExtractor {

  public static final String     MONET               = "MONET";
  public static final String     FNMT                = "FNMT";
  public static final String     DNIe                = "DIRECCION GENERAL DE LA POLICIA";
  public static final String     CAMERFIRMA          = "AC Camerfirma";
  public static final String     CAMERFIRMA_SA       = "AC Camerfirma SA";
  public static final String     GENERIC_CERTIFICATE = "Generic_Certificate";

  private Map<String, Extractor> extractorsMap;
  
  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  public interface Extractor {
    public ExtractorUser get(X509Certificate certificate);
    public MonetCertificateInfo getMonetInfo(X509Certificate certificate);
    public String getAuthorityName(X509Certificate certificate);
  }

  public CertificateExtractor() {
    extractorsMap = new HashMap<String, Extractor>();
    extractorsMap.put(MONET, new Monet());
    extractorsMap.put(FNMT, new FNMT());
    extractorsMap.put(DNIe, new DNIe());
    extractorsMap.put(CAMERFIRMA, new CamerFirma());
    extractorsMap.put(CAMERFIRMA_SA, new CamerFirma());
    extractorsMap.put(GENERIC_CERTIFICATE, new OpenSSLCertificate());
  }

  public ExtractorUser extractUser(X509Certificate certificate) {
    Extractor certificateExtractor = null;

    HashMap<String, String> issuerMap = X509CertificateHelper.toMap(certificate.getIssuerDN());
    String organization = issuerMap.get("O");
    if (organization != null)
      certificateExtractor = this.extractorsMap.get(organization);

    if (certificateExtractor == null)
      certificateExtractor = this.extractorsMap.get(GENERIC_CERTIFICATE);

    return certificateExtractor.get(certificate);
  }

  public MonetCertificateInfo extractMonetInfo(X509Certificate certificate) {
    Extractor extractor = this.extractorsMap.get(MONET);
    return extractor.getMonetInfo(certificate);
  }
  
  public String extractAuthorityName(X509Certificate certificate) {
    Extractor extractor = this.extractorsMap.get(MONET);
    return extractor.getAuthorityName(certificate);
  }
}
