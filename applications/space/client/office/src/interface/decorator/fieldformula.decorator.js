CGDecoratorFieldFormula = function () {
};

CGDecoratorFieldFormula.prototype = new CGDecorator;

CGDecoratorFieldFormula.prototype.execute = function (DOMField) {

  DOMField.getFormula = function () {
    var extField = Ext.get(this);
    if ((extFormula = extField.down(CSS_FIELD_DEF_FORMULA)) == null) return null;
    return extFormula.dom.innerHTML;
  };

};