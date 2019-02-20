package org.monet.pdf.barcodes;

import java.util.HashMap;
import java.util.Map;

import org.monet.pdf.PDFer;
import org.monet.pdf.PDFer.BARCODE_TYPE;
import org.monet.pdf.barcodes.impl.ActionBarcode;
import org.monet.pdf.barcodes.impl.ActionBarcode128;

public class BarcodeFactory {
    private Map<PDFer.BARCODE_TYPE,ActionBarcode> barcodes;
    private static BarcodeFactory instance;
    
    private BarcodeFactory(){
      this.barcodes = new HashMap<PDFer.BARCODE_TYPE,ActionBarcode>() ;
      this.barcodes.put(BARCODE_TYPE.BARCODE128, new ActionBarcode128());
    }
    
    public static BarcodeFactory getInstance(){
      return (instance == null) ? instance = new BarcodeFactory() : instance;
    }
    
    public  ActionBarcode getActionBarcode(PDFer.BARCODE_TYPE barcodeType){
      return this.barcodes.get(barcodeType);
    }
}
