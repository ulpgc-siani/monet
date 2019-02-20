function CGBPIFieldComposite(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldComposite.prototype = new CGBPIField;

//---------------------------------------------------------------------
// PRIVATE
CGBPIFieldComposite.prototype.getFieldCode = function (sName) {
  var Definition = Extension.getDefinition(this.CodeDefinition);
  return Definition.FieldsNames[sName];
};

//---------------------------------------------------------------------
// PUBLIC
CGBPIFieldComposite.prototype.isExtensible = function () {
  return this.DOMField.isExtensible();
};

//---------------------------------------------------------------------
CGBPIFieldComposite.prototype.isConditional = function () {
  return this.DOMField.isConditional();
};

//---------------------------------------------------------------------
CGBPIFieldComposite.prototype.getFields = function (sName) {
  var BPIFields = new Array(), Code, DOMFields;

  Code = this.getFieldCode(sName);
  if (Code == null) Code = sName;

  DOMFields = this.DOMField.getFieldsByCode(Code);

  for (var i = 0; i < DOMFields.length; i++)
    BPIFields.push(BPIFieldsFactory.get(DOMFields[i].getType(), DOMFields[i], this.CodeDefinition));

  return BPIFields;
};

//---------------------------------------------------------------------
CGBPIFieldComposite.prototype.getField = function (sName) {
  var Code, DOMField, BPIField;

  Code = this.getFieldCode(sName);
  if (Code == null) Code = sName;

  DOMField = this.DOMField.getField(Code);
  if (!DOMField) return false;

  BPIField = BPIFieldsFactory.get(DOMField.getType(), DOMField, this.CodeDefinition);

  return (BPIField) ? BPIField : false;
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_COMPOSITE, "CGBPIFieldComposite");