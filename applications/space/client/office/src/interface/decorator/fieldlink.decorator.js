CGDecoratorFieldLink = function () {
};

CGDecoratorFieldLink.prototype = new CGDecorator;

CGDecoratorFieldLink.prototype.execute = function (DOMField) {

  DOMField.getHistoryStore = function () {
    return this.getStore(CSS_FIELD_DEF_HISTORY_STORE);
  };

  DOMField.getDataLink = function () {
    return this.getStore(CSS_FIELD_DEF_DATA_LINK);
  };

  DOMField.getDataLinkLocations = function () {
    return this.getStore(CSS_FIELD_DEF_DATA_LINK_LOCATIONS);
  };

  DOMField.setParameter = function (Code, sValue) {
    if (!this.aParameters) this.aParameters = new Array();
    this.aParameters[Code] = sValue;
  };

  DOMField.getParameters = function () {
    if (!this.aParameters) this.aParameters = new Array();
    return this.aParameters;
  };

  DOMField.getHeader = function () {
    var extField = Ext.get(this);
    var DataHeader;

    DataHeader = new Object();
    DataHeader.Attributes = new Array();

    extHeader = extField.select(CSS_FIELD_DEF_HEADER).first();
    if (extHeader == null) {
      return DataHeader;
    }

    for (var iPos = 0; iPos < extHeader.dom.options.length; iPos++) {
      var DOMOption = $(extHeader.dom.options[iPos]);
      var sValue = DOMOption.value.toLowerCase();
      var sText = DOMOption.text.toLowerCase();
      if (DOMOption.hasClassName(CLASS_FIELD_DATA_HEADER_VALUE_COLUMN)) DataHeader.CodeValueColumn = sValue;
      DataHeader.Attributes.push([sValue, sText]);
    }

    return DataHeader;
  };

  DOMField.getNodeTypes = function () {
    var extField = Ext.get(this), extNodeTypes;
    if ((extNodeTypes = extField.down(CSS_FIELD_DEF_NODETYPES)) == null) return "";
    return extNodeTypes.dom.innerHTML.split(",");
  };

  DOMField.getLinkTemplates = function () {
    var extLinkTemplate;
    var extField = Ext.get(this);
    var Templates = new Object();
    Templates = new Object();

    Templates.View = (extLinkTemplate = extField.select(CSS_FIELD_DEF_TEMPLATE_VIEW).first()) ? extLinkTemplate.dom.innerHTML : null;
    Templates.Edit = (extLinkTemplate = extField.select(CSS_FIELD_DEF_TEMPLATE_EDIT).first()) ? extLinkTemplate.dom.innerHTML : null;

    return Templates;
  };

  DOMField.getStoreFilters = function () {
    return this.getDataLink().Filters;
  };

  DOMField.getStoreFiltersFields = function () {
    var result = [];
    var filters = this.getStoreFilters();

    for (var key in filters) {
      if (isFunction(filters[key])) continue;
      var filter = filters[key];

      if (filter.indexOf("_field:") != -1) {
        var field = filter.replace("_field:", "");
        if (field.indexOf("__") != -1) field = field.substring(0, field.indexOf("__"));
        result.push(DOMField.getField(field));
      }
    }

    return result;
  };

  DOMField.getStoreFiltersValues = function () {
	  var result = new Object();
	  var filters = this.getStoreFilters();

	  for (var key in filters) {
		  if (isFunction(filters[key])) continue;
		  var filter = filters[key];

		  if (filter.indexOf("_field:") != -1) {
		      var field = filter.replace("_field:", "");
		      var operator = "EQUALS";
		      if (field.indexOf("__") != -1) {
		        operator = field.substring(field.indexOf("__")+2);
                field = field.substring(0, field.indexOf("__"));
              }
              filter = DOMField.getFieldValueCode(field);
              filter = filter + "__" + operator;
          }

		  result[key] = filter;
	  }

	  return SerializerData.serialize(result);
  };

  DOMField.getCollection = function () {
    var extField = Ext.get(this), extNodeTypes;
    if ((extCollection = extField.down(CSS_FIELD_DEF_COLLECTION)) == null) return null;
    return extCollection.dom.innerHTML;
  };

};