CGExtension = function () {
  this.BusinessModel = new CGBusinessModel();
  this.BusinessModel.init(new CGDataLink());
};

CGExtension.prototype.getAllDefinitions = function (Code) {
  if (!this.BusinessModel.getDefinitions) return;

  var aDefinitions, aResult = new Array();

  aDefinitions = this.BusinessModel.getDefinitions(Code);
  if (!aDefinitions) aDefinitions = this.BusinessModel.getDefinitions(DEFAULT);
  if (!aDefinitions) return;

  for (var Index in aDefinitions) {
    if (isFunction(aDefinitions[Index])) continue;
    aResult.push(aDefinitions[Index]);
  }

  return aResult;
};

CGExtension.prototype.getDefinition = function (Code) {
  if (!this.BusinessModel.getDefinition) return;
  return this.BusinessModel.getDefinition(Code);
};

CGExtension.prototype.getDefinitions = function (DOMElement, Code) {
  if (!this.BusinessModel.getDefinitions) return;

  var Definition, aCodeNodes;
  var aDefinitions = this.BusinessModel.getDefinitions(Code);
  var aResult = new Array();

  if (!aDefinitions) aDefinitions = this.BusinessModel.getDefinitions(DEFAULT);
  if (!aDefinitions) return;

  if ((aCodeNodes = this.getDOMElementCodeNodes(DOMElement)) == false) return aResult;

  for (var Index in aCodeNodes) {
    if (isFunction(aCodeNodes[Index])) continue;
    if ((Definition = aDefinitions[aCodeNodes[Index]]) == null) continue;
    aResult.push(Definition);
  }

  return aResult;
};

CGExtension.prototype.getTaskDefinitions = function () {
  var aDefinitions, aResult;

  if (!this.BusinessModel.getTaskDefinitions) return;

  aResult = new Array();
  aDefinitions = this.BusinessModel.getTaskDefinitions();

  for (var Index in aDefinitions) {
    if (isFunction(aDefinitions[Index])) continue;
    aResult.push(aDefinitions[Index]);
  }

  return aResult;
};

CGExtension.prototype.isDefinitionComponent = function (Code) {
  var Definition = this.getDefinition(Code);
  if (Definition == null) return false;
  return Definition.IsComponent;
};

CGExtension.prototype.getDefinitionCaption = function (Code) {
  if (!this.BusinessModel.getDefinitionCaption) return EMPTY;
  var sCaption = this.BusinessModel.getDefinitionCaption(Code);
  return (sCaption) ? sCaption : EMPTY;
};

CGExtension.prototype.getDefinitionViews = function (DOMElement, Code, Type) {
  var Mode;

  if (!this.BusinessModel.getDefinitionViews) return;
  if ((Mode = this.getDOMElementMode(DOMElement)) == false) return false;

  var DefinitionViews = this.BusinessModel.getDefinitionViews(Code, Type);
  if (!DefinitionViews) DefinitionViews = this.BusinessModel.getDefinitionViews(DEFAULT, Type);
  if (!DefinitionViews) return;

  return DefinitionViews[Mode];
};

CGExtension.prototype.getDefinitionDefaultView = function (Code, Type) {
  if (!this.BusinessModel.getDefinitionViews) return;

  var aDefinitions = this.BusinessModel.getDefinitionViews(Code, Type);
  if (!aDefinitions) DefinitionViewList = this.BusinessModel.getDefinitionViews(DEFAULT, Type);
  if (!aDefinitions) return;

  return aDefinitions[DEFAULT_MODE][DEFAULT];
};

CGExtension.prototype.getDefinitionBehaviour = function (Code) {
  if (!this.BusinessModel.getDefinitionBehaviour) return;

  var Behaviour = this.BusinessModel.getDefinitionBehaviour(Code);
  if (!Behaviour) Behaviour = this.BusinessModel.getDefinitionBehaviour(DEFAULT);

  return Behaviour;
};

CGExtension.prototype.isDefinitionExportable = function (Code) {
  var Behaviour;
  if (!(Behaviour = this.getDefinitionBehaviour(Code))) return;
  return (Behaviour.Export != null);
};

CGExtension.prototype.getNodeConstructor = function () {
  return new CGNodeConstructor();
};

CGExtension.prototype.getEditNodeConstructor = function () {
  return new CGNodeConstructor();
};

CGExtension.prototype.getTaskConstructor = function () {
  return new CGTaskConstructor();
};

CGExtension.prototype.getHelperItemConstructor = function () {
  return new CGHelperItemConstructor();
};

CGExtension.prototype.getTeamConstructor = function () {
  return new CGTeamConstructor();
};

CGExtension.prototype.getDOMNode = function (IdNode) {
  return $(NODE_ID_PREFIX + IdNode);
};

CGExtension.prototype.getDOMNodeSection = function (IdSection) {
  return $(SECTION_ID_PREFIX + IdSection);
};

CGExtension.prototype.getDOMNodeReference = function (IdNodeReference) {
  return $(NODEREFERENCE_ID_PREFIX + IdNodeReference);
};

CGExtension.prototype.getDOMNodeCollection = function (DOMCommand) {
  var extCommand = Ext.get(DOMCommand);
  var extElement = extCommand.up(CSS_COLLECTION);
  if (extElement) return extElement.dom;
  return null;
};

CGExtension.prototype.isDOMNodeCollection = function (DOMNode) {
  return DOMNode.hasClassName(CLASS_COLLECTION);
};

CGExtension.prototype.getDOMNodeForm = function (DOMCommand) {
  var extCommand = Ext.get(DOMCommand);
  var extElement = extCommand.up(CSS_FORM);
  if (extElement) return extElement.dom;
  return null;
};

CGExtension.prototype.isDOMNodeForm = function (DOMNode) {
  return DOMNode.hasClassName(CLASS_FORM);
};

CGExtension.prototype.getDOMTask = function (DOMCommand) {
  var extCommand = Ext.get(DOMCommand);
  var extElement = extCommand.up(CSS_TASK);
  if (extElement) return extElement.dom;
  return null;
};

CGExtension.prototype.getDOMTeam = function (DOMCommand) {
  var extCommand = Ext.get(DOMCommand);
  var extElement = extCommand.up(CSS_TEAM);
  if (extElement) return extElement.dom;
  return null;
};

CGExtension.prototype.getDOMElementMode = function (DOMElement) {
  var Mode = DOMElement.getControlInfo().Mode;
  return (Mode != null) ? Mode : false;
};

CGExtension.prototype.getDOMElementCodeNodes = function (DOMElement) {
  var sNodes = DOMElement.getControlInfo().Nodes;
  return (sNodes != null) ? sNodes.split(CONTROL_INFO_SECTION_NODES_SEPARATOR) : false;
};

CGExtension.prototype.getTaskIdInput = function (DOMRootNode) {
  if (!this.BusinessModel.getTaskIdInput) return null;
  return this.BusinessModel.getTaskIdInput(DOMRootNode);
};