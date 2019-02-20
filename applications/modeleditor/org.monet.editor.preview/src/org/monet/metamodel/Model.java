package org.monet.metamodel;

import java.util.ArrayList;

// Model
// Define todo lo relativo al modelo de negocio

public class Model {
  public enum TypeEnumeration {
    OFFICE, CUSTOMER_SUPPORT
  }

  public static class Label {
    protected String _language;
    protected String content;

    public String getLanguage() {
      return _language;
    }

    public void setLanguage(String value) {
      _language = value;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }

  public static class Description {
    protected String _language;
    protected String content;

    public String getLanguage() {
      return _language;
    }

    public void setLanguage(String value) {
      _language = value;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }

  public static class Version {
    protected long _date;
    protected String _compilation;
    protected String _metamodelVersion;

    public long getDate() {
      return _date;
    }

    public void setDate(long value) {
      _date = value;
    }

    public String getCompilation() {
      return _compilation;
    }

    public void setCompilation(String value) {
      _compilation = value;
    }

    public String getMetamodelVersion() {
      return _metamodelVersion;
    }

    public void setMetamodelVersion(String value) {
      _metamodelVersion = value;
    }
  }

  protected String _code;
  protected TypeEnumeration _type;
  protected ArrayList<Label> _labelList = new ArrayList<Label>();
  protected ArrayList<Description> _descriptionList = new ArrayList<Description>();
  protected Version _version;

  public String getCode() {
    return _code;
  }

  public void setCode(String value) {
    _code = value;
  }

  public TypeEnumeration getType() {
    return _type;
  }

  public void setType(TypeEnumeration value) {
    _type = value;
  }

  public ArrayList<Label> getLabelList() {
    return _labelList;
  }

  public ArrayList<Description> getDescriptionList() {
    return _descriptionList;
  }

  public Version getVersion() {
    return _version;
  }

  public void setVersion(Version value) {
    _version = value;
  }

}
