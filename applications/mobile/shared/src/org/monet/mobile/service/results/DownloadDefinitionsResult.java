package org.monet.mobile.service.results;

import java.util.ArrayList;

import org.monet.mobile.model.TaskDefinition;
import org.monet.mobile.service.Result;

public class DownloadDefinitionsResult extends Result {
  
  public ArrayList<TaskDefinition> Definitions;
  public long SyncMark;  
  
}
