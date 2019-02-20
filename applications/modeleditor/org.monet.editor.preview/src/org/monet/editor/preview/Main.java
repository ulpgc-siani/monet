package org.monet.editor.preview;

import java.util.List;

import net.sf.json.JSONArray;

import org.monet.editor.preview.model.GlobalData;
import org.monet.editor.preview.model.Problem;

public class Main {

  public static void main(String[] args) {
    
    if (args.length < 2) {
      System.out.println("Incorrect parameters. Parameters are: [Model directory], [Output directory]");
      System.exit(-1);
      return;
    }
    
    String language = "es";
    if (args[2] != null)
      language = args[2];
    
    try {
      PreviewPipeline previewPipeline = new PreviewPipeline();
      previewPipeline.setData(GlobalData.MODEL_DIRECTORY, args[0]);
      previewPipeline.setData(GlobalData.OUTPUT_DIRECTORY, args[1]);
      previewPipeline.setData(GlobalData.LANGUAGE, language);
      previewPipeline.process();
      
      List<Problem> problems = previewPipeline.getProblems();
      JSONArray jsonProblems = new JSONArray();
      for (Problem problem : problems) {
        jsonProblems.add(problem.toJson());
      }
      
      System.out.println(jsonProblems.toString());
    }
    catch (Exception ex) {
      System.out.println(ex.getStackTrace());
      System.exit(-1);
    }
    
    System.exit(0);
  }

}
