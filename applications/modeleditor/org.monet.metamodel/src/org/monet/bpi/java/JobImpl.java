package org.monet.bpi.java;

import org.monet.bpi.BehaviorJob;
import org.monet.bpi.DelegationSetup;
import org.monet.bpi.Job;
import org.monet.bpi.JobRequest;
import org.monet.bpi.JobResponse;
import org.monet.bpi.JobSetup;
import org.monet.bpi.MonetLink;
import org.monet.bpi.Node;
import org.monet.bpi.Role;
import org.monet.bpi.RoleChooser;
import org.monet.bpi.TimeoutSetup;
import org.monet.bpi.ValidationResult;
import org.monet.bpi.WaitSetup;
import org.monet.bpi.types.Date;

public class JobImpl extends TaskImpl implements BehaviorJob {

  @Override
  public void addLog(String title, String text, Iterable<MonetLink> links) {
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
  public void onValidateForm(String placeCode, String actionCode, Node form, ValidationResult validationResult) {
    // TODO Auto-generated method stub

  }

  @Override
  public String onCalculateClassificatorPlace(String placeCode, String actionCode) {
    // TODO Auto-generated method stub
    return null;
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

  public void onSetupEdition(final String placeCode, final String actionCode, Node form) {
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

}
