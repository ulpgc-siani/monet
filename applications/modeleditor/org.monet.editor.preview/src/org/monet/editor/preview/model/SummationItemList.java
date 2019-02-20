package org.monet.editor.preview.model;

import java.util.ArrayList;
import java.util.List;

import org.monet.metamodel.SummationItemProperty;

public class SummationItemList {
  public ArrayList<SummationItem> Items = new ArrayList<SummationItem>();

  public SummationItemList() {
  }

  public SummationItemList(List<SummationItemProperty> itemDeclarationList) {
    this.Items.clear();
    
    for (SummationItemProperty itemDeclaration : itemDeclarationList) {
      this.Items.add(new SummationItem(itemDeclaration));
    }
  }
  
}
