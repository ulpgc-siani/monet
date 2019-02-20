ViewTaxonomies = function () {
  this.base = View;
  this.base();
  this.compareSelectors = new Object();
  this.filterSelectors = new Object();
  this.hasFilters = false;
};

ViewTaxonomies.prototype = new View;

ViewTaxonomies.prototype.init = function (DOMLayer) {

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewtaxonomies, Lang.ViewTaxonomies);

  if (!this.target) return;

  CommandListener.capture(this.DOMLayer);

  this.initTitles();
  this.addBehaviours();
};

ViewTaxonomies.prototype.isExpanded = function (jBlock) {
  if (jBlock.hasClass("compare") && jBlock.find("label:first").html().indexOf("-") != -1) return true;
  else if (jBlock.hasClass("filters") && jBlock.find("label:first").html().indexOf("-") != -1) return true;
  return false;
};

ViewTaxonomies.prototype.expandView = function (jBlock) {
  if (jBlock.hasClass("compare")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    jLabel.html(value.replace("+", "-"));
    jBlock.find("select:first").show();
    jBlock.find(".taxonomiesbox").show();
  }
  else if (jBlock.hasClass("filters")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    var jNoFilters = jBlock.find(".nofilters:first");
    jLabel.html(value.replace("+", "-"));
    jBlock.find("ul:first").show();
    if (!this.hasFilters) jNoFilters.show();
    else jNoFilters.hide();
  }
};

ViewTaxonomies.prototype.collapseView = function (jBlock) {
  if (jBlock.hasClass("compare")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    jLabel.html(value.replace("-", "+"));
    jBlock.find("select:first").hide();
    jBlock.find(".taxonomiesbox").hide();
  }
  else if (jBlock.hasClass("filters")) {
    var jLabel = jBlock.find("label:first");
    var value = jLabel.html();
    jLabel.html(value.replace("-", "+"));
    jBlock.find("ul:first").hide();
    jBlock.find(".nofilters:first").hide();
  }
};

ViewTaxonomies.prototype.toggleView = function (jBlock) {
    if (this.isExpanded(jBlock))
      this.collapseView(jBlock);
    else
      this.expandView(jBlock);
};

ViewTaxonomies.prototype.addBehaviours = function () {
  var jLayer = $(this.DOMLayer);

  var jSelector = jLayer.find(".block.compare select");
  jSelector.change(ViewTaxonomies.prototype.atTaxonomyCompareChange.bind(this));

  var jClearCompare = jLayer.find(".block.compare .clearcompare");
  jClearCompare.click(ViewTaxonomies.prototype.atClearCompareClick.bind(this));
  jClearCompare.hide();

  var jClearFilters = jLayer.find(".block.filters .clearfilters");
  jClearFilters.click(ViewTaxonomies.prototype.atClearFiltersClick.bind(this));
  jClearFilters.hide();
};

ViewTaxonomies.prototype.refreshColors = function (selector) {
  if (selector == null) return;

  var selection = selector.getSelection();
  var options = selector.getOptions(selection);

  selector.clearColors();

  for (var i = 0; i < options.length; i++) {
    var option = options[i];
    var color = View.getColor(View.ColorContext.COMPARE, option.code);

    if (color == null)
      color = View.retainColor(View.ColorContext.COMPARE, option.code);

    option.setColor(color);
  }

};

ViewTaxonomies.prototype.refresh = function () {
  this.refreshCompare();
  this.refreshFilters();
};

ViewTaxonomies.prototype.refreshCompareCategoriesList = function (taxonomyId) {
  var selection = new Array();
  var jTaxonomiesBox = $(this.DOMLayer).find(".block.compare .taxonomiesbox");
  var jTaxonomyCategories = jTaxonomiesBox.find(".categories." + taxonomyId);

  jTaxonomiesBox.find(".categories").hide();

  if (State.compareTaxonomy == taxonomyId)
    selection = State.compareList.getKeys();

  if (this.compareSelectors[taxonomyId] != null) {
    this.compareSelectors[taxonomyId].setSelection(selection);
    this.compareSelectors[taxonomyId].render(jTaxonomyCategories.get(0));
    jTaxonomyCategories.show();
    return;
  }

  jTaxonomyCategories = $("<div class='categories " + taxonomyId + "'></div>");
  var provider = new CategoriesProvider(this.target.dashboard.code, taxonomyId);
  var selector = new HierarchyMultipleSelector({provider: provider, language: Context.Config.Language});

  jTaxonomiesBox.append(jTaxonomyCategories);
  jTaxonomyCategories.show();

  selector.onChange = ViewTaxonomies.prototype.atSelectorCompareChange.bind(this, taxonomyId);
  selector.onColorClick = ViewTaxonomies.prototype.atSelectorCompareColorClick.bind(this, taxonomyId);
  selector.onRender = ViewTaxonomies.prototype.atSelectorCompareRender.bind(this, taxonomyId);
  selector.taxonomyId = taxonomyId;
  selector.setSelection(selection);
  selector.render(jTaxonomyCategories.get(0));

  this.compareSelectors[taxonomyId] = selector;
};

ViewTaxonomies.prototype.refreshCompare = function () {
  var dimensionList = this.target.dashboard.model.dimensionList;
  var compareTaxonomy = State.compareTaxonomy;
  var jLayer = $(this.DOMLayer);
  var DOMSelector = jLayer.find(".block.compare select").get(0);
  var jTaxonomiesBox = jLayer.find(".block.compare .taxonomiesbox");

  if (State.indicatorList.size() > 1) {
    jLayer.find(".block.compare").hide();
    return;
  }

  jLayer.find(".block.compare").show();
  DOMSelector.innerHTML = "";
  jTaxonomiesBox.find(".categories").hide();

  var first = true;
  for (var i = 0; i < dimensionList.length; i++) {
    var dimension = dimensionList[i];

    for (var j = 0; j < dimension.taxonomies.length; j++) {
      var taxonomy = dimension.taxonomies[j];

      addSelectOption(DOMSelector, taxonomy.id, taxonomy.label, (taxonomy.id == compareTaxonomy));

      if ((first && compareTaxonomy == null) || taxonomy.id == compareTaxonomy)
        this.refreshCompareCategoriesList(taxonomy.id);

      first = false;
    }
  }
};

ViewTaxonomies.prototype.refreshFilters = function () {
  var dimensionList = this.target.dashboard.model.dimensionList;
  var compareTaxonomy = State.compareTaxonomy;
  var jLayer = $(this.DOMLayer);
  var jBlock = jLayer.find(".block.filters");
  var jList = jLayer.find(".block.filters > ul");
  var jNoFilters = jLayer.find(".nofilters");
  var filterTemplate = translate(AppTemplate.viewtaxonomiesfilter, Lang.ViewTaxonomies);

  this.hasFilters = false;

  jList.hide();
  jList.find(".filter").hide();
  jNoFilters.show();

  for (var i = 0; i < dimensionList.length; i++) {
    var dimension = dimensionList[i];

    for (var j = 0; j < dimension.taxonomies.length; j++) {
      var taxonomy = dimension.taxonomies[j];
      var taxonomyId = taxonomy.id;
      var selection = State.filterList.getCategoriesOfTaxonomy(taxonomyId);
      var jFilter = jList.find(".filter." + taxonomyId);
      var jCategories = jFilter.find(".categories");

      if (taxonomyId == compareTaxonomy) {
        jFilter.hide();
        continue;
      }

      this.hasFilters = true;

      if (this.filterSelectors[taxonomyId] != null) {
        this.filterSelectors[taxonomyId].setSelection(selection);
        this.filterSelectors[taxonomyId].render(jCategories.get(0));
        jFilter.show();
        continue;
      }

      var jFilter = $.tmpl(filterTemplate, { id: taxonomyId, label: taxonomy.label });
      jFilter.find("a.togglefilter").click(ViewTaxonomies.prototype.atFilterToggle.bind(this, jFilter));
      jFilter.find("a.togglefilter").mouseover(ViewTaxonomies.prototype.atFilterMouseOver.bind(this, jFilter));
      jFilter.find("a.togglefilter").mouseout(ViewTaxonomies.prototype.atFilterMouseOut.bind(this, jFilter));
      jFilter.find("a.selectallfilters").click(ViewTaxonomies.prototype.atFilterSelectAll.bind(this, jFilter, taxonomyId));
      jFilter.find("a.selectallfilters").mouseover(ViewTaxonomies.prototype.atFilterSelectAllMouseOver.bind(this, jFilter));
      jFilter.find("a.selectallfilters").mouseout(ViewTaxonomies.prototype.atFilterSelectAllMouseOut.bind(this, jFilter));
      jList.append(jFilter);

      var jCategories = jFilter.find(".categories");
      var provider = new CategoriesProvider(this.target.dashboard.code, taxonomyId);
      var selector = new HierarchyMultipleSelector({provider: provider, language: Context.Config.Language});

      selector.onChange = ViewTaxonomies.prototype.atSelectorFilterChange.bind(this, taxonomyId);
      selector.setSelection(selection);
      selector.render(jCategories.get(0));

      this.filterSelectors[taxonomyId] = selector;
    }
  }

  if (this.isExpanded(jBlock)) {
    if (this.hasFilters) {
      jList.show();
      jNoFilters.hide();
    }
  }
  else
    jNoFilters.hide();

};

ViewTaxonomies.prototype.refreshClearFilters = function () {
  var jLayer = $(this.DOMLayer);
  var jBlock = jLayer.find(".block.filters");
  var jClearFilters = jLayer.find(".block.filters .clearfilters");

  if (State.filterList.size() > 0)
    jClearFilters.show();
  else
    jClearFilters.hide();
};

ViewTaxonomies.prototype.compare = function (taxonomyId, selector) {
  var selection = (selector != null) ? selector.getSelection() : new Array();
  var jLayer = $(this.DOMLayer);
  var jClearCompare = jLayer.find(".block.compare .clearcompare");

  if (selection.length <= 0) {
    State.compareTaxonomy = null;
    jClearCompare.hide();
  }
  else {
    State.compareTaxonomy = taxonomyId;
    jClearCompare.show();
  }

  this.refreshFilters();
  this.refreshClearFilters();
  this.refreshColors(selector);

  State.compareList.clear();
  var options = selector != null ? selector.getOptions(selection) : new Array();
  for (var i = 0; i < options.length; i++)
    State.compareList.add(taxonomyId, options[i].code, options[i].label);

  CommandListener.throwCommand("refreshdashboard(" + this.target.dashboard.code + ")");
};

ViewTaxonomies.prototype.delayCompare = function (taxonomyId, selector) {
  if (this.compareTimeout) {
    window.clearTimeout(this.compareTimeout);
    this.compareTimeout = null;
  }
  this.compareTimeout = window.setTimeout(ViewTaxonomies.prototype.compare.bind(this, taxonomyId, selector), 800);
};

ViewTaxonomies.prototype.filter = function (taxonomyId, selector) {
  var selection = selector.getSelection();
  var options = selector.getOptions(selection);

  State.filterList.deleteCategoriesOfTaxonomy(taxonomyId);
  for (var i = 0; i < options.length; i++)
    State.filterList.add(taxonomyId, options[i].code, options[i].label);

  this.refreshClearFilters();

  CommandListener.throwCommand("refreshdashboard(" + this.target.dashboard.code + ")");
};

ViewTaxonomies.prototype.delayFilter = function (taxonomyId, selector) {
  if (this.filterTimeout) {
    window.clearTimeout(this.filterTimeout);
    this.filterTimeout = null;
  }
  this.filterTimeout = window.setTimeout(ViewTaxonomies.prototype.filter.bind(this, taxonomyId, selector), 800);
};

ViewTaxonomies.prototype.updateColor = function (context, id, color) {
  if (this.colorSelector == null) return;

  var option = this.colorSelector.getOption(id);
  option.setColor(color);

  CommandListener.throwCommand("setcolor(" + this.target.id + "," + context + "," + id + "," + (color.indexOf("#") == -1 ? "#" : "") + color + ")");
};

ViewTaxonomies.prototype.atTaxonomyCompareChange = function (selectorChanged) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.compare._expansible").get(0));
  var taxonomyId = $(".block.compare select option:selected").val();
  if (taxonomyId == "-1") return false;
  this.refreshCompareCategoriesList(taxonomyId);
  this.compare(taxonomyId, null);
};

ViewTaxonomies.prototype.atSelectorCompareChange = function (taxonomyId, selectorChanged) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.compare._expansible").get(0));
  this.delayCompare(taxonomyId, selectorChanged);
};

ViewTaxonomies.prototype.atSelectorCompareColorClick = function (taxonomyId, selectorChanged, code, extra) {
  this.toggleColorsDialog(View.ColorContext.COMPARE, code, extra.getColorDOM());
  this.colorSelector = selectorChanged;
};

ViewTaxonomies.prototype.atSelectorCompareRender = function (taxonomyId, selectorChanged) {
  this.refreshColors(selectorChanged);
};

ViewTaxonomies.prototype.atClearCompareClick = function () {
  State.compareTaxonomy = null;
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.compare._expansible").get(0));
  CommandListener.throwCommand("unselectcategories(" + this.target.id + ")");
  $(this.DOMLayer).find(".block.compare .clearcompare").hide();
};

ViewTaxonomies.prototype.atSelectorFilterChange = function (taxonomyId, selectorChanged) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  this.delayFilter(taxonomyId, selectorChanged);
};

ViewTaxonomies.prototype.atFilterToggle = function (jFilter, jEvent) {
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  jFilter.toggleClass("opened");
};

ViewTaxonomies.prototype.atFilterMouseOver = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "inline";
};

ViewTaxonomies.prototype.atFilterSelectAllMouseOver = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "inline";
};

ViewTaxonomies.prototype.atFilterMouseOut = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "none";
};

ViewTaxonomies.prototype.atFilterSelectAllMouseOut = function (jFilter, jEvent) {
  jFilter.find("a.selectallfilters").get(0).style.display = "none";
};

ViewTaxonomies.prototype.atFilterSelectAll = function (jFilter, taxonomyId, jEvent) {
  if (!this.hasFilters) return;
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  if (!jFilter.hasClass("opened")) jFilter.addClass("opened");
  this.filterSelectors[taxonomyId].selectAll();
  this.filter(taxonomyId, this.filterSelectors[taxonomyId]);
};

ViewTaxonomies.prototype.atClearFiltersClick = function () {
  if (!this.hasFilters) return;
  Desktop.expandWestLayer($(this.DOMLayer).find(".block.filters._expansible").get(0));
  CommandListener.throwCommand("unselectfilters(" + this.target.id + ")");
  $(this.DOMLayer).find(".block.filters .clearfilters").hide();
};