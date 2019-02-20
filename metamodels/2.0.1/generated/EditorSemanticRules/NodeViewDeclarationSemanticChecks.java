package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class NodeViewDeclarationSemanticChecks extends ViewDeclarationSemanticChecks {

  public void check(NodeViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            
                      
            if(metaitem.getShowSet() != null) {
      NodeViewDeclaration.ShowSet item = metaitem.getShowSet();
                          }
          
            if(metaitem.getElementsPerPage() != null) {
      NodeViewDeclaration.ElementsPerPage item = metaitem.getElementsPerPage();
                          }
                  SelectDeclarationSemanticChecks selectDeclaration = new SelectDeclarationSemanticChecks();
      selectDeclaration.setProblems(this.getProblems());
      selectDeclaration.setModule(this.getModule());
      for(SelectDeclaration include : metaitem.getSelectDeclarationList()) {
        selectDeclaration.check(include);
      }
                  FilterDeclarationSemanticChecks filterDeclaration = new FilterDeclarationSemanticChecks();
      filterDeclaration.setProblems(this.getProblems());
      filterDeclaration.setModule(this.getModule());
      for(FilterDeclaration include : metaitem.getFilterDeclarationList()) {
        filterDeclaration.check(include);
      }
                  SortDeclarationSemanticChecks sortDeclaration = new SortDeclarationSemanticChecks();
      sortDeclaration.setProblems(this.getProblems());
      sortDeclaration.setModule(this.getModule());
      for(SortDeclaration include : metaitem.getSortDeclarationList()) {
        sortDeclaration.check(include);
      }
                  GroupDeclarationSemanticChecks groupDeclaration = new GroupDeclarationSemanticChecks();
      groupDeclaration.setProblems(this.getProblems());
      groupDeclaration.setModule(this.getModule());
      for(GroupDeclaration include : metaitem.getGroupDeclarationList()) {
        groupDeclaration.check(include);
      }
        }
}


















