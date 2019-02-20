Desktop = new Object;
Desktop.sLayerName = null;
Desktop.layout = null;
Desktop.iLayoutWidth = DEFAULT_WIDTH;
Desktop.iLayoutHeight = DEFAULT_HEIGHT;
Desktop.aModes = new Array();
Desktop.iHeight = null;

Desktop.init = function (sLayerName) {

  var html = AppTemplate.Desktop;
  html = translate(html, Lang.Desktop);

  Desktop.sLayerName = sLayerName;
  document.body.innerHTML = html;

  Desktop.initLayout();
};

Desktop.getMode = function (IdNode) {
  if (Desktop.aModes[IdNode] == null)
    return false;
  return Desktop.aModes[IdNode];
};

Desktop.setMode = function (IdNode, Mode) {
  Desktop.aModes[IdNode] = Mode;
};

Desktop.setLayerSize = function (iWidth, iHeight) {
  Desktop.iLayerWidth = iWidth;
  Desktop.iLayerHeight = iHeight;
};

Desktop.initLayout = function () {

  if (Desktop.layout)
    return;

  Desktop.layout = new Ext.BorderLayout(document.body, {
    north: {
      split: false,
      initialSize: Widths.Layout.Header,
      titlebar: false
    },
    center: {
      initialSize: Widths.Layout.Main,
      titlebar: false,
      autoScroll: true,
      closeOnTab: true
    },
    south: {
      split: false,
      initialSize: Widths.Layout.Footer,
      titlebar: false,
      collapsible: false,
      animate: false
    }
  });

  if ((Ext.isIE) || (Ext.isIE7))
    $(document.body).addClassName("ie");
  if (Ext.isSafari)
    $(document.body).addClassName("safari");
  if (Ext.isGecko)
    $(document.body).addClassName("mozilla");
  if (Ext.isOpera)
    $(document.body).addClassName("opera");

  Desktop.layout.beginUpdate();

  Desktop.Header = new CGLayoutHeader();
  Desktop.Header.init(Desktop.layout);

  Desktop.Main = new CGLayoutMain();
  Desktop.Main.init(Desktop.layout);

  Desktop.Footer = new CGLayoutFooter();
  Desktop.Footer.init(Desktop.layout);

  Desktop.layout.restoreState();
  Desktop.layout.endUpdate();

  Desktop.extReportContainer = Ext.get(Literals.ReportContainer);

  ViewerSidebar.hide();
};

Desktop.showLoading = function () {
  $(Literals.Loading).show();
  $(Literals.LoadingMask).style.zIndex = 20000;
  $(Literals.LoadingMask).show();
};

Desktop.hideLoading = function () {
  $(Literals.Loading).hide();
  $(Literals.LoadingMask).hide();
};

Desktop.showMessageBox = function (sTitle, sSummary, sClass, iMiliseconds) {
  if (!Desktop.extReportContainer)
    return;

  Desktop.extReportContainer.dom.style.opacity = 100;

  var extTemplate = Desktop.extReportContainer.select(".template").first();
  var extTop = Desktop.extReportContainer.select(".x-box-tc").first();
  var extMiddle = Desktop.extReportContainer.select(".x-box-mc").first();
  var extBottom = Desktop.extReportContainer.select(".x-box-bc").first();
  var extTitle = Desktop.extReportContainer.select(".title").first();
  var extSummary = Desktop.extReportContainer.select(".summary").first();

  extTemplate.dom.className = "template " + sClass;
  extTitle.dom.innerHTML = sTitle;
  extSummary.dom.innerHTML = sSummary;

  if (Ext.isIE || Ext.isIE7) {
    extTop.setWidth(extMiddle.getWidth() - 9);
    extBottom.setWidth(extMiddle.getWidth() - 9);
  }

  Desktop.extReportContainer.alignTo(document, 't-t', [ 0, 1 ]);
  Desktop.extReportContainer.slideIn('t');

  if (iMiliseconds) {
    if (iMiliseconds != -1)
      window.setTimeout(Desktop.hideMessageBox.bind(this), iMiliseconds);
  } else
    Desktop.hideMessageBox();
};

Desktop.hideMessageBox = function () {
  if (!Desktop.extReportContainer)
    return;
  Desktop.extReportContainer.dom.style.display = "none";
  Desktop.extReportContainer.ghost("t");
};

Desktop.showMask = function () {
  var extDocument = Ext.get(document.body);
  var extMask = extDocument.down(".ext-el-mask");

  if (extMask == null)
    extMask = Ext.get(new Insertion.Bottom(document.body, "<div class='ext-el-mask'></div>").element.immediateDescendants().last());

  if (Desktop.iHeight == null) {
    Desktop.iHeight = 0;
    var aExtLayouts = extDocument.select(".x-layout-panel");
    aExtLayouts.each(function (extLayout) {
      Desktop.iHeight += extLayout.getHeight();
    }, this);
  }

  extMask.dom.style.display = "block";
  extMask.dom.style.height = Desktop.iHeight + "px";
};

Desktop.hideMask = function () {
  var extDocument = Ext.get(document.body);
  var extMask = extDocument.down(".ext-el-mask");
  if (extMask != null) {
    extMask.dom.style.display = "none";
    extMask.dom.style.height = "1px";
  }
};

Desktop.reportProgress = function (sMessage, bModal) {
  // if (bModal) Ext.MessageBox.wait(sMessage, Lang.Information.Title);
  // else Desktop.showMessageBox(Lang.Information.Wait, sMessage, 'progress',
  // -1);
  this.showMask();
};

Desktop.reportError = function (sMessage) {
  Desktop.showMessageBox(Lang.Error.Title, sMessage, 'error', 5000);
};

Desktop.reportBPIError = function (sMessage) {
  Desktop.showMessageBox(Lang.Error.TitleBPI, sMessage, 'error', 5000);
};

Desktop.reportWarning = function (sMessage) {
  Desktop.showMessageBox(Lang.Warning.Title, sMessage, 'warning', 5000);
};

Desktop.reportSuccess = function (sMessage) {
  Desktop.showMessageBox(Lang.Information.Title, sMessage, 'success', 5000);
};

Desktop.hideReports = function () {
  // Desktop.hideMessageBox();
  // Ext.MessageBox.hide();
  this.hideMask();
};

Desktop.hideProgress = function () {
  // Ext.MessageBox.hide();
  this.hideMask();
};

Desktop.reportBanner = function (sMessage) {
  Desktop.Main.Center.Header.reportBanner(sMessage);
};

Desktop.showBanner = function () {
  Desktop.Main.Center.Header.showBanner();
};

Desktop.hideBanner = function () {
  Desktop.Main.Center.Header.hideBanner();
};

Desktop.refresh = function () {
  Desktop.Header.refresh();
  Desktop.Main.refresh();
  Desktop.Footer.refresh();
};

Desktop.createView = function (DOMObject, Object, ViewContainer, Mode, bRefresh) {
  var View = null;

  if (Object instanceof CGNode)
    View = new CGViewNode();
  else if (Object instanceof CGTask)
    View = new CGViewTask();
  else if (Object instanceof CGTeam)
    View = new CGViewTeam();

  if (View == null)
    return;

  View.init(DOMObject);
  View.setTarget(Object);
  View.setMode(Mode);
  View.setContainer(ViewContainer);
  Desktop.Main.Center.Body.addView(View.Type, View);

  if (bRefresh)
    View.refresh();

  if (View.Type == VIEW_NODE) {
    if (Extension.isDOMNodeCollection(View.getDOM()))
      View.setType(VIEW_NODE_TYPE_COLLECTION);
    if (Extension.isDOMNodeForm(View.getDOM()))
      View.setType(VIEW_NODE_TYPE_FORM);
    else
      View.setType(VIEW_NODE_TYPE_NODE);
  }

  return View;
};

Desktop.markNodesReferences = function (ViewNode) {
  var DOMNode = ViewNode.getDOM();

  if (DOMNode.markNodesReferences)
    DOMNode.markNodesReferences(State.aMarkedNodesReferences, State.NodeReferenceMarkType);

  if (DOMNode.getCollections) {
    var aDOMCollections = DOMNode.getCollections();
    for (var pos = 0; pos < aDOMCollections.length; pos++) {
      if (aDOMCollections[pos].markNodesReferences)
        aDOMCollections[pos].markNodesReferences(State.aMarkedNodesReferences, State.NodeReferenceMarkType);
    }
  }
};

Desktop.getDigitalSignatureApplication = function () {
  var frameLayer = $("#" + Literals.Frames.SignatureApp);
  var frame = frames[Literals.Frames.SignatureApp];

  if (frameLayer == null) frameLayer = $(Literals.Frames.SignatureApp);
  frameLayer.style.display = "block";

  var signatureApp = frame.document.application;
  signatureApp.language = Context.Config.Language;
  signatureApp.downloadUrl = Context.Config.SignatureApplication.DownloadUrl;
  signatureApp.storageUrl = Context.Config.SignatureApplication.StorageUrl;
  signatureApp.retrieveUrl = Context.Config.SignatureApplication.RetrieveUrl;
  signatureApp.init();

  return signatureApp;
};