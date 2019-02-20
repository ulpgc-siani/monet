/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.api.space.backservice.impl.library;

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

public class LibraryZip {
  
  private static final Integer BUFFER_SIZE = 8192;
  
  public static Boolean compress(ArrayList<String> aFiles, String sDestinationFilename) {
    BufferedInputStream oOrigin = null;
    FileOutputStream oDestination;
    ZipOutputStream oOutput;
    Iterator<String> oIterator;
    byte[] aData;
    
    try {
      oDestination = new FileOutputStream(sDestinationFilename);
      oOutput = new ZipOutputStream(new BufferedOutputStream(oDestination));

      aData = new byte[BUFFER_SIZE];

      oIterator=aFiles.iterator();
      while (oIterator.hasNext()) {
        String sFilename = (String)oIterator.next();
  
        FileInputStream fisInput = new FileInputStream(sFilename);
        oOrigin = new BufferedInputStream(fisInput, BUFFER_SIZE);
  
        ZipEntry oEntry = new ZipEntry(sFilename);
        oOutput.putNextEntry(oEntry);

        int iCount;
        while( (iCount = oOrigin.read(aData, 0, BUFFER_SIZE)) != -1) { oOutput.write(aData, 0, iCount); }

        oOrigin.close();
      }

      oOutput.close();
    }
    catch (Exception e) {
      return false;
    }
    
    return true;
  }

  public static Boolean decompress(String sFilename, String sDestination) {
    return decompress(new File(sFilename), sDestination);
  }
  
  public static Boolean decompress(File oFile, String sDestination) {
    BufferedOutputStream oDestination;
    FileInputStream oOrigin;
    ZipInputStream oInput; 
    
    try {
      oDestination = null;
      oOrigin = new FileInputStream(oFile);
      oInput = new ZipInputStream(new BufferedInputStream(oOrigin));

      int iCount;
      byte aData[] = new byte[BUFFER_SIZE];
   
      ZipEntry oEntry;
      while ((oEntry = oInput.getNextEntry()) != null) {
        if (oEntry.isDirectory()) new File(sDestination + File.separator + oEntry.getName()).mkdirs();
        else {
          String sDestDN = LibraryFile.getDirname(sDestination + File.separator + oEntry.getName()); 
          String sDestFN = sDestination + File.separator + oEntry.getName();
          
          new File(sDestDN).mkdirs();

          FileOutputStream oOutput = new FileOutputStream(sDestFN);
          oDestination = new BufferedOutputStream(oOutput, BUFFER_SIZE);
          
          while((iCount = oInput.read(aData, 0, BUFFER_SIZE)) != -1 ) {
            oDestination.write (aData, 0, iCount);
          }
          
          oDestination.flush();
          oDestination.close();
        }
      }
    
      oInput.close();
    }
    catch (Exception e) {
      return false;
    }
   
    return true;
  }
 
}