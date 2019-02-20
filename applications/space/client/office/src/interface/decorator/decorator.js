CSS_TABS = ".tabs";
CSS_TABS_DEFAULT = ".default";
CSS_TAB = ".tab";
TEMPLATE_TAB_ID = "#{id}_#{idtab}";

CGDecorator = function () {
};

CGDecorator.prototype.addCommonMethods = function (DOMElement) {

	DOMElement.extTabPanel = null;
	DOMElement.cssElementStyle = null;

	DOMElement.getTabId = function (IdTab) {
		var TemplateTabId = new Template(TEMPLATE_TAB_ID);
		return TemplateTabId.evaluate({'id': this.getId(), 'idtab': IdTab});
	};

	DOMElement.getTabCode = function (IdTab) {
		return IdTab.substr(IdTab.indexOf("_")+1);
	};

	DOMElement.hasTabs = function () {
		return this.extTabPanel != null;
	};

	DOMElement.initTabs = function (cssElementStyle) {
		var extElement = Ext.get(this);
		var extTabList = extElement.select(CSS_TAB);

		if (this.extTabPanel == null) {
			this.extTabPanel = extElement.select(CSS_TABS).first();
			if (this.extTabPanel == null) {
				this.executeOnloadCommandsAndRefreshBars(extElement, cssElementStyle);
				return;
			}
			this.extTabPanel = new Ext.TabPanel(this.extTabPanel);
		}

		extTabList.each(function (extTab) {
			var idTab = extTab.dom.id;
			extTab.dom.id = this.getTabId(idTab);
			var extLabel = extTab.select(CSS_LABEL).first();
			var extVisible = extTab.select(CSS_VISIBLE).first();
			var sLabel = (extLabel != null) ? extLabel.dom.innerHTML : "";
			var extTabPanelItem = this.extTabPanel.addTab(extTab.dom.id, (sLabel != "") ? sLabel : Lang.NoLabel);
			extTabPanelItem.IdTab = idTab;
			extTabPanelItem.on("activate", DOMElement.atPanelItemActivated.bind(this));

			if (extVisible != null && extVisible.dom.innerHTML == "false")
				this.hideTab(idTab);

		}, this);

		this.cssElementStyle = cssElementStyle;
		this.resize();
		Ext.EventManager.onWindowResize(DOMElement.atWindowResize.bind(this));
		//this.activateDefaultTab();
	};

	DOMElement.initToolbar = function (css) {
		if (!DOMElement.isPage()) return;

		var extElement = Ext.get(DOMElement);
		var extToolbar = extElement.down(css);
		this.viewerToolbar = new CGViewerToolbar();
		this.viewerToolbar.init();
		this.viewerToolbar.setTarget(this.getConfiguration().toolbar);
		this.viewerToolbar.render(extToolbar);
	};

	DOMElement.activateDefaultTab = function () {
		if (this.extTabPanel == null) return;
		var extDefault = this.extTabPanel.el.select(CSS_TABS_DEFAULT).first();
		this.activateTab(extDefault.dom.innerHTML);
	};

	DOMElement.resize = function () {
		var extLayerList = this.extTabPanel.bodyEl.select(".x-tabs-item-body");
		var extElement = Ext.get(this);
		var extTabs = extElement.up(".x-tabs-body.x-layout-tabs-body");
		var iHeight = (extTabs != null) ? extTabs.getHeight() : 0;
		var iOffsetHeight = extElement.getHeight() - this.extTabPanel.el.getHeight();

		extLayerList.each(function (extLayer) {
			extLayer.dom.style.height = (iHeight - iOffsetHeight - 55) + "px";
			extLayer.dom.style.overflow = "auto";
		}, this);
	};

	DOMElement.getActiveTab = function () {
		var ActiveTab;

		if (!this.extTabPanel) return null;

		ActiveTab = this.extTabPanel.getActiveTab();
		if (!ActiveTab) return null;

		return ActiveTab.IdTab;
	};

	DOMElement.existsTab = function (IdTab) {
		if (!this.extTabPanel) return;
		return this.extTabPanel.getTab(this.getTabId(IdTab)) != null;
	};

	DOMElement.activateTab = function (IdTab) {
		if (!this.extTabPanel) return;
		if (!this.existsTab(IdTab)) return;
		if (this.getActiveTab() != IdTab) this.extTabPanel.activate(this.getTabId(IdTab));
		else {
			var extTabPanelItem = this.extTabPanel.getTab(this.getTabId(IdTab));
			this.refreshPanelItem(extTabPanelItem);
		}
	};

	DOMElement.getTabsCodes = function () {
		var extElement = Ext.get(this);
		var extTabList = extElement.select(CSS_TAB);
		var result = new Array();

		extTabList.each(function (extTab) {
			result.push(this.getTabCode(extTab.dom.id));
		}, this);

		return result;
	};

	DOMElement.showTab = function (IdTab) {
		if (!this.extTabPanel) return;
		if (!this.existsTab(IdTab)) return;
		this.extTabPanel.unhideTab(this.getTabId(IdTab));
	};

	DOMElement.hideTab = function (IdTab) {
		if (!this.extTabPanel) return;
		if (!this.existsTab(IdTab)) return;
		this.extTabPanel.hideTab(this.getTabId(IdTab));
	};

	DOMElement.lockTab = function (IdTab) {
		if (!this.extTabPanel) return;
		if (!this.existsTab(IdTab)) return;
		this.extTabPanel.disableTab(this.getTabId(IdTab));
	};

	DOMElement.unLockTab = function (IdTab) {
		if (!this.extTabPanel) return;
		if (!this.existsTab(IdTab)) return;
		this.extTabPanel.enableTab(this.getTabId(IdTab));
	};

	DOMElement.showLink = function (code) {
		var extElement = Ext.get(this);
		var extLink = extElement.select(CSS_LINK + "." + code).first();
		if (extLink == null) return;
		extLink.dom.style.display = "block";
	};

	DOMElement.hideLink = function (code) {
		var extElement = Ext.get(this);
		var extLink = extElement.select(CSS_LINK + "." + code).first();
		if (extLink == null) return;
		extLink.dom.style.display = "none";
	};

	DOMElement.showOperation = function (sName) {
		var DOMPage = this.getDOMPage();
		DOMPage.viewerToolbar.showOperation(sName);
		ViewerSidebar.showOperation(sName);
	};

	DOMElement.enableOperation = function (sName) {
		var DOMPage = this.getDOMPage();
		DOMPage.viewerToolbar.enableOperation(sName);
		ViewerSidebar.enableOperation(sName);
	};

	DOMElement.hideOperation = function (sName) {
		var DOMPage = this.getDOMPage();
		DOMPage.viewerToolbar.hideOperation(sName);
		ViewerSidebar.hideOperation(sName);
	};

	DOMElement.disableOperation = function (sName) {
		var DOMPage = this.getDOMPage();
		DOMPage.viewerToolbar.disableOperation(sName);
		ViewerSidebar.disableOperation(sName);
	};

	DOMElement.executeOnloadCommands = function () {
		var extElement = Ext.get(this);

		if (this.extTabPanel) {
			var extTab = this.extTabPanel.getActiveTab();
			extElement = Ext.get(extTab.bodyEl);
		}

		var aExtOnloadCommands = extElement.select(CSS_ONLOAD_COMMAND);
		aExtOnloadCommands.each(function (extOnloadCommand) {
			var extElement = extOnloadCommand.up(CSS_NODE);
			if (extElement == null || extElement.dom != this) extElement = extOnloadCommand.up(CSS_TASK);
			if (extElement == null || extElement.dom != this) extElement = extOnloadCommand.up(CSS_TEAM);
			if (extElement == null || extElement.dom != this) return;
			CommandListener.throwCommand(extOnloadCommand.dom.innerHTML);
		}, this);
	};

	DOMElement.setObservers = function (Observers) {
	};

	DOMElement.addObserver = function (Observer) {
	};

	DOMElement.removeObserver = function (fieldPath) {
	};

	DOMElement.update = function (sData) {
		return true;
	};

	DOMElement.getDOMPage = function () {
		var DOMPage = this;

		if (!this.isPage()) {
			var extElement = Ext.get(this);
			var extPage = extElement.up(CSS_PAGE);
			if (extPage == null) return;
			DOMPage = extPage.dom;
		}

		return DOMPage;
	};

	DOMElement.refreshState = function () {
		CommandListener.throwCommand("refreshnodestate(" + this.IdView + ")");
	};

	DOMElement.refreshToolbar = function () {
		var configuration = this.getConfiguration();

		if (configuration == null) return;

		var DOMPage = this.getDOMPage();
		DOMPage.viewerToolbar.setTarget(configuration.toolbar);
		DOMPage.viewerToolbar.refresh();
	};

	DOMElement.refreshSidebar = function () {
		var configuration = this.getConfiguration();

		if (configuration == null) return;

		ViewerSidebar.setTarget(configuration.sidebar);
		ViewerSidebar.refresh();
	};

	DOMElement.refreshPanelItem = function (extTabPanelItem) {
		var extElement = extTabPanelItem.bodyEl.down(this.cssElementStyle);

		if (document.activeElement && document.activeElement != document.body) {
			try {
				document.activeElement.blur();
			}
			catch (e) {
			}
		}

		if (this.onTabFocus) this.onTabFocus(this, extTabPanelItem.bodyEl, (extElement) ? extElement.dom : null);

		if (extElement) {

			var extTabsBody = extElement.up(".x-tabs-body");
			if (extTabsBody) extTabsBody.scrollTo("top", 0);

			var Process = null;

			if (this.cssElementStyle == CSS_TASK) Process = new CGProcessLoadEmbeddedTask();
			else if (this.cssElementStyle == CSS_NODE) Process = new CGProcessLoadEmbeddedNode();

			if (Process != null) {
				Process.DOMItem = extElement.dom;
				Process.onFinish = DOMElement.refreshPanelItemCallback.bind(this, extTabPanelItem);
				Process.execute();
			}
			else extElement.dom.refreshPanelItemCallback(extTabPanelItem);

		}
	};

	DOMElement.refreshPanelItemCallback = function (extTabPanelItem) {
		this.executeOnloadCommandsAndRefreshBars(extTabPanelItem.bodyEl, CSS_OBJECT);
	};

	DOMElement.executeOnloadCommandsAndRefreshBars = function (extElement, cssType) {
		var extElementList = extElement.select(cssType);
		extElementList.each(function (extElement) {
			if (extElement == null) return;
			if (extElement.dom.executeOnloadCommands) extElement.dom.executeOnloadCommands();
			if (extElement.dom.refreshState) extElement.dom.refreshState();
			if (extElement.dom.refreshToolbar) extElement.dom.refreshToolbar();
			if (extElement.dom.refreshSidebar) extElement.dom.refreshSidebar();
		});

	};

	DOMElement.addSummationBehaviours = function (aDOMFields) {
		for (var iPos = 0; iPos < aDOMFields.length; iPos++) {
			var DOMField = aDOMFields[iPos];
			var extField = Ext.get(DOMField);

			if ((FieldType = DOMField.getType()) == null) continue;
			if (FieldType != FIELD_TYPE_SUMMATION) continue;

			Event.observe(extField.dom, "click", DOMElement.atSummationClick.bind(this, extField.dom));

			var extLabels = extField.select("a.label");
			extLabels.each(function (extLabel) {
				Event.observe(extLabel.dom, "click", DOMElement.atSummationLabelClick.bind(this, extLabel.dom));
			}, this);

			var extClose = extField.select("a.close").first();
			Event.observe(extClose.dom, "click", DOMElement.atSummationCloseClick.bind(this, extClose.dom));
		}
	};

	DOMElement.addTableViewBehaviours = function (aDOMFields) {
		var i = 0;

		for (var iPos = 0; iPos < aDOMFields.length; iPos++) {
			var DOMField = aDOMFields[iPos];
			var extField = Ext.get(DOMField);
			var sCode = DOMField.getCode();

			if ((FieldType = DOMField.getType()) == null) continue;
			if ((FieldType != FIELD_TYPE_COMPOSITE) && (FieldType != FIELD_TYPE_NODE)) continue;
			if (!extField.dom.isTableView()) continue;

			var extTableView = extField.select("ul.table").first();
			var extTableViewElements = extTableView.select("li.element");
			extTableViewElements.each(function (extTableViewElement) {
				if (sCode != extTableViewElement.up(".field").dom.getCode()) return;

				var extLink = extTableViewElement.down("table .link");
				if (extLink) Event.observe(extLink.dom, "click", DOMElement.atTableViewElementClick.bind(this, extTableViewElement.dom));

				var extMoveOption = extTableViewElement.down("table .move");
				if (extMoveOption != null) extTableViewElement.dom.DragObject = new dragObject(extTableViewElement.dom, extMoveOption.dom, new Position(0, -30), new Position(0, extTableView.getBottom() - 30), DOMElement.atTableViewElementDragStart.bind(DOMElement, extTableView.dom, extTableViewElement.dom, extMoveOption.dom), DOMElement.atTableViewElementDragMove.bind(extField.dom, DOMElement, extTableView.dom), DOMElement.atTableViewElementDragEnd.bind(DOMElement, extField.dom, extTableView.dom, extTableViewElement.dom, extMoveOption.dom), false);

				var extClose = extTableViewElement.down("table .close");
				if (extClose) Event.observe(extClose.dom, "click", DOMElement.atTableViewElementClose.bind(this, extTableViewElement.dom));

				extTableViewElement.dom.doRemove = DOMElement.doRemove.bind(this, extTableViewElement.dom);
				extTableViewElement.dom.pos = i;
				i++;
			}, this);

			extTableView.dom.DOMDragDropHolder = $(new Insertion.Bottom(extTableView.dom, "<div style='display:none;border:1px solid #ccc;background:rgb(225,225,225);'></div>").element.descendants().last());
			extTableView.dom.DOMDragDropHolder.DOMElement = null;

			this.addTableViewFilterBehaviours(extField);
			this.addTableViewListBehaviours(extField);
			this.addTableViewPagingBehaviours(extField);

			var extLists = extField.select(".tableview ul.list");
			extLists.each(function (extList) {
				i = 0;
				var extElements = extList.select("li");
				extElements.each(function (extElement) {
					if (extList.dom != extElement.up("ul.list").dom) return;
					if (sCode != extElement.up(".field").dom.getCode()) return;
					extElement.dom.style.display = "none";
					extElement.dom.pos = i;
					i++;
				}, this);
			}, this);
		}

	};

	DOMElement.getTableViewVisibleElementsCountToPos = function (extField, iPos) {
		var aCount = new Array();
		var aExtElements = extField.select("ul.table li.element");
		var iCurrentPos = 0;

		aCount[0] = aCount[1] = 0;
		aExtElements.each(function (extElement) {
			if ((extElement.dom.style.display == "") || (extElement.dom.style.display == "block") || (extElement.dom.style.display == "inline")) {
				if (iPos != iCurrentPos) {
					if (iCurrentPos < iPos) aCount[0]++;
					else aCount[1]++;
				}
			}
			iCurrentPos++;
		}, this);

		return aCount;
	};

	DOMElement.refreshTableViewPaging = function (extField) {
		var extPrevious, extCurrent;
		var extPaging = extField.down(".wtable .toolbar .paging");
		var iPos = this.getTableViewElementPos(extField, extField.extCurrentElement);
		var aCount = this.getTableViewVisibleElementsCountToPos(extField, iPos);

		extPaging.dom.style.display = (aCount[2] == 1) ? "none" : "block";
		extPrevious = extField.down(".wtable .toolbar .paging .previous");
		extCurrent = extField.down(".wtable .toolbar .paging .current");
		extNext = extField.down(".wtable .toolbar .paging .next");

		extCurrent.dom.innerHTML = aCount[0] + 1;

		if (aCount[0] <= 0) extPrevious.addClass(CLASS_DISABLED);
		else extPrevious.removeClass(CLASS_DISABLED);

		if (aCount[1] <= 0) extNext.addClass(CLASS_DISABLED);
		else extNext.removeClass(CLASS_DISABLED);
	};

	DOMElement.addTableViewFilterBehaviours = function (extField) {
		var extFilter = extField.down(".wtable .filter");
		var extFilterEmpty = extField.down(".wtable .filterempty");
		var extFilterClear = extField.down(".wtable .filterclear");

		extFilter.on("keyup", DOMElement.atTableViewFilterKeyUp.bind(DOMElement, extField));
		extFilter.on("focus", DOMElement.atTableViewFilterFocus.bind(DOMElement, extField));
		extFilter.on("blur", DOMElement.atTableViewFilterBlur.bind(DOMElement, extField));

		extFilterEmpty.dom.style.display = "block";
		extFilterEmpty.on("click", DOMElement.atTableViewFilterEmptyClick.bind(DOMElement, extField));

		extFilterClear.dom.style.display = "none";
		extFilterClear.on("click", DOMElement.atTableViewFilterClearClick.bind(DOMElement, extField));
	};

	DOMElement.addTableViewListBehaviours = function (extField) {
		var extHideList = extField.down(".wtable .hidelist");

		extHideList.on("click", DOMElement.atTableViewHideListClick.bind(DOMElement, extField));
		this.hideList(extField);
	};

	DOMElement.addTableViewPagingBehaviours = function (extField) {
		var extPrevious = extField.down(".wtable .toolbar .paging .previous");
		var extNext = extField.down(".wtable .toolbar .paging .next");

		extField.iCurrentElement = 0;

		Event.observe(extPrevious.dom, "click", DOMElement.atTableViewPreviousElementClick.bind(DOMElement, extField));
		Event.observe(extNext.dom, "click", DOMElement.atTableViewNextElementClick.bind(DOMElement, extField));

		DOMElement.refreshTableViewPaging(extField);
	};

	DOMElement.addTableViewElementBehaviours = function (DOMItem) {
		var extElement = Ext.get(DOMItem);
		var extField = Ext.get(extElement.up(".field"));
		var extTableView = Ext.get(extElement.up("ul"));
		var i = extElement.up("ul").select("li.element").getCount() - 1;

		var extLink = extElement.down("table .link");
		if (extLink) Event.observe(extLink.dom, "click", DOMElement.atTableViewElementClick.bind(this, extElement.dom));

		var extMoveOption = extElement.down("table .move");
		if (extMoveOption != null) extElement.dom.DragObject = new dragObject(extElement.dom, extMoveOption.dom, new Position(0, -30), new Position(0, extTableView.getBottom() - 30), DOMElement.atTableViewElementDragStart.bind(DOMElement, extTableView.dom, extElement.dom, extMoveOption.dom), DOMElement.atTableViewElementDragMove.bind(DOMElement, extTableView.dom), DOMElement.atTableViewElementDragEnd.bind(DOMElement, extField.dom, extTableView.dom, extElement.dom, extMoveOption.dom), false);

		var extClose = extElement.down("table .close");
		if (extClose) Event.observe(extClose.dom, "click", DOMElement.atTableViewElementClose.bind(this, extElement.dom));

		extElement.dom.doRemove = DOMElement.doRemove.bind(this, extElement.dom);
		extElement.dom.pos = i;

		return i;
	};

	DOMElement.getTableViewElementPos = function (extField, extElement) {
		var extTable = extField.select("ul.table").first();
		var aExtElements = extTable.select("li.element");
		var iCurrentPos = -1;

		if (extElement == null) return -1;

		for (var i = 0; i < aExtElements.elements.length; i++) {
			var extCurrentElement = Ext.get(aExtElements.elements[i]);
			if (extField.dom.getCode() != extCurrentElement.up(".field").dom.getCode()) continue;
			iCurrentPos++;
			if (extCurrentElement.dom == extElement.dom) break;
		}

		return iCurrentPos;
	};

	DOMElement.deactivateTableViewElement = function (extField) {
		if (extField.extCurrentElement != null && !extField.extCurrentElement.hasClass(CLASS_READONLY))
			this.hideList(extField);

		if (extField.extCurrentElement == null) return;
		extField.extCurrentElement.removeClass(CLASS_ACTIVE);
		extField.extCurrentElement = null;
	};

	DOMElement.activateTableViewElement = function (extField, extElement) {
		if (extElement.hasClass(CLASS_READONLY)) return;
		if (extElement == null) return;

		if (extField.extCurrentElement != null) extField.extCurrentElement.removeClass(CLASS_ACTIVE);

		if (extElement == extField.extCurrentElement) {
			this.deactivateTableViewElement(extField);
			return;
		}

		extElement.addClass(CLASS_ACTIVE);
		extElement.scrollIntoView(extElement.up(".table_envelope"));

		if (extElement.dom.onActivate) extElement.dom.onActivate(extElement);

		var extList = extField.down(".tableview ul.list");
		if (extList) {
			var extListElements = extList.select(".element");
			var iNumElements = 0;
			extListElements.each(function (extListElement) {
				if (extList.dom != extListElement.dom.parentNode) return;
				iNumElements++;
				if (extListElement.dom.pos == extElement.dom.pos) extListElement.dom.style.display = "block";
				else extListElement.dom.style.display = "none";
			}, this);
			if (iNumElements == 1) extListElements.first().dom.style.display = "block";
		}

		this.showList(extField, extList, extElement);
		extField.extCurrentElement = extElement;
		this.refreshTableViewPaging(extField);
	};

	DOMElement.updateTableViewListSize = function (extField) {
		var DOMFieldTable = extField.up(".field.tableview");
		if (DOMFieldTable == null) return;
		var extList = DOMFieldTable.down(".tableview").down("ul.list");
		var height = extList.getHeight();
		if (height == 0) return;
		var extElement = Ext.get(DOMFieldTable.down(".table_envelope").down("ul").down(".element." + CLASS_ACTIVE));
		this.showList(Ext.get(DOMFieldTable), extList, extElement);
	};

	DOMElement.activateTableViewPreviousElement = function (extField, extElement) {
		var extActiveElement = extField.select("ul.table li.element.active").first();
		if (extElement == null) extElement = extActiveElement;
		if (extElement == null) return;
		var extSibling = Ext.get(extElement.getPrevSibling());
		while (extSibling != null && extSibling.dom.style.display == "none") extSibling = Ext.get(extSibling.getPrevSibling());
		if (extSibling == null) {
			this.deactivateTableViewElement(extField);
			return;
		}
		if (extSibling.hasClass("header")) this.deactivateTableViewElement(extField);
		else this.activateTableViewElement(extField, extSibling);
	};

	DOMElement.filterTableView = function (extField) {
		var sFilter = extField.down(".wtable .filter").dom.value;
		var extTableViewElements = extField.select("ul.table li.element");
		var sCode = extField.dom.getCode();

		this.deactivateTableViewElement(extField);

		extTableViewElements.each(function (extTableViewElement) {
			if (sCode != extTableViewElement.up(".field").dom.getCode()) return;
			var sContent = extTableViewElement.dom.innerHTML.toLowerCase();
			extTableViewElement.dom.style.display = (sContent.indexOf(sFilter.toLowerCase()) != -1) ? "block" : "none";
		}, this);

		DOMElement.refreshTableViewPaging(extField);
	};

	DOMElement.resizeTableView = function (extField) {
		var extCurrentElement = extField.extCurrentElement;
		if (extCurrentElement == null) return;
		var extList = extField.down(".tableview ul.list");
		this.showList(extField, extList, extCurrentElement);
	};

	DOMElement.activateFirst = function (extField) {
		var extFirst = extField.select("ul.table li.element").first();
		if (extFirst) extFirst.dom.click();
		else {
			DOMElement.refreshTableViewPaging(extField);
			DOMElement.hideList(extField);
		}
	};

	DOMElement.showList = function (extField, extList, extElement) {
		var extHideList = extField.down(".wtable .hidelist");
		var extToolbar = extField.down(".wtable .toolbar.item");
		var extList = extField.down(".tableview ul.list");
		var widget = this;

		if (this.showListTimeout != null)
			window.clearTimeout(this.showListTimeout);

		if (extField.extCurrentElement) {
			this.hideList(extField);
		}

		this.showListTimeout = window.setTimeout(function() {
            extHideList.dom.style.display = "block";
            extToolbar.dom.style.display = "block";
            extList.dom.style.display = "block";

            extElement.setHeight(extList.getHeight() + 20);

            var extCloseFlag = extElement.select("td.flag .close").first();
            extCloseFlag.setBottom(0);
            extCloseFlag.dom.style.display = "block";

            var extValues = extElement.select("td .value");
            extValues.each(function (extValue) {
                extValue.dom.style.visibility = "hidden";
            }, widget);

            extList.dom.style.top = (extElement.dom.offsetTop) + "px";
		}, 200);
	};

	DOMElement.hideList = function (extField) {
		var extHideList = extField.down(".wtable .hidelist");
		var extToolbar = extField.down(".wtable .toolbar.item");
		var extList = extField.down(".tableview ul.list");
		extHideList.dom.style.display = "none";
		extToolbar.dom.style.display = "none";
		extList.dom.style.display = "none";

		if (extField.extCurrentElement) {
			extField.extCurrentElement.dom.style.height = "auto";
			var extCloseFlag = extField.extCurrentElement.select("td.flag .close").first();
			extCloseFlag.dom.style.height = "auto";
			extCloseFlag.dom.style.display = "none";
			var extValues = extField.extCurrentElement.select("td .value");
			extValues.each(function (extValue) {
				extValue.dom.style.visibility = "visible";
			}, this);
		}
	};

	DOMElement.doRemove = function (DOMElement) {
		var extElement = Ext.get(DOMElement);
		var extField = Ext.get(extElement.up(".field"));
		this.activateTableViewPreviousElement(extField, extElement);
		extElement.dom.remove();
	};

	DOMElement.getConfiguration = function () {
		var extElement = Ext.get(DOMElement);
		var extConfiguration = extElement.down(".def.configuration");

		if (extConfiguration == null) return null;
		eval(extConfiguration.dom.innerHTML);

		if (extElement.dom.isPage()) return configuration;

		var extPage = extElement.up(CSS_PAGE);
		if (extPage == null) return configuration;

		var pageConfiguration = extPage.dom.getConfiguration();

		var operationSet = configuration.toolbar.operationSet;
		for (var i = 0; i < pageConfiguration.toolbar.operationSet.length; i++) {
			var operation = pageConfiguration.toolbar.operationSet[i];
			if (containsObjectInList(operation, operationSet)) continue;
			operationSet.push(operation);
		}

		operationSet = configuration.sidebar.operationSet;
		for (var i = 0; i < pageConfiguration.sidebar.operationSet.length; i++) {
			var operation = pageConfiguration.sidebar.operationSet[i];
			if (containsObjectInList(operation, operationSet)) continue;
			operationSet.push(operation);
		}

		return configuration;
	};

	DOMElement.isPage = function () {
		var extElement = Ext.get(DOMElement);
		return extElement.hasClass(CLASS_PAGE);
	};

	// #############################################################################################################

	DOMElement.atWindowResize = function () {
		this.resize();
	};

	DOMElement.atPanelItemActivated = function (extTabPanel, extTabPanelItem) {
		if (DOMElement.onTabActivated) DOMElement.onTabActivated();
		else this.refreshPanelItem(extTabPanelItem);
	};

	DOMElement.atTableViewElementClick = function (DOMElement, EventLaunched) {
		var extField = Ext.get(DOMElement.up(".field"));
		this.activateTableViewElement(extField, Ext.get(DOMElement));
		Event.stop(EventLaunched);
		return false;
	};

	DOMElement.atTableViewElementClose = function (DOMElement, EventLaunched) {
		var extField = Ext.get(DOMElement.up(".field"));
		this.deactivateTableViewElement(extField);
		Event.stop(EventLaunched);
		return false;
	};

	DOMElement.atTableViewPreviousElementClick = function (extField) {
		this.activateTableViewPreviousElement(extField);
	};

	DOMElement.atTableViewNextElementClick = function (extField) {
		var extActiveElement = extField.select("ul.table li.element.active").first();
		if (extActiveElement == null) {
			var extFirst = extField.select("ul.table li.element").first();
			extFirst.dom.click();
			return;
		}
		var extSibling = Ext.get(extActiveElement.getNextSibling());
		while (extSibling != null && extSibling.dom.style.display == "none") extSibling = Ext.get(extSibling.getNextSibling());
		if (extSibling == null) return;
		this.activateTableViewElement(extField, extSibling);
	};

	DOMElement.atTableViewFilterKeyUp = function (extField, oEvent) {
		var extFilter = extField.down(".wtable .filter");
		var extFilterClear = extField.down(".wtable .filterclear");
		var codeKey = oEvent.keyCode;
		var sFilter = extFilter.dom.value;

		window.clearTimeout(this.idTimeoutFilter);

		if ((codeKey == oEvent.UP) || (codeKey == oEvent.DOWN) || (codeKey == oEvent.ENTER) || (codeKey == oEvent.LEFT) || (codeKey == oEvent.RIGHT) || (codeKey == oEvent.SHIFT)) return;

		extFilterClear.dom.style.display = (sFilter != "") ? "block" : "none";

		this.idTimeoutFilter = window.setTimeout(DOMElement.filterTableView.bind(DOMElement, extField), 300);
	};

	DOMElement.atTableViewFilterFocus = function (extField) {
		var extFilter = extField.down(".wtable .filter");
		var extFilterEmpty = extField.down(".wtable .filterempty");

		extFilterEmpty.dom.style.display = "none";
		extFilter.dom.style.background = "#EFF7FF";
		extFilter.dom.select();
	};

	DOMElement.atTableViewFilterBlur = function (extField) {
		var extFilter = extField.down(".wtable .filter");
		var extFilterEmpty = extField.down(".wtable .filterempty");
		var sFilter = extFilter.dom.value;
		extFilter.dom.style.background = (sFilter.length <= 0) ? "" : "#EFF7FF";
		extFilterEmpty.dom.style.display = (sFilter.length <= 0) ? "block" : "none";
	};

	DOMElement.atTableViewFilterEmptyClick = function (extField) {
		var extFilter = extField.down(".wtable .filter");
		extFilter.focus();
	};

	DOMElement.atTableViewFilterClearClick = function (extField) {
		var extFilter = extField.down(".wtable .filter");
		var extFilterEmpty = extField.down(".wtable .filterempty");
		var extFilterClear = extField.down(".wtable .filterclear");

		extFilter.dom.value = "";
		extFilter.dom.style.background = "";
		extFilterEmpty.dom.style.display = "block";
		extFilterClear.dom.style.display = "none";

		this.filterTableView(extField);
	};

	DOMElement.atTableViewHideListClick = function (extField) {
		this.hideList(extField);
	};

	DOMElement.atTableViewElementDragStart = function (DOMTable, DOMElement, DOMMoveOption, oEvent) {
		var iHeight = Ext.get(DOMElement).getHeight();
		DOMTable.addClassName("draglist");
		Ext.get(DOMMoveOption).addClass("grabbing");
		DOMElement.style.top = DOMElement.offsetTop + 'px';
		DOMElement.style.left = DOMElement.offsetLeft + 'px';
		DOMElement.addClassName("drag");
		DOMTable.DOMDragDropHolder.style.display = "block";
		DOMTable.DOMDragDropHolder.style.height = iHeight + "px";
		DOMTable.insertBefore(DOMTable.DOMDragDropHolder, DOMElement);
		DOMTable.DOMDragDropHolder.DOMElement = DOMElement;
	};

	DOMElement.atTableViewElementDragMove = function (DOMField, DOMTable, oPosition, DOMElement, oEvent) {
		var yPos = oPosition.Y + (oEvent.layerY ? oEvent.layerY : oEvent.offsetY);
		var temp;
		var bestItem = "end";

		for (var i = 0; i < DOMTable.childNodes.length; i++) {
			if (DOMTable.childNodes[i].className == "element") {
				temp = parseInt(Ext.get(DOMTable.childNodes[i]).getHeight());
				if (temp >= yPos) {
					bestItem = DOMTable.childNodes[i];
					break;
				}
				yPos -= temp;
			}
		}

		if (bestItem == DOMTable.DOMDragDropHolder || bestItem == DOMTable.DOMDragDropHolder.DOMElement) return;

		DOMTable.DOMDragDropHolder.DOMElement = bestItem;
		if (bestItem != "end") DOMTable.insertBefore(DOMTable.DOMDragDropHolder, DOMTable.childNodes[i]);
		else DOMTable.appendChild(DOMTable.DOMDragDropHolder);

		var extElement = Ext.get(DOMElement);
		var extField = Ext.get(DOMField);
		if (extField.extCurrentElement != null && extElement == extField.extCurrentElement) {
			var extList = extField.down(".tableview ul.list");
			extList.dom.style.top = (DOMTable.DOMDragDropHolder.offsetTop + 30) + "px";
		}
	};

	DOMElement.getElementPos = function (DOMField, DOMElement) {
		var extField = Ext.get(DOMField);
		var aExtElements = extField.select("ul.table li.element");
		for (var i = 0; i < aExtElements.getCount(); i++) {
			var extElement = aExtElements.item(i);
			if (extElement.dom.id == DOMElement.id) return i;
		}
		return -1;
	};

	DOMElement.atTableViewElementDragEnd = function (DOMField, DOMTable, DOMElement, DOMMoveOption) {
		var previousPos = this.getElementPos(DOMField, DOMElement);
		var newPos;

		Ext.get(DOMMoveOption).removeClass("grabbing");

		DOMTable.DOMDragDropHolder.style.display = "none";

		if (DOMTable.DOMDragDropHolder.DOMElement != null) {
			DOMTable.DOMDragDropHolder.DOMElement = null;
			DOMTable.replaceChild(DOMElement, DOMTable.DOMDragDropHolder);
		}

		newPos = this.getElementPos(DOMField, DOMElement);

		DOMElement.removeClassName('drag');
		DOMElement.style.top = '0px';
		DOMElement.style.left = '0px';
		DOMElement.pos = newPos;
		DOMTable.removeClassName("draglist");

		var extElement = Ext.get(DOMElement);
		var extField = Ext.get(DOMField);
		if (extField.extCurrentElement != null && extElement == extField.extCurrentElement) {
			var extList = extField.down(".tableview ul.list");
			extList.dom.style.top = (extElement.dom.offsetTop + 30) + "px";
		}

		if (DOMElement.onMove) DOMElement.onMove(Ext.get(DOMElement), previousPos, newPos);
	};

	DOMElement.atSummationLabelClick = function (DOMLabel) {
		var extLabel = Ext.get(DOMLabel);
		var extItem = extLabel.up(".summationitem");
		var extList = extItem.down("ul");
		var extClose = extItem.up(CSS_FIELD).select(".close").first();

		if (extItem.hasClass("expanded")) {
			extList.dom.style.display = "none";
			extClose.dom.style.display = "none";
			extItem.removeClass("expanded");
		}
		else {
			extList.dom.style.display = "block";
			extClose.dom.style.display = "block";
			extItem.addClass("expanded");
		}
	};

	DOMElement.atSummationCloseClick = function (DOMClose, oEvent) {
		var extClose = Ext.get(DOMClose);
		var extField = extClose.up(CSS_FIELD);
		var extList = extField.down(".widget ul");
		var extClose = extField.down(".widget .close");

		extList.dom.style.display = "none";
		extClose.dom.style.display = "none";

		Event.stop(oEvent);

		return false;
	};

	DOMElement.atSummationClick = function (DOMField) {
		var extField = Ext.get(DOMField);
		var extList = extField.down(".widget ul");
		var extClose = extField.down(".widget .close");

		extList.dom.style.display = "block";
		extClose.dom.style.display = "block";
	};

};