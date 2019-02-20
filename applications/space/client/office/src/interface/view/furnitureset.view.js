ViewFurnitureSet = new Object;
ViewFurnitureSet.account = null;
ViewFurnitureSet.banner = false;
ViewFurnitureSet.hideBannerTimeout = null;

ViewFurnitureSet.init = function (DOMLayer) {
  ViewFurnitureSet.DOMLayer = DOMLayer;

  var html = AppTemplate.ViewFurnitureSet;
  html = translate(html, Lang.ViewFurnitureSet);

  $(ViewFurnitureSet.DOMLayer).innerHTML = html;

  this.itemTemplate = new Template(translate(AppTemplate.ViewFurnitureSetItem, Lang.ViewFurnitureSet));
  this.bannerItemTemplate = new Template(translate(AppTemplate.ViewFurnitureSetBannerItem, Lang.ViewFurnitureSet));

  ViewFurnitureSet.initBehaviours();
};

ViewFurnitureSet.initBehaviours = function() {
  var extBanner = Ext.get(ViewFurnitureSet.DOMLayer).select(".banner").first();
  extBanner.on("mouseover", ViewFurnitureSet.stopHideBanner.bind(ViewFurnitureSet));
  extBanner.on("mouseout", ViewFurnitureSet.continueHideBanner.bind(ViewFurnitureSet));
};

ViewFurnitureSet.setTarget = function (target) {
  ViewFurnitureSet.account = target.account;
  ViewFurnitureSet.banner = target.banner;
};

ViewFurnitureSet.show = function () {
  if (!$(ViewFurnitureSet.DOMLayer)) return;
  $(ViewFurnitureSet.DOMLayer).show();
};

ViewFurnitureSet.hide = function () {
  if (!$(ViewFurnitureSet.DOMLayer)) return;
  $(ViewFurnitureSet.DOMLayer).hide();
};

ViewFurnitureSet.refresh = function () {
  if (!ViewFurnitureSet.account) return;

  var content = "";
  var links = ViewFurnitureSet.account.Links;
  var extList = Ext.get(ViewFurnitureSet.DOMLayer).select("ul").first();

  for (var i = 0; i < links.length; i++) {
    var link = links[i];
    var furniture = new Object();
    ViewFurnitureSet.fillFurniture(furniture, link);
    content += this.itemTemplate.evaluate(furniture);
  }

  this.refreshBanner();

  extList.dom.innerHTML = content;
  CommandListener.capture(extList.dom);
};

ViewFurnitureSet.refreshBanner = function () {
  var NotificationList = ViewFurnitureSet.account.getLastNotifications();

  if (NotificationList.unread <= 0 || !ViewFurnitureSet.banner)
    return;

  var bannerContent = "";
  for (var i=0; i<Account.Notifications.rows.length; i++) {
    var Notification = Account.Notifications.rows[i];
    Notification.command = getMonetLinkAction(Notification.target);
    if (!Notification.read) bannerContent += this.bannerItemTemplate.evaluate(Notification);
  }

  var extBanner = Ext.get(ViewFurnitureSet.DOMLayer).select(".banner").first();
  var extBannerList = extBanner.select("ul").first();
  extBannerList.dom.innerHTML = bannerContent;

  ViewFurnitureSet.showBanner();

  CommandListener.capture(extBanner.dom);
};

ViewFurnitureSet.showBanner = function () {
  var extBanner = Ext.get(ViewFurnitureSet.DOMLayer).select(".banner").first();
  extBanner.dom.style.display = "block";
  extBanner.dom.style.marginTop = "0";
  extBanner.dom.style.top = "0";
  extBanner.dom.style.opacity = 1;

  extBanner.dom.style.display = "block";
  new Effect.Move(extBanner.dom, { x: 0, y: "-" + (extBanner.getHeight() + 10), duration: 1 });
  this.hideBannerTimeout = window.setTimeout(ViewFurnitureSet.hideBanner.bind(this), 4000);

  Ext.get(document.body).on("click", ViewFurnitureSet.hideBanner, ViewFurnitureSet);
};

ViewFurnitureSet.hideBanner = function () {
  var extBanner = Ext.get(ViewFurnitureSet.DOMLayer).select(".banner").first();
  new Effect.DropOut(extBanner.dom, { duration: 1 });
  Ext.get(document.body).un("click", ViewFurnitureSet.hideBanner, ViewFurnitureSet);
};

ViewFurnitureSet.stopHideBanner = function () {
  if (this.hideBannerTimeout != null)
    window.clearTimeout(this.hideBannerTimeout);
};

ViewFurnitureSet.continueHideBanner = function() {
  this.hideBannerTimeout = window.setTimeout(ViewFurnitureSet.hideBanner.bind(this), 1000);
};

ViewFurnitureSet.getCommand = function (link) {
	if (link.type == Furniture.DESKTOP) return "showenvironment(" + link.id + ")";
	else if (link.type == Furniture.DASHBOARD) return "showdashboard(" + link.id + "," + link.view + ")";
	else if (link.type == Furniture.NEWS) return "shownews()";
	else if (link.type == Furniture.ROLES) return "showrolelist()";
	else if (link.type == Furniture.TASKBOARD) {
		if (link.view == null)
			link.view = "active";
		return "showtasklist(taskboard," + link.view + ")";
	}
	else if (link.type == Furniture.TASKTRAY) {
		if (link.view == null)
			link.view = "alive";
		return "showtasklist(tasktray," + link.view + ")";
	}
  else if (link.type == Furniture.TRASH) return "showtrash(trash)";
};

ViewFurnitureSet.fillFurniture = function (furniture, link) {

  if (link.type == Furniture.DESKTOP)
    furniture.label = link.label.toLowerCase();
  else if (link.type == Furniture.DASHBOARD)
    furniture.label = link.label.toLowerCase();
  else if (link.type == Furniture.NEWS) {
    var NotificationList = ViewFurnitureSet.account.getLastNotifications();
    furniture.label = Lang.ViewFurnitureSet.News;
    furniture.bubble = NotificationList.unread > 0 ? NotificationList.unread : null;
  }
  else if (link.type == Furniture.ROLES)
    furniture.label = Lang.ViewFurnitureSet.Roles;
  else if (link.type == Furniture.TASKBOARD) {
    var count = ViewFurnitureSet.account.getPendingTasks("taskboard");
    furniture.label = Lang.ViewFurnitureSet.Taskboard;
    furniture.bubble = count > 0 ? count : null;
  }
  else if (link.type == Furniture.TASKTRAY) {
    var count = ViewFurnitureSet.account.getPendingTasks("tasktray");
    furniture.label = Lang.ViewFurnitureSet.Tasktray;
    furniture.bubble = count > 0 ? count : null;
  }
  else if (link.type == Furniture.TRASH)
    furniture.label = Lang.ViewFurnitureSet.Trash;

  furniture.id = link.id;
  furniture.command = ViewFurnitureSet.getCommand(link);
  furniture.type = link.type;
  furniture.fullLabel = furniture.label;
  furniture.shortLabel = shortValue(furniture.label, 10, 5, 2);
  furniture.highlighted = furniture.bubble != null && furniture.bubble > 0 ? "true" : "";
  furniture.active = ViewFurnitureSet.isFurnitureActive(furniture) ? "true" : "";
};

ViewFurnitureSet.isFurnitureActive = function (furniture) {
  var activeFurniture = State.ActiveFurniture;

  if (activeFurniture.type == "desktop" || activeFurniture.type == "dashboard")
    return activeFurniture.id == furniture.id;

  return activeFurniture.type == furniture.type;
};