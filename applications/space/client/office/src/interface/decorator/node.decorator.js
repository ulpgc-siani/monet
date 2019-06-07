CGDecoratorNode = function () {
};

CGDecoratorNode.prototype = new CGDecorator;

CGDecoratorNode.prototype.execute = function (DOMNode) {

  DOMNode.Editors = null;
  DOMNode.extCurrentWidget = null;
  DOMNode.aMemento = new Array();
  this.addCommonMethods(DOMNode);

  DOMNode.initFields = function (aDOMFields) {

    if (this.Editors == null) return;

    for (var iPos = 0; iPos < aDOMFields.length; iPos++) {
      var DOMField = aDOMFields[iPos];
      if (DOMField.isInit()) continue;
      if ((FieldType = DOMField.getType()) == null) continue;

      if (FieldType == FIELD_TYPE_COMPOSITE) DOMField.Editors = this.Editors;
      DOMField.onFocused = this.atFieldFocused.bind(this);
      DOMField.onBlur = this.atFieldBlur.bind(this);
      DOMField.onAdd = this.atFieldAdd.bind(this);
      DOMField.onRegister = this.atFieldRegister.bind(this);
      DOMField.onEnter = this.atFieldEnter.bind(this);
      DOMField.onEscape = this.atFieldEscape.bind(this);
      DOMField.onLabelClick = this.atFieldLabelClick.bind(this);
      DOMField.onGotoField = this.gotoField.bind(this);
      DOMField.onLoadDefaultValue = this.atFieldLoadDefaultValue.bind(this);
      DOMField.onAddDefaultValue = this.atFieldAddDefaultValue.bind(this);
      DOMField.init();
      // avoid callings to onchange before initialization
      DOMField.onBeforeChange = this.atFieldBeforeChange.bind(this);
      DOMField.onChange = this.atFieldChange.bind(this);
      DOMField.getFieldValue = this.atGetFieldValue.bind(this, DOMField);
      DOMField.getFieldValueCode = this.atGetFieldValueCode.bind(this, DOMField);
    }

  };

  DOMNode.init = function (Editors) {
    var extNode = Ext.get(this);

    this.initToolbar(".header .content .toolbar");
    this.initTabs(CSS_NODE);

    var aExtCollections = extNode.select(CSS_COLLECTION);
    aExtCollections.each(function (aExtCollection) {
      aExtCollection.dom.init(Editors);
    }, this);
  };

  DOMNode.destroyFields = function (aDOMFields) {

    for (var iPos = 0; iPos < aDOMFields.length; iPos++) {
      var DOMField = aDOMFields[iPos];
      DOMField.onFocused = null;
      DOMField.onBlur = null;
      DOMField.onAdd = null;
      DOMField.onRegister = null;
      DOMField.onEnter = null;
      DOMField.onEscape = null;
      DOMField.onLabelClick = null;
      DOMField.onGotoField = null;
      DOMField.onLoadDefaultValue = null;
      DOMField.onAddDefaultValue = null;
      DOMField.onBeforeChange = null;
      DOMField.onChange = null;
      DOMField.destroy();
    }

  };

  DOMNode.destroy = function () {
    var extNode = Ext.get(this);
    var aExtCollections = extNode.select(CSS_COLLECTION);
    aExtCollections.each(function (aExtCollection) {
      aExtCollection.dom.destroy();
    }, this);
  };

  DOMNode.focus = function () {
  };

  DOMNode.blur = function () {
    if (this.extCurrentWidget) this.extCurrentWidget.blur();
  };

  DOMNode.refresh = function () {
    var extNode = Ext.get(this);

    if (!(NodeReferenceList = extNode.select(CSS_REFERENCE))) return false;
    if (!(HiddenableList = extNode.select(CSS_HIDDENABLE))) return false;

    var iCountNodesReferences = NodeReferenceList.getCount();
    HiddenableList.each(function (Hiddenable) {
      sVisibleClass = (Hiddenable.hasClass('inline')) ? "inline" : "block";
      Hiddenable.dom.style.display = (iCountNodesReferences != 0) ? sVisibleClass : "none";
    });
  };

  DOMNode.getSelectedNodesReferencesIds = function () {
    var extNode = Ext.get(this);
    var aInputs = extNode.select(HTML_INPUT);
    var aResult = new Array();

    aInputs.each(function (Input) {
      if (Input.dom.name == "") return;
      if (Input.dom.checked) {
        aResult.push(Input.dom.getIdNode());
      }
    });

    return aResult;
  };

  DOMNode.setSelectedNodesReferences = function (aIdNodes) {
    var aResult = new Array();

    this.selectNodesReferences(SELECT_NONE);

    for (var iPos in aIdNodes) {
      if (isFunction(aIdNodes[iPos])) continue;
      var aDOMNodeReferences = this.getNodeReferences(aIdNodes[iPos]);
      aDOMNodeReferences.each(function (DOMNodeReference) {
        var extInput;
        var extNodeReference = Ext.get(DOMNodeReference);
        if ((extInput = extNodeReference.select(HTML_INPUT).first()) == null) return;
        extInput.dom.checked = true;
        if (extInput.dom.select) extInput.dom.select();
      }, this);
    }

    return aResult;
  };

  DOMNode.selectNodesReferences = function (Type) {
    var aResult = new Array(), extNode = Ext.get(this);
    var aDOMInputs = null;

    switch (Type) {
      case SELECT_ALL:
        if (!(InputList = extNode.select(CSS_REFERENCE + " " + HTML_INPUT))) return false;
        aDOMInputs = selectAll(InputList, Type);
        break;
      case SELECT_NONE:
        if (!(InputList = extNode.select(CSS_REFERENCE + " " + HTML_INPUT))) return false;
        aDOMInputs = selectNone(InputList, Type);
        break;
      case SELECT_INVERT:
        if (!(InputList = extNode.select(CSS_REFERENCE + " " + HTML_INPUT))) return false;
        aDOMInputs = selectInvert(InputList, Type);
        break;
      case SELECT_HIGHLIGHTED:
        aResult = this.selectNodesReferences(SELECT_NONE);
        if (!(HighlightedList = extNode.select(CSS_REFERENCE + " " + HTML_INPUT + DOT + CLASS_HIGHLIGHTED))) return false;
        aDOMInputs = selectHighlighted(HighlightedList);
        break;
    }

    if (!aDOMInputs) return aResult;

    for (var iPos = 0; iPos < aDOMInputs.length; iPos++) {
      var DOMInput = aDOMInputs[iPos];
      if (!DOMInput.getIdNode) continue;
      aResult[DOMInput.getIdNode()] = DOMInput.checked;
    }

    return aResult;
  };

  DOMNode.markNodesReferences = function (aIdNodes, Mark) {
    var sMessage = "";
    if (Mark != null) eval("sMessage = (Lang.Decorator.NodeMark." + Mark + ")?Lang.Decorator.NodeMark." + Mark + ":''");

    for (var iPos in aIdNodes) {
      if (isFunction(aIdNodes[iPos])) continue;
      var aDOMNodeReferences = this.getNodeReferences(aIdNodes[iPos]);
      aDOMNodeReferences.each(function (DOMNodeReference) {
        var extMark;
        var extNodeReference = Ext.get(DOMNodeReference);
        if ((extMark = extNodeReference.select(CSS_MARK).first()) == null) return;
        extMark.dom.innerHTML = sMessage;
      }, this);
    }
  };

  DOMNode.getNodesReferencesInfo = function () {
    var extNode = Ext.get(this);
    var aNodesReferencesInfo;

    if (!(aNodesReferences = extNode.select(CSS_REFERENCE))) return false;

    aNodesReferencesInfo = new Array();
    aNodesReferences.each(function (CurrentNodeReference) {
      var NodeReferenceInfo = new Object();
      var extId, extTitle, extDescription;
      NodeReferenceInfo.id = CurrentNodeReference.dom.id.replace(NODEREFERENCE_ID_PREFIX, "");
      NodeReferenceInfo.idNode = (extId = CurrentNodeReference.select(CSS_CONTROL_INFO + " > .idnode").first()) ? extId.dom.innerHTML : "";
      NodeReferenceInfo.title = (extTitle = CurrentNodeReference.select(CSS_TITLE).first()) ? extTitle.dom.innerHTML : "Sin etiqueta";
      NodeReferenceInfo.description = (extDescription = CurrentNodeReference.select(CSS_DESCRIPTION).first()) ? extDescription.dom.innerHTML : "Sin comentarios";
      aNodesReferencesInfo.push(NodeReferenceInfo);
    });

    return aNodesReferencesInfo;
  };

  DOMNode.getNodesReferences = function () {
    var aResult = new Array(), extNode = Ext.get(this);
    var aNodeReferences;

    if (!(aNodeReferences = extNode.select(CSS_REFERENCE))) return false;

    aNodeReferences.each(function (CurrentNodeReference) {
      aResult.push(CurrentNodeReference.dom);
    }, this);

    return aResult;
  };

  DOMNode.getNodesReferencesCount = function () {
    var extNode = Ext.get(this);
    return (extNode.select(CSS_REFERENCE)).getCount();
  };

  DOMNode.getNodeReferences = function (IdNode) {
    var aResult = new Array(), extNode = Ext.get(this);
    var aNodeReferences;

    if (!(aNodeReferences = extNode.select(CSS_REFERENCE + DOT + IdNode))) return false;

    aNodeReferences.each(function (CurrentNodeReference) {
      aResult.push(CurrentNodeReference.dom);
    }, this);

    return aResult;
  };

  DOMNode.getNodeReferencesCount = function (IdNode) {
    var extNode = Ext.get(this);
    return (extNode.select(CSS_REFERENCE + DOT + IdNode)).getCount();
  };

  DOMNode.getMagnet = function (CodeType) {
    var extNode = Ext.get(this);
    if (!(eMagnet = extNode.select(".magnet#" + CodeType).first())) return false;
    return eMagnet.dom;
  };

  DOMNode.addNodeReference = function (CodeType, sNodeReferenceContent) {
    var extNode = Ext.get(this);

    if (!(eMagnet = this.getMagnet(CodeType))) {
      if (!(extNodeReferenceList = extNode.select(CSS_REFERENCE_LIST).first())) return false;
      DOMNodeReference = new Insertion.Bottom(extNodeReferenceList.dom, sNodeContent).element.immediateDescendants().last();
      this.refresh();
    }
    else {
      if (!(Section = $(eMagnet.innerHTML))) return false;
      DOMNodeReference = Section.addNodeReference(sNodeReferenceContent);
    }

    return DOMNodeReference;
  };

  DOMNode.deleteNodeReferences = function (Id) {
    var aExtNodeReferences, extNode = Ext.get(this);

    if ((aExtNodeReferences = extNode.select(CSS_REFERENCE + DOT + Id)) == null) return false;

    aExtNodeReferences.each(function (extNodeReference) {
      if ((extNode = extNodeReference.up(CSS_COLLECTION)) != null) {
        extNode.dom.deleteNodeReference(Id);
      }
      else {
        extNodeReference.remove();
        this.refresh();
      }
    }, this);
  };

  DOMNode.scrollTo = function (Id, bAnimate) {
    var eElement = $(SECTION_ID_PREFIX + Id);
    if (!eElement) eElement = $(NODEREFERENCE_ID_PREFIX + Id);
    if (!eElement) return false;
    if (bAnimate) this.highlight();
    return eElement.scrollTo();
  };

  DOMNode.isAncestor = function (IdNode) {
    var extNode = Ext.get(this);
    var extBreadcrumbs = extNode.select(".breadcrumbs").first();
    var sBreadcrumbs = extBreadcrumbs.dom.innerHTML;

    return (sBreadcrumbs.indexOf("shownode%28" + IdNode + "%29") != -1) || (sBreadcrumbs.indexOf("shownode(" + IdNode + ")") != -1);
  };

  DOMNode.isHighlighted = function () {
    var extNode = Ext.get(this);
    if (!(eHighlightedLink = extNode.select(".command.star").first())) return false;
    return (eHighlightedLink.dom.hasClassName(CLASS_HIGHLIGHTED));
  };

  DOMNode.setHighlighted = function (bHighlight) {
    var extNode = Ext.get(this);
    if (!(extHighlightedLink = extNode.select(".command.star").first())) return false;
    if (bHighlight) extHighlightedLink.addClass(CLASS_HIGHLIGHTED);
    else extHighlightedLink.removeClass(CLASS_HIGHLIGHTED);
  };

  DOMNode.highlight = function () {
    new Effect.Highlight(this, {duration: HIGHLIGHT_DURATION});
  };

  DOMNode.getHtmlDialog = function () {
    var extNode = Ext.get(this);
    if (!(eDialog = extNode.select('.dialog').first())) return false;
    return eDialog.dom.innerHTML;
  };

  DOMNode.getFieldsDefinition = function () {
    var aResult = new Array();
    var extNode = Ext.get(this);

    aExtFields = extNode.select('.field');
    aExtFields.each(function (extField) {
      if (extField.up(CSS_NODE).dom != this) return;
      var Type = extField.dom.getType();
      if ((Type == FIELD_TYPE_TEXT) || (Type == FIELD_TYPE_NUMBER) || (Type == FIELD_TYPE_DATE)) {
        var codeField = extField.dom.getAbsoluteCode();
        var sTitle = extField.dom.getAbsoluteTitle();
        aResult.push({code: codeField, id: codeField, name: codeField, title: sTitle, type: Type});
      }
    }, this);

    return aResult;
  };

  DOMNode.getDefinition = function () {
    var Definition = new Object();
    Definition.aFields = this.getFieldsDefinition();
    return Definition;
  };

  DOMNode.isEditable = function () {
    var extNode = Ext.get(this);
    return (extNode.hasClass(CLASS_EDITABLE));
  };

  DOMNode.getChildNodeId = function (code) {
    var extNode = Ext.get(this);
    if (!(extChild = extNode.select(CSS_CONTROL_INFO + " > " + CSS_CHILD_NODE + " ." + code))) return "";
    return extChild.dom.value;
  };

  DOMNode.getChildrenNodes = function (IdChild) {
    var extNode = Ext.get(this);
    var extChildrenList = extNode.select(CSS_NODE + "." + IdChild);
    var aResult = new Array();

    extChildrenList.each(function (extChild) {
      aResult.push(extChild.dom);
    });

    return aResult;
  };

  DOMNode.getIndicatorValue = function () {
    var aAttributes = this.getAttributes();
    return aAttributes;
  };

  DOMNode.getFields = function () {
    var aFields = new Array();
    var aComposites = new Array();
    var ControlInfo = this.getControlInfo();
    var extNode = Ext.get(this);
    var extFieldList;

    if (!(extFieldList = extNode.select(CSS_FIELD_NODE_PREFIX + ControlInfo.IdNode))) return false;

    extFieldList.each(function (extField) {
      var extParent = extField.up(CSS_FIELD_NODE_PREFIX + ControlInfo.IdNode);
      if (extParent != null) return;
      if (extField.dom.belongsToTemplate()) return;
      if (extField.hasClass(CLASS_FIELD_COMPOSITE)) aComposites.push(extField.dom);
      else aFields.push(extField.dom);
    }, this);

    // Priorize fields vs composites to minimize onchange fields calls
    for (var iPos = aComposites.length - 1; iPos >= 0; iPos--) aFields.push(aComposites[iPos]);

    return aFields;
  };

  DOMNode.getField = function (Code) {
    var extNode = Ext.get(this);
    var extFieldList = extNode.select(CSS_FIELD + "." + Code);
    if (extFieldList.getCount() == 0) return null;
    var DOMField = extFieldList.first().dom;
    if (DOMField.belongsToTemplate()) return null;
    return DOMField;
  };

  DOMNode.getFieldsByCode = function (Code) {
    var aFields = new Array();
    var extNode = Ext.get(this);
    extNode.select(CSS_FIELD + "." + Code).each(function (el) {
      aFields.push(el.dom);
    });
    return aFields;
  };

  DOMNode.getContent = function () {
    return "";
  };

  DOMNode.getEmbeddedNodes = function () {
    var aResult = new Array();
    var extNode = Ext.get(this);
    var extNodeList;

    if (!(extNodeList = extNode.select(CSS_NODE))) return false;

    extNodeList.each(function (extNode) {
      aResult.push($(extNode.dom));
    }, this);

    return aResult;
  };

  DOMNode.isEmbedded = function () {
    var extNode = Ext.get(this);
    return extNode.hasClass(CLASS_EMBED);
  };

  DOMNode.getFieldNodes = function () {
    var aResult = new Array();
    var extNode = Ext.get(this);

    if (!(extNodeList = extNode.select(CSS_NODE_FIELD))) return false;

    extNodeList.each(function (extNode) {
      aResult.push($(extNode.dom));
    }, this);

    return aResult;
  };

  DOMNode.getCollections = function () {
    var aResult = new Array();
    var extNode = Ext.get(this);

    if (!(eCollectionList = extNode.select(CSS_COLLECTION))) return false;

    eCollectionList.each(function (extCollection) {
      aResult.push($(extCollection.dom));
    }, this);

    return aResult;
  };

  DOMNode.getForms = function (IdNode) {
    var aResult = new Array();
    var extNode = Ext.get(this);

    if (!(eFormList = extNode.select(".form." + IdNode))) return false;

    eFormList.each(function (extForm) {
      aResult.push($(extForm.dom));
    }, this);

    return aResult;
  };

  DOMNode.getForm = function (IdNode) {
    var eForm = null;
    var extNode = Ext.get(this);

    if (!(eForm = extNode.select(".form" + IdNode).first())) return false;
    if (!eForm) return false;

    return eForm.dom;
  };

  DOMNode.getIdNode = function () {
    return this.getControlInfo().IdNode;
  };

  DOMNode.getId = function () {
    return this.getIdNode();
  };

  DOMNode.getParentNode = function () {
    var extNode = Ext.get(this);
    var extElement = extNode.up(CSS_NODE);
    if (extElement) return extElement.dom;
    return null;
  };

  DOMNode.getTitle = function () {
    var extNode = Ext.get(this);
    var extTitle = extNode.select(".title .descriptor").first();
    if (extTitle == null) return Context.Config.DefaultLabel;
    return extTitle.dom.innerHTML;
  };

  DOMNode.setTitle = function (sTitle) {
    var extNode = Ext.get(this);
    var extTitle = extNode.select(".title .descriptor").first();
    var extBreadcrumbsTitle = extNode.select(".breadcrumbs span").first();
    if (extTitle != null) extTitle.dom.innerHTML = sTitle;
    if (extBreadcrumbsTitle != null) extBreadcrumbsTitle.dom.innerHTML = sTitle;
  };

  DOMNode.getControlInfo = function () {
    var aResult = new Array();
    var extNode = Ext.get(this);
    var extId, extAncestors, extCode, extCodeView, extMode, extNodes, extResult, extTimeStamp, extState;

    if (this.ControlInfo) return this.ControlInfo;

    extState = extNode.select(CSS_CONTROL_INFO + " > .state").first();
    this.ControlInfo = new Object();
    this.ControlInfo.IdNode = (extId = extNode.select(CSS_CONTROL_INFO + " > .idnode").first()) ? extId.dom.innerHTML : "-1";
    this.ControlInfo.IdRevision = (extId = extNode.select(CSS_CONTROL_INFO + " > .idrevision").first()) ? extId.dom.innerHTML : null;
    this.ControlInfo.Ancestors = (extAncestors = extNode.select(CSS_CONTROL_INFO + " > .ancestors").first()) ? extAncestors.dom.innerHTML : "-1";
    this.ControlInfo.Code = (extCode = extNode.select(CSS_CONTROL_INFO + " > .code").first()) ? extCode.dom.innerHTML : "";
    this.ControlInfo.CodeView = (extCodeView = extNode.select(CSS_CONTROL_INFO + " > .codeview").first()) ? extCodeView.dom.innerHTML : "-1";
    this.ControlInfo.Mode = (extMode = extNode.select(CSS_CONTROL_INFO + " > .mode").first()) ? extMode.dom.innerHTML.replace(/&amp;/g, AMP) : null;
    this.ControlInfo.Nodes = (extNodes = extNode.select(CSS_CONTROL_INFO + " > .nodes").first()) ? extNodes.dom.innerHTML : null;
    this.ControlInfo.TimeStamp = (extTimeStamp = extNode.select(CSS_CONTROL_INFO + " > .timestamp").first()) ? extTimeStamp.dom.innerHTML : null;
    this.ControlInfo.State = (extState != null && extState.dom.innerHTML != "") ? Ext.util.JSON.decode(extState.dom.innerHTML) : null;
    this.ControlInfo.Templates = new Object();

    aResult = extNode.select(".tpl.collectionreference.added");
    this.ControlInfo.Templates.NodeReferenceAdded = (extResult = aResult.first()) ? extResult.dom.innerHTML : null;

    aResult = extNode.select(".tpl.refresh");
    this.ControlInfo.Templates.Refresh = (extResult = aResult.first()) ? extResult.dom.innerHTML : null;

    aResult = extNode.select(".tpl.edit");
    this.ControlInfo.Templates.Edit = (extResult = aResult.first()) ? extResult.dom.innerHTML : null;

    return this.ControlInfo;
  };

  DOMNode.setDirty = function (bValue) {
    var extNode = Ext.get(this);
    if (!(extFieldList = extNode.select(CSS_FIELD))) return false;
    extFieldList.each(function (extField) {
      extField.dom.setDirty(bValue);
    }, this);
  };

  DOMNode.isDirty = function () {
    var bIsDirty = false;
    var extNode = Ext.get(this);

    if (!(extFieldList = extNode.select(CSS_FIELD))) return false;

    extFieldList.each(function (extField) {
      if (bIsDirty) return;
      if (!extField) return;
      if (!extField.dom) return;
      if (!extField.dom.isDirty) return;
      if (extField.dom.isDirty()) bIsDirty = true;
    }, this);

    return bIsDirty;
  };

  DOMNode.getFirstWidget = function (extElement) {
    var extNode = Ext.get(this);
    if (extElement == null) extElement = extNode;
    var extWidgetList = extElement.select('.widget');
    if (extWidgetList.getCount() == 0) return null;
    return extWidgetList.first();
  };

  DOMNode.isFirstWidget = function () {
    var extWidget = this.getFirstWidget();
    return (extWidget == this.extCurrentWidget);
  };

  DOMNode.firstField = function () {
    var extWidget = this.getFirstWidget();
    if (extWidget) {
      if ((this.extCurrentWidget != null) && (this.extCurrentWidget.dom != extWidget.dom)) this.extCurrentWidget.blur();
      this.extCurrentWidget = extWidget;
      extWidget.focus();
    }
  };

  DOMNode.getPreviousWidget = function () {
    var extWidget = null;

    if (this.isFirstWidget()) return;
    if (this.extCurrentWidget == null) return this.getFirstWidget();

    var extElement = this.extCurrentWidget.up('.element');
    if (extElement != null) extElement = getPreviousElement(extElement, 'element');
    if (extElement != null) extWidget = extElement.child('.widget');
    if (extWidget != null) return extWidget;

    extElement = this.extCurrentWidget.up('.field');
    if (extElement != null) {
      extParent = extElement;
      extElement = null;
      while ((extParent != null) && (extElement == null)) {
        extElement = getPreviousElement(extParent, 'field');
        extParent = extParent.up('.field');
      }
    }
    if (extElement != null) {
      var extWidget = this.getLastWidget(extElement);
      if (extWidget != null) return extWidget;
    }

    extElement = this.extCurrentWidget.up('.form');
    if (extElement != null) extElement = extElement.up('.field');
    if (extElement != null) extElement = getPreviousElement(extElement, 'field');
    if (extElement != null) {
      var extWidget = this.getLastWidget(extElement);
      if (extWidget != null) return extWidget;
    }

    extWidget = this.getLastWidget(this.extCurrentWidget);
    if (extWidget == null) extWidget = this.getFirstWidget();

    return extWidget;
  };

  DOMNode.previousField = function () {
    var extWidget = this.getPreviousWidget();
    if ((extWidget) && (this.extCurrentWidget != extWidget)) {
      if (this.extCurrentWidget != null) this.extCurrentWidget.blur();
      this.extCurrentWidget = extWidget;
      extWidget.focus();
    }
  };

  DOMNode.getNextWidget = function (extElement) {
    var extElement, extWidget;

    if (this.isLastWidget()) return this.getLastWidget();
    if (this.extCurrentWidget == null) return this.getFirstWidget();

    extWidget = this.extCurrentWidget.child('.widget');
    if ((extWidget != null) && (extWidget.up('.template') == null)) return extWidget;

    extWidget = null;
    extElement = this.extCurrentWidget.up('.element');
    if (extElement != null) extElement = getNextElement(extElement, 'element');
    if (extElement != null) extWidget = extElement.child('.widget');
    if (extWidget != null) return extWidget;

    extElement = this.extCurrentWidget.up('.field');
    if (extElement != null) {
      extParent = extElement;
      extElement = null;
      while ((extParent != null) && (extElement == null)) {
        extElement = getNextElement(extParent, 'field');
        extParent = extParent.up('.field');
      }
    }
    if (extElement != null) extWidget = extElement.child('.widget');
    if (extWidget != null) return extWidget;

    extElement = this.extCurrentWidget.up('.form');
    if (extElement != null) extElement = extElement.up('.field');
    if (extElement != null) extElement = getNextElement(extElement, 'field');
    if (extElement != null) extWidget = extElement.child('.widget');
    if (extWidget != null) return extWidget;

    return this.getFirstWidget();
  };

  DOMNode.nextField = function () {
    var extWidget = this.getNextWidget();
    if ((extWidget) && (this.extCurrentWidget != extWidget)) {
      if (this.extCurrentWidget != null) this.extCurrentWidget.blur();
      this.extCurrentWidget = extWidget;
      extWidget.focus();
    }
  };

  DOMNode.isLastWidget = function () {
    var extWidget = this.getLastWidget();
    return (extWidget == this.extCurrentWidget);
  };

  DOMNode.getLastWidget = function (extElement) {
    var extWidget, extNode = Ext.get(this);
    if (extElement == null) extElement = extNode;
    var extWidgetList = extElement.select('.widget');
    if (extWidgetList.getCount() == 0) return null;

    for (var iPos = extWidgetList.getCount() - 1; iPos >= 0; iPos--) {
      extWidget = Ext.get(extWidgetList.elements[iPos]);
      if (extWidget.up('.template') == null) return extWidget;
    }

    return null;
  };

  DOMNode.lastField = function () {
    var extWidget = this.getLastWidget();
    if ((extWidget) && (this.extCurrentWidget != extWidget)) {
      if (this.extCurrentWidget != null) this.extCurrentWidget.blur();
      this.extCurrentWidget = extWidget;
      extWidget.focus();
    }
  };

  DOMNode.parentField = function () {
    if (this.extCurrentWidget == null) return;
    var extWidget = this.extCurrentWidget.up('.widget');
    if ((extWidget) && (this.extCurrentWidget != extWidget)) {
      this.extCurrentWidget.blur();
      this.extCurrentWidget = extWidget;
      extWidget.focus();
    }
  };

  DOMNode.gotoField = function (sPath) {
    var DOMElement = $(sPath);
    if (DOMElement) {
      if (Ext.isIE) window.setTimeout(DOMElement.focus, 100);
      else DOMElement.focus();
    }
  };

  DOMNode.getContext = function (DOMField) {
    return DOMField.getContext();
  };

  DOMNode.isField = function (extWidget, DOMField) {
    var extField = extWidget.up(".field");
    if (extField == null) return false;
    return (extField.dom == DOMField);
  };

  DOMNode.getCurrentField = function () {
    if (this.extCurrentWidget == null) return;
    var extField = this.extCurrentWidget.up(".field");
    if (!extField) return;
    return extField.dom;
  };

  DOMNode.addMemento = function (Memento) {
    var iPos = this.IndexMemento + 1;
    while (iPos < this.aMemento.length) this.aMemento.pop();
    this.aMemento.push(Memento);
    this.IndexMemento = this.aMemento.length - 1;
  };

  DOMNode.undo = function () {
    var Memento, CurrentMemento, DOMField;

    this.IndexMemento--;

    if (this.IndexMemento < 0) {
      this.IndexMemento = 0;
      return;
    }

    if (this.aMemento.length == 0) return;

    CurrentMemento = this.aMemento[this.IndexMemento + 1];
    Memento = this.aMemento[this.IndexMemento];

    if (Memento == null) return;

    if ((CurrentMemento != null) && (CurrentMemento.id != Memento.id)) {
      this.undo();
      return;
    }

    DOMField = $(Memento.id);

    if (DOMField == null) {
      this.undo();
      return;
    }

    DOMField.restoreFromMemento(Memento);
    DOMField.focus();
  };

  DOMNode.redo = function () {
    var Memento, CurrentMemento, DOMField;

    this.IndexMemento++;

    if (this.IndexMemento >= this.aMemento.length) {
      this.IndexMemento = this.aMemento.length - 1;
      return;
    }

    CurrentMemento = this.aMemento[this.IndexMemento - 1];
    Memento = this.aMemento[this.IndexMemento];

    if (Memento == null) return;

    if ((CurrentMemento != null) && (CurrentMemento.id != Memento.id)) {
      this.redo();
      return;
    }

    DOMField = $(Memento.id);

    if (DOMField == null) {
      this.redo();
      return;
    }

    DOMField.restoreFromMemento(Memento);
    DOMField.focus();
  };

  DOMNode.isLoaded = function () {
    var extNode = Ext.get(this);
    var extBody = extNode.down(CSS_BODY);
    if (extBody == null) return true;
    return (!extBody.hasClass(CLASS_LOADING));
  };

  DOMNode.isChild = function (IdParent) {
    var ControlInfo = this.getControlInfo();
    if (ControlInfo.Ancestors == null) return false;
    return (ControlInfo.Ancestors.indexOf("," + IdParent) != -1);
  };

  DOMNode.showBackTaskCommand = function (IdTask) {
    if (this.viewerToolbar != null) this.viewerToolbar.showBackTaskCommand(IdTask);
  };

  DOMNode.hideBackTaskCommand = function () {
    if (this.viewerToolbar != null) this.viewerToolbar.hideBackTaskCommand();
  };

  DOMNode.showBackLinkCommand = function (IdNode) {
    if (this.viewerToolbar != null) this.viewerToolbar.showBackLinkCommand(IdNode);
  };

  DOMNode.hideBackLinkCommand = function () {
    if (this.viewerToolbar != null) this.viewerToolbar.hideBackLinkCommand();
  };

  // #############################################################################################################

  DOMNode.atFieldFocused = function (DOMField, Widget) {
    var extWidget = null;
    if (Widget != null) extWidget = Ext.get(Widget.getDOM());
    if (extWidget == null) extWidget = this.getFirstWidget(Ext.get(DOMField));
    if (extWidget) {
      if ((this.extCurrentWidget != null) && (this.extCurrentWidget.dom.id != extWidget.dom.id)) this.extCurrentWidget.blur();
      this.extCurrentWidget = extWidget;
      if (this.onFieldFocus) this.onFieldFocus(DOMField);
    }
  };

  DOMNode.atFieldBlur = function (DOMField) {
    if (this.onFieldBlur) this.onFieldBlur(DOMField);
  };

  DOMNode.atFieldAdd = function (DOMFieldsComposite, DOMFieldsCompositeItem) {
    var Widget = Ext.get(DOMFieldsCompositeItem).select(CSS_WIDGET).first().dom;
    if ((Widget != null) && (Widget.getFields)) this.initFields(Widget.getFields());
    else {
      if (DOMFieldsComposite.getFields) this.initFields(DOMFieldsComposite.getFields());
    }
  };

  DOMNode.atFieldRegister = function (DOMFieldsComposite, DOMFieldsCompositeItem) {
    var Widget = Ext.get(DOMFieldsCompositeItem).select(CSS_WIDGET).first().dom;
    if ((Widget != null) && (Widget.getFields)) this.initFields(Widget.getFields());
  };

  DOMNode.atFieldEnter = function (DOMField) {
    this.nextField();
  };

  DOMNode.atFieldEscape = function (DOMField) {
    this.parentField();
  };

  DOMNode.atFieldBeforeChange = function (DOMField) {
    this.addMemento(DOMField.saveToMemento());
    if (!DOMField.isLink()) DOMField.onBeforeChange = null;
    if (this.onFieldBeforeChange) this.onFieldBeforeChange(DOMField);
  };

  DOMNode.atFieldChange = function (DOMField) {
    this.addMemento(DOMField.saveToMemento());
    if (this.onFieldChange) this.onFieldChange(DOMField);
  };

  DOMNode.atFieldLabelClick = function (DOMField, oEvent) {
    if (DOMField) {
      var extWidget = this.getFirstWidget(Ext.get(DOMField));
      if ((this.extCurrentWidget != null) && (this.extCurrentWidget.dom.id != extWidget.dom.id)) this.extCurrentWidget.blur();
      this.extCurrentWidget = extWidget;
      extWidget.focus();
    }
  };

  DOMNode.atFieldLoadDefaultValue = function (DOMField) {
    var Process = new CGProcessLoadDefaultValue();
    Process.NodeType = this.getControlInfo().Code;
    Process.Property = DOMField.getCode();
    Process.DOMField = DOMField;
    Process.execute();
  };

  DOMNode.atFieldAddDefaultValue = function (DOMField) {
    var Process = new CGProcessAddDefaultValue();
    Process.NodeType = this.getControlInfo().Code;
    Process.Property = DOMField.getCode();
    Process.Data = DOMField.getData();
    Process.execute();
  };

  DOMNode.atGetFieldValue = function (DOMFieldSender, code) {
    var DOMTarget = DOMFieldSender.getBrother(code);
    if (!DOMTarget || DOMTarget == null) DOMTarget = this.getField(code);
    return DOMTarget.getValue();
  };

  DOMNode.atGetFieldValueCode = function (DOMFieldSender, code) {
    var DOMTarget = DOMFieldSender.getBrother(code);
    if (!DOMTarget || DOMTarget == null) DOMTarget = this.getField(code);
    return DOMTarget.getValueCode();
  };

};