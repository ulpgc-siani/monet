CGWidgetList = function (extWidget) {
    this.base = CGWidget;
    this.base(extWidget);

    if (!extWidget) return;

    this.extWidget.dom.focus = CGWidgetList.prototype.focus.bind(this);
    this.aElementsWidgets = new Array();
    this.extMoreLink = this.extWidget.down(CSS_WIDGET_ELEMENT_MORE_LINK);
    this.extList = this.extWidget.down(CSS_WIDGET_ELEMENT_LIST);
    this.extTemplate = this.extWidget.down(CSS_WIDGET_ELEMENT_TEMPLATE);

    this.DOMDragDropHolder = $(new Insertion.Bottom(this.extList.dom, "<div style='display:none;border:1px solid #ccc;background:rgb(225,225,225);'></div>").element.descendants().last());
    this.DOMDragDropHolder.DOMElement = null;

    if (this.extMoreLink) Event.observe(this.extMoreLink.dom, "click", this.atMoreLinkClick.bind(this));

    this.registerElements();
};

CGWidgetList.prototype = new CGWidget;

CGWidgetList.prototype.init = function () {
    for (var index in this.aElementsWidgets) {
        if (isFunction(this.aElementsWidgets[index])) continue;
        var Widget = this.aElementsWidgets[index];
        if (!Widget.isInit) Widget.init();
    }
    this.bIsReady = true;
};

CGWidgetList.prototype.destroy = function () {
    for (var index in this.aElementsWidgets) {
        if (isFunction(this.aElementsWidgets[index])) continue;
        var Widget = this.aElementsWidgets[index];
        Widget.destroy();
        WidgetManager.unregister(Widget.getId());
    }
    this.destroyOptions();
    this.destroyBehaviours();
};

CGWidgetList.prototype.registerElementOptions = function (extElement, extWidget) {
    var Type, extOptions, extWidgetOptions, DOMDeleteOption, DOMMoveOption;

    if ((extOptions = extElement.select(CSS_WIDGET_ELEMENT_LIST_OPTIONS).first()) == null) return;
    if ((extWidgetOptions = extElement.select(CSS_EDITOR_DIALOG_ELEMENT_ITEM_WIDGET).first()) == null) return;

    Type = extWidget.dom.getType();

    if (Type == WIDGET_COMPOSITE) this.extWidget.addClass(CLASS_WIDGET_ELEMENT_LIST_ITEM_OPTIONS_COMPOSITE);

    DOMDeleteOption = extWidgetOptions.down(CSS_WIDGET_ELEMENT_LIST_ITEM_DELETE).dom;
    if (Type == WIDGET_COMPOSITE || Type == WIDGET_NODE) {
        Event.observe(DOMDeleteOption, 'click', this.atElementDeleteClick.bindAsEventListener(this, DOMDeleteOption, extWidget.dom));
    }
    else DOMDeleteOption.style.display = "none";

    DOMMoveOption = extOptions.select(CSS_WIDGET_ELEMENT_LIST_OPTIONS_MOVE).first().dom;
    extWidget.dom.DragObject = new dragObject(extElement.dom, DOMMoveOption, new Position(0, -30), new Position(0, this.extList.getBottom() - 30), CGWidgetList.prototype.atElementDragStart.bind(this, DOMMoveOption), CGWidgetList.prototype.atElementDragMove.bind(this), CGWidgetList.prototype.atElementDragEnd.bind(this, DOMMoveOption), false);
};

CGWidgetList.prototype.registerElementWidget = function (extElement, extWidget) {
    var Id = (this.Target) ? this.getId() + "." + this.aElementsWidgets.size() : Ext.id();

    extWidget.dom.Id = Id;
    extWidget.dom.iOrder = this.aElementsWidgets.size();
    this.aElementsWidgets[Id] = WidgetFactory.get(extWidget.dom.getType(), extWidget);
    this.aElementsWidgets[Id].setWidgetRequired(this.WidgetRequired);
    if (this.Target) {
        this.aElementsWidgets[Id].setId(Id);
        this.aElementsWidgets[Id].setEditor(EditorsFactory.get(this.Target.getType())); // Important. This line before setfield
        this.aElementsWidgets[Id].setTarget(this.Target);
        this.aElementsWidgets[Id].showClearValue();
        this.aElementsWidgets[Id].lockClearValue();
    }
    this.aElementsWidgets[Id].onKeyPress = this.atWidgetKeyPress.bind(this, Id);
    this.aElementsWidgets[Id].onFocused = this.atWidgetFocused.bind(this, Id);
    this.aElementsWidgets[Id].onChange = this.updateData.bind(this, Id);
    this.aElementsWidgets[Id].onEnter = this.atEnter.bind(this, Id);
    this.aElementsWidgets[Id].onEscape = this.atEscape.bind(this, Id);
    this.aElementsWidgets[Id].onLoadDefaultValue = this.atFieldLoadDefaultValue.bind(this, Id);
    this.aElementsWidgets[Id].onAddDefaultValue = this.atFieldAddDefaultValue.bind(this, Id);
    this.aElementsWidgets[Id].clearValue = this.clearValue.bind(this, Id);

    return this.aElementsWidgets[Id];
};

CGWidgetList.prototype.registerElement = function (extElement) {
    var extWidget;

    if ((extWidget = extElement.select(CSS_WIDGET).first()) == null) return;

    this.registerElementOptions(extElement, extWidget);
    this.registerElementWidget(extElement, extWidget);

    return extElement;
};

CGWidgetList.prototype.registerElements = function () {
    var aElements = this.getElements();
    for (var iPos = 0; iPos < aElements.length; iPos++) {
        var extElement = Ext.get(aElements[iPos]);
        var Constructor = Extension.getEditNodeConstructor();
        Constructor.init(extElement.dom);
        this.registerElement(extElement);
    }
};

CGWidgetList.prototype.getElements = function () {
    var aResult = new Array();
    var aExtListElements = this.extList.select("> " + CSS_WIDGET_ELEMENT_LIST_ITEM);
    aExtListElements.each(function (extElement) {
        var extParent = extElement.up(CSS_WIDGET_ELEMENT_LIST);
        if (extParent != this.extList) return;
        if ((extElement.dom.id == null) || (extElement.dom.id == "")) extElement.dom.id = Ext.id();
        aResult.push(extElement.dom.id);
    }, this);
    return aResult;
};

CGWidgetList.prototype.getLastElement = function () {
    var aElements = this.getElements();
    return aElements[aElements.length - 1];
};

CGWidgetList.prototype.getElementWidget = function (DOMElement) {
    var extElement = Ext.get(DOMElement);
    var extWidget = extElement.select(CSS_WIDGET).first();
    if (extWidget == null) return null;
    return this.aElementsWidgets[extWidget.dom.Id];
};

CGWidgetList.prototype.getData = function () {
    var iOrder = 1;
    var aElements = this.getElements();
    var aResult = new Array();

    aResult.code = this.Target.getCode();
    aResult.order = -1;

    for (var iPos = 0; iPos < aElements.length; iPos++) {
        var extElement = Ext.get(aElements[iPos]);
        var Widget = this.getElementWidget(extElement.dom);
        var FieldData = Widget != null ? Widget.getData() : this.getItemData(iPos);
        if (FieldData == null) return null;
        FieldData.order = iOrder;
        aResult.push(FieldData);
        iOrder++;
    }

    aResult.toXml = function () {
        var Attribute = new CGAttribute();
        var AttributeList = new CGAttributeList();
        var sData = EMPTY;

        Attribute.code = this.code;
        Attribute.iOrder = this.order;

        for (var iPos = 0; iPos < this.length; iPos++) {
            if (Attribute.code == "") Attribute.code = this[iPos].code;
            sData += this[iPos].toXml();
        }

        sData = AttributeList.serializeWithData(sData);

        return Attribute.serializeWithData(sData);
    };

    return aResult;
};

CGWidgetList.prototype.setData = function (sData) {
    if (!this.Target) return;

    this.clear();

    var Attribute = new CGAttribute();
    Attribute.unserialize(sData);

    var aAttributes = Attribute.getAttributes();
    for (var iPos = 0; iPos < aAttributes.length; iPos++) {
        var CurrentAttribute = aAttributes[iPos];
        var extElement = this.addElement();
        var IdWidget = extElement.select(CSS_WIDGET).first().dom.Id;
        this.aElementsWidgets[IdWidget].onChange = null;
        this.aElementsWidgets[IdWidget].setData(CurrentAttribute.serialize());
        this.aElementsWidgets[IdWidget].onChange = this.updateData.bind(this, IdWidget);
    }

    this.validate();

    if (this.onChange) this.onChange();
};

CGWidgetList.prototype.getItemData = function(pos) {
    var data = this.Target.getData();
    var Attribute = new CGAttribute();
    Attribute.unserialize(data);
    var result = Attribute.getAttributes()[pos];
    result.toXml = function(){
        return result;
    }
};

CGWidgetList.prototype.setTarget = function (Target) {
    this.Target = Target;
    this.createRequiredWidget();

    var editor = EditorsFactory.get(this.Target.getType());
    editor.onSelectMultiple = this.atSelectMultiple.bind(this);

    for (var Id in this.aElementsWidgets) {
        if (isFunction(this.aElementsWidgets[Id])) continue;
        this.aElementsWidgets[Id].onChange = null;
        this.aElementsWidgets[Id].setWidgetRequired(this.WidgetRequired);
        this.aElementsWidgets[Id].setEditor(editor); // Important. This line before setfield
        this.aElementsWidgets[Id].setTarget(this.Target);
        this.aElementsWidgets[Id].onChange = this.updateData.bind(this, Id);
        this.aElementsWidgets[Id].showClearValue();
        this.aElementsWidgets[Id].lockClearValue();
    }

    //this.updateData();
};

CGWidgetList.prototype.setReadonly = function (bValue) {
    for (var Id in this.aElementsWidgets) {
        if (isFunction(this.aElementsWidgets[Id])) continue;
        this.aElementsWidgets[Id].setReadonly(bValue);
    }
};

CGWidgetList.prototype.validate = function () {
    if (!this.WidgetRequired) return;
    if (this.countElements() == 0) this.WidgetRequired.show();
    else this.WidgetRequired.hide();
};

CGWidgetList.prototype.getDOMElementFromId = function (Id) {
    var Widget = this.aElementsWidgets[Id];
    if (!Widget) return null;
    return Ext.get(Widget.getDOM()).up(CSS_WIDGET_ELEMENT_LIST_ITEM).dom;
};

CGWidgetList.prototype.addElement = function () {
    var extElement, extWidget;
    var sContent;
    var Id = Ext.id();

    new Insertion.Bottom(this.extList.dom, WidgetListElementTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'id': Id, 'widget': cleanContentIds(this.extTemplate.dom.innerHTML)}));
    extElement = Ext.get(this.getLastElement());

    var Constructor = Extension.getEditNodeConstructor();
    Constructor.init(extElement.dom);

    if (this.onAdd) this.onAdd(extElement.dom);

    this.registerElement(extElement);
    this.validate();
    this.updateData();
    this.refreshDialog();

    var IdWidget = extElement.select(CSS_WIDGET).first().dom.Id;
    if (this.aElementsWidgets[IdWidget]) {
        this.aElementsWidgets[IdWidget].isInit = true;
        this.aElementsWidgets[IdWidget].init();
        this.aElementsWidgets[IdWidget].focus();
    }

    return extElement;
};

CGWidgetList.prototype.clear = function () {
    var aElements = this.getElements();
    for (var iPos = 0; iPos < aElements.length; iPos++) {
        var DOMElement = $(aElements[iPos]);
        this.removeElement(DOMElement);
    }
};

CGWidgetList.prototype.removeElement = function (DOMElement) {
    var extWidget = Ext.get(DOMElement).select(CSS_WIDGET).first();
    var DOMElement = $(DOMElement);
    if (this.aElementsWidgets[extWidget.dom.Id].onRemove) this.aElementsWidgets[extWidget.dom.Id].destroy();
    delete this.aElementsWidgets[extWidget.dom.Id];
    DOMElement.remove();
    this.validate();
    this.updateOrders();
    this.updateData();
    this.refreshDialog();
    if (this.onDelete) this.onDelete();
};

CGWidgetList.prototype.buryElementToLast = function (DOMElement) {
    var extLast = Ext.get(this.getLastElement());
    if (DOMElement != extLast.dom) {
        this.extList.dom.removeChild(DOMElement);
        this.extList.dom.appendChild(DOMElement);
        this.updateOrders();
        this.updateData();
        this.refreshDialog();
    }
};

CGWidgetList.prototype.updateOrders = function () {
    var extWidgetList = this.extList.select(CSS_WIDGET);
    var iOrder = 1;
    extWidgetList.each(function (extWidget) {
        extWidget.dom.iOrder = iOrder;
        iOrder++;
    }, this);
};

CGWidgetList.prototype.reorderElements = function (aOrder) {

    for (var iPos = 0; iPos < aOrder.length; iPos++) {
        var DOMElement = this.getDOMElementFromId(aOrder[iPos]);
        this.buryElementToLast(DOMElement);
    }

    this.updateData();
    this.refreshDialog();
};

CGWidgetList.prototype.countElements = function () {
    return this.getElements().length;
};

CGWidgetList.prototype.getWidgetsInfo = function () {
    var aItems = new Array();

    var extWidgetList = this.extList.select(CSS_WIDGET);
    extWidgetList.each(function (extWidget) {
        var Widget = this.aElementsWidgets[extWidget.dom.Id];
        if (!Widget) return;
        aItems.push({id: extWidget.dom.Id, title: Widget.getValue()});
    }, this);

    return aItems;
};

CGWidgetList.prototype.focus = function () {
    var Dialog;

    if (!this.Target) return;
    if (!this.isReady()) this.init();

    this.Editor.show();
    this.Editor.setConfiguration({Field: this.Target, Dialogs: [
        {sName: LIST, Items: this.getWidgetsInfo()}
    ]});
    this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
    this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
    this.Editor.onClearValue = this.atClearValue.bind(this);
    this.Editor.refresh();

    if (this.isLocked()) this.Editor.lock();
    else this.Editor.unLock();

    if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());

    Dialog = this.Editor.getDialog(LIST);
    Dialog.onAdd = CGWidgetList.prototype.atEditorAdd.bind(this);
    Dialog.onReorder = CGWidgetList.prototype.atEditorReorder.bind(this);
    Dialog.onDelete = CGWidgetList.prototype.atEditorDelete.bind(this);

    this.bFocused = true;
    this.extWidget.addClass(CLASS_FOCUS);
    if (this.onFocused) this.onFocused();
};

CGWidgetList.prototype.refreshDialog = function () {
    var Dialog = this.Editor.getDialog(LIST);
    Dialog.setData(this.getWidgetsInfo());
    Dialog.refresh();
};

CGWidgetList.prototype.setObserver = function (Observer, iPos) {
    var extValue;

    if (Observer == null) {
        for (var id in this.aElementsWidgets) {
            if (isFunction(this.aElementsWidgets[id])) continue;
            this.aElementsWidgets[id].setObserver(Observer, iPos);
        }
        return;
    }

    extValue = $(Observer.field);
    if (extValue) {
        var DOMWidget = extValue.up(CSS_WIDGET);
        if (DOMWidget && this.aElementsWidgets[DOMWidget.Id]) {
            this.aElementsWidgets[DOMWidget.Id].setObserver(Observer, iPos);
        }
    }
};

CGWidgetList.prototype.clearValue = function (Id) {
    if (this.isLocked()) return;
    if (!this.aElementsWidgets[Id]) return false;
    this.removeElement(this.getDOMElementFromId(Id));
};

// #############################################################################################################
CGWidgetList.prototype.atSelectMultiple = function (dataList) {
    var onChange = this.onChange;

    try {
        this.onChange = null;

        if (this.countElements() > 0) {
            var extElement = Ext.get(this.getLastElement());
            var widget = this.getElementWidget(extElement);
            if (widget.getValue() == "")
                this.removeElement(extElement.dom);
        }

        for (var i = 0; i < dataList.length; i++) {
            var extElement = this.addElement();
            var IdWidget = extElement.select(CSS_WIDGET).first().dom.Id;
            this.notifySelect(dataList[i], IdWidget);
        }
    }
    finally {
        this.onChange = onChange;
    }

    this.updateData();
};

CGWidgetList.prototype.notifySelect = function (data, IdWidget) {
    this.aElementsWidgets[IdWidget].onChange = null;
    this.aElementsWidgets[IdWidget].atSelect(data);
    this.aElementsWidgets[IdWidget].onChange = this.updateData.bind(this, IdWidget);
};

CGWidgetList.prototype.atWidgetKeyPress = function (IdWidget, sValue, codeKey) {
    if (this.onKeyPress) this.onKeyPress(sValue, codeKey);
};

CGWidgetList.prototype.atWidgetFocused = function (IdWidget) {
    var Widget = this.aElementsWidgets[IdWidget];

    var editor = EditorsFactory.get(this.Target.getType());
    editor.onSelectMultiple = this.atSelectMultiple.bind(this);

    if (Widget == null) return;
    if (!this.isReady()) this.init();

    if (this.onFocused) this.onFocused(Widget);
};

CGWidgetList.prototype.atEnter = function () {
    if (this.onEnter) this.onEnter();
};

CGWidgetList.prototype.atEscape = function () {
    if (this.onEscape) this.onEscape();
};

CGWidgetList.prototype.atMoreLinkClick = function (oEvent) {
    if (!this.isLocked()) this.addElement();
    if (oEvent) Event.stop(oEvent);
    return false;
};

CGWidgetList.prototype.atElementDeleteClick = function (oEvent, DOMTarget, DOMWidget) {
    if (this.isLocked()) return;
    var extTarget = Ext.get(DOMTarget);
    this.focus();
    DOMElement = extTarget.up(CSS_WIDGET_ELEMENT_LIST_ITEM).dom;
    this.removeElement(DOMElement);
};

CGWidgetList.prototype.atElementDragStart = function (DOMMoveOption, oEvent, DOMElement) {
    if (this.isLocked()) return;
    var iHeight = Ext.get(DOMElement).getHeight();
    this.extList.addClass("draglist");
    Ext.get(DOMMoveOption).addClass("grabbing");
    DOMElement.style.top = DOMElement.offsetTop + 'px';
    DOMElement.style.left = DOMElement.offsetLeft + 'px';
    DOMElement.className = "drag";
    this.DOMDragDropHolder.style.display = "block";
    this.DOMDragDropHolder.style.height = iHeight + "px";
    this.extList.dom.insertBefore(this.DOMDragDropHolder, DOMElement);
    this.DOMDragDropHolder.DOMElement = DOMElement;
};

CGWidgetList.prototype.atElementDragMove = function (oPosition, DOMElement, oEvent) {
    if (this.isLocked()) return;
    var yPos = oPosition.Y + (oEvent.layerY ? oEvent.layerY : oEvent.offsetY);
    var temp;
    var bestItem = "end";

    for (var i = 0; i < this.extList.dom.childNodes.length; i++) {
        if (this.extList.dom.childNodes[i].className == "element") {
            temp = parseInt(Ext.get(this.extList.dom.childNodes[i]).getHeight());
            if (temp / 2 >= yPos) {
                bestItem = this.extList.dom.childNodes[i];
                break;
            }
            yPos -= temp;
        }
    }

    if (bestItem == this.DOMDragDropHolder || bestItem == this.DOMDragDropHolder.DOMElement) return;

    this.DOMDragDropHolder.DOMElement = bestItem;
    if (bestItem != "end") this.extList.dom.insertBefore(this.DOMDragDropHolder, this.extList.dom.childNodes[i]);
    else this.extList.dom.appendChild(this.DOMDragDropHolder);
};

CGWidgetList.prototype.atElementDragEnd = function (DOMMoveOption, DOMElement) {
    if (this.isLocked()) return;

    Ext.get(DOMMoveOption).removeClass("grabbing");

    this.DOMDragDropHolder.style.display = "none";

    if (this.DOMDragDropHolder.DOMElement != null) {
        this.DOMDragDropHolder.DOMElement = null;
        this.extList.dom.replaceChild(DOMElement, this.DOMDragDropHolder);
    }

    DOMElement.className = 'element';
    DOMElement.style.top = '0px';
    DOMElement.style.left = '0px';
    this.extList.removeClass("draglist");

    this.updateOrders();
    this.updateData();
    this.refreshDialog();
};

CGWidgetList.prototype.atEditorAdd = function () {
    if (this.isLocked()) return;
    this.addElement();
};

CGWidgetList.prototype.atEditorReorder = function (aOrder) {
    if (this.isLocked()) return;
    this.reorderElements(aOrder);
};

CGWidgetList.prototype.atEditorDelete = function (aElements) {
    if (this.isLocked()) return;
    for (var iPos = 0; iPos < aElements.length; iPos++) {
        var DOMElement = this.getDOMElementFromId(aElements[iPos]);
        if (DOMElement) this.removeElement(DOMElement);
    }
};

CGWidgetList.prototype.atFieldLoadDefaultValue = function (Id) {
};

CGWidgetList.prototype.atFieldAddDefaultValue = function (Id) {
};

CGWidgetList.prototype.atClearValue = function (Id) {
    this.clearValue(Id);
    return false;
};