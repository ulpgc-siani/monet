package org.monet.docservice.docprocessor.pdf.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.xml.xmp.PdfA1Schema;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.library.LibraryBase64;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.PresignedDocument;
import org.monet.docservice.docprocessor.pdf.Signer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class PdfSigner implements Signer {

  private static final int     ENCODED_SIGNATURE_LENGTH   = 15000;
  private static final String  RESOURCES_PRESIGN_DOCUMENT = "/resources/%s/presign/%s.pdf";

  private Configuration        configuration;
  private Logger               logger;
  private LibraryBase64        libraryBase64;
  private Provider<Repository> repositoryProvider;

  @Inject
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }

  @Inject
  public void injectConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Inject
  public void injectLibraryBase64(LibraryBase64 libraryBase64) {
    this.libraryBase64 = libraryBase64;
  }

  @Inject
  public void injectRepository(Provider<Repository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public PresignedDocument prepareDocument(String documentId, byte[] aCertificate, String reason, String location, String contact, String signField) {
    logger.debug("prepareDocument(%s, %s)", documentId, aCertificate);

    try {
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      Certificate certificate = cf.generateCertificate(new ByteArrayInputStream(aCertificate));

      return prepareDocument(documentId, certificate, reason, location, contact, signField);
    } catch (CertificateException e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException(String.format("Error processing certificate"));
    }
  }

  private PresignedDocument prepareDocument(String documentId, Certificate certificate, String reason, String location, String contact, String signField) {
    logger.debug("prepareDocument(%s, %s)", documentId, certificate);

    PresignedDocument info = new PresignedDocument();
    
    InputStream document = getDocumentFile(documentId);
    String instanceId = UUID.randomUUID().toString();
    File preparedDoc = getPresignedDocumentFile(documentId, instanceId);
    byte[] hash = getDocumentHash(document, preparedDoc, certificate, reason, location, contact, signField, documentId, info);

    info.setSignId(instanceId);
    info.setHash(libraryBase64.encode(hash));
    return info;
  }

  public byte[] getDocumentHash(InputStream document, File preparedDoc, Certificate certificate, String reason, String location, String contact, String signField, String documentId, PresignedDocument info) {
    logger.debug("getDocumentHash(%s, %s, %s)", document, preparedDoc, certificate);

    try {
      PdfReader pdfReader = new PdfReader(document);
      FileOutputStream fout = new FileOutputStream(preparedDoc);
      PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, fout, '\0', null, true);
      if (getPDFXConformance(pdfReader) == PdfWriter.PDFA1B)
        pdfStamper.getWriter().setPDFXConformance(PdfWriter.PDFA1B);
      PdfSignatureAppearance sap = pdfStamper.getSignatureAppearance();
      Certificate[] certs = new Certificate[1];
      certs[0] = certificate;

      sap.setCrypto(null, certs, null, PdfSignatureAppearance.WINCER_SIGNED);

      if(pdfReader.getAcroFields().getField(signField) != null)
        sap.setVisibleSignature(signField);

      sap.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
      /*
       * PdfContentByte over = pdfStamper.getOverContent(1); //Obtener nueva capa sobre la página 1 com.itextpdf.text.Image img;
       * 
       * //TODO: Ahora mismo usamos la primera fuente disponible en el documento para la firma File fontFile = new File(this.serverConfigurator.getAppDataPath(), "arial.ttf"); //BaseFont bf = BaseFont.createFont((PRIndirectReference)BaseFont.getDocumentFonts(pdfReader).get(0)[1]); BaseFont bf = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.CP1252, BaseFont.EMBEDDED); sap.setLayer2Font(new Font(bf));
       * 
       * 
       * //Código 1D tipo 128 Barcode barcode = new Barcode128(); barcode.setCode("1111-2222-3333"); img = com.itextpdf.text.Image.getInstance(barcode.createAwtImage(Color.black, Color.white), null); over.addImage(img, img.getWidth(), 0, 0, img.getHeight(), 100, 20);
       * 
       * //Código 2D tipo PDF417 BarcodePDF417 barcode417 = new BarcodePDF417(); barcode417.setText("Hola amigo mio"); img = barcode417.getImage(); over.addImage(img, img.getWidth(), 0, 0, img.getHeight(), 300, 20);
       * 
       * 
       * BarcodeQRCode qrCode = new BarcodeQRCode("http://www.monet.com/doc?cve=ADE3556dwerrtDDFWGsd342SdfD", 50, 50, null); img = qrCode.getImage(); over.addImage(img, img.getWidth(), 0, 0, img.getHeight(), 100, 20);
       */
      Date signDate = addSignMetadata(certificate, sap, reason, location, contact);
      info.setDate(signDate);

      HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
      exc.put(PdfName.CONTENTS, Integer.valueOf(ENCODED_SIGNATURE_LENGTH + 2));
      sap.preClose(exc);

      MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
      byte buf[] = new byte[8192];
      int n;
      InputStream inp = sap.getRangeStream();
      while ((n = inp.read(buf)) > 0) {
        messageDigest.update(buf, 0, n);
      }
      byte[] hash = messageDigest.digest();

      PdfDictionary dic2 = new PdfDictionary();
      dic2.put(PdfName.CONTENTS, new PdfString(PdfSignerUtils.getPlaceHolderArr(ENCODED_SIGNATURE_LENGTH)).setHexWriting(true));
      sap.close(dic2);

      return hash;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException(String.format("Error calculating document '%s' sign field '%s' hash", documentId, signField));
    }
  }

  private int getPDFXConformance(PdfReader pdfReader) {
    try {
      
      if (pdfReader.getMetadata() == null)
        return PdfWriter.PDFXNONE;
      
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(pdfReader.getMetadata()));
      NodeList nodeList = document.getElementsByTagName(PdfA1Schema.CONFORMANCE);
      Node node1 = nodeList.item(0);
      if (node1.getTextContent().equals("A"))
        return PdfWriter.PDFA1A;
      else if (node1.getTextContent().equals("B"))
        return PdfWriter.PDFA1B;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return PdfWriter.PDFXNONE;
  }

  private Date addSignMetadata(Certificate certificate, PdfSignatureAppearance sap, String reason, String location, String contact) {
    logger.debug("addSignMetadata(%s, %s)", certificate, sap);

    Calendar date = Calendar.getInstance();

    sap.setSignDate(date);

    if (reason != null && !reason.isEmpty())
      sap.setReason(reason);

    if (location != null && !location.isEmpty())
      sap.setLocation(location);

    if (contact != null && !contact.isEmpty())
      sap.setContact(contact);

    sap.setAcro6Layers(true);
    sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);

    PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_SHA1);
    dic.setDate(new PdfDate(date));
    dic.setName(PdfPKCS7.getSubjectFields((X509Certificate) certificate).getField("CN"));
    dic.setReason(reason);
    dic.setLocation(location);
    dic.setContact(contact);
    sap.setCryptoDictionary(dic);
    
    return date.getTime();
  }

  private File getPresignedDocumentFile(String documentId, String instanceId) {
    logger.debug("getPresignedDocumentFile(%s, %s)", documentId, instanceId);

    File file = new File(this.configuration.getPath(Configuration.PATH_TEMP) + String.format(RESOURCES_PRESIGN_DOCUMENT, documentId, instanceId));
    file.getParentFile().mkdirs();
    return file;
  }

  private InputStream getDocumentFile(String documentId) {
    logger.debug("getDocumentFile(%s)", documentId);
    return repositoryProvider.get().getDocumentData(documentId);
    // return new File(this.configuration.getPath(Configuration.PATH_TEMP) + String.format(RESOURCES_DOCUMENT, documentId, documentId));
  }

  public void signDocument(String documentId, String instanceId, byte[] pkcs7Block) {
    logger.debug("signDocument(%s, %s, %s)", documentId, instanceId, pkcs7Block);

    File presignedDoc = getPresignedDocumentFile(documentId, instanceId);

    try {

      String sigAreaHex = PdfSignerUtils.byteArrayToHexString(pkcs7Block);
      byte[] placeHolder = PdfSignerUtils.byteArrayToHexString(PdfSignerUtils.getPlaceHolderArr(ENCODED_SIGNATURE_LENGTH)).getBytes();

      byte paddedHexArea[] = new byte[placeHolder.length];
      for (int i = 0; i < paddedHexArea.length; i++) {
        paddedHexArea[i] = 0x30;
      }

      System.arraycopy(sigAreaHex.getBytes(), 0, paddedHexArea, 0, sigAreaHex.getBytes().length);
      PdfSignerUtils.replace(presignedDoc, placeHolder, paddedHexArea);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new ApplicationException("Creation of signed document failed.");
    }

    boolean isValid = false;
    try {

      isValid = verifyDocument(presignedDoc);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    if (!isValid)
      throw new ApplicationException("Creation of signed document failed: invalid document generated.");
    else {
      String contentType = repositoryProvider.get().getDocumentDataContentType(documentId);
      try {
        FileInputStream fileInputStream = new FileInputStream(presignedDoc);
        String hash = StreamHelper.calculateHashToHexString(fileInputStream);
        StreamHelper.close(fileInputStream);
        fileInputStream = new FileInputStream(presignedDoc);
        
        repositoryProvider.get().saveDocumentData(documentId, fileInputStream, contentType, hash);
        presignedDoc.delete();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        presignedDoc.delete();
      }
    }
  }

  private boolean verifyDocument(File documentFile) throws IOException, SignatureException {
    logger.debug("verifyDocument(%s)", documentFile);

    boolean isValid = false;
    PdfReader reader = null;

    try {
      reader = new PdfReader(documentFile.getAbsolutePath());
      AcroFields af = reader.getAcroFields();
      ArrayList<String> names = (ArrayList<String>) af.getSignatureNames();
      isValid = true;
      for (String name : names) {
        PdfPKCS7 pk = af.verifySignature(name);
        isValid &= pk.verify();
      }
    } finally {
      if (reader != null)
        reader.close();
    }
    return isValid;
  }
}
