CGViewNodeNotes = function () {
  this.base = CGView;
  this.base();
  this.Type = VIEW_NODE_TYPE_NODE;

  this.noteTemplate = new Ext.Template(translate(AppTemplate.ViewNodeNotesItem, Lang.ViewNodeNotes));
  this.noteTemplate.compile();
};

CGViewNodeNotes.prototype = new CGView;

CGViewNodeNotes.prototype.initBehaviours = function () {
  var extLayer = Ext.get(this.DOMLayer);
  var extDialog = extLayer.select(".dialog").first();
  var extAddButton = extDialog.select("a.addnote").first();
  Event.observe(extDialog.select(".name").first().dom, "keypress", CGViewNodeNotes.prototype.atDialogKeyPress.bind(this));
  Event.observe(extDialog.select(".value").first().dom, "keypress", CGViewNodeNotes.prototype.atDialogKeyPress.bind(this));
  Event.observe(extAddButton.dom, "click", CGViewNodeNotes.prototype.atAddNote.bind(this));
};

CGViewNodeNotes.prototype.init = function () {
  this.DOMLayer.innerHTML = translate(AppTemplate.ViewNodeNotes, Lang.ViewNodeNotes);
  this.initBehaviours();
};

CGViewNodeNotes.prototype.setTarget = function (target) {
  this.nodeId = target.nodeId;
  this.notes = target.notes;
  this.options = target.options;
};

CGViewNodeNotes.prototype.addNote = function (note) {
  this.notes[note.name] = note.value;
  this.refresh();
  CommandListener.throwCommand("addnodenote(" + this.nodeId + "," + escape(utf8Encode(note.name)) + "," + escape(utf8Encode(note.value)) + ")");
};

CGViewNodeNotes.prototype.deleteNote = function (name) {
  delete(this.notes[name]);
  this.refresh();
  CommandListener.throwCommand("deletenodenote(" + this.nodeId + "," + escape(utf8Encode(name)) + ")");
};

CGViewNodeNotes.prototype.refreshNote = function (note) {
  var extLayer = Ext.get(this.DOMLayer);
  var extNotes = extLayer.select("ul.notes").first();
  var extNote = this.noteTemplate.append(extNotes, {name: note.name, value: note.value, readonly: this.options.Editable ? "" : "readonly"}, true);

  Event.observe(extNote.select(".name").first().dom, "change", CGViewNodeNotes.prototype.atSaveNote.bind(this, extNote, note));
  Event.observe(extNote.select(".value").first().dom, "change", CGViewNodeNotes.prototype.atSaveNote.bind(this, extNote, note));
  Event.observe(extNote.select(".delete").first().dom, "click", CGViewNodeNotes.prototype.atDeleteNote.bind(this, note.name));
};

CGViewNodeNotes.prototype.refresh = function () {
  var extLayer = Ext.get(this.DOMLayer);
  var extNotes = extLayer.select("ul.notes").first();
  var hasNotes = false;

  if (!this.options.Editable)
    extLayer.select(".node.notes").addClass("readonly");

  extNotes.removeClass("empty");
  extNotes.dom.innerHTML = "";
  for (var name in this.notes) {
    if (isFunction(this.notes[name])) continue;
    hasNotes = true;
    this.refreshNote({name: name, value: this.notes[name]});
  }

  if (!hasNotes) {
    extNotes.addClass("empty");
    extNotes.dom.innerHTML = Lang.ViewNodeNotes.Empty;
  }
};

// #############################################################################################################
CGViewNodeNotes.prototype.atAddNote = function () {
  var extLayer = Ext.get(this.DOMLayer);
  var extDialog = extLayer.select(".dialog").first();
  var extName = extDialog.select(".name").first();
  var extValue = extDialog.select(".value").first();
  var name = extName.dom.value;
  var value = extValue.dom.value;

  if (name == "" || value == "")
    return;

  this.addNote({name: name, value: value});

  extName.dom.value = "";
  extValue.dom.value = "";

  extName.dom.focus();
};

CGViewNodeNotes.prototype.atSaveNote = function (DOMNote, note) {
  var extNote = Ext.get(DOMNote);
  var extName = extNote.select(".name").first();
  var extValue = extNote.select(".value").first();
  var name = extName.dom.value;
  var value = extValue.dom.value;

  if (name == "" || value == "")
    return;

  this.deleteNote(note.name);
  this.addNote({name: name, value: value});
};

CGViewNodeNotes.prototype.atDeleteNote = function (name) {
  this.deleteNote(name);
};

CGViewNodeNotes.prototype.atDialogKeyPress = function (event) {
  if (event.keyCode == 13)
    this.atAddNote();
};