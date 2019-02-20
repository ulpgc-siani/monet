package org.monet.encrypt.extractor.extractors;

import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.monet.encrypt.extractor.CertificateExtractor.Extractor;
import org.monet.encrypt.extractor.ExtractorUser;
import org.monet.encrypt.extractor.MonetCertificateInfo;
import org.monet.encrypt.library.X509CertificateHelper;

public class DNIe extends ExtractorImpl implements Extractor {

  @Override
  public ExtractorUser get(X509Certificate certificate) {
    ExtractorUser user = new ExtractorUser();

    HashMap<String, String> values = X509CertificateHelper.toMap(certificate.getSubjectDN());
    String username = values.get("SERIALNUMBER");
    String fullname = values.get("GIVENNAME") + " " + values.get("CN");

    user.setUsername(username);
    user.setFullname(fullname);

    return user;
  }

  @Override
  public MonetCertificateInfo getMonetInfo(X509Certificate certificate) {
    return null;
  }
}
