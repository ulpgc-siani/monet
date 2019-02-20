function HierarchySelector(config) {
  this.config = config;
  this.translations = new Object();
  this.translations["es"] = new Object();
  this.translations["es"].All = "todos";
  this.translations["en"] = new Object();
  this.translations["en"].All = "all";
  this.templates = new Object();
  this.templates["es"] = "<div class='layer'><div class='container'></div><a class='add' style='cursor:pointer;display:block;margin-left:2px;margin-top:3px;width:110px;color:blue;'>" + (this.config.selectLabel?this.config.selectLabel:"seleccionar") + "</a></div>";
  this.templates["en"] = "<div class='layer'><div class='container'></div><a class='add' style='cursor:pointer;display:block;margin-left:2px;margin-top:3px;width:110px;color:blue;'>" + (this.config.selectLabel?this.config.selectLabel:"select") + "</a></div>";
  this.language = "es";
  this.resetOnChange = this.config.resetOnChange != null?this.config.resetOnChange:true;
  if (config.language && this.translations[config.language] != null) this.language = config.language;
}

HierarchySelector.prototype.addBehaviours = function(levelPos, jSelector) {
};

HierarchySelector.prototype.addSelectorBehaviours = function(levelPos, jSelector) {
  jSelector.change(HierarchySelector.prototype.atSelectChange.bind(this, levelPos, jSelector));
};

HierarchySelector.prototype.renderLevel = function(levelPos) {
  var level = this.config.levels[levelPos];
  
  if (!this.config.provider) return;
  
  var parent = this.config.levels[levelPos-1];
  var selection = null;
  
  if (parent != null) {
    var value = this.jSelectorContainer.find("select:last").last().find("option:selected").val();
    selection = {"code":parent.code,"value":value};
  }
  
  var result = this.config.provider.load(level, selection);
  var jSelector = $("<select></select>").attr("class", "level").attr("name", level.code);
  
  this.jSelectorContainer.append(jSelector);
  jSelector.append("<option value='all'>" + this.translations[this.language].All + "</option>");
  for (var i=0; i<result.rows.length; i++) {
    var item = result.rows[i];
    jSelector.append("<option value=" + item.code + ">" + item.value + "</option>");
  }
  
  this.addSelectorBehaviours(levelPos, jSelector);
  
  if (this.getSelected() == null) this.jAdd.addClass("disabled");
  else this.jAdd.removeClass("disabled");
};

HierarchySelector.prototype.render = function(DOMLayer) {
  var selectorTemplate = $.template(null, this.templates[this.language]);
  var content = $.tmpl(selectorTemplate, {}).get(0).outerHTML;
  
  this.jLayer = $(DOMLayer);
  this.jLayer.html(content);
  
  this.jSelectorContainer = this.jLayer.find(".layer .container");
  this.jAdd = this.jLayer.find(".add");
  
  this.jAdd.click(HierarchySelector.prototype.atAddClick.bind(this));
  this.jAdd.addClass("disabled");
  
  this.addBehaviours();
  this.renderLevel(0);
};

HierarchySelector.prototype.reset = function() {
  var jSelectList = this.jLayer.find("select");
  if (jSelectList.length == 1) return;
  jSelectList.remove();
  this.renderLevel(0);
};

HierarchySelector.prototype.getSelected = function() {
  var jSelectList = this.jSelectorContainer.find("select");
  
  var jSelect = $(jSelectList[jSelectList.length-1]);
  if (jSelectList.length > 1 && jSelectList.length != this.config.levels.length) jSelect = $(jSelectList[jSelectList.length-2]);
  
  var jOption = jSelect.find("option:selected");
  
  if (jOption.val() == "all") {
    jSelect = $(jSelectList[jSelectList.length-2]);
    if (jSelect.length == 0) return null;
    jOption = jSelect.find("option:selected");
  }
  
  return {key: jSelect.attr("name"), value: jOption.val(), label: jOption.text()};
};

HierarchySelector.prototype.atSelectChange = function(levelPos, jSelector) {
  jSelector.nextAll().remove();
  
  if (jSelector.find("option:selected").val() != "all" && levelPos < this.config.levels.length-1) 
    this.renderLevel(levelPos+1);
  else 
    if (this.getSelected() == null) this.jAdd.addClass("disabled");
    else this.jAdd.removeClass("disabled");
};

HierarchySelector.prototype.atAddClick = function() {
  if (this.onChange) this.onChange(this);
  if (this.resetOnChange) this.reset();
  return false;
};