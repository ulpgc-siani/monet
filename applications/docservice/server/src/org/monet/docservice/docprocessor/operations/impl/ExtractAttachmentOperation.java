package org.monet.docservice.docprocessor.operations.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.itextpdf.text.pdf.*;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.ImageSupport;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.Document;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.worker.WorkQueueItem;
import org.monet.docservice.servlet.RequestParams;
import org.monet.docservice.servlet.factory.ActionFactory;
import org.monet.docservice.servlet.factory.impl.Action;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ExtractAttachmentOperation implements Operation {

  private WorkQueueItem target;
  private Logger logger;
  private ActionFactory actionFactory;
  private Provider<Repository> repositoryProvider;

  @Inject 
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void setTarget(WorkQueueItem target) {
    this.target = target;  
  }

  @Inject
  public void injectActionFactory(ActionFactory actionFactory) {
    this.actionFactory = actionFactory;
  }
  
  @Inject
  public void injectRepository(Provider<Repository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public void execute() {
    logger.debug("execute()");

    InputStream pdfFile = null;
    try{
      pdfFile = new FileInputStream(target.getDataFile());

      extractAttachment(target.getDocumentKey(), pdfFile, target.getXmlData());
    } catch(Exception e){
      logger.info("Documento id="+target.getDocumentKey()+" sin modelo xml asociado",e);
    } finally {
      StreamHelper.close(pdfFile);
    }
  }

  private void extractAttachment(Key documentKey, InputStream sourcePdfFileStream, String xmlData) {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;

    if(xmlData == null || xmlData.trim().isEmpty()) return;

    try {
      docBuilder = docFactory.newDocumentBuilder();
      org.w3c.dom.Document doc = docBuilder.parse(new ByteArrayInputStream(xmlData.getBytes("UTF-8")));

      XPath xpath = XPathFactory.newInstance().newXPath();
      String expression = "//*[@is-attachment=\"true\"]/text()";

      NodeList nodeList =  (NodeList) xpath.evaluate(expression,doc,XPathConstants.NODESET);

      saveFileAttach(documentKey, nodeList,sourcePdfFileStream);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty("indent", "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      this.target.setXmlData(writer.toString());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException("Error uploading document.");
    } 
  }


  private void saveFileAttach(Key documentKey, NodeList nodeList, InputStream sourcePdfFileStream)throws Exception {
    PdfReader reader = new PdfReader(sourcePdfFileStream);
    PdfArray array;
    PdfDictionary annot;
    PdfDictionary fs;
    PdfDictionary refs;

    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
      array = reader.getPageN(i).getAsArray(PdfName.ANNOTS);
      if (array == null) continue;

      for (int j = 0; j < array.size(); j++) {
        annot = array.getAsDict(j);
        if (PdfName.FILEATTACHMENT.equals(annot.getAsName(PdfName.SUBTYPE))) {
          fs = annot.getAsDict(PdfName.FS);
          refs = fs.getAsDict(PdfName.EF);

          for(int k = 0; k < nodeList.getLength(); k++){
            String attachId = nodeList.item(k).getNodeValue();
            Key attachKey = new Key(documentKey.getSpace(), attachId);
            if(fs.getAsString(PdfName.F).toString().equals(attachId)){
              nodeList.item(k).setNodeValue(attachKey.getId());
              
              InputStream fileAttach = new ByteArrayInputStream(PdfReader.getStreamBytes((PRStream)refs.getAsStream(PdfName.F)));
              Map<String , Object> params = new HashMap<String, Object>();
              params.put(RequestParams.REQUEST_PARAM_DOCUMENT_CODE , attachKey.getId());
              params.put(RequestParams.REQUEST_PARAM_SPACE , attachKey.getSpace());
              params.put(RequestParams.REQUEST_PARAM_DOCUMENT_DATA, fileAttach);
              String contentType = annot.getAsString(PdfName.CONTENTS).toString();
              params.put(RequestParams.REQUEST_PARAM_CONTENT_TYPE, annot.getAsString(PdfName.CONTENTS).toString());
              
              InputStream[] fileAttachcopy = null;
              try{   
                if(ImageSupport.isImage(attachKey.getId())){
                  BufferedImage image = ImageIO.read(fileAttach);
                  Repository repository = repositoryProvider.get();
                  
                  int width = image.getWidth();
                  int height = image.getHeight();
                  fileAttach.reset();
                  
                  if (repository.existsDocument(attachKey))
                    repository.removeDocument(attachKey);
                    
                  Action action = this.actionFactory.create(ActionFactory.ACTION_UPLOAD_IMAGE);
                  params.put(RequestParams.REQUEST_PARAM_WIDTH, String.valueOf(width));
                  params.put(RequestParams.REQUEST_PARAM_HEIGHT, String.valueOf(height));
                  action.execute(params, null);
                }
                else{
                  Repository repository = repositoryProvider.get();
                  
                  if(!repository.existsDocument(attachKey))
                    repository.createEmptyDocument(attachKey, Document.STATE_CONSOLIDATED);
                  
                  fileAttachcopy = org.monet.filesystem.StreamHelper.copy(fileAttach, 2);
                  String hash = StreamHelper.calculateHashToHexString(fileAttachcopy[0]);
                  String xmlData = null;
                  repository.saveDocumentData(attachKey, fileAttachcopy[1], xmlData, contentType, hash);
                }
                break;
              }
              catch (IOException e) {
                this.logger.error(e.getMessage(), e);
              }
              finally{
                if(fileAttachcopy != null){
                  StreamHelper.close(fileAttachcopy[0]);
                  StreamHelper.close(fileAttachcopy[1]);
                }
              }
            }
          }

        }
      }
    }
  }

}
