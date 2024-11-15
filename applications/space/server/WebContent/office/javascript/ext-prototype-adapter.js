/*
 * Ext JS Library 1.1
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://www.extjs.com/license
 */


Ext = {};
window["undefined"] = window["undefined"];
Ext.apply = function (o, c, _3) {
  if (_3) {
    Ext.apply(o, _3);
  }
  if (o && c && typeof c == "object") {
    for (var p in c) {
      o[p] = c[p];
    }
  }
  return o;
};
(function () {
  var _5 = 0;
  var ua = navigator.userAgent.toLowerCase();
  var _7 = document.compatMode == "CSS1Compat", _8 = ua.indexOf("opera") > -1, _9 = (/webkit|khtml/).test(ua), _a = ua.indexOf("msie") > -1, _b = ua.indexOf("msie 7") > -1, _c = !_9 && ua.indexOf("gecko") > -1, _d = _a && !_7, _e = (ua.indexOf("windows") != -1 || ua.indexOf("win32") != -1), _f = (ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1), _10 = (ua.indexOf("linux") != -1), _11 = window.location.href.toLowerCase().indexOf("https") === 0;
  if (_a && !_b) {
    try {
      document.execCommand("BackgroundImageCache", false, true);
    } catch (e) {
    }
  }
  Ext.apply(Ext, {isStrict: _7, isSecure: _11, isReady: false, enableGarbageCollector: true, enableListenerCollection: false, SSL_SECURE_URL: "javascript:false", BLANK_IMAGE_URL: "http:/" + "/extjs.com/s.gif", emptyFn: function () {
  }, applyIf: function (o, c) {
    if (o && c) {
      for (var p in c) {
        if (typeof o[p] == "undefined") {
          o[p] = c[p];
        }
      }
    }
    return o;
  }, addBehaviors: function (o) {
    if (!Ext.isReady) {
      Ext.onReady(function () {
        Ext.addBehaviors(o);
      });
      return;
    }
    var _16 = {};
    for (var b in o) {
      var _18 = b.split("@");
      if (_18[1]) {
        var s = _18[0];
        if (!_16[s]) {
          _16[s] = Ext.select(s);
        }
        _16[s].on(_18[1], o[b]);
      }
    }
    _16 = null;
  }, id: function (el, _1b) {
    _1b = _1b || "ext-gen";
    el = Ext.getDom(el);
    var id = _1b + (++_5);
    return el ? (el.id ? el.id : (el.id = id)) : id;
  }, extend: function () {
    var io = function (o) {
      for (var m in o) {
        this[m] = o[m];
      }
    };
    return function (sb, sp, _22) {
      if (typeof sp == "object") {
        _22 = sp;
        sp = sb;
        sb = function () {
          sp.apply(this, arguments);
        };
      }
      var F = function () {
      }, sbp, spp = sp.prototype;
      F.prototype = spp;
      sbp = sb.prototype = new F();
      sbp.constructor = sb;
      sb.superclass = spp;
      if (spp.constructor == Object.prototype.constructor) {
        spp.constructor = sp;
      }
      sb.override = function (o) {
        Ext.override(sb, o);
      };
      sbp.override = io;
      Ext.override(sb, _22);
      return sb;
    };
  }(), override: function (_27, _28) {
    if (_28) {
      var p = _27.prototype;
      for (var _2a in _28) {
        p[_2a] = _28[_2a];
      }
    }
  }, namespace: function () {
    var a = arguments, o = null, i, j, d, rt;
    for (i = 0; i < a.length; ++i) {
      d = a[i].split(".");
      rt = d[0];
      eval("if (typeof " + rt + " == \"undefined\"){" + rt + " = {};} o = " + rt + ";");
      for (j = 1; j < d.length; ++j) {
        o[d[j]] = o[d[j]] || {};
        o = o[d[j]];
      }
    }
  }, urlEncode: function (o) {
    if (!o) {
      return"";
    }
    var buf = [];
    for (var key in o) {
      var ov = o[key];
      var _35 = typeof ov;
      if (_35 == "undefined") {
        buf.push(encodeURIComponent(key), "=&");
      } else {
        if (_35 != "function" && _35 != "object") {
          buf.push(encodeURIComponent(key), "=", encodeURIComponent(ov), "&");
        } else {
          if (ov instanceof Array) {
            for (var i = 0, len = ov.length; i < len; i++) {
              buf.push(encodeURIComponent(key), "=", encodeURIComponent(ov[i] === undefined ? "" : ov[i]), "&");
            }
          }
        }
      }
    }
    buf.pop();
    return buf.join("");
  }, urlDecode: function (_38, _39) {
    if (!_38 || !_38.length) {
      return{};
    }
    var obj = {};
    var _3b = _38.split("&");
    var _3c, _3d, _3e;
    for (var i = 0, len = _3b.length; i < len; i++) {
      _3c = _3b[i].split("=");
      _3d = decodeURIComponent(_3c[0]);
      _3e = decodeURIComponent(_3c[1]);
      if (_39 !== true) {
        if (typeof obj[_3d] == "undefined") {
          obj[_3d] = _3e;
        } else {
          if (typeof obj[_3d] == "string") {
            obj[_3d] = [obj[_3d]];
            obj[_3d].push(_3e);
          } else {
            obj[_3d].push(_3e);
          }
        }
      } else {
        obj[_3d] = _3e;
      }
    }
    return obj;
  }, each: function (_41, fn, _43) {
    if (typeof _41.length == "undefined" || typeof _41 == "string") {
      _41 = [_41];
    }
    for (var i = 0, len = _41.length; i < len; i++) {
      if (fn.call(_43 || _41[i], _41[i], i, _41) === false) {
        return i;
      }
    }
  }, combine: function () {
    var as = arguments, l = as.length, r = [];
    for (var i = 0; i < l; i++) {
      var a = as[i];
      if (a instanceof Array) {
        r = r.concat(a);
      } else {
        if (a.length !== undefined && !a.substr) {
          r = r.concat(Array.prototype.slice.call(a, 0));
        } else {
          r.push(a);
        }
      }
    }
    return r;
  }, escapeRe: function (s) {
    return s.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1");
  }, callback: function (cb, _4d, _4e, _4f) {
    if (typeof cb == "function") {
      if (_4f) {
        cb.defer(_4f, _4d, _4e || []);
      } else {
        cb.apply(_4d, _4e || []);
      }
    }
  }, getDom: function (el) {
    if (!el) {
      return null;
    }
    return el.dom ? el.dom : (typeof el == "string" ? document.getElementById(el) : el);
  }, getCmp: function (id) {
    return Ext.ComponentMgr.get(id);
  }, num: function (v, _53) {
    if (typeof v != "number") {
      return _53;
    }
    return v;
  }, destroy: function () {
    for (var i = 0, a = arguments, len = a.length; i < len; i++) {
      var as = a[i];
      if (as) {
        if (as.dom) {
          as.removeAllListeners();
          as.remove();
          continue;
        }
        if (typeof as.purgeListeners == "function") {
          as.purgeListeners();
        }
        if (typeof as.destroy == "function") {
          as.destroy();
        }
      }
    }
  }, type: function (o) {
    if (o === undefined || o === null) {
      return false;
    }
    if (o.htmlElement) {
      return"element";
    }
    var t = typeof o;
    if (t == "object" && o.nodeName) {
      switch (o.nodeType) {
        case 1:
          return"element";
        case 3:
          return(/\S/).test(o.nodeValue) ? "textnode" : "whitespace";
      }
    }
    if (t == "object" || t == "function") {
      switch (o.constructor) {
        case Array:
          return"array";
        case RegExp:
          return"regexp";
      }
      if (typeof o.length == "number" && typeof o.item == "function") {
        return"nodelist";
      }
    }
    return t;
  }, isEmpty: function (v, _5b) {
    return v === null || v === undefined || (!_5b ? v === "" : false);
  }, isOpera: _8, isSafari: _9, isIE: _a, isIE7: _b, isGecko: _c, isBorderBox: _d, isWindows: _e, isLinux: _10, isMac: _f, useShims: ((_a && !_b) || (_c && _f))});
})();
Ext.namespace("Ext", "Ext.util", "Ext.grid", "Ext.dd", "Ext.tree", "Ext.data", "Ext.form", "Ext.menu", "Ext.state", "Ext.lib", "Ext.layout", "Ext.app", "Ext.ux");
Ext.apply(Function.prototype, {createCallback: function () {
  var _5c = arguments;
  var _5d = this;
  return function () {
    return _5d.apply(window, _5c);
  };
}, createDelegate: function (obj, _5f, _60) {
  var _61 = this;
  return function () {
    var _62 = _5f || arguments;
    if (_60 === true) {
      _62 = Array.prototype.slice.call(arguments, 0);
      _62 = _62.concat(_5f);
    } else {
      if (typeof _60 == "number") {
        _62 = Array.prototype.slice.call(arguments, 0);
        var _63 = [_60, 0].concat(_5f);
        Array.prototype.splice.apply(_62, _63);
      }
    }
    return _61.apply(obj || window, _62);
  };
}, defer: function (_64, obj, _66, _67) {
  var fn = this.createDelegate(obj, _66, _67);
  if (_64) {
    return setTimeout(fn, _64);
  }
  fn();
  return 0;
}, createSequence: function (fcn, _6a) {
  if (typeof fcn != "function") {
    return this;
  }
  var _6b = this;
  return function () {
    var _6c = _6b.apply(this || window, arguments);
    fcn.apply(_6a || this || window, arguments);
    return _6c;
  };
}, createInterceptor: function (fcn, _6e) {
  if (typeof fcn != "function") {
    return this;
  }
  var _6f = this;
  return function () {
    fcn.target = this;
    fcn.method = _6f;
    if (fcn.apply(_6e || this || window, arguments) === false) {
      return;
    }
    return _6f.apply(this || window, arguments);
  };
}});
Ext.applyIf(String, {escape: function (_70) {
  return _70.replace(/('|\\)/g, "\\$1");
}, leftPad: function (val, _72, ch) {
  var _74 = new String(val);
  if (ch === null || ch === undefined || ch === "") {
    ch = " ";
  }
  while (_74.length < _72) {
    _74 = ch + _74;
  }
  return _74;
}, format: function (_75) {
  var _76 = Array.prototype.slice.call(arguments, 1);
  return _75.replace(/\{(\d+)\}/g, function (m, i) {
    return _76[i];
  });
}});
String.prototype.toggle = function (_79, _7a) {
  return this == _79 ? _7a : _79;
};
Ext.applyIf(Number.prototype, {constrain: function (min, max) {
  return Math.min(Math.max(this, min), max);
}});
Ext.applyIf(Array.prototype, {indexOf: function (o) {
  for (var i = 0, len = this.length; i < len; i++) {
    if (this[i] == o) {
      return i;
    }
  }
  return-1;
}, remove: function (o) {
  var _81 = this.indexOf(o);
  if (_81 != -1) {
    this.splice(_81, 1);
  }
}});
Date.prototype.getElapsed = function (_82) {
  return Math.abs((_82 || new Date()).getTime() - this.getTime());
};

(function () {
  var _1;
  Ext.lib.Dom = {getViewWidth: function (_2) {
    return _2 ? this.getDocumentWidth() : this.getViewportWidth();
  }, getViewHeight: function (_3) {
    return _3 ? this.getDocumentHeight() : this.getViewportHeight();
  }, getDocumentHeight: function () {
    var _4 = (document.compatMode != "CSS1Compat") ? document.body.scrollHeight : document.documentElement.scrollHeight;
    return Math.max(_4, this.getViewportHeight());
  }, getDocumentWidth: function () {
    var _5 = (document.compatMode != "CSS1Compat") ? document.body.scrollWidth : document.documentElement.scrollWidth;
    return Math.max(_5, this.getViewportWidth());
  }, getViewportHeight: function () {
    var _6 = self.innerHeight;
    var _7 = document.compatMode;
    if ((_7 || Ext.isIE) && !Ext.isOpera) {
      _6 = (_7 == "CSS1Compat") ? document.documentElement.clientHeight : document.body.clientHeight;
    }
    return _6;
  }, getViewportWidth: function () {
    var _8 = self.innerWidth;
    var _9 = document.compatMode;
    if (_9 || Ext.isIE) {
      _8 = (_9 == "CSS1Compat") ? document.documentElement.clientWidth : document.body.clientWidth;
    }
    return _8;
  }, isAncestor: function (p, c) {
    p = Ext.getDom(p);
    c = Ext.getDom(c);
    if (!p || !c) {
      return false;
    }
    if (p.contains && !Ext.isSafari) {
      return p.contains(c);
    } else {
      if (p.compareDocumentPosition) {
        return!!(p.compareDocumentPosition(c) & 16);
      } else {
        var _c = c.parentNode;
        while (_c) {
          if (_c == p) {
            return true;
          } else {
            if (!_c.tagName || _c.tagName.toUpperCase() == "HTML") {
              return false;
            }
          }
          _c = _c.parentNode;
        }
        return false;
      }
    }
  }, getRegion: function (el) {
    return Ext.lib.Region.getRegion(el);
  }, getY: function (el) {
    return this.getXY(el)[1];
  }, getX: function (el) {
    return this.getXY(el)[0];
  }, getXY: function (el) {
    var p, pe, b, _14, bd = document.body;
    el = Ext.getDom(el);
    if (el.getBoundingClientRect) {
      b = el.getBoundingClientRect();
      _14 = fly(document).getScroll();
      return[b.left + _14.left, b.top + _14.top];
    }
    var x = 0, y = 0;
    p = el;
    var _18 = fly(el).getStyle("position") == "absolute";
    while (p) {
      x += p.offsetLeft;
      y += p.offsetTop;
      if (!_18 && fly(p).getStyle("position") == "absolute") {
        _18 = true;
      }
      if (Ext.isGecko) {
        pe = fly(p);
        var bt = parseInt(pe.getStyle("borderTopWidth"), 10) || 0;
        var bl = parseInt(pe.getStyle("borderLeftWidth"), 10) || 0;
        x += bl;
        y += bt;
        if (p != el && pe.getStyle("overflow") != "visible") {
          x += bl;
          y += bt;
        }
      }
      p = p.offsetParent;
    }
    if (Ext.isSafari && _18) {
      x -= bd.offsetLeft;
      y -= bd.offsetTop;
    }
    if (Ext.isGecko && !_18) {
      var dbd = fly(bd);
      x += parseInt(dbd.getStyle("borderLeftWidth"), 10) || 0;
      y += parseInt(dbd.getStyle("borderTopWidth"), 10) || 0;
    }
    p = el.parentNode;
    while (p && p != bd) {
      if (!(Ext.isOpera && p.tagName != "TR" && fly(p).getStyle("display") != "inline")) {
        x -= p.scrollLeft;
        y -= p.scrollTop;
      }
      p = p.parentNode;
    }
    return[x, y];
  }, setXY: function (el, xy) {
    el = Ext.fly(el, "_setXY");
    el.position();
    var pts = el.translatePoints(xy);
    if (xy[0] !== false) {
      el.dom.style.left = pts.left + "px";
    }
    if (xy[1] !== false) {
      el.dom.style.top = pts.top + "px";
    }
  }, setX: function (el, x) {
    this.setXY(el, [x, false]);
  }, setY: function (el, y) {
    this.setXY(el, [false, y]);
  }};
  Ext.lib.Event = {getPageX: function (e) {
    return Event.pointerX(e.browserEvent || e);
  }, getPageY: function (e) {
    return Event.pointerY(e.browserEvent || e);
  }, getXY: function (e) {
    e = e.browserEvent || e;
    return[Event.pointerX(e), Event.pointerY(e)];
  }, getTarget: function (e) {
    return Event.element(e.browserEvent || e);
  }, resolveTextNode: function (_27) {
    if (_27 && 3 == _27.nodeType) {
      return _27.parentNode;
    } else {
      return _27;
    }
  }, getRelatedTarget: function (ev) {
    ev = ev.browserEvent || ev;
    var t = ev.relatedTarget;
    if (!t) {
      if (ev.type == "mouseout") {
        t = ev.toElement;
      } else {
        if (ev.type == "mouseover") {
          t = ev.fromElement;
        }
      }
    }
    return this.resolveTextNode(t);
  }, on: function (el, _2b, fn) {
    Event.observe(el, _2b, fn, false);
  }, un: function (el, _2e, fn) {
    Event.stopObserving(el, _2e, fn, false);
  }, purgeElement: function (el) {
  }, preventDefault: function (e) {
    e = e.browserEvent || e;
    if (e.preventDefault) {
      e.preventDefault();
    } else {
      e.returnValue = false;
    }
  }, stopPropagation: function (e) {
    e = e.browserEvent || e;
    if (e.stopPropagation) {
      e.stopPropagation();
    } else {
      e.cancelBubble = true;
    }
  }, stopEvent: function (e) {
    Event.stop(e.browserEvent || e);
  }, onAvailable: function (id, fn, _36) {
    var _37 = new Date(), iid;
    var f = function () {
      if (_37.getElapsed() > 10000) {
        clearInterval(iid);
      }
      var el = document.getElementById(id);
      if (el) {
        clearInterval(iid);
        fn.call(_36 || window, el);
      }
    };
    iid = setInterval(f, 50);
  }};
  Ext.lib.Ajax = function () {
    var _3b = function (cb) {
      return cb.success ? function (xhr) {
        cb.success.call(cb.scope || window, {responseText: xhr.responseText, responseXML: xhr.responseXML, argument: cb.argument});
      } : Ext.emptyFn;
    };
    var _3e = function (cb) {
      return cb.failure ? function (xhr) {
        cb.failure.call(cb.scope || window, {responseText: xhr.responseText, responseXML: xhr.responseXML, argument: cb.argument});
      } : Ext.emptyFn;
    };
    return{request: function (_41, uri, cb, _44, _45) {
      var o = {method: _41, parameters: _44 || "", timeout: cb.timeout, onSuccess: _3b(cb), onFailure: _3e(cb)};
      if (_45) {
        if (_45.headers) {
          o.requestHeaders = _45.headers;
        }
        if (_45.xmlData) {
          _41 = "POST";
          o.contentType = "text/xml";
          o.postBody = _45.xmlData;
          delete o.parameters;
        }
      }
      new Ajax.Request(uri, o);
    }, formRequest: function (_47, uri, cb, _4a, _4b, _4c) {
      new Ajax.Request(uri, {method: Ext.getDom(_47).method || "POST", parameters: Form.serialize(_47) + (_4a ? "&" + _4a : ""), timeout: cb.timeout, onSuccess: _3b(cb), onFailure: _3e(cb)});
    }, isCallInProgress: function (_4d) {
      return false;
    }, abort: function (_4e) {
      return false;
    }, serializeForm: function (_4f) {
      return Form.serialize(_4f.dom || _4f);
    }};
  }();
  Ext.lib.Anim = function () {
    var _50 = {easeOut: function (pos) {
      return 1 - Math.pow(1 - pos, 2);
    }, easeIn: function (pos) {
      return 1 - Math.pow(1 - pos, 2);
    }};
    var _53 = function (cb, _55) {
      return{stop: function (_56) {
        this.effect.cancel();
      }, isAnimated: function () {
        return this.effect.state == "running";
      }, proxyCallback: function () {
        Ext.callback(cb, _55);
      }};
    };
    return{scroll: function (el, _58, _59, _5a, cb, _5c) {
      var _5d = _53(cb, _5c);
      el = Ext.getDom(el);
      el.scrollLeft = _58.to[0];
      el.scrollTop = _58.to[1];
      _5d.proxyCallback();
      return _5d;
    }, motion: function (el, _5f, _60, _61, cb, _63) {
      return this.run(el, _5f, _60, _61, cb, _63);
    }, color: function (el, _65, _66, _67, cb, _69) {
      return this.run(el, _65, _66, _67, cb, _69);
    }, run: function (el, _6b, _6c, _6d, cb, _6f, _70) {
      var o = {};
      for (var k in _6b) {
        switch (k) {
          case"points":
            var by, pts, e = Ext.fly(el, "_animrun");
            e.position();
            if (by = _6b.points.by) {
              var xy = e.getXY();
              pts = e.translatePoints([xy[0] + by[0], xy[1] + by[1]]);
            } else {
              pts = e.translatePoints(_6b.points.to);
            }
            o.left = pts.left + "px";
            o.top = pts.top + "px";
            break;
          case"width":
            o.width = _6b.width.to + "px";
            break;
          case"height":
            o.height = _6b.height.to + "px";
            break;
          case"opacity":
            o.opacity = String(_6b.opacity.to);
            break;
          default:
            o[k] = String(_6b[k].to);
            break;
        }
      }
      var _77 = _53(cb, _6f);
      _77.effect = new Effect.Morph(Ext.id(el), {duration: _6c, afterFinish: _77.proxyCallback, transition: _50[_6d] || Effect.Transitions.linear, style: o});
      return _77;
    }};
  }();
  function fly(el) {
    if (!_1) {
      _1 = new Ext.Element.Flyweight();
    }
    _1.dom = el;
    return _1;
  }

  Ext.lib.Region = function (t, r, b, l) {
    this.top = t;
    this[1] = t;
    this.right = r;
    this.bottom = b;
    this.left = l;
    this[0] = l;
  };
  Ext.lib.Region.prototype = {contains: function (_7d) {
    return(_7d.left >= this.left && _7d.right <= this.right && _7d.top >= this.top && _7d.bottom <= this.bottom);
  }, getArea: function () {
    return((this.bottom - this.top) * (this.right - this.left));
  }, intersect: function (_7e) {
    var t = Math.max(this.top, _7e.top);
    var r = Math.min(this.right, _7e.right);
    var b = Math.min(this.bottom, _7e.bottom);
    var l = Math.max(this.left, _7e.left);
    if (b >= t && r >= l) {
      return new Ext.lib.Region(t, r, b, l);
    } else {
      return null;
    }
  }, union: function (_83) {
    var t = Math.min(this.top, _83.top);
    var r = Math.max(this.right, _83.right);
    var b = Math.max(this.bottom, _83.bottom);
    var l = Math.min(this.left, _83.left);
    return new Ext.lib.Region(t, r, b, l);
  }, adjust: function (t, l, b, r) {
    this.top += t;
    this.left += l;
    this.right += r;
    this.bottom += b;
    return this;
  }};
  Ext.lib.Region.getRegion = function (el) {
    var p = Ext.lib.Dom.getXY(el);
    var t = p[1];
    var r = p[0] + el.offsetWidth;
    var b = p[1] + el.offsetHeight;
    var l = p[0];
    return new Ext.lib.Region(t, r, b, l);
  };
  Ext.lib.Point = function (x, y) {
    if (x instanceof Array) {
      y = x[1];
      x = x[0];
    }
    this.x = this.right = this.left = this[0] = x;
    this.y = this.top = this.bottom = this[1] = y;
  };
  Ext.lib.Point.prototype = new Ext.lib.Region();
  if (Ext.isIE) {
    function fnCleanUp() {
      var p = Function.prototype;
      delete p.createSequence;
      delete p.defer;
      delete p.createDelegate;
      delete p.createCallback;
      delete p.createInterceptor;
      window.detachEvent("onunload", fnCleanUp);
    }

    window.attachEvent("onunload", fnCleanUp);
  }
})();
