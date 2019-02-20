::properties::
::views::
::form::
::collection::
::behaviour::

@properties
var Definition = new Object();
Definition.Code = "::code::";
Definition.Type = "::type::";
Definition.Caption = "::label::";
Definition.Description = "::description::";
Definition.IsComponent = false;
Definition.Loaded = true;

Definition.isEnvironment = function() {
  var environment = "::environment::";
  return environment == "environment";
};

Definition.isForm = function() {
  return Definition.Type == "form";
};

Definition.isDesktop = function() {
  return Definition.Type == "desktop";
};

Definition.isContainer = function() {
  return Definition.Type == "container";
};

Definition.isCollection = function() {
  return Definition.Type == "collection";
};

Definition.isCatalog = function() {
  return Definition.Type == "catalog";
};

Definition.isDocument = function() {
  return Definition.Type == "document";
};

@views
Definition.Views = new Array();
Definition.Views["browse"] = new Array();
Definition.Views["browse"]["edition"] = new Array();
Definition.Views["browse"]["edition"]["default"] = {Name: "preview.html?mode=page", Caption:"Normal", Hint:"Show the element with default view"};
Definition.Views["browse"]["preview"] = new Array();
Definition.Views["browse"]["preview"]["default"] = {Name: "preview.html?mode=page", Caption:"Normal", Hint:"Show the element with default view"};

@form
Definition.FieldsNames = new Array();
::fieldsNames::

@form.field
Definition.FieldsNames["::name::"] = "::code::";

@collection
Definition.Collection = new Object();
Definition.Collection.Items = [::nodes::];

@collection$node
::comma|,::"::code::"

@behaviour
Definition.Behaviour = new Object();
Definition.Behaviour.Export = {Sorting: "producedate"};
Definition.Behaviour.Import = {};
Definition.Behaviour.Search = {Templates: {Edit: "search.html?mode=page", View: "search.html?mode=page"}};
Definition.Behaviour.AddNode = {Templates: {Edit: "edit.html?mode=page", View: "preview.html?mode=page"}};
Definition.Behaviour.ShowNode = {Templates: {Edit: "edit.html?mode=page", View: "preview.html?mode=page"}};
Definition.Behaviour.Events = new Object();
Definition.Behaviour.Events.Loaded = false;
