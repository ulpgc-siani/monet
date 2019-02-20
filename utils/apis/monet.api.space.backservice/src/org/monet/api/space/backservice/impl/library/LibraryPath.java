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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryPath {
  private static LibraryPath oInstance;
  
  public static final String SEPARATOR = ".";
  private static final String EXPRESSION = "(\\[([^\\]]+)\\])|([^\\.]+)";
  
  private LibraryPath() {
  }
  
  private static String[] splitPath(String sPath) {
    Pattern oPattern = Pattern.compile(LibraryPath.EXPRESSION);
    Matcher oMatcher = oPattern.matcher(sPath);
    ArrayList<String> alResult = new ArrayList<String>();
    
    while (oMatcher.find()) {
    	alResult.add(oMatcher.group(0));
    }
    
    return alResult.toArray(new String[0]); 
  }

  public synchronized static LibraryPath getInstance() {
    if (oInstance == null) oInstance = new LibraryPath();
    return oInstance;
  }
  
  public static String constructPath(String codeAttribute, String codeIndicator) {
  	return codeAttribute + LibraryPath.SEPARATOR + codeIndicator;
  }

  public static String[] splitAttributePath(String sPath) {
  	return LibraryPath.splitPath(sPath);
  }

  public static String getAttributePath(String sPath) {
    String[] aResult, aPath;
    
    aPath = LibraryPath.splitPath(sPath);
    aResult = new String[aPath.length-1];
    System.arraycopy(aPath, 0, aResult, 0, aPath.length-1);
    
    return LibraryArray.implode(aResult, ".");
  }

  public static String getAttributeCode(String sPath) {
  	String[] aPath = LibraryPath.splitPath(sPath);
    if (aPath.length <= 0) return sPath;
    return LibraryString.removeBrackets(aPath[aPath.length-1]);
  }

  public static String getIndicatorCode(String sPath) {
  	String[] aPath = LibraryPath.splitPath(sPath);
  	if (aPath.length <= 0) return sPath;
  	return LibraryString.removeBrackets(aPath[aPath.length-1]);
  }

}
