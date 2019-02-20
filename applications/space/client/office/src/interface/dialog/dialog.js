CGDialog = function (sName) {
  this.sName = sName;
  this.layer = null;
  this.dialog = null;
  this.onAccept = null;
  this.onCancel = null;
};

//------------------------------------------------------------------
CGDialog.prototype.initRequiredFields = function () {
  var extRequiredFieldList, extLayer;

  if (!this.layer) return;

  extLayer = Ext.get(this.layer);
  extRequiredFieldList = extLayer.select('.outfield.required .outtitle');
  extRequiredFieldList.each(function (extRequiredField) {
    extRequiredField.dom.innerHTML = "<div>" + extRequiredField.dom.innerHTML + "<span class='required'>&nbsp;</span></div>";
  });
};

//------------------------------------------------------------------
CGDialog.prototype.initDialog = function () {
  if (!this.layer) return;

  this.dialog = new Ext.LayoutDialog(this.layer, {
    modal: true, shadow: true, minWidth: 300, minHeight: 300, closable: false,
    center: { autoScroll: true }
  });

  this.dialog.addKeyListener(27, this.atCancel, this);
  this.dialog.addButton(Lang.Buttons.Accept, this.atAccept, this);
  this.dialog.addButton(Lang.Buttons.Cancel, this.atCancel, this);

  var layout = this.dialog.getLayout();
  layout.beginUpdate();
  layout.add('center', new Ext.ContentPanel(this.sName + ".center"));
  layout.endUpdate();
};

//------------------------------------------------------------------
CGDialog.prototype.show = function () {
  var DOMStatus = $(this.sName + ".status");
  var extLayout = Ext.get(this.sName);
  var extInput;

  if (this.dialog == null) return;
  if (!DOMStatus) return;
  if (!extLayout) return;

  DOMStatus.style.display = "none";
  this.dialog.show();

  extInput = extLayout.select(HTML_INPUT).first();
  if (extInput) extInput.dom.focus();
};

//------------------------------------------------------------------
CGDialog.prototype.hide = function () {
  if (this.dialog == null) return;
  this.dialog.hide();
};

//------------------------------------------------------------------
CGDialog.prototype.destroy = function () {
  if (this.dialog && this.dialog.destroy) this.dialog.destroy(true);
};

//------------------------------------------------------------------
CGDialog.prototype.check = function () {
  var sMessage = EMPTY;
  return (sMessage == EMPTY);
};

//------------------------------------------------------------------
CGDialog.prototype.showStatus = function (sMessage) {
  var extStatus = Ext.get(this.sName + ".status");
  var extLayout = Ext.get(this.sName);

  if (!extStatus) return;

  var extTop = extStatus.select(".x-box-tc").first();
  var extMiddle = extStatus.select(".x-box-mc").first();
  var extBottom = extStatus.select(".x-box-bc").first();
  var extTitle = extStatus.select(".title").first();
  var extSummary = extStatus.select(".summary").first();

  extTitle.dom.innerHTML = Lang.Dialog.Required;
  extSummary.dom.innerHTML = sMessage;

  extStatus.dom.style.display = "block";
  if (extLayout) extStatus.dom.style.left = (extLayout.getWidth() / 2) - (extStatus.getWidth() / 2) + "px";

  if (Ext.isIE || Ext.isIE7) {
    extTop.setWidth(extMiddle.getWidth() - 9);
    extBottom.setWidth(extMiddle.getWidth() - 9);
  }

  if (extLayout) {
    extStatus.slideIn('t');
    window.setTimeout(CGDialog.prototype.hideStatus.bind(this), 3000);
  }
  else extStatus.show();
};

//------------------------------------------------------------------
CGDialog.prototype.hideStatus = function () {
  var extStatus = Ext.get(this.sName + ".status");
  if (!extStatus) return;
  extStatus.dom.style.display = "none";
};

//------------------------------------------------------------------
CGDialog.prototype.showReport = function (sReport) {
  var extReport = Ext.get(this.sName + ".report");
  if (!extReport) return;
  extReport.dom.style.display = "block";
  extReport.dom.innerHTML = sReport;
};

//==================================================================
CGDialog.prototype.atAccept = function () {
  if (!this.check()) return;
  this.hide();
  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialog.prototype.atCancel = function () {
  this.hide();
  this.destroy();
  if (this.onCancel) this.onCancel();
};