CGBusinessModel = function() {
  this.DataLink = null;
  this.createDefinitionLists();
  this.initDefaultDefinition();
};

CGBusinessModel.prototype.init = function(DataLink) {
  this.DataLink = DataLink;
};

CGBusinessModel.prototype.getCode = function() {
  return "::code::";
};

CGBusinessModel.prototype.loadDefinition = function(Code) {
  var Definition = null;
  var aParameters = {code: Code};
  var sResult = this.DataLink.loadDefinition(aParameters);
  
  eval(sResult);
  
  if (Definition == null) {
    alert("Definition not found: '" + Code + "'");
    return false;
  }

  this.aDefinitions[Code] = Definition;
};

CGBusinessModel.prototype.loadDefinitionBehaviourEvents = function(Code) {
  var aParameters = {path: "scripts/" + Code + ".client.js"};
  var sResult = this.DataLink.loadFile(aParameters);
  var sBehaviourEvents = "var BehaviourEvents = " + Code.replace(/\\./g,"_") + ";";
  
  if (sResult.substring(0,4) == "ERR_") {
    this.aDefinitions[Code].Behaviour.Events.Loaded = true;
    return;
  }
  
  eval(sResult);
  eval(sBehaviourEvents);
  
  if (BehaviourEvents == null) {
    alert("Behaviours not found for definition '" + Code + "'");
    return false;
  }

  this.aDefinitions[Code].Behaviour.Events = BehaviourEvents;
  this.aDefinitions[Code].Behaviour.Events.Loaded = true;
};

CGBusinessModel.prototype.createDefinitionLists = function() {
  this.aDefinitions = new Array();
  
  this.aDefinitionsTypes = new Array();
  this.aDefinitionsTypes['desktop'] = new Array();
  this.aDefinitionsTypes['container'] = new Array();
  this.aDefinitionsTypes['collection'] = new Array();
  this.aDefinitionsTypes['catalog'] = new Array();
  this.aDefinitionsTypes['form'] = new Array();
  this.aDefinitionsTypes['document'] = new Array();
  this.aDefinitionsTypes['task'] = new Array();
  this.aDefinitionsTypes['service'] = new Array();
  this.aDefinitionsTypes['undefined'] = new Array();
  
  ::definitionList::
  ::definitionTypeList::
};

CGBusinessModel.prototype.initDefaultDefinition = function () {
  this.aDefinitions["default"] = new Object();
  this.aDefinitions["default"].Code = null;
  this.aDefinitions["default"].Caption = "Default type node";
  this.aDefinitions["default"].Loaded = true;
  this.aDefinitions["default"].Views = new Array();
  this.aDefinitions["default"].Views["edition"] = new Array();
  this.aDefinitions["default"].Views["edition"]["default"] = {Name: "preview.html?mode=page", Caption:"Normal", Hint:"Show the element with default view"};
  this.aDefinitions["default"].Views["preview"] = new Array();
  this.aDefinitions["default"].Views["preview"]["default"] = {Name: "preview.html?mode=page", Caption:"Normal", Hint:"Show the element with default view"};
  this.aDefinitions["default"].Collection = new Object();
  this.aDefinitions["default"].Collection.Items = new Array();
  this.aDefinitions["default"].Behaviour = new Object();
  this.aDefinitions["default"].Behaviour.AddNode = {Templates : {Edit: "edit.html?mode=page", View: "preview.html?mode=page"}};
  this.aDefinitions["default"].Behaviour.ShowNode = {Templates : {Edit: "edit.html?mode=page", View: "preview.html?mode=page"}};
  this.aDefinitions["default"].Behaviour.ShowTask = {Templates : {Edit: "preview.html?mode=page", View: "preview.html?mode=page"}};
  this.aDefinitions["default"].Behaviour.Events = new Object();
  this.aDefinitions["default"].Behaviour.Events.Loaded = true;
};

CGBusinessModel.prototype.getDefinitions = function(Code) {
  var aResult = new Array();

  if (this.aDefinitions[Code] == null) return false;
  if (!this.aDefinitions[Code].Loaded) this.loadDefinition(Code);
  if (!this.aDefinitions[Code].Collection) return aResult;

  for(var iPos=0; iPos<this.aDefinitions[Code].Collection.Items.length; iPos++) {
    var CodeDefinition = this.aDefinitions[Code].Collection.Items[iPos];
    if (this.aDefinitions[CodeDefinition]) aResult[CodeDefinition] = this.aDefinitions[CodeDefinition];
  }

  return aResult;
};

CGBusinessModel.prototype.getTaskDefinitions = function() {
  var aResult = new Array();

  if (this.aDefinitionsTypes['task'] == null) return aResult;

  for(var iPos=0; iPos<this.aDefinitionsTypes['task'].length; iPos++) {
    var CodeDefinition = this.aDefinitionsTypes['task'][iPos];
    if (this.aDefinitions[CodeDefinition]) aResult[CodeDefinition] = this.aDefinitions[CodeDefinition];
  }

  return aResult;
};

CGBusinessModel.prototype.getDefinition = function(Code) {
  var aResult = new Array();

  if (this.aDefinitions[Code] == null) return false;
  if (!this.aDefinitions[Code].Loaded) this.loadDefinition(Code);

  return this.aDefinitions[Code];
};

CGBusinessModel.prototype.getDefinitionCaption = function(Code) {
  if (this.aDefinitions[Code] == null) return false;
  return this.aDefinitions[Code].Caption;
};

CGBusinessModel.prototype.getDefinitionViews = function(Code, Type) {
  if (this.aDefinitions[Code] == null) return false;
  if (!this.aDefinitions[Code].Loaded) this.loadDefinition(Code);
  if (!this.aDefinitions[Code].Views) return false;
  return this.aDefinitions[Code].Views[Type];
};

CGBusinessModel.prototype.getDefinitionDescriptors = function(Code) {
  if (this.aDefinitions[Code] == null) return false;
  if (!this.aDefinitions[Code].Loaded) this.loadDefinition(Code);
  if (!this.aDefinitions[Code].Collection) return false;
  return this.aDefinitions[Code].Collection.Descriptors;
};

CGBusinessModel.prototype.getDefinitionBehaviour = function(Code) {
  return this.aDefinitions["default"].Behaviour;
/*
  if (this.aDefinitions[Code] == null) return false;
  if (!this.aDefinitions[Code].Loaded) this.loadDefinition(Code);
  if (!this.aDefinitions[Code].Behaviour.Events.Loaded) this.loadDefinitionBehaviourEvents(Code);
  return this.aDefinitions[Code].Behaviour;
*/
};

CGBusinessModel.prototype.getTaskIdInput = function(DOMRootNode) {
  var aDOMCollections, ControlInfo, Node;
  aDOMCollections = DOMRootNode.getCollections();
  if (aDOMCollections.length <= 0) return null;
  ControlInfo = aDOMCollections[0].getControlInfo();
  return ControlInfo.IdNode;
};

@definition
this.aDefinitions["::code::"] = {Code: "::code::", Type: "::type::", Caption: "::label::", Description: "::description::", IsComponent: ::isComponent::, Loaded: false};

@definitionType
this.aDefinitionsTypes["::type::"].push("::code::");
