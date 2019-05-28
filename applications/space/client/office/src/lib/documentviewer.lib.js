var SELECTED = "selected";
var LOADED = "THUMB_LOADED";
var PAGE = "PAGE";
var TIME = "TIME";
var COUNT = "COUNT";
var CLEAR = "CLEAR";
var ACCEPT = "ACCEPT";
var PAGE_ITEM_DIV = ".pageItemImage";
var PAGE_ITEM_IMAGE = "";
var THUMB_ITEMS = "THUMB_ITEMS";
var THUMB_ITEM_IMAGE = ".thumbItemImage";

var DocumentViewerTemplates = {
	"THUMBS_VIEW": "<div class=\"thumbsView\"></div>",
	"THUMB_ITEM": "<div class=\"thumbItem\"><div class=\"thumbItemImage\">::PAGE::</div></div>",
	"PAGES_VIEW": "<div class=\"pagesView\"/>",
	"PAGE_ITEM": "<div class=\"pageItem\"><div class=\"pageItemImage\"></div></div>",
	"LABEL": "<div class=\"pagesLabel\"><div class=\"dialog title\"></div></div>",
	"LABEL_EDITABLE": "<div class=\"pagesLabel\"><div class=\"dialog\"><input type=\"text\" class=\"title\"/><a class=\"behaviour accept\" style=\"display:none;\">::ACCEPT::</a><a class=\"behaviour clear\" alt=\"::CLEAR::\"></a></div></div>"
};

var DocumentViewerLang = {
	es: {
		DocumentInProgress: "<span>El documento se está procesando, tiempo estimado para terminar ::TIME:: segundos.</span>",
		MorePages: "<div style='margin:0 auto;width:calc(100% - 20px);text-align:center;font-size:12pt;background:#ff6601;color:white;padding:5px 10px;border-radius:4px;'>El documento tiene ::COUNT:: páginas. Descargue el documento para ver las páginas restantes.</div>",
		Accept: "aceptar",
		Clear: "borrar"
	},
	en: {
		DocumentInProgress: "<span>Document generation is in progress, estimated time to finish is ::TIME:: seconds.</span>",
		MorePages: "<div style='margin:0 auto;width:calc(100% - 20px);text-align:center;font-size:12pt;background:#ff6601;color:white;padding:5px 10px;border-radius:4px;'>Document has ::COUNT:: pages. Download document to see all pages.</div>",
		Accept: "accept",
		Clear: "delete"
	}
};

var DocumentViewerMaxPages = 15;

var ThumbViewer = function (eThumb, item, sBaseUrl) {
	this.item = item;
	this.eControl = eThumb;
	this.selectedHandler = null;
	this.sBaseUrl = sBaseUrl;

	this.eThumbItemImage = this.eControl.child(THUMB_ITEM_IMAGE, false);
};
ThumbViewer.prototype = {

	getId: function () {
		return this.item.id;
	},
	isLoaded: function () {
		return this.eControl.hasClass(LOADED);
	},

	load: function () {
		if (!this.isLoaded()) {
			var img = this.eThumbItemImage.createChild({tag: "img", src: this.sBaseUrl + "&thumb=" + this.item.id + "&r=" + Math.random()});
			this.eThumbItemImage.appendChild(img.dom);
			img.on('click', this.onClick, this);
			this.eControl.addClass(LOADED);
		}
	},

	onClick: function () {
		if (this.selectedHandler != null)
			this.selectedHandler(this);
	},

	setSelected: function (selected) {
		if (selected) {
			this.eControl.addClass(SELECTED);
			if (this.outOfViewportHandler != null) this.outOfViewportHandler(this.eControl);
		}
		else
			this.eControl.removeClass(SELECTED);
	},

	setClickHandler: function (delegate) {
		this.selectedHandler = delegate;
	},

	setOutOfViewportHandler: function (delegate) {
		this.outOfViewportHandler = delegate;
	},

	dispose: function () {
		this.eControl = null;
	}
};

var PageViewer = function (eThumb, item, sBaseUrl) {
	this.item = item;
	this.label = null;
	this.labelEditable = false;
	this.eControl = eThumb;
	this.selectedHandler = null;
	this.fZoomFactor = 0.75;
	this.ePageItemImage = this.eControl.child(PAGE_ITEM_DIV, false);
	this.ePageItemImage.setStyle({ width: item.width * this.fZoomFactor + PX, height: item.height * this.fZoomFactor + PX});
	this.sBaseUrl = sBaseUrl;
};

PageViewer.prototype = {

	getId: function () {
		return this.item.id;
	},
	isLoaded: function () {
		return this.eControl.hasClass(LOADED);
	},

	load: function () {
		if (!this.isLoaded()) {
			var img = this.ePageItemImage.createChild({tag: "img", src: this.sBaseUrl + "&page=" + this.item.id + "&r=" + Math.random()});
			img.setStyle({ width: this.item.width * this.fZoomFactor + PX,
				height: this.item.height * this.fZoomFactor + PX});
			this.ePageImage = img;
			this.ePageImage.on("click", this.atPageClick, this);
			this.eControl.addClass(LOADED);
		}
	},

	setZoom: function (zoomFactor) {
		this.fZoomFactor = zoomFactor;
		this.ePageItemImage.setStyle({ width: this.item.width * this.fZoomFactor + PX,
			height: this.item.height * this.fZoomFactor + PX});
		if (this.ePageImage) {
			this.ePageImage.setStyle({ width: this.item.width * this.fZoomFactor + PX,
				height: this.item.height * this.fZoomFactor + PX});
		}
	},

	getWidth: function () {
		return this.item.width;
	},

	select: function (extContainer) {
		extContainer.scrollTo("t", extContainer.getScroll().top + this.eControl.getTop() - 5 - extContainer.getTop());
		this.load();
	},

	setClickHandler: function (delegate) {
		this.selectedHandler = delegate;
	},

	dispose: function () {
		this.eControl = this.ePageItemImage = this.ePageImage = null;
	},

	getPageTop: function () {
		return this.eControl.getTop();
	},

	getPageBottom: function () {
		return this.eControl.getBottom();
	},

	atPageClick: function () {
		if (this.onClick) this.onClick(this);
	}
};

var DocumentViewer = function (thumbsContainer, pagesContainer, buttonsContainer, sLang) {
	this.sLang = sLang ? sLang : "en";
	this.aThumbViewers = new Array();
	this.aPageViewers = new Array();
	this.extThumbContainer = null;
	this.extPageContainer = null;
	this.iSelectedPage = -1;
	this.bInitialized = false;

	this.thumbsContainer = thumbsContainer;
	this.pagesContainer = pagesContainer;
	this.buttonsContainer = buttonsContainer;

	this.fZoomFactor = 0.75;
	this.fZoomFactorIncr = 0.25;
};
DocumentViewer.prototype = {
	dispose: function () {
		for (var i = 0; i < this.aThumbViewers.length; i++)
			this.aThumbViewers[i].dispose();
		for (var i = 0; i < this.aPageViewers.length; i++)
			this.aPageViewers[i].dispose();
	},

	setDocumentId: function (sDocumentId) {
		this.sDocumentId = sDocumentId;
	},

	setDocumentLabel: function (label, editable) {
		this.label = label;
		this.labelEditable = editable;
	},

	showDocumentLabel: function () {
		this.labelVisible = true;
	},

	hideDocumentLabel: function () {
		this.labelVisible = false;
	},

	setBaseUrl: function (sBaseUrl) {
		this.sBaseUrl = sBaseUrl;
	},

	init: function () {

		if (!this.extCurrentPage) {
			this.extCurrentPage = new Ext.form.TextField({cls: "page"});
			this.extCurrentPage.on('change', this.onCurrentPageChanged, this);
			this.extCurrentPage.on('specialkey', this.onCurrentPageKeyPress, this);
			this.extToolbar = new Ext.Toolbar(this.buttonsContainer,
				[
					{ iconCls: "previous", handler: this.onGoToPrevious.createDelegate(this) },
					this.extCurrentPage,
					{ iconCls: "next", handler: this.onGoToNext.createDelegate(this) },
					'-',
					{ iconCls: "zoomIn", handler: this.onZoomIn.createDelegate(this) },
					{ iconCls: "zoomOut", handler: this.onZoomOut.createDelegate(this) }
				]
			);
		}
	},

	setViewport: function (extViewport) {
		this.extPageViewPort = extViewport;
	},

	onCurrentPageKeyPress: function (extTextField, event) {
		if (event.getKey() == Ext.EventObject.ENTER) {
			this.onCurrentPageChanged(extTextField, extTextField.getValue());
		}
	},

	onCurrentPageChanged: function (extTextField, newValue, oldValue) {
		var iValue = parseInt(newValue) - 1;
		if (iValue >= 0 && iValue < (this.aThumbViewers.length)) {
			this.changePage(iValue, true);
		}
	},

	updateLabel: function () {
		if (this.label != null) {
			var extPagesContainer = Ext.get(this.pagesContainer);
			var eDialog = extPagesContainer.select(".dialog").first();
			eDialog.setWidth(this.aPageViewers[0].getWidth() * this.fZoomFactor);
		}
	},

	updateAllPages: function () {
		var totalPages = this.aPageViewers.length;
		var iMaxWidth = -1;

		for (var i = 0; i < totalPages; i++) {
			this.aPageViewers[i].setZoom(this.fZoomFactor);
			var iCurrentWidth = this.aPageViewers[i].getWidth();
			if (iCurrentWidth > iMaxWidth) iMaxWidth = iCurrentWidth;
		}

		if ((totalPages > 0) && (this.fZoomFactor >= 1)) this.extPageContainer.setWidth((iMaxWidth * this.fZoomFactor) + this.extPageContainer.dom.style.paddingRight);

		this.aPageViewers[this.iSelectedPage].select(this.extPageContainer);
		this.checkPageLoad();
	},

	onZoomIn: function () {
		if (this.bInitialized) {
			this.fZoomFactor += this.fZoomFactorIncr;
			this.updateLabel();
			this.updateAllPages();
		}
	},

	onZoomOut: function () {
		if (this.bInitialized) {
			if (this.fZoomFactor - this.fZoomFactorIncr >= (this.fZoomFactorIncr * 2))
				this.fZoomFactor -= this.fZoomFactorIncr;
			this.updateLabel();
			this.updateAllPages();
		}
	},

	onGoToPrevious: function () {
		if (this.bInitialized)
			this.changePage(this.iSelectedPage - 1, true);
	},

	onGoToNext: function () {
		if (this.bInitialized)
			this.changePage(this.iSelectedPage + 1, true);
	},

	load: function () {
		this.init();

		Ext.Ajax.request({
			"url": this.sBaseUrl,
			"scope": this,
			"params": {
				"id": this.sDocumentId,
				"type": "JSON"
			},
			"method": "GET",
			"success": function (response, options) {
				this.onMetadataReceived(Ext.util.JSON.decode(response.responseText));
			},
			"failure": function (response, options) {
				alert(response.responseText);
			}
		});
	},

	onMetadataReceived: function (metadata) {
		var extPagesContainer = Ext.get(this.pagesContainer);

		extPagesContainer.dom.innerHTML = "";

		if (!metadata.hasPendingOperations) {
			var numberOfPages = metadata.numberOfPages;
			var sUrlSeparator = this.sBaseUrl.indexOf("?") ? "&" : "?";
			var sUrl = this.sBaseUrl + sUrlSeparator + "id=" + this.sDocumentId;

			this.extThumbContainer = Ext.get(this.thumbsContainer).insertHtml("beforeEnd", DocumentViewerTemplates.THUMBS_VIEW, true);
			this.extPageContainer = Ext.get(this.pagesContainer);

			for (var i = 1; i <= DocumentViewerMaxPages; i++) {
				var ePageControl = this.extPageContainer.insertHtml("beforeEnd", DocumentViewerTemplates.PAGE_ITEM, true);
				var oPageViewer = new PageViewer(ePageControl, metadata.pages[i], sUrl);

				oPageViewer.onClick = this.atPageClick.bind(this);
				this.aPageViewers.push(oPageViewer);

				var eThumbControl = this.extThumbContainer.insertHtml("beforeEnd",
					DocumentViewerTemplates.THUMB_ITEM.replace(TEMPLATE_SEPARATOR + PAGE + TEMPLATE_SEPARATOR, i),
					true);
				var oThumbViewer = new ThumbViewer(eThumbControl, metadata.pages[i], sUrl);
				oThumbViewer.setClickHandler(this.onThumbSelected.createDelegate(this));
				oThumbViewer.setOutOfViewportHandler(this.onThumbOutOfViewport.createDelegate(this));
				oThumbViewer.load();
				this.aThumbViewers.push(oThumbViewer);

				var width = 0;
				if (i != 1) width = this.extThumbContainer.getWidth();
				this.extThumbContainer.setWidth(width + eThumbControl.getWidth() + 20);

				if (i == 1) {
					this.setSelectedPage(0);
					this.aThumbViewers[this.iSelectedPage].setSelected(true);
					this.aPageViewers[this.iSelectedPage].load();
				}
			}

			this.bInitialized = numberOfPages > 0;

			if (this.label != null) {
				var template = this.labelEditable ? DocumentViewerTemplates.LABEL_EDITABLE : DocumentViewerTemplates.LABEL;
				var eLabel = extPagesContainer.insertHtml("afterBegin", template.replace(TEMPLATE_SEPARATOR + CLEAR + TEMPLATE_SEPARATOR, DocumentViewerLang[this.sLang].Clear).replace(TEMPLATE_SEPARATOR + ACCEPT + TEMPLATE_SEPARATOR, DocumentViewerLang[this.sLang].Accept), true);
				var ePagesLabel = extPagesContainer.select(".pagesLabel").first();
				var eDialog = eLabel.select(".dialog").first();
				eDialog.setWidth(metadata.pages[1].width * this.fZoomFactor);
				var eTitle = eLabel.select(".title").first();

				if (this.labelEditable) {
					eTitle.dom.value = this.label;
					eTitle.on("keyup", this.atLabelKeyUp, this);
					eTitle.on("focus", this.atLabelFocus, this);
					eTitle.on("blur", this.atLabelBlur, this);
					var eClear = eDialog.select(".clear").first();
					eClear.on("click", this.atClear, this);
					var eAccept = eDialog.select(".accept").first();
					eAccept.on("click", this.renameLabel, this);
				}
				else {
					//eTitle.dom.innerHTML = this.label;
					eTitle.dom.style.display = "none";
				}

				if (this.labelVisible)
					ePagesLabel.dom.style.display = "block";
				else
					ePagesLabel.dom.style.display = "none";
			}

			this.renderMorePagesMessage(numberOfPages, extPagesContainer);

		} else {
			extPagesContainer.insertHtml("beforeEnd",
				DocumentViewerLang[this.sLang].DocumentInProgress
					.replace(TEMPLATE_SEPARATOR + TIME + TEMPLATE_SEPARATOR,
						metadata.estimatedTimeToFinish));
			setTimeout(this.load.bind(this), metadata.estimatedTimeToFinish / 2 * 1000);
		}

	},

	renderMorePagesMessage : function(numberOfPages, extPagesContainer) {
		if (numberOfPages <= DocumentViewerMaxPages) return;
		extPagesContainer.insertHtml("beforeEnd", DocumentViewerLang[this.sLang].MorePages.replace(TEMPLATE_SEPARATOR + COUNT + TEMPLATE_SEPARATOR, numberOfPages));
	},

	renameLabel: function (event) {
		var extPagesContainer = Ext.get(this.pagesContainer);
		var eTitle = extPagesContainer.select(".dialog .title").first();
		if (eTitle.dom.value == "") {
			window.clearTimeout(this.blurTimeout);
			return;
		}
		if (this.onDocumentLabelChange) this.onDocumentLabelChange(eTitle.dom.value);
	},

	atLabelKeyUp: function (event) {
		var codeKey = event.getKey();
		if (codeKey == event.ENTER) this.renameLabel();

		var extPagesContainer = Ext.get(this.pagesContainer);
		var eTitle = extPagesContainer.select(".dialog .title").first();
		var eClear = extPagesContainer.select(".dialog .clear").first();
		eClear.dom.style.display = (eTitle.dom.value != "") ? "block" : "none";
	},

	atLabelFocus: function (event) {
		var extPagesContainer = Ext.get(this.pagesContainer);
		var eTitle = extPagesContainer.select(".dialog .title").first();
		var eAccept = extPagesContainer.select(".dialog .accept").first();
		eTitle.addClass("active");
		eAccept.dom.style.display = "block";
	},

	atPageClick: function (pageViewer) {
		if (this.onDocumentClick) this.onDocumentClick();
	},

	blurLabel: function () {
		var extPagesContainer = Ext.get(this.pagesContainer);
		var eTitle = extPagesContainer.select(".dialog .title").first();
		var eAccept = extPagesContainer.select(".dialog .accept").first();
		eTitle.removeClass("active");
		eAccept.dom.style.display = "none";
	},

	atLabelBlur: function (event) {
		this.blurTimeout = window.setTimeout(this.blurLabel.bind(this), 100);
	},

	atClear: function () {
		var extPagesContainer = Ext.get(this.pagesContainer);
		var eTitle = extPagesContainer.select(".dialog .title").first();
		var eClear = extPagesContainer.select(".dialog .clear").first();
		eTitle.dom.value = "";
		eClear.dom.style.display = "none";
	},

	onScrollPageView: function (event, obj) {
		var viewer = this.aPageViewers[this.iSelectedPage];
		var iTop = viewer.getPageTop() - 10;
		var iBottom = viewer.getPageBottom();

		var viewport = this.extPageViewPort.getHeight();
		var halfViewport = viewport / 2;
		var dif = 0;

		if (iTop > halfViewport)
			dif = -1;
		else if (iBottom < halfViewport)
			dif = 1;

		if (dif != 0) {
			this.changePage(this.iSelectedPage + dif);
		} else {
			this.checkPageLoad();
		}
	},

	checkPageLoad: function () {
		var viewport = this.extPageViewPort.getHeight();
		var viewer;
		if (this.iSelectedPage > 0) {
			viewer = this.aPageViewers[this.iSelectedPage - 1];
			if (viewer.getPageBottom() > this.extPageViewPort.getTop())
				viewer.load();
		}

		if (this.iSelectedPage < this.aPageViewers.length - 1) {
			viewer = this.aPageViewers[this.iSelectedPage + 1];
			if (viewer.getPageTop() < viewport + this.extPageViewPort.getTop())
				viewer.load();
		}
	},

	changePage: function (newPage, bSelectPageView) {
		if (newPage >= 0 && newPage < (this.aThumbViewers.length) && this.iSelectedPage != newPage) {
			this.aThumbViewers[this.iSelectedPage].setSelected(false);
			this.setSelectedPage(newPage);
			var viewer = this.aPageViewers[this.iSelectedPage];
			viewer.load();
			if (bSelectPageView)
				viewer.select(this.extPageViewPort);
			this.aThumbViewers[this.iSelectedPage].setSelected(true);
		}
	},

	setSelectedPage: function (iNewValue) {
		this.iSelectedPage = iNewValue;
		this.extCurrentPage.setValue(this.iSelectedPage + 1);
	},

	onThumbSelected: function (thumbViewer) {
		var iNewPage = thumbViewer.getId() - 1;
		if (this.iSelectedPage != iNewPage) {
			thumbViewer.setSelected(true);
			this.aThumbViewers[this.iSelectedPage].setSelected(false);
			this.setSelectedPage(iNewPage);
		}

		this.aPageViewers[this.iSelectedPage].select(this.extPageViewPort, false);
	},

	onThumbOutOfViewport: function (thumbViewer) {
		this.extThumbContainer.dom.scrollLeft = thumbViewer.dom.offsetLeft;
	}
};