package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// PatternFieldDeclaration
// Declaración que se utiliza para modelar un campo de patron

@Root(name="field-pattern")
public  class PatternFieldDeclaration extends MultipleableFieldDeclaration 
 {

protected @ElementList(inline=true) ArrayList<PatternDeclaration> _patternDeclarationList = new ArrayList<PatternDeclaration>();

public ArrayList<PatternDeclaration> getPatternDeclarationList() { return _patternDeclarationList; }

}







































