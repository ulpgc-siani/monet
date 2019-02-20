package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class WorklineDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(WorklineDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                      
                      
            if(metaitem.getFrom() != null) {
      WorklineDeclaration.From item = metaitem.getFrom();
                    if(item.getWorkplace() != null && !item.getWorkplace().isEmpty())
      this.checkName(item.getWorkplace(), WorkplaceDeclaration.class);
            }
                  WorkstopDeclarationSemanticChecks workstopDeclaration = new WorkstopDeclarationSemanticChecks();
      workstopDeclaration.setProblems(this.getProblems());
      workstopDeclaration.setModule(this.getModule());
      for(WorkstopDeclaration include : metaitem.getWorkstopDeclarationList()) {
        workstopDeclaration.check(include);
      }
                    BranchLockDeclarationSemanticChecks branchLockDeclaration = new BranchLockDeclarationSemanticChecks();
      branchLockDeclaration.setProblems(this.getProblems());
      branchLockDeclaration.setModule(this.getModule());
      for(BranchLockDeclaration include : metaitem.getBranchLockDeclarationList()) {
        branchLockDeclaration.check(include);
      }
                  DecisionLockDeclarationSemanticChecks decisionLockDeclaration = new DecisionLockDeclarationSemanticChecks();
      decisionLockDeclaration.setProblems(this.getProblems());
      decisionLockDeclaration.setModule(this.getModule());
      for(DecisionLockDeclaration include : metaitem.getDecisionLockDeclarationList()) {
        decisionLockDeclaration.check(include);
      }
                  FormLockDeclarationSemanticChecks formLockDeclaration = new FormLockDeclarationSemanticChecks();
      formLockDeclaration.setProblems(this.getProblems());
      formLockDeclaration.setModule(this.getModule());
      for(FormLockDeclaration include : metaitem.getFormLockDeclarationList()) {
        formLockDeclaration.check(include);
      }
                  ServiceLockDeclarationSemanticChecks serviceLockDeclaration = new ServiceLockDeclarationSemanticChecks();
      serviceLockDeclaration.setProblems(this.getProblems());
      serviceLockDeclaration.setModule(this.getModule());
      for(ServiceLockDeclaration include : metaitem.getServiceLockDeclarationList()) {
        serviceLockDeclaration.check(include);
      }
                  SyncLockDeclarationSemanticChecks syncLockDeclaration = new SyncLockDeclarationSemanticChecks();
      syncLockDeclaration.setProblems(this.getProblems());
      syncLockDeclaration.setModule(this.getModule());
      for(SyncLockDeclaration include : metaitem.getSyncLockDeclarationList()) {
        syncLockDeclaration.check(include);
      }
                  TimerLockDeclarationSemanticChecks timerLockDeclaration = new TimerLockDeclarationSemanticChecks();
      timerLockDeclaration.setProblems(this.getProblems());
      timerLockDeclaration.setModule(this.getModule());
      for(TimerLockDeclaration include : metaitem.getTimerLockDeclarationList()) {
        timerLockDeclaration.check(include);
      }
        }
}


















