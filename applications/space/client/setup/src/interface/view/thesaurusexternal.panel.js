
function ThesaurusExternalPanel(layername) {
  this.layername = layername;
  this.$layer = $(layername);
  this.html = translate(AppTemplate.ThesaurusExternalPanel, Lang.ThesaurusExternalPanel);
  this.thesaurus = null;
  this.providers = [];
    
  this.bind();
}

//-----------------------------------------------------------------------------------
ThesaurusExternalPanel.prototype.show = function() {
  this.$layer.show();	
};

//-----------------------------------------------------------------------------------
ThesaurusExternalPanel.prototype.hide = function() {
  this.$layer.hide();	
};

//-----------------------------------------------------------------------------------
ThesaurusExternalPanel.prototype.refresh = function(thesaurus) {
  this._loadProviders(thesaurus);	
};

//-----------------------------------------------------------------------------------
ThesaurusExternalPanel.prototype.bind = function() {
  this.extProvidersCb = Ext.get(this.layername).query('select').first();
  this.extWarning    = Ext.get(this.layername).select('.message').first();
};

//-----------------------------------------------------------------------------------
ThesaurusExternalPanel.prototype._loadProviders = function(thesaurus) {
  var successHandler = function(response, request) {
    if (response.responseText.indexOf("ERR_") != -1) {failureHandler.apply(this, [response, request]); return; }
    var data = Ext.util.JSON.decode(response.responseText);
    var rows = data.rows;
 
    for (var i=0; i < rows.length; i++) {
      var provider = new Provider(rows[i]);	
	  this.providers.push(provider);
    }
    this.bindData(this.providers);
  };
		  
  var failureHandler = function(response, request) {    	  
	if (thesaurus.isExternal()) {
	  Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.LoadThesaurusProviders.Failure_External);		
	} else {
	  Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.LoadThesaurusProviders.Failure);
	}
  };
  
  this.thesaurus = thesaurus;
		  
  Ext.Ajax.request({
    url: Context.Config.Api + "?op=loadthesaurusproviders",
	method: 'get',	
	params: {code: thesaurus.code},
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
 });
};

//-----------------------------------------------------------------------------------
ThesaurusExternalPanel.prototype.bindData = function(providers) {    
  for (var i=0; i < providers.length; i++) {
	var provider = list[i];
	var html = '<option value="$1" $2>$3</option>';
	html = html.replace(/\$1/, provider.id);
	html = html.replace(/\$2/, (this.thesaurus.providerName)? 'selected' : '');
	html = html.replace(/\$3/, provider.label);
		
    this.extProvidersCb.dom.innerHTML += html;
  }
};
