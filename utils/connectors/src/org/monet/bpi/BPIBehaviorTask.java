package org.monet.bpi;


public interface BPIBehaviorTask<WorkPlace extends Enum<?>,
                                 WorkLine extends Enum<?>,
                                 WorkStop extends Enum<?>,
                                 Lock extends Enum<?>,
                                 ServiceUseLock extends Enum<?>,
                                 FormLock extends Enum<?>>  {

  public void onCreateTask();

  public void onAbortTask();

  public void onInitializeWorkMap();
  
  public void onTerminateWorkMap();
  
  public void onAbortWorkMap();

  public void onArriveWorkPlace(WorkStop workStopName, WorkPlace workPlaceName);

  public void onArriveWorkPlaceByException(WorkPlace workPlaceName);
  
  public void onLeaveWorkPlace(WorkPlace workPlaceName);

  public void onStartWorkLine(WorkLine workLineName);

  public void onTakeWorkStop(WorkStop workStopName);

  public void onSolveLock(Lock lockName, BPILock lock, WorkLine workLineName);

  public void onServiceLockCreated(ServiceUseLock lockName, BPILockService lock, BPIBaseNode<?> document);

  public void onFormLockCreated(FormLock lockName, BPILockForm lock, BPIBaseNode<?> form);
  
  public void onCalculateFactsAtStart();
  
  public void onCalculateFactsAtFinish();

}