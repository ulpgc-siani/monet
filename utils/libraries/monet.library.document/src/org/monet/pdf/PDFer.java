package org.monet.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.PDFToImage;
import org.monet.pdf.barcodes.BarcodeFactory;
import org.monet.pdf.barcodes.impl.ActionBarcode;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

public class PDFer {
  public static enum BARCODE_TYPE {BARCODE128}; 

  public static final String FORMAT_PNG = "png";
  public static final String FORMAT_JPG = "jpg";



  public static void addBarCode(PdfReader pdfReader, PdfStamper stamper, BARCODE_TYPE  barcodeType, int numPage, String code,
      int positionX, int positionY) throws Exception{
    ActionBarcode aBCode = BarcodeFactory.getInstance().getActionBarcode(barcodeType);
    aBCode.addedBarcode(pdfReader, stamper, numPage, code, positionX, positionY);
  }


  public static void convertAPDFtoImage(String pdfSource, String outPath, String imagesName, String resolution, String format) throws Exception {
    String[] parameters = new String[7];

    parameters[0]= "-resolution";
    parameters[1]= resolution;
    parameters[2]= "-outputPrefix";
    parameters[3]= outPath + File.separator + imagesName;
    parameters[4]= "-imageType";
    parameters[5]= format;
    parameters[6]= pdfSource;

    PDFToImage.main(parameters);
  }

  public static void convertAPDFPagetoImage(String pdfSource, String outPath, String imagesName, int resolution, String format, int numPage) throws Exception {
    String[] parameters = new String[11];

    parameters[0]= "-resolution";
    parameters[1]= String.valueOf(resolution);
    parameters[2]= "-outputPrefix";
    parameters[3]= outPath + File.separator + imagesName;
    parameters[4]= "-imageType";
    parameters[5]= format;
    parameters[6]= "-startPage";
    parameters[7]= String.valueOf(numPage);
    parameters[8]= "-endPage";
    parameters[9]= String.valueOf(numPage);
    parameters[10]= pdfSource;

    PDFToImage.main(parameters);
  }


  public static void convertAPDFPageRangetoImage(String pdfSource, String outPath, String imagesName, int resolution, String format, int startPage, int endPage) throws Exception {
    String[] parameters = new String[11];

    parameters[0]= "-resolution";
    parameters[1]= String.valueOf(resolution);
    parameters[2]= "-outputPrefix";
    parameters[3]= outPath + File.separator + imagesName;
    parameters[4]= "-imageType";
    parameters[5]= format;
    parameters[6]= "-startPage";
    parameters[7]= String.valueOf(startPage);
    parameters[8]= "-endPage";
    parameters[9]= String.valueOf(endPage);
    parameters[10]= pdfSource;

    PDFToImage.main(parameters);
  }

  public static String getContentFieldFromDocument(InputStream sourcePdfFileStream, String field) throws Exception {
    PdfReader pdfReader = null;
    String content = null;

    try {
      pdfReader = new PdfReader(sourcePdfFileStream);
      content = pdfReader.getAcroFields().getField(field);
    } catch(Exception e) {
      throw e;
    } finally {
      if(pdfReader != null) pdfReader.close();
      if(sourcePdfFileStream != null) sourcePdfFileStream.close();
    }

    return content;
  }

  public static void setContentFieldInDocument(File documentInput, String fieldName, String contentField, File documentOutput, boolean isHiddenField, int page) throws Exception{
    Map<String,String> fieldContentMap = new HashMap<String,String>();
    fieldContentMap.put(fieldName, contentField);
    addContentFields(documentInput, fieldContentMap, documentOutput, isHiddenField, page);
  }
  
  public static void setContentFieldInDocument(File documentInput, Map<String,String> fieldContentMap, File documentOutput, boolean isHiddenField, int page) throws Exception{
    addContentFields(documentInput, fieldContentMap, documentOutput, isHiddenField, page);
  }

  private static void addContentFields(File documentInput, Map<String,String> fieldContentMap, File documentOutput, boolean isHiddenField, int page) throws Exception{
    FileInputStream documentInputStream = null;
    FileOutputStream outputStream = null;
    PdfReader pdfReader = null;
    PdfStamper stamper = null;

    try {
      documentInputStream = new FileInputStream(documentInput);
      outputStream = new FileOutputStream(documentOutput);

      pdfReader = new PdfReader(documentInputStream);
      stamper = new PdfStamper(pdfReader, outputStream);
      PdfWriter pdfWriter = stamper.getWriter();

      for (String field : fieldContentMap.keySet()) {
        TextField textField = new TextField(pdfWriter, new Rectangle(0,0,10,10), field);
        textField.setText(fieldContentMap.get(field));
        if(isHiddenField) textField.setVisibility(BaseField.HIDDEN);
        stamper.addAnnotation(textField.getTextField(), page);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if(stamper != null) stamper.close();
      if(pdfReader != null) pdfReader.close();
      if(documentInputStream != null) documentInputStream.close();
      if(outputStream != null) outputStream.close();
    }
  }
}
