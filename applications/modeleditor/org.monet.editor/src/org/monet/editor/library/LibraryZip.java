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

package org.monet.editor.library;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.monet.editor.MonetLog;

public class LibraryZip {
  
  private static final Integer BUFFER_SIZE = 8192;
  
  public static Boolean decompress(String filename, String destination) {
    return decompress(new File(filename), destination);
  }
  
  public static Boolean decompress(File file, String destination) {
    FileInputStream origin;
    try {
      origin = new FileInputStream(file);
      return decompress(origin, destination);
    } catch (Exception e) {
      MonetLog.print(e);
      return false;
    }
  }
  
  public static Boolean decompress(InputStream origin, String destination) {
    BufferedOutputStream destinationStream;
    ZipInputStream input = null; 
    
    try {
      destinationStream = null;
      input = new ZipInputStream(origin);

      int count;
      byte data[] = new byte[BUFFER_SIZE];
   
      ZipEntry entry;
      while ((entry = input.getNextEntry()) != null) {
        if (entry.isDirectory()) new File(destination + File.separator + entry.getName()).mkdirs();
        else {
          String destDN = LibraryFile.getDirname(destination + File.separator + entry.getName()); 
          String destFN = destination + File.separator + entry.getName();
          
          new File(destDN).mkdirs();

          FileOutputStream output = new FileOutputStream(destFN);
          destinationStream = new BufferedOutputStream(output, BUFFER_SIZE);
          
          while((count = input.read(data, 0, BUFFER_SIZE)) != -1 ) {
            destinationStream.write (data, 0, count);
          }
          
          destinationStream.flush();
          destinationStream.close();
        }
      }
    
    }
    catch (Exception e) {
      MonetLog.print(e);
      return false;
    } finally {
      StreamHelper.close(input);
    }
   
    return true;
  }
 
}