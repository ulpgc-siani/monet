CGTask = function () {
  this.Id = -1;
  this.IdTarget = "";
  this.IdInput = "";
  this.IdOutput = "";
  this.Code = null;
  this.State = null;
  this.sLabel = EMPTY;
  this.sDescription = EMPTY;
  this.sSender = EMPTY;
  this.dtCreated = null;
  this.dtExpired = null;
  this.dtFinished = null;
  this.bRead = null;
  this.bAborted = null;
  this.iOrdering = null;
  this.bIsInitializer = false;
  this.bIsPending = false;
  this.bIsWaiting = false;
  this.sContent = EMPTY;
  this.bDirty = false;
};

/*********************************************************************/
/*  Properties                                                       */
/*********************************************************************/
CGTask.prototype.getId = function () {
  return this.Id;
};

CGTask.prototype.setId = function (Id) {
  this.Id = Id;
};

CGTask.prototype.getLabel = function () {
  return this.sLabel;
};

CGTask.prototype.setLabel = function (sLabel) {
  this.sLabel = sLabel;
};

CGTask.prototype.getDescription = function () {
  return this.sDescription;
};

CGTask.prototype.setDescription = function (sDescription) {
  this.sDescription = sDescription;
};

CGTask.prototype.getSender = function () {
  return this.sSender;
};

CGTask.prototype.setSender = function (sSender) {
  this.sSender = sSender;
};

CGTask.prototype.getContent = function () {
  return this.sContent;
};

CGTask.prototype.setContent = function (sContent) {
  this.sContent = sContent;
};

CGTask.prototype.isInitializer = function () {
  return this.bIsInitializer;
};

CGTask.prototype.isPending = function () {
  return this.bIsPending;
};

CGTask.prototype.isWaiting = function () {
  return this.bIsWaiting;
};

CGTask.prototype.toArray = function () {
  return {
    Id: this.Id,
    IdTarget: this.IdTarget,
    IdInput: this.IdInput,
    IdOutput: this.IdOutput,
    Code: this.Code,
    State: this.State,
    sLabel: this.sLabel,
    sDescription: this.sDescription,
    sSender: this.sSender,
    dtCreated: getFormattedDateTime(parseServerDate(this.dtCreated), Context.Config.Language),
    dtExpired: this.dtExpired ? getFormattedDateTime(parseServerDate(this.dtExpired), Context.Config.Language) : null,
    dtFinished: this.dtExpired ? getFormattedDateTime(parseServerDate(this.dtFinished), Context.Config.Language) : null,
    bRead: this.bRead,
    bAborted: this.bAborted,
    iOrdering: this.iOrdering
  };
};

CGTask.prototype.unserializeFromJSON = function (ItemStructure) {
  this.Id = ItemStructure.id;
  this.IdTarget = ItemStructure.idTarget;
  this.IdInput = ItemStructure.idInput;
  this.IdOutput = ItemStructure.idOutput;
  this.Code = ItemStructure.code;
  this.State = ItemStructure.state;
  this.sLabel = ItemStructure.label;
  this.sDescription = ItemStructure.description;
  this.sSender = ItemStructure.sender;
  this.dtCreated = ItemStructure.created;
  this.dtExpired = (ItemStructure.expired && ItemStructure.expired != "null") ? ItemStructure.expired : null;
  this.dtFinished = (ItemStructure.finished && ItemStructure.finished != "null") ? ItemStructure.finished : null;
  this.bRead = ItemStructure.read;
  this.bAborted = ItemStructure.aborted;
  this.iOrdering = ItemStructure.ordering;
  this.bIsInitializer = ItemStructure.isInitializer;
  this.bIsPending = ItemStructure.isPending;
  this.bIsWaiting = ItemStructure.isWaiting;
  this.sContent = ItemStructure.content;
};

CGTask.prototype.clean = function () {
  this.bDirty = false;
};

CGTask.prototype.isDirty = function () {
  return this.bDirty;
};

CGTask.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);
  this.unserializeFromJSON(jsonData);
};