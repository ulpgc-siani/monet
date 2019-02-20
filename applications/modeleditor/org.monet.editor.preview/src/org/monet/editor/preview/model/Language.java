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

package org.monet.editor.preview.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;

public class Language {
  private static Language             oInstance;
  protected HashMap<String, Properties> hmLabels;
  protected HashMap<String, Properties> hmMessages;
  protected HashMap<String, Properties> hmErrorMessages;
  
  protected static HashSet<String> supportedLanguages;
  
  public static final String ENGLISH = "en";
  public static final String SPANISH = "es";
  public static final String DEUTSCH = "de";
  public static final String FRENCH = "ft";  
  public static final String PORTUGUESE = "pt";
  
  public static final String USER_LANGUAGE = "USER_LANGUAGE";
  
  protected static final Integer TYPE_LABELS = 1;
  protected static final Integer TYPE_MESSAGES = 2;
  protected static final Integer TYPE_ERROR_MESSAGES = 3;
  
  protected Language() {
    this.hmLabels = new HashMap<String, Properties>();
    this.hmMessages = new HashMap<String, Properties>();
    this.hmErrorMessages = new HashMap<String, Properties>();
  }
    
  public static void initLanguages() {
    if(supportedLanguages == null) {
      supportedLanguages = new HashSet<String>();
      supportedLanguages.add(SPANISH);
    }
  }
  
  public synchronized static Language getInstance() {
    if (oInstance == null) { oInstance = new Language(); }
    return oInstance;
  }
  
  public String getModelResource(Object res, String codeLanguage) {
    if(res == null)
      return null;
    if(res instanceof String)
      return (String)res;
    Integer resId = (Integer)res;
    
    String[] parts = codeLanguage.split("-");
    String langCode = parts[0];
    String countryCode = parts.length > 1 ? parts[1] : null;
    Locale locale = countryCode != null ? new Locale(langCode, countryCode) : new Locale(langCode);
    
    int iResId = resId.intValue();
    String resource = null;
    Dictionary dictionary = Dictionary.getInstance();
    org.monet.metamodel.interfaces.Language language = dictionary.getLanguage(codeLanguage);
    if(language != null) //Language exists
      resource = language.get(iResId);
    if(!locale.getLanguage().equals(codeLanguage) && (language == null || resource == null)) {
      //Check only with locale language
      language = dictionary.getLanguage(locale.getLanguage());
      
      if(language != null)
        resource = language.get(iResId);
    }
    
    if(resource == null) {
      //From default language
      language = dictionary.getDefaultLanguage();
      resource = language.get(iResId);
    }
      
    return resource;
  }

}