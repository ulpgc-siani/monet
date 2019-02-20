package org.monet.bpi.java;

import org.monet.bpi.*;
import org.monet.bpi.types.Date;


public class SensorImpl extends TaskImpl implements BehaviorSensor {

	@Override
	public void addLog(String title, String text, Iterable<MonetLink> link) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivePlace(String placeCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTimeoutPlace(String placeCode, String actionCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTakePlace(String placeCode, String actionCode, String routeCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSolveAction(String placeCode, String actionCode, Node form) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateJobAction(String placeCode, String actionCode, JobRequest msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreatedJobAction(String placeCode, String actionCode, Job instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishedJobAction(String placeCode, String actionCode, JobResponse msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSelectDelegationRole(String placeCode, String actionCode, RoleChooser roleChooser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetupDelegation(String placeCode, String actionCode, DelegationSetup delegationSetup) {
		// TODO Auto-generated method stub

	}

    @Override
    public void onSetupWait(String placeCode, String actionCode, WaitSetup waitSetup) {
        // TODO Auto-generated method stub

    }

	@Override
	public void onSetupTimeout(String placeCode, String actionCode, TimeoutSetup timeoutSetup) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSelectJobRole(String placeCode, String actionCode, RoleChooser roleChooser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSelectJobRoleComplete(String placeCode, String actionCode, Role role) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetupJob(String placeCode, String actionCode, JobSetup jobSetup) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetupJobComplete(String placeCode, String actionCode, String provider, Date suggestedStartDate, Date suggestedEndDate, String observations, boolean urgent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetupEdition(String placeCode, String actionCode, Node form) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValidateForm(String placeCode, String actionCode, Node form, ValidationResult validationResult) {
		// TODO Auto-generated method stub

	}

	@Override
	public String onCalculateClassificatorPlace(String placeCode, String actionCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onFinished(SensorResponse msg) {

	}

	@Override
	public boolean onIsBackEnable(String placeCode, String actionCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBack(String placeCode, String actionCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetupDelegationComplete(String placeCode, String actionCode, String provider, Date suggestedStartDate, Date suggestedEndDate, String observations, boolean urgent) {
		// TODO Auto-generated method stub

	}

}
