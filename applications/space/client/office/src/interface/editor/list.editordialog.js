CGEditorDialogList = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.aSelectedItems = new Array();
  this.extEmpty = null;
  this.extList = null;
  this.init();
};

CGEditorDialogList.prototype = new CGEditorDialog;

//private
CGEditorDialogList.prototype.init = function () {
  this.Items = new Array();
  this.extList = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_ITEMS).first();

  new Insertion.After(this.extList.dom, "<div class='loading'></div>");
  this.extLoading = this.extLayer.select(".loading").first();
  new Insertion.After(this.extList.dom, "<div class='empty'>" + Lang.Editor.Empty + "</div>");
  this.extEmpty = this.extLayer.select(".empty").first();

  var extBehaviourList = this.extLayer.select(".behaviour");
  extBehaviourList.each(function (extBehaviour) {
    Event.observe(extBehaviour.dom, "click", CGEditorDialogList.prototype.atBehaviourClick.bind(this, extBehaviour.dom));
  }, this);

  this.extDeleteItems = this.extLayer.select(".deleteitems").first();
  Event.observe(this.extDeleteItems.dom, "click", CGEditorDialogList.prototype.atItemsDeleteClick.bind(this));

  this.DOMDragDropHolder = $(new Insertion.Bottom(this.extList.dom, "<div style='display:none;border:1px solid #ccc;background:rgb(225,225,225);'></div>").element.descendants().last());
  this.DOMDragDropHolder.DOMElement = null;
};

CGEditorDialogList.prototype.registerElements = function () {
  var extElements = this.extList.select("li");
  extElements.each(function (extElement) {
    this.registerElement(extElement);
  }, this);
};

CGEditorDialogList.prototype.registerElement = function (extElement) {
  var DOMDeleteOption, DOMMoveOption;

  if ((extInput = extElement.select(HTML_INPUT).first()) == null) return;
  if ((extOptions = extElement.select(CSS_EDITOR_DIALOG_ELEMENT_ITEM_OPTIONS).first()) == null) return;

  Event.observe(extInput.dom, 'click', this.atItemClick.bindAsEventListener(this, extInput.dom));

  DOMDeleteOption = extOptions.down(CSS_EDITOR_DIALOG_ELEMENT_ITEM_DELETE).dom;
  Event.observe(DOMDeleteOption, 'click', this.atItemDeleteClick.bindAsEventListener(this, extInput.dom));

  DOMMoveOption = extOptions.down(CSS_EDITOR_DIALOG_ELEMENT_ITEM_MOVE).dom;
  extElement.dom.style.height = extElement.getHeight() + "px";
  new dragObject(extElement.dom, DOMMoveOption, new Position(0, -30), new Position(0, this.extList.getBottom() - 30), CGEditorDialogList.prototype.atItemDragStart.bind(this, DOMMoveOption), CGEditorDialogList.prototype.atItemDragMove.bind(this), CGEditorDialogList.prototype.atItemDragEnd.bind(this, DOMMoveOption), false);
};

//public
CGEditorDialogList.prototype.hide = function () {
  this.hideLoading();
  this.extLayer.dom.style.display = "none";
};

CGEditorDialogList.prototype.setConfiguration = function (Config) {
  if ((Config.Items != null) && (Config.Items == this.Items)) {
    return;
  }
  this.aSelectedItems = new Array();
  this.Items = Config.Items;
};

CGEditorDialogList.prototype.moveUp = function (Sender) {
};

CGEditorDialogList.prototype.moveDown = function (Sender) {
};

CGEditorDialogList.prototype.getData = function () {
};

CGEditorDialogList.prototype.setData = function (Data) {
  this.Items = Data;
};

CGEditorDialogList.prototype.getSelected = function () {
  var aResult = new Array();
  var extInputList = this.extList.select(HTML_INPUT);

  extInputList.each(function (extInput) {
    if (extInput.checked) aResult.push(extInput.name);
  }, this);

  return aResult;
};

CGEditorDialogList.prototype.deleteItems = function () {
  var aItems = new Array();
  for (var Id in this.aSelectedItems) {
    if (isFunction(this.aSelectedItems[Id])) continue;
    aItems.push(this.aSelectedItems[Id]);
  }
  if (this.onDelete) this.onDelete(aItems);
};

CGEditorDialogList.prototype.deleteItem = function (Id) {
  if (this.onDelete) this.onDelete([Id]);
};

CGEditorDialogList.prototype.getOrder = function () {
  var aResult = new Array();
  var extInputList = this.extList.select(HTML_INPUT);

  extInputList.each(function (extInput) {
    aResult.push(extInput.dom.name);
  }, this);

  return aResult;
};

CGEditorDialogList.prototype.reorder = function (Id) {
  if (this.onReorder) this.onReorder(this.getOrder());
};

CGEditorDialogList.prototype.refreshItems = function () {
  var sContent = "";

  this.extList.dom.innerHTML = "";
  for (var iPos = 0; iPos < this.Items.length; iPos++) {
    sContent += EditorDialogListItemTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'id': this.Items[iPos].id, 'title': this.Items[iPos].title});
  }
  this.extList.dom.innerHTML = sContent;

  this.registerElements();

  this.aSelectedItems = new Array();
  this.selectNone();
  this.extDeleteItems.addClass("disabled");

  if (this.Items.length > 0) {
    this.extEmpty.dom.style.display = "none";
  }
  else {
    this.extEmpty.dom.style.display = "block";
  }
};

CGEditorDialogList.prototype.refreshToolbar = function () {
  if (this.aSelectedItems.size() > 0) this.extDeleteItems.removeClass("disabled");
  else this.extDeleteItems.addClass("disabled");
};

CGEditorDialogList.prototype.refresh = function () {
  this.refreshItems();
  this.refreshToolbar();
};

CGEditorDialogList.prototype.select = function (Id) {
  this.aSelectedItems[Id] = Id;
  this.refreshToolbar();
};

CGEditorDialogList.prototype.unSelect = function (Id) {
  delete this.aSelectedItems[Id];
  this.refreshToolbar();
};

CGEditorDialogList.prototype.selectAll = function () {
  var extInputList = this.extList.select(HTML_INPUT);

  this.aSelectedItems = new Array();
  extInputList.each(function (extInput) {
    this.aSelectedItems[extInput.dom.name] = extInput.dom.name;
  }, this);

  selectAll(extInputList);
  this.refreshToolbar();
};

CGEditorDialogList.prototype.selectNone = function () {
  var extInputList = this.extList.select(HTML_INPUT);

  this.aSelectedItems = new Array();
  selectNone(extInputList);

  this.refreshToolbar();
};

CGEditorDialogList.prototype.selectInvert = function () {
  var extInputList = this.extList.select(HTML_INPUT);

  selectInvert(extInputList);
  extInputList = this.extList.select(HTML_INPUT);

  this.aSelectedItems = new Array();
  extInputList.each(function (extInput) {
    if (extInput.dom.checked) this.aSelectedItems[extInput.dom.name] = extInput.dom.name;
  }, this);

  this.refreshToolbar();
};

// #############################################################################################################

CGEditorDialogList.prototype.atSelect = function (oEvent) {
};

CGEditorDialogList.prototype.atBehaviourClick = function (DOMBehaviour, oEvent) {
  var CommandInfo = new CGCommandInfo(DOMBehaviour.href);
  var sOperation = CommandInfo.getOperation();

  Event.stop(oEvent);

  if (sOperation == "selectall") this.selectAll();
  else if (sOperation == "selectnone") this.selectNone();
  else if (sOperation == "selectinvert") this.selectInvert();
  else if (sOperation == "additem") if (this.onAdd) this.onAdd();

  return false;
};

CGEditorDialogList.prototype.atItemsDeleteClick = function (oEvent) {
  Event.stop(oEvent);

  if (this.aSelectedItems.size() == 0) return false;
  else this.deleteItems();

  return false;
};

CGEditorDialogList.prototype.atItemClick = function (oEvent, DOMInput) {
  if (DOMInput.checked) this.select(DOMInput.name);
  else this.unSelect(DOMInput.name);
};

CGEditorDialogList.prototype.atItemDeleteClick = function (oEvent, DOMInput) {
  this.deleteItem(DOMInput.name);
};

CGEditorDialogList.prototype.atItemDragStart = function (DOMMoveOption, oEvent, DOMElement) {
  var iHeight = Ext.get(DOMElement).getHeight();
  this.extList.addClass("draglist");
  Ext.get(DOMMoveOption).addClass("grabbing");
  DOMElement.style.top = DOMElement.offsetTop + 'px';
  DOMElement.style.left = DOMElement.offsetLeft + 'px';
  DOMElement.className = "drag";
  this.DOMDragDropHolder.style.display = "block";
  this.DOMDragDropHolder.style.height = iHeight + "px";
  this.extList.dom.insertBefore(this.DOMDragDropHolder, DOMElement);
  this.DOMDragDropHolder.DOMElement = DOMElement;
};

CGEditorDialogList.prototype.atItemDragMove = function (oPosition, DOMElement, oEvent) {
  var yPos = oPosition.Y + (oEvent.layerY ? oEvent.layerY : oEvent.offsetY);
  var temp;
  var bestItem = "end";

  for (var i = 0; i < this.extList.dom.childNodes.length; i++) {
    if (this.extList.dom.childNodes[i].className == "element") {
      temp = parseInt(Ext.get(this.extList.dom.childNodes[i]).getHeight());
      if (temp / 2 >= yPos) {
        bestItem = this.extList.dom.childNodes[i];
        break;
      }
      yPos -= temp;
    }
  }

  if (bestItem == this.DOMDragDropHolder || bestItem == this.DOMDragDropHolder.DOMElement) return;

  this.DOMDragDropHolder.DOMElement = bestItem;
  if (bestItem != "end") this.extList.dom.insertBefore(this.DOMDragDropHolder, this.extList.dom.childNodes[i]);
  else this.extList.dom.appendChild(this.DOMDragDropHolder);
};

CGEditorDialogList.prototype.atItemDragEnd = function (DOMMoveOption, DOMElement) {

  Ext.get(DOMMoveOption).removeClass("grabbing");

  this.DOMDragDropHolder.style.display = "none";

  if (this.DOMDragDropHolder.DOMElement != null) {
    this.DOMDragDropHolder.DOMElement = null;
    this.extList.dom.replaceChild(DOMElement, this.DOMDragDropHolder);
  }

  DOMElement.className = 'element';
  DOMElement.style.top = '0px';
  DOMElement.style.left = '0px';
  this.extList.removeClass("draglist");

  this.reorder();
};

CGEditorDialogList.prototype.atItemListKeyPress = function (oEvent) {
  var codeKey = oEvent.keyCode;
  var Sender = this.Sender;

  if ((Sender == null) && (this.extFilter)) Sender = this.extFilter.dom;
  if (Sender == null) return true;

  if (codeKey == oEvent.ENTER) {
    this.atSelect();
    Sender.focus();
    this.Sender = null;
  }
  else if (codeKey == oEvent.ESCAPE) {
    Sender.focus();
    this.Sender = null;
  }
  Event.stop(oEvent);
  return false;
};