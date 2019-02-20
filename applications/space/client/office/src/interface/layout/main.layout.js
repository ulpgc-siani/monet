CGLayoutMain = function () {
  this.nestedPanel = null;
  this.innerLayout = null;
  this.Header = null;
  this.Center = null;
  this.Right = null;
};

CGLayoutMain.prototype.init = function (CommonLayout) {
  var iWidth = CommonLayout.el.getWidth();
  var iCenterWidth = Math.floor(iWidth * 60 / 100);
  var iEastWidth = Math.floor(iWidth * 40 / 100);

  this.innerLayout = new Ext.BorderLayout($(Literals.Layout.Main), {
    //north:  { split:false, initialSize: Widths.Layout.MainHeader, titlebar: false },
    center: { initialSize: iCenterWidth, titlebar: false, autoScroll: true, closeOnTab: true },
    east: { split: true, initialSize: iEastWidth, minSize: Widths.Layout.MainRight, maxSize: (iEastWidth * 2), titlebar: true, collapsible: true, animate: true, hideTabs: true, collapsed: true }
  });

  this.nestedPanel = new Ext.NestedLayoutPanel(this.innerLayout, Literals.Layout.Main);
  CommonLayout.add('center', this.nestedPanel);

  this.initLayout();
};

CGLayoutMain.prototype.initLayout = function () {

  this.innerLayout.beginUpdate();

//  this.Header = new CGLayoutMainHeader();
//  this.Header.init(this.innerLayout);

  this.Center = new CGLayoutMainCenter();
  this.Center.init(this.innerLayout);

  this.Right = new CGLayoutMainRight();
  this.Right.init(this.innerLayout);

  this.innerLayout.restoreState();
  this.innerLayout.endUpdate();
};

CGLayoutMain.prototype.refresh = function () {
//  this.Header.refresh();
  this.Center.refresh();
  this.Right.refresh();
};