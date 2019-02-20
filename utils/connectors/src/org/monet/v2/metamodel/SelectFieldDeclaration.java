package org.monet.v2.metamodel;


import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.core.Commit;

// SelectFieldDeclaration
// Declaración que se utiliza para modelar un	campo de selección

public class SelectFieldDeclaration extends SelectFieldDeclarationBase {

  private HashMap<String, String> termValues = new HashMap<String, String>();
  private HashMap<String, String> termCodes  = new HashMap<String, String>();

  @Commit
  public void commit() {
    Import importDeclaration = this.getImport();

    if (this._termIndexDeclaration != null)
      this.processTermList(this._termIndexDeclaration._termDeclarationList);

    if (importDeclaration != null && importDeclaration._depth == 0)
      importDeclaration._depth = 1;
  }

  private void processTermList(List<TermDeclaration> termList) {
    for (TermDeclaration term : termList) {
      this.termCodes.put(term._label, term._code);
      this.termValues.put(term._code, term._label);
      processTermList(term._termDeclarationList);
    }
  }

  public String getTermCode(String sValue) {
    return this.termCodes.get(sValue);
  }

  public String getTermValue(String code) {
    return this.termValues.get(code);
  }

}
