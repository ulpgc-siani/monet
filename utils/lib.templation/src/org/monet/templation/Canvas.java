package org.monet.templation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class Canvas {

  public static final String FROM_RESOURCES_PREFIX = "_resources_";

  private static class Log implements CanvasLogger {
    @Override public void debug(String message, Object... args) {
      System.out.printf(message, args);
    }
  }

  private static HashMap<String, Canvas> canvasMap = new HashMap<String, Canvas>();
  private static boolean disableCache = false;
  private String path;
  private CanvasLogger logger;
  private String key = "";
  private String filename = "";
  private Canvas aura;
  private boolean keepFormat = false;
  private ArrayList<String> sliceList = new ArrayList<String>();
  private HashMap<String, Canvas> blockMap = new HashMap<String, Canvas>();

  public CanvasLogger getLogger() {
    return this.logger;
  }

  public void injectLogger(CanvasLogger logger) {
    this.logger = logger;
  }

  public String getPath() {
    return this.path;
  }

  public void injectPath(String path) {
    this.path = path;
  }
  
  private Reader getFileReader(String template, String blockName) throws FileNotFoundException {
    String fileTemplate = (!template.isEmpty()?"/" + template:"") + "/" + blockName + ".tpl";
    InputStream inputStream = null;
    
    if (this.path.indexOf(FROM_RESOURCES_PREFIX) != -1) {
      this.filename = path.replace(FROM_RESOURCES_PREFIX, "") + fileTemplate;
      inputStream = Canvas.class.getResourceAsStream(this.filename);
    }
    else {
      this.filename = path + fileTemplate;
      File file = new File(this.filename);
      if (!file.exists()) {
        this.filename = path + (!template.isEmpty()?"/" + template:"") + "/" + blockName;
        file = new File(this.filename);
        if (!file.exists()) {
          logger.debug("RenderBlock: Template '%s' doesn't exist\n", template);
          return null;
        }
      }
      inputStream = new FileInputStream(file);
    }
    
    try {
      return new InputStreamReader(inputStream,  "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
  
  public Canvas(CanvasLogger logger, String path, String template, String blockName, boolean keepFormat) {
  	Reader reader = null;
  	
    this.logger = (logger != null)?logger:new Log();
    this.path = path;
    this.key = template + "." + blockName;
    this.keepFormat = keepFormat;
    
    try {
      reader = this.getFileReader(template, blockName);
      analize(reader);
      canvasMap.put(this.key, this);
  
      String name = "";
      while (true) {
        int d = -1;
        d = reader.read();
        if (d == -1) break;
  
        char c = (char) d;
        if (c == '\r') continue;
        if (c == '\n') {
          Canvas block = new Canvas(this.logger, this.filename, name, reader, keepFormat);
          blockMap.put(name, block);
          name = "";
          continue;
        }
        name += c;
      }
    }
    catch (FileNotFoundException e) {
      logger.debug("Canvas: opening stream error \n %s\n", e.getMessage());
      return;
    }
    catch (IOException e) {
    	logger.debug("Canvas: reading stream error \n %s\n", e.getMessage());
      return;
    }
    finally {
      try {
        reader.close();
      } catch(Exception e) {}
    }
  }

  protected Canvas(CanvasLogger logger, String filename, String name, Reader reader, boolean keepFormat) throws IOException {
    this.logger = logger;
    this.filename = filename;
	this.keepFormat = keepFormat;
    analize(reader);
  }

  public static Canvas get(String template, String blockName) {
    String key = template + "." + blockName;
    if (System.getProperty("monet.debug") != null || disableCache) return null;
    return canvasMap.get(key);
  }
  
  public static void disableCache() {
    disableCache = true;
  }
  
  public void setKeepFormat(boolean value) {
    this.keepFormat = value;
  }
  
  public boolean getKeepFormat() {
    return this.keepFormat;
  }

  public Canvas getBlock(String blockName) {
    Canvas scope = this;
    do {
      Canvas block = scope.blockMap.get(blockName);
      if (block != null) return block;
      scope = scope.aura;
    }
    while (scope != null);
    
    return null; 
  }

  private void analize(Reader reader) throws IOException {
    String slice = "";
    boolean isColon = false;
    boolean isEscape = false;
    boolean isNewLine = true;
    boolean isComment = false;

    while (true) {
      int d = -1;
      d = reader.read();
      if (d == -1) break;

      char c = (char) d;
      if (isComment) {
        if (c == '\n') isComment = false;
        continue;
      }

      if (isEscape) {
        slice += c;
        isEscape = false;
        continue;
      }
      if (c == '\\') {
        isEscape = true;
        continue;
      }
      if (c == '\r' && !this.keepFormat) {
        continue;
      }
      if (c == '\n' && !this.keepFormat) {
        isNewLine = true;
        continue;
      }
      if (c == '#') {
        isComment = true;
        continue;
      }
      if (c == '@') break;

      if ((isNewLine) && (c <= ' ')) {
        continue;
      }
      isNewLine = false;

      if (c == ':') {
        if (isColon == false) {
          isColon = true;
          continue;
        }
        isColon = false;
        sliceList.add(slice);
        slice = "";
        continue;
      }

      if (isColon) {
        isColon = false;
        slice += ':';
      }
      slice += c;
    }
    sliceList.add(slice);
  }

  public Canvas getAura() {
    return this.aura;
  }

  public void setAura(Canvas canvas) {
    if (this.aura == canvas) return;

    if (this.aura == null) 
      this.aura = canvas;
    else 
      this.aura.setAura(canvas);
  }

  public String generate(HashMap<String, Object> markMap, String soul) {
    return replaceMarks(markMap, soul);
  }

  public String generate(HashMap<String, Object> markMap) {
    return replaceMarks(markMap, "");
  }

  public String generate() {
    return replaceMarks(null, "");
  }

  private String replaceMarks(HashMap<String, Object> markMap, String soul) {
    String result, key, token;

    if (markMap == null) return "";
    
    int size = sliceList.size();
    if (size <= 0) return "";

    result = sliceList.get(0);

    for (int i = 1; i < sliceList.size(); i += 2) {
      key = sliceList.get(i);
      token = "";
      if (key.equals("+")) 
        token = soul;
      else if (key.contains("|")) {
        String[] keyex = key.split("\\|");
        key = keyex[0];
        if (markMap.containsKey(key)) token = markMap.get(key).toString();
        if (!token.isEmpty()) token = keyex[1].replace("*", token);
      }
      else if (markMap.containsKey(key)) {
        Object value = markMap.get(key);
        token = value != null ? value.toString() : null;
      } else 
        logger.debug("Canvas.replaceMarks: key '%s' doesn't exist in '%s'\n", key, filename);

      token = token != null ? token : "";
      result += token + sliceList.get(i + 1);
    }

    if (this.aura != null) result = this.aura.replaceMarks(markMap, result);

    return result;
  }

}
