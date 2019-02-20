/*********************************************************************
 Request
 +session
 +onLoad       %event%
 +onFail       %event%
 +Load()

 -ReadyStateChange()
 -XMLHttpRequest      %XMLHttpRequest%

 *********************************************************************/

if (window.ActiveXObject && !window.XMLHttpRequest) {
  window.XMLHttpRequest = function () {
    var msxmls = new Array(
        'Msxml2.XMLHttpRequest.5.0',
        'Msxml2.XMLHttpRequest.4.0',
        'Msxml2.XMLHttpRequest.3.0',
        'Msxml2.XMLHttpRequest',
        'Microsoft.XMLHttpRequest',
        'Microsoft.XMLHTTP');
    for (var i = 0; i < msxmls.length; i++)
      try {
        return new ActiveXObject(msxmls[i]);
      } catch (e) {
      }
    return null;
  };
}
;

if (!window.ActiveXObject && window.XMLHttpRequest) {
  window.ActiveXObject = function (type) {
    switch (type.toLowerCase()) {
      case 'microsoft.XMLHttpRequest':
      case 'msxml2.XMLHttpRequest':
      case 'msxml2.XMLHttpRequest.3.0':
      case 'msxml2.XMLHttpRequest.4.0':
      case 'msxml2.XMLHttpRequest.5.0':
        return new XMLHttpRequest();
    }
    return null;
  };
}
;

//--------------------------------------------------------------------------
function Request(sURL) {
  this.sURL = sURL;
  this.sQuery = "";
  this.XMLHttpRequest = new XMLHttpRequest();
};

Request.prototype.onLoad = null;
Request.prototype.onFail = null;

Request.prototype.Add = function (key, value) {
  if (value) this.sQuery += AMP + key + EQUAL + value;
};

Request.prototype.AddForm = function (eForm) {
  var sData = "";
  for (var iPos = 0; iPos < eForm.elements.length; iPos++) {
    var eElement = eForm.elements[iPos];
    switch (eElement.type) {
      case 'text':
      case 'select-one':
      case 'hidden':
      case 'password':
      case 'textarea':
        sData += eElement.name + '=' + escape(utf8Encode(eElement.value)) + '&';
        break;
    }
  }
  this.sQuery += '&' + sData;
};

Request.prototype.Post = function (Data) {
  var sResult;

  this.XMLHttpRequest.open("post", this.sURL + this.sQuery, false);
  this.XMLHttpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
  this.XMLHttpRequest.send(Data);

  if (( this.XMLHttpRequest.status == 200) || ( this.XMLHttpRequest.status == 0 )) {
    sResult = this.XMLHttpRequest.responseText;
    if (this.onLoad) this.onLoad(this.sURL, sResult);
  }
  else {
    sResult = null;
    if (this.onFail) this.onFail(this.sURL);
  }

  this.sQuery = "";

  return sResult;
};

Request.prototype.Upload = function (eForm) {
  eForm.method = "post";
  eForm.enctype = "multipart/form-data";
  eForm.action = this.sQuery;
  eForm.submit();
};