/*! jQRangeSlider 5.0.0 - 2013-02-09 - Copyright (C) Guillaume Gautreau 2012 - MIT and GPLv3 licenses.*/
(function (t) {
  "use strict";
  t.widget("ui.rangeSliderMouseTouch", t.ui.mouse, {_mouseInit: function () {
    var e = this;
    t.ui.mouse.prototype._mouseInit.apply(this), this._mouseDownEvent = !1, this.element.bind("touchstart." + this.widgetName, function (t) {
      return e._touchStart(t)
    })
  }, _mouseDestroy: function () {
    t(document).unbind("touchmove." + this.widgetName, this._touchMoveDelegate).unbind("touchend." + this.widgetName, this._touchEndDelegate), t.ui.mouse.prototype._mouseDestroy.apply(this)
  }, _touchStart: function (e) {
    e.which = 1, e.preventDefault(), this._fillTouchEvent(e);
    var i = this, s = this._mouseDownEvent;
    this._mouseDown(e), s !== this._mouseDownEvent && (this._touchEndDelegate = function (t) {
      i._touchEnd(t)
    }, this._touchMoveDelegate = function (t) {
      i._touchMove(t)
    }, t(document).bind("touchmove." + this.widgetName, this._touchMoveDelegate).bind("touchend." + this.widgetName, this._touchEndDelegate))
  }, _touchEnd: function (e) {
    this._fillTouchEvent(e), this._mouseUp(e), t(document).unbind("touchmove." + this.widgetName, this._touchMoveDelegate).unbind("touchend." + this.widgetName, this._touchEndDelegate), this._mouseDownEvent = !1, t(document).trigger("mouseup")
  }, _touchMove: function (t) {
    return t.preventDefault(), this._fillTouchEvent(t), this._mouseMove(t)
  }, _fillTouchEvent: function (t) {
    var e;
    e = t.targetTouches === undefined && t.changedTouches === undefined ? t.originalEvent.targetTouches[0] || t.originalEvent.changedTouches[0] : t.targetTouches[0] || t.changedTouches[0], t.pageX = e.pageX, t.pageY = e.pageY
  }})
})(jQuery), function (t) {
  "use strict";
  t.widget("ui.rangeSliderDraggable", t.ui.rangeSliderMouseTouch, {cache: null, options: {containment: null}, _create: function () {
    setTimeout(t.proxy(this._initElement, this), 10)
  }, _initElement: function () {
    this._mouseInit(), this._cache()
  }, _setOption: function (e, i) {
    "containment" === e && (this.options.containment = null === i || 0 === t(i).length ? null : t(i))
  }, _mouseStart: function (t) {
    return this._cache(), this.cache.click = {left: t.pageX, top: t.pageY}, this.cache.initialOffset = this.element.offset(), this._triggerMouseEvent("mousestart"), !0
  }, _mouseDrag: function (t) {
    var e = t.pageX - this.cache.click.left;
    return e = this._constraintPosition(e + this.cache.initialOffset.left), this._applyPosition(e), this._triggerMouseEvent("sliderDrag"), !1
  }, _mouseStop: function () {
    this._triggerMouseEvent("stop")
  }, _constraintPosition: function (t) {
    return 0 !== this.element.parent().length && null !== this.cache.parent.offset && (t = Math.min(t, this.cache.parent.offset.left + this.cache.parent.width - this.cache.width.outer), t = Math.max(t, this.cache.parent.offset.left)), t
  }, _applyPosition: function (t) {
    var e = {top: this.cache.offset.top, left: t};
    this.element.offset({left: t}), this.cache.offset = e
  }, _cacheIfNecessary: function () {
    null === this.cache && this._cache()
  }, _cache: function () {
    this.cache = {}, this._cacheMargins(), this._cacheParent(), this._cacheDimensions(), this.cache.offset = this.element.offset()
  }, _cacheMargins: function () {
    this.cache.margin = {left: this._parsePixels(this.element, "marginLeft"), right: this._parsePixels(this.element, "marginRight"), top: this._parsePixels(this.element, "marginTop"), bottom: this._parsePixels(this.element, "marginBottom")}
  }, _cacheParent: function () {
    if (null !== this.options.parent) {
      var t = this.element.parent();
      this.cache.parent = {offset: t.offset(), width: t.width()}
    } else this.cache.parent = null
  }, _cacheDimensions: function () {
    this.cache.width = {outer: this.element.outerWidth(), inner: this.element.width()}
  }, _parsePixels: function (t, e) {
    return parseInt(t.css(e), 10) || 0
  }, _triggerMouseEvent: function (t) {
    var e = this._prepareEventData();
    this.element.trigger(t, e)
  }, _prepareEventData: function () {
    return{element: this.element, offset: this.cache.offset || null}
  }})
}(jQuery), function (t) {
  "use strict";
  t.widget("ui.rangeSlider", {options: {bounds: {min: 0, max: 100}, defaultValues: {min: 20, max: 50}, wheelMode: null, wheelSpeed: 4, arrows: !0, valueLabels: "show", formatter: null, durationIn: 0, durationOut: 400, delayOut: 200, range: {min: !1, max: !1}, step: !1, scales: !1}, _values: null, _valuesChanged: !1, bar: null, leftHandle: null, rightHandle: null, innerBar: null, container: null, arrows: null, labels: null, changing: {min: !1, max: !1}, changed: {min: !1, max: !1}, ruler: null, _create: function () {
    this._values = {min: this.options.defaultValues.min, max: this.options.defaultValues.max}, this.labels = {left: null, right: null, leftDisplayed: !0, rightDisplayed: !0}, this.arrows = {left: null, right: null}, this.changing = {min: !1, max: !1}, this.changed = {min: !1, max: !1}, this._createElements(), this._bindResize(), setTimeout(t.proxy(this.resize, this), 1), setTimeout(t.proxy(this._initValues, this), 1)
  }, _bindResize: function () {
    var e = this;
    this._resizeProxy = function (t) {
      e.resize(t)
    }, t(window).resize(this._resizeProxy)
  }, _initWidth: function () {
    this.container.css("width", this.element.width() - this.container.outerWidth(!0) + this.container.width()), this.innerBar.css("width", this.container.width() - this.innerBar.outerWidth(!0) + this.innerBar.width())
  }, _initValues: function () {
    this.values(this._values.min, this._values.max)
  }, _setOption: function (t, e) {
    this._setWheelOption(t, e), this._setArrowsOption(t, e), this._setLabelsOption(t, e), this._setLabelsDurations(t, e), this._setFormatterOption(t, e), this._setBoundsOption(t, e), this._setRangeOption(t, e), this._setStepOption(t, e), this._setScalesOption(t, e)
  }, _validProperty: function (t, e, i) {
    return null === t || t[e] === undefined ? i : t[e]
  }, _setStepOption: function (t, e) {
    "step" === t && (this.options.step = e, this._leftHandle("option", "step", e), this._rightHandle("option", "step", e), this._changed(!0))
  }, _setScalesOption: function (t, e) {
    "scales" === t && (e === !1 || null === e ? (this.options.scales = !1, this._destroyRuler()) : e instanceof Array && (this.options.scales = e, this._updateRuler()))
  }, _setRangeOption: function (t, e) {
    "range" === t && (this._bar("option", "range", e), this.options.range = this._bar("option", "range"), this._changed(!0))
  }, _setBoundsOption: function (t, e) {
    "bounds" === t && e.min !== undefined && e.max !== undefined && this.bounds(e.min, e.max)
  }, _setWheelOption: function (t, e) {
    ("wheelMode" === t || "wheelSpeed" === t) && (this._bar("option", t, e), this.options[t] = this._bar("option", t))
  }, _setLabelsOption: function (t, e) {
    if ("valueLabels" === t) {
      if ("hide" !== e && "show" !== e && "change" !== e)return;
      this.options.valueLabels = e, "hide" !== e ? (this._createLabels(), this._leftLabel("update"), this._rightLabel("update")) : this._destroyLabels()
    }
  }, _setFormatterOption: function (t, e) {
    "formatter" === t && null !== e && "function" == typeof e && (this.options.formatter = e, "hide" !== this.options.valueLabels && (this._destroyLabels(), this._createLabels()))
  }, _setArrowsOption: function (t, e) {
    "arrows" !== t || e !== !0 && e !== !1 || e === this.options.arrows || (e === !0 ? (this.element.removeClass("ui-rangeSlider-noArrow").addClass("ui-rangeSlider-withArrows"), this.arrows.left.css("display", "block"), this.arrows.right.css("display", "block"), this.options.arrows = !0) : e === !1 && (this.element.addClass("ui-rangeSlider-noArrow").removeClass("ui-rangeSlider-withArrows"), this.arrows.left.css("display", "none"), this.arrows.right.css("display", "none"), this.options.arrows = !1), this._initWidth())
  }, _setLabelsDurations: function (t, e) {
    if ("durationIn" === t || "durationOut" === t || "delayOut" === t) {
      if (parseInt(e, 10) !== e)return;
      null !== this.labels.left && this._leftLabel("option", t, e), null !== this.labels.right && this._rightLabel("option", t, e), this.options[t] = e
    }
  }, _createElements: function () {
    "absolute" !== this.element.css("position") && this.element.css("position", "relative"), this.element.addClass("ui-rangeSlider"), this.container = t("<div class='ui-rangeSlider-container' />").css("position", "absolute").appendTo(this.element), this.innerBar = t("<div class='ui-rangeSlider-innerBar' />").css("position", "absolute").css("top", 0).css("left", 0), this._createHandles(), this._createBar(), this.container.prepend(this.innerBar), this._createArrows(), "hide" !== this.options.valueLabels ? this._createLabels() : this._destroyLabels(), this._updateRuler()
  }, _createHandle: function (e) {
    return t("<div />")[this._handleType()](e).bind("sliderDrag", t.proxy(this._changing, this)).bind("stop", t.proxy(this._changed, this))
  }, _createHandles: function () {
    this.leftHandle = this._createHandle({isLeft: !0, bounds: this.options.bounds, value: this._values.min, step: this.options.step}).appendTo(this.container), this.rightHandle = this._createHandle({isLeft: !1, bounds: this.options.bounds, value: this._values.max, step: this.options.step}).appendTo(this.container)
  }, _createBar: function () {
    this.bar = t("<div />").prependTo(this.container).bind("sliderDrag scroll zoom", t.proxy(this._changing, this)).bind("stop", t.proxy(this._changed, this)), this._bar({leftHandle: this.leftHandle, rightHandle: this.rightHandle, values: {min: this._values.min, max: this._values.max}, type: this._handleType(), range: this.options.range, wheelMode: this.options.wheelMode, wheelSpeed: this.options.wheelSpeed}), this.options.range = this._bar("option", "range"), this.options.wheelMode = this._bar("option", "wheelMode"), this.options.wheelSpeed = this._bar("option", "wheelSpeed")
  }, _createArrows: function () {
    this.arrows.left = this._createArrow("left"), this.arrows.right = this._createArrow("right"), this.options.arrows ? this.element.addClass("ui-rangeSlider-withArrows") : (this.arrows.left.css("display", "none"), this.arrows.right.css("display", "none"), this.element.addClass("ui-rangeSlider-noArrow"))
  }, _createArrow: function (e) {
    var i, s = t("<div class='ui-rangeSlider-arrow' />").append("<div class='ui-rangeSlider-arrow-inner' />").addClass("ui-rangeSlider-" + e + "Arrow").css("position", "absolute").css(e, 0).appendTo(this.element);
    return i = "right" === e ? t.proxy(this._scrollRightClick, this) : t.proxy(this._scrollLeftClick, this), s.bind("mousedown touchstart", i), s
  }, _proxy: function (t, e, i) {
    var s = Array.prototype.slice.call(i);
    return t[e].apply(t, s)
  }, _handleType: function () {
    return"rangeSliderHandle"
  }, _barType: function () {
    return"rangeSliderBar"
  }, _bar: function () {
    return this._proxy(this.bar, this._barType(), arguments)
  }, _labelType: function () {
    return"rangeSliderLabel"
  }, _leftLabel: function () {
    return this._proxy(this.labels.left, this._labelType(), arguments)
  }, _rightLabel: function () {
    return this._proxy(this.labels.right, this._labelType(), arguments)
  }, _leftHandle: function () {
    return this._proxy(this.leftHandle, this._handleType(), arguments)
  }, _rightHandle: function () {
    return this._proxy(this.rightHandle, this._handleType(), arguments)
  }, _getValue: function (t, e) {
    return e === this.rightHandle && (t -= e.outerWidth()), t * (this.options.bounds.max - this.options.bounds.min) / (this.container.innerWidth() - e.outerWidth(!0)) + this.options.bounds.min
  }, _trigger: function (t) {
    var e = this;
    setTimeout(function () {
      e.element.trigger(t, {label: e.element, values: e.values()})
    }, 1)
  }, _changing: function () {
    this._updateValues() && (this._trigger("valuesChanging"), this._valuesChanged = !0)
  }, _changed: function (t) {
    (this._updateValues() || this._valuesChanged) && (this._trigger("valuesChanged"), t !== !0 && this._trigger("userValuesChanged"), this._valuesChanged = !1)
  }, _updateValues: function () {
    var t = this._leftHandle("value"), e = this._rightHandle("value"), i = this._min(t, e), s = this._max(t, e), n = i !== this._values.min || s !== this._values.max;
    return this._values.min = this._min(t, e), this._values.max = this._max(t, e), n
  }, _min: function (t, e) {
    return Math.min(t, e)
  }, _max: function (t, e) {
    return Math.max(t, e)
  }, _createLabel: function (e, i) {
    var s;
    return null === e ? (s = this._getLabelConstructorParameters(e, i), e = t("<div />").appendTo(this.element)[this._labelType()](s)) : (s = this._getLabelRefreshParameters(e, i), e[this._labelType()](s)), e
  }, _getLabelConstructorParameters: function (t, e) {
    return{handle: e, handleType: this._handleType(), formatter: this._getFormatter(), show: this.options.valueLabels, durationIn: this.options.durationIn, durationOut: this.options.durationOut, delayOut: this.options.delayOut}
  }, _getLabelRefreshParameters: function () {
    return{formatter: this._getFormatter(), show: this.options.valueLabels, durationIn: this.options.durationIn, durationOut: this.options.durationOut, delayOut: this.options.delayOut}
  }, _getFormatter: function () {
    return this.options.formatter === !1 || null === this.options.formatter ? this._defaultFormatter : this.options.formatter
  }, _defaultFormatter: function (t) {
    return Math.round(t)
  }, _destroyLabel: function (t) {
    return null !== t && (t.remove(), t = null), t
  }, _createLabels: function () {
    this.labels.left = this._createLabel(this.labels.left, this.leftHandle), this.labels.right = this._createLabel(this.labels.right, this.rightHandle), this._leftLabel("pair", this.labels.right)
  }, _destroyLabels: function () {
    this.labels.left = this._destroyLabel(this.labels.left), this.labels.right = this._destroyLabel(this.labels.right)
  }, _stepRatio: function () {
    return this._leftHandle("stepRatio")
  }, _scrollRightClick: function (t) {
    t.preventDefault(), this._bar("startScroll"), this._bindStopScroll(), this._continueScrolling("scrollRight", 4 * this._stepRatio(), 1)
  }, _continueScrolling: function (t, e, i, s) {
    this._bar(t, i), s = s || 5, s--;
    var n = this, o = 16, a = Math.max(1, 4 / this._stepRatio());
    this._scrollTimeout = setTimeout(function () {
      0 === s && (e > o ? e = Math.max(o, e / 1.5) : i = Math.min(a, 2 * i), s = 5), n._continueScrolling(t, e, i, s)
    }, e)
  }, _scrollLeftClick: function (t) {
    t.preventDefault(), this._bar("startScroll"), this._bindStopScroll(), this._continueScrolling("scrollLeft", 4 * this._stepRatio(), 1)
  }, _bindStopScroll: function () {
    var e = this;
    this._stopScrollHandle = function (t) {
      t.preventDefault(), e._stopScroll()
    }, t(document).bind("mouseup touchend", this._stopScrollHandle)
  }, _stopScroll: function () {
    t(document).unbind("mouseup touchend", this._stopScrollHandle), this._bar("stopScroll"), clearTimeout(this._scrollTimeout)
  }, _createRuler: function () {
    this.ruler = t("<div class='ui-rangeSlider-ruler' />").appendTo(this.innerBar)
  }, _destroyRuler: function () {
    null !== this.ruler && t.fn.ruler && (this.ruler.ruler("destroy"), this.ruler.remove(), this.ruler = null)
  }, _updateRuler: function () {
    this._destroyRuler(), this.options.scales !== !1 && t.fn.ruler && (null === this.ruler && this._createRuler(), this.ruler.ruler({min: this.options.bounds.min, max: this.options.bounds.max, scales: this.options.scales}))
  }, values: function (t, e) {
    var i = this._bar("values", t, e);
    return t !== undefined && e !== undefined && this._changed(!0), i
  }, min: function (t) {
    return this._values.min = this.values(t, this._values.max).min, this._values.min
  }, max: function (t) {
    return this._values.max = this.values(this._values.min, t).max, this._values.max
  }, bounds: function (t, e) {
    return this._isValidValue(t) && this._isValidValue(e) && e > t && (this._setBounds(t, e), this._updateRuler(), this._changed(!0)), this.options.bounds
  }, _isValidValue: function (t) {
    return t !== undefined && parseFloat(t) === t
  }, _setBounds: function (t, e) {
    this.options.bounds = {min: t, max: e}, this._leftHandle("option", "bounds", this.options.bounds), this._rightHandle("option", "bounds", this.options.bounds), this._bar("option", "bounds", this.options.bounds)
  }, zoomIn: function (t) {
    this._bar("zoomIn", t)
  }, zoomOut: function (t) {
    this._bar("zoomOut", t)
  }, scrollLeft: function (t) {
    this._bar("startScroll"), this._bar("scrollLeft", t), this._bar("stopScroll")
  }, scrollRight: function (t) {
    this._bar("startScroll"), this._bar("scrollRight", t), this._bar("stopScroll")
  }, resize: function () {
    this._initWidth(), this._leftHandle("update"), this._rightHandle("update"), this._bar("update")
  }, destroy: function () {
    this.element.removeClass("ui-rangeSlider-withArrows").removeClass("ui-rangeSlider-noArrow"), this.bar.detach(), this.leftHandle.detach(), this.rightHandle.detach(), this.innerBar.detach(), this.container.detach(), this.arrows.left.detach(), this.arrows.right.detach(), this.element.removeClass("ui-rangeSlider"), this._destroyLabels(), delete this.options, t(window).unbind("resize", this._resizeProxy), t.Widget.prototype.destroy.apply(this, arguments)
  }})
}(jQuery), function (t) {
  "use strict";
  t.widget("ui.rangeSliderHandle", t.ui.rangeSliderDraggable, {currentMove: null, margin: 0, parentElement: null, options: {isLeft: !0, bounds: {min: 0, max: 100}, range: !1, value: 0, step: !1}, _value: 0, _left: 0, _create: function () {
    t.ui.rangeSliderDraggable.prototype._create.apply(this), this.element.css("position", "absolute").css("top", 0).addClass("ui-rangeSlider-handle").toggleClass("ui-rangeSlider-leftHandle", this.options.isLeft).toggleClass("ui-rangeSlider-rightHandle", !this.options.isLeft), this.element.append("<div class='ui-rangeSlider-handle-inner' />"), this._value = this.options.value
  }, _setOption: function (e, i) {
    "isLeft" !== e || i !== !0 && i !== !1 || i === this.options.isLeft ? "step" === e && this._checkStep(i) ? (this.options.step = i, this.update()) : "bounds" === e ? (this.options.bounds = i, this.update()) : "range" === e && this._checkRange(i) && (this.options.range = i, this.update()) : (this.options.isLeft = i, this.element.toggleClass("ui-rangeSlider-leftHandle", this.options.isLeft).toggleClass("ui-rangeSlider-rightHandle", !this.options.isLeft), this._position(this._value), this.element.trigger("switch", this.options.isLeft)), t.ui.rangeSliderDraggable.prototype._setOption.apply(this, [e, i])
  }, _checkRange: function (t) {
    return t === !1 || !this._isValidValue(t.min) && !this._isValidValue(t.max)
  }, _isValidValue: function (t) {
    return t !== undefined && t !== !1 && parseFloat(t) !== t
  }, _checkStep: function (t) {
    return t === !1 || parseFloat(t) === t
  }, _initElement: function () {
    t.ui.rangeSliderDraggable.prototype._initElement.apply(this), 0 === this.cache.parent.width || null === this.cache.parent.width ? setTimeout(t.proxy(this._initElement, this), 500) : (this._position(this._value), this._triggerMouseEvent("initialize"))
  }, _bounds: function () {
    return this.options.bounds
  }, _cache: function () {
    t.ui.rangeSliderDraggable.prototype._cache.apply(this), this._cacheParent()
  }, _cacheParent: function () {
    var t = this.element.parent();
    this.cache.parent = {element: t, offset: t.offset(), padding: {left: this._parsePixels(t, "paddingLeft")}, width: t.width()}
  }, _position: function (t) {
    var e = this._getPositionForValue(t);
    this._applyPosition(e)
  }, _constraintPosition: function (t) {
    var e = this._getValueForPosition(t);
    return this._getPositionForValue(e)
  }, _applyPosition: function (e) {
    t.ui.rangeSliderDraggable.prototype._applyPosition.apply(this, [e]), this._left = e, this._setValue(this._getValueForPosition(e)), this._triggerMouseEvent("moving")
  }, _prepareEventData: function () {
    var e = t.ui.rangeSliderDraggable.prototype._prepareEventData.apply(this);
    return e.value = this._value, e
  }, _setValue: function (t) {
    t !== this._value && (this._value = t)
  }, _constraintValue: function (t) {
    if (t = Math.min(t, this._bounds().max), t = Math.max(t, this._bounds().min), t = this._round(t), this.options.range !== !1) {
      var e = this.options.range.min || !1, i = this.options.range.max || !1;
      e !== !1 && (t = Math.max(t, this._round(e))), i !== !1 && (t = Math.min(t, this._round(i)))
    }
    return t
  }, _round: function (t) {
    return this.options.step !== !1 && this.options.step > 0 ? Math.round(t / this.options.step) * this.options.step : t
  }, _getPositionForValue: function (t) {
    if (null === this.cache.parent.offset)return 0;
    t = this._constraintValue(t);
    var e = (t - this.options.bounds.min) / (this.options.bounds.max - this.options.bounds.min), i = this.cache.parent.width - this.cache.width.outer, s = this.cache.parent.offset.left;
    return e * i + s
  }, _getValueForPosition: function (t) {
    var e = this._getRawValueForPositionAndBounds(t, this.options.bounds.min, this.options.bounds.max);
    return this._constraintValue(e)
  }, _getRawValueForPositionAndBounds: function (t, e, i) {
    var s = null === this.cache.parent.offset ? 0 : this.cache.parent.offset.left, n = this.cache.parent.width - this.cache.width.outer, o = (t - s) / n;
    return o * (i - e) + e
  }, value: function (t) {
    return t !== undefined && (this._cache(), t = this._constraintValue(t), this._position(t)), this._value
  }, update: function () {
    this._cache();
    var t = this._constraintValue(this._value), e = this._getPositionForValue(t);
    t !== this._value ? (this._triggerMouseEvent("updating"), this._position(t), this._triggerMouseEvent("update")) : e !== this.cache.offset.left && (this._triggerMouseEvent("updating"), this._position(t), this._triggerMouseEvent("update"))
  }, position: function (t) {
    return t !== undefined && (this._cache(), t = this._constraintPosition(t), this._applyPosition(t)), this._left
  }, add: function (t, e) {
    return t + e
  }, substract: function (t, e) {
    return t - e
  }, stepsBetween: function (t, e) {
    return this.options.step === !1 ? e - t : (e - t) / this.options.step
  }, multiplyStep: function (t, e) {
    return t * e
  }, moveRight: function (t) {
    var e;
    return this.options.step === !1 ? (e = this._left, this.position(this._left + t), this._left - e) : (e = this._value, this.value(this.add(e, this.multiplyStep(this.options.step, t))), this.stepsBetween(e, this._value))
  }, moveLeft: function (t) {
    return-this.moveRight(-t)
  }, stepRatio: function () {
    if (this.options.step === !1)return 1;
    var t = (this.options.bounds.max - this.options.bounds.min) / this.options.step;
    return this.cache.parent.width / t
  }})
}(jQuery), function (t) {
  "use strict";
  t.widget("ui.rangeSliderBar", t.ui.rangeSliderDraggable, {options: {leftHandle: null, rightHandle: null, bounds: {min: 0, max: 100}, type: "rangeSliderHandle", range: !1, drag: function () {
  }, stop: function () {
  }, values: {min: 0, max: 20}, wheelSpeed: 4, wheelMode: null}, _values: {min: 0, max: 20}, _waitingToInit: 2, _wheelTimeout: !1, _create: function () {
    t.ui.rangeSliderDraggable.prototype._create.apply(this), this.element.css("position", "absolute").css("top", 0).addClass("ui-rangeSlider-bar"), this.options.leftHandle.bind("initialize", t.proxy(this._onInitialized, this)).bind("mousestart", t.proxy(this._cache, this)).bind("stop", t.proxy(this._onHandleStop, this)), this.options.rightHandle.bind("initialize", t.proxy(this._onInitialized, this)).bind("mousestart", t.proxy(this._cache, this)).bind("stop", t.proxy(this._onHandleStop, this)), this._bindHandles(), this._values = this.options.values, this._setWheelModeOption(this.options.wheelMode)
  }, _setOption: function (t, e) {
    "range" === t ? this._setRangeOption(e) : "wheelSpeed" === t ? this._setWheelSpeedOption(e) : "wheelMode" === t && this._setWheelModeOption(e)
  }, _setRangeOption: function (t) {
    if (("object" != typeof t || null === t) && (t = !1), t !== !1 || this.options.range !== !1) {
      if (t !== !1) {
        var e = t.min === undefined ? this.options.range.min || !1 : t.min, i = t.max === undefined ? this.options.range.max || !1 : t.max;
        this.options.range = {min: e, max: i}
      } else this.options.range = !1;
      this._setLeftRange(), this._setRightRange()
    }
  }, _setWheelSpeedOption: function (t) {
    "number" == typeof t && t > 0 && (this.options.wheelSpeed = t)
  }, _setWheelModeOption: function (t) {
    (null === t || t === !1 || "zoom" === t || "scroll" === t) && (this.options.wheelMode !== t && this.element.parent().unbind("mousewheel.bar"), this._bindMouseWheel(t), this.options.wheelMode = t)
  }, _bindMouseWheel: function (e) {
    "zoom" === e ? this.element.parent().bind("mousewheel.bar", t.proxy(this._mouseWheelZoom, this)) : "scroll" === e && this.element.parent().bind("mousewheel.bar", t.proxy(this._mouseWheelScroll, this))
  }, _setLeftRange: function () {
    if (this.options.range === !1)return!1;
    var t = this._values.max, e = {min: !1, max: !1};
    e.max = (this.options.range.min || !1) !== !1 ? this._leftHandle("substract", t, this.options.range.min) : !1, e.min = (this.options.range.max || !1) !== !1 ? this._leftHandle("substract", t, this.options.range.max) : !1, this._leftHandle("option", "range", e)
  }, _setRightRange: function () {
    var t = this._values.min, e = {min: !1, max: !1};
    e.min = (this.options.range.min || !1) !== !1 ? this._rightHandle("add", t, this.options.range.min) : !1, e.max = (this.options.range.max || !1) !== !1 ? this._rightHandle("add", t, this.options.range.max) : !1, this._rightHandle("option", "range", e)
  }, _deactivateRange: function () {
    this._leftHandle("option", "range", !1), this._rightHandle("option", "range", !1)
  }, _reactivateRange: function () {
    this._setRangeOption(this.options.range)
  }, _onInitialized: function () {
    this._waitingToInit--, 0 === this._waitingToInit && this._initMe()
  }, _initMe: function () {
    this._cache(), this.min(this._values.min), this.max(this._values.max);
    var t = this._leftHandle("position"), e = this._rightHandle("position") + this.options.rightHandle.width();
    this.element.offset({left: t}), this.element.css("width", e - t)
  }, _leftHandle: function () {
    return this._handleProxy(this.options.leftHandle, arguments)
  }, _rightHandle: function () {
    return this._handleProxy(this.options.rightHandle, arguments)
  }, _handleProxy: function (t, e) {
    var i = Array.prototype.slice.call(e);
    return t[this.options.type].apply(t, i)
  }, _cache: function () {
    t.ui.rangeSliderDraggable.prototype._cache.apply(this), this._cacheHandles()
  }, _cacheHandles: function () {
    this.cache.rightHandle = {}, this.cache.rightHandle.width = this.options.rightHandle.width(), this.cache.rightHandle.offset = this.options.rightHandle.offset(), this.cache.leftHandle = {}, this.cache.leftHandle.offset = this.options.leftHandle.offset()
  }, _mouseStart: function (e) {
    t.ui.rangeSliderDraggable.prototype._mouseStart.apply(this, [e]), this._deactivateRange()
  }, _mouseStop: function (e) {
    t.ui.rangeSliderDraggable.prototype._mouseStop.apply(this, [e]), this._cacheHandles(), this._values.min = this._leftHandle("value"), this._values.max = this._rightHandle("value"), this._reactivateRange(), this._leftHandle().trigger("stop"), this._rightHandle().trigger("stop")
  }, _onDragLeftHandle: function (t, e) {
    return this._cacheIfNecessary(), this._switchedValues() ? (this._switchHandles(), this._onDragRightHandle(t, e), undefined) : (this._values.min = e.value, this.cache.offset.left = e.offset.left, this.cache.leftHandle.offset = e.offset, this._positionBar(), undefined)
  }, _onDragRightHandle: function (t, e) {
    return this._cacheIfNecessary(), this._switchedValues() ? (this._switchHandles(), this._onDragLeftHandle(t, e), undefined) : (this._values.max = e.value, this.cache.rightHandle.offset = e.offset, this._positionBar(), undefined)
  }, _positionBar: function () {
    var t = this.cache.rightHandle.offset.left + this.cache.rightHandle.width - this.cache.leftHandle.offset.left;
    this.cache.width.inner = t, this.element.css("width", t).offset({left: this.cache.leftHandle.offset.left})
  }, _onHandleStop: function () {
    this._setLeftRange(), this._setRightRange()
  }, _switchedValues: function () {
    if (this.min() > this.max()) {
      var t = this._values.min;
      return this._values.min = this._values.max, this._values.max = t, !0
    }
    return!1
  }, _switchHandles: function () {
    var t = this.options.leftHandle;
    this.options.leftHandle = this.options.rightHandle, this.options.rightHandle = t, this._leftHandle("option", "isLeft", !0), this._rightHandle("option", "isLeft", !1), this._bindHandles(), this._cacheHandles()
  }, _bindHandles: function () {
    this.options.leftHandle.unbind(".bar").bind("sliderDrag.bar update.bar moving.bar", t.proxy(this._onDragLeftHandle, this)), this.options.rightHandle.unbind(".bar").bind("sliderDrag.bar update.bar moving.bar", t.proxy(this._onDragRightHandle, this))
  }, _constraintPosition: function (e) {
    var i, s = {};
    return s.left = t.ui.rangeSliderDraggable.prototype._constraintPosition.apply(this, [e]), s.left = this._leftHandle("position", s.left), i = this._rightHandle("position", s.left + this.cache.width.outer - this.cache.rightHandle.width), s.width = i - s.left + this.cache.rightHandle.width, s
  }, _applyPosition: function (e) {
    t.ui.rangeSliderDraggable.prototype._applyPosition.apply(this, [e.left]), this.element.width(e.width)
  }, _mouseWheelZoom: function (e, i, s, n) {
    var o = this._values.min + (this._values.max - this._values.min) / 2, a = {}, h = {};
    return this.options.range === !1 || this.options.range.min === !1 ? (a.max = o, h.min = o) : (a.max = o - this.options.range.min / 2, h.min = o + this.options.range.min / 2), this.options.range !== !1 && this.options.range.max !== !1 && (a.min = o - this.options.range.max / 2, h.max = o + this.options.range.max / 2), this._leftHandle("option", "range", a), this._rightHandle("option", "range", h), clearTimeout(this._wheelTimeout), this._wheelTimeout = setTimeout(t.proxy(this._wheelStop, this), 200), this.zoomOut(n * this.options.wheelSpeed), !1
  }, _mouseWheelScroll: function (e, i, s, n) {
    return this._wheelTimeout === !1 ? this.startScroll() : clearTimeout(this._wheelTimeout), this._wheelTimeout = setTimeout(t.proxy(this._wheelStop, this), 200), this.scrollLeft(n * this.options.wheelSpeed), !1
  }, _wheelStop: function () {
    this.stopScroll(), this._wheelTimeout = !1
  }, min: function (t) {
    return this._leftHandle("value", t)
  }, max: function (t) {
    return this._rightHandle("value", t)
  }, startScroll: function () {
    this._deactivateRange()
  }, stopScroll: function () {
    this._reactivateRange(), this._triggerMouseEvent("stop"), this._leftHandle().trigger("stop"), this._rightHandle().trigger("stop")
  }, scrollLeft: function (t) {
    return t = t || 1, 0 > t ? this.scrollRight(-t) : (t = this._leftHandle("moveLeft", t), this._rightHandle("moveLeft", t), this.update(), this._triggerMouseEvent("scroll"), undefined)
  }, scrollRight: function (t) {
    return t = t || 1, 0 > t ? this.scrollLeft(-t) : (t = this._rightHandle("moveRight", t), this._leftHandle("moveRight", t), this.update(), this._triggerMouseEvent("scroll"), undefined)
  }, zoomIn: function (t) {
    if (t = t || 1, 0 > t)return this.zoomOut(-t);
    var e = this._rightHandle("moveLeft", t);
    t > e && (e /= 2, this._rightHandle("moveRight", e)), this._leftHandle("moveRight", e), this.update(), this._triggerMouseEvent("zoom")
  }, zoomOut: function (t) {
    if (t = t || 1, 0 > t)return this.zoomIn(-t);
    var e = this._rightHandle("moveRight", t);
    t > e && (e /= 2, this._rightHandle("moveLeft", e)), this._leftHandle("moveLeft", e), this.update(), this._triggerMouseEvent("zoom")
  }, values: function (t, e) {
    if (t !== undefined && e !== undefined) {
      var i = Math.min(t, e), s = Math.max(t, e);
      this._deactivateRange(), this.options.leftHandle.unbind(".bar"), this.options.rightHandle.unbind(".bar"), this._values.min = this._leftHandle("value", i), this._values.max = this._rightHandle("value", s), this._bindHandles(), this._reactivateRange(), this.update()
    }
    return{min: this._values.min, max: this._values.max}
  }, update: function () {
    this._values.min = this.min(), this._values.max = this.max(), this._cache(), this._positionBar()
  }})
}(jQuery), function (t) {
  "use strict";
  function e(e, i, s, n) {
    this.label1 = e, this.label2 = i, this.type = s, this.options = n, this.handle1 = this.label1[this.type]("option", "handle"), this.handle2 = this.label2[this.type]("option", "handle"), this.cache = null, this.left = e, this.right = i, this.moving = !1, this.initialized = !1, this.updating = !1, this.Init = function () {
      this.BindHandle(this.handle1), this.BindHandle(this.handle2), "show" === this.options.show ? (setTimeout(t.proxy(this.PositionLabels, this), 1), this.initialized = !0) : setTimeout(t.proxy(this.AfterInit, this), 1e3)
    }, this.AfterInit = function () {
      this.initialized = !0
    }, this.Cache = function () {
      "none" !== this.label1.css("display") && (this.cache = {}, this.cache.label1 = {}, this.cache.label2 = {}, this.cache.handle1 = {}, this.cache.handle2 = {}, this.cache.offsetParent = {}, this.CacheElement(this.label1, this.cache.label1), this.CacheElement(this.label2, this.cache.label2), this.CacheElement(this.handle1, this.cache.handle1), this.CacheElement(this.handle2, this.cache.handle2), this.CacheElement(this.label1.offsetParent(), this.cache.offsetParent))
    }, this.CacheIfNecessary = function () {
      null === this.cache ? this.Cache() : (this.CacheWidth(this.label1, this.cache.label1), this.CacheWidth(this.label2, this.cache.label2), this.CacheHeight(this.label1, this.cache.label1), this.CacheHeight(this.label2, this.cache.label2), this.CacheWidth(this.label1.offsetParent(), this.cache.offsetParent))
    }, this.CacheElement = function (t, e) {
      this.CacheWidth(t, e), this.CacheHeight(t, e), e.offset = t.offset(), e.margin = {left: this.ParsePixels("marginLeft", t), right: this.ParsePixels("marginRight", t)}, e.border = {left: this.ParsePixels("borderLeftWidth", t), right: this.ParsePixels("borderRightWidth", t)}
    }, this.CacheWidth = function (t, e) {
      e.width = t.width(), e.outerWidth = t.outerWidth()
    }, this.CacheHeight = function (t, e) {
      e.outerHeightMargin = t.outerHeight(!0)
    }, this.ParsePixels = function (t, e) {
      return parseInt(e.css(t), 10) || 0
    }, this.BindHandle = function (e) {
      e.bind("updating", t.proxy(this.onHandleUpdating, this)), e.bind("update", t.proxy(this.onHandleUpdated, this)), e.bind("moving", t.proxy(this.onHandleMoving, this)), e.bind("stop", t.proxy(this.onHandleStop, this))
    }, this.PositionLabels = function () {
      if (this.CacheIfNecessary(), null !== this.cache) {
        var t = this.GetRawPosition(this.cache.label1, this.cache.handle1), e = this.GetRawPosition(this.cache.label2, this.cache.handle2);
        this.ConstraintPositions(t, e), this.PositionLabel(this.label1, t.left, this.cache.label1), this.PositionLabel(this.label2, e.left, this.cache.label2)
      }
    }, this.PositionLabel = function (t, e, i) {
      var s, n, o, a = this.cache.offsetParent.offset.left + this.cache.offsetParent.border.left;
      a - e >= 0 ? (t.css("right", ""), t.offset({left: e})) : (s = a + this.cache.offsetParent.width, n = e + i.margin.left + i.outerWidth + i.margin.right, o = s - n, t.css("left", ""), t.css("right", o))
    }, this.ConstraintPositions = function (t, e) {
      t.center < e.center && t.outerRight > e.outerLeft ? (t = this.getLeftPosition(t, e), e = this.getRightPosition(t, e)) : t.center > e.center && e.outerRight > t.outerLeft && (e = this.getLeftPosition(e, t), t = this.getRightPosition(e, t))
    }, this.getLeftPosition = function (t, e) {
      var i = (e.center + t.center) / 2, s = i - t.cache.outerWidth - t.cache.margin.right + t.cache.border.left;
      return t.left = s, t
    }, this.getRightPosition = function (t, e) {
      var i = (e.center + t.center) / 2;
      return e.left = i + e.cache.margin.left + e.cache.border.left, e
    }, this.ShowIfNecessary = function () {
      "show" === this.options.show || this.moving || !this.initialized || this.updating || (this.label1.stop(!0, !0).fadeIn(this.options.durationIn || 0), this.label2.stop(!0, !0).fadeIn(this.options.durationIn || 0), this.moving = !0)
    }, this.HideIfNeeded = function () {
      this.moving === !0 && (this.label1.stop(!0, !0).delay(this.options.delayOut || 0).fadeOut(this.options.durationOut || 0), this.label2.stop(!0, !0).delay(this.options.delayOut || 0).fadeOut(this.options.durationOut || 0), this.moving = !1)
    }, this.onHandleMoving = function (t, e) {
      this.ShowIfNecessary(), this.CacheIfNecessary(), this.UpdateHandlePosition(e), this.PositionLabels()
    }, this.onHandleUpdating = function () {
      this.updating = !0
    }, this.onHandleUpdated = function () {
      this.updating = !1, this.cache = null
    }, this.onHandleStop = function () {
      this.HideIfNeeded()
    }, this.UpdateHandlePosition = function (t) {
      null !== this.cache && (t.element[0] === this.handle1[0] ? this.UpdatePosition(t, this.cache.handle1) : this.UpdatePosition(t, this.cache.handle2))
    }, this.UpdatePosition = function (t, e) {
      e.offset = t.offset
    }, this.GetRawPosition = function (t, e) {
      var i = e.offset.left + e.outerWidth / 2, s = i - t.outerWidth / 2, n = s + t.outerWidth - t.border.left - t.border.right, o = s - t.margin.left - t.border.left, a = e.offset.top - t.outerHeightMargin;
      return{left: s, outerLeft: o, top: a, right: n, outerRight: o + t.outerWidth + t.margin.left + t.margin.right, cache: t, center: i}
    }, this.Init()
  }

  t.widget("ui.rangeSliderLabel", t.ui.rangeSliderMouseTouch, {options: {handle: null, formatter: !1, handleType: "rangeSliderHandle", show: "show", durationIn: 0, durationOut: 500, delayOut: 500, isLeft: !1}, cache: null, _positionner: null, _valueContainer: null, _innerElement: null, _create: function () {
    this.options.isLeft = this._handle("option", "isLeft"), this.element.addClass("ui-rangeSlider-label").css("position", "absolute").css("display", "block"), this._createElements(), this._toggleClass(), this.options.handle.bind("moving", t.proxy(this._onMoving, this)).bind("update", t.proxy(this._onUpdate, this)).bind("switch", t.proxy(this._onSwitch, this)), "show" !== this.options.show && this.element.hide(), this._mouseInit()
  }, _createElements: function () {
    this._valueContainer = t("<div class='ui-rangeSlider-label-value' />").appendTo(this.element), this._innerElement = t("<div class='ui-rangeSlider-label-inner' />").appendTo(this.element)
  }, _handle: function () {
    var t = Array.prototype.slice.apply(arguments);
    return this.options.handle[this.options.handleType].apply(this.options.handle, t)
  }, _setOption: function (t, e) {
    "show" === t ? this._updateShowOption(e) : ("durationIn" === t || "durationOut" === t || "delayOut" === t) && this._updateDurations(t, e)
  }, _updateShowOption: function (t) {
    this.options.show = t, "show" !== this.options.show ? this.element.hide() : (this.element.show(), this._display(this.options.handle[this.options.handleType]("value")), this._positionner.PositionLabels()), this._positionner.options.show = this.options.show
  }, _updateDurations: function (t, e) {
    parseInt(e, 10) === e && (this._positionner.options[t] = e, this.options[t] = e)
  }, _display: function (t) {
    this.options.formatter === !1 ? this._displayText(Math.round(t)) : this._displayText(this.options.formatter(t))
  }, _displayText: function (t) {
    this._valueContainer.text(t)
  }, _toggleClass: function () {
    this.element.toggleClass("ui-rangeSlider-leftLabel", this.options.isLeft).toggleClass("ui-rangeSlider-rightLabel", !this.options.isLeft)
  }, _mouseDown: function (t) {
    this.options.handle.trigger(t)
  }, _mouseUp: function (t) {
    this.options.handle.trigger(t)
  }, _mouseMove: function (t) {
    this.options.handle.trigger(t)
  }, _onMoving: function (t, e) {
    this._display(e.value)
  }, _onUpdate: function () {
    "show" === this.options.show && this.update()
  }, _onSwitch: function (t, e) {
    this.options.isLeft = e, this._toggleClass(), this._positionner.PositionLabels()
  }, pair: function (t) {
    null === this._positionner && (this._positionner = new e(this.element, t, this.widgetName, {show: this.options.show, durationIn: this.options.durationIn, durationOut: this.options.durationOut, delayOut: this.options.delayOut}), t[this.widgetName]("positionner", this._positionner))
  }, positionner: function (t) {
    return t !== undefined && (this._positionner = t), this._positionner
  }, update: function () {
    this._positionner.cache = null, this._display(this._handle("value")), "show" === this.options.show && this._positionner.PositionLabels()
  }})
}(jQuery), function (t) {
  "use strict";
  t.widget("ui.dateRangeSlider", t.ui.rangeSlider, {options: {bounds: {min: new Date(2010, 0, 1), max: new Date(2012, 0, 1)}, defaultValues: {min: new Date(2010, 1, 11), max: new Date(2011, 1, 11)}}, _create: function () {
    t.ui.rangeSlider.prototype._create.apply(this), this.element.addClass("ui-dateRangeSlider")
  }, destroy: function () {
    this.element.removeClass("ui-dateRangeSlider"), t.ui.rangeSlider.prototype.destroy.apply(this)
  }, _setOption: function (e, i) {
    ("defaultValues" === e || "bounds" === e) && i !== undefined && null !== i && this._isValidDate(i.min) && this._isValidDate(i.max) ? t.ui.rangeSlider.prototype._setOption.apply(this, [e, {min: i.min.valueOf(), max: i.max.valueOf()}]) : t.ui.rangeSlider.prototype._setOption.apply(this, this._toArray(arguments))
  }, _handleType: function () {
    return"dateRangeSliderHandle"
  }, option: function (e) {
    if ("bounds" === e || "defaultValues" === e) {
      var i = t.ui.rangeSlider.prototype.option.apply(this, arguments);
      return{min: new Date(i.min), max: new Date(i.max)}
    }
    return t.ui.rangeSlider.prototype.option.apply(this, this._toArray(arguments))
  }, _defaultFormatter: function (t) {
    var e = t.getMonth() + 1, i = t.getDate();
    return"" + t.getFullYear() + "-" + (10 > e ? "0" + e : e) + "-" + (10 > i ? "0" + i : i)
  }, _getFormatter: function () {
    var t = this.options.formatter;
    return(this.options.formatter === !1 || null === this.options.formatter) && (t = this._defaultFormatter), function (t) {
      return function (e) {
        return t(new Date(e))
      }
    }(t)
  }, values: function (e, i) {
    var s = null;
    return s = this._isValidDate(e) && this._isValidDate(i) ? t.ui.rangeSlider.prototype.values.apply(this, [e.valueOf(), i.valueOf()]) : t.ui.rangeSlider.prototype.values.apply(this, this._toArray(arguments)), {min: new Date(s.min), max: new Date(s.max)}
  }, min: function (e) {
    return this._isValidDate(e) ? new Date(t.ui.rangeSlider.prototype.min.apply(this, [e.valueOf()])) : new Date(t.ui.rangeSlider.prototype.min.apply(this))
  }, max: function (e) {
    return this._isValidDate(e) ? new Date(t.ui.rangeSlider.prototype.max.apply(this, [e.valueOf()])) : new Date(t.ui.rangeSlider.prototype.max.apply(this))
  }, bounds: function (e, i) {
    var s;
    return s = this._isValidDate(e) && this._isValidDate(i) ? t.ui.rangeSlider.prototype.bounds.apply(this, [e.valueOf(), i.valueOf()]) : t.ui.rangeSlider.prototype.bounds.apply(this, this._toArray(arguments)), {min: new Date(s.min), max: new Date(s.max)}
  }, _isValidDate: function (t) {
    return t !== undefined && t instanceof Date
  }, _toArray: function (t) {
    return Array.prototype.slice.call(t)
  }})
}(jQuery), function (t) {
  "use strict";
  t.widget("ui.dateRangeSliderHandle", t.ui.rangeSliderHandle, {_steps: !1, _boundsValues: {}, _create: function () {
    t.ui.rangeSliderHandle.prototype._create.apply(this), this._createBoundsValues()
  }, _getValueForPosition: function (t) {
    var e = this._getRawValueForPositionAndBounds(t, this.options.bounds.min.valueOf(), this.options.bounds.max.valueOf());
    return this._constraintValue(new Date(e))
  }, _setOption: function (e, i) {
    return"step" === e ? (this.options.step = i, this._createSteps(), this.update(), undefined) : (t.ui.rangeSliderHandle.prototype._setOption.apply(this, [e, i]), "bounds" === e && this._createBoundsValues(), undefined)
  }, _createBoundsValues: function () {
    this._boundsValues = {min: this.options.bounds.min.valueOf(), max: this.options.bounds.max.valueOf()}
  }, _bounds: function () {
    return this._boundsValues
  }, _createSteps: function () {
    if (this.options.step === !1 || !this._isValidStep())return this._steps = !1, undefined;
    var t = new Date(this.options.bounds.min), e = new Date(this.options.bounds.max), i = t, s = 0;
    for (this._steps = []; e >= i;)this._steps.push(i.valueOf()), i = this._addStep(t, s, this.options.step), s++
  }, _isValidStep: function () {
    return"object" == typeof this.options.step
  }, _addStep: function (t, e, i) {
    var s = new Date(t.valueOf());
    return s = this._addThing(s, "FullYear", e, i.years), s = this._addThing(s, "Month", e, i.months), s = this._addThing(s, "Date", e, i.days), s = this._addThing(s, "Hours", e, i.hours), s = this._addThing(s, "Minutes", e, i.minutes), s = this._addThing(s, "Seconds", e, i.seconds)
  }, _addThing: function (t, e, i, s) {
    return 0 === i || 0 === (s || 0) ? t : (t["set" + e](t["get" + e]() + i * (s || 0)), t)
  }, _round: function (t) {
    if (this._steps === !1)return t;
    for (var e, i, s = this.options.bounds.max.valueOf(), n = this.options.bounds.min.valueOf(), o = Math.max(0, (t - n) / (s - n)), a = Math.floor(this._steps.length * o); this._steps[a] > t;)a--;
    for (; this._steps.length > a + 1 && t >= this._steps[a + 1];)a++;
    return a >= this._steps.length - 1 ? this._steps[this._steps.length - 1] : 0 === a ? this._steps[0] : (e = this._steps[a], i = this._steps[a + 1], i - t > t - e ? e : i)
  }, update: function () {
    this._createBoundsValues(), this._createSteps(), t.ui.rangeSliderHandle.prototype.update.apply(this)
  }, add: function (t, e) {
    return this._addStep(new Date(t), 1, e).valueOf()
  }, substract: function (t, e) {
    return this._addStep(new Date(t), -1, e).valueOf()
  }, stepsBetween: function (t, e) {
    if (this.options.step === !1)return e - t;
    var i = Math.min(t, e), s = Math.max(t, e), n = 0, o = !1, a = t > e;
    for (0 > this.add(i, this.options.step) - i && (o = !0); s > i;)o ? s = this.add(s, this.options.step) : i = this.add(i, this.options.step), n++;
    return a ? -n : n
  }, multiplyStep: function (t, e) {
    var i = {};
    for (var s in t)t.hasOwnProperty(s) && (i[s] = t[s] * e);
    return i
  }, stepRatio: function () {
    if (this.options.step === !1)return 1;
    var t = this._steps.length;
    return this.cache.parent.width / t
  }})
}(jQuery), function (t) {
  "use strict";
  var e = {first: function (t) {
    return t
  }, next: function (t) {
    return t + 1
  }, label: function (t) {
    return Math.round(t)
  }, stop: function () {
    return!1
  }};
  t.widget("ui.ruler", {options: {min: 0, max: 100, scales: []}, _create: function () {
    this.element.addClass("ui-ruler"), this._createScales()
  }, destroy: function () {
    this.element.removeClass("ui-ruler"), this.element.empty()
  }, _regenerate: function () {
    this.element.empty(), this._createScales()
  }, _setOption: function (t, e) {
    return"min" === t || "max" === t && e !== this.options[t] ? (this.options[t] = e, this._regenerate(), void 0) : "scales" === t && e instanceof Array ? (this.options.scales = e, this._regenerate(), void 0) : void 0
  }, _createScales: function () {
    if (this.options.max !== this.options.min)for (var t = 0; this.options.scales.length > t; t++)this._createScale(this.options.scales[t], t)
  }, _createScale: function (i, s) {
    var n = t.extend({}, e, i), o = t("<div class='ui-ruler-scale' />").appendTo(this.element);
    o.addClass("ui-ruler-scale" + s), this._createTicks(o, n)
  }, _createTicks: function (t, e) {
    var i, s, n, o = e.first(this.options.min, this.options.max), a = this.options.max - this.options.min, h = !0;
    do i = o, o = e.next(i), s = (Math.min(o, this.options.max) - Math.max(i, this.options.min)) / a, n = this._createTick(i, o, e), t.append(n), n.css("width", 100 * s + "%"), h && i > this.options.min && n.css("margin-left", 100 * (i - this.options.min) / a + "%"), h = !1; while (!this._stop(e, o))
  }, _stop: function (t, e) {
    return t.stop(e) || e >= this.options.max
  }, _createTick: function (e, i, s) {
    var n = t("<div class='ui-ruler-tick' style='display:inline-block' />"), o = t("<div class='ui-ruler-tick-inner' />").appendTo(n), a = t("<span class='ui-ruler-tick-label' />").appendTo(o);
    return a.text(s.label(e, i)), n
  }})
}(jQuery);