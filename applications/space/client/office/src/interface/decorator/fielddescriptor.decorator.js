CGDecoratorFieldDescriptor = function () {
};

CGDecoratorFieldDescriptor.prototype = new CGDecorator;

CGDecoratorFieldDescriptor.prototype.execute = function (DOMField) {

  DOMField.getDescriptorName = function () {
    var extField = Ext.get(this);
    return (extDescriptor = extField.select(CSS_FIELD_DEF_NAME).first()) ? extDescriptor.dom.innerHTML : null;
  };

  DOMField.getResultType = function () {
    var extField = Ext.get(this);
    return (extResultType = extField.select(CSS_FIELD_DEF_RESULT_TYPE).first()) ? extResultType.dom.innerHTML : null;
  };

};