package org.monet.bpi;

import org.monet.bpi.types.Date;

public interface BehaviorTask {

	public void onCreate();

	public void onInitialize();

	public void onAbort();

	public void onTerminate();

	public void onArrivePlace(final String placeCode);

	public void onTimeoutPlace(final String placeCode, final String actionCode);

	public void onTakePlace(final String placeCode, final String actionCode, final String routeCode);

	public void onSolveAction(final String placeCode, final String actionCode, final Node form);

	public void onCreateJobAction(final String placeCode, final String actionCode, final JobRequest msg);

	public void onCreatedJobAction(final String placeCode, final String actionCode, final Job instance);

	public void onFinishedJobAction(final String placeCode, final String actionCode, final JobResponse msg);

	public void onSelectDelegationRole(final String placeCode, final String actionCode, final RoleChooser roleChooser);

	public void onSetupDelegation(final String placeCode, final String actionCode, final DelegationSetup delegationSetup);

    public void onSetupWait(final String placeCode, final String actionCode, final WaitSetup waitSetup);

    public void onSetupTimeout(final String placeCode, final String actionCode, final TimeoutSetup timeoutSetup);

	public void onSetupDelegationComplete(final String placeCode, final String actionCode, final String provider, final Date suggestedStartDate, final Date suggestedEndDate, final String observations, final boolean urgent);

	public void onSetupEdition(final String placeCode, final String actionCode, Node form);

	public void onSelectJobRole(final String placeCode, final String actionCode, final RoleChooser roleChooser);

	public void onSelectJobRoleComplete(final String placeCode, final String actionCode, final Role role);

	public void onSetupJob(final String placeCode, final String actionCode, final JobSetup jobSetup);

	public void onSetupJobComplete(final String placeCode, final String actionCode, final String provider, final Date suggestedStartDate, final Date suggestedEndDate, final String observations, final boolean urgent);

	public void onValidateForm(final String placeCode, final String actionCode, final Node form, final ValidationResult validationResult);

	public boolean onIsBackEnable(String placeCode, String actionCode);

	public void onBack(String placeCode, String actionCode);

	public String onCalculateClassificatorPlace(final String placeCode, final String actionCode);

	public void onAssign(User user);

	public void onUnassign();

}