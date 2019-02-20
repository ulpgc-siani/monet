package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class NodeViewDeclarationSyncDDBB extends ViewDeclarationSyncDDBB {

  public void sync(NodeViewDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            SelectDeclarationSyncDDBB selectDeclaration = new SelectDeclarationSyncDDBB();
      selectDeclaration.setModule(this.getModule());
      for(SelectDeclaration include : metaitem.getSelectDeclarationList()) {
        selectDeclaration.sync(include);
      }
                  FilterDeclarationSyncDDBB filterDeclaration = new FilterDeclarationSyncDDBB();
      filterDeclaration.setModule(this.getModule());
      for(FilterDeclaration include : metaitem.getFilterDeclarationList()) {
        filterDeclaration.sync(include);
      }
                  SortDeclarationSyncDDBB sortDeclaration = new SortDeclarationSyncDDBB();
      sortDeclaration.setModule(this.getModule());
      for(SortDeclaration include : metaitem.getSortDeclarationList()) {
        sortDeclaration.sync(include);
      }
                  GroupDeclarationSyncDDBB groupDeclaration = new GroupDeclarationSyncDDBB();
      groupDeclaration.setModule(this.getModule());
      for(GroupDeclaration include : metaitem.getGroupDeclarationList()) {
        groupDeclaration.sync(include);
      }
        }
}


















