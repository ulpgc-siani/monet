::properties::
::behaviour::

@properties
var Definition = new Object();
Definition.Code = "::code::";
Definition.Type = "task";
Definition.Caption = "::label::";
Definition.Description = "::description::";
Definition.Loaded = true;

@properties$workplace
::comma|,::{Code:"::code::", Label: "::label::"}

@behaviour
Definition.Behaviour = new Object();
Definition.Behaviour.ShowTask = {Templates: {Edit: "preview.html?mode=page", View: "preview.html?mode=page"}};
Definition.Behaviour.CreateTask = {Templates: {Edit: "preview.html?mode=page", View:"preview.html?mode=page"}};
Definition.Behaviour.Events = new Object();
Definition.Behaviour.Events.Loaded = false;