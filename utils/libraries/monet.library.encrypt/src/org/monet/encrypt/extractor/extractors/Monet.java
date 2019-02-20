package org.monet.encrypt.extractor.extractors;

import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.monet.encrypt.extractor.CertificateExtractor.Extractor;
import org.monet.encrypt.extractor.ExtractorUser;
import org.monet.encrypt.extractor.MonetCertificateInfo;
import org.monet.encrypt.library.X509CertificateHelper;

public class Monet extends ExtractorImpl implements Extractor {

  @Override
  public ExtractorUser get(X509Certificate certificate) {
    return null;
  }

  @Override
  public MonetCertificateInfo getMonetInfo(X509Certificate certificate) {
    String[] data = this.getMonetData(X509CertificateHelper.toMap(certificate.getSubjectDN()));
        
    if (data == null)
      return null;
    
    String monetType = data[0];
    String businessUnitName = data[1];
    
    data = this.getMonetData(X509CertificateHelper.toMap(certificate.getIssuerDN()));

    if (data == null)
      return null;
    
    String federationName = data[1];

    MonetCertificateInfo info = new MonetCertificateInfo(monetType); 
    info.setFederation(federationName);
    info.setBusinessUnit(businessUnitName);
    
    return info;
  }
  
  private String[] getMonetData(HashMap<String, String> values) {
    
    if (!values.containsKey("CN"))
      return null;
    
    String[] data = values.get("CN").split("\\|");
    
    if (data.length < 2)
      throw new RuntimeException("Can't extract monet info from certificate");
    
    return data;
  }
}
