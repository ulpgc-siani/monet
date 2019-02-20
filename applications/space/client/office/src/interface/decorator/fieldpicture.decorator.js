CGDecoratorFieldPicture = function () {
};

CGDecoratorFieldPicture.prototype = new CGDecorator;

CGDecoratorFieldPicture.prototype.execute = function (DOMField) {

  DOMField.getSourceStore = function () {
    return this.getStore(CSS_FIELD_DEF_SOURCE_STORE);
  };

  DOMField.getSize = function () {
    var extField = Ext.get(this);
    var extSize = extField.down(CSS_FIELD_DEF_SIZE);
    if (!extSize) return null;
    var aSize = extSize.dom.innerHTML.split(COMMA);
    return {Width: aSize[0], Height: aSize[1]};
  };

  DOMField.getLimit = function () {
      var extField = Ext.get(this);
      var extLimit = extField.down(CSS_FIELD_DEF_LIMIT);
      if (!extLimit) return null;
      return extLimit.dom.innerHTML;
  };

  DOMField.getDefaultPicture = function () {
    var extField = Ext.get(this);
    var extDefault = extField.down(CSS_FIELD_DEF_DEFAULT);
    var Attribute = new CGAttribute();
    if (!extDefault) return null;
    Attribute.unserialize(extDefault.dom.innerHTML);
    return Attribute.getIndicatorValue(CGIndicator.VALUE);
  };

};