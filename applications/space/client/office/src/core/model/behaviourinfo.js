function CGBehaviourInfo(sLink) {

  this.sName = null;
  this.aParameters = null;

  this.processLink(sLink);
};

CGBehaviourInfo.prototype.processLink = function (sLink) {

  while ((iPos = sLink.indexOf(SLASH)) != -1) {
    sLink = sLink.substring(iPos + 1, sLink.length);
  }

  var reg = new RegExp(/(.*)\(([^\)]*)/g);
  aResult = reg.exec(sLink);

  if (aResult == null) {
    return false;
  }

  this.sName = aResult[1];
  this.aParameters = (aResult[2] != EMPTY) ? aResult[2].split(',') : new Array();
};

CGBehaviourInfo.prototype.getName = function () {
  return this.sName;
};

CGBehaviourInfo.prototype.getParameters = function () {
  return this.aParameters;
};