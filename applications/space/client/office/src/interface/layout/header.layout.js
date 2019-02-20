CGLayoutHeader = function () {
};

CGLayoutHeader.prototype.initLogo = function () {
  var extHeaderLogo = Ext.get(Literals.HeaderLogo);

  extHeaderLogo.dom.innerHTML = translate(extHeaderLogo.dom.innerHTML, Lang.LayoutHeader);

  var extFederationImage = extHeaderLogo.select(CSS_FEDERATION_IMAGE).first();
  extFederationImage.dom.src = Context.Config.FederationLogoUrl;
  extFederationImage.dom.title = Context.Config.FederationLabel;
  extFederationImage.dom.alt = Context.Config.FederationLabel;

  var extSpaceLabel = extHeaderLogo.select(".subtitle").first();
  extSpaceLabel.dom.innerHTML = Context.Config.SpaceLabel;

  var extModelLabel = extHeaderLogo.select(".title").first();
  extModelLabel.dom.innerHTML = Context.Config.ModelLabel;

  CommandListener.capture(extHeaderLogo.dom);
};

CGLayoutHeader.prototype.init = function (CommonLayout) {

  CommonLayout.add("north", new Ext.ContentPanel(Literals.Layout.Header, {title: Lang.LayoutHeader.Title}));

  var ToolbarHeader = new TToolbarHandler($(Literals.Toolbars.Header));
  ToolbarHeader.AddDefinition(ToolbarDefinition.Header);
  CommandListener.capture($(Literals.Toolbars.Header));

  this.initLogo();
  ViewUser.init(Literals.Views.User);
};

CGLayoutHeader.prototype.refresh = function () {
  ViewUser.refresh();
};