LIST_VIEW_MAX_PAGE_ITEMS = 10;

TListView = function () {
  this.extTemplate = null;
  this.extEmpty = null;
  this.extItems = null;
  this.iCounter = 0;
  this.sUrl = null;
  this.onLoadData = null;
  this.extStore = null;
  this.sCondition = "";
  this.iMaxPageItems = LIST_VIEW_MAX_PAGE_ITEMS;
};

TListView.prototype.setTemplate = function (sTemplate) {
  this.extTemplate = new Ext.DomHelper.Template(sTemplate);
};

TListView.prototype.setUrl = function (sUrl) {
  this.sUrl = sUrl;
};

TListView.prototype.setMaxPageItems = function (iCount) {
  this.iMaxPageItems = iCount;
};

TListView.prototype.init = function (layer, sEmptyMessage) {
  var extLayer = Ext.get(layer);

  extLayer.addClass("ListView");
  extLayer.dom.innerHTML = EMPTY;

  if (this.sUrl != null) {
    this.extStore = new Ext.data.Store({
      proxy: new Ext.data.HttpProxy({url: this.sUrl}),
      reader: new Ext.data.JsonReader({root: 'items', totalProperty: 'totalCount', id: 'code'}, [
        {name: 'id', mapping: 'id'},
        {name: 'value', mapping: 'value'}
      ]),
      remoteSort: false
    });
    this.extStore.on("load", this.atLoadData, this);
    this.extStore.on("beforeload", this.atBeforeLoadData, this);

    if (this.iMaxPageItems > -1) {
      var extPaging = new Ext.PagingToolbar(extLayer.dom, this.extStore, {
        pageSize: this.iMaxPageItems,
        displayInfo: true,
        displayMsg: '{0} - {1} / {2}',
        emptyMsg: ''
      });
    }
  }

  new Insertion.Bottom(extLayer.dom, "<div class='empty'>" + sEmptyMessage + "</div>");
  new Insertion.Bottom(extLayer.dom, "<ul class='items'></ul>");

  this.extEmpty = extLayer.select(".empty").first();
  this.extItems = extLayer.select(".items").first();
};

TListView.prototype.addElement = function (aElements) {
  var DOMElement = this.extTemplate.append(this.extItems.dom, aElements);
  this.extEmpty.dom.style.display = "none";
  this.iCounter++;
  this.refresh();
  return DOMElement;
};

TListView.prototype.refresh = function () {
  if (this.iCounter > 0) {
    this.extEmpty.dom.style.display = "none";
  }
  else {
    this.extEmpty.dom.style.display = "block";
  }
};

TListView.prototype.removeAllElements = function () {
  this.extItems.dom.innerHTML = EMPTY;
  this.iCounter = 0;
  this.refresh();
};

TListView.prototype.removeElement = function (DOMElement) {
  var extElement = Ext.get(DOMElement);
  if (extElement) extElement.remove();
  this.iCounter--;
  this.refresh();
};

TListView.prototype.destroy = function (e) {
};

TListView.prototype.filter = function (sCondition) {
  this.sCondition = sCondition;
  if (this.extStore) this.extStore.load({params: {query: sCondition, start: 0}});
};

TListView.prototype.atBeforeLoadData = function (Store, Options) {
  if (Options) {
    if (!Options.params) Options.params = new Object();
    if (!Options.params.query) Options.params.query = this.sCondition;
    Options.params.limit = this.iMaxPageItems;
    if (this.onBeforeLoadData) this.onBeforeLoadData(Options);
  }
};

TListView.prototype.atLoadData = function (Store, aRecords) {
  if (this.onLoadData) this.onLoadData(aRecords);
};