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

package org.monet.api.federation.setupservice.impl.library;

import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

public class LibraryHTML {

  private static HashMap<String, String> htmlEntities;

  static {
    htmlEntities = new HashMap<String,String>();
    htmlEntities.put("&lt;","<"); 
    htmlEntities.put("&gt;",">");
    htmlEntities.put("&amp;","&"); 
    htmlEntities.put("&quot;","\"");
    htmlEntities.put("&aacute;","á"); 
    htmlEntities.put("&Aacute;","Á");
    htmlEntities.put("&agrave;","à"); 
    htmlEntities.put("&Agrave;","À");
    htmlEntities.put("&acirc;","â"); 
    htmlEntities.put("&auml;","ä");
    htmlEntities.put("&Auml;","Ä"); 
    htmlEntities.put("&Acirc;","Â");
    htmlEntities.put("&aring;","å"); 
    htmlEntities.put("&Aring;","Å");
    htmlEntities.put("&aelig;","æ"); 
    htmlEntities.put("&AElig;","Æ" );
    htmlEntities.put("&ccedil;","ç"); 
    htmlEntities.put("&Ccedil;","Ç");
    htmlEntities.put("&eacute;","é"); 
    htmlEntities.put("&Eacute;","É" );
    htmlEntities.put("&egrave;","è"); 
    htmlEntities.put("&Egrave;","È");
    htmlEntities.put("&ecirc;","ê"); 
    htmlEntities.put("&Ecirc;","Ê");
    htmlEntities.put("&euml;","ë"); 
    htmlEntities.put("&Euml;","Ë");
    htmlEntities.put("&iuml;","ï"); 
    htmlEntities.put("&Iuml;","Ï");
    htmlEntities.put("&ocirc;","ô"); 
    htmlEntities.put("&Ocirc;","Ô");
    htmlEntities.put("&ouml;","ö"); 
    htmlEntities.put("&Ouml;","Ö");
    htmlEntities.put("&oacute;","ó"); 
    htmlEntities.put("&Oacute;","Ó" );
    htmlEntities.put("&ograve;","ò"); 
    htmlEntities.put("&Ograve;","Ò");
    htmlEntities.put("&oslash;","ø"); 
    htmlEntities.put("&Oslash;","Ø");
    htmlEntities.put("&szlig;","ß"); 
    htmlEntities.put("&ugrave;","ù");
    htmlEntities.put("&Ugrave;","Ù"); 
    htmlEntities.put("&ucirc;","û");
    htmlEntities.put("&Ucirc;","Û"); 
    htmlEntities.put("&uuml;","ü");
    htmlEntities.put("&Uuml;","Ü"); 
    htmlEntities.put("&nbsp;"," ");
    htmlEntities.put("&copy;","\u00a9");
    htmlEntities.put("&reg;","\u00ae");
    htmlEntities.put("&euro;","\u20a0");
  }

  public static String encode(String sContent) {
    StringBuffer bfrOut = new StringBuffer();
    
    for(int i=0; i<sContent.length(); i++) {
      char c = sContent.charAt(i);
      if(c > 127 || c=='"' || c=='<' || c=='>') bfrOut.append("&#"+(int)c+";");
      else bfrOut.append(c);
    }
    
    return bfrOut.toString();  
  }
  
  public static final String escape(String content) {
  	return StringEscapeUtils.escapeHtml(content);
  }
  
  public static final String unescape(String content, int startPos){
    int i, j;

		i = content.indexOf("&", startPos);
		if (i > -1) {
		  j = content.indexOf(";", i);
		  if (j > i) {
		    String entityToLookFor = content.substring(i , j + 1);
		    String value = (String)htmlEntities.get(entityToLookFor);
		    if (value != null) {
		      content = new StringBuffer().append(content.substring(0 , i))
		                                  .append(value)
		                                  .append(content.substring(j + 1))
		                                  .toString();
		      return LibraryHTML.unescape(content, i + 1);
        }
      }
    }
    return content;
  }
  
}