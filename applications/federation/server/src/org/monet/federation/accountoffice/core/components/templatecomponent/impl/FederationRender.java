package org.monet.federation.accountoffice.core.components.templatecomponent.impl;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.TemplateParams;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.library.LibraryDate;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.templation.CanvasLogger;
import org.monet.templation.Render;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


public abstract class FederationRender extends Render {
  protected String lang;

  public static final class Parameter {
      public static final String CODE = "code";
      public static final String BUSINESS_UNIT = "businessUnit";
      public static final String BASE_URL = "baseUrl";
      public static final String REQUEST_TOKEN = "requestToken";
      public static final String USER = "user";
      public static final String SHOW_ERROR = "showError";
      public static final String SHOW_CAPTCHA = "showCaptcha";
      public static final String ADDITIONAL_MESSAGE = "additionalMessage";
      public static final String FEDERATION = "federation";
      public static final String RESULT = "result";
      public static final String RETRIES = "retries";
  }
  
  private static class RenderLogger implements CanvasLogger {
    @Override public void debug(String message, Object... args) {
      frLogger.error(message,args);
    }
  }
  
  protected static Logger frLogger;
  protected Configuration configuration;
  
  public FederationRender(Logger logger, Configuration configuration, String template){
    super(new RenderLogger(), configuration.getTemplatePath());
    
    frLogger = logger;
    this.configuration = configuration;
    this.template = template;
  }
   
  public FederationRender(Logger logger, String path) {
    super(new RenderLogger(), path);
    
    frLogger = logger;
  }
  
  public void addLanguagesMark(String lang) {
    HashMap<String,Object> map = new HashMap<String,Object>();
    String languages = "";
    Map<String,String> langs = this.configuration.getSelectableLanguages();
    Iterator<String> itr = langs.keySet().iterator();
    while(itr.hasNext()){
      String langKey = itr.next();
      map.put(TemplateParams.LANG, langKey);
      map.put(TemplateParams.LANG_LABEL, langs.get(langKey));
      if(langKey.equalsIgnoreCase(lang))
        map.put(TemplateParams.SELECTED, "selected");
      else 
        map.put(TemplateParams.SELECTED, "");
      languages += block(TemplateParams.BLOCK_OPTIONS, map);
    }
    addMark(TemplateParams.LANGUAGE_OPTIONS,languages);
  }
  
  protected String getCopyright(Properties props) {
    return props.getProperty(TemplateComponent.COPYRIGHT).replace("::year::", String.valueOf(LibraryDate.getCurrentYear()));
  }

  public abstract void init(String lang) ;
}
