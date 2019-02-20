TListView = function(){
  this.listLayer = null;
  this.tpl = null;
};

TListView.prototype.setTemplate = function (sTemplate) {
  this.tpl = new Ext.DomHelper.Template(sTemplate);
};

TListView.prototype.init = function(layer){
  this.listLayer = Ext.get(layer);
  this.listLayer.addClass("ListView");
  this.listLayer.handler = this;
};

TListView.prototype.addElement = function(aElements){
  return this.tpl.append(this.listLayer.dom, aElements);
};

TListView.prototype.removeAllElements = function(){
  this.listLayer.dom.innerHTML = EMPTY;
};

TListView.prototype.setNoResultsMessage = function(sMessage){
  this.listLayer.dom.innerHTML = sMessage;
};

TListView.prototype.destroy = function(e){
};