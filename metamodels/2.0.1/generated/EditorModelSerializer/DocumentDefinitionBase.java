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

// DocumentDefinition
// Declaración que se utiliza para modelar un documento

@Root(name="document")
public  class DocumentDefinitionBase extends NodeDefinition 
 {
@Root(name = "format"
)
public static class Format {
protected @Attribute(name="xsd",required=false) String _xsd;
public String getXsd() { return _xsd; }
public void setXsd(String value) { _xsd = value; }
}
@Root(name = "signature"
)
public static class Signature {
protected @Attribute(name="role",required=false) String _role;
public String getRole() { return _role; }
public void setRole(String value) { _role = value; }
}

protected @Element(name="format",required=false) Format _format;
protected @ElementList(inline=true,required=false) ArrayList<Signature> _signatureList = new ArrayList<Signature>();
protected @ElementList(inline=true,required=false) ArrayList<DocumentViewDeclaration> _documentViewDeclarationList = new ArrayList<DocumentViewDeclaration>();

public Format getFormat() { return _format; }
public void setFormat(Format value) { _format = value; }
public ArrayList<Signature> getSignatureList() { return _signatureList; }
public ArrayList<DocumentViewDeclaration> getDocumentViewDeclarationList() { return _documentViewDeclarationList; }

}







































