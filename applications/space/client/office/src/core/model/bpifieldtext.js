function CGBPIFieldText(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldText.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldText.prototype.getMinLength = function () {
  return this.DOMField.getMinLength();
};

//---------------------------------------------------------------------
CGBPIFieldText.prototype.getMaxLength = function () {
  return this.DOMField.getMaxLength();
};

//---------------------------------------------------------------------
CGBPIFieldText.prototype.getPatterns = function () {
  return this.DOMField.getPatterns();
};

//---------------------------------------------------------------------
CGBPIFieldText.prototype.getIndicators = function () {
  var Attribute = new CGAttribute();
  var aResult = new Array();

  Attribute.unserialize(this.DOMField.getData());
  aIndicators = Attribute.getIndicators();

  for (var iPos = 0; iPos < aIndicators.length; iPos++) {
    var Indicator = aIndicators[iPos];
    if (Indicator.code != CGIndicator.CODE) aResult.push({Code: Indicator.code, Value: Indicator.getValue()});
  }

  return aResult;
};

//---------------------------------------------------------------------
CGBPIFieldText.prototype.splitIndicators = function (sValue) {
  var iPos = 0;
  var bMatchPattern = false;
  var aResult = new Array();
  var aPatterns = this.getPatterns();

  while ((!bMatchPattern) && (iPos < aPatterns.length)) {
    var Pattern = aPatterns[iPos];
    if (sValue.match(Pattern.Expression)) {
      var aMatching = Pattern.Expression.exec(sValue);
      for (var jPos = 0; jPos < Pattern.aCodes.length; jPos++) {
        var Indicator = new Object();
        Indicator.Code = Pattern.aCodes[jPos];
        Indicator.Value = aMatching[jPos + 1];
        aResult.push(Indicator);
      }
      bMatchPattern = true;
    }
    iPos++;
  }

  return aResult;
};

//---------------------------------------------------------------------
CGBPIFieldText.prototype.setValue = function (sValue) {
  var Attribute = new CGAttribute();

  sValue = this.DOMField.format(sValue);

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.VALUE, 1, sValue);

  var aIndicators = this.splitIndicators(sValue);
  for (var iPos = 0; iPos < aIndicators.length; iPos++) {
    var Indicator = aIndicators[iPos];
    Attribute.addIndicatorByValue(Indicator.Code, iPos + 2, Indicator.Value);
  }

  this.DOMField.setData(Attribute.serialize());
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_TEXT, "CGBPIFieldText");