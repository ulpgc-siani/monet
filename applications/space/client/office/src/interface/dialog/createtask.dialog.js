CGDialogCreateTask = function () {
  this.base = CGDialog;
  this.base("dlgCreateTask");
  this.TaskType = new Object();
};

//------------------------------------------------------------------
CGDialogCreateTask.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogCreateTask.prototype.init = function () {

  var html = AppTemplate.DialogCreateTask;
  html = translate(html, Lang.DialogCreateTask);

  this.layer = new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.initDialog();
  this.initTaskTypeList();
  this.initRequiredFields();
};

//------------------------------------------------------------------
CGDialogCreateTask.prototype.initTaskTypeList = function () {
  var Store = new Ext.data.SimpleStore({fields: ['code', 'caption']});

  this.TaskTypeList = new Ext.form.ComboBox({
    store: Store,
    displayField: 'caption',
    emptyText: Lang.DialogCreateTask.SelectOne,
    typeAhead: true,
    triggerAction: 'all',
    hideTrigger: false,
    mode: 'local',
    resizable: true,
    width: 460,
    listWidth: 460,
    tpl: new Ext.Template("<div class='tasktypelistitem'><div class='head'>{caption}</div><div class='body'>{description}</div></div>")
  });

  this.TaskTypeList.applyTo('dlgCreateTask.tasklistinput');
  this.TaskTypeList.on('select', this.atSelectTaskType, this);
};

//------------------------------------------------------------------
CGDialogCreateTask.prototype.refreshTaskTypeLists = function () {
  var RecordDefinition = new Ext.data.Record.create({id: 'code'}, {name: 'caption'});

  this.TaskTypeList.store.removeAll();
  for (var iPos = 0; iPos < this.Target.TaskTypes.length; iPos++) {
    var TaskType = this.Target.TaskTypes[iPos];
    var record = new RecordDefinition({code: TaskType.Code, caption: TaskType.Caption, description: TaskType.Description});
    this.TaskTypeList.store.add(record);
  }
};

//------------------------------------------------------------------
CGDialogCreateTask.prototype.refresh = function () {
  this.refreshTaskTypeLists();
  $("dlgCreateTask.title").value = Context.Config.DefaultLabel;
};

//------------------------------------------------------------------
CGDialogCreateTask.prototype.check = function () {
  var sMessage = EMPTY;
  var DOMTitle = $("dlgCreateTask.title");

  this.TaskTypeList.el.dom.removeClassName("error");
  if (this.TaskType.Code == null) {
    sMessage += "<li>" + Lang.DialogCreateTask.Error.TypeRequired + "</li>";
    this.TaskTypeList.el.dom.addClassName("error");
  }

  DOMTitle.removeClassName("error");
  if (DOMTitle.value == "") {
    sMessage += "<li>" + Lang.DialogCreateTask.Error.TitleRequired + "</li>";
    DOMTitle.addClassName("error");
  }

  if (sMessage != EMPTY) {
    this.showStatus("<ul>" + sMessage + "</ul>");
  }

  return (sMessage == EMPTY);
};

//==================================================================
CGDialogCreateTask.prototype.atAccept = function () {
  if (!this.check()) return;

  this.Title = $("dlgCreateTask.title").value;
  this.hide();

  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogCreateTask.prototype.atSelectTaskType = function (ComboBox, Record, Index) {
  this.TaskType.Code = Record.data.code;
  this.TaskType.Caption = Record.data.caption;
};