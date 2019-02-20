CGDialogGenerateReport = function () {
  this.base = CGDialog;
  this.base("dlgGenerateReport");
  this.aSelectedNodeTypes = new Array();
};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.init = function () {
  var html;

  html = AppTemplate.DialogGenerateReport;
  html = translate(html, Lang.DialogGenerateReport);

  this.layer = new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.initDialog();
  this.initNodeTypeListView();
  this.initNodeTypeSelector();
  this.initFilters();
};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.initNodeTypeListView = function () {
  var template = AppTemplate.DialogGenerateReportType;
  template = translate(template, Lang.DialogGenerateReportType);

  this.NodeTypeListView = new TListView();
  this.NodeTypeListView.init($("dlgGenerateReport.filtertypelist"), "");
  this.NodeTypeListView.setTemplate(template);
  this.NodeTypeListView.removeAllElements();
};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.initNodeTypeSelector = function () {
  var Store = new Ext.data.SimpleStore({fields: ['code', 'caption']});

  this.NodeTypeSelector = new Ext.form.ComboBox({
    store: Store,
    displayField: 'caption',
    emptyText: Lang.DialogGenerateReport.SelectOne,
    typeAhead: true,
    triggerAction: 'all',
    hideTrigger: false,
    mode: 'local',
    resizable: true,
    width: 500,
    listWidth: 500,
    tpl: new Ext.Template("<div>{caption}</div>")
  });

  this.NodeTypeSelector.applyTo('dlgGenerateReport.filtertypeselector');
  this.NodeTypeSelector.on('select', this.atSelectNodeTypeClick, this);
  this.NodeTypeSelector.on('focus', this.atNodeTypeFocus, this);
};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.initFilters = function () {

  this.extDateFrom = new Ext.form.DateField({
    format: 'd/m/Y',
    altFormats: 'd-m-y|d/m/y|d-m-Y|Y-m-d|Y/m/d|Y-m|Y/m|m-Y|m/Y|Y',
    minLength: 4
  });
  this.extDateFrom.applyTo($("dlgGenerateReport.filterdatesfrom"));
  this.extDateFrom.on('focus', this.atDateFocus, this);

  this.extDateTo = new Ext.form.DateField({
    format: 'd/m/Y',
    altFormats: 'd-m-y|d/m/y|d-m-Y|Y-m-d|Y/m/d|Y-m|Y/m|m-Y|m/Y|Y',
    minLength: 4
  });
  this.extDateTo.applyTo($("dlgGenerateReport.filterdatesto"));
  this.extDateTo.on('focus', this.atDateFocus, this);

};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.refreshOptions = function () {
  var CurrentNode = NodesCache.getCurrent();
  var extOption = Ext.get("dlgGenerateReport.optionselection").up(".option");
  var aSelectedNodes = State.getSelectedNodesReferences(CurrentNode.getId());

  $("dlgGenerateReport.optionselection").disabled = (!aSelectedNodes || (aSelectedNodes.size() <= 0)) ? true : false;
  $("dlgGenerateReport.optionselection").checked = (aSelectedNodes && (aSelectedNodes.size() > 0)) ? true : false;

  if (extOption) {
    if (!aSelectedNodes || (aSelectedNodes.size() <= 0)) extOption.addClass("disabled");
    else extOption.removeClass("disabled");
  }
};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.refreshNodeTypeSelector = function () {
  var RecordDefinition = new Ext.data.Record.create({id: 'code'}, {name: 'caption'});

  this.NodeTypeSelector.store.removeAll();
  for (var iPos = 0; iPos < this.Target.NodeTypes.length; iPos++) {
    var NodeType = this.Target.NodeTypes[iPos];
    var record = new RecordDefinition({code: NodeType.Code, caption: NodeType.Caption});
    this.NodeTypeSelector.store.add(record);
  }
};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.refresh = function () {
  if (!this.dialog) return false;
  this.refreshOptions();
  this.refreshNodeTypeSelector();
};

//------------------------------------------------------------------
CGDialogGenerateReport.prototype.addBehaviours = function (DOMElement) {
  var extElement = Ext.get(DOMElement);
  aLinks = extElement.select("a");

  aLinks.each(function (extLink) {
    Event.observe(extLink.dom, "click", CGDialogGenerateReport.prototype.atDeleteNodeTypeClick.bind(this, extLink.dom));
  }, this);
};

//==================================================================
CGDialogGenerateReport.prototype.atAccept = function () {
  var extDlgShareNode;
  var aInputs;

  if (!this.check()) return;

  if ($("dlgGenerateReport.optionall").checked) this.Option = OPTION_ALL;
  else this.Option = OPTION_SELECTION;

  this.Filters = new Array();

  if ($("dlgGenerateReport.filtertype").checked) {
    this.Filters[FILTER_NODE_TYPES] = this.aSelectedNodeTypes;
  }

  if ($("dlgGenerateReport.filterdates").checked) {
    this.Filters[FILTER_DATES] = new Object();
    this.Filters[FILTER_DATES].From = $("dlgGenerateReport.filterdatesfrom").value;
    this.Filters[FILTER_DATES].To = $("dlgGenerateReport.filterdatesto").value;
  }

  this.hide();

  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogGenerateReport.prototype.atNodeTypeFocus = function () {
  $("dlgGenerateReport.filtertype").checked = true;
};

//==================================================================
CGDialogGenerateReport.prototype.atSelectNodeTypeClick = function (ComboBox, Record, Index) {
  if (this.aSelectedNodeTypes[Record.data.code] != null) return;

  var DOMElement = this.NodeTypeListView.addElement({sTitle: Record.data.caption, Id: Record.data.code});
  this.addBehaviours(DOMElement);
  this.aSelectedNodeTypes[Record.data.code] = Record.data.code;

  return false;
};

//==================================================================
CGDialogGenerateReport.prototype.atDeleteNodeTypeClick = function (DOMItem, EventLaunched) {
  var extItem = Ext.get(DOMItem);
  var CommandInfo = new CGCommandInfo(DOMItem.href);
  var aParameters = CommandInfo.getParameters();

  if (DOMItem) {
    extLink = extItem.up("li");
    if (extLink) extLink.remove();
    delete this.aSelectedNodeTypes[aParameters[0]];
  }

  Event.stop(EventLaunched);
  return false;
};

//==================================================================
CGDialogGenerateReport.prototype.atDateFocus = function () {
  $("dlgGenerateReport.filterdates").checked = true;
};