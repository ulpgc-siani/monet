var SelectServerDialog = Dialog.extend({
  init : function(element, servers) {
    this._super(element);
    this.servers = servers;
    this.html = translate(AppTemplate.SelectServerDialog, Lang.SelectServerDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }
});
SelectServerDialog.CHANGE_SELECTION_EVENT = "change_selection";

//------------------------------------------------------------------
SelectServerDialog.prototype.getData = function() {
  var selectedOption = this.extSelect.dom.options[this.extSelect.dom.selectedIndex];
    
  var data = {
    serverId : selectedOption.getAttribute("value"),    
  };
  return data;
};

//------------------------------------------------------------------
SelectServerDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);  
  this.extSelect = this.extParent.select('select').first();
  this.extSelect.on('change', this._selectHandler, this);
        
  var options = this._createServerOptions();
  if (options.length > 0) this.extSelect.dom.innerHTML = options;    
};

//------------------------------------------------------------------
SelectServerDialog.prototype._selectHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != "select") return;	
  this.fire({name: SelectServerDialog.CHANGE_SELECTION_EVENT, data: this});    
};

//------------------------------------------------------------------
SelectServerDialog.prototype._createServerOptions = function() {
  var options = "";
  var defaultOption = '<option value="">' + Lang.SelectServerDialog.select_server_message + '</option>';
  options += defaultOption;
  
  var tpl = '<option value="$1">$2</option>';	
  for (var i=0; i < this.servers.length; i++) {
	var server = this.servers[i]; 
	var option = tpl.replace(/\$1/g, server.id);
	option = option.replace(/\$2/g, server.name);
	options += option;
  }
  return options;
};
