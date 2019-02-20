var ModelVersionView = EventsSource.extend({
  init : function(element, model) {
    this._super();
    this.element = element;
    this.model   = model;
    this.spaces  = [];
    this.initialized = false;
  }
});
ModelVersionView.collapsed_image = "images/grided/collapsed-arrow.png";
ModelVersionView.expanded_image  = "images/grided/expanded-arrow.png";

ModelVersionView.TOGGLE_VERSION = "toogle_version";
ModelVersionView.CHECK_SPACE        = "check_space";
ModelVersionView.ON_OPEN_SPACE      = "open_space";
ModelVersionView.ON_OPEN_FEDERATION = "open_federation";
ModelVersionView.ON_UPGRADE         = "click_upgrade";


//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.setVersion = function(version) {
  this.version = version;  
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;  
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.getVersion = function() {
  return this.version;
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.refresh = function(version) {
  if (! this.initialized) this._init();
  
  this.$block.setAttribute('code', this.version.id);
  var ul = this.extBlockBody.query('ul').first();
  var p  = this.extBlockBody.query('p').first();
  
  if (! ul) {
    ul = document.createElement('ul');
    if (this.spaces.length > 0) this.extBlockBody.appendChild(ul);
    if (p) this.extBlockBody.dom.removeChild(p);
  }
  
  this.extVersionInput.disabled = false;
    
  while (ul.hasChildNodes()) {
    ul.removeChild(ul.lastChild);
  }

  for (var i=0, l = this.spaces.length; i < l; i++) {
    var space = this.spaces[i];
    var inputHTML = _.template(AppTemplate.ModelVersionSpace, {space : space});
    var li = document.createElement('li');
    li.innerHTML = inputHTML;       
    ul.appendChild(li);
  }
  
  if (! ul.hasChildNodes()) {    
    this.extBlockBody.dom.innerHTML = "";
    this.extVersionInput.dom.checked = false;
    this.extVersionInput.dom.disabled = true;
    
    var p = document.createElement('p');
    p.innerHTML = Lang.ModelVersionView.no_spaces;
    this.extBlockBody.dom.appendChild(p);
  }
         
  var extBlock = Ext.get(this.$block);
  var extUpgrade = this.extBlockBody.select('a[code]').first();

  extBlock.on('click', this._clickFederationOrSpaceHandler, this);
  
  if (extUpgrade) {
    extUpgrade.on('click', this._clickUpgradeHandler, this);
  }
  
  this.initialized = true;
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.expand = function() {  
  this.$li.removeClassName('collapsed');
  this.$li.addClassName('expanded');
  this.extImage.dom.src = ModelVersionView.expanded_image;      
  this.$block.show();    
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.collapse = function() {
  this.$li.removeClassName('expanded');
  this.$li.addClassName('collapsed');
  this.extImage.dom.src = ModelVersionView.collapsed_image;      
  this.$block.hide();  
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.getSelectedSpaces = function() {
  var spacesIds = [];
  var extBlock = Ext.get(this.$block);
  
  extBlock.select('input[type="checkbox"]').each(function(input, index) {
    var spaceId = input.dom.getAttribute('code');
    if (input.dom.checked) spacesIds.push(spaceId);        
  });
  return spacesIds;  
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype.isExpanded = function() {
  return this.$li.className == 'expanded';
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype._init = function() {
  this.extParent = Ext.get(this.element);
    
  var template = translate(AppTemplate.ModelVersionView, Lang.ModelVersionView);
  var html     = _.template(template, {model: this.model, version : this.version});
  
  var div = document.createElement('div');
  div.innerHTML = html;  
  this.extParent.dom.appendChild(div.firstChild);
  
  var extVersionLi     = Ext.get(this.extParent.dom.lastChild);  
  this.extImage        = Ext.get(extVersionLi.query('img').first());
  this.extVersionInput = Ext.get(extVersionLi.query('input[type="checkbox"]').first());  
  this.versionId       = this.extVersionInput.dom.getAttribute('code');

  this.$li        = $(extVersionLi.dom);
  this.$block     = this.$li.down('.block');  
  this.extBlockBody = Ext.get(this.$block.firstChild);
 
  this.extImage.on('click', this._clickImageHandler, this);
  this.extVersionInput.on('click', this._clickCheckbox, this);
  this.extBlockBody.on('click', this._clickVersionHandler, this);

  var spacesTemplate = translate(AppTemplate.ModelVersionSpaces, Lang.ModelVersionView);
  var spacesHTML = _.template(spacesTemplate, {model : this.model, version: this.version, spaces : this.spaces});
  this.extBlockBody.dom.innerHTML = spacesHTML;  

  this.$upgrade   = this.$block.down('.block-action');
  
  if (this.$upgrade) {
    Ext.get(this.$upgrade).on('click', this._clickUpgradeHandler, this);
  }  
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype._clickImageHandler = function(event, target, options) {  
  var evt = {name: ModelVersionView.TOGGLE_VERSION, data : {view: this}};
  this.fire(evt);  
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype._clickCheckbox = function(event, target, options) {
  this.extBlockBody.query('input[type="checkbox"]').each(function(item, index) {
    item.checked = target.checked;
  });
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype._clickVersionHandler = function(event, target, options) {
  var nodeName = target.nodeName.toLowerCase();
  
  switch (nodeName) {
    case 'a'    : this._clickFederationOrSpaceHandler(event, target, options); break;
    case 'input': this._clickSpaceCheckbox(event, target, options); break;
    default: return;
  }
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype._clickFederationOrSpaceHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'a') return;
  
  event.preventDefault();
  event.stopPropagation();
  var type = target.getAttribute('type');
  var id   = target.getAttribute('code');
      
  switch (type) {
    case 'federation': 
      evt = {name : ModelVersionView.ON_OPEN_FEDERATION, data: {id : id}};
      this.fire(evt);
    break;
    case 'space' :
      evt = {name : ModelVersionView.ON_OPEN_SPACE, data: {id : id}};
      this.fire(evt);      
    break;     
  }  
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype._clickSpaceCheckbox = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'input') return;
  
  var allInputsChecked = true;
  this.extBlockBody.query('input[type="checkbox"]').each(function(input, index) {
    if (! input.checked) allInputsChecked = false;
  });
  this.extVersionInput.dom.checked = allInputsChecked;
};

//---------------------------------------------------------------------------------------------
ModelVersionView.prototype._clickUpgradeHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  
  var spaceIds = this.getSelectedSpaces();
  var evt = {name: ModelVersionView.ON_UPGRADE, data : {spaceIds : spaceIds, versionId : this.versionId}};
  this.fire(evt);  
};
