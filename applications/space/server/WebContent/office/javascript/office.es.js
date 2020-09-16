var AppTemplate=new Array;
var Lang=new Array;
var ToolbarDefinition=new Array;
var MenuDefinition=new Array;
var Path=new Array;

Lang.Exceptions = {
  Title: "Se ha producido un error",

  Default: "Error desconocido",

  LoadOperation: "No se ha podido cargar la aplicación",

  Request: {
    Title: "Ha ocurrido un error: Informe del error",
    Description: "<b>Se ha producido un error al intentar procesar la solicitud.</b><br/>Póngase en contanto con el Servicio Técnico e informe de este error:<br/><br/>"
  },

  Internal: {
    Title: "",
    Description: "<b>Se ha producido un error al intentar procesar la solicitud.</b><br/>Pongase en contanto con el Servicio Tecnico e informe de este error:<br/><br/>"
  },

  SessionExpired: "Por motivos de seguridad, la sesión tiene un límite de tiempo. Ese límite de tiempo se ha superado. Vuelva a iniciar sesión.",
  BusinessUnitStopped: "La aplicación ha sido parada. Contacte con el administrador para más información.",
  InitApplication: "Se ha producido un error al intentar iniciar su sesión. Por favor, contacte con el administrador del sistema.",
  ConnectionFailure: "Parece que ha perdido la conexión a internet. Reintentando en 5 segundos..."

};

Lang.Process = {

  AddNodeBlank: {
    Waiting: "Añadiendo #{caption}"
  },

  AddPrototype: {
    Waiting: "Añadiendo #{caption}"
  },

  GenerateReport: {
    Waiting: "Añadiendo #{caption}"
  },

  AddNodeFromFile: {
    Waiting: "Añadiendo desde fichero"
  },

  AddNodeFromClipboard: {
    Waiting: "Añadiendo desde el portapapeles"
  },

  AddFieldNodeLink: {
    Failure: "No se ha podido añadir #{caption}. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Waiting: "Añadiendo #{caption}. Por favor, espere.",
    Done: "El elemento se ha añadido correctamente."
  },

  AddFieldNode: {
    Failure: "No se ha podido añadir #{caption}. Quizás no tenga permisos. Contacte con el administrador del sistema."
  },

  DoTaskAttachNode: {
    Failure: "No se ha podido agregar el/los elemento/s",
    Done: "El/Los elemento/s se ha/n agregado satisfactoriamente"
  },

  DoTaskShare: {
    Failure: "No se ha podido agregar el elemento",
    Done: "El elemento se ha agregado satisfactoriamente"
  },

  DoTaskRevision: {
    Failure: "No se ha podido modificar el/los elemento/s",
    Done: "El/Los elemento/s se ha/n modificado satisfactoriamente"
  },

  LoadDefaultValue: {
    Failure: "No se ha podido obtener el valor predeterminado."
  },

  AddDefaultValue: {
    Failure: "No se ha podido asignar el valor como predeterminado.",
    Done: "El valor ha sido establecido como valor predeterminado."
  }

};

Lang.Action = {

  ShowNode: {
    Failure: "No se ha podido obtener el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema."
  },

  EditNode: {
    Failure: "No se ha podido editar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El elemento se ha guardado correctamente"
  },

  EditNodeDescriptors: {
    Failure: "No se han podido editar los descriptores del elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "Se han guardado correctamente los descriptores"
  },

  SaveNode: {
    Failure: "No se ha podido guardar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El elemento se ha guardado correctamente"
  },

  SaveEmbeddedNode: {
    Failure: "No se ha podido guardar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El elemento se ha guardado correctamente"
  },

  SaveNodeDescriptors: {
    Failure: "No se ha podido guardar el descriptor del elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El descriptor del elemento se ha guardado correctamente"
  },

  AddNode: {
    Done: "Los elementos han sido creados correctamente",
    Waiting: "Creando el elemento...",
    Failure: "#response#"
  },

  PrintNode : {
      Waiting: "Generando documento...",
      TimeConsumption: {
          Title : "Advertencia de tiempo",
          Description : "Se han detectado muchos elementos que cumplen la condición de descarga. Es por ello que la generación del documento va a requerir tiempo. Pruebe a reducir el intervalo temporal que haya indicado. ¿Está seguro que desea continuar?"
      }
  },

  AddPrototype: {
    Done: "El elemento ha sido creado correctamente",
    Waiting: "Creando el elemento...",
    Failure: "#response#"
  },

  GenerateReport: {
    Done: "El informe ha sido creado correctamente",
    Waiting: "Creando el informe...",
    Failure: "No se ha podido generar el informe"
  },

  CopyNode: {
    Failure: "No se ha podido agregar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El elemento ha sido agregado a su curriculum"
  },

  CopyNodes: {
    Failure: "No se ha podido agregar los elementos. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "Los elementos seleccionados han sido agregados a su curriculum"
  },

  ShareNode: {
    Failure: "No se ha podido enviar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El elemento ha sido compartido"
  },

  DeleteNode: {
    Failure: "No se ha podido eliminar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El elemento ha sido enviado a la papelera"
  },

  DeleteNodes: {
    Failure: "No se ha podido eliminar los elementos. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "Los elementos han sido enviados a la papelera"
  },

  DiscardNode: {
    Failure: "No se ha podido descartar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done: "El elemento ha sido descartado correctamente"
  },

  ToggleHighlightNode: {
    Failure: "No se ha podido resaltar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema."
  },

  ImportNode: {
    Failure: "No se ha podido añadir los elementos desde la fuente externa.#response#",
    Waiting: "Añadiendo elementos desde una fuente externa...",
    Done: "Los elementos han sido añadidos correctamente"
  },

  ExportNode: {
    Failure: "No se ha podido realizar exportar. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Waiting: "Generando. Por favor, espere...",
    ParametersWrong: "No se ha indicado el formato de la exportación"
  },

  DownloadNode: {
    Failure: "No se ha podido generar. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Waiting: "Generando. Por favor, espere...",
    ParametersWrong: "Parámetros incorrectos"
  },

  SearchNodes: {
    Failure: "No se ha podido realizar la búsqueda. Contacte con el administrador del sistema."
  },

  ExecuteNodeCommand: {
    Done: "Se ha realizado la operación con éxito",
    Failure: "No se ha podido realizar la operación",
    Waiting: "Realizando la operación. Por favor, espere..."
  },

  ShowTask: {
    Failure: "No se ha podido mostrar la tarea. Contacte con el administrador del sistema."
  },

  ShowTaskNode: {
    Failure: "No se ha podido mostrar el elemento de la tarea"
  },

  DoTask: {
    Failure: "No se ha podido realizar la tarea. Contacte con el administrador del sistema."
  },

  CreateTask: {
    Failure: "No se ha podido crear la tarea. Contacte con el administrador del sistema.",
    Done: "La tarea ha sido creada correctamente."
  },

  AbortTask: {
    Failure: "No se ha podido abortar la tarea.",
    Done: "La tarea ha sido abortada correctamente."
  },

  AlertTask: {
    Failure: "No se ha podido alertar sobre la tarea.",
    Done: "Se ha alertado sobre la tarea correctamente."
  },

  AlertNode: {
    Failure: "No se ha podido alertar sobre esta entidad.",
    Done: "Se ha alertado sobre la entidad correctamente."
  },

  SelectTaskDelegationRole: {
    Failure: "No se ha podido seleccionar el destinatario del encargo.",
    Done: "Se ha seleccionado el destinatario del encargo correctamente."
  },

  SetupTaskDelegation: {
    Failure: "No se ha podido configurar el encargo.",
    Done: "Se ha configurado el encargo correctamente."
  },

  SelectTaskSendJobRole: {
    Failure: "No se ha podido seleccionar el destinatario del trabajo.",
    Done: "Se ha seleccionado el destinatario del trabajo correctamente."
  },

  SetupTaskSendJob: {
    Failure: "No se ha podido configurar el trabajo.",
    Done: "Se ha configurado el trabajo correctamente."
  },

  SetupTaskEnroll: {
    Failure: "No se ha podido resolver debido a algún fallo con el elemento.",
    Done: "Se ha avanzado la tarea correctamente."
  },

  SetupTaskWait: {
    Failure: "No se ha podido resolver debido a algún fallo con el elemento.",
    Done: "Se ha avanzado la tarea correctamente."
  },

  SolveTaskLine: {
    Failure: "No se ha podido avanzar la tarea.",
    Done: "Se ha avanzado la tarea correctamente."
  },

  SolveTaskEdition: {
    Failure: "No se ha podido resolver debido a algún fallo con el elemento.",
    Done: "Se ha avanzado la tarea correctamente."
  },

  EmptyTrash: {
    Done: "La papelera ha sido vaciada correctamente"
  },

  RecoverNodeFromTrash: {
    Done: "El elemento ha sido recuperado"
  },

  RecoverNodesFromTrash: {
    Done: "Los elementos han sido recuperados",
    NoSelectedReferences: "No ha seleccionado ningún elemento"
  },

  SetTaskTitle: {
    Failure: "No se ha podido cambiar el título de la tarea"
  },

  ShowTaskTab: {
    Failure: "No se ha podido cambiar de tab en la tarea"
  },

  ToggleTaskUrgency: {
    Failure: "No se ha podido establecer la urgencia de la tarea"
  },

  UnsetTaskOwner: {
    Failure: "No se ha podido desasignar la tarea",
    Done: "Se ha desasignado la tarea al usuario"
  },

  UnsetTasksOwner: {
    Failure: "No se han podido desasignar las tareas",
    Done: "Se han desasignado las tareas al usuario"
  },

  SetTaskOwner: {
    Failure: "No se ha podido asignar la tarea",
    Done: "Se ha asignado la tarea al usuario"
  },

  SetTasksOwner: {
    Failure: "No se han podido asignar las tareas",
    Done: "Se han asignado las tareas al usuario"
  },

  SendSuggestion: {
    Label: "Opiniones",
    Description: "Describa con detalle la duda o sugerencia que quiera realizarnos",
    DescriptionWithEmptyMessage: "Describa con detalle la duda o sugerencia que quiera realizarnos<br/><span style='color:red;'>Por favor, describa la duda / sugerencia en el cuadro de texto que se encuentra justo debajo</span>",
    Failure: "No se ha podido enviar la duda / sugerencia",
    Done: "Se ha enviado la duda / sugerencia al responsable de la unidad de negocio"
  },

  EditNodeDocument: {
    Waiting: "Editando el documento. Por favor, espere...",
    Failure: "#response#"
  },

  SignNodeDocument: {
    Failure: "No se ha podido firmar el documento. Quizás no tenga permisos."
  },

  ShowEnvironment: {
    Waiting: "Cargando el escritorio. Por favor, espere..."
  },

  ShowDashboard: {
    Waiting: "Cargando el panel de control. Por favor, espere..."
  },

  ToggleDashboard: {
    Waiting: "Cargando el panel de control. Por favor, espere..."
  },

  AddFileAttachment: {
    Failure: "El documento ya existe"
  },

  RenameAttachment: {
    Failure: "No hay documento para renombrar"
  },

  DeleteAttachment: {
    Failure: "No hay documento para borrar"
  },

  DownloadAttachment: {
    Failure: "No hay documento para descargar"
  },

  ReplaceAttachment: {
    Failure: "No hay documento para reemplazar"
  }

};

Lang.BPI = {

  GetNode: {
    Parameters: "Número de parámetros incorrecto en llamada a getnode",
    Failure: "No se ha podido cargar el nodo #id#"
  },

  GetNodeNotes: {
    Parameters: "Número de parámetros incorrecto en llamada a getnodenotes",
    Failure: "No se ha podido cargar las notas del nodo #id#"
  },

  SaveNode: {
    Parameters: "Número de parámetros incorrecto en llamada a savenode",
    Failure: "No se ha podido guardar el nodo #id#"
  },

  CreateNode: {
    Parameters: "Número de parámetros incorrecto en llamada a createnode",
    Failure: "No se ha podido crear el nodo #id#"
  },

  RemoveNode: {
    Parameters: "Número de parámetros incorrecto en llamada a removenode",
    Failure: "No se ha podido borrar el nodo #id#"
  }

};

Lang.Buttons = {
  Accept: "Aceptar",
  Previous: "Anterior",
  Next: "Siguiente",
  Finish: "Finalizar",
  Cancel: "Cancelar",
  Close: "Cerrar",
  SendMail: "Enviar por correo al administrador",
  Yes: "Si",
  No: "No"
};

Lang.Response = {
  Yes: "Sí",
  No: "No"
};

Lang.Warning = {
  Title: "Advertencia"
};

Lang.Error = {
  Title: "Error",
  TitleBPI: "Error de BPI"
};

Lang.Information = {
  Title: "Información",
  Wait: "Por favor, espere",
  Updated: "Actualización realizada. Reiniciando..."
};

Lang.Dates = {
  Of: "de",
  At: "a las",
  Today: "Hoy",
  Yesterday: "Ayer"
};

Lang.Element = "elemento";
Lang.Elements = "elementos";
Lang.NoLabel = "Sin nombre";
Lang.Goto = "Ir al elemento: ";

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
  Required: "Par�metros obligatorios"
};

Lang.ViewNodeLocation = {
  AddPoint: "Añadir un punto",
  AddLine: "Añadir una línea",
  AddPolygon: "Añadir un polígono",
  CenterLocation: "Centrar en el objeto",
  CleanLocation: "Borrar marcador",
  FinishEditingLocation: "Finalizar edición",
  CancelEditingLocation: "Cancelar edición",
  FindLocation: "Ir a ubicación",
  Accept: "Buscar"
};

Lang.ViewMapLayer = {
    FindLocation: "Ir a ubicación",
    Accept: "Aceptar",
    Page: "página",
    Previous: "anterior",
    Next: "siguiente",
    Loading: "cargando mapa..."
};

Lang.ViewerDocumentList = {
  FileLabel: "Título",
  File: "Documento",
  NoLabel: "Sin etiqueta",
  Previous: "anterior",
  Next: "siguiente",
  NoDocuments: "No hay documentos",
  Loading: "cargando...",
  Uploading: "Subiendo el fichero. Por favor, espere...",
  ErrorOnUpload: "Ha habido un error al subir el fichero",
  Accept: "Aceptar",
  Cancel: "Cancelar"
};

Lang.DialogPrintEntity = {
  Accept: "descargar",
  Headers: "Cabecera del documento",
  Column: "Columna",
  Range: "Fecha",
  RangeEmptyMessage : "Dejar valor vacío si no tiene límite",
  FromDate : "Fecha desde",
  ToDate : "Fecha hasta",
  DateAttribute : "Fecha por la que restringir",
  None : "Ninguna",

  TaskListAttributes : {
    Label: "Título",
    Description: "Descripción",
    CreateDate: "Fecha creación",
    UpdateDate: "Fecha actualización",
    State: "Estado",
    Urgent: "Urgente",
    Owner: "Asignada a"
  }
};

Lang.DialogRememberPreference = {
  Title: {
    LayoutMainRightExpand: "Panel derecho oculto"
  },
  Description: {
    LayoutMainRightExpand: "Ha ocultado el panel derecho con anterioridad. ¿Desea abrirlo?"
  },
  RememberPreference: "Recordar mi preferencia"
};

Lang.DialogSelectTaskOwner = {
  Accept: "elegir",
  Reason: "motivo (dejar vacío si no hay motivo)",
  Username: "usuario"
};

Lang.DialogAlertEntity = {
  Title: "Alertar sobre este elemento",
  Users: "Usuarios a los que alertar sobre este elemento",
  NoUsers: "No hay ningún usuario seleccionado",
  Message: "Mensaje",
  Searching: 'Buscando...',
  Search: 'Buscar usuarios:',
  Search: 'Buscar usuarios:',
  SearchDialogComments: 'Indique un filtro para realizar la búsqueda. Se requiere 4 caracteres como mínimo.',

  Error: {
    NoUsers: "No ha seleccionado ningún usuario a los que alertar sobre este elemento"
  }

};

Lang.DialogAlertEntityUser = {
  Delete: "Eliminar"
};

Lang.DialogSearchUsers = {
  Title: "Buscar usuarios",
  Users: "Usuarios disponibles",
  Filter: "Filtro:",
  NoResults: "No se han encontrado usuarios"
};

Lang.DialogSearchUsersItem = {
  Add: "A�adir"
};

Lang.DialogSearchRoles = {
  Title: "Buscar roles",
  Roles: "Roles disponibles",
  Filter: "Filtro:",
  NoResults: "No se han encontrado roles"
};

Lang.DialogSearchRolesItem = {
  Add: "Añadir"
};

Lang.DialogTaskComments = {
  Title: "Más información"
};

Lang.ViewUser = {
	Home: "inicio",
	GotoHome: "ir al inicio",
	CloseSessionFor: "Cerrar sesión de",
	User: "Nombre de usuario",
	ViewNotifications: "Ver noticias",
	Trash: "papelera",
	Notifications: "noticias",
	More: "Ver más noticias",
	Environments: "Escritorios",
	Dashboards: "Paneles de control",
	Units: "Unidades de negocio",
	Current: "(actual)",
	StartSessionWith: "iniciar sesión en",
	GotoUnit: "ir a",
	Logout: "cerrar sesión",
	SendSuggestion: "opiniones",
	EditProfile: "editar perfil",
	Loading: "cargando..."
};

Lang.Desktop = {
  Title: "Ir al inicio",
  Loading: "cargando...",
  Starting: "Cargando. Por favor, espere..."
};

Lang.LayoutHeader = {
  Title: "",
  GotoFederation: "ir a la federación",
  ShowHome: "ir al inicio"
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
  NoViews: "No se dispone de vistas para este elemento",
  NoSortingOptions: "No se dispone de opciones de ordenaci�n para este elemento"
};

Lang.DialogSearchNodes = {
  Title: "Ir a...",
  Loading: "Cargando...",
  Search: "Buscar",
  Searching: "Buscando...",

  Error: {
    EmptyCondition: "La condición no puede ser vacía"
  }
};

ToolbarDefinition.Main = {

  cmdShowHome: {
    hint: "Muestra la página principal",
    caption: "Ir al inicio",
    showCaption: true
  }

};

Lang.LayoutMainCenterBody = {
  Title: ""
};

Lang.ViewNode = {
  Show: "ver",
  SortByHint: "Ordenar por ",
  ViewingPrototype: "<b>Modo plantilla</b>. Volver a <a class='command showtask' href='shownode(#{idnode})'>#{node}</a>",

  DialogSaveNode: {
    Title: "elemento modificado",
    Description: "El elemento ha sido modificado. ¿Desea guardar los cambios?"
  },

  DialogUnload: {
    Description: "El elemento ha sido modificado. Al salir, perderá los cambios..."
  },

  DialogAddNode: {
    NoNodes: "Este elemento no permite añadir elementos"
  },

  DialogCopyNodes: {
    NoNodesReferencesSelected: "No ha seleccionado ningún elemento"
  },

  DialogDeleteNode: {
    Title: "Eliminar un elemento",
    Description: "¿Está seguro que desea eliminar el elemento?"
  },

  DialogDeleteNodes: {
    Title: "Eliminar elementos seleccionados",
    Description: "¿Está seguro que desea eliminar los elementos seleccionados?",
    NoNodesReferencesSelected: "No ha seleccionado ningún elemento"
  },

  DialogExportNode: {
    Title: "Exportar elementos seleccionados",
    NoNodesReferencesSelected: "No ha seleccionado ningún elemento"
  },

  DialogDeleteAttachment: {
    Title: "Eliminar documento",
    Description: "¿Está seguro que desea eliminar el documento seleccionado?"
  },

  DialogSignNode: {
    Title: "Certificado seleccionado",
    Description: "¿Desea incluir la firma en el documento?"
  },

  DialogExecuteNodeCommand: {
    Title: "Ejecutar operación",
    Description: "¿Está seguro que desea ejecutar la operación?"
  },

  ChildView : {
    Final: "Se ha llegado al final"
  },

  SignatureState: {
    Pending: "Pendiente",
    Waiting: "En espera",
    Signed: "Firma realizada",
    Delayed: "En espera",
    Disabled: "Deshabilitada",
    Signing: "Firmando"
  }

};

Lang.DialogEditNode = {
  Title: "Editar formulario",
  NoFields: "No existen campos",
  Loading: "Cargando el formulario",

  Error: {
  }

};

Lang.DialogEditNodeDescriptors = {
  Title: "Cambiar los descriptores del formulario",
  Label: "Etiqueta:",
  Description: "Comentarios:",
  Loading: "Cargando los descriptores",

  Error: {
    LabelRequired: "La etiqueta no puede quedar vacía"
  },

  Report: {
    NoDescriptors: "Aún no ha definido los descriptores para este formulario..."
  }
};

Lang.DialogAddNode = {
  Title: "Añadir formulario/s",
  File: "Seleccione el fichero que contiene los formularios a añadir:",
  Description: "Notas (se incluirán en las descripciones de los formularios añadidos):",
  AddFrom: "Elija la acción que desea...",
  AddBlank: "Añadir un nuevo formulario en blanco",
  AddFromFile: "Añadir los formularios desde un fichero externo",
  AddFromClipboard: "Añadir los formularios que tengo copiados en el portapapeles",
  NodeTypeList: "¿De qué tipo son los formularios que va a añadir?",
  NodeTypeListBlank: "¿De qué tipo es el formulario que va a añadir?",
  SelectOne: "Elija un tipo...",
  Format: "¿Cuál es el formato del fichero que contiene los datos?",
  FormatMonet: "Monet",
  FormatCvn: "Curriculum Vitae Normalizado (CVN)",
  Options: "En caso de formularios duplicados...",
  OptionPreserveOriginal: "Conservar el original",
  OptionReplace: "Reemplazar el original",
  OptionDuplicate: "Duplicar los formularios",
  AddedAt: "Añadido usando una fuente externa el día",
  LoadingNodeType: "Cargando la lista de campos. Por favor, espere...",

  Error: {
    TypeRequired: "Seleccione un tipo de formulario",
    FileRequired: "Seleccione el fichero que contiene los formularios a añadir",
    DescriptionRequired: "La descripción no puede quedar vacía"
  }

};

Lang.Wizard = {
  Cancel: "Cancelar",
  Previous: "Anterior",
  Next: "Siguiente",
  Finish: "Finalizar"
};

Lang.DialogEditNodeDocument = {
  Step1Wizard: "1 de 3",
  Step1Title: "Editando el documento",
  Step1Description: "Descarga el documento para poder realizar la edición del documento en su ordenador. Continúa si ya ha editado el documento.",
  DownloadDocument: "descargar",
  ContinueEdition: "continuar",

  Step2Wizard: "1 de 3",
  Step2Title: "Editando el documento",
  Step2Description: "Espere a que finalice la descarga del documento. Cuando la descarga finalice, podrá empezar a editarlo",
  DownloadAgain: "volver a descargar",

  Step3Wizard: "2 de 3",
  Step3Title: "Editando el documento",
  Step3Description: "Abra el documento e introduzca los cambios que desee. Cuando termine deberá subir el documento modificado",
  ReplaceDocument: "ya he modificado el documento",

  Step4Wizard: "3 de 3",
  Step4Title: "Editando el documento",
  Step4Description: "Indique la localización del documento modificado para guardar la última versión en el sistema",
  FinishEdition: "finalizar",

  Title: "Editar documento",
  CancelEdition: "cancelar",

  Error: {
    FileRequired: "Seleccione el documento de su ordenador que contiene la nueva versión"
  }

};

Lang.DialogGenerateReport = {
  Title: "Descargar...",
  Options: "¿Qué formularios deseas incluir?",
  OptionAll: "Incluir todos los formularios",
  OptionSelection: "Incluir solo los formularios seleccionados",
  Filters: "¿Quieres aplicar algún filtrado?",
  FilterNodeType: "Solo deseas los formularios del tipo:",
  FilterDates: "Los formularios creados en este rango de fechas:",
  NoNodeTypes: "No has seleccionado ningún tipo de formulario",
  SelectOne: "Seleccione un tipo de formulario",
  FromDate: "Desde",
  ToDate: "Hasta",
  AddSelectedNodesInfo: "(para que esta opción está activa deberá seleccionar al menos un formulario del curriculum)",

  Error: {
    DescriptionRequired: "La descripción no puede quedar vacía"
  }

};

Lang.DialogGenerateReportType = {
  Delete: "Eliminar"
};

Lang.DialogShareNode = {
  Title: "Enviar formulario",
  Users: "Usuarios seleccionados",
  NoUsers: "Actualmente no tiene ningún usuario seleccionado",
  ShowSearchUsers: "Mostrar buscador...",
  HideSearchUsers: "Ocultar buscador...",
  SearchUsersPanel: "Buscador",
  Description: "Notas (se le mostrará al usuario cuando vea el formulario compartido):",
  ExpireDate: "Enviar el formulario durante un tiempo determinado (un valor vacío indicará que no caduca)",
  ExpiresAt: "Desde hoy hasta el...",
  PanelInfo: "General",
  PanelUsers: "Usuarios con los que comparto",

  Error: {
    DescriptionRequired: "La descripción no puede quedar vacía"
  }

};

Lang.DialogShareNodeUser = {
  Delete: "Eliminar"
};

Lang.ViewTask = {
  ViewingTaskTarget: "Está viendo el expediente asociado a la tarea <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  ViewingTaskInput: "Está viendo el encargo que originó la tarea <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  ViewingTaskOutput: "Está viendo el resultado obtenido en la tarea <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  NoTeam: "No hay equipo de trabajo",
  CustomerLabel: "Petición realizada por:",

  DialogCreateTask: {
    NoTasks: "No existe la posibilidad de crear tareas"
  },

  DialogSetTasksOwner: {
    NoSelection: "No ha seleccionado ninguna tarea"
  },

  DialogUnsetTasksOwner: {
    NoSelection: "No ha seleccionado ninguna tarea",
    Title: "Quitar asignación de tareas",
    Description: "¿Está seguro que quiere quitar la asignación de esas tareas?"
  },

  DialogUnsetTaskOwner: {
    Title: "Quitar asignación",
    Description: "¿Está seguro que quiere quitar la asignación de la tarea?"
  },

  DialogConfirmAction: {
    Title: "Continuar",
    Description: "¿Está seguro que desea continuar?"
  }

};

Lang.ViewTeam = {
  DialogDeleteDelegate: {
    Title: "Eliminar un delegado",
    Description: "¿Está seguro que desea eliminar el delegado?"
  },

  DialogDeleteDelegates: {
    Title: "Eliminar delegados seleccionados",
    Description: "¿Está seguro que desea eliminar los delegados seleccionados?"
  },

  DialogDeleteWorker: {
    Title: "Eliminar un trabajador",
    Description: "¿Está seguro que desea eliminar el trabajador?"
  },

  DialogDeleteWorkers: {
    Title: "Eliminar trabajadores seleccionados",
    Description: "¿Está seguro que desea eliminar los trabajadores seleccionados?"
  }
};

Lang.ViewerHelperToolbar = {
  Add: "Añadir",
  Copy: "Copiar",
  Download: "Descargar",
  Tools: "Herramientas",
  BackTask: "Volver a la tarea",
  BackNode: "Volver al elemento"
};

Lang.ViewNodeNotes = {
  DialogAddNoteName: "Nombre de la nota",
  DialogAddNoteValue: "Valor de la nota",
  Add: "añadir nota",
  Delete: "borrar",
  Empty: "no hay notas"
};

Lang.LayoutMainRight = {
  Title: "Panel derecho",
  TaskListTitle: "Tareas",
  HelperTitle: "Asistente",
  TrashTitle: "Papelera"
};

Lang.ViewerHelperSidebar = {
  Add: "Añadir",
  Tools: "Herramientas"
};

Lang.ViewerHelperPage = {
  Label: "Ayuda"
};

Lang.Editor = {

  MoreInfo: "[Más información]",
  Option: "Valor",
  Code: "Código",
  Empty: "No existen opciones",
  ShowHistory: "Ver valores recientes",
  HideHistory: "Ocultar valores recientes",
  FilterEmpty: "Introduzca aquí los términos para la búsqueda",
  FilterEmptyHistory: "Valores recientes. Introduzca aquí los términos para la búsqueda",
  Check: "Marcar",
  Uncheck: "Desmarcar",
  Up: "Subir",
  Down: "Bajar",

  Dialogs: {
    Other: {
      Title: "Añadir...",
      Description: "Por favor, indique el nuevo valor:"
    },
    FileUpload: {
      Uploading: "Subiendo el fichero. Por favor, espere...",
      UploadingFailed: "No se ha podido subir el fichero"
    },
    PictureUpload: {
      Processing: "Reconociendo la imagen. Por favor, espere...",
      Uploading: "Subiendo el archivo de imagen. Por favor, espere...",
      UploadingFailed: "No se ha podido subir el archivo de imagen"
    },
    Number: {
      Accepts: "Acepta ",
      Decimals: " decimales",
      MultipleDecimals: "Acepta tantos decimales como quiera",
      Increments: "Aumentará y disminuirá en #count#",
      Range: "Acepta valores entre #min# y #max#",
      Equivalences: "Valor en otras unidades: #equivalences#"
    },
    Text: {
      Length: "Acepta valores con una longitud entre #min# y #max#",
      Undefined: "la longitud máxima que se quiera"
    },
    CheckReload: {
      Reloading: "Actualizando. Por favor, espere...",
      ReloadingFailed: "No se ha podido obtener la lista de términos para la fuente indicada"
    }
  },

  Templates: {
    DialogListItem: '<li class="element"><table width="100%"><tr><td width="1px"><input name="#{id}" style="margin-top:4px;*margin-top:2px;" type="checkbox"/></td><td><label for="#{id}" style="cursor:pointer;display:block;padding:4px;*padding-top:5px;">#{title}</div></td><td width="35px"><div class="elementoptions"><a class="delete"><img src=#{ImagesPath}/s.gif?a=1" class="trigger" alt="eliminar" title="eliminar"/></a><a class="move"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"/></a></div></td></tr></table></li>'
  }
};

Lang.ViewerHelperEditors = {
  GotoField: "Ir al campo:",
  FirstField: "Primero",
  PreviousField: "Anterior",
  NextField: "Siguiente",
  LastField: "Último",
  Undo: "Deshacer cambios",
  Redo: "Rehacer cambios",
  All: "Todo",
  None: "Ninguno",
  Invert: "Invertir",
  Accept: "Aceptar",

  MessageBoolean: "<div>Flechas de cursor arriba y abajo para seleccionar el valor</div><div>[intro] para aceptar el valor y continuar la edición</div><div>[espacio] para marcar y desmarcar</div>",
  MessageCheck: "<div class='recents'>Flechas de cursor arriba y abajo para seleccionar un valor de la lista de valores recientes</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageDate: "<div>Flechas de cursor arriba y abajo para seleccionar el valor</div><div>Flechas de cursor izquierda y derecha para seleccionar dia/mes/año</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageFile: "<div>Pulse en examinar para elegir un fichero de su equipo</div>",
  MessageLink: "<div>Escribe \"+\" en el campo de búsqueda para mostrar los valores recientes</div><div>Flechas de cursor arriba y abajo para seleccionar un valor de la lista</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageList: "<div>Para reordenar la lista... arrastra un campo y suéltalo en la posición deseada</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageNumber: "<div>Flechas de cursor arriba y abajo para aumentar y disminuir el valor respectivamente</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageFormat: "<div>Rellene el campo tal cual indica el formato indicado más abajo</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessagePicture: "<div>Pulse en examinar para elegir un archivo de imagen de su equipo</div><div>Una vez elegido el archivo de imagen, pulse en aceptar para seleccionar la región que le interesa</div>",
  MessageSelect: "<div>Escribe \"+\" en el campo de búsqueda para mostrar los valores recientes</div><div>Flechas de cursor arriba y abajo para seleccionar un valor de la lista</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageText: "<div class='recents'>Flechas de cursor arriba y abajo para seleccionar un valor de la lista de valores recientes</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageSerial: "<div>Este campo no es editable</div>",
  MessageLocation: "<div>Seleccione un punto, línea o polígono en el mapa para indicar la ubicación del elemento</div>",

  DateWrong: "ERROR. La fecha no es correcta",
  NumberWrong: "ERROR. El valor introducido no es un número",
  NumberRangeWrong: "ERROR. El valor introducido no cumple con el rango de valores establecido para el número",
  FormatWrong: "ERROR. Campo con datos incorrectos",
  LengthWrong: "ERROR. Longitud de texto no permitida",

  Edition: "Edición",
  Actions: "Acciones",
  LoadDefaultValue: "Rellenar con el valor por defecto",
  Help: "Ayuda",
  Locations: "vista de mapa",
  Table: "vista de tabla",
  AddDefaultValue: "Establecer valor actual como valor por defecto",
  ClearField: "Borrar",
  Increment: "Aumentar",
  Decrement: "Disminuir",
  Check: "Marcar",
  Download: "Descargar",
  Add: "Añadir campo",
  DeleteSelected: "Borrar campos seleccionados",
  Select: "Seleccionar",
  SelectOther: "Añadir...",
  CropMessage: "Recorte el trozo de imagen que le interese y pulse en el botón Aceptar",
  PreviousYear: "Año anterior",
  NextYear: "Año siguiente",
  SupIndex: "Marcar como superíndice el texto seleccionado del campo",
  SubIndex: "Marcar como subíndice el texto seleccionado del campo",
  Bold: "Marcar en negrita el texto seleccionado del campo",
  Italic: "Marcar en cursiva el texto seleccionado del campo",
  AddPoint: "Añadir un punto",
  AddLine: "Añadir una línea",
  AddPolygon: "Añadir un polígono",
  CenterLocation: "Centrar en el objeto",
  CleanLocation: "Borrar marcador",
  FinishEditingLocation: "Finalizar edición",
  CancelEditingLocation: "Cancelar edición",
  File: "Fichero",
  FileLabel: "Título (si no se indica, se usa el nombre del fichero)",
  Source: "Fuente de datos"
};

Lang.ViewerHelperPreview = {
  Title: "Vista previa"
};

Lang.ViewerHelperObservers = {
  Editing: "editando",
  Label: "Otros usuarios que están viendo este elemento"
};

Lang.ViewerHelperRevisionList = {
  Title: "Versiones disponibles",
  CurrentRevision: "Actual",
  NoRevisions: "No existen versiones",
  Revisions: "versiones",
  RevisionAt: ""
};

Lang.ViewerHelperChat = {
  Title: "Mensajes",
  WriteMessage: "escribe un mensaje aquí",
  SendMessage: "enviar",
  NoMessages: "no hay mensajes"
};

Lang.ViewerHelperMap = {
  Edition: "Edición",
  Navigation: "Navegación"
};

Lang.ViewerHelperSource = {
  TermExists: "ya existe",
  SourceList: "Fuentes de datos",
  TermList: "Términos",
  Code: "Código",
  Label: "Etiqueta",
  Type: "Tipo",
  Term: "término",
  SuperTerm: "Contiene otros términos",
  Category: "categoría",
  Enabled: "Habilitado",
  Enable: "Habilitar",
  Disable: "Deshabilitar",
  Delete: "Quitar",
  AddTo: "Añadir a ",
  Add: "añadir",
  Tags: "Marcadores",
  NoTags: "no tiene marcadores",
  Publish: "Publicar",
  NewTerms: "Términos nuevos",
  Select: "seleccionar: ",
  SelectAll: "todos",
  SelectNone: "ninguno",
  NewTermsDescription: "Ha añadido recientemente nuevos términos pero no han sido publicados",
  Name: "nombre",
  Value: "valor"
};

Lang.ViewerHelperRole = {
  DefinitionList: "Roles",
  Add: "Añadir",
  ButtonAdd: "añadir",
  ButtonAddOther: "Añadir otro de este tipo",
  Save: "Editar",
  ButtonSave: "Guardar",
  Username: "Usuario",
  BeginDate: "Fecha inicio",
  Expires: "Caduca",
  ExpireDate: "Fecha de caducidad",
  DefinitionType: "Clase",
  Type: "Tipo",
  TypeUser: "Usuario",
  TypeService: "Proveedor de servicios",
  TypeFeeder: "Proveedor de fuente de datos",
  SelectService: 'Selecciona el servicio del proveedor',
  SelectFeeder: 'Selecciona la fuente de datos del proveedor',
  Searching: 'Buscando...',
  SelectUser: 'Seleccione un usuario de la lista',
  SelectBeginDate: 'Seleccione una fecha válida',
  User: "Usuario",
  Email: "Dirección de correo",
  SelectExpireDate: 'La fecha seleccionada es menor que la fecha de inicio',
  HistoryRoles: "historial",

  StateActive: "Activo",
  StatePending: "Pendiente",
  StateInactive: "Caducado",

  DatePending: "Se activa #{formattedBeginDate}",
  DateActive: "Activo desde #{formattedBeginDate}",
  DateNoExpires: "No caduca",
  DateExpires: "Caduca #{formattedExpireDate}",
  DateExpiredBegin: "Estuvo activo desde #{formattedBeginDate}",
  DateExpiredEnd: "Caducó #{formattedExpireDate}",

  UserRoleExists: "Ya existe un role habilitado para ese usuario",
  ServiceRoleExists: "Ya existe un role habilitado para ese proveedor con ese servicio",
  FeederRoleExists: "Ya existe un role habilitado para ese proveedor con esa fuente de datos",
  NoHistory: "sin historial para este role",
  NoUsers: "no existen usuarios",
  Users: "usuarios",
  NoServices: "no existen servicios disponibles",
  NoFeeders: "no existen fuentes de datos disponibles",
  Services: "servicios",
  Feeders: "fuentes de datos",
  EmailNotDefined: "sin email"
};

Lang.ViewerHelperList = {
};

Lang.ViewTaskList = {

  State: {
    New: "Nueva",
    Pending: "Activa",
    Waiting: "Pendiente",
    Expired: "Expirada",
    Finished: "Finalizada",
    Aborted: "Abortada",
    Failure: "Alerta",
    Undefined: "Sin definir"
  },

  Type: {
    Service: "Servicio",
    Activity: "Actividad",
    Job: "Trabajo"
  },

  StartAt: "Comienza ",
  StartedAt: "Comenzó ",

  DialogAbortTask: {
    Title: "Abortar tarea",
    Description: "¿Está seguro que desea abortar la tarea '<b>#{task}</b>'?"
  },

  DialogSolveTaskLock: {
    Title: "Responder a #{title}",
    Description: "¿Está seguro que desea responder '<b>#{lock}</b>'?"
  }

};

Lang.ViewTrash = {
  NoNodes: "La papelera está vacía",

  DialogEmptyTrash: {
    Title: "Vaciar la papelera",
    Description: "¿Está seguro que desea vaciar la papelera?"
  },

  DialogRecoverNodeFromTrash: {
    Title: "Recuperar elemento",
    Description: "¿Está seguro que desea recuperar el elemento?"
  },

  DialogRecoverNodesFromTrash: {
    Title: "Recuperar elementos",
    Description: "¿Está seguro que desea recuperar los elementos seleccionados?"
  }

};

Lang.ViewDashboard = {
};

Lang.ViewAnalysisboard = {
};

Lang.ViewSourceList = {
  NoTags: "no tiene marcadores"
};

Lang.ViewRoleList = {
};

Lang.ViewNotificationList = {
};

Lang.LayoutFooter = {
  Title: ""
};

Lang.ViewFurnitureSet = {
  News: "noticias",
  Roles: "roles",
  Taskboard: "tareas",
  Tasktray: "mis tareas",
  Trash: "papelera"
};

Lang.Widget = {

  EmptyNode: "No se ha definido el elemento",

  Boolean: {
    Yes: "Si",
    No: "No"
  },

  Table: {
    ElementLabel: "Sin etiqueta"
  },

  Templates: {
    Observer: '<div class="observer"><span></span> editando...&nbsp;<a class="command" href="requestnodefieldcontrol()">tomar el control</a></div>',
    WidgetOptions: '<div class="options"><a class="clearvalue" alt="borrar" title="borrar"/></div>',
    Loading: '<div class="wloading"><img src="#{ImagesPath}/icons/loading.gif" alt="Cargando..." title="Cargando..."/><span>Cargando...</span></div>',
    ListTableElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"></a></div></td><td class="label">#{label}</td></tr></table></li>',
    ListElement: '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"></a></div></td><td><div class="elementwidget"><a class="delete" alt="borrar" title="borrar">&nbsp;</a>#{widget}</div></td></tr></table></li>',
    CompositeExtensibleOptions: '<div class="options extensible"><a class="expand"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Ver todos los campos" title="Ver todos los campos"/><span title="Ver todos los campos">Más</span></a><a class="collapse"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Ver solo los campos mínimos" title="Ver solo los campos mínimos"/><span title="Ver solo los campos mínimos">Menos</span></a></div>',
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
    Required: '<img class="icon wrequired" src="#{ImagesPath}/icons/required.gif" title="Campo obligatorio incompleto" alt="*" /><span class="msgwhenrequired">#{messageWhenRequired}</span>',
    Waiting: '<img src="#{ImagesPath}/s.gif" class="#{cls}" alt="#{message}" title="#{message}"><span>#{message}</span>',
    FileSizeExceeded: '<span style="color:red">Tamaño de archivo excedido. El tamaño máximo es de #{size} megabytes (MB)</span>',
    FileOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Descargar documento" title="Descargar documento">Descargar documento</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>',
    ImageOnlineMenu: '<div class="onlinemenu"><a class="download" alt="Descargar imagen" title="Descargar imagen">Descargar imagen</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>',
    LinkOnlineMenu: '<div class="onlinemenu">Ir al elemento: <a class="hiperlink"></a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>'
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
    recoveredfromtrash: "recuperado de la papelera",
    added: "añadido recientemente",
    copied: "agregado recientemente"
  }

};

