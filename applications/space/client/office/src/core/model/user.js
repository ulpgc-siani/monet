function CGUser(Data) {
  this.Info = new CGUserInfo();
  if (Data) this.unserializeFromJSON(Data);
};

/*********************************************************************/
/*  Methods                                                          */
/*********************************************************************/
CGUser.prototype.getId = function () {
  return this.Id;
};

CGUser.prototype.setId = function (Id) {
  this.Id = Id;
};

CGUser.prototype.getName = function () {
  return this.sName;
};

CGUser.prototype.setName = function (sName) {
  this.sName = sName;
};

CGUser.prototype.getInitializerTask = function () {
  return this.InitializerTask;
};

CGUser.prototype.getRootNode = function () {
  return this.RootNode;
};

CGUser.prototype.getRootDashboard = function () {
  return this.RootDashboard;
};

CGUser.prototype.getInfo = function () {
  return this.Info;
};

CGUser.prototype.setInfo = function (Info) {
  this.Info = Info;
};

CGUser.prototype.toArray = function () {
  return {
    Id: this.Id,
    sName: this.sName,
    Language: this.Language,
    sFullname: this.Info.sFullname,
    sEmail: this.Info.sEmail,
    sPhoto: this.Info.sPhoto,
    sOccupations: this.Info.sOccupations
  };
};

CGUser.prototype.unserializeFromJSON = function (ItemStructure) {
  this.Id = ItemStructure.id;
  this.sName = ItemStructure.name;
  this.Language = ItemStructure.language;
  this.Info = new CGUserInfo(ItemStructure.info);
};

CGUser.prototype.serialize = function () {
  var sResult = "\"id\":\"" + this.Id + "\",";
  sResult += "\"language\":\"" + this.Language + "\",";
  sResult += "\"info\":" + this.Info.serialize();
  return "{" + sResult + "}";
};