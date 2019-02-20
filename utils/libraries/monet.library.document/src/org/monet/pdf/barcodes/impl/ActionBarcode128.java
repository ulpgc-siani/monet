package org.monet.pdf.barcodes.impl;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ActionBarcode128 implements ActionBarcode{

  @Override
  public void addedBarcode(PdfReader pdfReader, PdfStamper stamper,
      int numPage, String code, int positionX,
      int positionY) throws Exception {
    PdfContentByte cb = stamper.getOverContent(numPage);
    Barcode128 code128 = new Barcode128();
    code128.setCode(code);
    Image imageBCode128 = code128.createImageWithBarcode(cb, null, null);
    imageBCode128.setAbsolutePosition(positionX, positionY);
    cb.addImage(imageBCode128);
  }

}
