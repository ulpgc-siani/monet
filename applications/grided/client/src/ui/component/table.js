// columns: ['name', 'ip']
// options : {action:{name:'', condition:''}, checkbox:true, clickable:true, showHeader:true, empty_message: ''}
// row : [row:{id:1, checked:true, names...}]


var Table = EventsSource.extend({
  init : function(element, columns, options) {
	this._super();
	this.html = AppTemplate.Table;
	this.element = element;
	this.columns = columns;
	this.options = options;
	this.rows = [];	
	this.id = _.uniqueId();
	
	this.mouseOverFlag = false;
	this.browsingFlag = true;
	
	this.enterFlag = false;
  }
});

Table.CLICK_ROW_ACTION = 'click_action';
Table.CLICK_ROW = 'click';
Table.CHECK_ROW = 'check';
Table.MOUSE_ENTER  = 'mouseenter';
Table.MOUSE_LEAVE  = 'mouseleave';
Table.ROW_MOUSE_OVER  = 'rowmouseover';
Table.ROW_MOUSE_OUT  = 'rowmouseout';

Table.MOUSE_EVENTS_TIME = 300;

//-----------------------------------------------------------
Table.prototype.setData = function(rows) {  
  this.rows = rows;
  this._bind();
};

//-----------------------------------------------------------
Table.prototype.setAction = function(action) {
  this.action = action;	
};

//-----------------------------------------------------------
Table.prototype.getSelectedRows = function() {
  if (! this.options.checkbox) return 0;
  
  var rows = [];
  if (! this.extParent) return rows;
  var tbody = this.extTable.dom.firstChild;
  if (!tbody) return rows;
  
  for (var i=0; i < tbody.childNodes.length; i++) {
	var row = tbody.childNodes[i];  
    var input = Ext.get(row).query('input[type="checkbox"]').first();	 
	 if (input.checked) {
		 row = this._getRow(row);
	     rows.push(row);	     
	 }
  }  
  return rows;  
};

//-----------------------------------------------------------
Table.prototype.each = function(elementSelector, callback, scope) {
  var elements = this.extParent.query(elementSelector);
  for (var i=0; i < elements.length; i++) {
	var element = elements[i];  
	callback.call(scope, element, i);
  }
};

//-----------------------------------------------------------
Table.prototype.selectRow = function(rowId) {
  if (! this.options.checkbox) return;
  
  var tr = this.extTable.query('tr[id=' + rowId + ']').first();
  var input = tr.firstChild.firstChild;
  input.checked = true;
};

//-----------------------------------------------------------
Table.prototype.unSelectRow = function(rowId) {
  if (! this.options.checkbox) return;
  var tr = this.extTable.query('tr[id=' + rowId + ']').first();
  var input = tr.firstChild.firstChild;
  input.checked = false;
};

//-----------------------------------------------------------
Table.prototype.filterBy = function(columnName, value) {
  for (var i=0; i < this.rows.length; i++) {
	var row = this.rows[i];  
	var el = null;
    if (row[columnName] == value) {
      el = this.extTable.query('tr[id=' + row.id + ']').first();
      if (el) $(el).show();           
    } else {
      el = this.extTable.query('tr[id=' + row.id + ']').first();      
      if (el) $(el).hide();
    }	  
  }    	
};

//-----------------------------------------------------------
Table.prototype.showAll = function() {
  for (var i=0; i < this.rows.length; i++) {
	var row = this.rows[i];  	
    el = this.extTable.query('tr[id=' + row.id + ']').first();
    if (el) $(el).show();           
  }    	  
};

//-----------------------------------------------------------
Table.prototype.openRow = function(rowId) {
  for (var i=0; i < this.rows.length; i++) {
	var row = this.rows[i];  	
    el = this.extTable.query('tr[id=' + row.id + ']').first();
    if (el) $(el).removeClassName('selected');           
  }    	    	
  
  var el = this.extTable.query('tr[id=' + rowId + ']').first();
  if (el) $(el).addClassName('selected');
};

//-----------------------------------------------------------
Table.prototype._bind = function() {
  this.extParent = Ext.get(this.element);
  var action      = this.options.action || null;
  var conditional = this.options.conditional || null;
  var checkbox    = this.options.checkbox || null;
  var showHeader  = this.options.showHeader || null;
  var clickable   = this.options.clickable || null;
  var empty_message = this.options.empty_message || "";
  
  var html	= _.template(this.html, {'rows': this.rows, 'columns': this.columns, 'id': this.id, 'checkbox': checkbox, 'clickable': clickable, 'action': action, 'conditional': conditional,'showHeader': showHeader,  empty_message: empty_message});
  this.extParent.dom.innerHTML = html;
  
  this.extTable = Ext.get(this.extParent.query('table').first());  
  this.extTable.on('click', this._clickHandler, this);
  
  this.extTable.on('mouseover', this._rowMouseOverHandler, this);
  this.extTable.on('mouseout', this._rowMouseOutHandler, this);
  
  if ((this.listeners[Table.MOUSE_ENTER]) || (this.listeners[Table.MOUSE_LEAVE])) {
    this.extTable.on('mouseover', this._mouseEnterHandler, this);  
    this.extParent.on('mouseout', this._mouseLeaveHandler, this);
  }
};
  
//-----------------------------------------------------------
Table.prototype._rowMouseOverHandler = function(event, target, options) {	
  event.preventDefault();
  event.stopPropagation();
  if (event.target.nodeName.toLowerCase() != 'td' && 
	  event.target.nodeName.toLowerCase() != 'span' &&
	  event.target.nodeName.toLowerCase() != 'a') {
	  return;
  }   
	
  var scope = this;
  this.browsingFlag = true;
  if (! this.listeners[Table.ROW_MOUSE_OVER]) return;
	
  setTimeout(function(scope, arguments) { 
      if (scope.browsingFlag) scope._fireRowMouseOverEvent(arguments[0], arguments[1], arguments[2]); 
  }, Table.MOUSE_EVENTS_TIME, scope, arguments);   
};

//-----------------------------------------------------------
Table.prototype._rowMouseOutHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  
  if (event.target.nodeName.toLowerCase() != 'td' && 
    event.target.nodeName.toLowerCase() != 'span' &&
    event.target.nodeName.toLowerCase() != 'a') {
    return;
  }  
  
  var scope = this;
  this.browsingFlag = false;
  if (! this.listeners[Table.ROW_MOUSE_OUT]) return;	
		
  setTimeout(function(scope, arguments) {
    if (! scope.browsingFlag) scope._fireRowMouseOutEvent(arguments[0], arguments[1], arguments[2]); 	
  }, Table.MOUSE_EVENTS_TIME, scope, arguments);  					
};


//-----------------------------------------------------------
Table.prototype._isChildOf = function(parent, child) {
  if (parent === child)  return false;
  
   while (child && child !== parent) { 
     child = child.parentNode; 
   }
   return child === parent;
};

//-----------------------------------------------------------
Table.prototype._mouseEnterHandler = function(event, target, options) {	  
  if (! this.listeners[Table.MOUSE_ENTER]) return;
  this.browsingFlag = true;
  
  if (this.extTable.dom === target || this._isChildOf(this.extTable, target)) {  
	this.enterFlag = true;
	this.browsingFlag = true;  
	this._fireMouseEnterEvent(event, target, options);
  }
};

//-----------------------------------------------------------
Table.prototype._mouseLeaveHandler = function(event, target, options) {
		  
  if (! this.listeners[Table.MOUSE_LEAVE]) return;
  
  if (this.enterFlag && this.extTable.dom != target && ! this._isChildOf(this.extTable.dom, target)) { 
    this.enterFlag = false;

	var scope = this;  
	setTimeout(function(scope, arguments) {
		if (! scope.enterFlag) scope._fireMouseLeaveEvent(arguments[0], arguments[1], arguments[2]);       	
	}, Table.MOUSE_EVENTS_TIME, scope, arguments);    
  }
};

//-----------------------------------------------------------
Table.prototype._fireRowMouseOverEvent = function(event, target, options) {	  
  var trElement = $(event.target).up('tr');
  if (!trElement) return;
  
  var row = this._getRow(trElement);
  var e = {name: Table.ROW_MOUSE_OVER, data: {row: row}};      	 
  this.fire(e);      	
};

//-----------------------------------------------------------
Table.prototype._fireRowMouseOutEvent = function(event, target, options) {    
  var e = {name: Table.ROW_MOUSE_OUT, data: null};      	 
  this.fire(e);
};

//-----------------------------------------------------------
Table.prototype._fireMouseEnterEvent = function(event, target, options) {
  var e = {name: Table.MOUSE_ENTER, data: null};
  this.fire(e);
};

//-----------------------------------------------------------
Table.prototype._fireMouseLeaveEvent = function(event, target, options) {
  var e = {name: Table.MOUSE_LEAVE, data: null};
  this.fire(e);	
};

//-----------------------------------------------------------
Table.prototype._clickHandler = function(event, target, options) {
  var parentElement = target.parentElement || target.parentNode;
  
  switch (target.nodeName.toLowerCase()) {
    case 'a':      	
      event.stopPropagation();	
      event.preventDefault();
      
      var trElement = $(target).up('tr');
      var row = this._getRow(trElement);
      var e = null;
      
      if (parentElement.getAttribute("class") == 'action')
        e = {name: Table.CLICK_ROW_ACTION, data: {row: row, action: target.innerHTML}};
      else
    	e = {name: Table.CLICK_ROW, data: {row: row}};  
      this.fire(e);
    break;  
    
    case 'span':
      event.stopPropagation();	
      event.preventDefault();
      
      var trElement = parentElement.parentElement || parentElement.parentNode;
      var row = this._getRow(trElement);
      var e = {name: Table.CLICK_ROW, data: {row: row}};
      this.fire(e);
    break;
        
    case 'input':
      event.stopPropagation();
            
      var type = target.getAttribute('type');
      if (type != 'checkbox') return;
      
      var trElement = parentElement.parentElement || parentElement.parentNode;
      var row = this._getRow(trElement);      
      //row.checked = target.checked;
      var e = {name: Table.CHECK_ROW, data: {row: row}};
      this.fire(e);      
    break;        
  }     	
};

//-----------------------------------------------------------
Table.prototype._getRow = function(trElement) {
 var row = {};
 var extElement = Ext.get(trElement);
 
 row.id = trElement.getAttribute('id');
 
 if (this.options.checkbox) {
   var input = extElement.dom.firstChild.firstChild;
   row.checked = (input.checked)? true : false;
 }
 
 for (var i=0; i < this.columns.length; i++) {
   var columnName = this.columns[i];	  
   var element = extElement.query('*[name="' + columnName + '"]').first();   
   row[columnName] = element.innerText;
 }
 return row;
};


