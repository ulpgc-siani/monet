function CGUserInfo(Data) {
  if (!Data) return;
  this.sPhoto = Data.photo;
  this.sFullname = Data.fullname;
  this.aPreferences = SerializerData.deserialize(Data.preferences);
  this.sEmail = (Data.email != "null") ? Data.email : "";
  this.sOccupations = Data.occupations;
};

/*********************************************************************/
/*  Methods                                                          */
/*********************************************************************/
CGUserInfo.prototype.getPhoto = function () {
  return this.sPhoto;
};

CGUserInfo.prototype.setPhoto = function (sPhoto) {
  this.sPhoto = sPhoto;
};

CGUserInfo.prototype.getFullname = function () {
  return this.sFullname;
};

CGUserInfo.prototype.setFullname = function (sFullname) {
  this.sFullname = sFullname;
};

CGUserInfo.prototype.getPreferences = function () {
  return this.aPreferences;
};

CGUserInfo.prototype.setPreferences = function (aPreferences) {
  this.aPreferences = aPreferences;
};

CGUserInfo.prototype.getEmail = function () {
  return this.sEmail;
};

CGUserInfo.prototype.setEmail = function (sEmail) {
  this.sEmail = sEmail;
};

CGUserInfo.prototype.getOccupations = function () {
  return this.sOccupations;
};

CGUserInfo.prototype.setOccupations = function (sOccupations) {
  this.sOccupations = sOccupations;
};

CGUserInfo.prototype.serialize = function () {
  var sResult = "\"photo\":\"" + this.sPhoto + "\",";
  sResult += "\"fullname\":\"" + this.sFullname + "\",";
  sResult += "\"preferences\":\"" + SerializerData.serialize(this.aPreferences) + "\",";
  sResult += "\"email\":\"" + this.sEmail + "\",";
  sResult += "\"occupations\":\"" + this.sOccupations + "\"";
  return "{" + sResult + "}";
};