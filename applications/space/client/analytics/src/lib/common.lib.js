function readData(data, jdata) {
  var jdataList = jdata.find("div");
  jdataList.each(function() {
    var jData = $(this);
    eval("data." + this.id + "= '" + jData.text() + "';");
  }, this);
}

function isFunction(item) {
  return (typeof item == "function");
}

function writeServerRequest(mode, data) {
  var result = (mode) ? "r=" + Base64.encode(data) : data;
  return "?" + result;
};

function translate(text, data, prefix) {
  if (!text) return "";
  if (!prefix) prefix = "";

  if (Context.Config) {
    Expression = new RegExp("::ImagesUrl::", "g");
    text = text.replace(Expression, Context.Config.ImagesUrl);
  }

  for (var index in data) {
    var name = prefix + index;

    var Aux = data[index];
    if (typeof Aux == "object") {
      text = translate(text, Aux, name + ".");
    }
    else  {
      Expression = new RegExp("::" + name + "::", "g");
      text = text.replace(Expression, Aux);
    }
  }
  return text;
};

function serializeParameters(parameters) {
  var result = "";
  for (var index in parameters) {
    if (isFunction(parameters[index])) continue;
    result += "&" + index + "=" + parameters[index];
  }
  return result;
};

function readServerResponse(mode, data) {
  if (mode) return Base64.decode(data);
  return Encoder.htmlDecode(data);
};

function setIdToElementContent(id, content) {
  var pos = 0;
  var leftContent, rightContent;

  while ((content.charAt(pos) != " ") && (pos < content.length)) pos++;
  if (pos >= content.length) return false; 

  leftContent = content.substr(0, pos);
  rightContent = content.substr(pos);

  content = leftContent + " " + "id=" + "'" + id + "'" + rightContent;

  return content;
};

function replaceDOMElement(DOMElement, content) {
  var id = null;
  
  result = content.match(/^<[^>]*id=\"([^\"]*)\"[^>]*>/);
  if (! result) result = content.match(/^<[^>]*id=\'([^\']*)\'[^>]*>/);
  if (! result) result = content.match(/^<[^>]*id=([^ ]*) [^>]*>/);
  
  if (result) 
    id = result[1];
  else {
    id = DOMElement.id;
    if(!id) id = generateId();
    if (content != "") content = setIdToElementContent(id, content);
  }

  if ($.browser.msie) {
    try {
      DOMElement.outerHTML = content;
    } catch (e) {
      alert("HTML Exception", "Check your HTML template. Perhaps you have '<form>' tags. HTML doesn't allow including forms into forms.");
    }
  }
  else {
    try {
      var jElement = $(DOMElement);
      jElement.replaceWith(content);
    }
    catch(e) {}
  }

  DOMElement = $("#" + id).get(0);

  return DOMElement;
};

function generateId(element, prefix) {
  var generatedId;
  var counter = 1;
  do {
    generatedId = (prefix ? prefix + '-' : '') + (counter++);
  } while(document.getElementById(generatedId));

  if (element) {
    $(element).attr('id', generatedId);
  }
  return generatedId;
};

function utf8Encode(data) {
  return unescape(encodeURIComponent(data));
};

function utf8Decode(data) {
  return unescape(decodeURIComponent(data));
};

function size(elementArray) {
  var iLength = 0;
  for (var index in elementArray) { 
    if (isFunction(elementArray[index])) continue;
    iLength++; 
  }
  return iLength;
};

function replaceAll(content, search, replacement) {
  while (content.toString().indexOf(search) != -1)
    content = content.toString().replace(search,replacement);
  return content;
};

Array.prototype.size = function() {
  var iLength = 0;
  for (var index in this) { 
    if (isFunction(this[index])) continue;
    iLength++; 
  }
  return iLength;
};

function shortValue(value, maxLength) {
  if (!value.length) return;
  if (value.length <= maxLength) return value;
  var length = value.length;
  var middleLength = Math.floor(maxLength/2);
  var leftValue = value.substring(0, middleLength);
  var rightValue = value.substring(length-middleLength, length);
  return leftValue + "..." + rightValue;
};

String.prototype.toHtmlId = function() {
  var result = new String();
  result = this.replace(/[^\w]/g,"");
  return result;
};


Number.prototype.formatNumber = function(numberType, language, type) {
  
  if (numberType == "currency") c = 2;
  else if (numberType == "float") c = 3;
  else c = 0;
    
  if (language == "es") {
    d = ",";
    t = ".";
  }
  else {
    d = ".";
    t = ",";
  }
  
  var n = this, c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "," : d, t = t == undefined ? "." : t, s = n < 0 ? "-" : "", i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", j = (j = i.length) > 3 ? j % 3 : 0;
     return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};

//--------------------------------------------------------------------------------
function addSelectOption(DOMSelect, sValue, sText, selected, disabled) {
  var DOMOption = document.createElement('option');
  DOMOption.value = sValue;
  DOMOption.text = sText;
  DOMOption.selected = (selected!=null)?selected:false;
  DOMOption.disabled = (disabled!=null)?disabled:false;
  try {
    DOMSelect.add(DOMOption,null);
  } catch (e) {
    DOMSelect.add(DOMOption); // IE only version.
  }
};

function trim(content) {
  return content.replace(/^\s+|\s+$/g, '');
};

function isNumber(value) {
  var reg = new RegExp(/^\d+$/);
  return reg.test(toNumber(value));
};

function toNumber(value) {
  var valueToTest = value.replace(".", "");
  return valueToTest.replace(",", "");
};

function reload(location) {
  window.location = location + (location.indexOf("?")!=-1?"&":"?") + "m=" + Math.random();
};

function stringify(content) {
	return content.replace(/\"/g, "\\\"");
}