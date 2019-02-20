package org.monet.editor.preview.stages;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.Dictionary;
import org.monet.editor.preview.model.GlobalData;
import org.monet.editor.preview.model.Manifest;
import org.monet.editor.preview.model.Problem;

public class CheckModel extends Stage {

  @Override
  public void execute() {
    Dictionary dictionary = Dictionary.getInstance();
    Manifest manifest = Manifest.getInstance();
    String modelDir = this.globalData.getData(String.class, GlobalData.MODEL_DIRECTORY);

    try {
      dictionary.initialize(modelDir);
      manifest.initialize(modelDir);
      this.problems.addAll(dictionary.getProblems());
    }
    catch (Throwable exception) {
      this.problems.add(new Problem("Error loading dictionary classes", exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
    }
    
  }

  @Override
  public String getStepInfo() {
    return "Check model";
  }

}
