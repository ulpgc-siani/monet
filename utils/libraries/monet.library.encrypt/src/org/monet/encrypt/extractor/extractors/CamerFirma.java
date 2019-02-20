package org.monet.encrypt.extractor.extractors;

import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.monet.encrypt.extractor.CertificateExtractor.Extractor;
import org.monet.encrypt.extractor.ExtractorUser;
import org.monet.encrypt.extractor.MonetCertificateInfo;
import org.monet.encrypt.library.X509CertificateHelper;

public class CamerFirma extends ExtractorImpl implements Extractor {

  @Override
  public ExtractorUser get(X509Certificate certificate) {
    ExtractorUser user = new ExtractorUser();

    HashMap<String, String> values = X509CertificateHelper.toMap(certificate.getSubjectDN());
    String fullname = values.get("CN"); 

    user.setUsername(values.get("SERIALNUMBER"));
    user.setFullname(fullname);
    user.setEmail(values.get("E"));

    return user;
  }

  @Override
  public MonetCertificateInfo getMonetInfo(X509Certificate certificate) {
    return null;
  }
}
