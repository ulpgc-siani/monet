package org.monet.editor.preview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.monet.editor.preview.model.GlobalData;
import org.monet.editor.preview.model.Problem;
import org.monet.editor.preview.stages.CheckModel;
import org.monet.editor.preview.stages.CompileDefinitions;
import org.monet.editor.preview.stages.PrepareOutput;
import org.monet.editor.preview.stages.Stage;

public class PreviewPipeline implements GlobalData {

  private HashMap<String, Object> globalData = new HashMap<String, Object>();
  private ArrayList<Problem> problems = new ArrayList<Problem>();
  private ArrayList<Stage> stages = new ArrayList<Stage>();
  private int currentStage = 0;

  public PreviewPipeline() {
    this.initialize();
  }

  private void initialize() {
    stages.add(new CheckModel());
  	stages.add(new PrepareOutput());
  	stages.add(new CompileDefinitions());
  	
    for (Stage s : stages) {
      s.setGlobalData(this);
    }
  }

  public boolean hasNextStage() {
    return currentStage < stages.size();
  }

  public Stage getNextStage() {
    if (currentStage < stages.size())
      return stages.get(currentStage++);
    else
      return null;
  }

  public void process() {
    
    try {     
      while (this.hasNextStage()) {
        Stage currentStage = this.getNextStage();
        
        currentStage.execute();
        
        if (currentStage.getProblems().size() > 0) {
          addProblems(currentStage);
          if (currentStage instanceof CheckModel)              
            break;
        }
      }
    } catch (RuntimeException e) {
      throw e;
    }
  }

  private void addProblems(Stage currentStage) {
    for (Problem p : currentStage.getProblems()) {
      p.setStage(currentStage.getClass().getSimpleName());
      problems.add(p);
    }
  }

  public List<Problem> getProblems() {
    return this.problems;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getData(Class<T> type, String key) {
    return (T)globalData.get(key);
  }

  @Override
  public void setData(String key, Object data) {
    globalData.put(key, data);
  }
}
