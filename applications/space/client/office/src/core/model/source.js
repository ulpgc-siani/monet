function CGSource(Data) {
  if (Data != null) {
    this.Id = Data.id;
    this.Code = Data.code;
    this.Type = Data.type;
	this.Editable = Data.editable;
    this.sLabel = Data.label;
  }
};

CGSource.Type = {
  Glossary: "Glossary",
  Thesaurus: "Thesaurus"
};

CGSource.createFromListItem = function (ListItem) {
  var result = new CGSource();

  if (ListItem == null) return result;

  result.Id = ListItem.idSource;
  result.Code = ListItem.codeSource;
  result.Type = ListItem.typeSource;
  result.Editable = ListItem.isEditableSource;
  result.sLabel = ListItem.label;

  return result;
};