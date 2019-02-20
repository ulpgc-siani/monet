package org.monet.editor.preview.stages;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.monet.editor.preview.model.GlobalData;
import org.monet.editor.preview.model.Problem;
import org.monet.editor.preview.utils.FilesystemHelper;
import org.monet.editor.preview.utils.Resources;

public class PrepareOutput extends Stage {

  private void copyResourcesToOutput(String outputDir) throws IOException {
    FilesystemHelper.forceDir(outputDir + "/images");
    FilesystemHelper.copyResourcesRecursively(Resources.get("/images"), new File(outputDir + "/images"));
    FilesystemHelper.forceDir(outputDir + "/javascript");
    FilesystemHelper.copyResourcesRecursively(Resources.get("/javascript"), new File(outputDir + "/javascript"));
    FilesystemHelper.forceDir(outputDir + "/styles");
    FilesystemHelper.copyResourcesRecursively(Resources.get("/styles"), new File(outputDir + "/styles"));
  }
  
  private void copyModelResourcesToOutput(String modelDir, String outputDir) throws IOException {
    FilesystemHelper.forceDir(outputDir + "/templates");
    FilesystemHelper.copyResourcesRecursively(new File(modelDir + "/res/templates").toURI().toURL(), new File(outputDir + "/templates"));
    FilesystemHelper.forceDir(outputDir + "/help");
    FilesystemHelper.copyResourcesRecursively(new File(modelDir + "/res/help").toURI().toURL(), new File(outputDir + "/help"));
    FilesystemHelper.forceDir(outputDir + "/images/model");
    FilesystemHelper.copyResourcesRecursively(new File(modelDir + "/res/images").toURI().toURL(), new File(outputDir + "/images/model"));
  }

  @Override
  public void execute() {
    String modelDir = this.globalData.getData(String.class, GlobalData.MODEL_DIRECTORY);
    String outputDir = this.globalData.getData(String.class, GlobalData.OUTPUT_DIRECTORY);

    try {
      FilesystemHelper.removeDir(outputDir);
      FilesystemHelper.createDir(outputDir);
      
      this.copyResourcesToOutput(outputDir);
      this.copyModelResourcesToOutput(modelDir, outputDir);
    }
    catch (IOException exception) {
      this.problems.add(new Problem(String.format("Error preparing output directories. Check directories exists: %s - %s.", modelDir, outputDir), exception.getClass().toString(), ExceptionUtils.getStackTrace(exception), Problem.SEVERITY_ERROR));
    }
    
  }

  @Override
  public String getStepInfo() {
    return "Prepare output";
  }

}
