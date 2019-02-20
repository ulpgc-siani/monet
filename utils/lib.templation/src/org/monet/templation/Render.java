package org.monet.templation;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class Render {
  private Canvas canvas;
  private Map<String, Object> parameters;
  protected String template;
  protected String name;
  protected Object target;
  protected HashMap<String, Object> markMap;
  protected CanvasLogger logger;
  protected String path;

  public Render(CanvasLogger logger, String path) {
    this.logger = logger;
    this.path = path;
    this.template = "";
    this.name = "";
    this.parameters = new HashMap<String, Object>();
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Map<String, Object> getParameters() {
    return this.parameters;
  }

  public Object getParameter(String name) {
    return this.parameters.get(name);
  }

  public String getParameterAsString(String name) {
    String value = (String)this.parameters.get(name);
    return (value != null)?value:"";
  }

  public void setParameter(String key, Object value) {
    this.parameters.put(key, value);
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters.putAll(parameters);
  }

  public void setParameters(String parameters) {
    String[] parametersArray;

    if (parameters == null) return; 

    parametersArray = parameters.split("&");

    this.parameters.clear();
    for (String parameter : parametersArray) {
      String[] parameterArray = parameter.split("=");
      if (parameterArray.length < 2) continue;
      if ((parameterArray[0] != null) && (parameterArray[1] != null)) {
        this.parameters.put(parameterArray[0], parameterArray[1]);
      }
    }
  }

  public void setTarget(Object target) {
    this.target = target;
  }

  protected abstract void init();

  public String getOutput() {
    this.markMap = new HashMap<>();
    
    init();

    if (this.canvas == null) 
      return "";
    
    return this.canvas.generate(this.markMap); 
  }

  protected void loadCanvas(String name) {
    this.loadCanvas(name, false);
  }
  
  protected void loadCanvas(String name, boolean keepLinebreaks) {

    Canvas canvas = Canvas.get(this.template, name);
    
    if (canvas == null) {
      canvas = new Canvas(this.logger, this.path, this.template, name, keepLinebreaks);
    }

    if (this.canvas == null)
      this.canvas = canvas;
    else
      this.canvas.setAura(canvas);
  }

  protected boolean existsBlock(String blockName) {
    return (this.canvas.getBlock(blockName) != null);
  }

  protected String block(String blockName, HashMap<String, Object> map) {
    if (this.canvas.getBlock(blockName) == null) return "";

    String result = this.canvas.getBlock(blockName).generate(map);
    
    if (blockName.indexOf(":client-side") != -1) {
      result = StringEscapeUtils.escapeHtml(result);
    }
    
    return result;
  }

  protected String block(String blockName, String name, String value) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put(name, value);
    return block(blockName, map);
  }

  protected void addMark(String key, Object value) {
    Object data = this.markMap.get(key);
    data = (data == null) ? value : data.toString() + value;
    this.markMap.put(key, data);
  }

}
