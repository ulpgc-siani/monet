package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Root(name = "model", strict = false)
public class Model {

  public static class Version {

    @Attribute(name = "date")
    private long   timestamp;
    @Attribute
    private String compilation;
    @Attribute(name = "metamodel")
    private String metaModel;

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setCompilation(String compilation) {
      this.compilation = compilation;
    }

    public String getCompilation() {
      return compilation;
    }

    public void setMetaModel(String metaModel) {
      this.metaModel = metaModel;
    }

    public String getMetaModel() {
      return this.metaModel;
    }
  }

  protected @Attribute(name = "code")
  String              code;
  protected @Attribute(name = "name")
  String              name;

  protected @ElementMap(entry = "label", key = "language", attribute = true, inline = true)
  Map<String, String> labelMap       = new HashMap<String, String>();
  protected @ElementMap(entry = "description", key = "language", attribute = true, inline = true, required = false)
  Map<String, String> descriptionMap = new HashMap<String, String>();
  private @Element(name = "version")
  Version             version;

  public String getCode() {
    return this.code;
  }

  public String getLabel(String language) {
    if (labelMap.get(language) == null)
      return "";
    return labelMap.get(language);
  }

  public Collection<String> getLabels() {
    return labelMap.values();
  }

  public String getDescription(String language) {
    if (descriptionMap.get(language) == null)
      return "";
    return descriptionMap.get(language);
  }

  public Collection<String> getDescriptions() {
    return descriptionMap.values();
  }

  public String getLabel() {
    return this.getLabel("es");
  }

  public String getDescription() {
    return this.getDescription("es");
  }

  public void setVersion(Version version) {
    this.version = version;
  }

  public Version getVersion() {
    return version;
  }
}
