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

package org.monet.editor.preview.utils;

import java.util.Iterator;
import java.util.List;

public class StringHelper {
	
  public static String shortValue(String value) {
    return StringHelper.shortValue(value, 100);
  }

  public static String shortValue(String value, int size) {
    if (value == null) return null;
    if (value.length() <= size) return value;
    int length = value.length();
    int middle = (int)(Math.floor(size/2)*0.80);
    String leftValue = value.substring(0, middle);
    String rightValue = value.substring(length-middle, length);
    return leftValue + "..." + rightValue;
  }
  
  public static String replaceSpecialChars(String sData, String sReplacement) {
    sData = sData.replaceAll("\n", sReplacement);
    sData = sData.replaceAll("\r", sReplacement);
    sData = sData.replaceAll("\t", sReplacement);
    return sData;
  }
  
  public static String cleanSpecialChars(String sData) {
    return sData != null ? StringHelper.replaceSpecialChars(sData, "") : "";
  }

  public static String implode(List<String> items, String separator) {
    String result = "";
    Iterator<String> iterator = items.iterator();
    
    while (iterator.hasNext()) {
      result += iterator.next() + separator;
    }
    
    if (result.length() > 0) result = result.substring(0, result.length()-separator.length());
    
    return result;
  }

}