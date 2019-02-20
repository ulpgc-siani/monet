function CGBPIField(DOMField) {
  this.DOMField = DOMField;
  this.CodeDefinition = null;
};

//---------------------------------------------------------------------
CGBPIField.getClassName = function (Type) {
  if (Type == FIELD_TYPE_SELECT) return "CGBPIFieldSelect";
  else if (Type == FIELD_TYPE_BOOLEAN) return "CGBPIFieldBoolean";
  else if (Type == FIELD_TYPE_DATE) return "CGBPIFieldDate";
  else if (Type == FIELD_TYPE_TEXT) return "CGBPIFieldText";
  else if (Type == FIELD_TYPE_PICTURE) return "CGBPIFieldPicture";
  else if (Type == FIELD_TYPE_FILE) return "CGBPIFieldFile";
  else if (Type == FIELD_TYPE_NUMBER) return "CGBPIFieldNumber";
  else if (Type == FIELD_TYPE_COMPOSITE) return "CGBPIFieldComposite";
  else if (Type == FIELD_TYPE_LINK) return "CGBPIFieldLink";
  else if (Type == FIELD_TYPE_FORMULA) return "CGBPIFieldFormula";
  else if (Type == FIELD_TYPE_CHECK) return "CGBPIFieldCheck";
  else if (Type == FIELD_TYPE_NODE) return "CGBPIFieldNode";
  else if (Type == FIELD_TYPE_SERIAL) return "CGBPIFieldSerial";
  else if (Type == FIELD_TYPE_LOCATION) return "CGBPIFieldLocation";
  else if (Type == FIELD_TYPE_SUMMATION) return "CGBPIFieldSummation";
  return null;
};

//---------------------------------------------------------------------
CGBPIField.prototype.setCodeDefinition = function (CodeDefinition) {
  this.CodeDefinition = CodeDefinition;
};

//---------------------------------------------------------------------
CGBPIField.prototype.getCode = function () {
  return this.DOMField.getCode();
};

//---------------------------------------------------------------------
CGBPIField.prototype.getLabel = function () {
  var Info = this.DOMField.getInfo();
  return Info.Title;
};

//---------------------------------------------------------------------
CGBPIField.prototype.getDescription = function () {
  var Info = this.DOMField.getInfo();
  return Info.Description;
};

//---------------------------------------------------------------------
CGBPIField.prototype.getHelp = function () {
  var Info = this.DOMField.getInfo();
  return Info.eHelp;
};

//---------------------------------------------------------------------
CGBPIField.prototype.getMessage = function () {
  var Info = this.DOMField.getInfo();
  return Info.iHelp;
};

//---------------------------------------------------------------------
CGBPIField.prototype.isVisible = function () {
  return this.DOMField.isVisible();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isRequired = function () {
  return this.DOMField.isRequired();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isExtended = function () {
  return this.DOMField.isExtended();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isMultiple = function () {
  return this.DOMField.isMultiple();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isSelect = function () {
  return this.DOMField.isSelect();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isBoolean = function () {
  return this.DOMField.isBoolean();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isDate = function () {
  return this.DOMField.isDate();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isText = function () {
  return this.DOMField.isText();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isPicture = function () {
  return this.DOMField.isPicture();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isFile = function () {
  return this.DOMField.isFile();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isNumber = function () {
  return this.DOMField.isNumber();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isComposite = function () {
  return this.DOMField.isComposite();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isLink = function () {
  return this.DOMField.isLink();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isFormula = function () {
  return this.DOMField.isFormula();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isCheck = function () {
  return this.DOMField.isCheck();
};

//---------------------------------------------------------------------
CGBPIField.prototype.isEqualsTo = function (Value) {
  return this.DOMField.isEqualsTo(Value);
};

//---------------------------------------------------------------------
CGBPIField.prototype.isGT = function (Value) {
  return this.getValue() > Value;
};

//---------------------------------------------------------------------
CGBPIField.prototype.isGE = function (Value) {
  return this.getValue() >= Value;
};

//---------------------------------------------------------------------
CGBPIField.prototype.isLT = function (Value) {
  return this.getValue() < Value;
};

//---------------------------------------------------------------------
CGBPIField.prototype.isLE = function (Value) {
  return this.getValue() <= Value;
};

//---------------------------------------------------------------------
CGBPIField.prototype.getIndicator = function (Code) {
  var Attribute = new CGAttribute();
  Attribute.unserialize(this.DOMField.getData());
  return Attribute.getIndicator(Code);
};

//---------------------------------------------------------------------
CGBPIField.prototype.getIndicatorValue = function (Code) {
  var Indicator = this.getIndicator(Code);
  return (Indicator) ? Indicator.getValue() : "";
};

//---------------------------------------------------------------------
CGBPIField.prototype.getValue = function () {
  var Indicator = this.getIndicator(CGIndicator.VALUE);
  return (Indicator) ? Indicator.getValue() : "";
};

//---------------------------------------------------------------------
CGBPIField.prototype.setValue = function (sValue) {
  var Attribute = new CGAttribute();

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.VALUE, 1, sValue);

  this.DOMField.setData(Attribute.serialize());
};

//---------------------------------------------------------------------
CGBPIField.prototype.getBrother = function (Code) {
  var brother = this.DOMField.getBrother(Code);
  if (!brother) return false;
  var BPIField = BPIFieldsFactory.get(brother.getType(), brother, this.CodeDefinition);
  return (BPIField) ? BPIField : false;
};

//---------------------------------------------------------------------
CGBPIField.prototype.show = function () {
  this.DOMField.show();
};

//---------------------------------------------------------------------
CGBPIField.prototype.hide = function () {
  this.DOMField.hide();
};

//---------------------------------------------------------------------
CGBPIField.prototype.lock = function () {
  this.DOMField.lock();
};

//---------------------------------------------------------------------
CGBPIField.prototype.unLock = function () {
  this.DOMField.unLock();
};

//---------------------------------------------------------------------
CGBPIField.prototype.expand = function () {
  this.DOMField.expand();
};

//---------------------------------------------------------------------
CGBPIField.prototype.collapse = function () {
  this.DOMField.collapse();
};

//---------------------------------------------------------------------
CGBPIField.prototype.serialize = function () {
  return this.DOMField.getData();
};

//---------------------------------------------------------------------
CGBPIField.prototype.unserialize = function (sData) {
  this.DOMField.setData(sData);
};