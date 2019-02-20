package org.monet.filesystem.zipper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.monet.exceptions.FilesystemException;
import org.monet.filesystem.StreamHelper;
import org.monet.filesystem.files.LibraryFile;


public class LibraryZipImpl {
  
  private static final Integer BUFFER_SIZE = 16384;
  

  public static void compress(String sSourceDir, ArrayList<String> aFiles, String sDestinationFilename) throws FilesystemException{

    BufferedInputStream oOrigin = null;
    FileOutputStream oDestination;
    ZipOutputStream oOutput = null;
    Iterator<String> oIterator;
    byte[] aData;
    
    try {
      oDestination = new FileOutputStream(sDestinationFilename);
      oOutput = new ZipOutputStream(new BufferedOutputStream(oDestination));
      
      aData = new byte[BUFFER_SIZE];

      oIterator=aFiles.iterator();
      while (oIterator.hasNext()) {
        try {
          String sFilename = (String)oIterator.next();
    
          FileInputStream fisInput = new FileInputStream(sSourceDir + File.separator + sFilename);
          oOrigin = new BufferedInputStream(fisInput, BUFFER_SIZE);
    
          ZipEntry oEntry = new ZipEntry(sFilename.replace('\\', '/'));
          oOutput.putNextEntry(oEntry);
  
          int iCount;
          while( (iCount = oOrigin.read(aData, 0, BUFFER_SIZE)) != -1) 
            oOutput.write(aData, 0, iCount);
        } finally {
          StreamHelper.close(oOrigin);
        }
      }
    } catch (Exception oException) {
      throw new FilesystemException(oException.getMessage(),oException);
    } finally {
      StreamHelper.close(oOutput);
    }
  }
  
  public static void decompress(String sFilename, String sDestination) throws FilesystemException{
    BufferedOutputStream oDestination;
    FileInputStream oOrigin;
    ZipInputStream oInput = null; 
    
    try {
      oDestination = null;
      oOrigin = new FileInputStream(new File(sFilename));
      oInput = new ZipInputStream(new BufferedInputStream(oOrigin));

      int iCount;
      byte aData[] = new byte[BUFFER_SIZE];
   
      ZipEntry oEntry;
      while ((oEntry = oInput.getNextEntry()) != null) {
        if (oEntry.isDirectory()) new File(sDestination + File.separator + oEntry.getName()).mkdirs();
        else {
          try {
            String sDestDN = LibraryFile.getDirname(sDestination + File.separator + oEntry.getName()); 
            String sDestFN = sDestination + File.separator + oEntry.getName();
            
            new File(sDestDN).mkdirs();
  
            FileOutputStream oOutput = new FileOutputStream(sDestFN);
            oDestination = new BufferedOutputStream(oOutput, BUFFER_SIZE);
            
            while((iCount = oInput.read(aData, 0, BUFFER_SIZE)) != -1 ) {
              oDestination.write (aData, 0, iCount);
            }
            
            oDestination.flush();
          } finally {
            StreamHelper.close(oDestination);
          }
        }
      }
    }
    catch (Exception oException) {
      throw new FilesystemException(oException.getMessage(),oException);
    } finally {
      StreamHelper.close(oInput);
    }
  }
 
}