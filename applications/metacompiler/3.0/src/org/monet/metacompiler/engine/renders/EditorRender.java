package org.monet.metacompiler.engine.renders;

import java.util.HashSet;

import org.monet.core.metamodel.MetaSyntagma;

public abstract class EditorRender extends MetaModelRender {

  protected MetaSyntagma metasyntagma;
  protected HashSet<String> hashSet;

  public EditorRender() {
    super();
  }
  
  public void setMetaSyntagma(MetaSyntagma metasyntagma) {
    this.metasyntagma = metasyntagma;
  }

  public void setRuleSet(HashSet<String> hashSet) {
    this.hashSet = hashSet;
  }
    
}
