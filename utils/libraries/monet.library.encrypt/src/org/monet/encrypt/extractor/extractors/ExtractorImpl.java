package org.monet.encrypt.extractor.extractors;

import java.security.cert.X509Certificate;

import org.monet.encrypt.extractor.CertificateExtractor.Extractor;

public abstract class ExtractorImpl implements Extractor {

  @Override
  public String getAuthorityName(X509Certificate certificate) {
    return "";
  }

}
