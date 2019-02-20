CGDialogPrintEntity = function () {
	this.base = CGDialog;
	this.base("dlgPrintEntity");
	this.doHide = true;
	this.Reason = "";
	this.Attributes = new Array();
};

//------------------------------------------------------------------
CGDialogPrintEntity.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogPrintEntity.prototype.init = function (sLayerName) {
	if (!sLayerName) return;

	this.Layer = $(sLayerName);

	var html = AppTemplate.DialogPrintEntity;
	html = translate(html, Lang.DialogPrintEntity);

	var attributeTemplate = AppTemplate.DialogPrintEntityAttribute;
	attributeTemplate = translate(attributeTemplate, Lang.DialogPrintEntity);

	var dateAttributeTemplate = AppTemplate.DialogPrintEntityDateAttribute;
	dateAttributeTemplate = translate(dateAttributeTemplate, Lang.DialogPrintEntity);

	this.Layer.innerHTML = html;

	var extLayer = Ext.get(this.Layer);
	extLayer.on("click", this.atLayerClick, this);
	extLayer.select(".dialog.printentity").first().dom.addClassName(this.target.Mode);

	var extAccept = extLayer.select(".dialog.printentity .accept");
	extAccept.on("click", this.atAccept, this);

	this.autoAccept();
	this.initHeader(attributeTemplate);
	this.initRange(dateAttributeTemplate);
	this.update();
	this.checkAcceptEnable();
};

CGDialogPrintEntity.prototype.autoAccept = function (itemTemplate) {
    if (this.target.Mode == "pdf") return;
    if (!this.onAccept) return;
    if (this.existsRange()) return;
	this.onAccept();
};

CGDialogPrintEntity.prototype.initHeader = function (itemTemplate) {
	var extLayer = Ext.get(this.Layer);
	var extColumns = extLayer.select(".dialog.printentity .column");
	var template = new Ext.DomHelper.Template(itemTemplate);
	var attributes = this.target.Attributes;

	for (var i=0; i<extColumns.getCount(); i++) {
		var selected = this.Attributes[i];
		for (var attribute in attributes) {
			if (isFunction(attributes[attribute])) continue;
			attributes[attribute].selected = attributes[attribute].code == selected ? "selected" : "";
			template.append(extColumns.elements[i], attributes[attribute]);
		}
	}

	extColumns.on("change", CGDialogPrintEntity.prototype.atColumnChange.bind(this));
	extColumns.on("focus", this.atColumnFocus, this);
	extColumns.on("click", this.atColumnClick, this);
};

CGDialogPrintEntity.prototype.initRange = function (dateAttributeTemplate) {

	if (!this.existsRange()) {
		this.hideRange();
		return;
	}

	var extRange = Ext.get(this.Layer).select(".dialog.printentity td.range").first();
	if (this.RangeChecked)
		extRange.dom.removeClassName(CLASS_DISABLED);

	//Ext.get("dlgPrintEntity.range").on("change", CGDialogPrintEntity.prototype.atRangeClick.bind(this));

	this.initDateAttributes(dateAttributeTemplate);
	this.initFromDate();
	this.initToDate();
};

CGDialogPrintEntity.prototype.countDateAttributes = function () {
	var count = 0;
	var attributes = this.target.Attributes;
	for (var attribute in attributes) {
		if (isFunction(attributes[attribute])) continue;
		if (this.isDateAttribute(attributes[attribute]))
			count++;
	}
	return count;
};

CGDialogPrintEntity.prototype.hideRange = function () {
	var extLayer = Ext.get(this.Layer);
	var extRange = extLayer.select(".dialog.printentity .range").first();
	extRange.dom.style.display = "none";
};

CGDialogPrintEntity.prototype.initDateAttributes = function (dateAttributeTemplate) {
	var extLayer = Ext.get(this.Layer);
	var extDateAttributes = extLayer.select(".dialog.printentity .date-attributes").first();
	var template = new Ext.DomHelper.Template(dateAttributeTemplate);
	var attributes = this.target.Attributes;

	for (var attribute in attributes) {
		if (isFunction(attributes[attribute])) continue;
		if (!this.isDateAttribute(attributes[attribute])) continue;
		attributes[attribute].selected = attributes[attribute].code == this.DateAttribute ? "selected" : "";
		template.append(extDateAttributes.dom, attributes[attribute]);
	}

	extDateAttributes.on("change", CGDialogPrintEntity.prototype.atDateAttributeChange.bind(this));
};

CGDialogPrintEntity.prototype.isDateAttribute = function (attribute) {
	return attribute.type.toLowerCase() === "date";
};

CGDialogPrintEntity.prototype.initFromDate = function () {
	this.extFromDate = new Ext.form.DateField({
		format: 'd/m/Y',
		altFormats: 'd-m-y|d/m/y|d-m-Y|Y-m-d|Y/m/d|Y-m|Y/m|m-Y|m/Y|Y',
		minLength: 4
	});
	this.extFromDate.applyTo($("dlgPrintEntity.fromDate"));
	this.extFromDate.on('change', this.atFromDateChange, this);
	this.extFromDate.on('focus', this.atDateFocus, this);
	this.extFromDate.on('click', this.atDateFocus, this);

	if (this.FromDate != null)
		this.extFromDate.setValue(this.FromDate);
};

CGDialogPrintEntity.prototype.initToDate = function () {
	this.extToDate = new Ext.form.DateField({
		format: 'd/m/Y',
		altFormats: 'd-m-y|d/m/y|d-m-Y|Y-m-d|Y/m/d|Y-m|Y/m|m-Y|m/Y|Y',
		minLength: 4
	});
	this.extToDate.applyTo($("dlgPrintEntity.toDate"));
	this.extToDate.on('change', this.atToDateChange, this);
	this.extToDate.on('focus', this.atDateFocus, this);
	this.extToDate.on('click', this.atDateFocus, this);

	if (this.ToDate != null)
		this.extToDate.setValue(this.ToDate);
};

CGDialogPrintEntity.prototype.checkAcceptEnable = function () {
	if (this.target.Mode != "pdf") return true;

	var extLayer = Ext.get(this.Layer);
	var extAccept = extLayer.select(".dialog.printentity .accept").first();

	if (this.Attributes.length > 0) extAccept.removeClass("disabled");
	else extAccept.addClass("disabled");

	return !extAccept.hasClass("disabled");
};

CGDialogPrintEntity.prototype.hideDialogOnTimer = function () {
	if (!this.doHide) {
		this.doHide = true;
		return;
	}
	this.hideDialogTimer = window.setTimeout(this.hide.bind(this), 100);
};

CGDialogPrintEntity.prototype.clearHideDialogTimer = function () {
	window.clearTimeout(this.hideDialogTimer);
	return false;
};

CGDialogPrintEntity.prototype.show = function () {
	var extLayer = Ext.get(this.Layer);

	Ext.get(document.body).on("click", this.hideDialogOnTimer, this);
	extLayer.on("click", this.clearHideDialogTimer, this);

	extLayer.show();
};

CGDialogPrintEntity.prototype.hide = function () {
	var extLayer = Ext.get(this.Layer);
	Ext.get(document.body).un("click", this.hideDialogOnTimer, this);
	extLayer.hide();
	if (this.dialog != null)
		this.dialog.hide();
};

CGDialogPrintEntity.prototype.update = function () {
	this.updateHeader();
	this.updateRange();
};

CGDialogPrintEntity.prototype.updateHeader = function () {
	var extLayer = Ext.get(this.Layer);
	var extColumns = extLayer.select(".dialog.printentity .column");

	for (var i = 0; i < extColumns.getCount(); i++) {
		var extColumn = extColumns.elements[i];

		for (var j=0; j<extColumn.options.length; j++) {
			var domOption = extColumn.options[j]
			domOption.disabled = this.isAttributeSelected(domOption.value);
		}
	}
};

CGDialogPrintEntity.prototype.updateRange = function () {
	var extLayer = Ext.get(this.Layer);
	var extRange = extLayer.select(".dialog.printentity td.range").first();
	var extDateAttributes = extLayer.select(".dialog.printentity .date-attributes").first();
	var rangeDisabled = false;//extRange.dom.hasClassName(CLASS_DISABLED);

	//$("dlgPrintEntity.range").checked = !rangeDisabled;
	extDateAttributes.dom.disabled = false;//extRange.dom.hasClassName(CLASS_DISABLED);
	this.DateAttribute = rangeDisabled ? null : extDateAttributes.getValue();
	this.FromDate = rangeDisabled ? null : $("dlgPrintEntity.fromDate").value;
	this.ToDate = rangeDisabled ? null : $("dlgPrintEntity.toDate").value;
	this.RangeChecked = !rangeDisabled;

	if (!this.existsRange())
		return;

	if (rangeDisabled) {
		this.extFromDate.disable();
		this.extToDate.disable();
	}
	else {
		this.extFromDate.enable();
		this.extToDate.enable();
	}
};

CGDialogPrintEntity.prototype.existsRange = function () {
	return this.countDateAttributes() > 0 && !this.target.DisableRange;
};

//==================================================================
CGDialogPrintEntity.prototype.atAccept = function () {
	if (this.checkAcceptEnable() == false) {
		this.doHide = false;
		return;
	}
	if (this.onAccept) this.onAccept();
};

CGDialogPrintEntity.prototype.atCancel = function () {
	if (this.onCancel) this.onCancel();
};

CGDialogPrintEntity.prototype.atRangeClick = function () {
	var extLayer = Ext.get(this.Layer);
	var extRange = extLayer.select(".dialog.printentity td.range").first();
	extRange.dom.toggleClassName(CLASS_DISABLED);

	this.updateRange();
};

CGDialogPrintEntity.prototype.atColumnChange = function () {
	var extLayer = Ext.get(this.Layer);
	var extColumns = extLayer.select(".dialog.printentity .column");

	this.Attributes.clear();
	for (var i = 0; i < extColumns.getCount(); i++) {
		var extColumn = extColumns.elements[i];
		var value = extColumn.value;
		if (value == "_none") continue;
		this.Attributes.push(value);
	}

	this.checkAcceptEnable();
	this.update();

	this.doHide = false;
	return false;
};

CGDialogPrintEntity.prototype.isAttributeSelected = function(attribute) {
	for (var i=0; i<this.Attributes.length; i++)
		if (this.Attributes[i] == attribute)
			return true;
	return false;
}

CGDialogPrintEntity.prototype.atDateAttributeChange = function() {
	var extLayer = Ext.get(this.Layer);
	var extDateAttributes = extLayer.select(".dialog.printentity .date-attributes").first();
	this.DateAttribute = extDateAttributes.getValue();
	this.update();
};

CGDialogPrintEntity.prototype.atColumnFocus = function (event) {
	this.doHide = false;
};

CGDialogPrintEntity.prototype.atColumnClick = function (event) {
	this.doHide = false;
};

CGDialogPrintEntity.prototype.atDateFocus = function (event) {
	this.doHide = false;
};

CGDialogPrintEntity.prototype.atLayerClick = function () {
	this.doHide = false;
};

CGDialogPrintEntity.prototype.atFromDateChange = function (field, newValue, oldValue) {
	if (this.extToDate != null && this.extToDate.getValue() != "" && newValue > this.extToDate.getValue())
		this.extFromDate.setValue(oldValue);

	this.FromDate = $("dlgPrintEntity.fromDate").value;
};

CGDialogPrintEntity.prototype.atToDateChange = function (field, newValue, oldValue) {
	if (this.extFromDate != null && this.extFromDate.getValue() != "" && newValue < this.extFromDate.getValue())
		this.extToDate.setValue(oldValue);

	this.ToDate = $("dlgPrintEntity.toDate").value;
};