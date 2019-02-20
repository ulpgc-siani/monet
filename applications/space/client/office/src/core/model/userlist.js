CGUserList = function () {
  this.aUsers = new Array();
  this.bDirty = false;
  this.iCount = 0;
};

CGUserList.prototype.newUsers = function (aJsonItems) {

  for (var i = 0; i < aJsonItems.length; i++) {
    Item = aJsonItems[i];
    User = new CGUser();
    User.unserializeFromJSON(Item);
    this.aUsers[User.getId()] = User;
  }

  return this.aUsers;
};

CGUserList.prototype.addUser = function (User) {
  this.aUsers[User.getId()] = User;
};

CGUserList.prototype.deleteUser = function (Id) {
  if (this.aUsers[Id]) delete this.aUsers[Id];
};

CGUserList.prototype.getUser = function (Id) {
  return this.aUsers[Id];
};

CGUserList.prototype.getUsers = function () {
  return this.aUsers;
};

CGUserList.prototype.getUsersIds = function () {
  var aResult = new Array();
  for (IdUser in this.aUsers) {
    if (isFunction(this.aUsers[IdUser])) continue;
    aResult.push(IdUser);
  }
  return aResult;
};

CGUserList.prototype.setUsers = function (aUsers) {
  this.aUsers = aUsers;
};

CGUserList.prototype.clean = function () {
  this.bDirty = false;
};

CGUserList.prototype.isDirty = function () {
  return this.bDirty;
};

CGUserList.prototype.getCount = function () {
  return this.iCount;
};

CGUserList.prototype.setCount = function (iCount) {
  this.iCount = iCount;
};

CGUserList.prototype.unserializeFromJSON = function (ItemStructure) {
  this.aUsers = new Array();
  this.newUsers(ItemStructure.items);
  this.iCount = ItemStructure.count;
};

CGUserList.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);
  this.unserializeFromJSON(jsonData);
};

CGUserList.prototype.serialize = function () {
  var sResult = "";

  for (var iPos in this.aUsers) {
    if (isFunction(this.aUsers[iPos])) continue;
    sResult += this.aUsers[iPos].serialize() + ",";
  }
  if (this.aUsers.size() > 0) sResult = sResult.substring(0, sResult.length - 1);

  return "[" + sResult + "]";
};