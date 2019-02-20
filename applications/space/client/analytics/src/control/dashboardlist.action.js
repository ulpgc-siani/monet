//----------------------------------------------------------------------
// Action show dashboard list
//----------------------------------------------------------------------
function ActionShowDashboardList() {
	this.base = Action;
	this.base(3);
};

ActionShowDashboardList.prototype = new Action;
ActionShowDashboardList.constructor = ActionShowDashboardList;
CommandFactory.register(ActionShowDashboardList, null, false);

ActionShowDashboardList.prototype.step_1 = function () {
	Kernel.loadDashboardList(this);
};

ActionShowDashboardList.prototype.step_2 = function () {
	var dashboardList = jQuery.parseJSON(this.data);

	var view = new ViewDashboardList();
	view.id = View.DASHBOARDS.replace("::id::", "dashboardlist");
	view.target = { id: "dashboardlist", dashboardList: dashboardList };
	Desktop.addState(view);

	Desktop.cleanViewsContainer();
	view.init(Desktop.createView());
	view.refresh();

	Desktop.registerView(view.id, view);
	Desktop.hideLoading();
	Desktop.hideWestLayer();

	this.terminate();
};