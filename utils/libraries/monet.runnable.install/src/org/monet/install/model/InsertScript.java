package org.monet.install.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.monet.encrypt.Hasher;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name="script", strict=false)
public class InsertScript {
  @ElementList(entry="definition-base-insert", inline=true) List<DefinitionInsert> definitions;
  @ElementList(entry="insert", inline=true) List<Insert> inserts;
  public List<DefinitionInsert> getDefinitions() { return definitions; }
  public List<Insert> getInserts() { return inserts; }
  
  protected Map<String,DefinitionInsert> definitionsMap;
  public Object getDefinition(String definitionName){
    getDefinitionInsertMap();
    return this.definitionsMap.get(definitionName);
  }
  
  public Map<String,DefinitionInsert> getDefinitionInsertMap(){
    if(definitionsMap == null){
      this.definitionsMap = new HashMap<String,DefinitionInsert>();
      for (int i = 0; i < definitions.size(); i++) {
        DefinitionInsert param = this.definitions.get(i);
        this.definitionsMap.put(param.getName(), param);
      }
    }
    return definitionsMap;
  }

  public static class Definition{
    @ElementList(entry="param", inline=true) List<Param> params;
    protected Map<String,Object> paramsMap;
    public List<Param> getParams() { return params; }
    public Object getParamName(String paramName){
      getParmsMap();
      return this.paramsMap.get(paramName);
    }
    
    @SuppressWarnings("deprecation")
    public Map<String,Object> getParmsMap(){
      if(paramsMap == null){
        this.paramsMap = new HashMap<String,Object>();
        for (int i = 0; i < params.size(); i++) {
          Param param = this.params.get(i);
          Object value = param.getValue();
         
            try {
              if(param.needHash())
              value = Hasher.getMD5asHexadecimal(param.getValue());
            } catch (Exception e) {
             e.printStackTrace();
            }
            if(param.isDate()) value = new java.sql.Date(new Date(param.getValue()).getTime());
          this.paramsMap.put(param.getName(), value);
        }
      }
      return paramsMap;
    }
  }
  
  public static class DefinitionInsert extends Definition{
    @Attribute(name="name") String name;
    @Attribute(name="query") String query;
    public String getName() { return name; }
    public String getQuery() { return query; }
  }
  
  public static class Insert extends Definition{
    @Attribute(name="extends") String extend;
    public String getExtends() { return extend; }
  }
  
  public static class Param{
    @Attribute(name="name") String name;
    @Attribute(name="hash",required=false) String hash;
    @Attribute(name="isDate",required=false) boolean isDate;
    @Text String value;
    public String getName() { return name; }
    public String getValue() { return value; }
    public boolean needHash() { return hash != null; }
    public boolean isDate() { return isDate; }
  }
}
