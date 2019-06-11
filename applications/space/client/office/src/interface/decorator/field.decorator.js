CGDecoratorField = function () {
};

CGDecoratorField.prototype = new CGDecorator;

CGDecoratorField.prototype.execute = function (DOMField) {

  DOMField.CurrentWidget = null;
  this.addCommonMethods(DOMField);

  DOMField.addWidgetBehaviours = function () {
    var extWidget, Widget, extField = Ext.get(this);
    var Type = this.getType(), EditorType;

    if (Type == FIELD_TYPE_FORMULA) this.style.display = "none";
    if (!(extWidget = extField.select(CSS_WIDGET).filter(this.getWidgetCss()).first())) return;

    EditorType = (this.isMultiple()) ? FIELD_TYPE_LIST : Type;

    Widget = WidgetFactory.get(this.getWidgetType(), extWidget);
    this.IdWidget = Widget.getId();

    Widget.onKeyPress = this.atWidgetKeyPress.bind(this);
    Widget.onFormat = this.atWidgetFormat.bind(this);
    Widget.onFocused = this.atWidgetFocused.bind(this);
    Widget.onBlur = this.atWidgetBlur.bind(this);
    Widget.onAdd = this.atWidgetAdd.bind(this);
    Widget.onDelete = this.atWidgetDelete.bind(this);
    Widget.onRegister = this.atWidgetRegister.bind(this);
    Widget.onBeforeChange = this.atWidgetBeforeChange.bind(this);
    Widget.onChange = this.atWidgetChange.bind(this);
    Widget.onRefresh = this.atWidgetRefresh.bind(this);
    Widget.onEnter = this.atWidgetEnter.bind(this);
    Widget.onEscape = this.atWidgetEscape.bind(this);
    Widget.onLoadDefaultValue = this.atWidgetLoadDefaultValue.bind(this);
    Widget.onAddDefaultValue = this.atWidgetAddDefaultValue.bind(this);
    Widget.setEditor(EditorsFactory.get(EditorType));
    Widget.setTarget(this);
  };

  DOMField.isLockedByDefinition = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_LOCK);
  };

  DOMField.init = function () {
    var extField = Ext.get(this);

    this.addWidgetBehaviours();

    if (this.isComposite()) {
      var aFields = this.getFields();
      this.initFields(aFields);
      this.addTableViewBehaviours([this]);
    }
    else if (this.isSummation()) {
      this.addSummationBehaviours([this]);
    }
    else if (this.isNode()) {
      this.addTableViewBehaviours([this]);
    }

    this.setDirty(false);
    if (extField.hasClass(CLASS_LOCK)) this.lock();

    var extLabel = extField.down("label");
    if (extLabel == null) extLabel = extField.select(".label").first();
    if (extLabel == null) return;
    Event.observe(extLabel.dom, "click", this.atLabelClick.bind(this));
  };

  DOMField.deleteWidgetBehaviours = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;
    Widget.destroy();
    WidgetManager.unregister(this.IdWidget);
  };

  DOMField.destroy = function () {
    if (!this.IdWidget) return;
    if (this.isComposite()) this.destroyFields(this.getFields());
    this.deleteWidgetBehaviours();
  };

  DOMField.isInit = function () {
    return (this.IdWidget != null);
  };

  DOMField.getType = function () {
    var extField = Ext.get(this);
    if (extField.hasClass(CLASS_FIELD_SELECT)) return FIELD_TYPE_SELECT;
    else if (extField.hasClass(CLASS_FIELD_BOOLEAN)) return FIELD_TYPE_BOOLEAN;
    else if (extField.hasClass(CLASS_FIELD_DATE)) return FIELD_TYPE_DATE;
    else if (extField.hasClass(CLASS_FIELD_TEXT)) return FIELD_TYPE_TEXT;
    else if (extField.hasClass(CLASS_FIELD_PICTURE)) return FIELD_TYPE_PICTURE;
    else if (extField.hasClass(CLASS_FIELD_FILE)) return FIELD_TYPE_FILE;
    else if (extField.hasClass(CLASS_FIELD_NUMBER)) return FIELD_TYPE_NUMBER;
    else if (extField.hasClass(CLASS_FIELD_COMPOSITE)) return FIELD_TYPE_COMPOSITE;
    else if (extField.hasClass(CLASS_FIELD_LINK)) return FIELD_TYPE_LINK;
    else if (extField.hasClass(CLASS_FIELD_CHECK)) return FIELD_TYPE_CHECK;
    else if (extField.hasClass(CLASS_FIELD_DESCRIPTOR)) return FIELD_TYPE_DESCRIPTOR;
    else if (extField.hasClass(CLASS_FIELD_NODE)) return FIELD_TYPE_NODE;
    else if (extField.hasClass(CLASS_FIELD_SERIAL)) return FIELD_TYPE_SERIAL;
    else if (extField.hasClass(CLASS_FIELD_LOCATION)) return FIELD_TYPE_LOCATION;
    else if (extField.hasClass(CLASS_FIELD_SUMMATION)) return FIELD_TYPE_SUMMATION;
    return null;
  };

  DOMField.getWidgetType = function () {
    var extField = Ext.get(this);
    if (this.isMultiple() && this.isTableView()) return WIDGET_TABLE;
    else if (this.isMultiple()) return WIDGET_LIST;
    else if (extField.hasClass(CLASS_FIELD_SELECT)) return WIDGET_SELECT;
    else if (extField.hasClass(CLASS_FIELD_BOOLEAN)) return WIDGET_BOOLEAN;
    else if (extField.hasClass(CLASS_FIELD_DATE)) return WIDGET_DATE;
    else if (extField.hasClass(CLASS_FIELD_TEXT)) return WIDGET_TEXT;
    else if (extField.hasClass(CLASS_FIELD_PICTURE)) return WIDGET_PICTURE;
    else if (extField.hasClass(CLASS_FIELD_FILE)) return WIDGET_FILE;
    else if (extField.hasClass(CLASS_FIELD_NUMBER)) return WIDGET_NUMBER;
    else if (extField.hasClass(CLASS_FIELD_COMPOSITE)) return WIDGET_COMPOSITE;
    else if (extField.hasClass(CLASS_FIELD_LINK)) return WIDGET_LINK;
    else if (extField.hasClass(CLASS_FIELD_CHECK)) return WIDGET_CHECK;
    else if (extField.hasClass(CLASS_FIELD_DESCRIPTOR)) return WIDGET_DESCRIPTOR;
    else if (extField.hasClass(CLASS_FIELD_NODE)) return WIDGET_NODE;
    else if (extField.hasClass(CLASS_FIELD_SERIAL)) return WIDGET_SERIAL;
    else if (extField.hasClass(CLASS_FIELD_LOCATION)) return WIDGET_LOCATION;
    else if (extField.hasClass(CLASS_FIELD_SUMMATION)) return WIDGET_SUMMATION;
    return null;
  };

  DOMField.getWidgetCss = function () {
    var extField = Ext.get(this);
    if (this.isMultiple() && this.isTableView()) return CSS_WIDGET_TABLE;
    else if (this.isMultiple()) return CSS_WIDGET_LIST;
    else if (extField.hasClass(CLASS_FIELD_SELECT)) return CSS_WIDGET_SELECT;
    else if (extField.hasClass(CLASS_FIELD_BOOLEAN)) return CSS_WIDGET_BOOLEAN;
    else if (extField.hasClass(CLASS_FIELD_DATE)) return CSS_WIDGET_DATE;
    else if (extField.hasClass(CLASS_FIELD_TEXT)) return CSS_WIDGET_TEXT;
    else if (extField.hasClass(CLASS_FIELD_PICTURE)) return CSS_WIDGET_PICTURE;
    else if (extField.hasClass(CLASS_FIELD_FILE)) return CSS_WIDGET_FILE;
    else if (extField.hasClass(CLASS_FIELD_NUMBER)) return CSS_WIDGET_NUMBER;
    else if (extField.hasClass(CLASS_FIELD_COMPOSITE)) return CSS_WIDGET_COMPOSITE;
    else if (extField.hasClass(CLASS_FIELD_LINK)) return CSS_WIDGET_LINK;
    else if (extField.hasClass(CLASS_FIELD_CHECK)) return CSS_WIDGET_CHECK;
    else if (extField.hasClass(CLASS_FIELD_DESCRIPTOR)) return CSS_WIDGET_DESCRIPTOR;
    else if (extField.hasClass(CLASS_FIELD_NODE)) return CSS_WIDGET_NODE;
    else if (extField.hasClass(CLASS_FIELD_SERIAL)) return CSS_WIDGET_SERIAL;
    else if (extField.hasClass(CLASS_FIELD_LOCATION)) return CSS_WIDGET_LOCATION;
    else if (extField.hasClass(CLASS_FIELD_SUMMATION)) return CSS_WIDGET_SUMMATION;
    return null;
  };

  DOMField.isSuper = function () {
    var extField = Ext.get(this);
    return (extField.hasClass(CLASS_FIELD_SUPER));
  };

  DOMField.isSelect = function () {
    return (this.getType() == FIELD_TYPE_SELECT);
  };

  DOMField.isBoolean = function () {
    return (this.getType() == FIELD_TYPE_BOOLEAN);
  };

  DOMField.isDate = function () {
    return (this.getType() == FIELD_TYPE_DATE);
  };

  DOMField.isText = function () {
    return (this.getType() == FIELD_TYPE_TEXT);
  };

  DOMField.isPicture = function () {
    return (this.getType() == FIELD_TYPE_PICTURE);
  };

  DOMField.isFile = function () {
    return (this.getType() == FIELD_TYPE_FILE);
  };

  DOMField.isNumber = function () {
    return (this.getType() == FIELD_TYPE_NUMBER);
  };

  DOMField.isComposite = function () {
    return (this.getType() == FIELD_TYPE_COMPOSITE);
  };

  DOMField.isLink = function () {
    return (this.getType() == FIELD_TYPE_LINK);
  };

  DOMField.isCheck = function () {
    return (this.getType() == FIELD_TYPE_CHECK);
  };

  DOMField.isNode = function () {
    return (this.getType() == FIELD_TYPE_NODE);
  };

  DOMField.isSerial = function () {
    return (this.getType() == FIELD_TYPE_SERIAL);
  };

  DOMField.isLocation = function () {
    return (this.getType() == FIELD_TYPE_LOCATION);
  };

  DOMField.isSummation = function () {
    return (this.getType() == FIELD_TYPE_SUMMATION);
  };

  DOMField.getData = function () {
    var extField = Ext.get(this);
    var extData = extField.down(CSS_DATA);
    return (extData) ? extData.dom.value : "";
  };

  DOMField.getContent = function () {
    var aPath = this.getPath().split(".");
    var ParentAttribute = null;
    var FieldAttribute = new CGAttribute();
	var data = cleanNonAsciiCharacters(this.getData());

    FieldAttribute.unserialize(data);
    for (var i = 1; i < aPath.length - 1; i++) {
      var Attribute = new CGAttribute();
      Attribute.setCode(aPath[i]);
      if (ParentAttribute != null) ParentAttribute.getAttributeList().addAttribute(Attribute);
      ParentAttribute = Attribute;
    }

    if (ParentAttribute != null) {
      ParentAttribute.getAttributeList().addAttribute(FieldAttribute);
      return ParentAttribute.serialize();
    }

    return FieldAttribute.serialize();
  };

  DOMField.updateData = function (sData, bNotifyChanges) {
    var extField = Ext.get(this);
    var extData = extField.down(CSS_DATA);

    if (!extData) return;
    if ((bNotifyChanges) && (this.onBeforeChange)) this.onBeforeChange(this);

    extData.dom.value = sData;
    this.setDirty(true);
    if ((bNotifyChanges) && (this.onChange)) {
      this.onChange(this);
    }
  };

  DOMField.reset = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;
    Widget.reset();
  };

  DOMField.setData = function (sData) {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;
    Widget.setData(sData);
  };

  DOMField.update = function (sData) {
    var Widget = WidgetManager.get(this.IdWidget);

    if (Widget == null) return;

    Widget.onBeforeChange = null;
    Widget.onChange = null;

    Widget.setData(sData);

    Widget.onBeforeChange = this.atWidgetBeforeChange.bind(this);
    Widget.onChange = this.atWidgetChange.bind(this);
  };

  DOMField.fillWithDefaultData = function () {
    var sDefaultValue = this.getDefault();
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;
    if ((sDefaultValue != null) && (sDefaultValue != "")) Widget.setDefault(sDefaultValue);
  };

  DOMField.saveToMemento = function () {
    return {id: this.id, data: this.getData()};
  };

  DOMField.restoreFromMemento = function (oMemento) {
    var extField = Ext.get(this);
    var extData = extField.down(CSS_DATA);

    if (!extData) return;

    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;

    extData.dom.value = oMemento.data;
    this.setDirty(true);
    Widget.onChange = null;
    Widget.setData(oMemento.data);
    Widget.onChange = this.atWidgetChange.bind(this);
  };

  DOMField.getCode = function () {
    var extField = Ext.get(this);
    var extData = extField.down(CSS_DATA);
    return (extData) ? extData.dom.name : "";
  };

  DOMField.getId = function () {
    return this.getCode();
  };

  DOMField.getAbsoluteCode = function () {
    var code = "", extField = Ext.get(this);
    var extParent = extField;
    while ((extParent = extParent.up(".field")) != null) {
      if (extParent.dom.getType() != FIELD_TYPE_COMPOSITE) code += extParent.dom.getCode();
    }
    return ((code != "") ? code + FIELD_CODE_SEPARATOR : "") + this.getCode();
  };

  DOMField.getTitle = function (bMultipleTitle) {
    var extField = Ext.get(this);

    var extLabel = extField.down("label");
    if (extLabel == null) extLabel = extField.select(".label span").first();
    if (extLabel == null) return "";

    sOrder = "";
    if ((bMultipleTitle) && (this.isMultiple()) && (this.getCurrentWidget() != null)) {
      var Widget = this.getCurrentWidget();
      if (Widget != null) sOrder = " " + Widget.getOrder();
    }

    return extLabel.dom.innerHTML + sOrder;
  };

  DOMField.getAbsoluteTitle = function () {
    var sTitle = "", extField = Ext.get(this);
    var extParent = extField;
    while ((extParent = extParent.up(".field")) != null) {
      sTitle += extParent.dom.getTitle();
    }
    var extLabel = extField.down("label");
    if (extLabel == null) extLabel = extField.select(".label span").first();
    if (extLabel == null) return "";
    return ((sTitle != "") ? sTitle + DOT : "") + extLabel.dom.innerHTML;
  };

  DOMField.getDOMLabel = function () {
    var extField = Ext.get(this);
    return extField.down(HTML_LABEL).dom;
  };

  DOMField.getPath = function (bMultiplePath) {
    var Widget = WidgetManager.get(this.IdWidget);
    if ((bMultiplePath) && (this.isMultiple()) && (this.getCurrentWidget() != null)) Widget = this.getCurrentWidget();
	if (Widget == null) return "";
    return Widget.getPath();
  };

  DOMField.getParentPath = function() {
    var path = this.getPath();
    return path.substr(0, path.lastIndexOf("."));
  };

  DOMField.sameParent = function(DOMField) {
    if (DOMField == null) return false;
    return this.getParentPath() === DOMField.getParentPath();
  },

  DOMField.isEqualsTo = function (Value) {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return false;
    var aIndicators = Widget.getData().value;
    for (var i = 0; i < aIndicators.length; i++) {
      if (aIndicators[i].value == Value)
        return true;
    }
    return false;
  };

  DOMField.getContext = function () {
    var aItems = new Array();
    var extField = Ext.get(this);

    if ((this.isMultiple()) && (this.getCurrentWidget() != null)) {
      aItems.push({Title: this.getTitle(true), Path: this.getPath(true)});
    }
    aItems.push({Title: this.getTitle(false), Path: this.getPath(false)});

    if (!this.getAncestors()) return aItems;

    var aDOMAncestors = this.getAncestors();
    for (var iPos = 0; iPos < aDOMAncestors.length; iPos++) {
      var DOMAncestor = aDOMAncestors[iPos];
      if ((DOMAncestor.isMultiple()) && (DOMAncestor.getCurrentWidget() != null)) {
        aItems.push({Title: DOMAncestor.getTitle(true), Path: DOMAncestor.getPath(true)});
      }
      aItems.push({Title: DOMAncestor.getTitle(false), Path: DOMAncestor.getPath(false)});
    }

    var extForm = extField.up(CSS_NODE);
    aItems.push({Id: null, Title: extForm.dom.getTitle()});

    return aItems;
  };

  DOMField.getInfo = function () {
    var extITitle, extIHelp, extEHelp;
    var Info = new Object();
    var Id = this.id;
    var sTitle = this.getTitle();
    var extField = Ext.get(this);

    Info.Id = Id;
    Info.Title = sTitle;
    Info.Description = ((extITitle = extField.select(CSS_ITITLE).first()) != null) ? extITitle.dom.innerHTML : null; //skip possible required widget
    Info.iHelp = ((extIHelp = extField.select(CSS_IHELP).first()) != null) ? extIHelp.dom.innerHTML : null; //skip possible required widget
    Info.eHelp = ((extEHelp = extField.select(CSS_EHELP).first()) != null) ? extEHelp.dom.innerHTML : null; //skip possible required widget
    Info.Context = this.getContext();
    Info.Required = this.isRequired();

    return Info;
  };

  DOMField.getStore = function (Type) {
    var extField = Ext.get(this), extStore;
    var extDomain = extField.down(CSS_FIELD_DEF_DATA_DOMAIN);
    var extFlatten, extDepth, extFrom, extPartnerContext, extFilters;

    if ((extStore = extField.down(Type)) == null) return;

    extFlatten = extField.down(CSS_FIELD_DEF_FLATTEN);
    extDepth = extField.down(CSS_FIELD_DEF_DEPTH);
    extFrom = extField.down(CSS_FIELD_DEF_FROM);
    extPartnerContext = extField.down(CSS_FIELD_DEF_PARTNER_CONTEXT);
    extFilters = extField.down(CSS_FIELD_DEF_FILTERS);

    var Store = new Object();
    Store.Domain = (extDomain != null) ? extDomain.dom.innerHTML : "";
    Store.Id = extStore.dom.innerHTML;
    Store.ShowCode = extStore.hasClass(CLASS_FIELD_STORE_SHOWCODE);
    Store.Flatten = (extFlatten != null) ? extFlatten.dom.innerHTML : "";
    Store.Depth = (extDepth != null) ? extDepth.dom.innerHTML : "";
    Store.PartnerContext = (extPartnerContext != null) ? extPartnerContext.dom.innerHTML : "";
    Store.From = (extFrom != null) ? extFrom.dom.innerHTML : "";
    Store.IsRemote = (extStore.dom.tagName.toLowerCase() == HTML_PARAGRAPH.toLowerCase());
    Store.Filters = (extFilters != null) ? SerializerData.deserialize(extFilters.dom.innerHTML) : "";
    Store.Items = new Array();
    if (!Store.IsRemote) {
      for (var iPos = 0; iPos < extStore.dom.options.length; iPos++) {
        var DOMOption = extStore.dom.options[iPos];
        Store.Items.push([DOMOption.value, DOMOption.text]);
      }
    }
    return Store;
  };

  DOMField.isRequired = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_REQUIRED);
  };

  DOMField.isVisible = function () {
    return this.style.display != "none";
  };

  DOMField.isEditable = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_EDITABLE);
  };

  DOMField.isExtended = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_EXTENDED);
  };

  DOMField.isMultiple = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_MULTIPLE);
  };

  DOMField.isTableView = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_TABLE_VIEW);
  };

  DOMField.allowOthers = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_ALLOW_OTHERS);
  };

  DOMField.setDirty = function (bValue) {
    var extField = Ext.get(this);
    if (bValue) extField.addClass(CLASS_DIRTY);
    else extField.removeClass(CLASS_DIRTY);
  };

  DOMField.isDirty = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_DIRTY);
  };

  DOMField.belongsToTemplate = function () {
    var extField = Ext.get(this);
    return (extField.up(CSS_TEMPLATE) != null);
  };

  DOMField.isFocused = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_FOCUS);
  };

  DOMField.focus = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    this.initWidget();

    if (Widget == null) return;

    if (this.isMultiple()) {
      var sPath = this.getPath(true);
      var Widget = WidgetManager.get(sPath);
      if (Widget == null) return;
      Widget.unLock();
      return;
    }

    if (!Widget.isFocused()) Widget.focus();
  };

  DOMField.blur = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    this.initWidget();

    if (Widget == null) return;

    if (this.isMultiple()) {
      var sPath = this.getPath(true);
      var Widget = WidgetManager.get(sPath);
      if (Widget == null) return;
      Widget.unLock();
      return;
    }

    if (Widget.isFocused()) Widget.blur();
  };

  DOMField.show = function () {
    var extField = Ext.get(this);
    extField.removeClass(CLASS_HIDDEN);
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget != null) Widget.show();
  };

  DOMField.hide = function () {
    var extField = Ext.get(this);
    extField.addClass(CLASS_HIDDEN);
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget != null) Widget.hide();
  };

  DOMField.lock = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;

    if (this.isMultiple()) {
      var sPath = this.getPath(true);
      var Widget = WidgetManager.get(sPath);
      Widget.lock();
      return;
    }

    Widget.lock();
  };

  DOMField.unLock = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;

    if (this.isMultiple()) {
      var sPath = this.getPath(true);
      var Widget = WidgetManager.get(sPath);
      if (Widget == null) return;
      Widget.unLock();
      return;
    }

    Widget.unLock();
  };

  DOMField.expand = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;
    if (Widget.expand) Widget.expand();
  };

  DOMField.collapse = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;
    if (Widget.collapse) Widget.collapse();
  };

  DOMField.initWidget = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return;
    if (Widget.isReady()) return;
    Widget.init();
  };

  DOMField.getWidget = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget.isReady()) this.initWidget();
    return Widget;
  };

  DOMField.getDefault = function () {
    var extField = Ext.get(this);
    var extDefault = extField.select(CSS_FIELD_DEF_DEFAULT).first();
    return (extDefault) ? extDefault.dom.innerHTML : "";
  };

  DOMField.getMessageWhenEmpty = function () {
    var extField = Ext.get(this);
    var extMessage = extField.select(CSS_FIELD_DEF_MESSAGE_WHEN_EMPTY).first();
    return (extMessage) ? extMessage.dom.innerHTML : "";
  };

  DOMField.getMessageWhenRequired = function () {
    var extField = Ext.get(this);
    var extMessage = extField.select(CSS_FIELD_DEF_MESSAGE_WHEN_REQUIRED).first();
    return (extMessage) ? extMessage.dom.innerHTML : "";
  };

  DOMField.getValue = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return "";
    if (!Widget.getValue) return "";
    return Widget.getValue();
  };

  DOMField.getValueCode = function () {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget == null) return "";
    if (!Widget.getValueCode) return "";
    return Widget.getValueCode();
  };

  DOMField.getAncestors = function () {
    var aDOMAncestors = new Array();
    var extAncestor, extField = Ext.get(this);

    extAncestor = extField.up(".field");
    while (extAncestor != null) {
      aDOMAncestors.push(extAncestor.dom);
      extAncestor = extAncestor.up(".field");
    }

    return aDOMAncestors;
  };

  DOMField.getBrother = function (Code) {
    var composite = Ext.get(this).up(".wcomposite");
    if (!composite) return false;
    var field = composite.select(DOT + Code + CSS_FIELD);
    if (field.getCount() == 0) return false;
    return field.first().dom;
  };

  DOMField.getCurrentWidget = function () {
    return this.CurrentWidget;
  };

  DOMField.gotoField = function (Path) {
    if (this.onGotoField) this.onGotoField(Path);
  };

  DOMField.saveToMememento = function () {
    return {id: this.id, data: this.stateMemento};
  };

  DOMField.setObserver = function (Observer, iPos) {
    var Widget = WidgetManager.get(this.IdWidget);
    if (Widget != null) Widget.setObserver(Observer, iPos);
  };

  DOMField.getNode = function () {
    var extField = Ext.get(this);
    var extNode = extField.up(CSS_NODE);
    return (extNode != null) ? extNode.dom : null;
  };

  DOMField.getNodeId = function () {
    var extField = Ext.get(this);
    var extNode = extField.up(CSS_NODE);
    return (extNode != null) ? extNode.dom.getId() : null;
  };

  // #############################################################################################################

  DOMField.atWidgetKeyPress = function (sValue, codeKey) {
    if (this.onKeyPress) this.onKeyPress(sValue, codeKey);
  };

  DOMField.atWidgetFormat = function (sValue) {
    if (this.onFormat) this.onFormat(sValue);
  };

  DOMField.atWidgetFocused = function (Widget) {
    var extField = Ext.get(this);
    if (this.getType() != FIELD_TYPE_COMPOSITE) {
      extField.addClass(CLASS_FOCUS);
    }
    this.CurrentWidget = Widget;
    if (this.onFocused) this.onFocused(this, Widget);
  };

  DOMField.atWidgetBlur = function () {
    var extField = Ext.get(this);
    if (this.getType() != FIELD_TYPE_COMPOSITE) {
      extField.removeClass(CLASS_FOCUS);
    }
    if (this.onBlur) this.onBlur(this);
  };

  DOMField.atWidgetAdd = function (DOMItem) {
    if (this.onAdd) this.onAdd(this, DOMItem);
    this.updateTableViewListSize(DOMField);
  };

  DOMField.atWidgetDelete = function (DOMItem) {
    this.updateTableViewListSize(DOMField);
  };

  DOMField.atWidgetRegister = function (DOMItem) {
    if (this.onRegister) this.onRegister(this, DOMItem);
  };

  DOMField.atWidgetBeforeChange = function (DOMField) {
    if ((DOMField) && (this.onBeforeChange)) this.onBeforeChange(DOMField);
  };

  DOMField.atWidgetChange = function (DOMField) {
    var Widget = WidgetManager.get(this.IdWidget);
    if ((DOMField) && (this.onChange)) this.onChange(DOMField);
    else if (Widget != null && Widget.getData() != null) this.updateData(Widget.getData().toXml(), true);
  };

  DOMField.atWidgetRefresh = function (DOMField) {
    if (this.onRefresh) this.onRefresh(DOMField);
  };

  DOMField.atWidgetEnter = function () {
    if (this.onEnter) this.onEnter();
  };

  DOMField.atWidgetEscape = function () {
    if (this.onEscape) this.onEscape();
  };

  DOMField.atWidgetLoadDefaultValue = function () {
    if (this.onLoadDefaultValue) this.onLoadDefaultValue(this);
  };

  DOMField.atWidgetAddDefaultValue = function () {
    if (this.onAddDefaultValue) this.onAddDefaultValue(this);
  };

  DOMField.atFieldFocused = function (DOMField, Widget) {
    if (this.onFocused) this.onFocused(DOMField, Widget);
  };

  DOMField.atFieldBlur = function (DOMField) {
    if (this.onBlur) this.onBlur(DOMField);
  };

  DOMField.atFieldAdd = function (DOMFieldsComposite, DOMFieldsCompositeItem) {
    var Widget = Ext.get(DOMFieldsCompositeItem).select(CSS_WIDGET).first().dom;
    if ((Widget != null) && (Widget.getFields)) this.initFields(Widget.getFields());
    else {
      if (DOMFieldsComposite.getFields) this.initFields(DOMFieldsComposite.getFields());
    }
  };

  DOMField.atFieldRegister = function (DOMFieldsComposite, DOMFieldsCompositeItem) {
    var Widget = Ext.get(DOMFieldsCompositeItem).select(CSS_WIDGET).first().dom;
    if ((Widget != null) && (Widget.getFields)) this.initFields(Widget.getFields());
  };

  DOMField.atFieldEnter = function (DOMField) {
    if (this.onEnter) this.onEnter(DOMField);
  };

  DOMField.atFieldEscape = function (DOMField) {
    if (this.onEscape) this.onEscape(DOMField);
  };

  DOMField.atFieldBeforeChange = function (DOMField) {
    if (this.onBeforeChange) this.onBeforeChange(DOMField);
  };

  DOMField.atFieldChange = function (DOMField) {
    var Widget = WidgetManager.get(this.IdWidget);
    if (this.onChange) this.onChange(DOMField);
    if (Widget != null) this.updateData(Widget.getData().toXml(), false);
  };

  DOMField.atFieldLabelClick = function (DOMField, oEvent) {
    if (this.onLabelClick) this.onLabelClick(DOMField);
  };

  DOMField.atLabelClick = function () {
    this.CurrentWidget = null;
    if (this.onLabelClick) this.onLabelClick(this);
  };

  DOMField.atFieldLoadDefaultValue = function (DOMField, oEvent) {
    if (this.onLoadDefaultValue) this.onLoadDefaultValue(DOMField);
  };

  DOMField.atFieldAddDefaultValue = function (DOMField, oEvent) {
    if (this.onAddDefaultValue) this.onAddDefaultValue(DOMField);
  };

  DOMField.isObserver = function (fieldCode) {
    var partnerContext = this.getStorePartnerContext ? this.getStorePartnerContext() : "";
    var from = this.getStoreFrom ? this.getStoreFrom() : "";
    var filters = this.getStoreFilters ? this.getStoreFilters() : new Array();
    var fieldPattern = "_field:" + fieldCode;

    if (partnerContext.indexOf(fieldPattern) != -1) return true;
    if (from.indexOf(fieldPattern) != -1) return true;

    for (var key in filters) {
      if (isFunction(filters[key])) continue;
      var filter = filters[key];
      if (filter.indexOf(fieldPattern) != -1) return true;
    }

    return false;
  };

  var Type = DOMField.getType();
  var Decorator = null;

  if (Type == FIELD_TYPE_DATE) Decorator = new CGDecoratorFieldDate();
  else if (Type == FIELD_TYPE_FILE) Decorator = new CGDecoratorFieldFile();
  else if (Type == FIELD_TYPE_LINK) Decorator = new CGDecoratorFieldLink();
  else if (Type == FIELD_TYPE_NUMBER) Decorator = new CGDecoratorFieldNumber();
  else if (Type == FIELD_TYPE_PICTURE) Decorator = new CGDecoratorFieldPicture();
  else if (Type == FIELD_TYPE_COMPOSITE) Decorator = new CGDecoratorFieldComposite();
  else if (Type == FIELD_TYPE_SELECT) Decorator = new CGDecoratorFieldSelect();
  else if (Type == FIELD_TYPE_BOOLEAN) Decorator = new CGDecoratorFieldBoolean();
  else if (Type == FIELD_TYPE_TEXT) Decorator = new CGDecoratorFieldText();
  else if (Type == FIELD_TYPE_FORMULA) Decorator = new CGDecoratorFieldFormula();
  else if (Type == FIELD_TYPE_DESCRIPTOR) Decorator = new CGDecoratorFieldDescriptor();
  else if (Type == FIELD_TYPE_CHECK) Decorator = new CGDecoratorFieldCheck();
  else if (Type == FIELD_TYPE_NODE) Decorator = new CGDecoratorFieldNode();
  else if (Type == FIELD_TYPE_SERIAL) Decorator = new CGDecoratorFieldSerial();
  else if (Type == FIELD_TYPE_LOCATION) Decorator = new CGDecoratorFieldLocation();
  else if (Type == FIELD_TYPE_SUMMATION) Decorator = new CGDecoratorFieldSummation();

  if (Decorator != null) Decorator.execute(DOMField);

};