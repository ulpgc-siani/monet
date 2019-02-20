var TEMPLATE = "<div class='panel clipboard'><label>Pega aquí el texto que deseas importar:</label><textarea class='data' rows='32'></textarea></div><div class='panel options'><fieldset class='delimiters'><legend>Delimita el texto que se seleccionará en cada momento indicando cómo están separados los campos</legend><form><div><input name='delimiter' id='dparagraph' type='radio' class='command paragraph' checked><label for='dparagraph'>Cada campo es un párrafo</label></div><div><input name='delimiter' id='dtab' type='radio' class='command tab' ><label for='dtab'>Con tabulaciones</label></div><div><input name='delimiter' id='ddot' type='radio' class='command dot' ><label for='ddot'>Con puntos</label></div><div><input name='delimiter' id='dsemicolon' type='radio' class='command semicolon' ><label for='dsemicolon'>Con puntos y coma</label></div><div><input name='delimiter' id='dcomma' type='radio' class='command comma' ><label for='dcomma'>Con comas</label></div><div><input name='delimiter' id='dfree' type='radio' class='command free' ><span class='label'><label for='dfree'>Empiezan con </label><input type='text' class='text free startwith'><label for='dfree'> y terminan con </label><input type='text' class='text free endwith'></label></span></div><!--<div><input name='delimiter' id='dregexp' type='radio' class='command regexp' ><span class='label'><label for='dregexp'>Necesito una expresión regular para indicar los marcadores de inicio y fin. Expresión: <label for='dregexp'></label><input type='text' class='text regexp'></span></div>--></form></fieldset><fieldset class='definition fields'><legend>Indica el orden en que aparecen estos campos en el texto. Esto te facilitará cumplimentar los formularios</legend><ul class='list'></ul></fieldset><div class='toolbar'><div class='command close'></div></div></div><div class='panel layout'><div class='identation'><label>Texto:</label><div><textarea class='data' rows='10'></textarea><div class='pattern'>Patrón: <a class='command options'><span class='type paragraph'>Cada campo es un párrafo</span><span class='type tab'>Cada campo se delimita por una tabulación</span><span class='type dot'>Cada campo se delimita por un punto</span><span class='type semicolon'>Cada campo se delimita por un punto y coma</span><span class='type comma'>Cada campo se delimita por una coma</span><span class='type free'>Cada campo empieza por '<span class='startwith'></span>' y termina con '<span class='endwith'></span>'</span><span class='type regexp'>Cada campo se delimita por la expresión regular '<span class='regexp'></span>'</span> cambiar...</a></div></div><div class='toolbar'><div class='left'><div class='command addform'></div></div><div class='right'><div class='command search'></div><div class='fields'></div><div class='command addfieldsplit'></div></div></div></div><div class='tabs'></div></div>";
var TEMPLATE_FIELD = "<li class='definition field'><div class='code hidden'>#{code}</div>#{title}<a class='raise'>subir<a class='bury'>bajar</a></li>";
var TEMPLATE_TAB = "<div class='tab'><ul class='list'>#{fields}</ul></div>";
var TEMPLATE_TAB_FIELD = "<li class='field #{code} #{class}'><div class='code hidden'>#{code}</div><table><tr><td width='15'><div class='arrow'>&nbsp;</div></td><td width='30%'><p class='title'>#{title}</p></td><td width='6%'><a class='raise'><img class='trigger' src='#{ImagesPath}/s.gif' alt='subir' title='subir'/></a><a class='bury'><img class='trigger' src='#{ImagesPath}/s.gif' alt='bajar' title='bajar'/></a></td><td width='64%'><a class='command value showfieldeditor'>#{value}</a></td></tr></table></li>";
var TEMPLATE_TAB_FIELD_VALUE_LINK = "<a id='#{id}' class='command value showfieldeditor'>#{value}</a>";
var TEMPLATE_TAB_FIELD_VALUE_EDITOR = "<div id='#{id}' class='editor'><textarea class='value' type='text'>#{value}</textarea><a class='command setfieldvalue'>cambiar</a></div>";

LangDataImporter = {
  FieldEmpty: "[Haz click para cumplimentar]",
  FieldValueEmpty: "vacío",
  TabTitle: "Formulario #num",
  Search: "Buscar",
  Close: "Cerrar",
  AddForm: "A�adir formulario",
  AddField: "A�adir",
  AddFields: "continuar desde aqu�"
};

String.prototype.indexOfDelimiter = function (sDelimiter, iStartPos) {
  var iPos = iStartPos;

  while (iPos < this.length) {
    iEndPos = iPos + sDelimiter.length;
    if (iEndPos > this.length) iEndPos = this.length;
    if (this.substring(iPos, iEndPos) == sDelimiter) return iPos;
    iPos++;
  }

  return -1;
};

String.prototype.delimitedSubstring = function (sStartDelimiter, sEndDelimiter) {
};

String.prototype.indexOfParagraph = function (iStartPos) {
  return this.indexOfDelimiter("\n", iStartPos);
};

String.prototype.paragraph = function (iStartPos) {
  var iPos = this.indexOfParagraph(iStartPos);
  if (iPos == -1) return "";
  return this.substr(iStartPos, iPos - iStartPos + 1);
};

function CGClipboardBehaviour() {
};

CGClipboardBehaviour.prototype.init = function (extData) {

  extData.dom = extData.getEl().dom;

  extData.scroll = function (iPos) {
    var sValue = this.getValue();
    var iTextPercentage = (iPos * 100) / sValue.length;
    var iScrollPosition = (this.dom.scrollHeight * iTextPercentage) / 100;
    this.dom.scrollTop = iScrollPosition;
  };

  extData.select = function (iStartPos, iEndPos) {
    var iTextLength = this.getValue().length;

    if (iStartPos < 0) iStartPos = 0;
    if (iEndPos > iTextLength) iEndPos = iTextLength;

    this.focus();
    this.scroll(iStartPos - 250);
    this.selectText(iStartPos, iEndPos);
  };

  extData.getSelectedContent = function () {
    if (document.selection) {
      return document.selection.createRange().text;
    }
    var sContent = this.getContent();
    return sContent.substr(this.dom.selectionStart, this.dom.selectionEnd - this.dom.selectionStart);
  };

  extData.getContent = function () {
    return this.getValue();
  };

  extData.setContent = function (sContent) {
    return this.setValue(sContent);
  };

  extData.setCursorPosition = function (iPosition) {
    if (this.setSelectionRange) {
      this.dom.focus();
      this.dom.setSelectionRange(iPosition, iPosition);
    }
    else if (this.createTextRange) {
      var Range = this.createTextRange();
      Range.collapse(true);
      Range.moveStart('character', Position);
      Range.moveEnd('character', iPosition);
      Range.select();
    }
  };

  extData.getCursorPosition = function () {
    return this.dom.selectionStart;
  };

  extData.atDataClick = function (EventLaunched) {
    this.setCursorPosition(this.dom.selectionStart);
  };

  Event.observe(extData.dom, "click", extData.atDataClick.bind(extData));

};

CGLayoutTabSet = function () {
  this.extDisplay = null;
  this.extTabPanel = null;
  this.extCurrentField = null;
  this.Definition = new Array();
  this.aFields = new Array();
  this.sLastFieldValue = "";
  this.iForm = 1;
};

CGLayoutTabSet.prototype.init = function (extDisplay) {
  this.extDisplay = extDisplay;
  this.extTabPanel = new Ext.TabPanel(extDisplay, {resizeTabs: true, minTabWidth: 20, preferredTabWidth: 150});
};

CGLayoutTabSet.prototype.setDefinition = function (Definition) {
  this.Definition = Definition;
};

CGLayoutTabSet.prototype.setFields = function (aFields) {
  this.aFields = aFields;
};

CGLayoutTabSet.prototype.addTab = function (codeForm, bCloseable) {
  var TemplateTab = new Template(TEMPLATE_TAB);
  var TemplateTabField = new Template(TEMPLATE_TAB_FIELD);
  var extTabPanelItem;
  var sContent = "";
  var id = Ext.id();

  for (var iPos = 0; iPos < this.Definition.aFields.length; iPos++) {
    var Field = this.Definition.aFields[iPos];
    var sClass = ((iPos % 2) == 0) ? "even" : "odd";
    sContent += TemplateTabField.evaluate({'ImagesPath': Context.Config.ImagesPath, 'code': Field.code, 'title': Field.title, 'value': (Field.defaultValue && Field.defaultValue != "") ? Field.defaultValue : LangDataImporter.FieldEmpty, 'class': sClass});
  }

  sContent = TemplateTab.evaluate({'ImagesPath': Context.Config.ImagesPath, 'fields': sContent});

  extTabPanelItem = this.extTabPanel.addTab(id, LangDataImporter.TabTitle.replace("#num", this.iForm), sContent, bCloseable);
  extTabPanelItem.on("activate", CGLayoutTabSet.prototype.atTabActivate.bind(this));
  extTabPanelItem.on("beforeclose", CGLayoutTabSet.prototype.atTabClose.bind(this));
  extTabPanelItem.codeForm = codeForm;

  this.addBehaviours(id);
  this.extTabPanel.activate(id);
  this.iForm++;
};

CGLayoutTabSet.prototype.addBehaviours = function (idTab) {
  var extTab = Ext.get(idTab);
  var extList;

  extList = extTab.select(".raise");
  extList.each(function (extItem) {
    Event.observe(extItem.dom, 'click', CGLayoutTabSet.prototype.atFieldRaiseClick.bindAsEventListener(this, extItem.dom));
  }, this);

  extList = extTab.select(".bury");
  extList.each(function (extItem) {
    Event.observe(extItem.dom, 'click', CGLayoutTabSet.prototype.atFieldBuryClick.bindAsEventListener(this, extItem.dom));
  }, this);

  extList = extTab.select(".field");
  extList.each(function (extItem) {
    Event.observe(extItem.dom, 'click', CGLayoutTabSet.prototype.atSelectField.bindAsEventListener(this, extItem.dom));
  }, this);

  extList = extTab.select(".command.showfieldeditor");
  extList.each(function (extItem) {
    Event.observe(extItem.dom, 'click', CGLayoutTabSet.prototype.atShowFieldValueEditorClick.bindAsEventListener(this, extItem.dom));
  }, this);
};

CGLayoutTabSet.prototype.raiseField = function (DOMElement) {
  var extElement = Ext.get(DOMElement);
  var DOMList = extElement.up(".list").dom;
  if (DOMElement != DOMList.immediateDescendants().first()) {
    extPrevious = Ext.get(DOMElement.previous());
    DOMElement.remove();
    extPrevious.insertSibling(DOMElement, 'before');
  }
  new Effect.Highlight(DOMElement, {duration: 0.5});
};

CGLayoutTabSet.prototype.buryField = function (DOMElement) {
  var extElement = Ext.get(DOMElement);
  var DOMList = extElement.up(".list").dom;
  if (DOMElement != DOMList.immediateDescendants().last()) {
    extNext = Ext.get(DOMElement.next());
    DOMElement.remove();
    extNext.insertSibling(DOMElement, 'after');
  }
  new Effect.Highlight(DOMElement, {duration: 0.5});
};

CGLayoutTabSet.prototype.getDefinitionFieldsOrder = function () {
  var extTab = Ext.get(this.extTabPanel.getActiveTab().id);
  var extList = extTab.select(".list").first();

  var extCodeList = extList.select(".code");
  var aResult = new Array();

  extCodeList.each(function (extCode) {
    aResult.push(extCode.dom.innerHTML);
  }, this);

  return aResult;
};

CGLayoutTabSet.prototype.selectField = function (code) {
  var extTab = Ext.get(this.extTabPanel.getActiveTab().id);
  var extField = extTab.select(".field." + code).first();
  if (this.extCurrentField) this.extCurrentField.removeClass("selected");
  extField.addClass("selected");
  this.extCurrentField = extField;
  this.extCurrentField.code = code;
};

CGLayoutTabSet.prototype.setFieldValue = function (code, sValue) {
  var extTab = Ext.get(this.extTabPanel.getActiveTab().id);
  var extField = extTab.select(".field." + code).first();
  var extData = extField.select(".value").first();

  if (sValue == "") sValue = LangDataImporter.FieldValueEmpty;

  if (extData.dom.value) extData.dom.value = sValue;
  else extData.dom.innerHTML = sValue;
};

CGLayoutTabSet.prototype.showFieldValueLink = function (extLayer, sValue) {
  var TemplateTabFieldValueLink = new Template(TEMPLATE_TAB_FIELD_VALUE_LINK);
  var DOMLayer = $(extLayer.dom.id);

  if (sValue == "") sValue = LangDataImporter.FieldEmpty;

  DOMLayer.replace(TemplateTabFieldValueLink.evaluate({'ImagesPath': Context.Config.ImagesPath, id: extLayer.dom.id, value: sValue}));
  extLayer = Ext.get(extLayer.dom.id);
  extLayer.on("click", this.atShowFieldValueEditorClick, this);
};

CGLayoutTabSet.prototype.showFieldValueEditor = function (extLayer, sValue) {
  var TemplateTabFieldValueEditor = new Template(TEMPLATE_TAB_FIELD_VALUE_EDITOR);
  var extCommand;
  var DOMLayer = $(extLayer.dom.id);

  this.sLastFieldValue = sValue;

  DOMLayer.replace(TemplateTabFieldValueEditor.evaluate({'ImagesPath': Context.Config.ImagesPath, id: extLayer.dom.id, value: sValue}));
  extLayer = Ext.get(extLayer.dom.id);

  extCommand = extLayer.select(".value").first();
  extCommand.focus();
  extCommand.on("blur", this.atSetFieldValueClick, this);
  extCommand.on("keyup", this.atSetFieldValueInputPress, this);
};

CGLayoutTabSet.prototype.refresh = function () {
  var TemplateTab = new Template(TEMPLATE_TAB);
  var TemplateTabField = new Template(TEMPLATE_TAB_FIELD);
  var sContent = "";

  for (var iPos = 0; iPos < this.extTabPanel.getCount(); iPos++) {
    var extTab = this.extTabPanel.getTab(iPos);
    if (!this.aFields[extTab.codeForm]) continue;

    sContent = "";
    for (var jPos = 0; jPos < this.Definition.aFields.length; jPos++) {
      Field = this.Definition.aFields[jPos];
      sContent += TemplateTabField.evaluate({'ImagesPath': Context.Config.ImagesPath, 'code': Field.code, 'title': Field.title, 'value': (this.aFields[extTab.codeForm][Field.code] != "") ? this.aFields[extTab.codeForm][Field.code] : LangDataImporter.FieldEmpty});
    }

    extTab.setContent(TemplateTab.evaluate({'ImagesPath': Context.Config.ImagesPath, 'fields': sContent}));
    this.addBehaviours(extTab.id);

    if (extTab.isActive() && this.extCurrentField) this.selectField(this.extCurrentField.code);
  }

};

//---------------------------------------------------------------------------------------------------------------------

CGLayoutTabSet.prototype.atFieldRaiseClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  DOMElement = extTarget.up(".field").dom;
  this.raiseField(DOMElement);
  if (this.onDefinitionChange) this.onDefinitionChange();
};

CGLayoutTabSet.prototype.atFieldBuryClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  DOMElement = extTarget.up(".field").dom;
  this.buryField(DOMElement);
  if (this.onDefinitionChange) this.onDefinitionChange();
};

CGLayoutTabSet.prototype.atSelectField = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  var code = extTarget.down(".code").dom.innerHTML;
  this.selectField(code);
  if (this.onSelectField) this.onSelectField(code);
  if (oEvent) Event.stop(oEvent);
  return false;
};

CGLayoutTabSet.prototype.atShowFieldValueEditorClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  var sValue = DOMTarget.innerHTML;

  this.atSelectField(oEvent, extTarget.up(".field").dom);

  if (sValue == LangDataImporter.FieldEmpty) sValue = "";
  if (extTarget.dom.id == null) extTarget.dom.id = Ext.id();

  this.showFieldValueEditor(extTarget, sValue);
};

CGLayoutTabSet.prototype.atSetFieldValueInputPress = function (oEvent, DOMTarget) {
  if ((oEvent.keyCode != oEvent.ENTER) && (oEvent.keyCode != oEvent.ESC)) return;

  var extEditor = Ext.get(DOMTarget).up(".editor");

  if (((oEvent.keyCode == oEvent.ENTER) && (DOMTarget.type != "textarea")) || ((oEvent.keyCode == oEvent.ENTER) && (oEvent.ctrlKey))) {
    var code = extEditor.up(".field").down(".code").dom.innerHTML;
    this.setFieldValue(code, DOMTarget.value);
    this.showFieldValueLink(extEditor, DOMTarget.value);
    if (this.onFieldChange) this.onFieldChange(code, DOMTarget.value);
  }
  else if (oEvent.keyCode == oEvent.ESC) {
    this.showFieldValueLink(extEditor, this.sLastFieldValue);
  }

};

CGLayoutTabSet.prototype.atSetFieldValueClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  var extEditor = extTarget.up(".editor");
  var code = extTarget.up(".field").down(".code").dom.innerHTML;
  var sValue = extEditor.select('.value').first().dom.value;

  this.setFieldValue(code, sValue);
  this.showFieldValueLink(extEditor, sValue);
  if (this.onFieldChange) this.onFieldChange(code, sValue);
};

CGLayoutTabSet.prototype.atTabActivate = function () {
  if (this.onSelectForm) this.onSelectForm(this.extTabPanel.getActiveTab().codeForm);
  if (this.extCurrentField) this.selectField(this.extCurrentField.code);
};

CGLayoutTabSet.prototype.atTabClose = function (extTabPanelItem, extObject) {
  if (this.onDeleteForm) this.onDeleteForm(extTabPanelItem.codeForm);
  if (this.extTabPanel.getCount() <= 1) extObject.cancel = true;
};

CGPanel = function () {
  this.Definition = null;
  this.Delimiter = null;
  this.extDisplay = null;
};

CGPanel.prototype.init = function (extDisplay) {
};

CGPanel.prototype.setDefinition = function (Definition) {
  this.Definition = Definition;
};

CGPanel.prototype.setFields = function (aFields) {
  this.aFields = aFields;
};

CGPanel.prototype.setDelimiter = function (Delimiter) {
  this.Delimiter = Delimiter;
};

CGPanel.prototype.show = function () {
  this.extDisplay.dom.style.display = "block";
};

CGPanel.prototype.showDialog = function () {
  this.extDisplay.dom.style.display = "block";
  this.extDisplay.addClass("dialog");
};

CGPanel.prototype.hide = function () {
  this.extDisplay.dom.style.display = "none";
};

CGPanel.prototype.hideDialog = function () {
  this.extDisplay.dom.style.display = "none";
  this.extDisplay.removeClass("dialog");
};

CGPanel.prototype.isDialog = function () {
  return this.extDisplay.hasClass("dialog");
};

CGPanel.prototype.refresh = function (Delimiter) {
};

CGPanelClipboard = function () {
  this.base = CGPanel;
  this.base();
  this.extDisplay = null;
  this.Clipboard = null;
};

CGPanelClipboard.prototype = new CGPanel;

CGPanelClipboard.prototype.init = function (extDisplay) {
  this.extDisplay = extDisplay;
  this.initClipboard();
};

CGPanelClipboard.prototype.initClipboard = function () {
  var ClipboardBehaviour = new CGClipboardBehaviour();
  this.Clipboard = new Ext.form.TextArea();
  this.Clipboard.applyTo(this.extDisplay.select('.data').first());
  this.Clipboard.on("change", this.atDataChange, this);
  ClipboardBehaviour.init(this.Clipboard);
};

//---------------------------------------------------------------------------------------------------------------------

CGPanelClipboard.prototype.atDataChange = function () {
  if (this.onDataChange) this.onDataChange();
};

CGPanelLayout = function () {
  this.base = CGPanel;
  this.base();
  this.extDisplay = null;
  this.Clipboard = null;
  this.onShowOptions = null;
  this.onSearch = null;
  this.onFieldChange = null;
};

CGPanelLayout.prototype = new CGPanel;

CGPanelLayout.prototype.init = function (extDisplay) {
  this.extDisplay = extDisplay;
  this.initData();
  this.initPattern();
  this.initToolbar();
  this.initTabs();
};

CGPanelLayout.prototype.initData = function () {
  var ClipboardBehaviour = new CGClipboardBehaviour();
  var extData = this.extDisplay.select('.data').first();
  this.Clipboard = new Ext.form.TextArea();
  this.Clipboard.applyTo(extData);
  extData.on("click", this.atDataClick, this);
  extData.on("select", this.atDataSelect, this);
  ClipboardBehaviour.init(this.Clipboard);
};

CGPanelLayout.prototype.initPattern = function () {
  var extPattern = this.extDisplay.select('.pattern').first();
  var extShowOptions = extPattern.select('.command.options').first();
  extShowOptions.on("click", this.atShowOptionsClick, this);
};

CGPanelLayout.prototype.initToolbar = function () {
  var extToolbar = this.extDisplay.select('.toolbar').first();
  var extSearch = extToolbar.select('.search').first();
  var extAddForm = this.extDisplay.select('.addform').first();
  var extAddField = extToolbar.select('.addfieldsplit').first();

  extSearch = new Ext.Button(extSearch.dom, {text: LangDataImporter.Search});
  extAddForm = new Ext.Button(extAddForm.dom, {text: LangDataImporter.AddForm});
  extAddField = new Ext.SplitButton(extAddField.dom,
      {cls: "input", text: LangDataImporter.AddField, menu: {items: [new Ext.menu.Item({text: LangDataImporter.AddFields, handler: CGPanelLayout.prototype.atAddFieldsClick.bind(this)})]}}
  );

  extSearch.on("click", this.atSearchClick, this);
  extAddForm.on("click", this.atAddFormClick, this);
  extAddField.on("click", this.atAddFieldClick, this);

  this.extSelectorFields = extToolbar.select('.fields').first();
  this.extSelectorFields.on("change", this.atSelectorFieldsChange, this);
  this.extSelectorFields.dom.innerHTML = "";
};

CGPanelLayout.prototype.initTabs = function () {
  this.TabSet = new CGLayoutTabSet();
  this.TabSet.setDefinition(this.Definition);
  this.TabSet.init(this.extDisplay.select(".tabs").first());
  this.TabSet.onDefinitionChange = CGPanelLayout.prototype.atTabSetDefinitionChange.bind(this);
  this.TabSet.onSelectForm = CGPanelLayout.prototype.atTabSetSelectForm.bind(this);
  this.TabSet.onSelectField = CGPanelLayout.prototype.atTabSetSelectField.bind(this);
  this.TabSet.onDeleteForm = CGPanelLayout.prototype.atTabSetDeleteForm.bind(this);
  this.TabSet.onFieldChange = CGPanelLayout.prototype.atTabSetFieldChange.bind(this);
};

CGPanelLayout.prototype.setDefinition = function (Definition) {
  this.Definition = Definition;
  if (this.TabSet) this.TabSet.setDefinition(Definition);
};

CGPanelLayout.prototype.setFields = function (aFields) {
  this.aFields = aFields;
  if (this.TabSet) this.TabSet.setFields(aFields);
};

CGPanelLayout.prototype.getDefinitionFieldsOrder = function () {
  return this.TabSet.getDefinitionFieldsOrder();
};

CGPanelLayout.prototype.addTab = function (codeForm) {
  var extSelector = this.extSelectorFields.down("select");
  if (extSelector.dom.options.length <= 0) return;
  var code = extSelector.dom.options[0].value;
  this.TabSet.addTab(codeForm, true);
  this.TabSet.selectField(code);
};

CGPanelLayout.prototype.setFieldValue = function (code, sValue) {
  this.TabSet.setFieldValue(code, sValue);
};

CGPanelLayout.prototype.refresh = function () {
  var extSelector = this.extSelectorFields.down("select");
  var codeSelected = null;
  var extPattern = this.extDisplay.select(".pattern").first();
  var extDelimiterType = extPattern.select('.type.' + this.Delimiter.type).first();
  var sFields;

  if ((extSelector != null) && (extSelector.dom.selectedIndex > 0)) codeSelected = extSelector.dom.options[extSelector.dom.selectedIndex].value;

  sFields = "<select name='pltb_fields'>";
  for (var iPos = 0; iPos < this.Definition.aFields.length; iPos++) {
    var Field = this.Definition.aFields[iPos];
    var sSelected = ((codeSelected != null) && (Field.code == codeSelected)) ? " selected" : "";
    sFields += "<option value='" + Field.code + "'" + sSelected + ">" + Field.title + "</option>";
  }
  sFields += "</select>";
  this.extSelectorFields.dom.innerHTML = sFields;

  if (this.Delimiter.type == "free") {
    extDelimiterType.select(".startwith").first().dom.innerHTML = this.Delimiter.start;
    extDelimiterType.select(".endwith").first().dom.innerHTML = this.Delimiter.end;
  }
  else if (this.Delimiter.type == "regexp") {
    extDelimiterType.select(".regexp").first().dom.innerHTML = this.Delimiter.start;
  }

  extPattern.dom.className = "pattern";
  extPattern.addClass(this.Delimiter.type);

  this.TabSet.refresh();
};

//---------------------------------------------------------------------------------------------------------------------

CGPanelLayout.prototype.atDataClick = function (EventLaunched, DOMTarget) {
  if (this.onClipboardClick) this.onClipboardClick();
};

CGPanelLayout.prototype.atDataSelect = function (EventLaunched, DOMTarget) {
  if (this.onClipboardSelect) this.onClipboardSelect();
};

CGPanelLayout.prototype.atShowOptionsClick = function (EventLaunched) {
  if (this.onShowOptions) this.onShowOptions();
  if (EventLaunched) Event.stop(EventLaunched);
  return false;
};

CGPanelLayout.prototype.atSearchClick = function (EventLaunched) {
  if (this.onSearch) this.onSearch();
  if (EventLaunched) Event.stop(EventLaunched);
  return false;
};

CGPanelLayout.prototype.atSelectorFieldsChange = function (EventLaunched, DOMElement) {
  this.TabSet.selectField(DOMElement.options[DOMElement.selectedIndex].value);
};

CGPanelLayout.prototype.atAddFormClick = function (EventLaunched) {
  if (this.onAddForm) this.onAddForm();
};

CGPanelLayout.prototype.atAddFieldClick = function (EventLaunched) {
  var extSelector = this.extSelectorFields.down("select");
  var code = extSelector.dom.options[extSelector.dom.selectedIndex].value;
  var sValue = this.Clipboard.getSelectedContent();
  var index = extSelector.dom.selectedIndex + 1;

  if (this.onFieldChange) this.onFieldChange(code, sValue);

  this.atSearchClick(EventLaunched);

  if (index >= extSelector.dom.options.length) {
    index = 0;
    if (this.onAddForm) this.onAddForm();
  }
  else this.TabSet.selectField(code);

  extSelector.dom.selectedIndex = index;

  if (EventLaunched) Event.stop(EventLaunched);

  return false;
};

CGPanelLayout.prototype.atAddFieldsClick = function (EventLaunched) {
  if (this.onGenerate) this.onGenerate();
};

CGPanelLayout.prototype.atTabSetDefinitionChange = function () {
  if (this.onDefinitionChange) this.onDefinitionChange();
};

CGPanelLayout.prototype.atTabSetSelectForm = function (code) {
  if (this.onSelectForm) this.onSelectForm(code);
};

CGPanelLayout.prototype.atTabSetSelectField = function (code) {
  var extSelector = this.extSelectorFields.down("select");
  for (var iPos = 0; iPos < extSelector.dom.options.length; iPos++) {
    var Option = extSelector.dom.options[iPos];
    if (Option.value == code) {
      extSelector.dom.selectedIndex = iPos;
      return;
    }
  }
};

CGPanelLayout.prototype.atTabSetDeleteForm = function (code) {
  if (this.onDeleteForm) this.onDeleteForm(code);
};

CGPanelLayout.prototype.atTabSetFieldChange = function (codeField, sValue) {
  if (this.onFieldChange) this.onFieldChange(codeField, sValue);
};

CGPanelOptions = function () {
  this.base = CGPanel;
  this.base();
  this.extDisplay = null;
  this.extList = null;
  this.onSetDelimiter = null;
};

CGPanelOptions.prototype = new CGPanel;

CGPanelOptions.prototype.init = function (extDisplay) {
  this.extDisplay = extDisplay;
  this.initDelimiters();
  this.initFields();
  this.initToolbar();
};

CGPanelOptions.prototype.initDelimiters = function () {
  var extDelimiters = this.extDisplay.select(".delimiters").first();
  var extCommandList = extDelimiters.select(".command");
  var extInputList = extDelimiters.select("input.text");

  extCommandList.each(function (extCommand) {
    var DOMCommand = extCommand.dom;
    Event.observe(DOMCommand, 'click', this.atSetDelimiterClick.bindAsEventListener(this, DOMCommand));
  }, this);

  extInputList.each(function (extInput) {
    var DOMInput = extInput.dom;
    Event.observe(DOMInput, 'click', this.atInputTextClick.bindAsEventListener(this, DOMInput));
    Event.observe(DOMInput, 'change', this.atInputChangeClick.bindAsEventListener(this, DOMInput));
  }, this);
};

CGPanelOptions.prototype.initFields = function () {
  this.extList = this.extDisplay.select(".definition.fields .list").first();
  this.extList.dom.innerHTML = "";
};

CGPanelOptions.prototype.initToolbar = function () {
  var extToolbar = this.extDisplay.select('.toolbar').first();
  var extClose = extToolbar.select('.command.close').first();
  extClose = new Ext.Button(extClose.dom, {text: LangDataImporter.Close});
  extClose.on("click", this.atCloseClick, this);
};

CGPanelOptions.prototype.raiseElement = function (DOMElement) {
  if (DOMElement != this.extList.dom.immediateDescendants().first()) {
    extPrevious = Ext.get(DOMElement.previous());
    DOMElement.remove();
    extPrevious.insertSibling(DOMElement, 'before');
  }
  new Effect.Highlight(DOMElement, {duration: 0.5});
};

CGPanelOptions.prototype.buryElement = function (DOMElement) {
  if (DOMElement != this.extList.dom.immediateDescendants().last()) {
    extNext = Ext.get(DOMElement.next());
    DOMElement.remove();
    extNext.insertSibling(DOMElement, 'after');
  }
  new Effect.Highlight(DOMElement, {duration: 0.5});
};

CGPanelOptions.prototype.getDefinitionFieldsOrder = function () {
  var extCodeList = this.extList.select(".definition.field .code");
  var aResult = new Array();

  extCodeList.each(function (extCode) {
    aResult.push(extCode.dom.innerHTML);
  }, this);

  return aResult;
};

CGPanelOptions.prototype.refresh = function () {
  var TemplateField = new Template(TEMPLATE_FIELD);
  var extDelimiter = this.extDisplay.select(".delimiters ." + this.Delimiter.type).first();

  this.extList.dom.innerHTML = "";
  for (var iPos = 0; iPos < this.Definition.aFields.length; iPos++) {
    Field = this.Definition.aFields[iPos];
    sContent = TemplateField.evaluate({'ImagesPath': Context.Config.ImagesPath, 'code': Field.code, 'title': Field.title});

    new Insertion.Bottom(this.extList.dom, sContent);
    extElement = Ext.get(this.extList.dom.immediateDescendants().last());

    var DOMRaiseOption = extElement.down(".raise").dom;
    Event.observe(DOMRaiseOption, 'click', this.atFieldRaiseClick.bindAsEventListener(this, DOMRaiseOption));

    var DOMBuryOption = extElement.down(".bury").dom;
    Event.observe(DOMBuryOption, 'click', this.atFieldBuryClick.bindAsEventListener(this, DOMBuryOption));
  }

  if (extDelimiter != null) extDelimiter.dom.checked = true;

};

//---------------------------------------------------------------------------------------------------------------------

CGPanelOptions.prototype.atSetDelimiterClick = function (EventLaunched, DOMCommand) {
  var extCommand = Ext.get(DOMCommand);
  var sStart = null, sEnd = null;
  var Delimiter = new Object();

  if (extCommand.hasClass("paragraph")) Delimiter = {type: "paragraph", start: null, end: "\n"};
  else if (extCommand.hasClass("tab")) Delimiter = {type: "tab", start: null, end: "\t"};
  else if (extCommand.hasClass("dot")) Delimiter = {type: "dot", start: null, end: "."};
  else if (extCommand.hasClass("semicolon")) Delimiter = {type: "semicolon", start: null, end: ";"};
  else if (extCommand.hasClass("comma")) Delimiter = {type: "comma", start: null, end: ","};
  else if (extCommand.hasClass("free")) {
    Delimiter.type = "free";
    Delimiter.start = this.extDisplay.select(".text.free.startwith").first().dom.value;
    Delimiter.end = this.extDisplay.select(".text.free.endwith").first().dom.value;
  }
  else if (extCommand.hasClass("regexp")) {
    Delimiter.type = "regexp";
    Delimiter.start = this.extDisplay.select(".text.regexp").first().dom.value;
  }

  if (this.onSetDelimiter) this.onSetDelimiter(Delimiter);
};

CGPanelOptions.prototype.atInputTextClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  var sClass = "";

  if (extTarget.hasClass("free")) sClass = "free";
  else if (extTarget.hasClass("regexp")) sClass = "regexp";

  if (sClass != "") {
    var extRadio = this.extDisplay.select(".command." + sClass).first();
    extRadio.dom.checked = true;
  }

  this.atInputChangeClick(oEvent, DOMTarget);
};

CGPanelOptions.prototype.atInputChangeClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  var sStart = null, sEnd = null;
  var Delimiter = new Object();

  if (extTarget.hasClass("regexp")) {
    Delimiter.type = "regexp";
    Delimiter.start = this.extDisplay.select(".text.regexp").first().dom.value;
  }
  else if (extTarget.hasClass("free")) {
    Delimiter.type = "free";
    Delimiter.start = this.extDisplay.select(".text.free.startwith").first().dom.value;
    Delimiter.end = this.extDisplay.select(".text.free.endwith").first().dom.value;
  }

  if (this.onSetDelimiter) this.onSetDelimiter(Delimiter);
};

CGPanelOptions.prototype.atFieldRaiseClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  DOMElement = extTarget.up(".definition.field").dom;
  this.raiseElement(DOMElement);
  if (this.onDefinitionChange) this.onDefinitionChange();
};

CGPanelOptions.prototype.atFieldBuryClick = function (oEvent, DOMTarget) {
  var extTarget = Ext.get(DOMTarget);
  DOMElement = extTarget.up(".definition.field").dom;
  this.buryElement(DOMElement);
  if (this.onDefinitionChange) this.onDefinitionChange();
};

CGPanelOptions.prototype.atCloseClick = function () {
  if (this.onClose) this.onClose();
};

CGDataImporter = function () {
  this.extDisplay = null;
  this.Definition = null;
  this.Result = new Array();
  this.aPanels = new Array();
  this.aCodeForms = new Array();
  this.iPosition = 0;
  this.indexCurrentForm = 0;
  this.indexCurrentField = 0;
  this.Delimiter = {type: 'paragraph', start: null, end: "\n"};
};

CGDataImporter.prototype.init = function (sDisplay) {
  this.extDisplay = Ext.get(sDisplay);
  this.extDisplay.dom.innerHTML = TEMPLATE;
  this.initPanels();
};

CGDataImporter.prototype.initPanels = function () {
  this.aPanels["clipboard"] = new CGPanelClipboard();
  this.aPanels["clipboard"].init(this.extDisplay.down(".panel.clipboard"));
  this.aPanels["clipboard"].onDataChange = CGDataImporter.prototype.updateData.bind(this);

  this.aPanels["options"] = new CGPanelOptions();
  this.aPanels["options"].init(this.extDisplay.down(".panel.options"));
  this.aPanels["options"].setDelimiter(this.Delimiter);
  this.aPanels["options"].onSetDelimiter = CGDataImporter.prototype.setDelimiter.bind(this);
  this.aPanels["options"].onClose = CGDataImporter.prototype.closePanelOptions.bind(this);
  this.aPanels["options"].onDefinitionChange = CGDataImporter.prototype.updateDefinition.bind(this, "options");

  this.aPanels["layout"] = new CGPanelLayout();
  this.aPanels["layout"].init(this.extDisplay.down(".panel.layout"));
  this.aPanels["layout"].setDelimiter(this.Delimiter);
  this.aPanels["layout"].onShowOptions = CGDataImporter.prototype.renderPanelOptions.bind(this, true);
  this.aPanels["layout"].onSearch = CGDataImporter.prototype.search.bind(this);
  this.aPanels["layout"].onDefinitionChange = CGDataImporter.prototype.updateDefinition.bind(this, "layout");
  this.aPanels["layout"].onGenerate = CGDataImporter.prototype.generate.bind(this);
  this.aPanels["layout"].onSelectForm = CGDataImporter.prototype.selectForm.bind(this);
  this.aPanels["layout"].onAddForm = CGDataImporter.prototype.addForm.bind(this);
  this.aPanels["layout"].onDeleteForm = CGDataImporter.prototype.deleteForm.bind(this);
  this.aPanels["layout"].onFieldChange = CGDataImporter.prototype.setFieldValue.bind(this);
  this.aPanels["layout"].onClipboardClick = CGDataImporter.prototype.atClipboardClick.bind(this);
  this.aPanels["layout"].onClipboardSelect = CGDataImporter.prototype.atClipboardSelect.bind(this);
};

CGDataImporter.prototype.setDefinition = function (Definition) {
  this.Definition = Definition;

  this.Definition.aFieldsCodes = new Array();
  for (var iPos = 0; iPos < this.Definition.aFields.length; iPos++) {
    var Field = this.Definition.aFields[iPos];
    this.Definition.aFieldsCodes[Field.code] = iPos;
  }

  this.aPanels["options"].setDefinition(this.Definition);
  this.aPanels["layout"].setDefinition(this.Definition);
};

CGDataImporter.prototype.setDelimiter = function (Delimiter) {
  this.Delimiter = Delimiter;
  this.aPanels["layout"].setDelimiter(Delimiter);
  this.aPanels["layout"].refresh();
};

CGDataImporter.prototype.setPosition = function (iPosition) {
  this.iPosition = iPosition;
};

CGDataImporter.prototype.updateData = function () {
  var sContent = this.aPanels["clipboard"].Clipboard.getContent();
  this.aPanels["layout"].Clipboard.setContent(sContent);
  this.aPanels["layout"].refresh();
};

CGDataImporter.prototype.updateDefinition = function (sEventPanel) {
  var aFieldsOrder = this.aPanels[sEventPanel].getDefinitionFieldsOrder();
  var aResult = new Array();

  for (var iPos = 0; iPos < aFieldsOrder.length; iPos++) {
    var iCurrentPos = this.Definition.aFieldsCodes[aFieldsOrder[iPos]];
    var Field = this.Definition.aFields[iCurrentPos];
    if (Field == null) continue;
    aResult.push(Field);
  }

  this.Definition.aFields = aResult;
  this.setDefinition(this.Definition);

  if (sEventPanel != "options") this.aPanels["options"].refresh();
  this.aPanels["layout"].refresh();
};

CGDataImporter.prototype.search = function () {
  var sContent = this.aPanels["layout"].Clipboard.getContent();
  var iStartPos, iEndPos;

  if ((this.iPosition == null) || (this.iPosition > sContent.length)) this.iPosition = 0;
  iStartPos = this.iPosition;

  if (this.Delimiter.start) {
    iStartPos = sContent.indexOfDelimiter(this.Delimiter.start, this.iPosition);
    if (iStartPos == -1) iStartPos = sContent.length;
    else iStartPos = iStartPos + this.Delimiter.start.length;
  }

  iEndPos = sContent.indexOfDelimiter(this.Delimiter.end, iStartPos);
  if (iEndPos == -1) {
    if (this.Delimiter.type != "free") iEndPos = sContent.length;
    else iEndPos = iStartPos;
  }

  this.aPanels["layout"].Clipboard.select(iStartPos, iEndPos);

  this.setPosition(iEndPos + this.Delimiter.end.length);
};

CGDataImporter.prototype.selectForm = function (iForm) {
  this.indexCurrentForm = iForm;
};

CGDataImporter.prototype.getResult = function () {
  return this.Result;
};

CGDataImporter.prototype.addForm = function () {
  codeForm = this.createEmptyForm();
  this.aPanels["layout"].addTab(codeForm);
  return codeForm;
};

CGDataImporter.prototype.deleteForm = function (code) {
  delete (this.Result[code]);
};

CGDataImporter.prototype.setFieldValue = function (codeField, sValue) {
  if (this.Result[this.indexCurrentForm] == null) return false;
  this.Result[this.indexCurrentForm][codeField] = sValue;
  this.aPanels["layout"].setFieldValue(codeField, sValue);
  this.indexCurrentField = this.Definition.aFieldsCodes[codeField];
};

CGDataImporter.prototype.nextField = function () {
  this.indexCurrentField++;
  if (this.Definition.aFields[this.indexCurrentField] == null) {
    this.indexCurrentForm = this.addForm();
    this.indexCurrentField = 0;
  }
  return this.Definition.aFields[this.indexCurrentField].code;
};

CGDataImporter.prototype.generate = function () {
  var sContent = this.aPanels["layout"].Clipboard.getContent();
  var codeField, sData;
  var indexForm, indexField;

  while (this.iPosition < sContent.length) {
    this.search();
    codeField = this.nextField();
    sData = this.aPanels["layout"].Clipboard.getSelectedContent();
    this.setFieldValue(codeField, sData);
  }
};

CGDataImporter.prototype.createEmptyForm = function () {
  var codeForm = this.indexCurrentForm = this.Result.length;
  this.Result[this.indexCurrentForm] = new Array();

  for (var iPos = 0; iPos < this.Definition.aFields.length; iPos++) {
    var Field = this.Definition.aFields[iPos];
    this.Result[this.indexCurrentForm][Field.code] = (Field.defaultValue) ? Field.defaultValue : "";
  }

  return codeForm;
};

CGDataImporter.prototype.hidePanels = function () {
  this.aPanels["clipboard"].hide();
  this.aPanels["options"].hide();
  this.aPanels["layout"].hide();
};

CGDataImporter.prototype.renderPanelClipboard = function () {
  this.hidePanels();
  this.aPanels["clipboard"].show();
};

CGDataImporter.prototype.renderPanelOptions = function (bDialog) {
  if (bDialog) {
    if (this.aPanels["options"].isDialog()) this.aPanels["options"].hideDialog();
    else this.aPanels["options"].showDialog();
  }
  else {
    this.hidePanels();
    this.aPanels["options"].show();
  }
};

CGDataImporter.prototype.renderPanelLayout = function () {
  this.hidePanels();
  this.aPanels["layout"].show();
  if (this.Result.length == 0) this.addForm();
};

CGDataImporter.prototype.closePanelOptions = function () {
  this.aPanels["options"].hide();
};

CGDataImporter.prototype.refresh = function (sDisplay) {
  this.aPanels["clipboard"].refresh();
  this.aPanels["options"].refresh();
  this.aPanels["layout"].refresh();
};

//---------------------------------------------------------------------------------------------------------------------

CGDataImporter.prototype.atClipboardClick = function (EventLaunched, iPosition) {
  this.setPosition(this.aPanels["layout"].Clipboard.getCursorPosition());
};

CGDataImporter.prototype.atClipboardSelect = function (EventLaunched, iPosition) {
  var iPosition = this.aPanels["layout"].Clipboard.getCursorPosition();
  iPosition += this.aPanels["layout"].Clipboard.getSelectedContent().length + 1;
  this.aPanels["layout"].Clipboard.setCursorPosition(iPosition);
  this.setPosition(iPosition);
};
