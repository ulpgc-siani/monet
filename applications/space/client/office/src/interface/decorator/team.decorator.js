CGDecoratorTeam = function () {
};

CGDecoratorTeam.prototype = new CGDecorator;

CGDecoratorTeam.prototype.execute = function (DOMTeam) {

  this.addCommonMethods(DOMTeam);

  DOMTeam.init = function () {
    var extTeam = Ext.get(this);

    this.initToolbar(".header .content .toolbar");
    this.initTabs(CSS_TEAM);
  };

  DOMTeam.destroy = function () {
  };

  DOMTeam.getId = function () {
    return DOMTeam.getControlInfo().IdTeam;
  };

  DOMTeam.getControlInfo = function () {
    var extId;
    var aResult = new Array();
    var extTeam = Ext.get(this);

    if (DOMTeam.ControlInfo) return DOMTeam.ControlInfo;

    DOMTeam.ControlInfo = new Object();
    DOMTeam.ControlInfo.IdTeam = (extId = extTeam.select(CSS_CONTROL_INFO + " > .idteam").first()) ? extId.dom.innerHTML : "-1";
    DOMTeam.ControlInfo.Templates = new Object();

    aResult = extTeam.select(".tpl.refresh");
    DOMTeam.ControlInfo.Templates.Refresh = (eResult = aResult.first()) ? eResult.dom.innerHTML : null;

    return DOMTeam.ControlInfo;
  };

  DOMTeam.isLoaded = function () {
    var extTeam = Ext.get(this);
    var extBody = extTeam.down(CSS_BODY);
    if (extBody == null) return true;
    return (!extBody.hasClass(CLASS_LOADING));
  };

};