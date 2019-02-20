DialogMapLayer = function () {
	this.base = Dialog;
	this.base();
};

DialogMapLayer.prototype = new Dialog;

DialogMapLayer.prototype.render = function (DOMLayer) {
	var content = translate(AppTemplate.dialogmaplayer, Lang.DialogMapLayer);

	content = replaceAll(content, "${id}", this.id);

	this.DOMLayer = DOMLayer;
	this.DOMLayer.innerHTML = content;

	if (!this.target) return;

	if (this.target.left) {
		var jLayer = $(this.DOMLayer);
		jLayer.css("left", this.target.left);
	}

	CommandListener.capture(this.DOMLayer);

	this.init();
	this.refresh();
};

DialogMapLayer.prototype.init = function () {
	this.initBehaviours();
};

DialogMapLayer.prototype.initBehaviours = function () {
	var jLayer = $(this.DOMLayer);

	var jLayerPoint = jLayer.find(".layer." + Map.POINT + " a");
	jLayerPoint.click(DialogMapLayer.prototype.atLayerClick.bind(this, Map.POINT));

	var jLayerHeat = jLayer.find(".layer." + Map.HEAT + " a");
	jLayerHeat.click(DialogMapLayer.prototype.atLayerClick.bind(this, Map.HEAT));
};

DialogMapLayer.prototype.refresh = function (jsonData) {
	var jLayer = $(this.DOMLayer);

	jLayer.find(".layer").removeClass("active");

	var jActiveLayer = jLayer.find(".layer." + this.target.selected);
	jActiveLayer.addClass("active");
};

//************************************************************************************************************

DialogMapLayer.prototype.atLayerClick = function (layer) {
	if (this.onChange) this.onChange(layer);
};