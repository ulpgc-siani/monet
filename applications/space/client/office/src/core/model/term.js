function CGTerm(Data) {
  if (Data != null) {
    this.Id = Data.id;
    this.IdSource = Data.idSource;
    this.CodeSource = Data.codeSource;
    this.TypeSource = Data.typeSource;
    this.CodeParent = Data.parent;
    this.Code = Data.code;
    this.Type = Data.type;
    this.Enable = Data.enable;
    this.IsNew = Data.isNew;
    this.sLabel = Data.label;
    this.aTags = SerializerData.deserializeSet(Data.tags);
  }
};

CGTerm.TERM = "0";
CGTerm.SUPER_TERM = "1";
CGTerm.CATEGORY = "2";

CGTerm.createFromListItem = function (ListItem) {
  var result = new CGTerm();

  if (ListItem == null) return result;

  result.Id = ListItem.id;
  result.IdSource = ListItem.idSource;
  result.CodeSource = ListItem.codeSource;
  result.TypeSource = ListItem.typeSource;
  result.CodeParent = ListItem.parent;
  result.Code = ListItem.code;
  result.Type = ListItem.type;
  result.Enable = ListItem.enable;
  result.IsNew = ListItem.isNew;
  result.sLabel = ListItem.label;
  result.aTags = SerializerData.deserializeSet(ListItem.tags);

  return result;
};