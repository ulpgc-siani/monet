/*
 * accepts model: {index:number , data:{}}
 * 
 */
function ListComponent(layername) {
  var html = "<ul></ul>";
  this.layername = layername;
  this.$layer = $(layername);
  this.$layer.innerHTML = html;
  this.extElement = Ext.get(this.$layer.firstChild);

  this.items  = [];
  this.models = [];
  this.bind();
}

//-----------------------------------------------------------------
ListComponent.prototype.setItemViewClass = function(viewclass) {
  this.itemViewClass = viewclass;
};

//-----------------------------------------------------------------
ListComponent.prototype.selectFirstItem = function() {
  if (this.items.length == 0) return;	
  this._doSelect(0);
};

//-----------------------------------------------------------------
ListComponent.prototype.add = function(model) {  	
  var item = new ListItem(this.extElement);
  var view = new this.itemViewClass(model.data);
  view.el().index = model.index;  
  item.insert(view.el());
  
  this.items[model.index] = item;
  this.models[model.index] = model.data;
};

//-----------------------------------------------------------------
ListComponent.prototype.remove = function(model) {
  var item = this.items[model.index];
  if (item) this.extElement.dom.removeChild(item.el());    
};

//-----------------------------------------------------------------
ListComponent.prototype.onSelect = function(handler, scope) {
 this.handler = handler; 
 this.scope = scope;
};

//-----------------------------------------------------------------
ListComponent.prototype.bind = function() {
  this.extElement.on('click', this._clickHandler, this, {delegate: 'li'});  
};

//-----------------------------------------------------------------
ListComponent.prototype.el = function() {
  this.extElement.dom;
};

//-----------------------------------------------------------------
ListComponent.prototype._clickHandler = function(event, target, options) {
  event.preventDefault();  	
  var element = target.firstChild;    
  this._doSelect(element.index);  
};

//-----------------------------------------------------------------
ListComponent.prototype._doSelect = function(index) {    	
  var view = this.items[index];
  var model = this.models[index];
  view.select();
  this.handler.apply(this.scope, [model]);  	
};


/*-----------------------------------------------------------------
* element
-----------------------------------------------------------------*/

function ListItem(extParent) {
  this.extParent  = extParent;	
  this.extElement = Ext.DomHelper.append(extParent, {tag: 'li'}, true);
  this.Styles = {
    item_selected : 'item_selected',
    item_unselected : 'item_unselected'
  };
}

//-----------------------------------------------------------------
ListItem.prototype.insert = function(child) {
  this.extElement.appendChild(child);
};

//-----------------------------------------------------------------
ListItem.prototype.select = function() {
  var extElements = this.extParent.select('li');
  Ext.each(extElements, function(elt) {
	 elt.removeClass(this.Styles.item_selected); 	 
  }, this);
  this.extElement.addClass(this.Styles.item_selected);
};

//-----------------------------------------------------------------
ListItem.prototype.el = function() {
  return this.extElement.dom;
};






