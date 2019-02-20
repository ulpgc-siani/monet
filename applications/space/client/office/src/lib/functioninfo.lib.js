function CGFunctionInfo(sFunction) {

  this.sName = null;
  this.aParameters = new Array();

  if (sFunction != null) this.processFunction(sFunction);
};

//---------------------------------------------------------------------
CGFunctionInfo.prototype.processFunction = function (sFunction) {

  sFunction = sFunction.replace(/%20/g, EMPTY);
  sFunction = sFunction.replace(/%28/g, LEFT_BRACKET);
  sFunction = sFunction.replace(/%29/g, RIGHT_BRACKET);

  while ((iPos = sFunction.indexOf(SLASH)) != -1) {
    var iBracketPos = sFunction.indexOf(LEFT_BRACKET);
    if (iPos < iBracketPos) sFunction = sFunction.substring(iPos + 1, sFunction.length);
    else sFunction = sFunction.substring(0, iPos, sFunction.length) + "#@@@@#" + sFunction.substring(iPos + 1, sFunction.length);
  }
  sFunction = sFunction.replace(/#@@@@#/g, "/");

  if ((iPos = sFunction.indexOf(LEFT_BRACKET)) == -1) {
    this.sName = sFunction;
    return true;
  }

  if (sFunction.substring(sFunction.length - 1, sFunction.length) != RIGHT_BRACKET) return false;

  this.sName = sFunction.substring(0, iPos);

  sFunction = sFunction.substring(iPos + 1, sFunction.length - 1);
  this.aParameters = sFunction.split(',');
};

//---------------------------------------------------------------------
CGFunctionInfo.prototype.getName = function () {
  return this.sName;
};

//---------------------------------------------------------------------
CGFunctionInfo.prototype.getParameters = function () {
  return this.aParameters;
};