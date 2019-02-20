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


public class LibraryFile {

  public static String getBasename(String sFilename) {
    Integer iPos = sFilename.lastIndexOf(".");
    if (iPos == -1) return null;
    return sFilename.substring(0, iPos);
  }
  
  public static String getDirname(String sFilename) {
    sFilename = LibraryString.replaceAll(sFilename, "\\", "/");
    Integer iPos = sFilename.lastIndexOf("/");
    if (iPos == -1) return null;
    return sFilename.substring(0, iPos);
  }

  public static String getFilename(String sFilename) {
    sFilename = sFilename.replaceAll("\\"+"\\", "/");
    Integer iPos = sFilename.lastIndexOf("/");
    if (iPos == -1) return sFilename;
    return sFilename.substring(iPos+1);
  }

  public static String getFilenameWithoutExtension(String sFilename) {
    sFilename = sFilename.replaceAll("\\"+"\\", "/");
    Integer iPos = sFilename.lastIndexOf("/");
    int iDotPos = sFilename.lastIndexOf(".");
    if (iPos == -1) return null;
    return sFilename.substring(iPos+1, iDotPos);
  }
  
  public static String getExtension(String sFilename) {
    Integer iPos = sFilename.lastIndexOf(".");
    if (iPos == -1) return null;
    return sFilename.substring(iPos+1);
  }

}