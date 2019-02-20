package org.monet.mobile.service.results;

import org.monet.mobile.model.Task;
import org.monet.mobile.service.Result;

import java.util.List;

public class DownloadTasksResult extends Result {
  
  public List<Task> Tasks;
  public long SyncMark;  
  
}
