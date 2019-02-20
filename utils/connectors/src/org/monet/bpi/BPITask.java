package org.monet.bpi;

import org.monet.bpi.types.Link;

public interface BPITask<Target extends BPIBaseNode<?>,
                         Input extends BPIBaseNode<?>,
                         Output extends BPIBaseNode<?>,
                         WorkPlace extends Enum<?>> {

  public String getId();
  
  public void setLabel(String label);
  
  public String getLabel();

  public void setDescription(String description);
  
  public <T extends Target> T getTarget();

  public void setTarget(Target target);

  public <T extends Input> T getInput();
  
  public void setInput(Input input);

  public <T extends Output> T getOutput();

  public void setOutput(Output output);

  public void keepLock(String id);

  public void throwException(WorkPlace workPlaceCode);
  
  public String getContextVariable(String name);

  public void setContextVariable(String name, String value);

  public void removeContextVariable(String name);
  
  public void addLog(String title, String text);
  
  public void addLog(String title, String text, Link link);
  
  public void save();
  
  public void resume();
  
  public void onCalculateFactsAtStart();
  
  public void onCalculateFactsAtFinish();
  
}