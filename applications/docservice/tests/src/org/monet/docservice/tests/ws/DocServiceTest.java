package org.monet.docservice.tests.ws;

import java.io.File;
import java.io.FileInputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.monet.docservice.docprocessor.model.DocumentType;
import org.monet.services.docservice.timeoutadapter.DocserviceProxy;


public class DocServiceTest {

  @Test
  public void createTemplateTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy("http://localhost:8080/monet.docservice/ws/docs", 100000);
   
    File template = new File("resources/oxf/Template.docx");
    FileInputStream inputStream = new FileInputStream(template);
    byte[] file = new byte[inputStream.available()];
    inputStream.read(file);
    inputStream.close();
    
    proxy.uploadTemplate("template", DocumentType.toString(DocumentType.OPEN_XML), file);
    
    //    proxy.uploadTemplate("testTamplate_20", DocumentType.toString(DocumentType.OPEN_XML), file);
    //    proxy.uploadTemplate("testTamplate_30", DocumentType.toString(DocumentType.OPEN_XML), file);
    //    proxy.uploadTemplate("testTamplate_40", DocumentType.toString(DocumentType.OPEN_XML), file);
    //    proxy.uploadTemplate("testTamplate_50", DocumentType.toString(DocumentType.OPEN_XML), file);
    //    proxy.uploadTemplate("testTamplate_60", DocumentType.toString(DocumentType.OPEN_XML), file);

  	
   //String url = "http://localhost:8082/docservice/ws/docs";
   

  }

  @Test
  public void createDocumentTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy("http://localhost:8080/monet.docservice/ws/docs", 100000);

    try {
      File docModel = new File("resources/docModels/sampleModel.xml");
      FileInputStream inputStream = new FileInputStream(docModel);
      byte[] file = new byte[inputStream.available()];
      inputStream.read(file);
      inputStream.close();

      String is = "sampleXXw2222";
      for (int i = 0; i < 1; i++) {
        is += i;
        proxy.createDocument("template", is);
        proxy.updateDocument(is , file, false);
        proxy.consolidateDocument(is, false);
        is = "sampleXXw2";
      }
      
    } finally {
      //proxy.removeDocument("testDocument1");
    }
  }

  @Test
  public void uploadDocumentTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy("http://localhost:8081/docservice/ws/docs", 100000);
    File docModel = new File("resources/pdf/fecyt_PN2008.pdf");
    FileInputStream inputStream = new FileInputStream(docModel);
    byte[] file = new byte[inputStream.available()];
    inputStream.read(file);
    inputStream.close();
    proxy.uploadDocument("fecyt_PN2008", file, "application/pdf", true);
  }

  @Test
  public void uploadImageTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy();
    File img = new File("resources/img/image4.jpeg");
    FileInputStream inputStream = new FileInputStream(img);
    byte[] file = new byte[inputStream.available()];
    inputStream.read(file);
    inputStream.close();
   // proxy.uploadImage("image4.jpeg", file, "image/jpeg", 1024, 768);
  }

  @Test
  public void createEmptyDocumentTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy();

    try {

      File template = new File("assets/oxf/EmptyTemplate.docx");
      FileInputStream inputStream = new FileInputStream(template);
      byte[] file = new byte[inputStream.available()];
      inputStream.read(file);
      inputStream.close();

      File signs = new File("assets/xml/signatures.xml");
      FileInputStream inputStream1 = new FileInputStream(signs);
      byte[] fileSigns = new byte[inputStream1.available()];
      inputStream1.read(fileSigns);
      inputStream1.close();

      proxy.uploadTemplate("Template", DocumentType.toString(DocumentType.OPEN_XML), file);

      //      proxy.removeDocument("emptyDocument");WithSigns
      //      File docModel = new File("assets/docModels/emptyModel.xml");
      //      inputStream = new FileInputStream(docModel);
      //      file = new byte[inputStream.available()];
      //      inputStream.read(file);
      //      inputStream.close();
      //      
      //      proxy.createDocument("emptyTemplate", "emptyDocument");
      //      proxy.updateDocument("emptyDocument", file);
    } finally {
      //proxy.removeDocument("testDocument1");
    }
  }

  @Test
  public void createBadDocumentTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy();

    try {

      File template = new File("assets/oxf/Template.docx");
      FileInputStream inputStream = new FileInputStream(template);
      byte[] file = new byte[inputStream.available()];
      inputStream.read(file);
      inputStream.close();

      proxy.uploadTemplateWithSigns("Template", DocumentType.toString(DocumentType.OPEN_XML), file, file);

      proxy.removeDocument("BadDocument");
      File docModel = new File("assets/docModels/emptyModel.xml");
      inputStream = new FileInputStream(docModel);
      file = new byte[inputStream.available()];
      inputStream.read(file);
      inputStream.close();

      proxy.createDocument("Template", "BadDocument");
      proxy.updateDocument("BadDocument", file, false);
    } finally {
      //proxy.removeDocument("testDocument1");
    }
  }

  @Test
  public void consolidateDocumentTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy();

    try {
      proxy.consolidateDocument("80", false);
    } finally {
      //proxy.removeDocument("testDocument1");
    }

  }

  @Test
  public void removeAllNodeFilesTest() throws Exception {
    DocserviceProxy proxy = new DocserviceProxy();
    int results = proxy.removeAllNodeFiles(9828);
    Assert.assertTrue("Something has been deleted", results > 0);
  }

}
