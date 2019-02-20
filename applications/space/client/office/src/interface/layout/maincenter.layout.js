CGLayoutMainCenter = function () {
  this.Header = null;
  this.Body = null;
};

CGLayoutMainCenter.prototype.init = function (InnerLayout) {

  this.innerLayout = new Ext.BorderLayout($(Literals.Layout.MainCenter), {
    north: { split: false, initialSize: Widths.Layout.MainCenterHeader, titlebar: false },
    center: { initialSize: Widths.Layout.MainCenterBody, titlebar: false, autoScroll: true, closeOnTab: true, hideTabs: true, tabPosition: 'top' }
  });

  this.nestedPanel = new Ext.NestedLayoutPanel(this.innerLayout, Literals.Layout.MainCenter);
  this.nestedPanel.setTitle(Lang.LayoutMainCenter.Title);
  InnerLayout.add('center', this.nestedPanel);

  this.initLayout();
};

CGLayoutMainCenter.prototype.initLayout = function () {

  this.innerLayout.beginUpdate();

  this.Header = new CGLayoutMainCenterHeader();
  this.Header.init(this.innerLayout);

  this.Body = new CGLayoutMainCenterBody();
  this.Body.init(this.innerLayout);

  this.innerLayout.restoreState();
  this.innerLayout.endUpdate();

};

CGLayoutMainCenter.prototype.refresh = function () {
  this.Header.refresh();
  this.Body.refresh();
};