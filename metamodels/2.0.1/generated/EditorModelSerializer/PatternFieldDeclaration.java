package org.monet.modelling.kernel.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

// PatternFieldDeclaration
// Declaración que se utiliza para modelar un campo de patron

@Root(name="field-pattern")
public  class PatternFieldDeclaration extends MultipleableFieldDeclaration 
 {

protected @ElementList(inline=true) ArrayList<PatternDeclaration> _patternDeclarationList = new ArrayList<PatternDeclaration>();

public ArrayList<PatternDeclaration> getPatternDeclarationList() { return _patternDeclarationList; }

}







































