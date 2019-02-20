package org.monet.install.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(strict=false)
public class Configuration {
  @Attribute(name="build-all", required=false) boolean buildAll;
  @Element(name="target-webxml", required=false) WebXML webxml;
  @Element(name="target-contextxml", required=false) ContextXML contextxml;
  @Element(name="target-classpath", required=false) Classpath classpath;
  @ElementList(name="target-generic",entry="target-generic", required=false, inline=true) List<Generic> generic;
  @Element(name="target-copy-userdata", required=false) CopyOthers copyOthers;
  @Element(name="target-database", required=false) Database database;

  public WebXML getWebxml() { return webxml; }
  public ContextXML getContextxml() { return contextxml; }
  public Classpath getClasspath() { return classpath; }
  public List<Generic> getGeneric() { return generic; }
  public CopyOthers getCopyOthers() { return copyOthers; }
  public Database getDatabase() { return this.database; }
  public boolean needBuildAll() { return this.buildAll; }
  public void setBuildAll(boolean buildAll) { this.buildAll = buildAll; }

  public static class Target{
    protected @Attribute(name="build") boolean build;
    protected @ElementList(name="option",entry="option",required=false, inline=true) List<Option> options;
    protected Map<String,String> optionsMap;
    public boolean needBuild() { return build; }
    public void setBuild(boolean build) { this.build = build; }
    public List<Option> getOptions() { return options; }
    public Object getOption(String optionName){
      getOptionsMap();
      return this.optionsMap.get(optionName);
    }
    public Map<String,String> getOptionsMap(){
      if(optionsMap == null){
        this.optionsMap = new HashMap<String,String>();
        for (int i = 0; i < options.size(); i++) {
          Option option = this.options.get(i);
          this.optionsMap.put(option.getName(), option.getValue());
        }
      }
      
      return optionsMap;
    }
  }

  public static class WebXML extends Target{
    @Attribute(name="display-name") String displayName;
    public String getDisplayName() { return displayName; }
  }
  
  public static class ContextXML extends Target{
    @Attribute(name="as-resource-link") boolean asResourceLink;
    public boolean isAsResourceLink() { return asResourceLink; }
  }
  
  public static class Classpath extends Target{ }

  public static class Generic extends Target{
    @Attribute(name="file") String filename;
    @Attribute(name="is-properties-file",required=false) boolean isPropertiesFile;
    @Attribute(name="move-user-data",required=false) boolean moveToUserData;
    public String getFileName() { return filename; }
    public boolean isPropertiesFile() { return isPropertiesFile; }
    public boolean needMoveToUserData() { return moveToUserData; }
  }

  public static class CopyOthers extends Target{
  }
  
  public static class Database extends Target{
    public static final String TYPE = "database-type";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String URL  = "url";
    @Attribute(name="folder") String folder;
    protected @ElementList(name="insert-scriptt",entry="insert-script",required=false, inline=true) List<InsertScript> insertScripts;
    public List<InsertScript> getInsertScripts() { return insertScripts; }
    public String getFolder() { return this.folder; }
  }

  public static class Option {
    @Attribute(name="name") String name;
    @Text String text;
    public String getName() { return name; }
    public String getValue() { return text; }
  }
  
  public static class InsertScript {
    @Attribute(name="file") String file;
    public String getFile() { return file; }
  }
}
