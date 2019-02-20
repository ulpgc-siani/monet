function isFunction(item) {
  return (typeof item == "function");
}

function HierarchyMultipleSelector(config) {
  this.config = config;
  this.translations = new Object();
  this.translations["es"] = new Object();
  this.translations["es"].All = "todos";
  this.translations["en"] = new Object();
  this.translations["en"].All = "all";
  this.templates = new Object();
  this.templates["es"] = new Object();
  this.templates["es"].Layout = "<div class='layer'><ul class='list'></ul></div>";
  this.templates["es"].ListItem = "<li class='option ${code}'><input type='checkbox' value='${value}'/><a class='hoverable ${leaf}' style='margin-left:4px;' title='${label}'>${label}</a><div class='colorbox'></div><ul class='children' style='display:none;'>Cargando...</ul></li>";
  this.templates["es"].EmptyList = "<li>No existen opciones</li>";
  this.templates["es"].LoadingList = "<li style='padding:5px;'>obteniendo datos...</li>";
  this.templates["en"] = new Object();
  this.templates["en"].Layout = "<div class='layer'><ul class='list'></ul></div>";
  this.templates["en"].ListItem = "<li class='option ${code}'><input type='checkbox' value='${value}'/><a class='hoverable ${leaf}' style='margin-left:4px;' title='${label}'>${label}</a><div class='colorbox'></div><ul class='children' style='display:none;'>Loading...</ul></li>";
  this.templates["en"].EmptyList = "<li>No options</li>";
  this.templates["en"].LoadingList = "<li style='padding:5px;'>retrieving data...</li>";
  this.language = "es";
  this.selection = (config.selection!=null)?config.selection:new Object();
  this.resetOnChange = this.config.resetOnChange != null?this.config.resetOnChange:true;
  if (config.language && this.translations[config.language] != null) this.language = config.language;
};

HierarchyMultipleSelector.prototype.addBehaviours = function(jList) {
  jList.find("a").click(HierarchyMultipleSelector.prototype.atToggleOption.bind(this));
  jList.find("input").change(HierarchyMultipleSelector.prototype.atOptionChange.bind(this));
};

HierarchyMultipleSelector.prototype.cleanCode = function(code) {
  return code.replace(/ /g, ".").replace(/[^\w\s]/gi, "");
};

HierarchyMultipleSelector.prototype.getList = function(parentItem) {
  var jList = this.jList;
  
  if (parentItem != null)
    jList = parentItem.jOption.find(".children");
  
  return jList;
};

HierarchyMultipleSelector.prototype.renderItems = function(parentItem) {
  
  this.jLayer.removeClass("empty");
  
  if (!this.config.provider) return;
  
  var jList = this.getList(parentItem);
  jList.html(this.templates[this.language].LoadingList);
  
  if (parentItem != null) {
    if (parentItem.jOption == null)
      parentItem.jOption = this.jList.find(".option." + parentItem.name);
  }
  
  this.config.provider.load(parentItem!=null?parentItem.name:null, HierarchyMultipleSelector.prototype.renderItemsCallback.bind(this, parentItem));
};
  
HierarchyMultipleSelector.prototype.renderItemsCallback = function(parentItem, result) {  
  var jList = this.getList(parentItem);

  jList.html("");
  
  if (result.rows.length <= 0) {
    jList.html(this.templates[this.language].EmptyList);
    this.jLayer.addClass("empty");
  }
  
  for (var i=0; i<result.rows.length; i++) {
    var item = result.rows[i];
    var cleanCode = this.cleanCode(item.code);

    if (cleanCode == "") continue;

    var listItemTemplate = $.template(null, this.templates[this.language].ListItem);
    var jItem = $.tmpl(listItemTemplate, { code: cleanCode, leaf: "leaf_" + item.leaf, value: item.code, label: item.name, label: item.name });
    var jCheckBox = jItem.find("input");
    var jColorBox = jItem.find(".colorbox");
    
    jItem.code = item.code;
    jItem.cleanCode = cleanCode;
    if (this.selection[item.code] != null) {
      jCheckBox.attr("checked", true);
      this.selection[item.code] = { value: item.code, label: item.name };
    }
    
    jColorBox.click(HierarchyMultipleSelector.prototype.atOptionColorClick.bind(this, item.code));
    jList.append(jItem);
  }
  
  this.addBehaviours(jList);
  
  if (parentItem != null) {
    parentItem.jOption.addClass("loaded");
    this.updateChildrenOptions(parentItem.jOption.find("input:first").get(0));
  }
  
  if (this.onRender) this.onRender(this);
};

HierarchyMultipleSelector.prototype.render = function(DOMLayer) {
  var selectorTemplate = $.template(null, this.templates[this.language].Layout);
  var content = $.tmpl(selectorTemplate, {}).get(0).outerHTML;
  
  this.jLayer = $(DOMLayer);
  this.jLayer.html(content);
  this.jList = this.jLayer.find(".layer .list");

  this.renderItems(null);
};

HierarchyMultipleSelector.prototype.reset = function() {
  this.selection = new Object();
  this.renderItems(null);
};

HierarchyMultipleSelector.prototype.clearColors = function(code) {
  this.jLayer.find(".colorbox").css("background-color", "");
};

HierarchyMultipleSelector.prototype.getOptions = function(codes) {
  var result = [];
  for (var i=0; i<codes.length; i++)
    result.push(this.getOption(codes[i]));
  return result;
};

HierarchyMultipleSelector.prototype.searchOption = function(code) {
  var jOption = this.jList.find(".option." + this.cleanCode(code));
  
  if (jOption.length == 0) {
    var cleanCode = this.cleanCode(code);
    jOption = this.jList.find(".option." + cleanCode);
    if (jOption.length == 0)
      return null;
  }

  return jOption;
};

HierarchyMultipleSelector.prototype.getOption = function(code) {
  var jOption = this.searchOption(code);

  if (jOption == null)
    return null;
  
  var option = {};
  option.code = code;
  option.label = jOption.find("input:first").next("a").html();
  option.jOption = jOption;
  
  option.checked = function() {
    return this.jOption.find("input:first").get(0).checked;
  };
  
  option.setColor = function(color) {
    this.jOption.find(".colorbox:first").css("background-color", color);
  };
  
  return option;
};

HierarchyMultipleSelector.prototype.selectAll = function() {
  var DOMInputList = this.jLayer.find("input");

  for (var i=0; i<DOMInputList.length; i++) {
    var DOMInput = DOMInputList[i];
    var value = DOMInput.value;
    DOMInput.checked = true;
    this.selection[value] = { value: value, label: $(DOMInput).next("a").html() };
  }
};

HierarchyMultipleSelector.prototype.setSelection = function(selection) {
  this.selection = {};
  for (var i=0;i<selection.length;i++) {
    var selected = selection[i];
    this.selection[selected] = { value : selected, label: "" };
  }
};

HierarchyMultipleSelector.prototype.getSelection = function() {
  var result = new Array();
  for (var index in this.selection) {
    if (isFunction(this.selection[index])) continue;
    if (this.selection[index] != null) result.push(this.selection[index].value);
  } 
  return result;
};

HierarchyMultipleSelector.prototype.removeParentsFromSelection = function(DOMInput) {
  var jOption = $(DOMInput).parents("li.option:first");
  var jParent = jOption.parents("li.option:first");

  while (jParent.length > 0) {
    var DOMParentInput = jParent.find("input").get(0);

    if (DOMInput.checked)
      delete this.selection[DOMParentInput.value];
    else if (DOMParentInput.checked)
      this.selection[DOMParentInput.value] = { value: DOMParentInput.value, label: $(DOMParentInput).next("a").html() };

    jParent = jParent.parents("li.option:first");
  }
};

HierarchyMultipleSelector.prototype.highlightParents = function(DOMInput) {
  if (DOMInput.checked) {
    var jParent = $(DOMInput).parents("li.option:first");
    while (jParent.length > 0) {
      jParent.addClass("selected");
      jParent = jParent.parents("li.option:first");
    }
  }
  else {
    var jParent = $(DOMInput).parents("li.option:first");
    while (jParent.length > 0) {
      var jSelectedChildren = jParent.find("input:checked");
      if (jSelectedChildren.length == 0) jParent.removeClass("selected");
      jParent = jParent.parents("li.option:first");
    }
  }
};

HierarchyMultipleSelector.prototype.updateChildrenOptions = function(DOMInput) {
  var jInput = $(DOMInput);
  var jOption = jInput.parents(".option:first");
  var DOMInputList = jOption.find(".children:first").find("input");
  for (var i=0; i<DOMInputList.length; i++) {
    var DOMCurrentInput = DOMInputList[i];
    DOMCurrentInput.checked = DOMInput.checked;
    this.highlightParents(DOMCurrentInput);
    delete this.selection[DOMCurrentInput.value];
  }
};

HierarchyMultipleSelector.prototype.updateOption = function(DOMInput) {
  
  if (DOMInput.checked) this.selection[DOMInput.value] = { value: DOMInput.value, label: $(DOMInput).next("a").html() };
  else delete this.selection[DOMInput.value];

  this.removeParentsFromSelection(DOMInput);
  this.highlightParents(DOMInput);
  this.updateChildrenOptions(DOMInput);
  
  if (this.onChange) this.onChange(this);
};

HierarchyMultipleSelector.prototype.isEmpty = function() {
  return this.jLayer.hasClass("empty");
};

HierarchyMultipleSelector.prototype.isHiddenable = function() {
  return this.jLayer.hasClass("hiddenable");
};

HierarchyMultipleSelector.prototype.atToggleOption = function(jEvent) {
  var jLink = $(jEvent.target);
  var jOption = jLink.parents(".option:first");
  var jCheckbox = jOption.find("input");
  
  if (jLink.hasClass("leaf_true")) {
    var DOMCheckbox = jCheckbox.get(0);
    DOMCheckbox.checked = !DOMCheckbox.checked;
    this.updateOption(jCheckbox.get(0));
    return;
  }
  
  var jChildren = jOption.find(".children:first");

  if (jLink.hasClass("opened"))
    jChildren.hide();
  else {
    jChildren.show();
    if (!jOption.hasClass("loaded")) {
      var parentItem = new Object();
      parentItem.name = jCheckbox.val();
      parentItem.jOption = jOption;
      this.renderItems(parentItem);
    }
  }
  
  jLink.toggleClass("opened");
};

HierarchyMultipleSelector.prototype.atOptionChange = function(jEvent) {
  var DOMInput = jEvent.target;
  this.updateOption(DOMInput);
  return false;
};

HierarchyMultipleSelector.prototype.atOptionColorClick = function(optionCode) {
  if (this.onColorClick) {
    var jOption = this.searchOption(optionCode);
    
    if (jOption == null)
      return null;
    
    var extra = {};
    extra.DOMColorBox = jOption.find(".colorbox:first").get(0);
    extra.getColorDOM = function() {
      return this.DOMColorBox;
    }

    this.onColorClick(this, optionCode, extra);
  }
};