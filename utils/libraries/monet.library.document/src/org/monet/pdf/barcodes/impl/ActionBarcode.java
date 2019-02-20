package org.monet.pdf.barcodes.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public interface ActionBarcode {
    void addedBarcode(PdfReader pdfReader, PdfStamper stamper, int numPage, String code, int positionX, int positionY) throws Exception ;
}
