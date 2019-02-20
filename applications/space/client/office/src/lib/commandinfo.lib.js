function CGCommandInfo(sLink) {

  this.sOperation = null;
  this.aParameters = new Array();

  if (sLink != null) this.processLink(sLink);
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.processLink = function (sLink) {

  sLink = sLink.replace(/%20/g, EMPTY);
  sLink = sLink.replace(/%28/g, LEFT_BRACKET);
  sLink = sLink.replace(/%29/g, RIGHT_BRACKET);

  var iLeftBracket = sLink.indexOf(LEFT_BRACKET);
  var iRightBracket = sLink.indexOf(RIGHT_BRACKET);

  if (iLeftBracket == -1) {
    this.sOperation = sLink;
    return true;
  }

  var sParameters = sLink.substring(iLeftBracket + 1, iRightBracket);
  sParameters = sParameters.replace(/\//g, "@bar45@");
  sLink = sLink.substring(0, iLeftBracket + 1) + sParameters + sLink.substring(iRightBracket);

  while ((iPos = sLink.indexOf(SLASH)) != -1) {
    sLink = sLink.substring(iPos + 1, sLink.length);
  }

  if ((iPos = sLink.indexOf(LEFT_BRACKET)) == -1) {
    this.sOperation = sLink;
    return true;
  }

  if (sLink.substring(sLink.length - 1, sLink.length) != RIGHT_BRACKET) return false;

  this.sOperation = sLink.substring(0, iPos);

  sLink = sLink.substring(iPos + 1, sLink.length - 1);
  sLink = sLink.replace(/@bar45@/g, "/");
  var aResult = sLink.split(',');
  this.aParameters = new Array();
  for (var iPos = 0; iPos < aResult.length; iPos++) {
    this.aParameters.push(unescape(aResult[iPos]));
  }
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.getOperation = function () {
  return this.sOperation;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.setOperation = function (sOperation) {
  this.sOperation = sOperation;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.getParameters = function () {
  return this.aParameters;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.addParameter = function (sParameter) {
  this.aParameters.push(sParameter);
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.setParameters = function (aParameters) {
  this.aParameters = aParameters;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.getLink = function (sOperation, aParameters) {
  var sResult = sOperation + LEFT_BRACKET;
  var sParameters = EMPTY;

  aParameters.each(function (sParameter) {
    sParameters += sParameter + COMMA;
  }, this);

  if (sParameters != EMPTY) sResult += sParameters.substring(0, sParameters.length - 1);

  return sResult + RIGHT_BRACKET;
};