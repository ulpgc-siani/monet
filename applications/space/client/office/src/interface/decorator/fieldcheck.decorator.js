CGDecoratorFieldCheck = function () {
};

CGDecoratorFieldCheck.prototype = new CGDecorator;

CGDecoratorFieldCheck.prototype.execute = function (DOMField) {

  DOMField.getSources = function () {
    var extField = Ext.get(this);
    var extSources = extField.select(CSS_FIELD_DEF_SOURCES).first();
    var sources = new Array();

    for (var iPos = 0; iPos < extSources.dom.options.length; iPos++) {
      var DOMOption = extSources.dom.options[iPos];
      sources.push({ id: DOMOption.value, label: DOMOption.text, partner: DOMOption.className});
    }

    return sources;
  };

  DOMField.getStoreFrom = function () {
    var extField = Ext.get(this);
    var extFrom = extField.select(CSS_FIELD_DEF_FROM).first();
    return (extFrom != null) ? extFrom.dom.innerHTML : "";
  };

  DOMField.getStoreFromValue = function () {
    var value = this.getStoreFrom();

    if (value.indexOf("_field:") != -1)
      value = DOMField.getFieldValueCode(value.replace("_field:", ""));

    return value;
  };

};