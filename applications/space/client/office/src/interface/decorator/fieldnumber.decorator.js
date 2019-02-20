CGDecoratorFieldNumber = function () {
};

CGDecoratorFieldNumber.prototype = new CGDecorator;

CGDecoratorFieldNumber.prototype.execute = function (DOMField) {

  DOMField.getFormat = function () {
    var extFormat, extField = Ext.get(this);
    sResult = "0";
    if ((extFormat = extField.down(CSS_FIELD_DEF_FORMAT)) == null) return sResult;
    sResult = extFormat.dom.innerHTML;
    return sResult;
  };

  DOMField.getMetrics = function () {
    var aMetrics = new Array();
    var extMetrics, extField = Ext.get(this);

    if ((extMetrics = extField.down(CSS_FIELD_DEF_METRICS)) == null) return aMetrics;

    for (var i = 0; i < extMetrics.dom.options.length; i++) {
      var DOMOption = extMetrics.dom.options[i];
      var Metric = {Code: DOMOption.value, Equivalence: DOMOption.className, Label: DOMOption.text};
      if (DOMOption.selected) Metric.IsDefault = true;
      aMetrics.push(Metric);
    }

    return aMetrics;
  };

  DOMField.getRange = function () {
    var aResult = new Array(), extRange, extField = Ext.get(this);
    if ((extRange = extField.down(CSS_FIELD_DEF_RANGE)) == null) return aResult;
    var aData = extRange.dom.innerHTML.split(COMMA);
    aResult[0] = eval(aData[0]);
    aResult[1] = eval(aData[1]);
    return aResult;
  };

  DOMField.getIncrements = function () {
    var extRange, extField = Ext.get(this);
    if ((extRange = extField.down(CSS_FIELD_DEF_RANGE)) == null) return 1;
    var aData = extRange.dom.innerHTML.split(COMMA);
    if (aData.length < 2) return 1;
    return (aData[2] != "") ? eval(aData[2]) : 1;
  };

  DOMField.roundDecimals = function (iNumber) {
    var aFormat = this.getFormat();
    var iDecimals;

    if (aFormat.length < 2) return iNumber;

    iDecimals = aFormat[1];

    if (iDecimals != INFINITE) {
      iDecimals = parseInt(iDecimals);
      iNumber = iNumber * (10 ^ iDecimals);
      iNumber = Math.round(iNumber);
      iNumber = iNumber / (10 ^ iDecimals);
    }

    return iNumber;
  };

  DOMField.isValidRange = function (iNumber) {
    var aRange = this.getRange();
    if (aRange.length < 2) return true;

    var min = parseInt(aRange[0]);
    var max = parseInt(aRange[1]);

    if (max < min)
      return iNumber >= min;

    return iNumber >= min && iNumber <= max;
  };

  DOMField.getNumberFromFormattedValue = function (sNumber) {
    var aFormat = this.getFormat();
    var formatter = new DecimalFormat(aFormat);
    return formatter.formatBack(sNumber);
  };

  DOMField.format = function (sNumber) {
    var aFormat = this.getFormat();
    var formatter = new DecimalFormat(aFormat);
    return formatter.format(sNumber);
  };

};