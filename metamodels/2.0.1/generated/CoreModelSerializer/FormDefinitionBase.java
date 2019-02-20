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

// FormDefinition
// Declaración que se utiliza para modelar un formulario

@Root(name="form")
public  class FormDefinitionBase extends NodeDefinition 
 {
@Root(name="is-extensible")
public static class IsExtensible {
}

protected @Element(name="is-extensible",required=false) IsExtensible _isExtensible;
protected @ElementList(inline=true,required=false) ArrayList<BooleanFieldDeclaration> _booleanFieldDeclarationList = new ArrayList<BooleanFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<CheckFieldDeclaration> _checkFieldDeclarationList = new ArrayList<CheckFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<DateFieldDeclaration> _dateFieldDeclarationList = new ArrayList<DateFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<FileFieldDeclaration> _fileFieldDeclarationList = new ArrayList<FileFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<LinkFieldDeclaration> _linkFieldDeclarationList = new ArrayList<LinkFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<LocationFieldDeclaration> _locationFieldDeclarationList = new ArrayList<LocationFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<MemoFieldDeclaration> _memoFieldDeclarationList = new ArrayList<MemoFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<NodeFieldDeclaration> _nodeFieldDeclarationList = new ArrayList<NodeFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<NumberFieldDeclaration> _numberFieldDeclarationList = new ArrayList<NumberFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<PatternFieldDeclaration> _patternFieldDeclarationList = new ArrayList<PatternFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<PictureFieldDeclaration> _pictureFieldDeclarationList = new ArrayList<PictureFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<SectionFieldDeclaration> _sectionFieldDeclarationList = new ArrayList<SectionFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<SelectFieldDeclaration> _selectFieldDeclarationList = new ArrayList<SelectFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<SerialFieldDeclaration> _serialFieldDeclarationList = new ArrayList<SerialFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<TextFieldDeclaration> _textFieldDeclarationList = new ArrayList<TextFieldDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<FormViewDeclaration> _formViewDeclarationList = new ArrayList<FormViewDeclaration>();

public boolean isExtensible() { return (_isExtensible != null); }
public IsExtensible getIsExtensible() { return _isExtensible; }
public ArrayList<BooleanFieldDeclaration> getBooleanFieldDeclarationList() { return _booleanFieldDeclarationList; }
public ArrayList<CheckFieldDeclaration> getCheckFieldDeclarationList() { return _checkFieldDeclarationList; }
public ArrayList<DateFieldDeclaration> getDateFieldDeclarationList() { return _dateFieldDeclarationList; }
public ArrayList<FileFieldDeclaration> getFileFieldDeclarationList() { return _fileFieldDeclarationList; }
public ArrayList<LinkFieldDeclaration> getLinkFieldDeclarationList() { return _linkFieldDeclarationList; }
public ArrayList<LocationFieldDeclaration> getLocationFieldDeclarationList() { return _locationFieldDeclarationList; }
public ArrayList<MemoFieldDeclaration> getMemoFieldDeclarationList() { return _memoFieldDeclarationList; }
public ArrayList<NodeFieldDeclaration> getNodeFieldDeclarationList() { return _nodeFieldDeclarationList; }
public ArrayList<NumberFieldDeclaration> getNumberFieldDeclarationList() { return _numberFieldDeclarationList; }
public ArrayList<PatternFieldDeclaration> getPatternFieldDeclarationList() { return _patternFieldDeclarationList; }
public ArrayList<PictureFieldDeclaration> getPictureFieldDeclarationList() { return _pictureFieldDeclarationList; }
public ArrayList<SectionFieldDeclaration> getSectionFieldDeclarationList() { return _sectionFieldDeclarationList; }
public ArrayList<SelectFieldDeclaration> getSelectFieldDeclarationList() { return _selectFieldDeclarationList; }
public ArrayList<SerialFieldDeclaration> getSerialFieldDeclarationList() { return _serialFieldDeclarationList; }
public ArrayList<TextFieldDeclaration> getTextFieldDeclarationList() { return _textFieldDeclarationList; }
public ArrayList<FormViewDeclaration> getFormViewDeclarationList() { return _formViewDeclarationList; }
      
    protected ArrayList<FieldDeclaration> _fieldDeclarationList;
  protected void createFieldDeclarationList() {
    _fieldDeclarationList = new ArrayList<FieldDeclaration>();
                  _fieldDeclarationList.addAll(_booleanFieldDeclarationList);
                _fieldDeclarationList.addAll(_checkFieldDeclarationList);
                      _fieldDeclarationList.addAll(_dateFieldDeclarationList);
                _fieldDeclarationList.addAll(_fileFieldDeclarationList);
                _fieldDeclarationList.addAll(_linkFieldDeclarationList);
                _fieldDeclarationList.addAll(_locationFieldDeclarationList);
                _fieldDeclarationList.addAll(_memoFieldDeclarationList);
                _fieldDeclarationList.addAll(_nodeFieldDeclarationList);
                _fieldDeclarationList.addAll(_numberFieldDeclarationList);
                _fieldDeclarationList.addAll(_patternFieldDeclarationList);
                _fieldDeclarationList.addAll(_pictureFieldDeclarationList);
                _fieldDeclarationList.addAll(_sectionFieldDeclarationList);
                _fieldDeclarationList.addAll(_selectFieldDeclarationList);
                _fieldDeclarationList.addAll(_serialFieldDeclarationList);
                _fieldDeclarationList.addAll(_textFieldDeclarationList);
        }
  public ArrayList<FieldDeclaration> getFieldDeclarationList() {
    if (_fieldDeclarationList == null) createFieldDeclarationList();
    return _fieldDeclarationList;
  }
      
    protected HashMap<String,FieldDeclaration> _fieldDeclarationMap;
  protected void createFieldDeclarationMap() {
    _fieldDeclarationMap = new HashMap<String,FieldDeclaration>();
    if (_fieldDeclarationList == null) createFieldDeclarationList();
    for (FieldDeclaration item : _fieldDeclarationList){
      _fieldDeclarationMap.put(item.getCode(), item);
      _fieldDeclarationMap.put(item.getName(), item);
    }
  }
  public FieldDeclaration getFieldDeclaration(String key) {
    if (_fieldDeclarationMap == null) createFieldDeclarationMap();
    return _fieldDeclarationMap.get(key);
  }

}







































