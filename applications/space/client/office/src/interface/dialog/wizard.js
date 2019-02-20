CGWizard = function () {
  this.extDisplay = null;
  this.Dialog = null;
  this.iStep = 1;
};

//------------------------------------------------------------------
CGWizard.prototype.init = function (sContent, DOMContainer, bModal) {
  if (DOMContainer == null) DOMContainer = document.body;

  if (bModal == null || bModal == true) this.initDialogBox(DOMContainer, sContent);
  else this.initLayer(DOMContainer, sContent);

  this.firstStep();
};

//------------------------------------------------------------------
CGWizard.prototype.initDialogBox = function (DOMContainer, sContent) {
  var DOMLayer = new Insertion.Bottom(DOMContainer, sContent).element.immediateDescendants().last();
  var extLayer = Ext.get(DOMLayer);

  this.extDisplay = extLayer.select(".body").first();

  this.Dialog = new Ext.LayoutDialog(DOMLayer, {
    modal: true, shadow: true, minWidth: 400, minHeight: 300, closable: false,
    center: { autoScroll: false }
  });

  this.Dialog.addKeyListener(27, this.atCancel, this);
  this.extCancel = this.Dialog.addButton(Lang.Buttons.Cancel, this.atCancel, this);
  this.extPrevious = this.Dialog.addButton(Lang.Buttons.Previous, this.atPreviousStep, this);
  this.extNext = this.Dialog.addButton(Lang.Buttons.Next, this.atNextStep, this);
  this.extFinish = this.Dialog.addButton(Lang.Buttons.Finish, this.atAccept, this);

  this.extSteps = null;

  var layout = this.Dialog.getLayout();
  layout.beginUpdate();
  layout.add('center', new Ext.ContentPanel(this.extDisplay.dom));
  layout.endUpdate();
};

//------------------------------------------------------------------
CGWizard.prototype.initLayer = function (DOMContainer, sContent) {
  var html = AppTemplate.Wizard;
  var DOMLayer = new Insertion.Bottom(DOMContainer, translate(html, Lang.Wizard)).element.immediateDescendants().last();
  var extLayer = Ext.get(DOMLayer);

  this.extDisplay = extLayer.select(".display").first();
  this.extDisplay.dom.innerHTML = sContent;

  this.extCancel = extLayer.select(".toolbar .cancel").first();
  if (this.extCancel) this.extCancel.on("click", this.atCancel, this);

  this.extPrevious = extLayer.select(".toolbar .previous").first();
  this.extPrevious.on("click", this.atPreviousStep, this);

  this.extNext = extLayer.select(".toolbar .next").first();
  this.extNext.on("click", this.atNextStep, this);

  this.extFinish = extLayer.select(".toolbar .finish").first();
  if (this.extFinish) this.extFinish.on("click", this.atAccept, this);

  this.extSteps = extLayer.select(".steps").first();
  var iNumSteps = this.extDisplay.select(".step").getCount();
  for (var i = 1; i <= iNumSteps; i++) {
    var extTitle = this.extDisplay.select(".step." + i + " .wizard.title").first();
    var extStep = Ext.get(new Insertion.Bottom(this.extSteps.dom, "<li class='assistant step" + i + "'>" + extTitle.dom.innerHTML + "</li>").element.immediateDescendants().last());
    extStep.on("click", function () {
      alert("has pulsado en un step");
    });
  }
};

//------------------------------------------------------------------
CGWizard.prototype.disable = function (extElement) {
  if (extElement == null) return;
  if (extElement.disable) extElement.disable();
  else extElement.addClass(CLASS_DISABLED);
};

//------------------------------------------------------------------
CGWizard.prototype.enable = function (extElement) {
  if (extElement == null) return;
  if (extElement.enable) extElement.enable();
  else extElement.removeClass(CLASS_DISABLED);
};

//------------------------------------------------------------------
CGWizard.prototype.refreshSteps = function (iStep) {
  if (this.extSteps == null) return;

  var extStepList = this.extSteps.select("li");
  var iCurrent = 1;
  extStepList.each(function (extStep) {
    extStep.removeClass(CLASS_DISABLED);
    if (iCurrent > iStep) extStep.addClass(CLASS_DISABLED);
    iCurrent++;
  }, this);
};

//------------------------------------------------------------------
CGWizard.prototype.refreshButtonbar = function (sBranch) {
  var extPreviousStep = this.getStep(this.iStep - 1, sBranch);
  var extNextStep = this.getStep(this.iStep + 1, sBranch);

  if (extPreviousStep == null) this.disable(this.extPrevious);
  else this.enable(this.extPrevious);

  if (extNextStep == null) {
    this.disable(this.extNext);
    this.enable(this.extFinish);
  }
  else {
    this.enable(this.extNext);
    this.disable(this.extFinish);
  }
};

//------------------------------------------------------------------
CGWizard.prototype.enablePreviousButton = function () {
  this.enable(this.extPrevious);
};

//------------------------------------------------------------------
CGWizard.prototype.disablePreviousButton = function () {
  this.disable(this.extPrevious);
};

//------------------------------------------------------------------
CGWizard.prototype.enableNextButton = function () {
  this.enable(this.extNext);
};

//------------------------------------------------------------------
CGWizard.prototype.disableNextButton = function () {
  this.disable(this.extNext);
};

//------------------------------------------------------------------
CGWizard.prototype.enableFinishButton = function () {
  this.enable(this.extFinish);
};

//------------------------------------------------------------------
CGWizard.prototype.disableFinishButton = function () {
  this.disable(this.extFinish);
};

//------------------------------------------------------------------
CGWizard.prototype.getStep = function (iStep, sBranch) {
  extStep = this.extDisplay.select(".step." + iStep + ((sBranch != null) ? sBranch : "")).first();
  if (extStep == null) extStep = this.extDisplay.select(".step." + iStep + ".main").first();
  return extStep;
};

//------------------------------------------------------------------
CGWizard.prototype.getActiveStep = function () {
  var extStep = this.getStep(this.iStep);
  if (extStep == null) return;
  extStep.id = this.iStep;
  return extStep;
};

//------------------------------------------------------------------
CGWizard.prototype.hideSteps = function () {
  var extStepList = this.extDisplay.select(".step");
  extStepList.each(function (extStep) {
    extStep.dom.style.display = "none";
  }, this);
};

//------------------------------------------------------------------
CGWizard.prototype.showStep = function (iStep, sBranch) {
  var extStep = this.getStep(iStep, sBranch);
  if (extStep == null) return;
  this.iStep = iStep;
  this.hideSteps();
  extStep.dom.style.display = "block";
  this.refreshSteps(iStep);
  this.refreshButtonbar(sBranch);
};

//------------------------------------------------------------------
CGWizard.prototype.firstStep = function () {
  this.showStep(1);
};

//------------------------------------------------------------------
CGWizard.prototype.previousStep = function (sBranch) {
  this.iStep--;
  if (this.iStep < 1) this.iStep = 1;
  this.showStep(this.iStep, sBranch);
  if (this.onPreviousStep) this.onPreviousStep();
};

//------------------------------------------------------------------
CGWizard.prototype.nextStep = function (sBranch) {
  this.iStep++;
  this.showStep(this.iStep, sBranch);
  if (this.onNextStep) this.onNextStep();
};

//------------------------------------------------------------------
CGWizard.prototype.show = function () {
  if (this.Dialog == null) return false;
  this.Dialog.show();
  this.firstStep();
};

//------------------------------------------------------------------
CGWizard.prototype.hide = function () {
  if (this.Dialog == null) return false;
  this.Dialog.hide();
};

//------------------------------------------------------------------
CGWizard.prototype.destroy = function () {
  this.Dialog.destroy(true);
};

//==================================================================
CGWizard.prototype.atCancel = function () {
  if (this.onCancel) this.onCancel();
};

//==================================================================
CGWizard.prototype.atPreviousStep = function () {
  if (this.PreviousStepHandler) this.PreviousStepHandler();
  else this.previousStep();
};

//==================================================================
CGWizard.prototype.atNextStep = function () {
  if (this.NextStepHandler) this.NextStepHandler();
  else this.nextStep();
};

//==================================================================
CGWizard.prototype.atAccept = function () {
  if (this.onAccept) this.onAccept();
};