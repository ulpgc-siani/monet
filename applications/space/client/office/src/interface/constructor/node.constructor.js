CGNodeConstructor = function () {
  this.delayBlur = true;
};

CGNodeConstructor.prototype.initNodes = function (extObject) {
  var DecoratorNode = new CGDecoratorNode();

  if (extObject.hasClass(CLASS_NODE)) DecoratorNode.execute(extObject.dom);

  var aExtNodes = extObject.select(CSS_NODE);
  aExtNodes.each(function (extNode) {
    DecoratorNode.execute(extNode.dom);
  }, this);
};

CGNodeConstructor.prototype.initTasks = function (extObject) {
  var DecoratorTask = new CGDecoratorTask();

  if (extObject.hasClass(CLASS_TASK)) DecoratorTask.execute(extObject.dom);

  var aExtTasks = extObject.select(CSS_TASK);
  aExtTasks.each(function (extTask) {
    DecoratorTask.execute(extTask.dom);
  }, this);
};

CGNodeConstructor.prototype.initSections = function (extObject) {
  var DecoratorSection = new CGDecoratorSection();

  if (extObject.hasClass(CLASS_SECTION)) {
    DecoratorSection.execute(extObject.dom);
  }
  else {
    var aExtSections = extObject.select(CSS_SECTION);
    aExtSections.each(function (extSection) {
      DecoratorSection.execute(extSection.dom);
    }, this);
  }
};

CGNodeConstructor.prototype.initCollections = function (extObject) {
  var DecoratorCollection = new CGDecoratorCollection();

  if (extObject.hasClass(CLASS_COLLECTION)) {
    DecoratorCollection.execute(extObject.dom);
  }
  else {
    var aExtCollections = extObject.select(CSS_COLLECTION);
    aExtCollections.each(function (extCollection) {
      DecoratorCollection.execute(extCollection.dom);
    }, this);
  }
};

CGNodeConstructor.prototype.initNodesReferences = function (extObject) {
  var DecoratorNodeReference = new CGDecoratorNodeReference();
  var DecoratorNode = new CGDecoratorNode();

  if (extObject.hasClass(CLASS_REFERENCE)) {
    DecoratorNode.execute(extObject.dom);
    DecoratorNodeReference.execute(extObject.dom);
  }
  else {
    var aExtNodesReferences = extObject.select(CSS_REFERENCE);
    aExtNodesReferences.each(function (extNodeReference) {
      DecoratorNode.execute(extNodeReference.dom);
      DecoratorNodeReference.execute(extNodeReference.dom);
    }, this);
  }
};

CGNodeConstructor.prototype.initForms = function (extObject) {
  var DecoratorForm = new CGDecoratorForm();

  if (extObject.hasClass(CLASS_FORM)) DecoratorForm.execute(extObject.dom);
  else {
    var aExtNodeForms = extObject.select(CSS_FORM);
    aExtNodeForms.each(function (extNodeForm) {
      DecoratorForm.execute(extNodeForm.dom);
    }, this);
  }
};

CGNodeConstructor.prototype.initFields = function (extObject) {
  var DecoratorField = new CGDecoratorField();

  if (extObject.hasClass(CLASS_FIELD)) DecoratorField.execute(extObject.dom);
  else {
    var aExtFields = extObject.select(CSS_FIELD);
    aExtFields.each(function (extField) {
      DecoratorField.execute(extField.dom);
    }, this);
  }
};

CGNodeConstructor.prototype.initWidgets = function (extObject) {
  var DecoratorWidget = new CGDecoratorWidget();

  if (extObject.hasClass(CLASS_WIDGET)) DecoratorWidget.execute(extObject.dom);
  else {
    var aExtWidgets = extObject.select(CSS_WIDGET);
    aExtWidgets.each(function (extWidget) {
      DecoratorWidget.execute(extWidget.dom);
    }, this);
  }
};

CGNodeConstructor.prototype.initBehaviours = function (extObject) {
  var aBehaviours = extObject.select(".behaviour");
  aBehaviours.each(function (extBehaviour) {
    DOMBehaviour = extBehaviour.dom;
    var extParent = extBehaviour.up(CSS_NODE);
    if (extParent != extObject) return;
    Event.observe(DOMBehaviour, 'click', CGNodeConstructor.prototype.atBehaviourClick.bind(this, extObject.dom, DOMBehaviour));
    if (DOMBehaviour.hasClassName("onfocus")) Event.observe(DOMBehaviour, 'focus', CGNodeConstructor.prototype.atBehaviourFocus.bind(this, DOMBehaviour));
    if (DOMBehaviour.hasClassName("onblur")) Event.observe(DOMBehaviour, 'blur', CGNodeConstructor.prototype.atBehaviourBlur.bind(this, DOMBehaviour));
  }, this);
};

CGNodeConstructor.prototype.initInputs = function (extObject) {
  var aInputs = extObject.select(HTML_INPUT + ".check");

  if (aInputs.getCount() <= 0)
    return;

  var IdObject = (extObject.dom.getControlInfo) ? extObject.dom.getControlInfo().IdNode : null;
  if (extObject.hasClass(CLASS_REFERENCE)) IdObject = extObject.dom.getParentNode().getControlInfo().IdNode;

  aInputs.each(function (Input) {
    Input = Input.dom;
    Event.observe(Input, "click", CGNodeConstructor.prototype.atCheckInputClick.bind(this, IdObject, Input));

    Input.select = function () {
      var eRow = $(this.parentNode.parentNode);
      if (!eRow) return false;

      if (this.checked) {
        Ext.get(eRow).addClass(CLASS_SELECTED);
      }
      else {
        Ext.get(eRow).removeClass(CLASS_SELECTED);
      }
    };

    Input.getIdNode = function () {
      return this.name.replace(INPUT_PREFIX, EMPTY);
    };

  }, this);
};

CGNodeConstructor.prototype.executeFunction = function (extObject) {
  var FunctionInfo = new CGCommandInfo(extObject.dom.innerHTML);
  var aParameters = FunctionInfo.getParameters();

  if (FunctionInfo.getOperation() == "printnodetypecaption") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    var sCaption = Extension.getDefinitionCaption(aParameters[0]);
    extObject.dom.innerHTML = (aParameters[1]) ? sCaption.toShort(aParameters[1]) : sCaption;
  }
  else if (FunctionInfo.getOperation() == "formatdate") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    if ((aParameters[0] == null) || (aParameters[0] == "")) extObject.dom.innerHTML = "";
    extObject.dom.innerHTML = getFormattedDate(parseServerDate(aParameters[0]), Context.Config.Language);
  }
  else if (FunctionInfo.getOperation() == "formatdatetime") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    if ((aParameters[0] == null) || (aParameters[0] == "")) extObject.dom.innerHTML = "";
    extObject.dom.innerHTML = getFormattedDateTime(parseServerDate(aParameters[0]), Context.Config.Language);
  }
  else if (FunctionInfo.getOperation() == "previewnode") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    if ((aParameters[0] == null) || (aParameters[0] == "")) extObject.dom.innerHTML = "";
    if ((extObject.dom.id == null) || (extObject.dom.id == "")) extObject.dom.id = Ext.id();
    CommandListener.dispatchCommand("previewnode(" + aParameters[0] + "," + extObject.dom.id + ")");
  }
  else if (FunctionInfo.getOperation() == "showonlinemenu") {
    var extOnlineMenu = extObject.up("div").down(".onlinemenu");
    if (extOnlineMenu == null) return;
    var aExtCommands = extOnlineMenu.select("a");
    if (aExtCommands.getCount() == 0) return;
    if (aExtCommands.getCount() == 1) CommandListener.executeCommand(aExtCommands.item(0).dom.href);
    else {
      extOnlineMenu.show();
    }
  }
};

CGNodeConstructor.prototype.executeFunctions = function (extObject) {
  if (extObject.hasClass(CLASS_FUNCTION)) this.executeFunction(extObject);
  else {
    var aExtFunctions = extObject.select(CSS_FUNCTION);
    aExtFunctions.each(function (extFunction) {
      this.executeFunction(extFunction);
    }, this);
  }
};

CGNodeConstructor.prototype.init = function (DOMObject) {
  var extObject = Ext.get(DOMObject);

  if (extObject == null) return;

  if (!extObject.dom.id) extObject.dom.id = Ext.id();
  this.IdObject = extObject.dom.id;

  this.initNodes(extObject);
  this.initTasks(extObject);
  this.initSections(extObject);
  this.initCollections(extObject);
  this.initNodesReferences(extObject);
  this.initForms(extObject);
  this.initFields(extObject);
  this.initWidgets(extObject);
  this.initBehaviours(extObject);
  this.initInputs(extObject);

  this.executeFunctions(extObject);
};

CGNodeConstructor.prototype.addTableViewElementBehaviours = function (DOMItem) {
  var DOMDummy = new Object();
  var Decorator = new CGDecorator();
  Decorator.addCommonMethods(DOMDummy);
  return DOMDummy.addTableViewElementBehaviours(DOMItem);
};

CGNodeConstructor.prototype.atBehaviourClick = function (DOMObject, DOMBehaviour, EventLaunched) {
  var BehaviourInfo = new CGCommandInfo(DOMBehaviour.href);
  var aParameters = BehaviourInfo.getParameters();

  if (BehaviourInfo.getOperation() == "togglesection") {
    var DOMSection = Extension.getDOMNodeSection(aParameters[0]);
    DOMSection.toggle();
  }
  else if (BehaviourInfo.getOperation() == "loadnodereference") {
    var DOMNode = Extension.getDOMNodeReference(aParameters[0], aParameters[1]);
    DOMNode.toggle();
  }
  else if (BehaviourInfo.getOperation() == "scrolltosection") {
    var DOMSection = Extension.getDOMNodeSection(aParameters[0]);
    DOMSection.scrollTo(true);
  }
  else if (BehaviourInfo.getOperation() == "scrolltonodereference") {
    var DOMNode = Extension.getDOMNodeReference(aParameters[0]);
    DOMNode.scrollTo(true);
  }
  else if (BehaviourInfo.getOperation() == "selectnodesreferences") {
    DOMObject = Extension.getDOMNodeSection(aParameters[1]);
    DOMObject.selectNodesReferences(aParameters[0]);
  }
  else if (BehaviourInfo.getOperation() == "changedescriptor") {
    var DOMView = DOMBehaviour;
    if (!DOMView.hasClassName("descriptor")) DOMView = DOMBehaviour.up("descriptor");
    if ((DOMView != null) && (aParameters.length >= 2)) {
      var Descriptor = new Object();
      Descriptor.IdNode = aParameters[0];
      Descriptor.Name = aParameters[1];
      this.showDescriptorDialog(Descriptor, DOMView, aParameters[2]);
    }
  }

  Event.stop(EventLaunched);

  return false;
};

CGNodeConstructor.prototype.atBehaviourFocus = function (DOMBehaviour, EventLaunched) {
  var BehaviourInfo = new CGCommandInfo(DOMBehaviour.href);

  if (BehaviourInfo.getOperation() == "toggleonlinemenu") {
    var extOnlineMenu = Ext.get(DOMBehaviour.up("div").down(".onlinemenu"));
    if (extOnlineMenu == null) return;
    var aExtCommands = extOnlineMenu.select("a");
    if (aExtCommands.getCount() == 0) return;
    if (aExtCommands.getCount() == 1) {
      CommandListener.dispatchCommand(aExtCommands.item(0).dom.href);
    }
    extOnlineMenu.show();
    DOMBehaviour.addClassName("focus");
  }
};

CGNodeConstructor.prototype.atBehaviourBlur = function (DOMBehaviour, EventLaunched) {
  var BehaviourInfo = new CGCommandInfo(DOMBehaviour.href);

  if (BehaviourInfo.getOperation() == "toggleonlinemenu") {
    if (this.delayBlur) {
      this.delayBlur = false;
      window.setTimeout(this.atBehaviourBlur.bind(this, DOMBehaviour, EventLaunched), 150);
      return;
    }
    var extOnlineMenu = Ext.get(DOMBehaviour.up("div").down(".onlinemenu"));
    if (extOnlineMenu == null) return;
    extOnlineMenu.hide();
    DOMBehaviour.removeClassName("focus");
    this.delayBlur = true;
  }
};

CGNodeConstructor.prototype.showDescriptorDialog = function (Descriptor, DOMDescriptorView, ViewMode) {
  var id = Ext.id(), extInput, extAccept;
  var DOMDescriptorDialog;

  if (!DOMDescriptorView.hasClassName("view")) Ext.get(DOMDescriptorView).addClass("view");

  DOMDescriptorDialog = DOMDescriptorView.next(".descriptor.dialogembed");
  if (!DOMDescriptorDialog) {
    new Insertion.After(DOMDescriptorView, "<div class='descriptor dialogembed' id='" + id + "'>" + ((ViewMode == "textarea") ? "<textarea class='textbox'></textarea>" : "<input class='textbox' type='text'/>") + "<a href='javascript:void(null)'>" + Lang.Buttons.Accept + "</a></div>");
    DOMDescriptorDialog = DOMDescriptorView.next(".descriptor.dialogembed");
    DOMDescriptorDialog.Descriptor = Descriptor;
    DOMInput = DOMDescriptorDialog.down(".textbox");
    extInput = Ext.get(DOMInput);
    extInput.on("keypress", this.atDescriptorDialogKeyPress, this);
    DOMAccept = DOMDescriptorDialog.down("a");
    extAccept = Ext.get(DOMAccept);
    extAccept.on("click", this.atDescriptorDialogAccept, this);
  }

  if (!DOMInput) DOMInput = DOMDescriptorDialog.down(".textbox");

  DOMDescriptorView.style.display = "none";
  DOMDescriptorDialog.style.display = "block";

  DOMInput.value = HtmlUtil.decode(DOMDescriptorView.innerHTML);
  DOMInput.focus();
  DOMInput.select();

  CommandListener.capture(DOMDescriptorDialog);
};

CGNodeConstructor.prototype.atDescriptorDialogKeyPress = function (EventLaunched, DOMInput) {
  var codeKey = EventLaunched.getKey();
  var DOMDescriptorDialog = DOMInput.up(".descriptor.dialogembed");
  if (!DOMDescriptorDialog) return;
  if (DOMDescriptorDialog.down(".textbox").value == "") return;
  if (codeKey == EventLaunched.ESC) this.showDescriptorView(DOMDescriptorDialog);
  if (codeKey == EventLaunched.ENTER) {
    var DOMDescriptorView = DOMDescriptorDialog.previous(".descriptor.view");
    var Descriptor = DOMDescriptorDialog.Descriptor;
    var sValue = DOMDescriptorDialog.down(".textbox").value;
    DOMDescriptorView.innerHTML = HtmlUtil.encode(sValue);
    CommandListener.dispatchCommand("savenodedescriptor(" + Descriptor.IdNode + "," + Descriptor.Name + "," + escape(sValue) + ")");
    this.showDescriptorView(DOMDescriptorDialog);
  }
};

CGNodeConstructor.prototype.atDescriptorDialogAccept = function (EventLaunched, DOMAccept) {
  var DOMDescriptorDialog = DOMAccept.up(".descriptor.dialogembed");
  if (!DOMDescriptorDialog) return;
  if (DOMDescriptorDialog.down(".textbox").value == "") return;
  var DOMDescriptorView = DOMDescriptorDialog.previous(".descriptor.view");
  var Descriptor = DOMDescriptorDialog.Descriptor;
  var sValue = DOMDescriptorDialog.down(".textbox").value;
  DOMDescriptorView.innerHTML = HtmlUtil.encode(sValue);
  CommandListener.dispatchCommand("savenodedescriptor(" + Descriptor.IdNode + "," + Descriptor.Name + "," + escape(sValue) + ")");
  this.showDescriptorView(DOMDescriptorDialog);
  Event.stop(EventLaunched);
  return false;
};

CGNodeConstructor.prototype.atShowDescriptorView = function (EventLaunched, DOMInput) {
  var DOMDescriptorDialog = DOMInput.up(".descriptor.dialogembed");
  if (!DOMDescriptorDialog) return;
  this.showDescriptorView(DOMDescriptorDialog);
  Event.stop(EventLaunched);
  return false;
};

CGNodeConstructor.prototype.showDescriptorView = function (DOMDescriptorDialog) {
  var DOMDescriptorView = DOMDescriptorDialog.previous(".descriptor.view");
  DOMDescriptorDialog.style.display = "none";
  if (DOMDescriptorView) {
    DOMDescriptorView.style.display = "";
  }
};

CGNodeConstructor.prototype.atCheckInputClick = function (IdNode, Input, EventLaunched) {
  if (Input.select) Input.select();
  if (this.onSelectNodeReference) this.onSelectNodeReference(IdNode, Input.getIdNode(), Input.checked);
};