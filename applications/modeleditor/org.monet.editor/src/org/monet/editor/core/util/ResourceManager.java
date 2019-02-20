package org.monet.editor.core.util;

import org.monet.editor.Constants;


public class ResourceManager {

	private static ResourceManager instance;
	private String internalPath;
	
	
	private ResourceManager(String internalPath){
		this.internalPath = internalPath;
	}
	
	public static void init(String internalPath){
		if (instance == null)
			instance = new ResourceManager(internalPath);
	}
	
	public static ResourceManager getInstance() {
	  return instance;
	}
	
	public String getTemplatesPath() {
		return internalPath + Constants.TEMPLATES_PATH;
	}
	
	public String getInternalResource(String key) {
	  return internalPath + key;
	}

  public String getMvmmanagerPath() {
    return internalPath + Constants.MSMANAGER_PATH;
  }
	
	
}
