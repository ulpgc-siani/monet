function CGRole(Data) {
  this.Id = Data != null ? Data.id : null;
  this.GroupedId = Data != null ? Data.groupedId : null;
  this.Type = Data != null ? Data.type : "user";
  this.User = new CGUser(Data != null ? Data.user : null);
  this.PartnerId = Data != null ? Data.partnerId : null;
  this.PartnerServiceName = Data != null ? Data.partnerServiceName : null;
  this.Code = Data != null ? Data.code : "";
  this.sLabel = Data != null ? Data.label : "";
  if (Data != null && Data.beginDate) this.dtBeginDate = parseServerDate(Data.beginDate);
  else {
    var date = new Date();
    date.setMonth(date.getMonth() + 1);
    this.dtBeginDate = date;
  }
  this.dtExpireDate = (Data != null && Data.expireDate) ? parseServerDate(Data.expireDate) : null;
  this.expires = Data != null ? Data.expires : null;
  this.expired = Data != null ? Data.expired : null;
  this.began = Data != null ? Data.began : null;
};

CGRole.prototype.clone = function (Role) {
  var date = new Date();
  date.setMonth(date.getMonth() + 1);

  this.Id = Role.Id;
  this.GroupedId = Role.GroupedId;
  this.Type = Role.Type;
  this.User = Role.User;
  this.PartnerId = Role.PartnerId;
  this.PartnerServiceName = Role.PartnerServiceName;
  this.Code = Role.Code;
  this.sLabel = Role.sLabel;
  this.dtBeginDate = date;
  this.dtExpireDate = null;
  this.expires = false;
  this.expired = false;
  this.began = false;
};