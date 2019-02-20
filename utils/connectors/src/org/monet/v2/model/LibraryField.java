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

package org.monet.v2.model;

import org.monet.v2.model.constants.JSONField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryField {
  private static LibraryField oInstance;
  
  private LibraryField() {
  }
  
  public synchronized static LibraryField getInstance() {
    if (oInstance == null) oInstance = new LibraryField();
    return oInstance;
  }

  public static Boolean isMultiple(String sKey) {
    return sKey.substring(0, JSONField.MULTIPLE_FIELD_PREFIX.length()).equals(JSONField.MULTIPLE_FIELD_PREFIX);
  }

  public static Boolean isSimple(String sKey) {
    return sKey.substring(0, JSONField.SIMPLE_FIELD_PREFIX.length()).equals(JSONField.SIMPLE_FIELD_PREFIX);
  }

  public static Boolean isReference(String sKey) {
    return sKey.substring(0, JSONField.REFERENCE_FIELD_PREFIX.length()).equals(JSONField.REFERENCE_FIELD_PREFIX);
  }

  public static String getAttributeCode(String sKey) {
    Pattern oPattern = Pattern.compile(".\\\\[(.*)\\\\]\\\\[(.*)\\\\]");
    Matcher oMatcher = oPattern.matcher(sKey);
    
    if (!oMatcher.find()) {
      oPattern = Pattern.compile(".\\\\[(.*)\\\\]");
      oMatcher = oPattern.matcher(sKey);
      if (!oMatcher.find()) return null;
    }
    
    return oMatcher.group(1);
  }

  public static String getIndicatorCode(String sKey) {
    Pattern oPattern = Pattern.compile(".\\\\[(.*)\\\\]\\\\[(.*)\\\\]");
    Matcher oMatcher = oPattern.matcher(sKey);
    if (!oMatcher.find()) return null;
    return oMatcher.group(2);
  }

}
