function CGRoleDefinition(Data) {
  this.Code = Data != null ? Data.code : "";
  this.sLabel = Data != null ? Data.label : "";
  this.enableUsers = Data != null ? Data.enableUsers : true;
  this.PartnerServiceOntologies = Data != null ? Data.PartnerServiceOntologies : null;
  this.PartnerFeederOntologies = Data != null ? Data.PartnerFeederOntologies : null;
};

CGRoleDefinition.prototype.getType = function () {
  if (this.enableUsers != null) return "user";
  if (this.PartnerServiceOntologies != null) return "service";
  if (this.PartnerFeederOntologies != null) return "feeder";
  return null;
};