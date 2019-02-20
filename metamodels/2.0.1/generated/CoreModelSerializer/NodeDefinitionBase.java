package org.monet.kernel.model.definition;

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

// NodeDefinition
// Declaración abstracta de un nodo. Un nodo es un elemento navegable del sistema de información

public abstract class NodeDefinitionBase extends EntityDefinition 
 {
@Root(name="is-singleton")
public static class IsSingleton {
}
@Root(name="is-component")
public static class IsComponent {
}
@Root(name="is-read-only")
public static class IsReadOnly {
}
@Root(name="is-duplicable")
public static class IsDuplicable {
}
@Root(name="is-private")
public static class IsPrivate {
}
@Root(name="is-georeferenced")
public static class IsGeoreferenced {
protected @Attribute(name="default-location",required=false) String _defaultLocation;
protected @Attribute(name="reference",required=false) String _reference;
protected @Attribute(name="view",required=false) String _view;
public String getDefaultLocation() { return _defaultLocation; }
public String getReference() { return _reference; }
public String getView() { return _view; }
}
@Root(name="allow-import")
public static class AllowImport {
protected @Attribute(name="importer",required=false) String _importer;
public String getImporter() { return _importer; }
}
@Root(name="schema")
public static class Schema {
protected @Text(data = true) String content;
public String getContent() { return content; }
}
@Root(name="implements")
public static class Implements {
protected @Attribute(name="reference",required=false) String _reference;
public String getReference() { return _reference; }
}

protected @Element(name="is-singleton",required=false) IsSingleton _isSingleton;
protected @Element(name="is-component",required=false) IsComponent _isComponent;
protected @Element(name="is-read-only",required=false) IsReadOnly _isReadOnly;
protected @Element(name="is-duplicable",required=false) IsDuplicable _isDuplicable;
protected @Element(name="is-private",required=false) IsPrivate _isPrivate;
protected @Element(name="is-georeferenced",required=false) IsGeoreferenced _isGeoreferenced;
protected @Element(name="allow-import",required=false) AllowImport _allowImport;
protected @Element(name="schema",required=false) Schema _schema;
protected @ElementList(inline=true,required=false) ArrayList<Implements> _implementsList = new ArrayList<Implements>();
protected @ElementList(inline=true,required=false) ArrayList<OperationDeclaration> _operationDeclarationList = new ArrayList<OperationDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<RuleDeclaration> _ruleDeclarationList = new ArrayList<RuleDeclaration>();

public boolean isSingleton() { return (_isSingleton != null); }
public IsSingleton getIsSingleton() { return _isSingleton; }
public boolean isComponent() { return (_isComponent != null); }
public IsComponent getIsComponent() { return _isComponent; }
public boolean isReadOnly() { return (_isReadOnly != null); }
public IsReadOnly getIsReadOnly() { return _isReadOnly; }
public boolean isDuplicable() { return (_isDuplicable != null); }
public IsDuplicable getIsDuplicable() { return _isDuplicable; }
public boolean isPrivate() { return (_isPrivate != null); }
public IsPrivate getIsPrivate() { return _isPrivate; }
public boolean isGeoreferenced() { return (_isGeoreferenced != null); }
public IsGeoreferenced getIsGeoreferenced() { return _isGeoreferenced; }
public boolean allowImport() { return (_allowImport != null); }
public AllowImport getAllowImport() { return _allowImport; }
public Schema getSchema() { return _schema; }
public ArrayList<Implements> getImplementsList() { return _implementsList; }
public ArrayList<OperationDeclaration> getOperationDeclarationList() { return _operationDeclarationList; }
public ArrayList<RuleDeclaration> getRuleDeclarationList() { return _ruleDeclarationList; }

}







































