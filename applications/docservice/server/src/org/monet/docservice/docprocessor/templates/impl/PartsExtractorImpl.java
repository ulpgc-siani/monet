package org.monet.docservice.docprocessor.templates.impl;

import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.docservice.docprocessor.templates.PartsExtractor;
import org.monet.docservice.docprocessor.templates.openxml.partsextractor.OpenXmlDocumentExtractor;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class PartsExtractorImpl implements PartsExtractor {

  private Provider<OpenXmlDocumentExtractor> openXmlPartsExtractorProvider;

  @Inject
  public void injectOpenXmlPartsExtractor(Provider<OpenXmlDocumentExtractor> openXmlPartsExtractorProvider) {
    this.openXmlPartsExtractorProvider = openXmlPartsExtractorProvider;
  }
  
  public void processTemplate(String templatePath, String templateInstanceId, int type) {
    //TODO: Implement others template parts processors
    if(type == DocumentType.OPEN_XML)
      openXmlPartsExtractorProvider.get().process(templatePath, templateInstanceId);
  }
  
}
