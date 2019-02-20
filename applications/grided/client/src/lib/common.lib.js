//------------------------------------------------------------------------------
// Augmenting simple javascript objects
//-------------------------------------------------------------------------------
String.prototype.trim = function() {return this.replace(/^\s+|\s+$/g,'');};

//------------------------------------------------------------------------------
//Validation
//-------------------------------------------------------------------------------
function isUrl(url) {
  var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
  return regexp.test(url);
};

//------------------------------------------------------------------------------
//Forms
//-------------------------------------------------------------------------------

function addInput(DOMForm, sName, sValue) {
  var DOMInput = document.createElement("input");
  DOMInput.setAttribute("type", "hidden");
  DOMInput.setAttribute("name", sName);
  DOMInput.setAttribute("value", sValue);
  DOMForm.appendChild(DOMInput);
};

//------------------------------------------------------------------------------
// URLEncoder
//-------------------------------------------------------------------------------

var URLEncoder = {
		 
  encode : function (string) {
	return escape(this._utf8_encode(string));
  },
	 
  decode : function (string) {
	return this._utf8_decode(unescape(string));
  },
	 
  _utf8_encode : function (string) {
	string = string.replace(/\r\n/g,"\n");
	var utftext = "";
	 
	for (var n = 0; n < string.length; n++) {
  	  var c = string.charCodeAt(n);
  	  if (c < 128) {
	    utftext += String.fromCharCode(c);
	  }
	  else if((c > 127) && (c < 2048)) {
        utftext += String.fromCharCode((c >> 6) | 192);
	    utftext += String.fromCharCode((c & 63) | 128);
	  }
	  else {
	    utftext += String.fromCharCode((c >> 12) | 224);
	    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
		utftext += String.fromCharCode((c & 63) | 128);
	  }	 
    }
	 
    return utftext;
  },
	 
  _utf8_decode : function (utftext) {
	var string = "";
	var i = 0;
	var c = c1 = c2 = 0;
	 
	while ( i < utftext.length ) {
  	  c = utftext.charCodeAt(i);
	 
	  if (c < 128) {
	    string += String.fromCharCode(c);
		i++;
	  }
	  else if((c > 191) && (c < 224)) {
	    c2 = utftext.charCodeAt(i+1);
		string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
		i += 2;
	  }
	  else {
	    c2 = utftext.charCodeAt(i+1);
		c3 = utftext.charCodeAt(i+2);
		string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
		i += 3;
	  }
	}
	
	return string;
  }	 
};

//------------------------------------------------------------------------------
// Object functions
//-------------------------------------------------------------------------------

function isFunction(item) {
  return (typeof item == "function");
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

function utf8Encode(sData) {
  return unescape(encodeURIComponent(sData));
}

//--------------------------------------------------------------------------------

function utf8Decode(sData) {
  return decodeURIComponent(escape(sData));
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