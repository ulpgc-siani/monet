CGDialogSearchNodes = function () {
  this.base = CGDialog;
  this.base("dlgSearchNodes");
  this.sLayerName = null;
  this.Target = null;
};

//------------------------------------------------------------------
CGDialogSearchNodes.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogSearchNodes.prototype.init = function (sLayerName) {
  this.sLayerName = sLayerName;

  var html = AppTemplate.DialogSearchNodes;
  html = translate(html, Lang.DialogSearchNodes);

  $(this.sLayerName).innerHTML = html;

  Event.observe($("dlgSearchNodes.ButtonSearch"), "click", CGDialogSearchNodes.prototype.atSearchClick.bind(this));
  Event.observe($("dlgSearchNodes.Condition"), "keypress", CGDialogSearchNodes.prototype.atSearchConditionClick.bind(this));
};

//------------------------------------------------------------------
CGDialogSearchNodes.prototype.refresh = function () {
  /*
   var ActionSearchNodes = new CGActionSearchNodes();
   var extButtonSearch = Ext.get("dlgSearchNodes.ButtonSearch");
   var extCondition = Ext.get("dlgSearchNodes.Condition");

   if (State.Searching) {
   extButtonSearch.dom.value = Lang.DialogSearchNodes.Searching;
   extButtonSearch.addClass(CLASS_DISABLED);
   extButtonSearch.dom.disable();
   extCondition.dom.disable();
   }
   else {
   extButtonSearch.dom.value = Lang.DialogSearchNodes.Search;
   if (ActionSearchNodes.enabled()) {
   extButtonSearch.removeClass(CLASS_DISABLED);
   extButtonSearch.dom.enable();
   extCondition.dom.enable();
   }
   else {
   extButtonSearch.addClass(CLASS_DISABLED);
   extButtonSearch.dom.disable();
   extCondition.dom.disable();
   }
   }
   */
};

//==================================================================
CGDialogSearchNodes.prototype.atSearchClick = function (Item) {
  var Type = NodesCache.getCurrent().Type;
  var Behaviour = Extension.getDefinitionBehaviour(Type);
  var Mode = null;

  this.Condition = $("dlgSearchNodes.Condition").value;
  if (this.Condition == EMPTY) {
    Ext.MessageBox.alert(Lang.Warning.Title, Lang.DialogSearchNodes.Error.EmptyCondition);
    return false;
  }

  if (Behaviour && Behaviour.Search && Behaviour.Search.Templates && Behaviour.Search.Templates.View) Mode = Behaviour.Search.Templates.View;
  else if (State.LastSearch.Mode) Mode = State.LastSearch.Mode;

  CommandListener.dispatchCommand("searchnodes(" + escape(this.Condition) + "," + Type + "," + Mode + ")");
};

//==================================================================
CGDialogSearchNodes.prototype.atSearchConditionClick = function (Item) {
  if (Item.keyCode == KEY_ENTER) this.atSearchClick();
};