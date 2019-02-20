CGDialogNodeViewList = function () {
  this.base = CGDialog;
  this.base("dlgNodeViewList");
};

CGDialogNodeViewList.prototype = new CGDialog;

CGDialogNodeViewList.prototype.init = function () {
};

//------------------------------------------------------------------
CGDialogNodeViewList.prototype.show = function () {
};

//------------------------------------------------------------------
CGDialogNodeViewList.prototype.hide = function () {
};

//------------------------------------------------------------------
CGDialogNodeViewList.prototype.destroy = function () {
};

//------------------------------------------------------------------
CGDialogNodeViewList.prototype.refresh = function () {
  if (!this.Target.NodeViews) return false;
  if (!this.Target.Node) return false;
  if (!this.Target.Menu) return false;

  this.Target.Menu.removeAll();

  var sCheckedNodeView = (State.View) ? State.View : this.Target.NodeViews[DEFAULT].Name;
  for (var Index in this.Target.NodeViews) {
    if (isFunction(this.Target.NodeViews[Index])) continue;
    var NodeView = this.Target.NodeViews[Index];
    var oCheckItem = new Ext.menu.CheckItem({
      name: NodeView.Name,
      text: NodeView.Caption,
      checked: (NodeView.Name == sCheckedNodeView) ? true : false,
      group: 'viewmenugroup',
      handler: CGDialogNodeViewList.prototype.atItemClick.bind(this)
    });
    oCheckItem.command = "setnodeview(" + this.Target.Node.getId() + "," + NodeView.Name + ")";
    this.Target.Menu.add(oCheckItem);
  }
};

//------------------------------------------------------------------
CGDialogNodeViewList.prototype.check = function () {
};

//==================================================================
CGDialogNodeViewList.prototype.atItemClick = function (Item, EventLaunched) {
  CommandListener.dispatchCommand(Item.command);
  if (EventLaunched) Event.stop(EventLaunched);
};

//==================================================================
CGDialogNodeViewList.prototype.atAccept = function () {
  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogNodeViewList.prototype.atCancel = function () {
  if (this.onCancel) this.onCancel();
};