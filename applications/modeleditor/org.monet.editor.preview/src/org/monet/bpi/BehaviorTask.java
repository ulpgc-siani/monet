package org.monet.bpi;


public interface BehaviorTask  {

  public void onCreate();

  public void onInitialize();
  
  public void onAbort();
  
  public void onTerminate();
  
  public void onArrivePlace(final String placeCode);
  
  public void onTimeoutPlace(final String placeCode, final String actionCode);
  
  public void onTakePlace(final String placeCode, final String actionCode, final String routeCode);
  
  public void onSolveAction(final String placeCode, final String actionCode, final Node form);
  
  public void onCreateJobAction(final String placeCode, final String actionCode, final JobRequest msg);
  
  public void onFinishedJobAction(final String placeCode, final String actionCode, final JobResponse msg);

  public void onSelectDelegationRole(final String placeCode, final String actionCode, final RoleChooser roleChooser);

  public void onSetupDelegation(final String placeCode, final String actionCode, final DelegationSetup delegationSetup);

  public void onValidateForm(final String placeCode, final String actionCode, final Node form, final ValidationResult validationResult);
  
  public String onCalculateClassificatorPlace(final String placeCode, final String actionCode);

}