CGDecoratorFieldSelect = function () {
};

CGDecoratorFieldSelect.prototype = new CGDecorator;

CGDecoratorFieldSelect.prototype.execute = function (DOMField) {

  DOMField.getSourceStore = function () {
    return this.getStore(CSS_FIELD_DEF_SOURCE_STORE);
  };

  DOMField.getHistoryStore = function () {
    return this.getStore(CSS_FIELD_DEF_HISTORY_STORE);
  };

  DOMField.getIndexStore = function () {
    return this.getStore(CSS_FIELD_DEF_INDEX_STORE);
  };

  DOMField.getCodeOnOthers = function () {
    var extField = Ext.get(this);
    var extCodeOther = extField.select(CSS_FIELD_DEF_CODE_OTHER).first();
    if (extCodeOther != null) return extCodeOther.dom.innerHTML;
    return CGIndicator.CODE_OTHER;
  };

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

  DOMField.getStorePartnerContext = function () {
    if (!this.getSourceStore) return "";
    var store = this.getSourceStore();
    if (store == null) return "";
    return store.PartnerContext;
  };

  DOMField.getStorePartnerContextValue = function () {
    var value = this.getStorePartnerContext();

    if (value.indexOf("_field:") != -1)
      value = DOMField.getFieldValueCode(value.replace("_field:", ""));

    return value;
  };

  DOMField.isStorePartnerContextLinked = function () {
    var value = this.getStorePartnerContext();
    return value != null && (value.indexOf("_field:") != -1);
  };

  DOMField.getStoreFrom = function () {
    if (!this.getSourceStore) return "";
    var store = this.getSourceStore();
    if (store == null) return "";
    return this.getSourceStore().From;
  };

  DOMField.getStoreFromValue = function () {
    var value = this.getStoreFrom();

    if (value.indexOf("_field:") != -1)
      value = DOMField.getFieldValueCode(value.replace("_field:", ""));

    return value;
  };

  DOMField.getStoreFilters = function () {
    return this.getSourceStore().Filters;
  };

  DOMField.getStoreFiltersValues = function () {
	  var result = new Array();
	  var filters = this.getStoreFilters();

	  for (var filter in filters) {
		  if (isFunction(filters[filter])) continue;

		  if (filter.indexOf("_field:") != -1)
			  filter = DOMField.getFieldValueCode(filter.replace("_field:", ""));

		  result.push(filter);
	  }

	  return SerializerData.serializeSet(result);
  };

};