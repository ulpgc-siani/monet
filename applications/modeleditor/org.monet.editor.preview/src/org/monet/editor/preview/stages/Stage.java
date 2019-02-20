package org.monet.editor.preview.stages;

import java.util.ArrayList;
import java.util.List;

import org.monet.editor.preview.model.GlobalData;
import org.monet.editor.preview.model.Problem;

public abstract class Stage {

  protected GlobalData globalData;
  protected List<Problem> problems = new ArrayList<Problem>();
  
  public List<Problem> getProblems() {
    return problems;
  }
  
  public void setGlobalData(GlobalData gd) {
    this.globalData = gd;
  }

  public abstract void execute();

  public abstract String getStepInfo();
  
}
