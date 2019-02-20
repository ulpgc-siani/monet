CGDecoratorFieldFile = function () {
};

CGDecoratorFieldFile.prototype = new CGDecorator;

CGDecoratorFieldFile.prototype.execute = function (DOMField) {
  DOMField.getSourceStore = function () {
    return this.getStore(CSS_FIELD_DEF_SOURCE_STORE);
  };
  DOMField.getLimit = function () {
      var extField = Ext.get(this);
      var extLimit = extField.down(CSS_FIELD_DEF_LIMIT);
      if (!extLimit) return null;
      return extLimit.dom.innerHTML;
  };
};