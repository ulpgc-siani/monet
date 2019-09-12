//--------------------------------------------------------------------------------

function translate(sText, Data, sPrefix) {
  if (!sText) return;
  if (!sPrefix) sPrefix = EMPTY;

  if (Context.Config) {
    var Expression = new RegExp(TEMPLATE_SEPARATOR + Literals.ImagesPath + TEMPLATE_SEPARATOR, "g");
    sText = sText.replace(Expression, Context.Config.ImagesPath);
    Expression = new RegExp(TEMPLATE_SEPARATOR + Literals.Url + TEMPLATE_SEPARATOR, "g");
    sText = sText.replace(Expression, Context.Config.Url);
  }

  for (var index in Data) {
    if (Literals[index])
      var sName = sPrefix + Literals[index];
    else
      var sName = sPrefix + index;

    var Aux = Data[index];
    if (typeof Aux == "object") {
      sText = translate(sText, Aux, sName + DOT);
    }
    else {
      Expression = new RegExp(TEMPLATE_SEPARATOR + sName + TEMPLATE_SEPARATOR, "g");
      sText = sText.replace(Expression, Aux);
    }
  }
  return sText;
};

function addLayer(sContent, sContainer) {
  var Layer = new Insertion.Bottom($(sContainer), sContent).element.immediateDescendants().last();
  return Layer;
};

//--------------------------------------------------------------------------------

function createLayer(Id, sContent, DOMContainer) {
  var Layer = new Insertion.Bottom(DOMContainer, "<div id='" + Id + "'>" + sContent + "</div>").element.immediateDescendants().last();
  return Layer;
};

//--------------------------------------------------------------------------------

function getCreateLayerHTMLContent(Id) {
  return "<div id='" + Id + "'></div>";
};

//--------------------------------------------------------------------------------

function removeLayer(Id) {
  $(Id).remove();
};

//--------------------------------------------------------------------------------

function setIdToElementContent(Id, sElementContent) {
  var iPos = 0;
  var sLeftContent, sRightContent;

  while ((sElementContent.charAt(iPos) != BLANK) && (iPos < sElementContent.length)) iPos++;
  if (iPos >= sElementContent.length) return false;

  sLeftContent = sElementContent.substr(0, iPos);
  sRightContent = sElementContent.substr(iPos);

  sElementContent = sLeftContent + BLANK + "id=" + QUOTE + Id + QUOTE + sRightContent;

  return sElementContent;
};

//--------------------------------------------------------------------------------

function cleanContentIds(sContent) {
  sContent = sContent.replace(/ id=\"([^\"]*)\"/g, "");
  sContent = sContent.replace(/ id=\'([^\']*)\'/g, "");
  sContent = sContent.replace(/ id=([^ ]*)/g, "");
  return sContent;
};

//--------------------------------------------------------------------------------

function replaceDOMElement(DOMElement, sContent) {

  Result = sContent.match(/^<[^>]*id=\"([^\"]*)\"[^>]*>/);
  if (!Result) Result = sContent.match(/^<[^>]*id=\'([^\']*)\'[^>]*>/);
  if (!Result) Result = sContent.match(/^<[^>]*id=([^ ]*) [^>]*>/);

  if (Result) {
    Id = Result[1];
  }
  else {
    Id = DOMElement.id;
    if (!Id) Id = Ext.id();
    if (sContent != "") sContent = setIdToElementContent(Id, sContent);
  }

  if (Ext.isIE) {
    try {
      DOMElement.outerHTML = sContent;
    } catch (e) {
      Ext.MessageBox.alert("HTML Exception", "Check your HTML template. Perhaps you have '<form>' tags. HTML doesn't allow including forms into forms.");
    }
  }
  else {
    try {
      if (DOMElement.replace) DOMElement = DOMElement.replace(sContent);
    }
    catch (e) {
    }
  }

  DOMElement = $(Id);

  return DOMElement;
};

//--------------------------------------------------------------------------------

function utf8Encode(sData) {
  return unescape(encodeURIComponent(sData));
};

//--------------------------------------------------------------------------------

function utf8Decode(sData) {
  return decodeURIComponent(escape(sData));
};

//--------------------------------------------------------------------------------

function select(InputList, Type) {
  var aResult = new Array();
  InputList.each(function (Field) {
    if (Field.dom.type != "checkbox") return;
    Field.dom.checked = (Type != null) ? Type : !Field.dom.checked;
    Field.dom.select();
    aResult.push(Field.dom);
  });
  return aResult;
};

//--------------------------------------------------------------------------------

function selectAll(InputList) {
  return select(InputList, true);
};

//--------------------------------------------------------------------------------

function selectNone(InputList) {
  return select(InputList, false);
};

//--------------------------------------------------------------------------------

function selectInvert(InputList) {
  return select(InputList, null);
};

//--------------------------------------------------------------------------------

function selectHighlighted(HighlightedList) {
  var aResult = new Array();
  HighlightedList.each(function (Field) {
    if (Field.dom.type != "checkbox") return;
    Field.dom.checked = true;
    Field.dom.select();
    aResult.push(Field.dom);
  });
  return aResult;
};

//--------------------------------------------------------------------------------

function readData(Data, extData) {
  var extDataList = extData.select(HTML_DIV);
  extDataList.each(function (extData) {
    eval("Data." + extData.dom.id + "= '" + Ext.util.Format.htmlDecode(extData.dom.innerHTML) + "';");
  }, this);
};

//--------------------------------------------------------------------------------
function getFileExtension(sFilename) {
  var iPos = sFilename.lastIndexOf('.');
  if (iPos == -1) return "";
  return sFilename.substring(iPos + 1);
};

//--------------------------------------------------------------------------------
function getFileName(sFilename) {
  var iPos = sFilename.lastIndexOf('/');
  if (iPos == -1) return sFilename;
  return sFilename.substring(iPos + 1);
};

//--------------------------------------------------------------------------------
function parseXml(sContent) {
  var Document = null;

  if (document.implementation && document.implementation.createDocument) {
    var Parser = new DOMParser();
    Document = Parser.parseFromString(sContent, "text/xml");
  }
  else if (window.ActiveXObject) {
    Document = new ActiveXObject("Microsoft.XMLDOM");
    Document.async = false;
    Document.loadXML(sContent);
  }

  return Document;
};

//--------------------------------------------------------------------------------
function isArray(oObject) {
  return Object.prototype.toString.call(oObject) === '[object Array]';
};

//--------------------------------------------------------------------------------
function readServerResponse(mode, sData) {
  if (mode) return Base64.decode(sData);
  return HtmlUtil.decode(sData);
};

//--------------------------------------------------------------------------------
function writeServerRequest(mode, sData) {
  var sResult = (mode) ? "r=" + Base64.encode(sData) : sData;
  return "?" + sResult;
};

//--------------------------------------------------------------------------------
function serializeParameters(aParameters) {
  var sResult = "";
  for (var index in aParameters) {
    if (isFunction(aParameters[index])) continue;
    sResult += "&" + index + "=" + aParameters[index];
  }
  return sResult;
};

//--------------------------------------------------------------------------------
function getPreviousElement(extElement, sClassname) {
  var extElement = Ext.get(extElement.getPrevSibling());
  while ((extElement != null) && (!extElement.hasClass(sClassname))) {
    extElement = Ext.get(extElement.getPrevSibling());
  }
  return extElement;
};

//--------------------------------------------------------------------------------
function getNextElement(extElement, sClassname) {
  var extElement = Ext.get(extElement.getNextSibling());
  while ((extElement != null) && (!extElement.hasClass(sClassname))) {
    extElement = Ext.get(extElement.getNextSibling());
  }
  return extElement;
};

//--------------------------------------------------------------------------------
function addSelectOption(DOMSelect, sValue, sText, selected) {
  var DOMOption = document.createElement('option');
  DOMOption.value = sValue;
  DOMOption.text = sText;
  DOMOption.selected = (selected != null) ? selected : false;
  try {
    DOMSelect.add(DOMOption, null);
  } catch (e) {
    DOMSelect.add(DOMOption); // IE only version.
  }
};

//--------------------------------------------------------------------------------
function trim(s) {
  if (s == null) return "";
  return s.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};

//--------------------------------------------------------------------------------
function getStackTrace(exception) {
  var callstack = [];
  var isCallstackPopulated = false;

  if (exception.stack) { //Firefox
    callstack.push(exception.stack);
    isCallstackPopulated = true;
  }
  else if (window.opera && exception.message) { //Opera
    callstack.push(exception.message);
    isCallstackPopulated = true;
  }

  if (!isCallstackPopulated) { //IE and Safari
    var currentFunction = arguments.callee.caller;
    var limit = 0;
    while (currentFunction && limit < 50) {
      var fn = currentFunction.toString();
      var fname = fn.substring(fn.indexOf("function") + 8, fn.indexOf('')) || 'anonymous';
      callstack.push(fname);
      currentFunction = currentFunction.caller;
      limit++;
    }
  }

  return callstack.join('\n\n');
};

//--------------------------------------------------------------------------------

function getMonetLinkAction(sMonetLink) {
  try {
    var Expression = new RegExp(MONET_LINK_PATTERN, "g");
    var aResult = Expression.exec(sMonetLink);

    var type = aResult[1];
    var entityId = aResult[2].split(".")[0];
    var editMode = aResult.length == 3;
    var viewId = aResult[2].indexOf(".")!=-1?aResult[2].substring(aResult[2].lastIndexOf(".") + 1):null;

    if (type == "node") {
      var command = "shownode(" + entityId;
      // if (viewId != null) command += "," + viewId;
      if (editMode) command += ",edit.html?mode=page";
      return command + ")";
    }
    else if (type == "task") {
      if (viewId != null) return "showtaskview(" + entityId + "," + viewId + ")";
      else return "showtask(" + entityId + ")";
    }
    else if (type == "news") return "shownews()";
  } catch (ex) {
  }
  return null;
};

//--------------------------------------------------------------------------------
function htmlDecode(input) {
  if (input == null) return null;
  var e = document.createElement('div');
  e.innerHTML = input;
  if (e.childNodes.length <= 0) return input;
  return e.childNodes[0].nodeValue;
};

//--------------------------------------------------------------------------------
function getColor(iPos) {
  var aColors = ["#A52A2A", "#4169E1", "#DC143C", "#008B8B", "#696969", "#006400", "#8B008B", "#FF8C00", "#E8653A", "#6F916F", "#483D8B", "#1E90FF", "#DAA520", "#BA55D3", "#F7E120", "#8B4513"];

  while (iPos > aColors.length) iPos = iPos - aColors.length;
  if (iPos < 0) pos = 0;
  if (iPos == aColors.length) iPos = aColors.length - 1;

  return aColors[iPos];
}

//--------------------------------------------------------------------------------
// FIXS BUG on IE9
//--------------------------------------------------------------------------------
if ((typeof Range !== "undefined") && !Range.prototype.createContextualFragment) {
  Range.prototype.createContextualFragment = function (html) {
    var frag = document.createDocumentFragment(),
        div = document.createElement("div");
    frag.appendChild(div);
    div.outerHTML = html;
    return frag;
  };
}

function clone(Object) {
  var newObj = (Object instanceof Array) ? [] : {};
  for (i in Object) {
    if (i == 'clone') continue;
    if (Object[i] && typeof Object[i] == "object") {
      newObj[i] = clone(Object[i]);
    } else newObj[i] = Object[i];
  }
  return newObj;
}

function shortValue(sValue, size, leftSize, rightSize) {
  if (sValue == null) return;
  if (!sValue.length) return;
  if (size == null) size = 100;
  if (leftSize == null) leftSize = 40;
  if (rightSize == null) rightSize = 40;
  if (sValue.length <= size) return sValue;
  var iLength = sValue.length;
  var sLeftValue = sValue.substring(0, leftSize);
  var sRightValue = sValue.substring(iLength - rightSize, iLength);
  return sLeftValue + "..." + sRightValue;
}

function capitalizeSentence(content) {

  content = content.toLowerCase();
  content = content.replace(/\.\n/g, ".[-<br>-]. ");
  content = content.replace(/\.\s\n/g, ". [-<br>-]. ");

  var wordSplit = '. ';
  var wordArray = content.split(wordSplit);
  var numWords = wordArray.length;

  for (var x = 0; x < numWords; x++) {
    wordArray[x] = wordArray[x].replace(wordArray[x].charAt(0), wordArray[x].charAt(0).toUpperCase());
    if (x == 0) {
      content = wordArray[x] + ". ";
    } else if (x != numWords - 1) {
      content = content + wordArray[x] + ". ";
    } else if (x == numWords - 1) {
      content = content + wordArray[x];
    }
  }

  content = content.replace(/\[-<br>-\]\.\s/g, "\n");
  content = content.replace(/\si\s/g, " I ");

  return content;
}

function capitalizeTitle(content) {

  content = content.toLowerCase();
  content = content.replace(/\n/g, ". [-<br>-] ");

  var wordSplit = ' ';
  var wordArray = content.split(wordSplit);
  var numWords = wordArray.length;

  for (var x = 0; x < numWords; x++) {
    wordArray[x] = wordArray[x].replace(wordArray[x].charAt(0), wordArray[x].charAt(0).toUpperCase());
    if (x == 0) {
      content = wordArray[x] + " ";
    } else if (x != numWords - 1) {
      content = content + wordArray[x] + " ";
    } else if (x == numWords - 1) {
      content = content + wordArray[x];
    }
  }

  content = content.replace(/\.\s\[-<br>-\]\s/g, "\n");
  content = content.replace(/\.\s\[-<br>-\]/g, "\n");
  content = content.replace(/\si\s/g, " I ");

  return content;
}

function resolveParametrizedUrl(url, object) {
  var result = url;

  if (object == null) return result;

  var expression = new RegExp("::([^\:\:]*)::");
  var codeArray = expression.exec(result);

  while (codeArray != null) {
    var code = codeArray[1];
    var value = null;

    eval("value = object." + code);

    if (value != null) result = result.replace(codeArray[0], value);
    else result = result.replace(codeArray[0], "");

    codeArray = expression.exec(result);
  }

  return result;
};

function reload(location) {
  window.location = location + (location.indexOf("?") != -1 ? "&" : "?") + "m=" + Math.random();
};

function generateIdForKey(key) {
  var result = 0;
  for (var i = 0; i < key.length; i++)
    result += parseInt(key.charCodeAt(i));
}

function containsObjectInList(obj, list) {
  var i;
  for (i = 0; i < list.length; i++) {
    if (objectEquals(list[i], obj)) {
      return true;
    }
  }

  return false;
}

function objectEquals(obj, x) {
  var p = 0;

  for (p in obj) {
    if (typeof(x[p]) == 'undefined') {
      return false;
    }
  }

  for (p in obj) {
    if (obj[p]) {
      switch (typeof(obj[p])) {
        case 'object':
          if (!objectEquals(obj[p], x[p])) {
            return false;
          }
          break;
        case 'function':
          if (typeof(x[p]) == 'undefined' ||
              (p != 'equals' && obj[p].toString() != x[p].toString()))
            return false;
          break;
        default:
          if (obj[p] != x[p]) {
            return false;
          }
      }
    } else {
      if (x[p])
        return false;
    }
  }

  for (p in x) {
    if (typeof(obj[p]) == 'undefined') {
      return false;
    }
  }

  return true;
};

function arrayToMap(array) {
  var result = new Object();
  if (array == null) return result;
  for (var i = 0; i < array.length; i++) {
    var value = array[i];
    result[value] = value;
  }
  return result;
}

function cleanNonAsciiCharacters(data) {
	if (data == null)
		return null;

	data = data.replace(/\n/g, "#crlf#");
	data = data.replace(/\t/g, "#tab#");
	data = data.replace(/[\u0000-\u001f]/g, "");
	data = data.replace(/#crlf#/g, "\n");
	data = data.replace(/#tab#/g, "\t");

	return data;
}