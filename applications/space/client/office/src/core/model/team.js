CGTeam = function () {
  this.Id = "team";
  this.sContent = EMPTY;
};

/*********************************************************************/
/*  Properties                                                       */
/*********************************************************************/
CGTeam.prototype.getId = function () {
  return this.Id;
};

CGTeam.prototype.setId = function (Id) {
  this.Id = Id;
};

CGTeam.prototype.getContent = function () {
  return this.sContent;
};

CGTeam.prototype.setContent = function (sContent) {
  this.sContent = sContent;
};

CGTeam.prototype.unserializeFromJSON = function (ItemStructure) {
  this.Id = ItemStructure.id;
  this.sContent = ItemStructure.content;
};

CGTeam.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);
  this.unserializeFromJSON(jsonData);
};