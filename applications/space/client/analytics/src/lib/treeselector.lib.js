function TreeSelector(config) {
  this.config = config;
  this.translations = new Object();
  this.translations["es"] = new Object();
  this.translations["en"] = new Object();
  this.templates = new Object();
  this.templates["es"] = new Object();
  this.templates["es"].Layer = "<div class='layer'><ul class='container'></ul></div>";
  this.templates["es"].ListItem = "<li class='${css}'><ul class='children'></ul><a class='${id}' title='${value}'>${value}</a></li>";
  this.templates["en"] = new Object();
  this.templates["en"].Layer = "<div class='layer'><ul class='container'></ul></div>";
  this.templates["en"].ListItem = "<li class='${css}'><ul class='children'></ul><a class='${id}' title='${value}'>${value}</a></li>";
  this.language = "es";
  this.active = { option: null, link: null };
  if (config.language && this.translations[config.language] != null) this.language = config.language;
}

TreeSelector.prototype.shortLabel = function(label) {
  if (label.length <= 30) return label;
  return label.substring(0, 30) + "...";
};

TreeSelector.prototype.addBehaviours = function() {
  var DOMLinkList = this.jContainer.find("li a");
  for (var i=0; i<DOMLinkList.length; i++) {
    var jLink = $(DOMLinkList[i]);
    jLink.click(TreeSelector.prototype.atClick.bind(this, jLink));
  }
};

TreeSelector.prototype.renderOption = function(option, jContainer) {
  var jOption = $.tmpl(this.templates[this.language].ListItem, { id: option.id, value: option.value, shortLabel: this.shortLabel(option.value), css: option.css });

  jContainer.append(jOption);
  
  var jChildren = jOption.find(".children");
  for (var i=0; i<option.children.length; i++) {
    var childOption = option.children[i];
    childOption.id = option.id + "-" + i;
    this.renderOption(childOption, jChildren);
  }
  
};

TreeSelector.prototype.render = function(DOMLayer) {
  var selectorTemplate = $.template(null, this.templates[this.language].Layer);
  var content = $.tmpl(selectorTemplate, {}).get(0).outerHTML;
  
  this.jLayer = $(DOMLayer);
  this.jLayer.html(content);
  
  this.jContainer = this.jLayer.find(".layer .container");
  
  for (var i=0; i<this.config.options.length; i++) {
    var option = this.config.options[i];
    option.id = i;
    this.renderOption(option, this.jContainer);
  }
  
  if (this.config.options.length > 0 && this.config.activateFirst) this.activateOption(this.config.options[0]);
  
  this.addBehaviours();
};

TreeSelector.prototype.setOptions = function(options) {
  this.config.options = options;
};

TreeSelector.prototype.getActiveOption = function() {
  return this.active.option;
};

TreeSelector.prototype.activateOption = function(option) {
  var jLink = this.jContainer.find("li a." + option.id);
  jLink.addClass("active");
  this.active = { option: option, link: jLink };
  if (this.onClick) this.onClick(option);
};

TreeSelector.prototype.deactivateOption = function(option) {
  var jLink = this.jContainer.find("li a." + option.id);
  jLink.removeClass("active");
  this.active = { option: null, link: null };
};

TreeSelector.prototype.atClick = function(jLink) {
  
  if (this.activeLink == jLink)
    return;
  
  var index = jLink.attr("class").replace("active","").trim();
  var indexArray = index.split("-");
  var options = this.config.options;
  var option = null;
  
  if (this.active.option != null)
    this.deactivateOption(this.active.option);
  
  for (var i=0; i<indexArray.length; i++) {
    var index = indexArray[i];
    option = options[index];
    options = option.children;
  }

  this.activateOption(option);
};