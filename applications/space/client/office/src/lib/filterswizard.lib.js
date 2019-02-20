var aFiltersWizardTemplates = new Array();
aFiltersWizardTemplates["es"] = new Array();
aFiltersWizardTemplates["es"]["MAIN"] = "<div class='separator'><div class='group groupby'><label>Mostrar</label><table class='filter'><tr><td class='content'><input type='text' class='value'/></td></tr></table><ul class='groupby list'></ul></div><div class='group sortby'><label>Ordenar por</label><ul class='sortby list'></ul><ul class='toolbar'><li><a class='more' href='javascript:void(null)'>más</a></li></ul></div><div class='group add edition'><label>Añadir</label><ul class='add list'></ul></div></div>";
aFiltersWizardTemplates["es"]["FILTER_EMPTY"] = "<span class='empty'>Introduzca el texto que desee encontrar</span>";
aFiltersWizardTemplates["es"]["SORTBYLIST_SECTION"] = "<ul class='section'></ul>";
aFiltersWizardTemplates["es"]["SORTBYLIST_SECTION_OPTIONS"] = "<div class='sortby options'>&nbsp;<a class='unselect' href='javascript:void(null)' alt='quitar'></a></div>";
aFiltersWizardTemplates["es"]["GROUPBYLIST_ITEM"] = "<li class='#{code}'><div class='label'>#{label}</div><div class='content'><select class='selector'><option value='all'>Todos</option></select></div></li>";
aFiltersWizardTemplates["es"]["SORTBYLIST_ITEM"] = "<li class='#{code}'><a class='label' href='javascript:void(null)'>#{label}</a></li>";
aFiltersWizardTemplates["es"]["ADDLIST_ITEM"] = "<li><a class='#{code}' href='#{command}'>#{label}</a><div class='description'>#{description}</div></li>";
aFiltersWizardTemplates["en"] = new Array();
aFiltersWizardTemplates["en"]["MAIN"] = "<div class='separator'><div class='group groupby'><label>Show</label><table class='filter'><tr><td class='content'><input type='text' class='value'/></td></tr></table><ul class='groupby list'></ul></div><div class='group sortby'><label>Sort by</label><ul class='sortby list'></ul><ul class='toolbar'><li><a class='more' href='javascript:void(null)'>more</a></li></ul></div><div class='group add edition'><label>Add</label><ul class='add list'></ul></div></div>";
aFiltersWizardTemplates["en"]["FILTER_EMPTY"] = "<span class='empty'>Enter the text you are looking for</span>";
aFiltersWizardTemplates["en"]["SORTBYLIST_SECTION"] = "<ul class='section'></ul>";
aFiltersWizardTemplates["en"]["SORTBYLIST_SECTION_OPTIONS"] = "<div class='sortby options'>&nbsp;<a class='unselect' href='javascript:void(null)' alt='delete'></a></div>";
aFiltersWizardTemplates["en"]["GROUPBYLIST_ITEM"] = "<li class='#{code}'><div class='label'>#{label}</div><div class='content'><select class='selector'><option value='all'>All</option></select></div></li>";
aFiltersWizardTemplates["en"]["SORTBYLIST_ITEM"] = "<li class='#{code}'><a class='label' href='javascript:void(null)'>#{label}</a></li>";
aFiltersWizardTemplates["en"]["ADDLIST_ITEM"] = "<li><a class='#{code}' href='#{command}'>#{label}</a><div class='description'>#{description}</div></li>";

// IMPORTANT: escape and utf8Encode functions are needed by this library

var CODE_WORDS_FILTER = "__words";
var LIST_VIEWER_QUERY_SEPARATOR = ":";
var LIST_VIEWER_QUERIES_SEPARATOR = "_f_";
var MODE_ASCENDANT = "asc";
var MODE_DESCENDANT = "desc";
var CLASS_ASCENDANT = "ascendant";
var CLASS_DESCENDANT = "descendant";
var CLASS_SELECTED = "selected";
var CLASS_LISTVIEWER_WIZARD = "listviewer wizard";
var CLASS_EDITABLE = 'editable';
var CLASS_READONLY = "readonly";
var LIST_SORTBY = "sortby";
var LIST_GROUPBY = "groupby";
var CSS_SELECTOR = ".selector";
var CSS_SELECTED = ".selected";
var CSS_MORE = ".more";
var CSS_FILTER = ".filter input.value";
var CSS_FILTER_EMPTY = ".filter .empty";
var CSS_SORTBYLIST = ".sortby.list";
var CSS_SORTBYOPTIONS = ".sortby.options";
var CSS_GROUPBYLIST = ".groupby.list";
var CSS_UNSELECT = ".unselect";
var CSS_LIST_SECTION = ".section";
var CSS_GROUP = ".group";
var CSS_SUMMARY = ".summary";
var KEY_UP = 38;
var KEY_DOWN = 40;
var KEY_ENTER = 13;
var KEY_LEFT = 37;
var KEY_RIGHT = 39;
var KEY_SHIFT = 16;
var KEY_CONTROL = 17;

function isFunction(item) {
  return (typeof item == "function");
};

function filtersWizardHtmlDecode(input) {
  if (input == null) return null;
  var e = document.createElement('div');
  e.innerHTML = input;
  if (e.childNodes.length <= 0) return input;
  return e.childNodes[0].nodeValue;
};

function CGFiltersWizard(Options, language, imagesPath) {
  this.Options = Options;
  this.aParameters = new Object();
  this.extFilter = null;
  this.extFilterEmpty = null;
  this.initState();
  this.wizardLayer = null;
  this.language = language;
  if (aFiltersWizardTemplates[this.language] == null) this.language = "es";
  this.imagesPath = imagesPath;
  this.groupByWidgets = new Array();
};

CGFiltersWizard.prototype.setWizardLayer = function (wizardLayer) {
  this.wizardLayer = wizardLayer;
};

CGFiltersWizard.prototype.init = function () {
  this.extWizardLayer = Ext.get(this.wizardLayer);
  this.extWizardLayer.dom.innerHTML = aFiltersWizardTemplates[this.language]["MAIN"];
  this.extWizardLayer.addClass(CLASS_LISTVIEWER_WIZARD);
};

CGFiltersWizard.prototype.render = function () {
  this.init();

  if (this.Options.Editable)
    this.extWizardLayer.addClass(CLASS_EDITABLE);
  else
    this.extWizardLayer.removeClass(CLASS_EDITABLE);

  this.extMoreSortByList = this.extWizardLayer.select(CSS_MORE).first();
  if (this.extMoreSortByList != null) {
    Event.observe(this.extMoreSortByList.dom, "click", CGFiltersWizard.prototype.atMoreSortByList.bind(this, this.extMoreSortByList.dom));
    this.extMoreSortByList.dom.style.display = "none";
  }

  this.renderList(this.Options.SortByList, LIST_SORTBY, CSS_SORTBYLIST);
  this.renderList(this.Options.GroupByList, LIST_GROUPBY, CSS_GROUPBYLIST);
  this.renderFilter();

  CommandListener.capture(this.extWizardLayer.dom);

  this.applyState();
  this.update();
};

CGFiltersWizard.prototype.renderList = function (List, CodeList, ListStyle) {
  var extGroup = this.extWizardLayer.select(CSS_GROUP + "." + CodeList).first();

  if (List == null) {
    var extElement = extGroup;
    if (CodeList == LIST_GROUPBY) extElement = this.extWizardLayer.select(ListStyle).first();
    extElement.dom.style.display = "none";
    return;
  }

  List.bUpdate = true;
  this.updateList(List, CodeList, ListStyle, ",&nbsp;");
};

CGFiltersWizard.prototype.renderFilter = function () {
  this.extFilter = this.extWizardLayer.select(CSS_FILTER).first();
  Event.observe(this.extFilter.dom, "keyup", CGFiltersWizard.prototype.atFilterKeyUp.bind(this));
  Event.observe(this.extFilter.dom, "focus", CGFiltersWizard.prototype.atFilterFocus.bind(this));
  Event.observe(this.extFilter.dom, "blur", CGFiltersWizard.prototype.atFilterBlur.bind(this));
  this.extFilterEmpty = this.extWizardLayer.select(CSS_FILTER_EMPTY).first();
  if (this.extFilterEmpty == null) {
    new Insertion.After(this.extFilter.dom, aFiltersWizardTemplates[this.language]["FILTER_EMPTY"]);
    this.extFilterEmpty = this.extWizardLayer.select(CSS_FILTER_EMPTY).first();
  }
  Event.observe(this.extFilterEmpty.dom, "click", CGFiltersWizard.prototype.atFilterEmptyClick.bind(this));
};

CGFiltersWizard.prototype.update = function (data, bClearItems) {
  this.updateList(this.Options.SortByList, LIST_SORTBY, CSS_SORTBYLIST, ",&nbsp;");
  this.updateList(this.Options.GroupByList, LIST_GROUPBY, CSS_GROUPBYLIST, ",&nbsp;");
};

CGFiltersWizard.prototype.updateList = function (List, CodeList, ListStyle, sSeparator) {
  var ListItemTemplate = null;
  var DOMLayer = null;

  if (this.extWizardLayer == null) return;

  var extGroup = this.extWizardLayer.select(CSS_GROUP + "." + CodeList).first();
  if (List == null) {
    var extElement = extGroup;
    if (CodeList == LIST_GROUPBY) extElement = this.extWizardLayer.select(ListStyle).first();
    extElement.dom.style.display = "none";
    return;
  }

  if (this.extWizardLayer == null) return;
  if (!List.bUpdate) return;

  var extList = this.extWizardLayer.select(ListStyle).first();
  List.Code = CodeList;

  if (CodeList == LIST_GROUPBY) ListItemTemplate = new Template(aFiltersWizardTemplates[this.language]["GROUPBYLIST_ITEM"]);
  else if (CodeList == LIST_SORTBY) ListItemTemplate = new Template(aFiltersWizardTemplates[this.language]["SORTBYLIST_ITEM"]);

  if (ListItemTemplate != null) {
    var DOMList = extList.dom;
    DOMList.innerHTML = "";
    DOMLayer = DOMList;
    if (List.Code == LIST_SORTBY) DOMLayer = new Insertion.Bottom(DOMList, aFiltersWizardTemplates[this.language]["SORTBYLIST_SECTION"]).element.immediateDescendants().last();
  }

  List.CountItems = 0;
  for (var Code in List.Items) {
    if (isFunction(List.Items[Code])) continue;
    var Item = List.Items[Code];
    var DOMItem = null;

    if (ListItemTemplate != null) {
      DOMItem = new Insertion.Bottom(DOMLayer, ListItemTemplate.evaluate({"code": Item.Code, "label": Item.Label, "description": Item.Description, "command": Item.Command ? Item.Command : "#"})).element.immediateDescendants().last();
      DOMItem.Code = Item.Code;
    }

    this.updateListItem(List.Code, Item, DOMItem);
    List.CountItems++;
  }

  if (extGroup != null)
    extGroup.dom.style.display = ((List.CountItems > 0) || (CodeList == LIST_GROUPBY)) ? "block" : "none";

  List.bUpdate = false;
};

CGFiltersWizard.prototype.updateListItem = function (CodeList, Item, DOMItem) {

  if (DOMItem != null)
    Event.observe(DOMItem, "click", CGFiltersWizard.prototype.atListItemClick.bind(this, CodeList, DOMItem));

  if (CodeList != LIST_GROUPBY)
    return;

  var DOMGroupBySelector = Ext.get(DOMItem).select(CSS_SELECTOR).first().dom;
  var width = DOMGroupBySelector.offsetWidth;
  var height = DOMGroupBySelector.offsetHeight;

  var DOMGroupBySelectorWidget = new Ext.form.ComboBox({
    typeAhead: false,
    triggerAction: 'all',
    transform: DOMGroupBySelector,
    width: 135,
    forceSelection: true
  });
  //DOMGroupBySelectorWidget.applyTo(DOMGroupBySelector);

  this.groupByWidgets[Item.Code] = DOMGroupBySelectorWidget;
  DOMGroupBySelectorWidget.setSize(width, height);
  DOMGroupBySelectorWidget.doQuery = CGFiltersWizard.prototype.queryGroupByOptions.bind(DOMGroupBySelectorWidget);

  DOMGroupBySelector.Code = Item.Code;
  DOMGroupBySelectorWidget.Code = Item.Code;
  DOMGroupBySelectorWidget.on("focus", CGFiltersWizard.prototype.atGroupByItemLoadOptions.bind(this, Item, DOMGroupBySelectorWidget));
  DOMGroupBySelectorWidget.on("expand", CGFiltersWizard.prototype.atGroupByItemLoadOptions.bind(this, Item, DOMGroupBySelectorWidget));
  DOMGroupBySelectorWidget.on("beforeselect", CGFiltersWizard.prototype.atGroupByItemOptionBeforeSelect.bind(this));

  //this.updateGroupByOptions(Item, DOMGroupBySelectorWidget, true);
};

CGFiltersWizard.prototype.addItemsToDOMSelector = function (DOMSelector, Options) {
  for (var index in Options) {
    if (isFunction(Options[index])) continue;
    var Option = Options[index];
    var DOMOption = document.createElement('option');

    DOMOption.value = filtersWizardHtmlDecode(Option.Code);
    DOMOption.text = filtersWizardHtmlDecode(Option.Label);
    DOMOption.name = filtersWizardHtmlDecode(Option.Command);

    try {
      DOMSelector.add(DOMOption, null);
    }
    catch (ex) {
      DOMSelector.add(DOMOption);
    }
  }
};

CGFiltersWizard.prototype.initState = function () {
  var i;

  this.State = new Object();
  this.State.CurrentPage = 1;
  this.State.Filter = "";
  this.State.Lists = new Object();
  this.State.Lists[LIST_SORTBY] = new Object();
  this.State.Lists[LIST_SORTBY].Orders = new Array();
  this.State.Lists[LIST_SORTBY].Items = new Array();
  this.State.Lists[LIST_GROUPBY] = new Object();
  this.State.Lists[LIST_GROUPBY].Orders = new Array();
  this.State.Lists[LIST_GROUPBY].Items = new Array();

  if (this.Options.SortByList && this.Options.SortByList.Selection) {
    for (i = 0; i < this.Options.SortByList.Selection.length; i++) {
      var CodeItem = this.Options.SortByList.Selection[i];
      this.State.Lists[LIST_SORTBY].Items[CodeItem] = {Code: CodeItem, Mode: MODE_ASCENDANT, Order: i};
    }
    this.State.Lists[LIST_SORTBY].Orders = this.Options.SortByList.Selection;

    for (i in this.Options.SortByList.Items) {
      if (isFunction(this.Options.SortByList.Items[i])) continue;
      var ItemOption = this.Options.SortByList.Items[i];
      if (this.State.Lists[LIST_SORTBY].Items[ItemOption.Code] == null) continue;
      if (ItemOption.DefaultMode) this.State.Lists[LIST_SORTBY].Items[ItemOption.Code].Mode = (ItemOption.DefaultMode.toLowerCase() == "descendant") ? MODE_DESCENDANT : MODE_ASCENDANT;
    }
  }

  if (this.Options.GroupByList && this.Options.GroupByList.Selection) {
    for (i = 0; i < this.Options.GroupByList.Selection.length; i++) {
      var aItem = this.Options.GroupByList.Selection[i];
      this.State.Lists[LIST_GROUPBY].Items[aItem[0]] = {Code: aItem[0], Value: aItem[1]};
      this.State.Lists[LIST_GROUPBY].Orders.push(aItem[0]);
    }
  }
};

CGFiltersWizard.prototype.getState = function () {
  var extFilter;
  var Result = new Object();

  extFilter = this.extWizardLayer.select(CSS_FILTER).first();

  if (extFilter == null)
    return;

  Result.Filter = extFilter.dom.value;

  Result.Sorts = new Array();
  for (var i = 0; i < this.State.Lists[LIST_SORTBY].Orders.length; i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    Result.Sorts.push(this.State.Lists[LIST_SORTBY].Items[CodeSort]);
  }

  Result.Groups = new Array();
  for (var i = 0; i < this.State.Lists[LIST_GROUPBY].Orders.length; i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    Result.Groups.push(this.State.Lists[LIST_GROUPBY].Items[CodeGroup]);
  }

  Result.Folder = this.State.Folder;
  Result.Selection = this.State.Selection;

  Result.toEscapeUrl = function() {
    var sorts = "";
    for (var i = 0; i < this.Sorts.length; i++) {
      var Sort = this.Sorts[i];
      if (i != 0) sorts += LIST_VIEWER_QUERIES_SEPARATOR;
      sorts += Sort.Code + LIST_VIEWER_QUERY_SEPARATOR + Sort.Mode;
    }

    var groups = "";
    for (var i = 0; i < this.Groups.length; i++) {
      var Group = this.Groups[i];
      if (i != 0) groups += LIST_VIEWER_QUERIES_SEPARATOR;
      groups += Group.Code + LIST_VIEWER_QUERY_SEPARATOR + escape(encodeURIComponent(Group.Value));
    }

    return "&query=" + this.Filter + "&sortsby=" + sorts + "&groupsby=" + groups;
  };

  return Result;
};

CGFiltersWizard.prototype.setState = function (NewState) {
  if (NewState == null) return;

  this.State.Filter = NewState.Filter;

  this.State.Lists[LIST_SORTBY].Items = new Array();
  this.State.Lists[LIST_SORTBY].Orders = new Array();
  for (var i = 0; i < NewState.Sorts.length; i++) {
    var Sort = NewState.Sorts[i];
    Sort.Order = i;
    this.State.Lists[LIST_SORTBY].Items[Sort.Code] = Sort;
    this.State.Lists[LIST_SORTBY].Orders[i] = Sort.Code;
  }

  this.State.Lists[LIST_GROUPBY].Items = new Array();
  this.State.Lists[LIST_GROUPBY].Orders = new Array();
  for (var i = 0; i < NewState.Groups.length; i++) {
    var Group = NewState.Groups[i];
    Group.Order = this.State.Lists[LIST_GROUPBY].Orders.length;
    this.State.Lists[LIST_GROUPBY].Items[Group.Code] = Group;
    this.State.Lists[LIST_GROUPBY].Orders[i] = Group.Code;
  }
};

CGFiltersWizard.prototype.applyState = function () {

  this.extFilter.dom.value = this.State.Filter;
  this.extFilterEmpty.dom.style.display = (this.State.Filter <= 0) ? "block" : "none";

  var extSortByList = this.extWizardLayer.select(CSS_SORTBYLIST + " li");
  extSortByList.each(function (extSortByItem) {
    this.unselectSortByItem(extSortByItem);
  }, this);

  for (var i = 0; i < this.State.Lists[LIST_SORTBY].Orders.length; i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    var Sort = this.State.Lists[LIST_SORTBY].Items[CodeSort];
    var extItem;

    if (i == 0) extItem = this.extWizardLayer.select(CSS_SORTBYLIST + " ." + Sort.Code).first();
    else {
      var extSection = this.createSortByListSection();
      extItem = extSection.select("." + Sort.Code).first();
    }

    if (extItem == null) continue;

    this.selectSortByItem(extItem);
    if (Sort.Mode == MODE_ASCENDANT) {
      extItem.addClass(CLASS_ASCENDANT);
      extItem.removeClass(CLASS_DESCENDANT);
    }
    else {
      extItem.removeClass(CLASS_ASCENDANT);
      extItem.addClass(CLASS_DESCENDANT);
    }
  }

  var extGroupByList = this.extWizardLayer.select(CSS_GROUPBYLIST + " li");
  extGroupByList.each(function (extGroupByItem) {
    this.unselectGroupByItem(extGroupByItem);
  }, this);

  var aDummyGroupsByItems = new Object();
  var aDummyGroupsByOrders = new Array();
  for (var i = 0; i < this.State.Lists[LIST_GROUPBY].Orders.length; i++) {
    var CodeGroup = this.State.Lists[LIST_GROUPBY].Orders[i];
    var Group = this.State.Lists[LIST_GROUPBY].Items[CodeGroup];
    var extItem = this.extWizardLayer.select(CSS_GROUPBYLIST + " ." + Group.Code).first();
    if (extItem == null) continue;
    this.selectGroupByItem(extItem, Group.Value);
    aDummyGroupsByItems[CodeGroup] = Group;
    aDummyGroupsByOrders.push(CodeGroup);
  }
  this.State.Lists[LIST_GROUPBY].Orders = aDummyGroupsByOrders;
  this.State.Lists[LIST_GROUPBY].Items = aDummyGroupsByItems;
};

CGFiltersWizard.prototype.loadGroupByOptions = function (GroupBy, DOMSelectorWidget) {
	var sRequestUrl = this.Options.DataSource.RequestGroupByOptionsUrl;

	if (this.Options.DataSource.Remote) {
		Ext.Ajax.request({
			url: sRequestUrl,
			params: "query=" + this.State.Filter + "&code=" + GroupBy.Code,
			method: "GET",
			callback: function (sOptions, bSuccess, Response) {
                var Options = new Array();
				eval(Response.responseText.replace(/\n/g, ""));
				GroupBy.Options = Options;
				this.renderGroupByOptions(GroupBy, DOMSelectorWidget);
			},
			scope: this
		});
	}
};

CGFiltersWizard.prototype.renderGroupByOptions = function(GroupBy, DOMGroupBySelectorWidget) {
	var RecordDefinition = new Ext.data.Record.create({value: 'value'}, {text: 'text'});
	var Store = DOMGroupBySelectorWidget.store;
	var Options = GroupBy.Options;
	var RecordAll = Store.getAt(0);

	Store.removeAll();
	Store.add(RecordAll);
	for (var index in Options) {
		if (isFunction(Options[index])) continue;
		var Option = Options[index];

		Store.add(new RecordDefinition({value: Option.Code, text: Option.Label}));
	}

    DOMGroupBySelectorWidget.list.setHeight(305);
};

CGFiltersWizard.prototype.queryGroupByOptions = function (C, B) {
	if (C === undefined || C === null) {
		C = ""
	}
	var A = {query: C, forceAll: B, combo: this, cancel: false};
	if (this.fireEvent("beforequery", A) === false || A.cancel) {
		return false
	}
	C = A.query;
	B = A.forceAll;
	if (B === true || (C.length >= this.minChars)) {
		if (this.lastQuery != C) {
			this.lastQuery = C;
			if (this.mode == "local") {
				this.selectedIndex = -1;
				if (B) {
					this.store.clearFilter()
				} else {
					this.store.filter(this.displayField, C, true);
				}
				this.onLoad()
			} else {
				this.store.baseParams[this.queryParam] = C;
				this.store.load({params: this.getParams(C)});
				this.expand()
			}
		} else {
			this.selectedIndex = -1;
			this.onLoad()
		}
	}
};

CGFiltersWizard.prototype.updateGroupByOptions = function (GroupBy, DOMSelectorWidget, loadRemote) {
    if (DOMSelectorWidget.store.getCount() > 1)
		return;

    if (GroupBy.Options.length > 0)
        this.renderGroupByOptions(GroupBy, DOMSelectorWidget);
    else if (loadRemote)
        this.loadGroupByOptions(GroupBy, DOMSelectorWidget);
};

CGFiltersWizard.prototype.addItemsGroup = function (extContainer, group) {
  var ContentTemplate = new Template(aFiltersWizardTemplates[this.language]["LIST_VIEWER_GROUP_ITEM_TEMPLATE"]);
  var sContent = ContentTemplate.evaluate(this.Options.DataSource.Groups[group]);
  new Insertion.Bottom(extContainer.dom, sContent);
};

CGFiltersWizard.prototype.refresh = function () {
  if (this.isRefreshing) return;
  this.isRefreshing = true;
  this.applyState();
  this.update();
};

CGFiltersWizard.prototype.unselectSortByItem = function (extItem) {
  var extSortByOptions = extItem.select(CSS_SORTBYOPTIONS).first();
  extItem.removeClass(CLASS_SELECTED);
  extItem.removeClass(CLASS_ASCENDANT);
  extItem.removeClass(CLASS_DESCENDANT);
  extItem.addClass(CLASS_ASCENDANT);
  if (extSortByOptions != null) extSortByOptions.dom.style.display = "none";
};

CGFiltersWizard.prototype.removeSortByListSectionsFromItem = function (extItem) {
  var extSections = this.extWizardLayer.select(CSS_LIST_SECTION);
  var extLastSection;

  for (var i = 0; i < extSections.getCount(); i++) {
    var CodeSort = this.State.Lists[LIST_SORTBY].Orders[i];
    if (CodeSort == null) {
      var extSection = extSections.elements[i];
      if (extSection) extSection.remove();
    }
  }

  extSections = this.extWizardLayer.select(CSS_LIST_SECTION);
  extLastSection = extSections.last();
  if (extLastSection) extLastSection.removeClass(CLASS_READONLY);
  this.extMoreSortByList.dom.style.display = (extSections.getCount() == this.Options.SortByList.CountItems) ? "none" : "block";
};

CGFiltersWizard.prototype.createSortByListSection = function () {
  var DOMList = this.extWizardLayer.select(CSS_SORTBYLIST).first().dom;
  var extSections = Ext.get(DOMList).select(CSS_LIST_SECTION);
  var iNumSections = extSections.getCount();
  var extLastSection = extSections.last();
  var extLayer = Ext.get(new Insertion.Bottom(DOMList, aFiltersWizardTemplates[this.language]["SORTBYLIST_SECTION"]).element.immediateDescendants().last());
  var ListItemTemplate = new Template(aFiltersWizardTemplates[this.language]["SORTBYLIST_ITEM"]);

  if (extLastSection) extLastSection.addClass(CLASS_READONLY);
  this.extMoreSortByList.dom.style.display = "none";

  var extSortByOptions = extLayer.select(CSS_SORTBYOPTIONS).first();
  if (extSortByOptions == null) {
    new Insertion.Bottom(extLayer.dom, new Template(aFiltersWizardTemplates[this.language]["SORTBYLIST_SECTION_OPTIONS"]).evaluate());
    extSortByOptions = extLayer.select(CSS_SORTBYOPTIONS).first();
    var extSortByOptionsUnselect = extLayer.select(CSS_SORTBYOPTIONS + " " + CSS_UNSELECT).first();
    Event.observe(extSortByOptionsUnselect.dom, "click", CGFiltersWizard.prototype.atSortBySectionUnselectClick.bind(this, extLayer.dom));
  }
  extSortByOptions.show();

  var i = 0;
  for (var Code in this.Options.SortByList.Items) {
    if (isFunction(this.Options.SortByList.Items[Code])) continue;
    var Item = this.Options.SortByList.Items[Code];
    var StateItem = this.State.Lists[LIST_SORTBY].Items[Code];
    var DOMItem = new Insertion.Bottom(extLayer.dom, ListItemTemplate.evaluate({"code": Item.Code, "label": Item.Label, "description": Item.Description})).element.immediateDescendants().last();
    DOMItem.Code = Item.Code;
    DOMItem.addClassName(CLASS_ASCENDANT);
    if (StateItem != null && StateItem.Order <= iNumSections) DOMItem.addClassName(CLASS_READONLY);
    Event.observe(DOMItem, "click", CGFiltersWizard.prototype.atListItemClick.bind(this, LIST_SORTBY, DOMItem));
    i++;
  }

  return extLayer;
};

CGFiltersWizard.prototype.removeSortByListSection = function (DOMSection) {
  DOMSection.remove();

  var extLastSection = this.extWizardLayer.select(CSS_LIST_SECTION).last();
  if (extLastSection) {
    extLastSection.removeClass(CLASS_READONLY);
    var extSelectedItem = extLastSection.select(CSS_SELECTED).first();
    this.extMoreSortByList.dom.style.display = (extSelectedItem != null) ? "block" : "none";
  }
  else this.extMoreSortByList.dom.style.display = "block";
};

CGFiltersWizard.prototype.selectSortByItem = function (extItem) {
  this.removeSortByListSectionsFromItem(extItem);
  if (!extItem.hasClass(CLASS_SELECTED)) extItem.addClass(CLASS_SELECTED);
  if (extItem.hasClass(CLASS_READONLY)) extItem.removeClass(CLASS_READONLY);
};

CGFiltersWizard.prototype.toggleSortByItemMode = function (extItem) {
  var DOMItem = extItem.dom;
  var Sort = this.State.Lists[LIST_SORTBY].Items[DOMItem.Code];

  DOMItem.toggleClassName(CLASS_ASCENDANT);
  DOMItem.toggleClassName(CLASS_DESCENDANT);
  Sort.Mode = DOMItem.hasClassName(CLASS_ASCENDANT) ? MODE_ASCENDANT : MODE_DESCENDANT;
};

CGFiltersWizard.prototype.unselectGroupByItem = function (extItem) {
  var Widget = this.groupByWidgets[extItem.dom.Code];
  if (Widget != null)
    Widget.setValue("all");
};

CGFiltersWizard.prototype.selectGroupByItem = function (extItem, sValue) {
  var Widget = this.groupByWidgets[extItem.dom.Code];
  if (Widget != null)
    Widget.setValue(sValue);
};

CGFiltersWizard.prototype.clearFilter = function () {
  this.State.Filter = "";
  this.extFilter.dom.value = "";
  this.extFilterEmpty.dom.style.display = "block";
  if (this.onClearFilter) this.onClearFilter();
  if (this.onUpdateState) this.onUpdateState(this.getState());
};

CGFiltersWizard.prototype.filter = function () {
  this.State.Filter = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (this.State.Filter.length <= 0) ? "block" : "none";
  if (this.onFilter) this.onFilter(this.State.Filter);
  if (this.onUpdateState) this.onUpdateState(this.getState());
};

CGFiltersWizard.prototype.addItemOnSelection = function (CodeList, Item) {
  var AuxItem = this.State.Lists[CodeList].Items[Item.Code];

  if (AuxItem) {
    Item.Order = AuxItem.Order;
    this.State.Lists[CodeList].Items[Item.Code] = Item;
  }
  else {
    this.State.Lists[CodeList].Orders.push(Item.Code);
    Item.Order = this.State.Lists[CodeList].Orders.length - 1;
    this.State.Lists[CodeList].Items[Item.Code] = Item;
  }
};

CGFiltersWizard.prototype.removeItemOnSelection = function (CodeList, CodeItem) {
  var sResult = this.State.Lists[CodeList].Orders.join(",");
  var iPos = sResult.indexOf(CodeItem);
  var aItems = new Array();

  if (iPos != -1) {
    if (iPos > 0) iPos = iPos - 1;
    sResult = sResult.substring(0, iPos) + sResult.substring(iPos + CodeItem.length + 1);
  }

  this.State.Lists[CodeList].Orders = (sResult.length > 0) ? sResult.split(",") : new Array();
  for (var i = 0; i < this.State.Lists[CodeList].Orders.length; i++) {
    var CodeItem = this.State.Lists[CodeList].Orders[i];
    aItems[CodeItem] = this.State.Lists[CodeList].Items[CodeItem];
    aItems[CodeItem].Order = i;
  }

  this.State.Lists[CodeList].Items = aItems;
};

CGFiltersWizard.prototype.sortByItem = function (DOMItem) {
  var DOMSection = DOMItem.up(CSS_LIST_SECTION);
  var extSelected = Ext.get(DOMSection).select(CSS_SELECTED).first();

  this.State.CurrentPage = 1;

  if (DOMItem.hasClassName(CLASS_SELECTED)) {
    this.toggleSortByItemMode(Ext.get(DOMItem));
    if (this.onSortBy) this.onSortBy();
    if (this.onUpdateState) this.onUpdateState(this.getState());
    return;
  }

  if (extSelected != null) {
    this.removeSortByListSectionsFromItem(extSelected);
    this.removeItemOnSelection(LIST_SORTBY, extSelected.dom.Code);
    this.unselectSortByItem(extSelected);
  }

  this.addItemOnSelection(LIST_SORTBY, {Code: DOMItem.Code, Mode: DOMItem.hasClassName(CLASS_ASCENDANT) ? MODE_ASCENDANT : MODE_DESCENDANT});
  this.selectSortByItem(Ext.get(DOMItem));
  if (this.onSortBy) this.onSortBy();
  if (this.onUpdateState) this.onUpdateState(this.getState());
};

CGFiltersWizard.prototype.setSortByList = function (SortByList) {
  this.Options.SortByList = SortByList;
  this.Options.SortByList.bUpdate = true;
};

CGFiltersWizard.prototype.setGroupByList = function (GroupByList) {
  this.Options.GroupByList = GroupByList;
  this.Options.GroupByList.bUpdate = true;
};

CGFiltersWizard.prototype.setDataSourceUrls = function (itemsUrl, groupByOptionsUrl) {
  this.Options.DataSource.Remote = true;
  this.Options.DataSource.RequestUrl = itemsUrl;
  this.Options.DataSource.RequestGroupByOptionsUrl = groupByOptionsUrl;
};

CGFiltersWizard.prototype.dispose = function () {
  if (this.extFilter.dom == null || this.extFirst.dom == null ||
      this.extPrevious.dom == null || this.extNext.dom == null ||
      this.extLast.dom == null)
    return;

  Event.stopObserving(this.extFilter.dom, "keyup", CGFiltersWizard.prototype.atFilterKeyUp.bind(this));
  Event.stopObserving(this.extFilter.dom, "focus", CGFiltersWizard.prototype.atFilterFocus.bind(this));
  Event.stopObserving(this.extFilter.dom, "blur", CGFiltersWizard.prototype.atFilterBlur.bind(this));

  this.extWizardLayer = null;
};

CGFiltersWizard.prototype.atListItemClick = function (CodeList, DOMItem, EventLaunched) {
  var href = DOMItem.href;

  if (!href) {
    var DOMAnchor = DOMItem.down("a");
    if (!DOMAnchor) return;
    href = DOMAnchor.href;
  }

  if (CodeList == LIST_SORTBY) this.sortByItem(DOMItem);

  Event.stop(EventLaunched);
  return false;
};

CGFiltersWizard.prototype.atGroupByItemLoadOptions = function (GroupBy, DOMSelectorWidget) {
    this.updateGroupByOptions(GroupBy, DOMSelectorWidget, true);
	DOMSelectorWidget.selectText();
};

CGFiltersWizard.prototype.atGroupByItemOptionBeforeSelect = function (DOMSelectorWidget, Record, index) {
  this.State.CurrentPage = 1;

  var value = Record.data.value;
  if (value == "all") this.removeItemOnSelection(LIST_GROUPBY, DOMSelectorWidget.Code);
  else this.addItemOnSelection(LIST_GROUPBY, {Code: DOMSelectorWidget.Code, Value: value});

  if (this.onGroupBy) this.onGroupBy();
  if (this.onUpdateState) this.onUpdateState(this.getState());

  return true;
};

CGFiltersWizard.prototype.atSortBySectionUnselectClick = function (DOMSection, EventLaunched) {
  var extSection = Ext.get(DOMSection);
  var extSelectedItem = extSection.select(CSS_SELECTED).first();

  if (extSelectedItem != null) {
    extSelectedItem.removeClass(CLASS_SELECTED);
    this.removeItemOnSelection(LIST_SORTBY, extSelectedItem.dom.Code);
    this.removeSortByListSectionsFromItem(extSelectedItem);
  }
  else this.removeSortByListSection(DOMSection);

  if (this.onSortBy) this.onSortBy();
  if (this.onUpdateState) this.onUpdateState(this.getState());

  Event.stop(EventLaunched);
};

CGFiltersWizard.prototype.atFilterKeyUp = function (oEvent) {
  var codeKey = oEvent.keyCode;
  var sFilter = this.extFilter.dom.value;

  if ((codeKey == KEY_UP) || (codeKey == KEY_DOWN) || (codeKey == KEY_ENTER) || (codeKey == KEY_LEFT) || (codeKey == KEY_RIGHT) || (codeKey == KEY_CONTROL) || (codeKey == KEY_SHIFT)) return;
  if (sFilter != "" && sFilter.length < 3) return;

  window.clearTimeout(this.idTimeoutFilter);
  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 500);
};

CGFiltersWizard.prototype.atFilterFocus = function () {
  this.extFilterEmpty.dom.style.display = "none";
  this.extFilter.dom.select();
};

CGFiltersWizard.prototype.atFilterBlur = function () {
  var sValue = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (sValue.length <= 0) ? "block" : "none";
};

CGFiltersWizard.prototype.atFilterEmptyClick = function () {
  this.extFilter.focus();
};

CGFiltersWizard.prototype.atUnselectFilter = function (DOMUnselect) {
  if (DOMUnselect.Code == CODE_WORDS_FILTER) {
    this.clearFilter();
  }
  else {
    var extItem = this.extWizardLayer.select(CSS_GROUPBYLIST + " ." + DOMUnselect.Code).first();
    this.removeItemOnSelection(LIST_GROUPBY, extItem.dom.Code);
    this.unselectGroupByItem(extItem);
    if (this.onGroupBy) this.onGroupBy();
    if (this.onUpdateState) this.onUpdateState(this.getState());
  }
};

CGFiltersWizard.prototype.atMoreSortByList = function () {
  this.createSortByListSection();
};