package org.monet.docservice.docprocessor.pdf.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.jpedal.PdfDecoder;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.exceptions.ApplicationException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.model.PreviewType;
import org.monet.docservice.docprocessor.pdf.PreviewGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class JPedalPreviewGenerator implements PreviewGenerator {
  
  private Logger logger;
  private Configuration configuration;
  private Provider<Repository> repositoryProvider;

  private static final int MaxPages = 15;

  @Inject
  public JPedalPreviewGenerator(Configuration configuration, Logger logger, Provider<Repository> repositoryProvider) {
    this.logger = logger;
    this.configuration = configuration;
    this.repositoryProvider = repositoryProvider;
    
    String[] fontsDirs = new String[1];
    fontsDirs[0] = this.configuration.getPath(Configuration.PATH_TRUETYPE_FONTS);
    PdfDecoder.setFontDirs(fontsDirs);
  }
  
  public void generatePreview(String pdfPath, Key documentKey) {
    logger.debug("generatePreview(%s, %s)", pdfPath, documentKey);
    
    Repository repository = this.repositoryProvider.get();
    File tempFile = new File(this.configuration.getPath(Configuration.PATH_TEMP) + File.separator + UUID.randomUUID().toString());
    
    PdfDecoder pdf = new PdfDecoder();
    
    try {
      pdf.openPdfFile(pdfPath);
      int pages = pdf.getPageCount();
      if (pages > MaxPages) pages = MaxPages;

      for(int i=1;i<=pages;i++) {       
        pdf.setPageParameters(1.2F, i);
        BufferedImage buff = pdf.getPageAsImage(i);
        ImageIO.write(buff, "png", tempFile);
        
        FileInputStream stream = new FileInputStream(tempFile);
        repository.saveDocumentPreviewData(documentKey,
                                           i, 
                                           stream, 
                                           "image/png", 
                                           PreviewType.PAGE, 
                                           buff.getWidth(), 
                                           buff.getHeight(), 
                                           buff.getWidth()/(float)buff.getHeight());
        StreamHelper.close(stream);
        
        pdf.setPageParameters(0.2F, i);
        buff = pdf.getPageAsImage(i);
        ImageIO.write(buff, "png", tempFile);
        
        stream = new FileInputStream(tempFile);
        repository.saveDocumentPreviewData(documentKey,
                                           i, 
                                           stream, 
                                           "image/png", 
                                           PreviewType.THUMBNAIL, 
                                           buff.getWidth(), 
                                           buff.getHeight(), 
                                           buff.getWidth()/(float)buff.getHeight());
        stream.close();
        stream = null;
        buff = null;
      }
            
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new ApplicationException(String.format("Error creating preview of document '%s'", documentKey));
    }
    finally {
      if(tempFile.exists()) tempFile.delete();
      pdf.closePdfFile();
    }
  } 
  
}
