package org.monet.federation.accountoffice.core.model;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Text;

public class InfoUnit {
  @ElementList (entry="label", name="label", inline=true) List<EntryLabel> labels;
  @ElementList (entry="description", name="description", inline=true, required=false) List<EntryDescription> descriptions;
  
  public List<EntryLabel> getLabels(){ return labels;}
  public List<EntryDescription> getDescriptions(){ return descriptions;}
  
  public static class EntryLabel{
    @Attribute String lang;
    @Text String text;
    
    public String getLang(){ return lang; }
    public String getText(){ return text; }
  }
  
  public static class EntryDescription{
    @Attribute String lang;
    @Text (required=false)String text;
    
    public String getLang(){ return lang; }
    public String getText(){ return text; }
  }
}
