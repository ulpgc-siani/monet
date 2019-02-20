function CGCommand(sLink, DOMElement) {
  this.sLink = sLink;
  this.DOMElement = DOMElement;
};

/*********************************************************************/
/*  Methods                                                          */
/*********************************************************************/
CGCommand.prototype.getLink = function () {
  return this.sLink;
};

CGCommand.prototype.setLink = function (sLink) {
  this.sLink = sLink;
};

CGCommand.prototype.getDOM = function () {
  return this.DOMElement;
};

CGCommand.prototype.setDOM = function (DOMElement) {
  this.DOMElement = DOMElement;
};