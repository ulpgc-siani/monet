CGLayoutFooter = function () {
  this.CurrentNode = null;
};

CGLayoutFooter.prototype.initLogos = function () {
  var extFooterLogosPanel = Ext.get(Literals.FooterLogosPanel);

  extFooterLogosPanel.dom.innerHTML = translate(extFooterLogosPanel.dom.innerHTML, Lang.LayoutFooter);

  var extModelImage = extFooterLogosPanel.select(CSS_MODEL_IMAGE).first();
  extModelImage.dom.src = Context.Config.ModelLogoSplashUrl;
  extModelImage.dom.title = Context.Config.ModelLabel;
  extModelImage.dom.alt = Context.Config.ModelLabel;
};

CGLayoutFooter.prototype.init = function (CommonLayout) {
  CommonLayout.add("south", new Ext.ContentPanel(Literals.Layout.Footer, {title: Lang.LayoutFooter.Title}));
  ViewFurnitureSet.init(Literals.Views.FurnitureSet);
  this.initLogos();
};

CGLayoutFooter.prototype.setCurrentNode = function (CurrentNode) {
  this.CurrentNode = CurrentNode;
};

CGLayoutFooter.prototype.refresh = function () {
};