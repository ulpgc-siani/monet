function Process(numStates) {
	this.state = 0;
	this.numStates = numStates;
	this.terminated = false;
	this.success = false;
	this.failureMessage = "";
	this.refreshTaskList = null;
};

Process.prototype.checkOption = function (ButtonResult) {
	if (ButtonResult == "yes") {
		this.execute();
	}
	else this.terminate();
};

Process.prototype.isFirstStep = function () {
	return this.state == 0;
};

Process.prototype.isLastStep = function () {
	return this.state == this.numStates;
};

Process.prototype.gotoStep = function (state) {
	if (state > this.numStates) return;
	this.state = state - 1;
	this.execute();
};

Process.prototype.resetState = function () {
	this.state = 0;
};

Process.prototype.restart = function () {
	this.state = 0;
	this.execute();
};

Process.prototype.terminate = function () {
	this.state = this.numStates;
	this.execute();
};

Process.prototype.terminateOnSuccess = function () {
	this.success = true;
	this.terminate();
};

Process.prototype.terminateOnFailure = function (failureMessage) {
	if (failureMessage != null) this.failureMessage = failureMessage;
	this.success = false;
	this.terminate();
};

Process.prototype.getFailure = function () {
	return this.failureMessage;
};

Process.prototype.success = function () {
	return this.success;
};

Process.prototype.addRefreshTask = function (Type, Target, Sender) {
	if (this.refreshTaskList == null) this.refreshTaskList = new RefreshTaskList();
	var refreshTask = new RefreshTask(Type, Target);
	if (Sender != null) refreshTask.setSender(Sender);
	this.refreshTaskList.addRefreshTask(refreshTask);
};

Process.prototype.doRefreshTaskList = function () {
	if (this.refreshProcessClass == null) return true;
	if (this.refreshTaskList == null) return true;
	this.refreshProcess = new this.refreshProcessClass;
	this.refreshProcess.refreshTaskList = this.refreshTaskList;
	this.refreshProcess.execute();
};

Process.prototype.execute = function () {
	if (this.terminated) return;

	if (this.isLastStep()) {
		this.terminated = true;
		this.doRefreshTaskList();
		if (this.returnProcess) this.returnProcess.execute();
		return;
	}

	var State = this.getNextState();
	try {
		this.method = State.nextMethod;
		this.method();
	}
	catch (e) {
		Kernel.registerException(e);
	}

};

Process.prototype.getNextState = function () {
	if (this.state > this.numStates) return false;

	var state = this.sState;
	this.state++;


	var State = {
		iProgress: Math.round((state / this.numStates) * 100) / 100,
		nextMethod: this["step_" + this.state]
	};

	return State;
};

Process.prototype.onSuccess = function () {
	this.execute();
};

Process.prototype.generateMessage = function (sMessage, Variables) {
	var template = new Template(sMessage);
	return template.evaluate(Variables);
};

Process.prototype.getErrorMessage = function (sMessage, failureMessage) {
	failureMessage = (failureMessage != null) ? failureMessage.substr(failureMessage.indexOf(":") + 1) : "";
	return sMessage.replace("#response#", failureMessage);
};

Process.prototype.onFailure = function (failureMessage) {
	alert(failureMessage);
	this.terminate();
};

Process.prototype.getDashboard = function (modelData) {
	var dashboard = jQuery.parseJSON(this.data);

	var model = null;
	eval(dashboard.model);

	dashboard.model = model;

	return dashboard;
};

Process.prototype.getChartApi = function (chartApiData) {
	var ChartApi = null;
	eval(chartApiData);
	return ChartApi;
};

Process.prototype.updateColors = function () {
	var indicatorList = State.indicatorList;
	var compareList = State.compareList;
	var keys = [];
	var colors = [];
	var context = null;

	if (compareList.size() > 0 && indicatorList.size() == 1) {
		keys = compareList.getKeys();
		context = View.ColorContext.COMPARE;
	}
	else {
		keys = indicatorList.getKeys();
		context = View.ColorContext.INDICATORS;
	}

	for (var i = 0; i < keys.length; i++) {
		var color = View.getColor(context, keys[i]);
		colors.push(color);
	}

	State.colors = colors;
};