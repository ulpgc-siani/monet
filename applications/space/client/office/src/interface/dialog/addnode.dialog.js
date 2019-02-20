ADD_NODE_BLANK = "blank";
ADD_NODE_FROM_FILE = "file";
ADD_NODE_FROM_CLIPBOARD = "clipboard";

ADD_NODE_OPTION_PRESERVE_ORIGINAL = "preserve_original";
ADD_NODE_OPTION_REPLACE = "replace";
ADD_NODE_OPTION_DUPLICATE = "duplicate";

CGDialogAddNode = function () {
  this.base = CGDialog;
  this.base("dlgAddNode");
  this.DataImporter = null;
  this.Wizard = null;
  this.NodeType = new Object();
};

//------------------------------------------------------------------
CGDialogAddNode.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogAddNode.prototype.init = function () {

  this.initWizard();
  this.initAddBlank();
  this.initAddFromClipboard();
  this.initBehaviours();
  this.initRequiredFields();

  if ((!Ext.isIE) && (frames["dlgAddNode.uploadfileframe"])) delete(frames["dlgAddNode.uploadfileframe"]);
  var eFrame = frames["dlgAddNode.uploadfileframe"];
  this.input = new InputFiles(eFrame, $("dlgAddNode.filelist"), 1, 90, 263);
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.initWizard = function () {
  var html, DOMLayer;

  html = AppTemplate.DialogAddNode;
  html = translate(html, Lang.DialogAddNode);

  this.Wizard = new CGWizard();
  this.Wizard.init(html);
  this.Wizard.onCancel = this.atCancel.bind(this);
  this.Wizard.onAccept = this.atAccept.bind(this);
  this.Wizard.PreviousStepHandler = this.atPreviousStep.bind(this);
  this.Wizard.NextStepHandler = this.atNextStep.bind(this);
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.initAddBlank = function () {
  var Store = new Ext.data.SimpleStore({fields: ['code', 'caption']});

  this.AddBlankNodeTypeList = new Ext.form.ComboBox({
    store: Store,
    displayField: 'caption',
    emptyText: Lang.DialogAddNode.SelectOne,
    typeAhead: true,
    triggerAction: 'all',
    hideTrigger: false,
    mode: 'local',
    resizable: true,
    width: 500,
    listWidth: 500,
    tpl: new Ext.Template("<div>{caption}</div>")
  });

  this.AddBlankNodeTypeList.applyTo('dlgAddNode.addblanklistinput');
  this.AddBlankNodeTypeList.on('select', this.atSelectAddBlankNodeType, this);
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.initAddFromClipboard = function () {
  this.initDataImporter();
  this.initNodeTypeSelector();
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.initDataImporter = function () {
  this.DataImporter = new CGDataImporter();
  this.DataImporter.init('dlgAddNode.dataimporter');
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.initNodeTypeSelector = function () {
  var Store = new Ext.data.SimpleStore({fields: ['code', 'caption']});

  this.AddFromClipboardNodeTypeList = new Ext.form.ComboBox({
    store: Store,
    displayField: 'caption',
    emptyText: Lang.DialogAddNode.SelectOne,
    typeAhead: true,
    triggerAction: 'all',
    hideTrigger: false,
    mode: 'local',
    resizable: true,
    width: 500,
    listWidth: 500,
    tpl: new Ext.Template("<div>{caption}</div>")
  });

  this.AddFromClipboardNodeTypeList.applyTo('dlgAddNode.addfromclipboardlistinput');
  this.AddFromClipboardNodeTypeList.on('select', this.atSelectFromClipboardNodeType, this);
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.initBehaviours = function () {
  var extOption;
  extOption = Ext.get("dlgAddNode.addblank");
  extOption.on("click", this.atAddBlankClick, this);
  extOption = Ext.get("dlgAddNode.addfromfile");
  extOption.on("click", this.atAddFromFileClick, this);
  extOption = Ext.get("dlgAddNode.addfromclipboard");
  extOption.on("click", this.atAddFromClipboardClick, this);
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.show = function () {
  if (this.Wizard == null) return false;
  $("dlgAddNode.status").hide();

  this.Wizard.show();

  if (this.Target.From == ADD_NODE_FROM_FILE) {
    this.activateOptionAddFromFile();
    this.atNextStep();
  }
  else if (this.Target.From == ADD_NODE_FROM_CLIPBOARD) this.activateOptionAddFromClipboard();
  else this.activateOptionAddBlank();

};

//------------------------------------------------------------------
CGDialogAddNode.prototype.activateOptionAddBlank = function () {
  var extAddBlankList = Ext.get("dlgAddNode.addblanklist");
  var extAddFromClipboardList = Ext.get("dlgAddNode.addfromclipboardlist");
  extAddBlankList.addClass("visible");
  extAddFromClipboardList.removeClass("visible");
  this.Wizard.disableNextButton();
  if (this.AddBlankNodeTypeList.getValue() == "") this.Wizard.disableFinishButton();
  else this.Wizard.enableFinishButton();
  $("dlgAddNode.addblank").checked = true;
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.activateOptionAddFromFile = function () {
  var extAddBlankList = Ext.get("dlgAddNode.addblanklist");
  var extAddFromClipboardList = Ext.get("dlgAddNode.addfromclipboardlist");
  extAddBlankList.removeClass("visible");
  extAddFromClipboardList.removeClass("visible");
  this.Wizard.enableNextButton();
  this.Wizard.disableFinishButton();
  $("dlgAddNode.addfromfile").checked = true;
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.activateOptionAddFromClipboard = function () {
  var extAddBlankList = Ext.get("dlgAddNode.addblanklist");
  var extAddFromClipboardList = Ext.get("dlgAddNode.addfromclipboardlist");
  extAddBlankList.removeClass("visible");
  extAddFromClipboardList.addClass("visible");
  if (this.AddFromClipboardNodeTypeList.getValue() == "") this.Wizard.disableNextButton();
  else this.Wizard.enableNextButton();
  this.Wizard.disableFinishButton();
  $("dlgAddNode.addfromclipboard").checked = true;
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.hide = function () {
  if (this.Wizard == null) return false;
  this.Wizard.hide();
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.destroy = function () {
  this.Wizard.destroy();
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.refreshNodeTypeLists = function () {
  var RecordDefinition = new Ext.data.Record.create({id: 'code'}, {name: 'caption'});

  this.AddBlankNodeTypeList.store.removeAll();
  this.AddFromClipboardNodeTypeList.store.removeAll();
  for (var iPos = 0; iPos < this.Target.NodeTypes.length; iPos++) {
    var NodeType = this.Target.NodeTypes[iPos];
    var record = new RecordDefinition({code: NodeType.Code, caption: NodeType.Caption});
    this.AddBlankNodeTypeList.store.add(record);
    this.AddFromClipboardNodeTypeList.store.add(record);
  }
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.refresh = function () {
  if (!this.Wizard) return false;

  this.refreshNodeTypeLists();

  $("dlgAddNode.optionpreserveoriginal").checked = true;
  $("dlgAddNode.description").value = Lang.DialogAddNode.AddedAt + " " + getFormattedDate(new Date(), Context.Config.Language, true);
  $("dlgAddNode.loadingnodetype").style.display = "none";
  $("dlgAddNode.nodetype").style.display = "none";

  if (this.Target.Definition != null) {
    var sNodeTypeFields = EMPTY;
    for (var iPos = 0; iPos < this.Target.Definition.aFields.length; iPos++) {
      var Field = this.Target.Definition.aFields[iPos];
      sNodeTypeFields += "<li>" + Field.title + "</li>";
    }
    $("dlgAddNode.nodetype").style.display = "block";
    $("dlgAddNode.nodetypefields").innerHTML = sNodeTypeFields;

    this.DataImporter.setDefinition(this.Target.Definition);
    this.DataImporter.refresh();
  }

  this.Wizard.enableNextButton();
};

//------------------------------------------------------------------
CGDialogAddNode.prototype.check = function () {
  var sMessage = EMPTY;
  var DOMAddBlank = $("dlgAddNode.addblank");
  var DOMAddFromFile = $("dlgAddNode.addfromfile");

  DOMAddBlank.removeClassName("error");
  if (DOMAddBlank.checked) {
    if (this.NodeType.Code == null) sMessage += "<li>" + Lang.DialogAddNode.Error.TypeRequired + "</li>";
    DOMAddBlank.addClassName("error");
  }

  DOMAddFromFile.removeClassName("error");
  if (DOMAddFromFile.checked) {
    if (this.input.GetCount() <= 0) sMessage += "<li>" + Lang.DialogAddNode.Error.FileRequired + "</li>";
    DOMAddFromFile.addClassName("error");
  }

  if (sMessage != EMPTY) {
    this.showStatus("<ul>" + sMessage + "</ul>");
  }

  return (sMessage == EMPTY);
};

//==================================================================
CGDialogAddNode.prototype.atAccept = function () {
  if (!this.check()) return;

  this.FileForm = this.input.eForm;

  if ($("dlgAddNode.addblank").checked) this.From = ADD_NODE_BLANK;
  else if ($("dlgAddNode.addfromfile").checked) this.From = ADD_NODE_FROM_FILE;
  else this.From = ADD_NODE_FROM_CLIPBOARD;

  if ($("dlgAddNode.optionpreserveoriginal").checked) this.Option = ADD_NODE_OPTION_PRESERVE_ORIGINAL;
  else if ($("dlgAddNode.optionreplace").checked) this.Option = ADD_NODE_OPTION_REPLACE;
  else this.Option = ADD_NODE_OPTION_DUPLICATE;

  this.Description = $("dlgAddNode.description").value;
  this.Result = this.DataImporter.getResult();

  this.hide();

  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogAddNode.prototype.atCancel = function () {
  this.hide();
  this.destroy();
  this.input = null;
  if (this.onCancel) this.onCancel();
};

//==================================================================
CGDialogAddNode.prototype.atPreviousStep = function () {
  var sBranch = $("dlgAddNode.addfromclipboard").checked ? $("dlgAddNode.addfromclipboard").value : $("dlgAddNode.addfromfile").value;

  this.Wizard.previousStep(sBranch);

  if ($("dlgAddNode.addfromclipboard").checked) {
    if (this.Wizard.getActiveStep().id == 2) this.DataImporter.renderPanelClipboard();
    else if (this.Wizard.getActiveStep().id == 3) this.DataImporter.renderPanelLayout();
  }

};

//==================================================================
CGDialogAddNode.prototype.atNextStep = function () {
  var sBranch = $("dlgAddNode.addfromclipboard").checked ? $("dlgAddNode.addfromclipboard").value : $("dlgAddNode.addfromfile").value;

  this.Wizard.nextStep(sBranch);

  if ($("dlgAddNode.addfromclipboard").checked) {
    if (this.Wizard.getActiveStep().id == 2) this.DataImporter.renderPanelClipboard();
    else if (this.Wizard.getActiveStep().id == 3) this.DataImporter.renderPanelLayout();
  }

};

//==================================================================
CGDialogAddNode.prototype.atSelectAddBlankNodeType = function (ComboBox, Record, Index) {
  this.NodeType.Code = Record.data.code;
  this.NodeType.Caption = Record.data.caption;
  this.Wizard.enableFinishButton();
};

//==================================================================
CGDialogAddNode.prototype.atSelectFromClipboardNodeType = function (ComboBox, Record, Index) {
  var Action = new CGActionAddNode();

  $("dlgAddNode.loadingnodetype").style.display = "block";
  $("dlgAddNode.nodetype").style.display = "none";
  this.NodeType.Code = Record.data.code;
  this.NodeType.Caption = Record.data.caption;
  this.Wizard.disableNextButton();

  Action.refresh(this);
};

//==================================================================
CGDialogAddNode.prototype.atAddBlankClick = function () {
  this.activateOptionAddBlank();
};

//==================================================================
CGDialogAddNode.prototype.atAddFromFileClick = function () {
  this.activateOptionAddFromFile();
};

//==================================================================
CGDialogAddNode.prototype.atAddFromClipboardClick = function () {
  this.activateOptionAddFromClipboard();
};