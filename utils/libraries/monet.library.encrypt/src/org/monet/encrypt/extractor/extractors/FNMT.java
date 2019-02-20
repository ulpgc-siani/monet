package org.monet.encrypt.extractor.extractors;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.monet.encrypt.extractor.CertificateExtractor.Extractor;
import org.monet.encrypt.extractor.ExtractorUser;
import org.monet.encrypt.extractor.MonetCertificateInfo;
import org.monet.encrypt.library.X509CertificateHelper;

public class FNMT extends ExtractorImpl implements Extractor {

  private static final String CN_1 = "NOMBRE ([^-]+) - NIF (.+)";
  private static final String CN_2 = "ENTIDAD ([^-]+) - CIF (.+)";

  private static final Pattern PatternCN1 = Pattern.compile(CN_1);
  private static final Pattern PatternCN2 = Pattern.compile(CN_2);
  
  @Override
  public ExtractorUser get(X509Certificate certificate) {
    ExtractorUser user = new ExtractorUser();

    HashMap<String, String> values = X509CertificateHelper.toMap(certificate.getSubjectDN());
    String subjectName = values.get("CN");
    
    Matcher matcher = PatternCN1.matcher(subjectName);
    if(!matcher.matches()) {
      matcher = PatternCN2.matcher(subjectName);
      if(!matcher.matches()) {
        matcher = null;
      }
    }
    
    if(matcher == null) {
      throw new RuntimeException("Can't extract data of user from certificate");
    }
    
    String username = matcher.group(2);
    String fullname = matcher.group(1);
    
    user.setUsername(username);
    user.setFullname(fullname);

    return user;
  }

  @Override
  public MonetCertificateInfo getMonetInfo(X509Certificate certificate) {
    return null;
  }
}
