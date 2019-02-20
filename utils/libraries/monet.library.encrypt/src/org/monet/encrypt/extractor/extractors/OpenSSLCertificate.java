package org.monet.encrypt.extractor.extractors;

import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.monet.encrypt.extractor.CertificateExtractor.Extractor;
import org.monet.encrypt.extractor.ExtractorUser;
import org.monet.encrypt.extractor.MonetCertificateInfo;
import org.monet.encrypt.library.X509CertificateHelper;

public class OpenSSLCertificate extends ExtractorImpl implements Extractor {

  @Override
  public ExtractorUser get(X509Certificate certificate) {
    ExtractorUser user = new ExtractorUser();

    HashMap<String, String> values = X509CertificateHelper.toMap(certificate.getSubjectDN());

    String eField = values.get("E");
    String username = eField != null ? eField : asId(values.get("CN"));
    String fullname = values.get("CN");
    
    user.setEmail(eField);
    user.setUsername(username);
    user.setFullname(fullname);

    return user;
  }

  private String asId(String value) {
    StringBuffer res = new StringBuffer();
    int idx = 0;
    char c;
    boolean toUpper = true;
    if (value == null)
      return null;
    while (idx < value.length()) {
      c = value.charAt(idx++);

      if (Character.isJavaIdentifierPart(c)) {
        if (toUpper) {
          c = Character.toUpperCase(c);
          toUpper = false;
        }
        res.append(c);
      } else {
        toUpper = true;
      }
    }
    return res.toString();
  }

  @Override
  public MonetCertificateInfo getMonetInfo(X509Certificate certificate) {
    return null;
  }
}
