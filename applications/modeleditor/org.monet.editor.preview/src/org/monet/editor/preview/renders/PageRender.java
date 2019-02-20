package org.monet.editor.preview.renders;

import java.util.HashMap;
import java.util.List;

import org.monet.editor.preview.model.Manifest;
import org.monet.metamodel.NodeDefinition;

public abstract class PageRender extends PreviewRender {
  
  public PageRender(String language) {
    super(language);
  }

  @Override protected void init() {
    loadCanvas("page");
    
    Manifest manifest = Manifest.getInstance();
    
    addMark("path", this.getParameterAsString("path"));
    addMark("home", this.getParameterAsString("home"));
    addMark("organizationLogo", this.getParameterAsString("imagesPath") + "/model/organization.logo.png");
    addMark("organizationLogoSplash", this.getParameterAsString("imagesPath") + "/model/organization.splash.png");
    addMark("subtitle", language.getModelResource(manifest.getSubTitle(), this.codeLanguage));
    addMark("title", language.getModelResource(manifest.getTitle(), this.codeLanguage));
    addMark("modelLogo", this.getParameterAsString("imagesPath") + "/model/model.logo.png");
    addMark("modelLogoSplash", this.getParameterAsString("imagesPath") + "/model/model.splash.png");
    addMark("monetLogoSplash", this.getParameterAsString("imagesPath") + "/monet.splash.png");
    addMark("imagesPath", this.getParameterAsString("imagesPath"));
    addMark("language", this.codeLanguage);
    addMark("environments", this.initEnvironments());
    addMark("page", this.initPage());
  }

  protected String initPage() {
    return "";
  }
  
  protected String initEnvironments() {
    List<NodeDefinition> environmentDefinitionList = DictionaryRender.dictionary.getEnvironmentDefinitionList();
    StringBuilder environments = new StringBuilder();
    String home = this.getParameterAsString("home");
    
    for (NodeDefinition environmentDefinition : environmentDefinitionList) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      
      map.put("label", environmentDefinition.getLabelString(this.codeLanguage));
      map.put("name", environmentDefinition.getName());
      
      if (home.isEmpty()) home = environmentDefinition.getName();
      
      environments.append(block("environment", map));
    }

    this.markMap.put("home", home);

    return environments.toString();
  }

}
