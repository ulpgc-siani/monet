CGPage = function () {
  this.sName = EMPTY;
  this.sContent = EMPTY;
};

/*********************************************************************/
/*  Properties                                                       */
/*********************************************************************/
CGPage.prototype.getName = function () {
  return this.sName;
};

CGPage.prototype.setName = function (sName) {
  this.sName = sName;
};

CGPage.prototype.getContent = function () {
  return this.sContent;
};

CGPage.prototype.setContent = function (sContent) {
  this.sContent = sContent;
};

CGPage.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);
  if (jsonData == null) return;
  this.sName = jsonData.name != null ? jsonData.name : "";
  this.sContent = jsonData.content != null ? jsonData.content : "";
};