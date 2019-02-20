var AppTemplate=new Array;
var Lang=new Array;
var ToolbarDefinition=new Array;
var MenuDefinition=new Array;
var Path=new Array;

Lang.Exceptions = {
  Title: "An error has been produced",

  Default: "Unknown error",

  LoadOperation: "The application could not load",

  Request: {
    Title: "An error has ocurred: report the error",
    Description: "<b>An error has been ocurred trying to process the request.</b><br/>Contact with Technical Service and report this error:<br/><br/>"
  },

  Internal: {
    Title: "",
    Description: "<b>An error has ocurred trying to process the request.</b><br/>Contact with Technical Service and report this error:<br/><br/>"
  },

  SessionExpired: "For safety reasons, the session has a limit of time. This limit of time has expired. Start the session again.",
  BusinessUnitStopped: "The application has been stopped. contact with administrator for more information.",
  InitApplication: "An error has ocurred trying to start your session. Please, contact with the system administrator.",
  ConnectionFailure: "It would seem that the Internet connection has been lost. Trying again in 5 seconds..."

};

Lang.Process = {
  AddNodeBlank: {
    Waiting: "Adding #{caption}"
  },

  AddPrototype: {
    Waiting: "Adding #{caption}"
  },

  GenerateReport: {
    Waiting: "Adding #{caption}"
  },

  AddNodeFromFile: {
    Waiting: "Adding from file"
  },

  AddNodeFromClipboard: {
    Waiting: "Adding from clipboard"
  },

  AddFieldNodeLink: {
    Failure: "It could not add #{caption}. May be you do not have permissions. Contact with the system administrator.",
    Waiting: "Adding #{caption}. Please, wait.",
    Done: "The element has been added correctly."
  },

  AddFieldNode: {
    Failure: "It could not add #{caption}. May be you do not have permissions. Contact with the system administrator."
  },

  DoTaskAttachNode: {
    Failure: "It could not add the element/s",
    Done: "The element/s has beend added successfully"
  },

  DoTaskShare: {
    Failure: "It could not add the element",
    Done: "The element has beend added successfully"
  },

  DoTaskRevision: {
    Failure: "It could not modify the element/s",
    Done: "The element/s has been modified successfully"
  },

  LoadDefaultValue: {
    Failure: "It could not obtain the default value"
  },

  AddDefaultValue: {
    Failure: "It could not add the value as default.",
    Done: "The value has been set as default value."
  }

};

Lang.Action = {

  ShowNode: {
    Failure: "It could not obtain the element. May be you do not have permissions. Contact with the system administrator."
  },

  EditNode: {
    Failure: "It could not edit the element. May be you do not have permissions. Contact with the system administrator.",
    Done: "The element has saved correctly"
  },

  EditNodeDescriptors: {
    Failure: "It could not edit the element descriptors. May be you do not have pemissions. Contact with the system administrator.",
    Done: " The descriptors have been saved correctly"
  },

  SaveNode: {
    Failure: "The element could not save. May be you do not have pemissions. Contact with the system administrator.",
    Done: "The element was saved correctly"
  },

  SaveEmbeddedNode: {
    Failure: "The element could not save. May be you do not have pemissions. Contact with the system administrator.",
    Done: "The element was saved correctly"
  },

  SaveNodeDescriptors: {
    Failure: "The element descriptor could not save. May be you do not have pemissions. Contact with the system administrator.",
    Done: "The element descritor was saved correctly"
  },

  AddNode: {
    Done: "The elements has been created correctly",
    Waiting: "Creating the element...",
    Failure: "#response#"
  },

  PrintNode : {
      Waiting: "Generating document...",
      TimeConsumption: {
          Title : "Time warning",
          Description : "It have been detected lots of items to be downloaded with condition defined. Time consumption will be required to do this task. Check reducing temporal range you define. ¿Are you sure?"
      }
  },

  AddPrototype: {
    Done: "The elements has been created correctly",
    Waiting: "Creating the element...",
    Failure: "#response#"
  },

  GenerateReport: {
    Done: "The report has been generated correctly",
    Waiting: "Generating the report...",
    Failure: "It could not generate the report"
  },

  CopyNode: {
    Failure: "It could not add the elementMay be you do not have permissions. Contact with the system administrator.",
    Done: "The element has been added to your curriculum"
  },

  CopyNodes: {
    Failure: "It could not add the elements. May be you do not have permissions. Contact with the system administrator.",
    Done: "The selected elements have been added to your curriculum"
  },

  ShareNode: {
    Failure: "It could not send the element. May be you do not have permissions. Contact with the system administrator.",
    Done: "The element has been shared"
  },

  DeleteNode: {
    Failure: "It could not delete the element.May be you do not have permissions. Contact with the system administrator.",
    Done: "The element has been sent to the trash"
  },

  DeleteNodes: {
    Failure: "It could not delete the elements.May be you do not have permissions. Contact with the system administrator.",
    Done: "The elements have been sent to the trash"
  },

  DiscardNode: {
    Failure: "It could not discard the element. May be you do not have permissions. Contact with the system administrator.",
    Done: "The element has been discarded correctly"
  },

  ToggleHighlightNode: {
    Failure: "It could not highlight the element. May be you do not have permissions. Contact with the system administrator."
  },

  ImportNode: {
    Failure: "It could not add the elements from an external source.#response#",
    Waiting: "Adding elements from an external source...",
    Done: "The elements have been added correctly"
  },

  ExportNode: {
    Failure: "It could not export. May be you do not have permissions. Contact with the system administrator.",
    Waiting: "Generating. Please, wait...",
    ParametersWrong: "It could not indicate the format of the exportation"
  },

  DownloadNode: {
    Failure: "It could not generate. May be you do not have permissions. Contact with the system administrator.",
    Waiting: "Generating. Please, wait...",
    ParametersWrong: "Wrong parameters"
  },

  SearchNodes: {
    Failure: "It could not do the search. Conect with the system administrator."
  },

  ExecuteNodeCommand: {
    Done: "Operation executed successfully",
    Failure: "It could not execute the operation",
    Waiting: "Executing the operation.Please, wait..."
  },

  ShowTask: {
    Failure: "It could not show the task. Conect with the system administrator."
  },

  ShowTaskNode: {
    Failure: "It could not show the task element"
  },

  DoTask: {
    Failure: "It could not do the task. Contact with the system administrator."
  },

  CreateTask: {
    Failure: "It could not create the task. Contact with the system administrator.",
    Done: "The task has been created correctly."
  },

  AbortTask: {
    Failure: "It could not abort the task.",
    Done: "The task has been aborted correctly."
  },

  AlertTask: {
    Failure: "It could not alert about the task.",
    Done: "It has alerted about the task correctly."
  },

  AlertNode: {
    Failure: "It could not alert about this entity.",
    Done: "It has alerted about the entity correctly."
  },

  SelectTaskDelegationRole: {
    Failure: "It could not select order provider.",
    Done: "It has been selected order provider correctly."
  },

  SetupTaskDelegation: {
    Failure: "It could not configure task order.",
    Done: "It has been configured order correctly."
  },

  SelectTaskSendJobRole: {
    Failure: "It could not select user.",
    Done: "It has been selected user correctly."
  },

  SetupTaskSendJob: {
    Failure: "It could not configure task order.",
    Done: "It has been configured order correctly."
  },

  SetupTaskEnroll: {
    Failure: "It could not solve because of a fail with the element.",
    Done: "It has progressed the task correctly."
  },

  SetupTaskWait: {
    Failure: "It could not solve because of a fail with the element.",
    Done: "It has progressed the task correctly."
  },

  SolveTaskLine: {
    Failure: "It could not progress with task correctly.",
    Done: "It has progressed correctly."
  },

  SolveTaskEdition: {
    Failure: "It could not solve because of a fail with the element.",
    Done: "It has progressed the task correctly."
  },

  EmptyTrash: {
    Done: "The trash has been emptied correctly"
  },

  RecoverNodeFromTrash: {
    Done: "The element has been recovered"
  },

  RecoverNodesFromTrash: {
    Done: "The elements have been recovered",
    NoSelectedReferences: "It could not selected any element"
  },

  SetTaskTitle: {
    Failure: "It could not change the task title."
  },

  ShowTaskTab: {
    Failure: "It could not change the task tab"
  },

  ToggleTaskUrgency: {
    Failure: "It could not change task urgency"
  },

  UnsetTaskOwner: {
    Failure: "It could not unset task to owner",
    Done: "Task have been unset to user"
  },

  UnsetTasksOwner: {
    Failure: "It could not unset tasks to owners",
    Done: "Tasks have been unset to user"
  },

  SetTaskOwner: {
    Failure: "It could not set task to owner",
    Done: "Task have been set to user"
  },

  SetTasksOwner: {
    Failure: "It could not set task to owner",
    Done: "Tasks have been unset to user"
  },

  SendSuggestion: {
    Label: "Suggestions",
    Description: "Describe in detail the doubt or suggestion that you want to do",
    DescriptionWithEmptyMessage: "Describe in detail the doubt or suggestion that you want to do<br/><span style='color:red;'>Please, describe the doubt / sugggestion in the textbox that it is localized just below</span>",
    Failure: "It could not send the doubt / suggestion",
    Done: "The doubt or suggestion have been sent to business unit manager"
  },

  EditNodeDocument: {
    Waiting: "Editing the document. Please, wait...",
    Failure: "#response#"
  },

  SignNodeDocument: {
    Failure: "It could not sign the document. May be you do not have permissions."
  },

  ShowEnvironment: {
    Waiting: "Loading the desktop. Please, wait..."
  },

  ShowDashboard: {
    Waiting: "Loading the dashboard. Please, wait..."
  },

  ToggleDashboard: {
    Waiting: "Loading the dashboard. Please, wait..."
  },

  AddFileAttachment: {
    Failure: "The document exists yet"
  },

  RenameAttachment: {
    Failure: "There is not document to rename"
  },

  DeleteAttachment: {
    Failure: "There is not document to delete"
  },

  DownloadAttachment: {
    Failure: "There is not document to download"
  },

  ReplaceAttachment: {
    Failure: "There is not document to replace"
  }

};

Lang.BPI = {

  GetNode: {
    Parameters: "Wrong number of parameters in the call to getnode",
    Failure: "It could not load the node #id#"
  },

  GetNodeNotes: {
    Parameters: "Wrong number of parameters in the call to getnodenotes",
    Failure: "It could not load the notes to the node #id#"
  },

  SaveNode: {
    Parameters: "Wrong number of parameters in the call to savenode",
    Failure: "No se ha podido guardar el nodo #id#"
  },

  CreateNode: {
    Parameters: "Wrong number of parameters in the call to createnode",
    Failure: "It could not create the node #id#"
  },

  RemoveNode: {
    Parameters: "Wrong number of parameters in the call to removenode",
    Failure: "It could not delete the node #id#"
  }

};

Lang.Buttons = {
  Accept: "Accept",
  Previous: "Previous",
  Next: "Next",
  Finish: "Finish",
  Cancel: "Cancel",
  Close: "Close",
  SendMail: "Send mail to the administrator",
  Yes: "Yes",
  No: "No"
};

Lang.Response = {
  Yes: "Yes",
  No: "No"
};

Lang.Warning = {
  Title: "Warning"
};

Lang.Error = {
  Title: "Error",
  TitleBPI: "Error of BPI"
};

Lang.Information = {
  Title: "Informationn",
  Wait: "Please wait",
  Updated: "Updated done. Reloading..."
};

Lang.Dates = {
  Of: "of",
  At: "at",
  Today: "Today",
  Yesterday: "Ayer"
};

Lang.Element = "element";
Lang.Elements = "elements";
Lang.NoLabel = "Without name";
Lang.Goto = "Go to the element: ";

var aMonths = {
  "es": ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
  "en": ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
};

var aDays = {
  "es": ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
  "en": ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
};

Date.patterns = {
  iso8601long: "d-m-Y H:i:s",
  iso8601short: "d-m-Y",
  shortdate: "j/n/Y",
  longdate: "l, d \\d\\e F \\d\\e Y",
  fulldatetime: "l, d \\d\\e F \\d\\e Y g:i:s A",
  monthday: "d \\d\\e F",
  shorttime: "g:i A",
  longtime: "g:i:s A",
  sortabledatetime: "d-m-Y\\TH:i:s",
  universalsortabledatetime: "d-m-Y H:i:sO",
  yearmonth: "F \\d\\e Y",
  year: "Y"
};

Lang.Dialog = {
  Required: "Required parameters"
};

Lang.ViewNodeLocation = {
  AddPoint: "Add a point",
  AddLine: "Add a line",
  AddPolygon: "Add a polygon",
  CenterLocation: "Center in the object",
  CleanLocation: "Delete marker",
  FinishEditingLocation: "Finish the edition",
  CancelEditingLocation: "Cancel the edition",
  FindLocation: "Go to the location",
  Accept: "Search"
};

Lang.ViewMapLayer = {
  FindLocation: "Go to the location",
  Accept: "Accept",
  Page: "page",
  Previous: "previous",
  Next: "next",
  Loading: "loading map..."
};

Lang.ViewerDocumentList = {
  FileLabel: "Title",
  File: "Document",
  NoLabel: "No label",
  Previous: "previous",
  Next: "next",
  NoDocuments: "No documents",
  Loading: "loading...",
  Uploading: "Uploading the file. Please, wait...",
  ErrorOnUpload: "There have been an error uploading the file",
  Accept: "Accept",
  Cancel: "Cancel"
};

Lang.DialogPrintEntity = {
  Accept: "download",
  Headers: "Document header",
  Column: "Column",
  Range: "Date",
  RangeEmptyMessage : "Let empty value if no limit for date",
  FromDate : "Posterior a, inclusive",
  ToDate : "Anterior a, inclusive",
  DateAttribute : "Date to limit",
  None : "None",

  TaskListAttributes : {
    Label: "Title",
    Description: "Description",
    CreateDate: "Create date",
    UpdateDate: "Update date",
    State: "State",
    Urgent: "Urgent",
    Owner: "Assigned to"
  }
};

Lang.DialogRememberPreference = {
  Title: {
    LayoutMainRightExpand: "Hidden right panel"
  },
  Description: {
    LayoutMainRightExpand: "You have hidden the right panel before, do you want to open it?"
  },
  RememberPreference: "Remember my preference"
};

Lang.DialogSelectTaskOwner = {
  Accept: "select",
  Reason: "reason (empty if no reason)",
  Username: "user"
};

Lang.DialogAlertEntity = {
  Title: "Alert about this element",
  Users: "Users to alert about this element",
  NoUsers: "There is not selected user",
  Message: "Message",
  Searching: 'Searching...',
  Search: 'Search users:',
  Search: 'Search users:',
  SearchDialogComments: 'Choose a filter to make the search. It is required 4 character at least.',

  Error: {
    NoUsers: "There is not selected user to alert about this element"
  }
};

Lang.DialogAlertEntityUser = {
  Delete: "Delete"
};

Lang.DialogSearchUsers = {
  Title: "Search users",
  Users: "Available users",
  Filter: "Filter:",
  NoResults: "The users have not been found"
};

Lang.DialogSearchUsersItem = {
  Add: "Add"
};

Lang.DialogSearchRoles = {
  Title: "Search roles",
  Roles: "Available roles",
  Filter: "Filter:",
  NoResults: "The roles have not been found"
};

Lang.DialogSearchRolesItem = {
  Add: "Add"
};

Lang.DialogTaskComments = {
  Title: "More information"
};

Lang.ViewUser = {
	Home: "home",
	GotoHome: "go home",
	CloseSessionFor: "Close session of",
	User: "User name",
	ViewNotifications: "Show news",
	Trash: "trash",
	Notifications: "news",
	More: "Show more news",
	Environments: "Desktops",
	Dashboards: "Dashboards",
	Units: "Business units",
	Current: "(current)",
	StartSessionWith: "start session in",
	GotoUnit: "go",
	Logout: "logout",
	SendSuggestion: "suggestions",
	EditProfile: "edit profile",
	Loading: "loading..."
};

Lang.Desktop = {
  Title: "Go to the beginning",
  Loading: "loading...",
  Starting: "Loaded. Please, wait..."
};

Lang.LayoutHeader = {
  Title: "",
  GotoFederation: "go the federation",
  ShowHome: "go home "
};

Lang.LayoutMain = {
  Title: "Curriculum Vitae"
};

Lang.LayoutMainHeader = {
  Title: ""
};

ToolbarDefinition.Header = {
};

Lang.LayoutMainCenter = {
  Title: "Curriculum Vitae"
};

Lang.LayoutMainCenterHeader = {
  Title: "Curriculum Vitae",
  NoViews: "No views for this element",
  NoSortingOptions: "No order options for this element"
};

Lang.DialogSearchNodes = {
  Title: "Go to...",
  Loading: "Loading...",
  Search: "Search",
  Searching: "Searching...",

  Error: {
    EmptyCondition: "The condition must not be empty."
  }
};

ToolbarDefinition.Main = {
};

Lang.LayoutMainCenterBody = {
  Title: ""
};

Lang.ViewNode = {
  Show: "show",
  SortByHint: "Sory by ",
  ViewingPrototype: "<b>Template mode</b>. Go back to <a class='command showtask' href='shownode(#{idnode})'>#{node}</a>",

  DialogSaveNode: {
    Title: "modified element",
    Description: "The element has been modified. Do you want to save the changes?"
  },

  DialogUnload: {
    Description: "The element has been modified. When you exit, you will loose the changes..."
  },

  DialogAddNode: {
    NoNodes: "This element does not allow add  elements"
  },

  DialogCopyNodes: {
    NoNodesReferencesSelected: "It has not selected any element"
  },

  DialogDeleteNode: {
    Title: "Delete a element",
    Description: "Are you sure that you want to delete the element?"
  },

  DialogDeleteNodes: {
    Title: "Delete selected elements",
    Description: "Are you sure that you want to delete the selected elements?",
    NoNodesReferencesSelected: "You have not chosen any element"
  },

  DialogExportNode: {
    Title: "Export the selected elements",
    NoNodesReferencesSelected: "You have not chosen any element"
  },

  DialogDeleteAttachment: {
    Title: "Delete document",
    Description: "Are you sure that you want to delete document?"
  },

  DialogSignNode: {
    Title: "Certificate selected",
    Description: "Do you want to record the signature into document?"
  },

  DialogExecuteNodeCommand: {
    Title: "Execute operation",
    Description: "Are you sure that you want execute operation?"
  },

  SignatureState: {
    Pending: "Pending",
    Waiting: "Waiting",
    Signed: "Signed",
    Delayed: "Waiting",
    Disabled: "Disabled",
    Signing: "Signing"
  }

};

Lang.DialogEditNode = {
  Title: "Edit form",
  NoFields: "No fields",
  Loading: "Loading form",

  Error: {
  }

};

Lang.DialogEditNodeDescriptors = {
  Title: "Change the descriptors form",
  Label: "Label:",
  Description: "Commets:",
  Loading: "Loading the descriptors",

  Error: {
    LabelRequired: "The label must not be empty"
  },

  Report: {
    NoDescriptors: "The descriptors for this form are not defined yet..."
  }
};

Lang.DialogAddNode = {
  Title: "Add form/s",
  File: "Select the file which contains the forms to add:",
  Description: "Notes (They will include in the descriptions of the added forms):",
  AddFrom: "Select the action that you want...",
  AddBlank: "Add a new empty form",
  AddFromFile: "Add the forms from an external file",
  AddFromClipboard: "Add the forms which are coppied in the clipboard",
  NodeTypeList: "For which type are the forms that you are going to add?",
  NodeTypeListBlank: " Which kind of forms are you going to add?",
  SelectOne: "Select a type ...",
  Format: "Which is the format of the data file?",
  FormatMonet: "Monet",
  Options: "In case of duplicated forms...",
  OptionPreserveOriginal: "Preserve the original",
  OptionReplace: "Replace the original",
  OptionDuplicate: "Duplicate the forms",
  AddedAt: "Added using an external source the date",
  LoadingNodeType: "Loading the list of fields. Please, wait...",

  Error: {
    TypeRequired: "Select a kind of form",
    FileRequired: "Select the file which contains the forms to add",
    DescriptionRequired: "The description must not be empty"
  }
};

Lang.Wizard = {
  Cancel: "Cancel",
  Previous: "Previous",
  Next: "Next",
  Finish: "Finish"
};

Lang.DialogEditNodeDocument = {
  Step1Wizard: "1 of 3",
  Step1Title: "Editing the document",
  Step1Description: "Download the document to make the edition of the document in your computer. Continue if you have already edit the document.",
  DownloadDocument: "download",
  ContinueEdition: "continue",

  Step2Wizard: "1 of 3",
  Step2Title: "Editing the document",
  Step2Description: "Wait to the download of the document is finished. When the download is finished, you can start to edit it",
  DownloadAgain: "download again",

  Step3Wizard: "2 of 3",
  Step3Title: "Editing the document",
  Step3Description: "Open the document and insert the changes that you want. When you finish, you should upload the modified document",
  ReplaceDocument: "I have just modified the document",

  Step4Wizard: "3 of 3",
  Step4Title: "Editing the document",
  Step4Description: "Indicate the location of the modified document to save the last version in the system",
  FinishEdition: "finish",

  Title: "Edit the document",
  CancelEdition: "cancel",

  Error: {
    FileRequired: "Select the document of your computer which contains the new version"
  }

};

Lang.DialogGenerateReport = {
  Title: "Download...",
  Options: "Which forms would you want to include?",
  OptionAll: "Include all the forms",
  OptionSelection: "Include only the selected forms",
  Filters: "Would you want to apply some filter?",
  FilterNodeType: "You only want the forms with the type:",
  FilterDates: "The created forms in this range of dates:",
  NoNodeTypes: "You have not selected any type of form",
  SelectOne: "Select a type of form",
  FromDate: "From",
  ToDate: "To",
  AddSelectedNodesInfo: "(for activate this option you should select one curriculum form at least",

  Error: {
    DescriptionRequired: "The description must not be empty"
  }

};

Lang.DialogGenerateReportType = {
  Delete: "Delete"
};

Lang.DialogShareNode = {
  Title: "Send the form",
  Users: "Selected users",
  NoUsers: "The is not any selected user currently",
  ShowSearchUsers: "Show the searcher...",
  HideSearchUsers: "Hide the searcher...",
  SearchUsersPanel: "Searcher",
  Description: "Notes (it will show to the user when when he sees the shared form):",
  ExpireDate: "Send the form for a certain period of time(an empty value will show it does not expire)",
  ExpiresAt: "From to day to...",
  PanelInfo: "General",
  PanelUsers: "Users with who I share",

  Error: {
    DescriptionRequired: "The description must not be empty"
  }
};

Lang.DialogShareNodeUser = {
  Delete: "Delete"
};

Lang.ViewTask = {
  ViewingTaskTarget: "You are watching the expedient asociated to the task <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  ViewingTaskInput: "You are watching the request which started the task <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  ViewingTaskOutput: "You are watching the result obteined in the task <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  NoTeam: "There is not any work team",
  CustomerLabel: "Request made by:",

  DialogCreateTask: {
    NoTasks: "There is not possibility to create tasks"
  },

  DialogSetTasksOwner: {
    NoSelection: "No tasks selected"
  },

  DialogUnsetTasksOwner: {
    NoSelection: "No tasks selected",
    Title: "Delete assignations",
    Description: "Are you sure that you want to delete assignations for tasks?"
  },

  DialogUnsetTaskOwner: {
    Title: "Delete assignation",
    Description: "Are you sure that you want to delete assignation for task?"
  },

  DialogConfirmAction: {
    Title: "Continue",
    Description: "Are you sure that you want continue?"
  }

};

Lang.ViewTeam = {
  DialogDeleteDelegate: {
    Title: "Delete a delegate",
    Description: "Are you sure that you want to delente a delegate?"
  },

  DialogDeleteDelegates: {
    Title: "Delete selected delegates",
    Description: "Are you sure that you want to delete the selected delegates?"
  },

  DialogDeleteWorker: {
    Title: "Delete a worker",
    Description: "Are you sure that you want to delete a worker?"
  },

  DialogDeleteWorkers: {
    Title: "Delete the selected workers",
    Description: "Are you sure that you want to delete the selected workers?"
  }
};

Lang.ViewerHelperToolbar = {
  Add: "Add",
  Copy: "Copy",
  Download: "Download",
  Tools: "Tools",
  BackTask: "Back",
  BackNode: "Back"
};

Lang.ViewNodeNotes = {
  DialogAddNoteName: "Note name",
  DialogAddNoteValue: "Note value",
  Add: "add note",
  Delete: "delete",
  Empty: "no notes"
};

Lang.LayoutMainRight = {
  Title: "Right panel",
  TaskListTitle: "Tasks",
  HelperTitle: "Assistant",
  TrashTitle: "Trash"
};

Lang.ViewerHelperSidebar = {
  Add: "Add",
  Tools: "Tools"
};

Lang.ViewerHelperPage = {
  Label: "Help"
};

Lang.Editor = {
  MoreInfo: "[More information]",
  Option: "Value",
  Code: "Code",
  Empty: "No options",
  ShowHistory: "Show recent values",
  HideHistory: "Hide recent values",
  FilterEmpty: "Insert here the search terms",
  FilterEmptyHistory: "Recent values. Insert here the terms for searching",
  Check: "Check",
  Uncheck: "Uncheck",
  Up: "Up",
  Down: "Down",

  Dialogs: {
    Other: {
      Title: "Add...",
      Description: "Please, show the new value:"
    },
    FileUpload: {
      Uploading: "Uploading the file.Please, wait...",
      UploadingFailed: "It could not upload the file"
    },
    PictureUpload: {
      Processing: "Recognizing the image. Please, wait...",
      Uploading: "Uploading the image file. Please, wait...",
      UploadingFailed: "It could not upload the image file"
    },
    Number: {
      Accepts: "Accept ",
      Decimals: " decimals",
      MultipleDecimals: "Accept as many decimals as you want",
      Increments: "It will increase and decrease in #count#",
      Range: "Accept values between #min# and #max#",
      Equivalences: "Value in other unities: #equivalences#"
    },
    Text: {
      Length: "Accept values with a length between #min# and #max#",
      Undefined: "the maxim length that it would be required"
    },
    CheckReload: {
      Reloading: "Reloading. Please, wait...",
      ReloadingFailed: "Could not obtain term list of selected source"
    }
  },

  Templates: {
    DialogListItem: '<li class="element"><table width="100%"><tr><td width="1px"><input name="#{id}" style="margin-top:4px;*margin-top:2px;" type="checkbox"/></td><td><label for="#{id}" style="cursor:pointer;display:block;padding:4px;*padding-top:5px;">#{title}</div></td><td width="35px"><div class="elementoptions"><a class="delete"><img src=#{ImagesPath}/s.gif?a=1" class="trigger" alt="eliminar" title="eliminar"/></a><a class="move"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Hold on the left mouse-button to drag and drop" title="Hold on the left mouse-button to drag and drop"/></a></div></td></tr></table></li>'
  }
};

Lang.ViewerHelperEditors = {

  GotoField: "Go to the field:",
  FirstField: "First",
  PreviousField: "Previous",
  NextField: "Next",
  LastField: "Last",
  Undo: "Undo changes",
  Redo: "Redo changes",
  All: "All",
  None: "None",
  Invert: "Invert",
  Accept: "Accept",

  MessageBoolean: "<div>Up and down cursor arrows to select the value</div><div>[intro] to accept the value and continue the edition</div><div>[espacio] for selecting and deselecting</div>",
  MessageCheck: "<div class='recents'>Up and down cursor arrows to select a value of the reciently value list</div><div>[intro] to accept the value and continue the edition</div>",
  MessageDate: "<div>Up and down cursor arrows to select the value</div><div>Left and right cursor arrows to select day/month/year</div><div>[intro] to accept the value and continue the edition</div>",
  MessageFile: "<div>Press on examinate to select a file from your equipment</div>",
  MessageLink: "<div>Write \"+\" in the searching field to show the recently values</div><div>Up and down cursor arrows to select a value from the list</div><div>[intro] to accept the value and continue the edition</div>",
  MessageList: "<div>To reorder the list... drag a field and drop in the desired position</div><div>[intro] to accept the value and continue the edition</div>",
  MessageNumber: "<div>Up and down cursor arrows to increase and decrease the value respectively.</div><div>[intro] to accept the value and continue the edition</div>",
  MessageFormat: "<div>Fill the field as the format indicated below</div><div>[intro] to accept the value and continue the edition</div>",
  MessagePicture: "<div>Prees on examinate to choose the image file in your equipment</div><div>Once you have chosen the image file, press on accept to select the región which interests to you</div>",
  MessageSelect: "<div>Write \"+\" in the searching field to show the recently values</div><div>Up and down cursor arrows to select a value from the list</div><div>[intro] to accept the value and continue the edition</div>",
  MessageText: "<div class='recents'>Up and down cursor arrows to select a value from the recently values list</div><div>[intro] to accept the value and continue the edition</div>",
  MessageSerial: "<div>This field is not editable</div>",
  MessageLocation: "<div>Select a point, line or polygone in the map to indicate the element location</div>",

  DateWrong: "ERROR. The date is wrong",
  NumberWrong: "ERROR. The inserted value is not a number",
  NumberRangeWrong: "ERROR. The inserted value does not obey with the range of established values for the number",
  FormatWrong: "ERROR. Field with wrong datas",
  LengthWrong: "ERROR. Text length does not allow",

  Edition: "Edition",
  Actions: "Actions",
  LoadDefaultValue: "Fill with the default value",
  Help: "Help",
  Locations: "map view",
  Table: "table view",
  AddDefaultValue: "Establish the current value as default value",
  ClearField: "Delete",
  Increment: "Increase",
  Decrement: "Decrease",
  Check: "Check",
  Download: "Download",
  Add: "Add field",
  DeleteSelected: "Delete selected fields",
  Select: "Select",
  SelectOther: "Add...",
  CropMessage: "Crop the part of the image what you are interesting and press the Accept button",
  PreviousYear: "Previous year",
  NextYear: "Next year",
  SupIndex: "Mark as superscript the selected text from the field",
  SubIndex: "Mark as subscript the selected text from the field",
  Bold: "Mark in bold the selected text in the field",
  Italic: "Mark in italic the selected text in the field",
  AddPoint: "Add a point",
  AddLine: "Add a line",
  AddPolygon: "Add a polygone",
  CenterLocation: "Center the object",
  CleanLocation: "Delete the marker",
  FinishEditingLocation: "Finish the edition",
  CancelEditingLocation: "Cancel the edition",
  File: "File",
  FileLabel: "Title (if it is not indicated, it use the file name",
  Source: "Data source"
};

Lang.ViewerHelperPreview = {
  Title: "Preview"
};

Lang.ViewerHelperObservers = {
  Editing: "editing",
  Label: "Other users that are showing this element"
};

Lang.ViewerHelperRevisionList = {
  Title: "Available versions",
  CurrentRevision: "Current",
  NoRevisions: "No revisions",
  Revisions: "revisions",
  RevisionAt: ""
};

Lang.ViewerHelperChat = {
  Title: "Messages",
  WriteMessage: "write a message",
  SendMessage: "send",
  NoMessages: "no messages"
};

Lang.ViewerHelperMap = {
  Edition: "Edtion",
  Navigation: "Navegation"
};

Lang.ViewerHelperSource = {
  TermExists: "It exists",
  SourceList: "Data sources",
  TermList: "Termss",
  Code: "Code",
  Label: "Label",
  Type: "Type",
  Term: "term",
  SuperTerm: "It contain other terms",
  Category: "category",
  Enabled: "Enabled",
  Enable: "Enable",
  Disable: "Disable",
  Delete: "Delete",
  AddTo: "Add to ",
  Add: "add",
  Tags: "Tags",
  NoTags: "No tags",
  Publish: "Publish",
  NewTerms: "New terms",
  Select: "select: ",
  SelectAll: "all",
  SelectNone: "none",
  NewTermsDescription: "It has added new terms recently but there have not been published",
  Name: "nombre",
  Value: "valor"
};

Lang.ViewerHelperRole = {
  DefinitionList: "Roles",
  Add: "Add",
  ButtonAdd: "add",
  ButtonAddOther: "Add other of same type",
  Save: "Edit",
  ButtonSave: "Save",
  Username: "User",
  BeginDate: "Start date",
  Expires: "Expires",
  ExpireDate: "Expire date",
  DefinitionType: "Class",
  Type: "Type",
  TypeUser: "User",
  TypeService: "Service partner",
  TypeFeeder: "Data source partner",
  SelectService: 'Select service provider',
  SelectFeeder: 'Select data source provider',
  Searching: 'Searching...',
  SelectUser: 'Select an user from list',
  SelectBeginDate: 'Select a valid date',
  User: "User",
  Email: "Email",
  SelectExpireDate: 'The selected date is less than the start date',
  HistoryRoles: "history",

  StateActive: "Active",
  StatePending: "Pending",
  StateInactive: "Expired",

  DatePending: "It actives #{formattedBeginDate}",
  DateActive: "Active from #{formattedBeginDate}",
  DateNoExpires: "It does not expires",
  DateExpires: "It expires #{formattedExpireDate}",
  DateExpiredBegin: "It was active from #{formattedBeginDate}",
  DateExpiredEnd: "It expired #{formattedExpireDate}",

  UserRoleExists: "Already exists an active role for that user",
  ServiceRoleExists: "Already exists an active role for that provider with that service",
  FeederRoleExists: "Already exists an active role for that provider with that data source",
  NoHistory: "no history for this role",
  NoUsers: "no users",
  Users: "users",
  NoServices: "no services",
  NoFeeders: "no feeders",
  Services: "services",
  Feeders: "data sources",
  EmailNotDefined: "no email"
};

Lang.ViewerHelperList = {
};

Lang.ViewTaskList = {

  State: {
    New: "New",
    Pending: "Active",
    Waiting: "Pending",
    Expired: "Expired",
    Finished: "Finished",
    Aborted: "Aborted",
    Failure: "Warning",
    Undefined: "Undefined"
  },

  Type: {
    Service: "Service",
    Activity: "Activity",
    Job: "Job"
  },

  StartAt: "Starts ",
  StartedAt: "Started ",

  DialogAbortTask: {
    Title: "Abort the task",
    Description: "Are you sure that you want to abort the task '<b>#{task}</b>'?"
  },

  DialogSolveTaskLock: {
    Title: "Answer a #{title}",
    Description: "Are you sure that you want to answer '<b>#{lock}</b>'?"
  }

};

Lang.ViewTrash = {
  NoNodes: "The trash is empty",

  DialogEmptyTrash: {
    Title: "Empty the trash",
    Description: "Are you sure that you want to empty the trash?"
  },

  DialogRecoverNodeFromTrash: {
    Title: "Recover element",
    Description: "Are you sure that you want to recover the element?"
  },

  DialogRecoverNodesFromTrash: {
    Title: "Recover elements",
    Description: "Are you sure that you want to recover the selected elements?"
  }

};

Lang.ViewDashboard = {
};

Lang.ViewAnalysisboard = {
};

Lang.ViewSourceList = {
  NoTags: "no tags"
};

Lang.ViewRoleList = {
};

Lang.ViewNotificationList = {
};

Lang.LayoutFooter = {
  Title: ""
};

Lang.ViewFurnitureSet = {
  News: "news",
  Roles: "roles",
  Taskboard: "tasks",
  Tasktray: "my tasks",
  Trash: "trash"
};

Lang.Widget = {

  EmptyNode: "There is not defined element",

  Boolean: {
    Yes: "Yes",
    No: "No"
  },

  Table: {
    ElementLabel: "No label"
  },

  Templates: {
    Observer: '<div class="observer"><span></span> editing...&nbsp;<a class="command" href="requestnodefieldcontrol()">tomar el control</a></div>',
    WidgetOptions: '<div class="options"><a class="clearvalue" alt="delete" title="delete"/></div>',
    Loading: '<div class="wloading"><img src="#{ImagesPath}/icons/loading.gif" alt="Loading..." title="Loading..."/><span>Loading...</span></div>',
    ListTableElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Hold on pushed the left button of the mouse to drag and drop" title="Hold on pushed the left button of the mouse to drag and drop"></a></div></td><td class="label">#{label}</td></tr></table></li>',
    ListElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Hold on pushed the left button of the mouse to drag and drop" title="Hold on pushed the left button of the mouse to drag and drop"></a></div></td><td><div class="elementwidget"><a class="delete" alt="delete" title="delete">&nbsp;</a>#{widget}</div></td></tr></table></li>',
    CompositeExtensibleOptions: '<div class="options extensible"><a class="expand"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Show all the fields" title="Show all the fields"/><span title="Show all the fields">More</span></a><a class="collapse"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Show only the minimum fields" title="Show only the minimum fields"/><span title="Show only the minimum fields">Less</span></a></div>',
    CompositeConditionalOptions: '<span class="options conditional"><input name="compositegroupoptions_#{id}" id="compositegroupoption_#{id}_yes" class="option" style="margin-left:10px;" type="radio" value="yes"/><label style="display:inline;" for="compositegroupoption_#{id}_yes">Si</label><input id="compositegroupoption_#{id}_no" name="compositegroupoptions_#{id}" class="option" style="margin-left:10px;" type="radio" value="no" checked /><label style="display:inline;" for="compositegroupoption_#{id}_no">No</label></span>',
    LinkNodeBox: '<div class="nodecontainer"></div>',
    HistoryStoreUrl: '#{api}?op=loadhistoryterms&code=#{code}&list=#{list}',
    SourceUrl: '#{api}?op=loadsourceterms&list=#{list}&flatten=#{flatten}&depth=#{depth}',
    IndexUrl: '#{api}?op=loadsourceterms&list=#{list}&flatten=#{flatten}&depth=#{depth}',
    DataLinkUrl: '#{api}?op=loadlinknodeitems&domain=#{domain}&code=#{code}&list=#{list}',
    DataLinkLocationsUrl: '#{api}?op=loadlinknodeitemslocations&domain=#{domain}&code=#{code}&list=#{list}',
    DataLinkLocationsCountUrl: '#{api}?op=loadlinknodeitemslocationscount&domain=#{domain}&code=#{code}&list=#{list}',
    AttributeListUrl: '#{api}?op=loadattributes&idnode=#{idnode}',
    CheckReloadSourceUrl: '#{api}?op=loadnodefieldcheckoptions&id=#{id}&source=#{source}&field=#{field}&idfield=#{fieldId}&from=#{from}',
    Required: '<img class="icon wrequired" src="#{ImagesPath}/icons/required.gif" title="Required field is incomplete" alt="*" /><span class="msgwhenrequired">#{messageWhenRequired}</span>',
    Waiting: '<img src="#{ImagesPath}/s.gif" class="#{cls}" alt="#{message}" title="#{message}"><span>#{message}</span>',
    FileSizeExceeded: '<span style="color:red">File size exceeded. Max size is #{size} megabytes (MB)</span>',
    FileOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Download the document" title="Download the document">Download the document</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Delete" title="Delete">Delete</a></div>',
    ImageOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Descargar imagen" title="Delete the image">Delete the image</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Delete" title="Delete">Delete</a></div>',
    LinkOnlineMenu: '<div class="onlinemenu">Go to the element: <a class="hiperlink"></a>&nbsp;-&nbsp;<a class="clearvalue" alt="Delete" title="Delete">Delete</a></div>'
  }

};

WidgetObserverTemplate = new Template(Lang.Widget.Templates.Observer);
WidgetLoadingTemplate = new Template(Lang.Widget.Templates.Loading);
WidgetListTableElementTemplate = new Template(Lang.Widget.Templates.ListTableElement);
WidgetListElementTemplate = new Template(Lang.Widget.Templates.ListElement);
WidgetCompositeExtensibleOptionsTemplate = new Template(Lang.Widget.Templates.CompositeExtensibleOptions);
WidgetCompositeConditionalOptionsTemplate = new Template(Lang.Widget.Templates.CompositeConditionalOptions);
WidgetLinkNodeBoxTemplate = new Template(Lang.Widget.Templates.LinkNodeBox);

WidgetTemplateHistoryStoreUrl = new Template(Lang.Widget.Templates.HistoryStoreUrl);
WidgetTemplateSourceUrl = new Template(Lang.Widget.Templates.SourceUrl);
WidgetTemplateIndexUrl = new Template(Lang.Widget.Templates.IndexUrl);
WidgetTemplateDataLinkUrl = new Template(Lang.Widget.Templates.DataLinkUrl);
WidgetTemplateDataLinkLocationsUrl = new Template(Lang.Widget.Templates.DataLinkLocationsUrl);
WidgetTemplateDataLinkLocationsCountUrl = new Template(Lang.Widget.Templates.DataLinkLocationsCountUrl);
WidgetTemplateAttributeListUrl = new Template(Lang.Widget.Templates.AttributeListUrl);
WidgetTemplateCheckReloadSourceUrl = new Template(Lang.Widget.Templates.CheckReloadSourceUrl);

WidgetOptionsTemplate = new Template(Lang.Widget.Templates.WidgetOptions);
WidgetWaitingTemplate = new Template(Lang.Widget.Templates.Waiting);
WidgetFileSizeExceededTemplate = new Template(Lang.Widget.Templates.FileSizeExceeded);
WidgetFileOnlineMenuTemplate = new Template(Lang.Widget.Templates.FileOnlineMenu);
WidgetImageOnlineMenuTemplate = new Template(Lang.Widget.Templates.ImageOnlineMenu);
WidgetLinkOnlineMenuTemplate = new Template(Lang.Widget.Templates.LinkOnlineMenu);

Lang.Decorator = {
  NodeMark: { // Be careful, case sensitive
    recoveredfromtrash: "recovered from trash",
    added: "added recently",
    copied: "attached recently"
  }
};

