package org.monet.editor.preview.renders;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Problem;
import org.monet.metamodel.NodeDefinition;

public class DictionaryRender extends PreviewRender {
  
  public DictionaryRender(String language) {
    super(language);
  }

  @Override protected void init() {
    loadCanvas("home");
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<NodeDefinition> environmentList = DictionaryRender.dictionary.getEnvironmentDefinitionList();
    
    if (environmentList.size() == 0)
      addMark("render(view.node)", block("noEnvironments", map));
    else {
      try {
        PreviewRender render = this.rendersFactory.get(environmentList.get(0), "page.html", this.codeLanguage);
        render.setParameters(this.getParameters());
        addMark("render(view.node)", render.getOutput());
      }
      catch (Exception exception) {
        this.problems.add(new Problem(String.format("Compiling definition %s", environmentList.get(0).getName()), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
      }
    }
  }

}
