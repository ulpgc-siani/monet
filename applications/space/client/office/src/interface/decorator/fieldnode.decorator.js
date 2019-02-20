CGDecoratorFieldNode = function () {
};

CGDecoratorFieldNode.prototype = new CGDecorator;

CGDecoratorFieldNode.prototype.execute = function (DOMField) {

  DOMField.getNodeTypes = function () {
    var extField = Ext.get(this), extNodeTypes;
    if ((extNodeTypes = extField.down(CSS_FIELD_DEF_NODETYPES)) == null) return "";
    return extNodeTypes.dom.innerHTML.split(",");
  };

  DOMField.allowAdd = function () {
    var extField = Ext.get(this), extAllowAdd;
    if ((extAllowAdd = extField.down(CSS_FIELD_DEF_ALLOW_ADD)) == null) return false;
    return extAllowAdd.dom.innerHTML == "true";
  };

  DOMField.getNodeTemplates = function () {
    var extNodeTemplate;
    var extField = Ext.get(this);
    var Templates = new Object();
    Templates = new Object();

    Templates.View = (extNodeTemplate = extField.select(CSS_FIELD_DEF_TEMPLATE_VIEW).first()) ? extNodeTemplate.dom.innerHTML : null;
    Templates.Edit = (extNodeTemplate = extField.select(CSS_FIELD_DEF_TEMPLATE_EDIT).first()) ? extNodeTemplate.dom.innerHTML : null;

    return Templates;
  };

};