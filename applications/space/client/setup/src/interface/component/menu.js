
function Menu(layername) {
  this.html = AppTemplate.Menu;
  this.layername = layername;
  this.$layer = $(layername);
  this.$layer.innerHTML = this.html;
  
  this.handlers = {};
  this.items = {};
  this.bind();
}

//--------------------------------------------------------------------------------
Menu.prototype.setTitle = function(title) {
  var html = '<span>$1</span><span class="arrow"></span>';
  html = html.replace(/\$1/, title);
  this.extTitle.dom.innerHTML = html;	
};

//--------------------------------------------------------------------------------
Menu.prototype.show = function() {
  this.$layer.show();	
};

//--------------------------------------------------------------------------------
Menu.prototype.hide = function() {
  this.$layer.hide();	
};

//--------------------------------------------------------------------------------
Menu.prototype.addItem = function(id, itemName, handler, scope) {
  if (handler) this.handlers[id] = {fn: handler, scope: scope};
  this._addItem(id, itemName, handler);
};

//--------------------------------------------------------------------------------
Menu.prototype.bind = function() {
  this.extParent = Ext.get(this.layername);
  this.extTitle  = this.extParent.select('.menu-title').first();
  this.extBody   = this.extParent.select('.menu-body').first();
  this.extUl     = Ext.get(this.extBody.query('ul').first());
  
  this.extBody.hide();
  var body = Ext.get(document.getElementsByTagName('body')[0]);
  body.on('click', this._closeMenuHandler, this);
  this.extTitle.on('click', this._clickMenuHandler, this);
  this.extUl.on('click', this._clickMenuItemHandler, this);
};

//--------------------------------------------------------------------------------
Menu.prototype._addItem = function(id, itemName, handler) {
  var elt = document.createElement('li');
  if (handler) {
    var link = document.createElement('a');
    link.setAttribute('id', id);
    link.setAttribute('href', "");
    link.innerHTML = itemName;
    elt.appendChild(link);
  } else {	  
	elt.innerHTML = itemName;
  }
  this.extUl.appendChild(elt);
};

//--------------------------------------------------------------------------------
Menu.prototype._closeMenuHandler = function(event, target, options) {
  if (! this.extParent.contains(target)) this.extBody.hide();    	 
};

//--------------------------------------------------------------------------------
Menu.prototype._clickMenuHandler = function(event, target, options) {
  this.extBody.setVisible(!this.extBody.isVisible());      	
};
//--------------------------------------------------------------------------------
Menu.prototype._clickMenuItemHandler = function(event, target, options) {
  var nodeName = target.nodeName.toLowerCase();  
  if (nodeName !== 'a') return;
  
  event.stopEvent();
  var id = target.attributes['id'].value;  
  var handler = this.handlers[id];
  if (!handler) return;
  handler.fn.apply(handler.scope, [id]);
};