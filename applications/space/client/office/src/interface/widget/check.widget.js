CGWidgetCheck = function (extWidget) {
	this.base = CGWidget;
	this.base(extWidget);
	this.aChecks = new Array();
	this.extCheckList = this.extWidget.down(CSS_WIDGET_ELEMENT_CHECK_LIST);
	this.registerChecks();
	this.sources = null;
	this.extWidget.dom.focus = CGWidgetCheck.prototype.focus.bind(this);
};

CGWidgetCheck.prototype = new CGWidget;

CGWidgetCheck.prototype.applyBehaviours = function () {
	this.setId(this.extWidget.dom.id);
};

CGWidgetCheck.prototype.createOptions = function () {
};

CGWidgetCheck.prototype.clearChecks = function () {
	this.aChecks = new Array();
	var aCheckList = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK);
	aCheckList.each(function (extCheck) {
		this.unregisterCheck(extCheck);
	}, this);
	this.extCheckList.dom.innerHTML = "";
};

CGWidgetCheck.prototype.registerCheck = function (extCheck) {
	var idChecker = Ext.id();
	var extChecker = extCheck.down(CSS_WIDGET_ELEMENT_CHECKER);
	var extLabel = extCheck.down("label");

	extChecker.dom.id = idChecker;
	extLabel.dom.htmlFor = idChecker;

	Event.observe(extChecker.dom, "click", CGWidgetCheck.prototype.atCheckerClick.bind(this, extCheck.dom, extChecker.dom));
	Event.observe(extCheck.dom, "mouseover", CGWidgetCheck.prototype.atCheckMouseOver.bind(this, extCheck.dom, extChecker.dom));
	Event.observe(extCheck.dom, "mouseout", CGWidgetCheck.prototype.atCheckMouseOut.bind(this, extCheck.dom, extChecker.dom));

	var extCheckAll = extCheck.down(CSS_WIDGET_ELEMENT_CHECKALL);
	if (extCheckAll) Event.observe(extCheckAll.dom, "click", CGWidgetCheck.prototype.atCheckAllClick.bind(this, extCheck.dom, extChecker.dom));

	var extUncheckAll = extCheck.down(CSS_WIDGET_ELEMENT_UNCHECKALL);
	if (extUncheckAll) Event.observe(extUncheckAll.dom, "click", CGWidgetCheck.prototype.atUncheckAllClick.bind(this, extCheck.dom, extChecker.dom));
};

CGWidgetCheck.prototype.registerChecks = function () {
	var aCheckList = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK);
	var pos = 0;
	aCheckList.each(function (extCheck) {
		extCheck.dom.pos = pos;
		this.registerCheck(extCheck);
		pos++;
	}, this);
	Event.observe(this.extCheckList.dom, "blur", CGWidgetCheck.prototype.blur.bind(this));
};

CGWidgetCheck.prototype.unregisterCheck = function (extCheck) {
	var extChecker = extCheck.down(CSS_WIDGET_ELEMENT_CHECKER);
	Event.stopObserving(extChecker.dom, "click", CGWidgetCheck.prototype.atCheckerClick.bind(this, extCheck.dom, extChecker.dom));
};

CGWidgetCheck.prototype.unregisterChecks = function () {
	var extCheckList = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK);
	extCheckList.each(function (extCheck) {
		this.unregisterCheck(extCheck);
	}, this);
};

CGWidgetCheck.prototype.init = function () {
	this.initStores();
	this.bIsReady = true;
};

CGWidgetCheck.prototype.destroyBehaviours = function () {
	if (!this.extWidget) return;
	this.unregisterChecks();
};

CGWidgetCheck.prototype.getData = function () {
	if (!this.Target) return null;
	if (!this.Target.getCode) return null;

	var Result = new Object();
	Result.code = this.Target.getCode();
	Result.order = -1;
	Result.value = new Array();
	Result.value.push({code: CGIndicator.SOURCE, order: 1, value: this.SourceId});
	Result.value.push({code: CGIndicator.FROM, order: 2, value: this.Target.getStoreFromValue()});
	if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 3, value: this.extSuper.dom.value});
	Result.checks = this.aChecks;

	Result.toXml = function () {
		var Attribute = new CGAttribute();
		var AttributeList = Attribute.getAttributeList();

		Attribute.code = this.code;
		Attribute.iOrder = this.order;

		for (var i = 0; i < this.checks.length; i++) {
			var OptionAttribute = new CGAttribute();
			OptionAttribute.code = CGAttribute.OPTION;
			OptionAttribute.iOrder = i;
			OptionAttribute.addIndicatorByValue(CGIndicator.CHECKED, 0, (this.checks[i].checked) ? "true" : "false");
			OptionAttribute.addIndicatorByValue(CGIndicator.CODE, 1, this.checks[i].code);
			OptionAttribute.addIndicatorByValue(CGIndicator.VALUE, 2, this.checks[i].value);
			AttributeList.addAttribute(OptionAttribute);
		}

		for (var i = 0; i < this.value.length; i++) {
			Attribute.addIndicatorByValue(this.value[i].code, this.value[i].order, this.value[i].value);
		}

		return Attribute.serialize();
	};

	return Result;
};

CGWidgetCheck.prototype.getCheckedValueInCollection = function (attributes, code) {

	for (var i=0; i<attributes.length; i++) {
		var optionAttribute = attributes[i];
		var attributeCode = optionAttribute.getIndicatorValue(CGIndicator.CODE);
		if (code == attributeCode) {
			var checkedValue = optionAttribute.getIndicatorValue(CGIndicator.CHECKED);
			return (checkedValue == "true" || checkedValue == "yes");
		}
	}

	return false;
};

CGWidgetCheck.prototype.setData = function (sData) {
	if (!this.Target) return;
	if (sData == "") this.setDataCallback(sData);
	else this.reload(CGWidgetCheck.prototype.setDataCallback.bind(this, sData));
};

CGWidgetCheck.prototype.setDataCallback = function (sData) {
	var aAttributes, extCheckItems;
	var Attribute = new CGAttribute();

	Attribute.unserialize(sData);
	aAttributes = Attribute.getAttributeList().getAttributes();
	extCheckItems = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK);
	this.aChecks = new Array();

	for (var i = 0; i < extCheckItems.elements.length; i++) {
		var extCheck = Ext.get(extCheckItems.elements[i]);
		var Check = new Object();
		var extInput = extCheck.down("input");
		var extChecker = extCheck.down(CSS_WIDGET_ELEMENT_CHECKER);
		Check.checked = this.getCheckedValueInCollection(aAttributes, extChecker.dom.name);
		Check.code = extInput.dom.name;
		Check.value = extInput.dom.value;
		extCheckItems.elements[i].down("input").checked = Check.checked;
		this.aChecks[i] = Check;
	}

	this.validate();

	if (this.onChange) this.onChange();
	if (this.onRefresh) this.onRefresh();
};

CGWidgetCheck.prototype.setTarget = function (Target) {
	this.Target = Target;

	this.createRequiredWidget();

	var aCheckList = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK);
	aCheckList.each(function (extCheck) {
		var extChecker = extCheck.down(CSS_WIDGET_ELEMENT_CHECKER);
		var Check = {checked: extChecker.dom.checked, code: extChecker.dom.name, value: extChecker.dom.value};
		this.aChecks.push(Check);
		this.registerCheck(extCheck);
	}, this);

	this.SourceId = this.extCheckList.dom.type;
	this.sources = this.Target.getSources();

	this.createOptions();
	this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
	this.validate();
	//this.updateData();
};

CGWidgetCheck.prototype.lock = function () {
	if (this.extWidget) {
		if (this.extWidget.hasClass(CLASS_FOCUS)) this.extWidget.dom.blur();
		this.extWidget.addClass(CLASS_LOCKED);
	}
	var extCheckItems = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK);
	extCheckItems.each(function (extCheck) {
		extCheck.down("input").dom.disabled = true;
	}, this);
};

CGWidgetCheck.prototype.unLock = function () {
	if (this.Target.isLockedByDefinition()) return;
	if (this.extWidget) this.extWidget.removeClass(CLASS_LOCKED);
	var extCheckItems = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK);
	extCheckItems.each(function (extCheck) {
		extCheck.down("input").dom.disabled = false;
	}, this);
	if (this.onUnLock) this.onUnLock();
};

CGWidgetCheck.prototype.save = function () {
	if (this.saveTimeout != null) window.clearTimeout(this.saveTimeout);
	this.validate();
	this.updateData();
};

CGWidgetCheck.prototype.showReloading = function () {
	this.extCheckList.dom.innerHTML = WidgetWaitingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'cls': CLASS_EDITOR_WAITING_MESSAGE, 'message': Lang.Editor.Dialogs.CheckReload.Reloading});
	this.setData("");
};

CGWidgetCheck.prototype.showReloadingError = function () {
	this.extCheckList.dom.innerHTML = WidgetWaitingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'cls': CLASS_EDITOR_WAITING_ERROR, 'message': Lang.Editor.Dialogs.CheckReload.ReloadingFailed});
};

CGWidgetCheck.prototype.reload = function (callback) {
	var reloadSourceUrl = WidgetTemplateCheckReloadSourceUrl.evaluate({'api': Context.Config.Api, 'id': this.Target.getNodeId(), 'source': this.SourceId, 'field': this.Target.getCode(), 'fieldId': this.Target.getAbsoluteCode(), 'from': this.Target.getStoreFromValue()});
	this.showReloading();

	Ext.Ajax.request({
		url: reloadSourceUrl,
		method: "POST",
		success: function (response, options) {
			var result = null;
			eval("result = " + response.responseText);
			this.extCheckList.dom.innerHTML = result.view;
			this.registerChecks();

			if (callback) callback();
			else this.setData(result.data);
		},
		failure: function (response, options) {
			this.showReloadingError();
		},
		scope: this
	});
};

CGWidgetCheck.prototype.focus = function () {
	this.bFocused = true;
	this.extWidget.addClass(CLASS_FOCUS);
	this.atFocused(null);
};

CGWidgetCheck.prototype.reset = function () {
	this.updateData();
	this.reload();
};

// #############################################################################################################

CGWidgetCheck.prototype.atFocused = function (oEvent) {
	if (!this.Target) return;
	if (!this.isReady()) this.init();
	if (this.onFocused) this.onFocused();

	this.Editor.show();
	this.Editor.setConfiguration({Field: this.Target, Dialogs: [
		{sName: HISTORY, Store: this.aStores[HISTORY]},
		{sName: SOURCE, SelectedSourceId: this.SourceId, Sources: this.sources}
	]});
	this.Editor.onSelectSource = this.atSelectSource.bind(this);
	this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
	this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
	this.Editor.refresh();

	if (this.isLocked()) this.Editor.lock();
	else this.Editor.unLock();

	if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
	if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};

CGWidgetCheck.prototype.atSelectSource = function (sourceId) {
	this.SourceId = sourceId;
	this.validate();
	this.updateData();
	this.reload();
};

CGWidgetCheck.prototype.atCheckerClick = function (DOMCheck, DOMChecker) {
	this.focus();

	var checkPos = DOMCheck.pos;
	if (!this.aChecks[checkPos]) return;
	this.aChecks[checkPos].checked = DOMChecker.checked;

	if (this.saveTimeout) window.clearTimeout(this.saveTimeout);
	this.saveTimeout = window.setTimeout(CGWidgetCheck.prototype.save.bind(this), 500);

	this.Editor.setConfiguration({Field: this.Target, Dialogs: [
		{sName: HISTORY, Store: this.aStores[HISTORY]},
		{sName: SOURCE, SelectedSourceId: this.SourceId, Sources: this.sources}
	], Checked: DOMChecker.checked});
	this.Editor.refresh();
};

CGWidgetCheck.prototype.atCheckMouseOver = function (DOMCheck) {
	var extCheckOptions = Ext.get(DOMCheck).down("span");
	if (extCheckOptions) extCheckOptions.dom.style.display = "inline";
};

CGWidgetCheck.prototype.atCheckMouseOut = function (DOMCheck) {
	var extCheckOptions = Ext.get(DOMCheck).down("span");
	if (extCheckOptions) extCheckOptions.dom.style.display = "none";
};

CGWidgetCheck.prototype.atCheckAllClick = function (DOMCheck, DOMChecker) {
	this.focus();

	var aExtCheckList = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK + ".parent_" + DOMChecker.name);
	aExtCheckList.each(function (extCheck) {
		var checkPos = extCheck.dom.pos;
		var extChecker = extCheck.down(CSS_WIDGET_ELEMENT_CHECKER);
		extChecker.dom.checked = true;
		if (!this.aChecks[checkPos]) return;
		this.aChecks[checkPos].checked = extChecker.dom.checked;
	}, this);

	if (this.saveTimeout) window.clearTimeout(this.saveTimeout);
	this.saveTimeout = window.setTimeout(CGWidgetCheck.prototype.save.bind(this), 500);

	this.Editor.setConfiguration({Field: this.Target, Dialogs: [
		{sName: HISTORY, Store: this.aStores[HISTORY]}
	], Checked: DOMChecker.checked});
	this.Editor.refresh();
};

CGWidgetCheck.prototype.atUncheckAllClick = function (DOMCheck, DOMChecker) {
	this.focus();

	var aExtCheckList = this.extCheckList.select(CSS_WIDGET_ELEMENT_CHECK + ".parent_" + DOMChecker.name);
	aExtCheckList.each(function (extCheck) {
		var checkPos = extCheck.dom.pos;
		var extChecker = extCheck.down(CSS_WIDGET_ELEMENT_CHECKER);
		extChecker.dom.checked = false;
		if (!this.aChecks[checkPos]) return;
		this.aChecks[checkPos].checked = extChecker.dom.checked;
	}, this);

	if (this.saveTimeout) window.clearTimeout(this.saveTimeout);
	this.saveTimeout = window.setTimeout(CGWidgetCheck.prototype.save.bind(this), 500);

	this.Editor.setConfiguration({Field: this.Target, Dialogs: [
		{sName: HISTORY, Store: this.aStores[HISTORY]}
	], Checked: DOMChecker.checked});
	this.Editor.refresh();
};