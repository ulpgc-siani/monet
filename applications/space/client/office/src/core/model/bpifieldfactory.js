BPIFieldsFactory = new Object();
BPIFieldsFactory.aItems = new Array();

//---------------------------------------------------------------------
BPIFieldsFactory.register = function (Type, sClassName) {
  BPIFieldsFactory.aItems[Type] = sClassName;
};

//---------------------------------------------------------------------
BPIFieldsFactory.get = function (Type, DOMField, CodeDefinition) {
  var BPIField;
  if (BPIFieldsFactory.aItems[Type] == null) return null;
  eval("BPIField = new " + BPIFieldsFactory.aItems[Type] + "(DOMField)");
  BPIField.setCodeDefinition(CodeDefinition);
  return BPIField;
};