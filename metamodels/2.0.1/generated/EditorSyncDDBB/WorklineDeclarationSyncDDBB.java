package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class WorklineDeclarationSyncDDBB extends IndexedDeclarationSyncDDBB {

  public void sync(WorklineDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            WorkstopDeclarationSyncDDBB workstopDeclaration = new WorkstopDeclarationSyncDDBB();
      workstopDeclaration.setModule(this.getModule());
      for(WorkstopDeclaration include : metaitem.getWorkstopDeclarationList()) {
        workstopDeclaration.sync(include);
      }
                    BranchLockDeclarationSyncDDBB branchLockDeclaration = new BranchLockDeclarationSyncDDBB();
      branchLockDeclaration.setModule(this.getModule());
      for(BranchLockDeclaration include : metaitem.getBranchLockDeclarationList()) {
        branchLockDeclaration.sync(include);
      }
                  DecisionLockDeclarationSyncDDBB decisionLockDeclaration = new DecisionLockDeclarationSyncDDBB();
      decisionLockDeclaration.setModule(this.getModule());
      for(DecisionLockDeclaration include : metaitem.getDecisionLockDeclarationList()) {
        decisionLockDeclaration.sync(include);
      }
                  FormLockDeclarationSyncDDBB formLockDeclaration = new FormLockDeclarationSyncDDBB();
      formLockDeclaration.setModule(this.getModule());
      for(FormLockDeclaration include : metaitem.getFormLockDeclarationList()) {
        formLockDeclaration.sync(include);
      }
                  ServiceLockDeclarationSyncDDBB serviceLockDeclaration = new ServiceLockDeclarationSyncDDBB();
      serviceLockDeclaration.setModule(this.getModule());
      for(ServiceLockDeclaration include : metaitem.getServiceLockDeclarationList()) {
        serviceLockDeclaration.sync(include);
      }
                  SyncLockDeclarationSyncDDBB syncLockDeclaration = new SyncLockDeclarationSyncDDBB();
      syncLockDeclaration.setModule(this.getModule());
      for(SyncLockDeclaration include : metaitem.getSyncLockDeclarationList()) {
        syncLockDeclaration.sync(include);
      }
                  TimerLockDeclarationSyncDDBB timerLockDeclaration = new TimerLockDeclarationSyncDDBB();
      timerLockDeclaration.setModule(this.getModule());
      for(TimerLockDeclaration include : metaitem.getTimerLockDeclarationList()) {
        timerLockDeclaration.sync(include);
      }
        }
}


















