CGLayoutMainHeader = function () {
};

CGLayoutMainHeader.prototype.init = function (InnerLayout) {
  InnerLayout.add("north", new Ext.ContentPanel(Literals.Layout.MainHeader, {title: Lang.LayoutMainHeader.Title}));
};

CGLayoutMainHeader.prototype.refresh = function () {
  $("CurrentDate").innerHTML = getFormattedDate(new Date(), Context.Config.Language);
};