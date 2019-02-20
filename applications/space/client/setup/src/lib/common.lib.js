//------------------------------------------------------------------------------
// Augmenting simple javascript objects
//-------------------------------------------------------------------------------
String.prototype.trim = function() {return this.replace(/^\s+|\s+$/g,'');};

//------------------------------------------------------------------------------
//Validation
//-------------------------------------------------------------------------------
function isUrl(url) {
  var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
  return regexp.test(url);
};

//--------------------------------------------------------------------------------
//  Inheritance
//--------------------------------------------------------------------------------
function extend(subclass, superclass) {
   function Dummy() {}
   Dummy.prototype = superclass.prototype;
   subclass.prototype = new Dummy();
   subclass.prototype.constructor = subclass;
   subclass.superclass = superclass;
   subclass.superproto = superclass.prototype;
}

//--------------------------------------------------------------------------------

function translate(sText, Data, sPrefix) {
  if (!sText) return;
  if (!sPrefix) sPrefix = EMPTY;

  if (Context.Config) {
    Expression = new RegExp(TEMPLATE_SEPARATOR + Literals.ImagesPath + TEMPLATE_SEPARATOR, "g");
    sText = sText.replace(Expression, Context.Config.ImagesPath);
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
    else  {
      Expression = new RegExp(TEMPLATE_SEPARATOR + sName + TEMPLATE_SEPARATOR, "g");
      sText = sText.replace(Expression, Aux);
    }
  }
  return sText;
}

//--------------------------------------------------------------------------------

function parseServerDate(dtDate) {
  return Date.parseDate(dtDate, SERVER_DATE_TIME_FORMAT);
}

//--------------------------------------------------------------------------------

function getFormattedDate(dtDate, Language) {

  Date.dayNames = eval("aDays." + Language);
  Date.monthNames = eval("aMonths." + Language);

  switch (Language) {
    case "es" : sFormat = "G:i:s \\e\\l l, j \\d\\e F \\d\\e Y"; break;
    default : sFormat = "G:i:s. l, F d, Y";
  }

  return dtDate.format(sFormat);
}

//--------------------------------------------------------------------------------

function addLayer(sContent, sContainer) {
  var Layer = new Insertion.Bottom($(sContainer), sContent).element.immediateDescendants().last();
  return Layer;
}

//--------------------------------------------------------------------------------

function createLayer(Id, sContent, DOMContainer) {
  var Layer = new Insertion.Bottom(DOMContainer, "<div id='" + Id + "'>" + sContent + "</div>").element.immediateDescendants().last();
  return Layer;
}

//--------------------------------------------------------------------------------

function getCreateLayerHTMLContent(Id) {
  return "<div id='" + Id + "'></div>";
}

//--------------------------------------------------------------------------------

function removeLayer(Id) {
  $(Id).remove();
}

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
}

//--------------------------------------------------------------------------------

function replaceDOMElement(DOMElement, sContent) {
  
  Result = sContent.match(/^<[^>]*id=\"([^\"]*)\"[^>]*>/);
  if (! Result) Result = sContent.match(/^<[^>]*id=\'([^\']*)\'[^>]*>/);
  if (! Result) Result = sContent.match(/^<[^>]*id=([^ ]*) [^>]*>/);
  
  if (Result) { Id = Result[1]; }
  else {
    Id = DOMElement.id;
    if(!Id) Id = Ext.id();
    sContent = setIdToElementContent(Id, sContent);
  }

  if (Ext.isIE) {
    try {
      DOMElement.outerHTML = sContent;
    } catch (e) {
      Ext.MessageBox.alert("HTML Exception", "Check your HTML template. Perhaps you have '<form>' tags. HTML don't allow including forms into forms.");
    }
  }
  else {
    if (DOMElement.replace) DOMElement = DOMElement.replace(sContent);
  }

  DOMElement = $(Id);

  return DOMElement;
}

//--------------------------------------------------------------------------------

function utf8Encode(sData) {
  return unescape(encodeURIComponent(sData));
}

//--------------------------------------------------------------------------------

function utf8Decode(sData) {
  return decodeURIComponent(escape(sData));
}

//--------------------------------------------------------------------------------

function select(InputList, Type) {
  var aResult = new Array();
  InputList.each(function(Field) {
    if (Field.dom.type != "checkbox") return;
    Field.dom.checked = (Type != null) ?  Type : !Field.dom.checked;
    Field.dom.select();
    aResult.push(Field.dom);
  });
  return aResult;
}

//--------------------------------------------------------------------------------

function selectAll(InputList) { return select(InputList, true); }

//--------------------------------------------------------------------------------

function selectNone(InputList) { return select(InputList, false); }

//--------------------------------------------------------------------------------

function selectInvert(InputList) { return select(InputList, null); }

//--------------------------------------------------------------------------------

function selectHighlighted(HighlightedList) {
  var aResult = new Array();
  HighlightedList.each(function(Field) {
    if (Field.dom.type != "checkbox") return;
    Field.dom.checked = true;
    Field.dom.select();
    aResult.push(Field.dom);
  });
  return aResult;
}

//--------------------------------------------------------------------------------

function readData(Data, extData) {
  var extDataList = extData.select(HTML_DIV);
  extDataList.each(function(extData) {
    eval("Data." + extData.dom.id + "= '" + extData.dom.innerHTML + "';");
  }, this);
}

//--------------------------------------------------------------------------------
function getFileExtension(sFilename) {
  var iPos = sFilename.lastIndexOf('.');
  if (iPos == -1) return "";
  return sFilename.substring(iPos+1);
}

//--------------------------------------------------------------------------------
function isAlphanumeric(sContent) {
	var sDummy = sContent;

	for(var iPos=0; iPos<sDummy.length; iPos++) {
	  var iChar = sDummy.charAt(iPos).charCodeAt(0);
		if ((iChar > 47 && iChar < 58) || (iChar > 64 && iChar < 91) || (iChar > 96 && iChar < 123)) {}
		else {
		  return false;
		}
  }

  return true;
}

//--------------------------------------------------------------------------------
function isEmail(sEmail) {
  var arr;

  if(sEmail.length <= 0) return false;
  
  arr = sEmail.match("^(.+)@(.+)$");
  if(arr == null) return false;
  
  if(arr[1] != null ) {
    var regexp_user=/^\"?[\w-_\.]*\"?$/;
    if(arr[1].match(regexp_user) == null) return false;
  }

  if(arr[2] != null) {
    var regexp_domain=/^[\w-\.]*\.[A-Za-z]{2,4}$/;
    if(arr[2].match(regexp_domain) == null) {
      var regexp_ip =/^\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\]$/;
      if(arr[2].match(regexp_ip) == null) return false;
    }
    return true;
  }

  return false;
}

//--------------------------------------------------------------------------------
function addInput(DOMForm, sName, sValue) {
  var DOMInput = document.createElement("input");
  DOMInput.setAttribute("type", "hidden");
  DOMInput.setAttribute("name", sName);
  DOMInput.setAttribute("value", sValue);
  DOMForm.appendChild(DOMInput);
}

//--------------------------------------------------------------------------------
function propertiesCount(object) {
  var count = 0;
  for (i in object) if (object.hasOwnProperty(i)) count++;
  return count;
}

//--------------------------------------------------------------------------------
/*
* Static counter
*/
//--------------------------------------------------------------------------------

var Counter = (function(value) {
  var initialValue = value || 0;
  var count = initialValue;
  return {
    read : function() {
	    return count = count + 1;	   
	  },
	  reset: function() {
	    count = initialValue;  
	  }
  };  
})(0);

