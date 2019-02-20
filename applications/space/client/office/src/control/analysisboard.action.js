//----------------------------------------------------------------------
// Show analysisboard
//----------------------------------------------------------------------
function CGActionShowAnalysisboard() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowAnalysisboard.prototype = new CGAction;
CGActionShowAnalysisboard.constructor = CGActionShowAnalysisboard;
CommandFactory.register(CGActionShowAnalysisboard, { Code: 0 }, true);

CGActionShowAnalysisboard.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  var process = new CGProcessCloseRightPanel();
  process.execute();

  Kernel.loadSystemTemplate(this, "analysisboard", this.Code);
};

CGActionShowAnalysisboard.prototype.step_2 = function () {
  ViewAnalysisboard.setContent(this.data);
  ViewAnalysisboard.refresh();
  ViewAnalysisboard.show();
  Desktop.Main.Center.Body.activateAnalysisboard();
};