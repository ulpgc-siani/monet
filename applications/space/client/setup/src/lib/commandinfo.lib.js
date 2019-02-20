function CGCommandInfo(sLink) {

  this.sOperation = null;
  this.aParameters = new Array();

  if (sLink != null) this.processLink(sLink);
}

//---------------------------------------------------------------------
CGCommandInfo.prototype.processLink = function(sLink) {

  sLink = sLink.replace(/%20/g, EMPTY);
  sLink = sLink.replace(/%28/g, LEFT_BRACKET);
  sLink = sLink.replace(/%29/g, RIGHT_BRACKET);

  while ((iPos=sLink.indexOf(SLASH)) != -1) {
    sLink = sLink.substring(iPos+1, sLink.length);
  }

  if (sLink.indexOf(LEFT_BRACKET) == -1) {
    this.sOperation = sLink;
    return true;
  }

  var reg = new RegExp(/(.*)\(([^\)]*)/g);
  aResult = reg.exec(sLink);

  if (aResult == null) { return false; }

  this.sOperation  = aResult[1];
  this.aParameters = (aResult[2] != EMPTY)?aResult[2].split(','):new Array();

  return true;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.getOperation = function() { return this.sOperation; };

//---------------------------------------------------------------------
CGCommandInfo.prototype.setOperation = function(sOperation) { 
  this.sOperation = sOperation; 
  return true;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.getParameters = function() { return this.aParameters; };

//---------------------------------------------------------------------
CGCommandInfo.prototype.addParameter = function(sParameter) { 
  this.aParameters.push(sParameter); 
  return true;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.setParameters = function(aParameters) { 
  this.aParameters = aParameters; 
  return true;
};

//---------------------------------------------------------------------
CGCommandInfo.prototype.getLink = function(sOperation, aParameters) { 
  var sResult = sOperation + LEFT_BRACKET;
  var sParameters = EMPTY;

  aParameters.each(function(sParameter) {
    sParameters += sParameter + COMMA;
  }, this);

  if (sParameters != EMPTY) sResult += sParameters.substring(0, sParameters.length-1);

  return sResult + RIGHT_BRACKET;
};