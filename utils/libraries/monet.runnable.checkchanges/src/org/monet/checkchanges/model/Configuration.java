package org.monet.checkchanges.model;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="configuration", strict=false)
public class Configuration {
  @Attribute(name="check-all", required=false) boolean checkAll;
  @ElementList(name="target-file",entry="target-file", required=false, inline=true) List<TargetFile> targetFiles;
  @Element(name="target-database") TargetDatabase targetDatabase;
  @Element(name="target-userdata") TargetUserdata targetUserData;

  public boolean needCheckAll() { return checkAll; }
  public void setCheckAll(boolean checkAll) { this.checkAll = checkAll; }
  public List<TargetFile> getTargetFiles() { return targetFiles; }
  public TargetDatabase getTargeDatabaset() { return targetDatabase; }
  public TargetUserdata getTargetUserData() { return targetUserData; }
  
  public static class Target{
    protected @Attribute(name="check") boolean check;
    protected @Attribute(name="relative-path") String relativePath;
    public boolean needCheck() { return check; }
    public void setCheck(boolean check) { this.check = check;}
    public String getRelativePath( ) { return this.relativePath ; }
  }
  
  public static class TargetFile extends Target{}
  public static class TargetDatabase extends Target{}
  public static class TargetUserdata extends Target{}
}
