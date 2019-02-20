//------------------------------------------------------------------
TToolHandler = function (eElement, sName) {
  this.eElement = eElement;
  this.sName = sName;
  this.Mode = null;
  this.bSelect = false;
};

//------------------------------------------------------------------
TToolHandler.prototype.SetAction = function (Action, sEvent) {
  if (!sEvent) sEvent = "onclick";
  if (this.bSelect) sEvent = "onchange";

  this.Action = Action;
  this.eElement.Handler = this;
  this.eElement[sEvent] = function (e) {
    if (this.Handler.bSelect)
      this.Handler.aParams = this.options[this.selectedIndex].value;
    if (this.nextSibling) this.nextSibling.focus();

    this.Handler.Execute();

    if (!e) var e = window.event;
    e.cancelBubble = true;
  };
};

//------------------------------------------------------------------
TToolHandler.prototype.SetEvent = function (Function, sEvent) {
  if (!sEvent) sEvent = "onclick";
  if (this.bSelect) sEvent = "onchange";

  this.eElement.Handler = this;
  this.eElement[sEvent] = Function;
};

//------------------------------------------------------------------
TToolHandler.prototype.SetParams = function (aParams) {
  this.aParams = aParams;
};

//------------------------------------------------------------------
TToolHandler.prototype.AddMode = function (value, sStyle) {
  if (!this.aModes) this.aModes = new Array;

  this.aModes[value] = sStyle;
};

//------------------------------------------------------------------
TToolHandler.prototype.SetMode = function (value) {
  var sCurrentClass = (this.aModes) ? this.aModes[this.Mode] : aModes[this.Mode];
  this.Mode = value;
  var sClass = (this.aModes) ? this.aModes[value] : aModes[value];
  var bHasClass = this.eElement.className.indexOf(sCurrentClass);

  if (bHasClass > -1) {
    this.eElement.className = this.eElement.className.replace(new RegExp(sCurrentClass), sClass);
  }
  else {
    this.eElement.className += BLANK + sClass;
  }

  if (typeof(this.eElement.disabled) == typeof(value)) this.eElement.disabled = !value;
};

//------------------------------------------------------------------
TToolHandler.prototype.Execute = function () {
  if (this.Mode) this.Action.Execute(this.aParams);
};

//------------------------------------------------------------------
TToolbarHandler = function (eElement) {
  this.eElement = eElement;
  this.aToolHandlers = new Array;
};

//------------------------------------------------------------------
TToolbarHandler.prototype.GenerateHTML = function (ToolbarDef) {
  var sHTML = EMPTY;
  var sAux = EMPTY;

  for (var key in ToolbarDef) {
    if (isFunction(ToolbarDef[key])) continue;

    var Data = ToolbarDef[key];

    if (!Data) {
      sAux = EMPTY.toLi({_class: "SEPARATOR"});
    }
    else if (Data.values) {
      var sOptions = EMPTY;

      for (var i = 0; i < Data.values.length; i++) {
        var Option = Data.values[i];
        sOptions += '<option value="' + Option.code + '">' + Option.label + '</option>';
      }

      sAux = String('<select id="::name::::key::" style="width:100%;">::options::</select>').toLi({_class: "SELECT", style: "width:::width::"});
      sAux = sAux.replace('::name::', name);
      sAux = sAux.replace('::key::', key);
      sAux = sAux.replace('::options::', sOptions);
      sAux = sAux.replace('::hint::', Data.hint);
      sAux = sAux.replace('::width::', Data.width);
    }
    else {
      sAux = String("::button::").toA({_class: "command", id: "::name::::key::", title: "::hint::", href: "::action::"}).toLi();
      var sButton = EMPTY;

      if (Data.icon) sButton = String("::icon::").replace('::icon::', Data.icon).toImg() + BLANK;
      if (Data.caption && Data.showCaption) sButton += ((sButton != EMPTY) ? BLANK : EMPTY) + Data.caption;
      if (Data.separator) sAux += String(Data.separator).toTag("span", new Object()).toLi();

      sAux = sAux.replace('::name::', name);
      sAux = sAux.replace('::key::', key);
      sAux = sAux.replace('::button::', sButton);
      sAux = sAux.replace('::hint::', Data.hint);
      sAux = sAux.replace('::action::', Data.action);
    }

    sHTML += sAux;
  }

  return sHTML;
};

//------------------------------------------------------------------
TToolbarHandler.prototype.CreateToolHandlers = function (ToolbarDef) {
  for (var key in ToolbarDef) {
    if (isFunction(ToolbarDef[key])) continue;

    var Data = ToolbarDef[key];

    if (!Data) continue;

    var eTool = $(name + key);
    var ToolHandler = new TToolHandler(eTool, key);

    if (Data.values) ToolHandler.bSelect = true;
    if (ToolbarDef[key]["params"])
      ToolHandler.SetParams(ToolbarDef[key]["params"]);

    this.aToolHandlers[key] = ToolHandler;
  }
};


//------------------------------------------------------------------
TToolbarHandler.prototype.AddDefinition = function (ToolbarDef) {
  var sHTML = '';

  sHTML += UL;
  sHTML += this.GenerateHTML(ToolbarDef);
  sHTML += _UL;

  this.eElement.innerHTML = sHTML;
  this.CreateToolHandlers(ToolbarDef);
};

//------------------------------------------------------------------
TToolbarHandler.prototype.AddDefinitions = function (aToolbarDefinition) {
  var sHTML = UL;

  for (var ToolbarDef in aToolbarDefinition) {
    if (!ToolbarDef) return;
    if (isFunction(ToolbarDef)) continue;

    sHTML += this.GenerateHTML(aToolbarDefinition[ToolbarDef]);
  }

  sHTML += _UL;
  this.eElement.innerHTML = sHTML;

  for (var ToolbarDef in aToolbarDefinition) {

    if (!ToolbarDef) return;
    if (isFunction(ToolbarDef)) continue;

    this.CreateToolHandlers(aToolbarDefinition[ToolbarDef]);
  }
};

//==================================================================
var aModes = new Array();
aModes[true] = "ENABLED";
aModes[false] = "DISABLED";