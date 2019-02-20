CGDecoratorFieldDate = function () {
};

CGDecoratorFieldDate.prototype = new CGDecorator;

CGDecoratorFieldDate.prototype.execute = function (DOMField) {

  DOMField.getFormat = function () {
    var extField = Ext.get(this);
    var extFormat = extField.select(CSS_FIELD_DEF_FORMAT).first();
    if (extFormat) return extFormat.dom.innerHTML;
    return null;
  };

  DOMField.getEdition = function () {
    var extField = Ext.get(this);
    var extEdition = extField.select(CSS_FIELD_DEF_EDITION).first();
    if (extEdition) return extEdition.dom.innerHTML;
    return null;
  };

  DOMField.isSequential = function () {
    var edition = this.getEdition();
    return edition == "sequential";
  };

  DOMField.isRandom = function () {
    var edition = this.getEdition();
    return edition == "random";
  };

};