package org.monet.editor.preview.model;

import java.util.ArrayList;

import org.monet.metamodel.SummationItemProperty;
import org.monet.metamodel.SummationItemPropertyBase.TypeEnumeration;

public class SummationItem {
  public String Key;
  public Object Label;
  public double Value;
  public TypeEnumeration Type = TypeEnumeration.SIMPLE;
  public boolean IsMultiple = false;
  public boolean IsNegative = false;
  public ArrayList<SummationItem> Children = new ArrayList<SummationItem>();

  public SummationItem() {
  }
  
  public SummationItem(SummationItemProperty itemDeclaration) {
    this.Key = itemDeclaration.getKey();
    this.Label = itemDeclaration.getLabel();
    this.Value = 0;
    this.Type = itemDeclaration.getType();
    this.IsMultiple = itemDeclaration.isMultiple();
    this.IsNegative = itemDeclaration.isNegative();
    
    for (SummationItemProperty childProperty : itemDeclaration.getSummationItemPropertyList()) {
      this.Children.add(new SummationItem(childProperty));
    }
  }
}
