package org.monet.editor.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

public class LanguageUtil {

  public static HashMap<String, String> getLanguages() {
    ArrayList<String> languageCodes = new ArrayList<String>();
    HashMap<String, String> languagesMap = new HashMap<String, String>();
    LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
    
    for(Locale locale : Locale.getAvailableLocales()) {
      StringBuilder codeBuilder = new StringBuilder();
      
      codeBuilder.append(locale.getLanguage());
      if(!locale.getCountry().isEmpty()) {
        codeBuilder.append("-");
        codeBuilder.append(locale.getCountry());
      }

      String code = codeBuilder.toString();
      languagesMap.put(code, locale.getDisplayName());
      languageCodes.add(code);
    }
    
    Collections.sort(languageCodes);

    for (String languageCode : languageCodes) {
      String languageLabel = languagesMap.get(languageCode);
      result.put(languageCode, languageLabel);
    }
    
    return result;
  }

}