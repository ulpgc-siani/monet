CGDialogSortNodesBy = function () {
  this.base = CGDialog;
  this.base("dlgSortNodesBy");
};

//------------------------------------------------------------------
CGDialogSortNodesBy.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogSortNodesBy.prototype.init = function () {
};

//------------------------------------------------------------------
CGDialogSortNodesBy.prototype.show = function () {
};

//------------------------------------------------------------------
CGDialogSortNodesBy.prototype.hide = function () {
};

//------------------------------------------------------------------
CGDialogSortNodesBy.prototype.destroy = function () {
};

//------------------------------------------------------------------
CGDialogSortNodesBy.prototype.refresh = function () {
  if (!this.Target.Descriptors) return false;
  if (!this.Target.Node) return false;
  if (!this.Target.Menu) return false;

  this.Target.Menu.removeAll();

  var sCheckedDescriptor = this.Target.Descriptors[DEFAULT].Name;
  for (var Index in this.Target.Descriptors) {
    if (isFunction(this.Target.Descriptors[Index])) continue;
    var Descriptor = this.Target.Descriptors[Index];
    var oCheckItem = new Ext.menu.CheckItem({
      name: Descriptor.Name,
      text: Descriptor.Caption,
      checked: (Descriptor.Name == sCheckedDescriptor) ? true : false,
      group: 'sortbygroup',
      handler: CGDialogSortNodesBy.prototype.atItemClick.bind(this)
    });
    sCommand = "sortnodesby(" + Descriptor.Name + ")";
    oCheckItem.command = sCommand;
    this.Target.Menu.add(oCheckItem);
  }

};

//------------------------------------------------------------------
CGDialogSortNodesBy.prototype.check = function () {
};

//==================================================================
CGDialogSortNodesBy.prototype.atItemClick = function (Item, EventLaunched) {
  CommandListener.dispatchCommand(Item.command);
  if (EventLaunched) Event.stop(EventLaunched);
};

//==================================================================
CGDialogSortNodesBy.prototype.atAccept = function () {
  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogSortNodesBy.prototype.atCancel = function () {
  if (this.onCancel) this.onCancel();
};