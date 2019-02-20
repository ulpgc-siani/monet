CGWidgetDate = function (extWidget) {
	this.base = CGWidget;
	this.base(extWidget);
	this.Format = null;
};

CGWidgetDate.prototype = new CGWidget;

CGWidgetDate.prototype.getValue = function () {
	var dtInternalDate = Date.parseDate(this.extValue.dom.value, Date.getPattern(this.Format));
	return (dtInternalDate) ? dtInternalDate.format(DATE_FORMAT_INTERNAL) : "";
};

CGWidgetDate.prototype.getData = function () {
	if (!this.Target) return null;
	if (!this.Target.getCode) return null;
	if (!this.extValue) return null;

	var dtInternalDate = Date.parseDate(this.extValue.dom.value, Date.getPattern(this.Format));
	var sInternalDate = (dtInternalDate) ? dtInternalDate.format(DATE_FORMAT_INTERNAL) : "";

	var Result = new Object();
	Result.code = this.Target.getCode();
	Result.order = -1;
	Result.value = new Array();
	Result.value.push({code: CGIndicator.VALUE, order: 1, value: this.extValue.dom.value});
	Result.value.push({code: CGIndicator.INTERNAL, order: 3, value: sInternalDate});
	if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 3, value: this.extSuper.dom.value});

	Result.toXml = function () {
		var Attribute = new CGAttribute();
		Attribute.code = this.code;
		Attribute.iOrder = this.order;
		for (var iPos = 0; iPos < this.value.length; iPos++) {
			Attribute.addIndicatorByValue(this.value[iPos].code, this.value[iPos].order, this.value[iPos].value);
		}
		return Attribute.serialize();
	};

	return Result;
};

CGWidgetDate.prototype.validate = function () {
	var Pattern, dtDate;
	var valid = true;

	if (this.WidgetRequired) {
		if (this.extValue.dom.value != "") this.WidgetRequired.hide();
		else this.WidgetRequired.show();
	}

	if (this.extValue.dom.value == "") {
		this.extValue.removeClass(CLASS_WRONG);
		this.Editor.hideValidationError(VALIDATION_ERROR_FORMAT);
		return true;
	}

	Pattern = Date.getPattern(this.Format);
	dtDate = Date.parseDate(this.extValue.dom.value, Pattern);

	if (dtDate) {
		this.extValue.dom.value = dtDate.format(Pattern);
		this.extValue.removeClass(CLASS_WRONG);
		this.Editor.hideValidationError(VALIDATION_ERROR_FORMAT);
	}
	else {
		this.extValue.addClass(CLASS_WRONG);
		this.Editor.showValidationError(VALIDATION_ERROR_FORMAT);
		valid = false;
	}

	if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

	return valid;
};

CGWidgetDate.prototype.setTarget = function (Target) {
	this.Target = Target;

	this.createRequiredWidget();

	this.Format = this.Target.getFormat();
	if (this.Format == null) this.Format = "iso8601short";
	this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());

	var value = this.extValue.dom.value;
	if (this.Target.isSequential() && value == "") {
		var dtNow = new Date();
		value = dtNow.format(Date.getPattern(this.Format));
	}

	try {
		var dtInternalDate = Date.parseDate(value, DATE_FORMAT_INTERNAL);
		this.extValue.dom.value = (dtInternalDate != null) ? dtInternalDate.format(Date.getPattern(this.Format)) : "";
	}
	catch (e) {
	}

	this.createOptions();
	this.validate();
	//this.updateData();
};

CGWidgetDate.prototype.clear = function () {
	this.extValue.dom.value = "";
	this.extValue.removeClass(CLASS_WRONG);
};

// #############################################################################################################

CGWidgetDate.prototype.atFocused = function () {
	if (!this.Target) return;
	if (!this.isReady()) this.init();
	if (this.onFocused) this.onFocused();

	this.Editor.show();
	this.Editor.setConfiguration({Field: this.Target, Dialogs: [
		{sName: DATE, Format: this.Format, Edition: this.Target.getEdition()}
	]});
	this.Editor.setData({code: null, value: this.extValue.dom.value});
	this.Editor.onSelect = this.atSelect.bind(this);
	this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
	this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
	this.Editor.onClearValue = this.atClearValue.bind(this);
	this.Editor.refresh();

	if (this.isLocked()) this.Editor.lock();
	else this.Editor.unLock();

	this.validate();
	this.extValue.addClass(CLASS_FOCUS);
	this.extValue.dom.select();

	if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
	if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};

CGWidgetDate.prototype.atSelect = function (Data) {
	this.extValue.dom.value = Data.value;
	this.showClearValue();
	this.validate();
	this.updateData();
	if (this.onSelect) this.onSelect(Data);
};

CGWidgetDate.prototype.atKeyUp = function (oEvent) {
	var codeKey = oEvent.getKey();

	this.validate();

	if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
	else if ((codeKey == oEvent.ENTER) || (codeKey == oEvent.TAB)) {
		var Data = this.Editor.getData();
		if (Data) this.select(Data);
		if ((codeKey == oEvent.ENTER) && (this.onEnter)) this.onEnter();
	}
	else if (codeKey == oEvent.DOWN) this.Editor.moveDown(this.extValue.dom);
	else if (codeKey == oEvent.UP) this.Editor.moveUp(this.extValue.dom);
	else if (this.onKeyPress) this.onKeyPress(this.extValue.dom.value, codeKey);

	if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

	if (this.extValue.dom.value != "") this.showClearValue();
	else this.hideClearValue();

	Event.stop(oEvent);
	return false;
};



CGWidgetDate.prototype.atChange = function (oEvent) {
	if (!this.validate())
		this.clear();

	this.updateData();
};