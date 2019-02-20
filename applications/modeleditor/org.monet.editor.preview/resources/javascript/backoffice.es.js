var AppTemplate=new Array;
var Lang=new Array;
var ToolbarDefinition=new Array;
var MenuDefinition=new Array;
var Path=new Array;

Lang.Exceptions = {
	Title : "Se ha producido un error",

  Default : "Error desconocido",

	LoadOperation : "No se ha podido cargar la aplicación",

  Request : {
    Title: "Ha ocurrido un error: Informe del error",
    Description: "<b>Se ha producido un error al intentar procesar la solicitud.</b><br/>Póngase en contanto con el Servicio Técnico e informe de este error:<br/><br/>"
  },

  Internal : {
    Title: "",
    Description: "<b>Se ha producido un error al intentar procesar la solicitud.</b><br/>Pongase en contanto con el Servicio Tecnico e informe de este error:<br/><br/>"
  },

  SessionExpired : "Por motivos de seguridad, la sesión tiene un límite de tiempo. Ese límite de tiempo se ha superado. Vuelva a iniciar sesión.",
  BusinessUnitStopped : "La aplicación ha sido parada. Contacte con el administrador para más información.",
  InitApplication : "Se ha producido un error al intentar iniciar su sesión. Por favor, contacte con el administrador del sistema.",
  ConnectionFailure : "Parece que ha perdido la conexión a internet. Reintentando en 5 segundos..."

};

Lang.Process = {

  AddNodeBlank : {
    Waiting : "Añadiendo #{caption}"
  },

  AddPrototype : {
    Waiting : "Añadiendo #{caption}"
  },

  GenerateReport : {
    Waiting : "Añadiendo #{caption}"
  },

  AddNodeFromFile : {
    Waiting : "Añadiendo desde fichero"
  },

  AddNodeFromClipboard : {
    Waiting : "Añadiendo desde el portapapeles"
  },

  AddFieldNodeLink : {
    Failure : "No se ha podido añadir #{caption}. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Waiting : "Añadiendo #{caption}. Por favor, espere.",
    Done : "El elemento se ha añadido correctamente."
  },

  AddFieldNode : {
    Failure : "No se ha podido añadir #{caption}. Quizás no tenga permisos. Contacte con el administrador del sistema."
  },

  EnrolTaskBeforeAction : {
    Failure : "No se ha podido unir al equipo de la tarea",
    Done : "Se ha unido al grupo de la tarea correctamente"
  },

  DoTaskAttachNode : {
    Failure : "No se ha podido agregar el/los elemento/s",
    Done : "El/Los elemento/s se ha/n agregado satisfactoriamente"
  },

  DoTaskShare : {
    Failure : "No se ha podido agregar el elemento",
    Done : "El elemento se ha agregado satisfactoriamente"
  },

  DoTaskRevision : {
    Failure : "No se ha podido modificar el/los elemento/s",
    Done : "El/Los elemento/s se ha/n modificado satisfactoriamente"
  },

  LoadDefaultValue : {
    Failure: "No se ha podido obtener el valor predeterminado."
  },

  AddDefaultValue : {
    Failure: "No se ha podido asignar el valor como predeterminado.",
    Done: "El valor ha sido establecido como valor predeterminado."
  }

};

Lang.Action = {
  
  ShowNode : {
    Failure : "No se ha podido obtener el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema."
  },

  EditNode : {
    Failure : "No se ha podido editar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El elemento se ha guardado correctamente"
  },

  EditNodeDescriptors : {
    Failure : "No se han podido editar los descriptores del elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "Se han guardado correctamente los descriptores"
  },

  SaveNode : {
    Failure : "No se ha podido guardar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El elemento se ha guardado correctamente"
  },

  SaveEmbeddedNode : {
    Failure : "No se ha podido guardar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El elemento se ha guardado correctamente"
  },

  SaveNodeDescriptors : {
    Failure : "No se ha podido guardar el descriptor del elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El descriptor del elemento se ha guardado correctamente"
  },

  AddNode : {
    Done : "Los elementos han sido creados correctamente",
    Waiting : "Creando el elemento...",
    Failure : "#response#"
  },

  AddPrototype : {
    Done : "El elemento ha sido creado correctamente",
    Waiting : "Creando el elemento...",
    Failure : "#response#"
  },

  GenerateReport : {
    Done : "El informe ha sido creado correctamente",
    Waiting : "Creando el informe...",
    Failure : "No se ha podido generar el informe"
  },

  CopyNode : {
    Failure : "No se ha podido agregar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El elemento ha sido agregado a su curriculum"
  },

  CopyNodes : {
    Failure : "No se ha podido agregar los elementos. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "Los elementos seleccionados han sido agregados a su curriculum"
  },

  ShareNode : {
    Failure : "No se ha podido enviar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El elemento ha sido compartido"
  },

  DeleteNode : {
    Failure : "No se ha podido eliminar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El elemento ha sido enviado a la papelera"
  },

  DeleteNodes : {
    Failure : "No se ha podido eliminar los elementos. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "Los elementos han sido enviados a la papelera"
  },

  DiscardNode : {
    Failure : "No se ha podido descartar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Done : "El elemento ha sido descartado correctamente"
  },

  ToggleHighlightNode : {
    Failure : "No se ha podido resaltar el elemento. Quizás no tenga permisos. Contacte con el administrador del sistema."
  },

  ImportNode : {
    Failure : "No se ha podido añadir los elementos desde la fuente externa.#response#",
    Waiting : "Añadiendo elementos desde una fuente externa...",
    Done : "Los elementos han sido añadidos correctamente"
  },
    
  ExportNode : {
    Failure : "No se ha podido realizar exportar. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Waiting : "Generando. Por favor, espere...",
    ParametersWrong : "No se ha indicado el formato de la exportación"
  },

  DownloadNode : {
    Failure : "No se ha podido generar. Quizás no tenga permisos. Contacte con el administrador del sistema.",
    Waiting : "Generando. Por favor, espere...",
    ParametersWrong : "Parámetros incorrectos"
  },

  SearchNodes : {
    Failure : "No se ha podido realizar la búsqueda. Contacte con el administrador del sistema."
  },

  ExecuteNodeCommand : {
    Failure : "No se ha podido realizar la operación",
    Waiting : "Realizando la operación. Por favor, espere..."
  },

  ShowTask : {
    Failure : "No se ha podido mostrar la tarea. Contacte con el administrador del sistema."
  },

  ShowTaskNode : {
    Failure : "No se ha podido mostrar el elemento de la tarea"
  },

  DoTask : {
    Failure : "No se ha podido realizar la tarea. Contacte con el administrador del sistema."
  },

  CreateTask : {
    Failure : "No se ha podido crear la tarea. Contacte con el administrador del sistema.",
    Done : "La tarea ha sido creada correctamente."
  },

  AbortTask : {
    Failure: "No se ha podido abortar la tarea.",
    Done: "La tarea ha sido abortada correctamente."
  },

  PriorizeTask : {
    Failure: "No se ha podido priorizar la tarea.",
    Done: "La tarea ha sido priorizada correctamente."
  },

  AlertTask : {
    Failure: "No se ha podido alertar sobre la tarea.",
    Done: "Se ha alertado sobre la tarea correctamente."
  },
  
  AlertNode : {
    Failure: "No se ha podido alertar sobre esta entidad.",
    Done: "Se ha alertado sobre la entidad correctamente."
  },

  EnrolTask : {
    Failure: "No se ha podido unir a la tarea.",
    Done: "Se ha unido a la tarea correctamente."
  },

  UnEnrolTask : {
    Failure: "No se ha podido abandonar la tarea.",
    Done: "Se ha abandonado la tarea correctamente."
  },

  RetryTaskLock : {
    Failure: "No se ha podido enviar la orden.",
    Done: "Reintentando la solicitud de servicio."
  },

  SolveTaskLock : {
    Failure: "No se ha podido resolver el bloqueo.",
    Done: "Se ha avanzado la tarea correctamente."
  },

  SolveTaskFormLock : {
    Failure: "No se ha podido resolver el bloqueo debido a algún fallo con el elemento.",
    Done: "Se ha avanzado la tarea correctamente."
  },

  DoTaskLockAction : {
    Failure: "No se ha podido ejecutar la operación.",
    Done: "Se ha avanzado la tarea correctamente."
  },

  CancelTaskLock : {
    Failure: "No se ha podido cancelar la tarea.",
    Done: "La tarea ha sido cancelada correctamente."
  },

  SetTaskGoal : {
    Failure: "No se ha podido cambiar el objetivo de la tarea.",
    Done: "Se actualizado el objetivo de la tarea correctamente."
  },

  EditTaskCheckPoints : {
    Failure: "No se ha podido actualizar los puntos de control de la tarea.",
    Done: "Los puntos de control de la tarea se han actualizado correctamente."
  },

  EmptyTrash : {
    Done : "La papelera ha sido vaciada correctamente"
  },

  RecoverNodeFromTrash : {
    Done : "El elemento ha sido recuperado"
  },

  RecoverNodesFromTrash : {
    Done : "Los elementos han sido recuperados",
    NoSelectedReferences :  "No ha seleccionado ningún elemento"
  },

  SetTaskTitle : {
    Failure : "No se ha podido cambiar el título de la tarea"
  },

  ShowTaskTab : {
    Failure : "No se ha podido cambiar de tab en la tarea"
  },

  SendSuggestion : {
    Label : "Enviar Duda / Sugerencia",
    Description : "Describa con detalle la duda o sugerencia que quiera realizarnos",
    DescriptionWithEmptyMessage : "Describa con detalle la duda o sugerencia que quiera realizarnos<br/><span style='color:red;'>Por favor, describa la duda / sugerencia en el cuadro de texto que se encuentra justo debajo</span>",
    Failure : "No se ha podido enviar la duda / sugerencia"
  },

  SaveCubeReport : {
    Done : "El informe se ha guardado correctamente"
  },
  
  SaveCubeReportAs : {
    Label : "Guardar informe como",
    Description : "Indique el nuevo nombre para el informe",
    DescriptionWithEmptyMessage : "Indique el nuevo nombre para el informe<br/><span style='color:red;'>Por favor, indique el nombre en el cuadro de texto que se encuentra justo debajo</span>",
    Failure : "No se ha podido guardar el informe",
    Done : "El informe se ha guardado correctamente"
  },

  EditNodeDocument : {
    Waiting : "Editando el documento. Por favor, espere...",
    Failure : "#response#"
  },
  
  SignNodeDocument : {
    Failure : "No se ha podido firmar el documento. Quizás no tenga permisos."
  },
  
  ShowEnvironment : {
    Waiting : "Cargando el escritorio. Por favor, espere..."
  },
  
  AddFileAttachment : {
    Failure : "El documento ya existe"
  },
  
  RenameAttachment : {
    Failure : "No hay documento para renombrar" 
  },

  DeleteAttachment : {
    Failure : "No hay documento para borrar" 
  },

  DownloadAttachment : {
    Failure : "No hay documento para descargar" 
  },

  ReplaceAttachment : {
    Failure : "No hay documento para reemplazar" 
  }
  
};

Lang.BPI = {

  GetNode : {
    Parameters : "Número de parámetros incorrecto en llamada a getnode",
    Failure : "No se ha podido cargar el nodo #id#"
  },

  GetNodeNotes : {
    Parameters : "Número de parámetros incorrecto en llamada a getnodenotes",
    Failure : "No se ha podido cargar las notas del nodo #id#"
  },

  SaveNode : {
    Parameters : "Número de parámetros incorrecto en llamada a savenode",
    Failure : "No se ha podido guardar el nodo #id#"
  },

  CreateNode : {
    Parameters : "Número de parámetros incorrecto en llamada a createnode",
    Failure : "No se ha podido crear el nodo #id#"
  },

  RemoveNode : {
    Parameters : "Número de parámetros incorrecto en llamada a removenode",
    Failure : "No se ha podido borrar el nodo #id#"
  }

};

Lang.Buttons = {
	Accept : "Aceptar",
  Previous: "Anterior",
  Next: "Siguiente",
  Finish: "Finalizar",
	Cancel : "Cancelar",
	Close : "Cerrar",
	SendMail : "Enviar por correo al administrador",
	Yes : "Si",
	No : "No"
};

Lang.Response = {
  Yes : "Sí",
  No : "No"
};

Lang.Warning = {
  Title: "Advertencia"
};

Lang.Error = {
  Title: "Error",
  TitleBPI : "Error de BPI"
};

Lang.Information = {
  Title: "Información",
  Wait: "Por favor, espere"
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
  "es" : ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
  "en" : ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
};

var aDays = {
  "es" : ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
  "en" : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
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
  Required : "Parámetros obligatorios"
};

Lang.DialogSignNodeDocument = {
  SignDocument : "Firmar documento",
  Certificates : "Seleccione el certificado con el que desea firmar"
};

Lang.ViewNodeLocation = {
  AddPoint : "Añadir un punto",
  AddLine : "Añadir una línea",
  AddPolygon : "Añadir un polígono",
  CenterLocation : "Centrar en el objeto",
  CleanLocation : "Borrar marcador",
  FinishEditingLocation : "Finalizar edición",
  CancelEditingLocation : "Cancelar edición",
  FindLocation: "Ir a ubicación",
  Accept: "Buscar"
};

Lang.ViewMapLayer = {
  FindLocation: "Ir a ubicación",
  Accept: "Aceptar"
};

Lang.ViewerDocumentList = {
  FileLabel : "Título",
  File : "Documento",
  NoLabel : "Sin etiqueta",
  Previous : "anterior",
  Next : "siguiente",
  NoDocuments : "No hay documentos",
  Loading : "cargando...",
  Uploading : "Subiendo el fichero. Por favor, espere...",
  ErrorOnUpload : "Ha habido un error al subir el fichero",
  Accept : "Aceptar",
  Cancel : "Cancelar"
};

Lang.DialogRememberPreference = {
  Title : {
    LayoutMainRightExpand: "Panel derecho oculto"
  },
  Description: {
    LayoutMainRightExpand: "Ha ocultado el panel derecho con anterioridad. ¿Desea abrirlo?"
  },
  RememberPreference: "Recordar mi preferencia"
};

Lang.DialogAlertEntity = {
  Title : "Alertar sobre este elemento",
  Users : "Usuarios a los que alertar sobre este elemento",
  NoUsers : "No hay ningún usuario seleccionado",
  Message : "Mensaje",
  Searching : 'Buscando...',
  Search : 'Buscar usuarios:',
  Search : 'Buscar usuarios:',
  SearchDialogComments : 'Indique un filtro para realizar la búsqueda. Se requiere 4 caracteres como mínimo.',

  Error : {
    NoUsers : "No ha seleccionado ningún usuario a los que alertar sobre este elemento"
  }

};

Lang.DialogAlertEntityUser = {
  Delete: "Eliminar"
};

Lang.DialogSearchUsers = {
  Title : "Buscar usuarios",
  Users : "Usuarios disponibles",
  Filter : "Filtro:",
  NoResults : "No se han encontrado usuarios"
};

Lang.DialogSearchUsersItem = {
  Add : "Añadir"
};

Lang.DialogSearchRoles = {
  Title : "Buscar roles",
  Roles : "Roles disponibles",
  Filter : "Filtro:",
  NoResults : "No se han encontrado roles"
};

Lang.DialogSearchRolesItem = {
  Add : "Añadir"
};

Lang.ViewUser = {
  Home : "inicio",
  GotoHome : "ir al inicio",
  CloseSessionFor : "Cerrar sesión de",
  User : "Nombre de usuario",
  ViewNotifications : "Ver las avisos",
  Notifications : "Avisos",
  NoNotifications : "No existen avisos",
  More : "Ver más avisos",
  Environments : "Escritorios",
  CurrentEnvironment : "(actual)",
  StartSessionWith : "iniciar sesión en",
  Logout : "cerrar sesión",
  SendSuggestion : "Duda / Sugerencia"
};

Lang.Desktop = {
  Title: "Ir al inicio",
  Loading: "cargando...",
  Starting: "Cargando. Por favor, espere..."
};

Lang.LayoutHeader = {
  Title : "",
  GotoFederation : "ir a la federación",
  ShowHome : "ir al inicio"
};

Lang.LayoutMain = {
  Title : "Curriculum Vitae"
};

Lang.LayoutMainHeader = {
  Title : ""
};

ToolbarDefinition.Header = {
};

Lang.LayoutMainCenter = {
  Title : "Curriculum Vitae"
};

Lang.LayoutMainCenterHeader = {
  Title : "Curriculum Vitae",
  NoViews: "No se dispone de vistas para este elemento",
  NoSortingOptions: "No se dispone de opciones de ordenación para este elemento"
};

Lang.DialogSearchNodes = {
  Title : "Ir a...",
  Loading : "Cargando...",
  Search: "Buscar",
  Searching: "Buscando...",

  Error : {
    EmptyCondition : "La condición no puede ser vacía"
  }
};

ToolbarDefinition.Main = {  

  cmdShowHome : {
    hint: "Muestra la página principal",
    caption: "Ir al inicio",
    showCaption: true
  }

};

Lang.LayoutMainCenterBody = {
  Title : ""
};

Lang.ViewNode = {
  Show: "ver",
  SortByHint : "Ordenar por ",
  ViewingPrototype : "<b>Modo plantilla</b>. Volver a <a class='command showtask' href='shownode(#{idnode})'>#{node}</a>",

  DialogSaveNode : {
    Title : "elemento modificado",
    Description : "El elemento ha sido modificado. ¿Desea guardar los cambios?"
  },

  DialogUnload : {
    Description : "El elemento ha sido modificado. Al salir, perderá los cambios..."
  },

  DialogAddNode : {
    NoNodes : "Este elemento no permite añadir elementos"
  },

  DialogCopyNodes : {
    NoNodesReferencesSelected : "No ha seleccionado ningún elemento"
  },

  DialogDeleteNode : {
    Title : "Eliminar un elemento",
    Description : "¿Está seguro que desea eliminar el elemento?"
  },

  DialogDeleteNodes : {
    Title : "Eliminar elementos seleccionados",
    Description : "¿Está seguro que desea eliminar los elementos seleccionados?",
    NoNodesReferencesSelected : "No ha seleccionado ningún elemento"
  },

  DialogExportNode : {
    Title : "Exportar elementos seleccionados",
    NoNodesReferencesSelected : "No ha seleccionado ningún elemento"
  },

  DialogDeleteAttachment : {
    Title : "Eliminar documento",
    Description : "¿Está seguro que desea eliminar el documento seleccionado?"
  },

  SignatureState : {
    Pending : "Pendiente",
    Waiting : "En espera",
    Signed : "Firma realizada",
    Disabled : "En espera"
  },

};

Lang.DialogEditNode = {
  Title : "Editar formulario",
  NoFields : "No existen campos",
  Loading : "Cargando el formulario",

  Error : {
  }

};

Lang.DialogEditNodeDescriptors = {
  Title : "Cambiar los descriptores del formulario",
  Label : "Etiqueta:",
  Description : "Comentarios:",
  Loading : "Cargando los descriptores",
  
  Error : {
    LabelRequired : "La etiqueta no puede quedar vacía"
  },

  Report : {
    NoDescriptors : "Aún no ha definido los descriptores para este formulario..."
  }
};

Lang.DialogAddNode = {
  Title : "Añadir formulario/s",
  File : "Seleccione el fichero que contiene los formularios a añadir:",
  Description : "Notas (se incluirán en las descripciones de los formularios añadidos):",
  AddFrom : "Elija la acción que desea...",
  AddBlank : "Añadir un nuevo formulario en blanco",
  AddFromFile : "Añadir los formularios desde un fichero externo",
  AddFromClipboard : "Añadir los formularios que tengo copiados en el portapapeles",
  NodeTypeList : "¿De qué tipo son los formularios que va a añadir?",
  NodeTypeListBlank : "¿De qué tipo es el formulario que va a añadir?",
  SelectOne : "Elija un tipo...",
  Format : "¿Cuál es el formato del fichero que contiene los datos?",
  FormatMonet : "Monet",
  FormatCvn : "Curriculum Vitae Normalizado (CVN)",
  Options : "En caso de formularios duplicados...",
  OptionPreserveOriginal : "Conservar el original",
  OptionReplace : "Reemplazar el original",
  OptionDuplicate : "Duplicar los formularios",
  AddedAt : "Añadido usando una fuente externa el día",
  LoadingNodeType : "Cargando la lista de campos. Por favor, espere...",

  Error : {
    TypeRequired : "Seleccione un tipo de formulario",
    FileRequired: "Seleccione el fichero que contiene los formularios a añadir",
    DescriptionRequired : "La descripción no puede quedar vacía"
  }

};

Lang.Wizard = {
  Cancel : "Cancelar",
  Previous : "Anterior",
  Next : "Siguiente",
  Finish : "Finalizar"
};

Lang.DialogEditNodeDocument = {
  Step1Wizard : "1 de 3",
  Step1Title : "Editando el documento",
  Step1Description : "Descarga el documento para poder realizar la edición del documento en su ordenador",
  DownloadDocument : "descargar",
  
  Step2Wizard : "1 de 3",
  Step2Title : "Editando el documento",
  Step2Description : "Espere a que finalice la descarga del documento. Cuando la descarga finalice, podrá empezar a editarlo",
  DownloadAgain : "volver a descargar",
  
  Step3Wizard : "2 de 3",
  Step3Title : "Editando el documento",
  Step3Description : "Abra el documento e introduzca los cambios que desee. Cuando termine deberá subir el documento modificado",
  ReplaceDocument : "ya he modificado el documento",

  Step4Wizard : "3 de 3",
  Step4Title : "Editando el documento",
  Step4Description : "Indique la localización del documento modificado para guardar la última versión en el sistema",
  FinishEdition : "finalizar",
  
  Title : "Editar documento",
  CancelEdition : "cancelar",

  Error : {
    FileRequired: "Seleccione el documento de su ordenador que contiene la nueva versión"
  }

};

Lang.DialogGenerateReport = {
  Title : "Descargar...",
  Options : "¿Qué formularios deseas incluir?",
  OptionAll : "Incluir todos los formularios",
  OptionSelection : "Incluir solo los formularios seleccionados",
  Filters : "¿Quieres aplicar algún filtrado?",
  FilterNodeType : "Solo deseas los formularios del tipo:",
  FilterDates : "Los formularios creados en este rango de fechas:",
  NoNodeTypes : "No has seleccionado ningún tipo de formulario",
  SelectOne : "Seleccione un tipo de formulario",
  FromDate : "Desde",
  ToDate : "Hasta",
  AddSelectedNodesInfo : "(para que esta opción está activa deberá seleccionar al menos un formulario del curriculum)",

  Error : {
    DescriptionRequired : "La descripción no puede quedar vacía"
  }

};

Lang.DialogGenerateReportType = {
  Delete: "Eliminar"
};

Lang.DialogShareNode = {
  Title : "Enviar formulario",
  Users : "Usuarios seleccionados",
  NoUsers : "Actualmente no tiene ningún usuario seleccionado",
  ShowSearchUsers : "Mostrar buscador...",
  HideSearchUsers : "Ocultar buscador...",
  SearchUsersPanel : "Buscador",
  Description : "Notas (se le mostrará al usuario cuando vea el formulario compartido):",
  ExpireDate : "Enviar el formulario durante un tiempo determinado (un valor vacío indicará que no caduca)",
  ExpiresAt : "Desde hoy hasta el...",
  PanelInfo : "General",
  PanelUsers : "Usuarios con los que comparto",

  Error : {
    DescriptionRequired : "La descripción no puede quedar vacía"
  }

};

Lang.DialogShareNodeUser = {
  Delete: "Eliminar"
};

Lang.ViewNodeDetails = {
  Selection : "Tienes #count elementos seleccionados <a class='command' href='selectnodes(none)'>Quitar selección</a>"
};

Lang.ViewTask = {
  ViewingTaskTarget : "Está viendo el expediente asociado a la tarea <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  ViewingTaskInput : "Está viendo la petición que originó la tarea <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  ViewingTaskOutput : "Está viendo el resultado obtenido en la tarea <a class='command showtask' href='showtask(#{idtask})'><b>#{task}</b></a>",
  NoTeam : "No hay equipo de trabajo",

  DialogCreateTask : {
    NoTasks: "No existe la posibilidad de crear tareas"
  }
  
};

Lang.DialogSetTaskGoal = {
  Title : "Cambiar objetivo de la tarea",
  CurrentGoal : "Objetivo actual",
  WorkPlaceCombo : "Seleccione el nuevo objetivo",
  SelectOne : "Elija un objetivo...",
  NoGoal : "No se ha seleccionado ningún objetivo",

  Error : {
    GoalRequired : "Ha de seleccionar un objetivo de la lista"
  }
};

Lang.DialogEditTaskCheckPoints = {
  Title : "Editar puntos de control",
  CheckPoints : "Arrastre y suelte para reordenar los puntos de control",
  RootLabel : "Puntos de control",
  AddCheckPoint : "Añadir",

  Error : {
  }
};

Lang.ViewCube = {
  NoReportLabel : "Informe",
    
  DialogDeleteReport : {
    Title : "Eliminar un informe",
    Description : "¿Está seguro que desea eliminar el informe?"
  },

  DialogDeleteReports : {
    Title : "Eliminar informes seleccionados",
    Description : "¿Está seguro que desea eliminar los informes seleccionados?",
    NoReportsSelected : "No ha seleccionado ningún informe"
  }

};

Lang.ViewCubeReport = {
};

Lang.ViewTeam = {
    DialogDeleteDelegate : {
      Title : "Eliminar un delegado",
      Description : "¿Está seguro que desea eliminar el delegado?"
    },

    DialogDeleteDelegates : {
      Title : "Eliminar delegados seleccionados",
      Description : "¿Está seguro que desea eliminar los delegados seleccionados?"
    },

    DialogDeleteWorker : {
      Title : "Eliminar un trabajador",
      Description : "¿Está seguro que desea eliminar el trabajador?"
    },

    DialogDeleteWorkers : {
      Title : "Eliminar trabajadores seleccionados",
      Description : "¿Está seguro que desea eliminar los trabajadores seleccionados?"
    }
};

Lang.ViewerHelperToolbar = {
  Add : "Añadir",
  Download : "Descargar",
  Tools : "Herramientas",
  BackTask : "Volver a la tarea",
  BackNode : "Volver al elemento"
};

Lang.LayoutMainRight = {
  Title : "Panel derecho",
  TaskListTitle : "Tareas",
  HelperTitle : "Asistente",
  TrashTitle : "Papelera"
};

Lang.ViewerHelperSidebar = {
  Add : "Añadir",
  Tools : "Herramientas"
};

Lang.ViewerHelperPage = {
  Label : "Ayuda"
};

Lang.Editor = {

  MoreInfo : "[Más información]",
  Option : "Valor",
  Code : "Código",
  Empty : "No existen opciones",
  ShowHistory : "Ver valores recientes",
  HideHistory : "Ocultar valores recientes",
  FilterEmpty : "Introduzca aquí los términos para la búsqueda",
  FilterEmptyHistory : "Valores recientes. Introduzca aquí los términos para la búsqueda",
  Check : "Marcar",
  Uncheck : "Desmarcar",
  Up : "Subir",
  Down : "Bajar",

  Dialogs : {
    Other : {
      Title : "Añadir...",
      Description : "Por favor, indique el nuevo valor:"
    },
    FileUpload : {
      Uploading : "Subiendo el fichero. Por favor, espere...",
      UploadingFailed : "No se ha podido subir el fichero"
    },
    PictureUpload : {
      Processing : "Reconociendo la imagen. Por favor, espere...",
      Uploading : "Subiendo el archivo de imagen. Por favor, espere...",
      UploadingFailed : "No se ha podido subir el archivo de imagen"
    },
    Number : {
      Accepts: "Acepta ",
      Decimals: " decimales",
      MultipleDecimals: "Acepta tantos decimales como quiera",
      Increments: "Aumentará y disminuirá en #count#",
      Range : "Acepta valores entre #min# y #max#",
      Equivalences : "Valor en otras unidades: #equivalences#"
    },
    Text : {
      Length : "Acepta valores con una longitud entre #min# y #max#",
      Undefined : "la longitud máxima que se quiera"
    }
  },

  Templates : {
    DialogListItem : '<li class="element"><table width="100%"><tr><td width="1px"><input name="#{id}" style="margin-top:4px;*margin-top:2px;" type="checkbox"/></td><td><label for="#{id}" style="cursor:pointer;display:block;padding:4px;*padding-top:5px;">#{title}</div></td><td width="35px"><div class="elementoptions"><a class="delete"><img src=#{ImagesPath}/s.gif?a=1" class="trigger" alt="eliminar" title="eliminar"/></a><a class="move"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"/></a></div></td></tr></table></li>'
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
  
  MessageBoolean : "<div>Flechas de cursor arriba y abajo para seleccionar el valor</div><div>[intro] para aceptar el valor y continuar la edición</div><div>[espacio] para marcar y desmarcar</div>",
  MessageCheck : "<div class='recents'>Flechas de cursor arriba y abajo para seleccionar un valor de la lista de valores recientes</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageDate : "<div>Flechas de cursor arriba y abajo para seleccionar el valor</div><div>Flechas de cursor izquierda y derecha para seleccionar dia/mes/año</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageFile : "<div>Pulse en examinar para elegir un fichero de su equipo</div>",
  MessageLink : "<div>Escribe \"+\" en el campo de búsqueda para mostrar los valores recientes</div><div>Flechas de cursor arriba y abajo para seleccionar un valor de la lista</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageList : "<div>Para reordenar la lista... arrastra un campo y suéltalo en la posición deseada</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageNumber : "<div>Flechas de cursor arriba y abajo para aumentar y disminuir el valor respectivamente</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageFormat : "<div>Rellene el campo tal cual indica el formato indicado más abajo</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessagePicture : "<div>Pulse en examinar para elegir un archivo de imagen de su equipo</div><div>Una vez elegido el archivo de imagen, pulse en aceptar para seleccionar la región que le interesa</div>",
  MessageSelect : "<div>Escribe \"+\" en el campo de búsqueda para mostrar los valores recientes</div><div>Flechas de cursor arriba y abajo para seleccionar un valor de la lista</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageText : "<div class='recents'>Flechas de cursor arriba y abajo para seleccionar un valor de la lista de valores recientes</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageThesaurus : "<div>Escribe \"+\" en el campo de búsqueda para mostrar los valores recientes</div><div>Flechas de cursor arriba y abajo para seleccionar un valor de la lista</div><div>[intro] para aceptar el valor y continuar la edición</div>",
  MessageSerial : "<div>Este campo no es editable</div>",
  MessageLocation : "<div>Seleccione un punto, línea o polígono en el mapa para indicar la ubicación del elemento</div>",
  
  DateWrong : "ERROR. La fecha no es correcta",
  NumberWrong : "ERROR. El valor introducido no es un número",
  NumberRangeWrong : "ERROR. El valor introducido no cumple con el rango de valores establecido para el número",
  FormatWrong : "ERROR. Campo con datos incorrectos",
  LengthWrong : "ERROR. Longitud de texto no permitida",

  Edition: "Edición",
  Actions: "Acciones",
  LoadDefaultValue: "Rellenar con el valor por defecto",
  Help: "Ayuda",
  AddDefaultValue: "Establecer valor actual como valor por defecto",
  ClearField: "Borrar",
  Increment : "Aumentar",
  Decrement : "Disminuir",
  Check : "Marcar",
  Download : "Descargar",
  Add : "Añadir campo",
  DeleteSelected : "Borrar campos seleccionados",
  Select: "Seleccionar",
  SelectOther: "Añadir...",
  CropMessage : "Recorte el trozo de imagen que le interese y pulse en el botón Aceptar",
  PreviousYear : "Año anterior",
  NextYear : "Año siguiente",
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
};

Lang.ViewerHelperPreview = {
  Title : "Vista previa"
};

Lang.ViewerHelperObservers = {
  Editing: "editando",
  Label : "Otros usuarios que están viendo este elemento"
};

Lang.ViewerHelperRevisionList = {
  Title : "Versiones disponibles",
  CurrentRevision : "Actual",
  NoRevisions : "No existen versiones",
  Revisions : "versiones",
  RevisionAt : ""
};

Lang.ViewerHelperMap = {
  Edition : "Edición",
  Navigation : "Navegación"
};

Lang.ViewerHelperThesaurus = {
  TermExists : "ya existe",
  ThesaurusList : "Tesauros",
  TermList : "Términos",
  Code: "Código",
  Label: "Etiqueta",
  Type: "Tipo",
  Term : "término",
  SuperTerm : "Contiene otros términos",
  Category : "categoría",
  Enabled: "Habilitado",
  Enable: "Habilitar",
  Disable: "Deshabilitar",
  Delete: "Quitar",
  AddTo: "Añadir a ",
  Add: "añadir",
  Tags : "Marcadores",
  NoTags : "no tiene marcadores",
  Publish : "Publicar",
  NewTerms : "Términos nuevos",
  Select : "seleccionar: ",
  SelectAll : "todos",
  SelectNone : "ninguno",
  NewTermsDescription : "Ha añadido recientemente nuevos términos pero no han sido publicados"
};

Lang.ViewerHelperRole = {
  DefinitionList : "Roles",
  Add : "Editar",
  ButtonAdd : "Añadir rol",
  Save : "Editar",
  ButtonSave : "Guardar",
  Username : "Usuario",
  BeginDate : "Fecha de inicio",
  Expires : "El rol caduca",
  ExpireDate : "Fecha de caducidad",
  DefinitionType : "Tipo",
  Searching : 'Buscando...',
  SelectUser : 'Seleccione un usuario',
  SelectBeginDate : 'Seleccione una fecha válida',
  User : "Usuario",
  Email : "Dirección de correo",
  SelectExpireDate : 'La fecha seleccionada es menor que la fecha de inicio'
};

Lang.ViewerHelperList = {
};

Lang.ViewTaskList = {

  State : {
    New : "Nueva",
    Pending : "Pendiente",
    Waiting : "En espera",
    Expired : "Expirada",
    Finished : "Finalizada",
    Aborted : "Abortada",
    Failure : "Alerta",
    Undefined : "Sin definir"
  },

  DialogAbortTask : {
    Title : "Abortar tarea",
    Description : "¿Está seguro que desea abortar la tarea '<b>#{task}</b>'?"
  },

  DialogPriorizeTask : {
    Title : "Priorizar la tarea",
    Description : "¿Está seguro que desea priorizar la tarea '<b>#{task}</b>'?"
  },
  
  DialogEnrolTask : {
    Title : "Unirse a la tarea",
    Description : "¿Está seguro que desea unirse a la tarea '<b>#{task}</b>'?"
  },

  DialogEnrolTaskBeforeAction : {
    Title : "Unirse a la tarea",
    Description : "Para poder realizar la acción debe pertenecer al equipo de <b>#{task}</b> ¿Quiere unirse al equipo?"
  },

  DialogUnEnrolTask : {
    Title : "Abandonar la tarea",
    Description : "¿Está seguro que desea abandonar la tarea '<b>#{task}</b>'?"
  },
  
  DialogSolveTaskLock : {
    Title : "Responder a #{title}",
    Description : "¿Está seguro que desea responder '<b>#{lock}</b>'?"
  },
  
  DialogDoTaskLockAction : {
    Title : "Responder a #{title}",
    Description : "¿Está seguro que desea responder '<b>#{lock}</b>'?"
  },

  DialogCancelTaskLock : {
    Title : "Cancelar",
    Description : "¿Está seguro que desea cancelar '<b>#{lock}</b>'?"
  }

};

Lang.ViewTrash = {
  NoNodes: "La papelera está vacía",

  DialogEmptyTrash : {
    Title : "Vaciar la papelera",
    Description : "¿Está seguro que desea vaciar la papelera?"
  },

  DialogRecoverNodeFromTrash : {
    Title : "Recuperar elemento",
    Description : "¿Está seguro que desea recuperar el elemento?"
  },

  DialogRecoverNodesFromTrash : {
    Title : "Recuperar elementos",
    Description : "¿Está seguro que desea recuperar los elementos seleccionados?"
  }

};

Lang.ViewThesaurusList = {
  NoTags : "(no tiene marcadores)"
};

Lang.ViewRoleList = {
};

Lang.ViewNotificationList = {
};

Lang.LayoutFooter = {
  Title : ""
};

Lang.Widget = {

  EmptyNode: "No se ha definido el elemento",

  Boolean : {
    Yes : "Si",
    No : "No"
  },
  
  Table : {
    ElementLabel : "Sin etiqueta"
  },

  Templates : {
    Observer : '<div class="observer"><span></span> editando...&nbsp;<a class="command" href="requestnodefieldcontrol()">tomar el control</a></div>',
    WidgetOptions : '<div class="options"><a class="clearvalue" alt="borrar" title="borrar"/></div>',
    Loading : '<div class="wloading"><img src="#{ImagesPath}/icons/loading.gif" alt="Cargando..." title="Cargando..."/><span>Cargando...</span></div>',
    ListTableElement : '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"></a></div></td><td class="label">#{label}</td></tr></table></li>',
    ListElement : '<li id="#{id}" class="element"><table width="100%"><tr><td width="24px"><div class="elementoptions"><a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"></a></div></td><td><div class="elementwidget"><a class="delete" alt="borrar" title="borrar">&nbsp;</a>#{widget}</div></td></tr></table></li>',
    SectionExtensibleOptions : '<div class="options extensible"><a class="expand"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Ver todos los campos" title="Ver todos los campos"/><span title="Ver todos los campos">Más</span></a><a class="collapse"><img src="#{ImagesPath}/s.gif" class="trigger" alt="Ver solo los campos mínimos" title="Ver solo los campos mínimos"/><span title="Ver solo los campos mínimos">Menos</span></a></div>',
    SectionConditionalOptions : '<span class="options conditional"><input name="sectiongroupoptions_#{id}" id="sectiongroupoption_#{id}_yes" class="option" style="margin-left:10px;" type="radio" value="yes"/><label style="display:inline;" for="sectiongroupoption_#{id}_yes">Si</label><input id="sectiongroupoption_#{id}_no" name="sectiongroupoptions_#{id}" class="option" style="margin-left:10px;" type="radio" value="no" checked /><label style="display:inline;" for="sectiongroupoption_#{id}_no">No</label></span>',
    LinkNodeBox : '<div class="nodecontainer"></div>',
    HistoryStoreUrl : '#{api}?op=loadhistoryterms&code=#{code}&list=#{list}',
    ThesaurusSourceUrl : '#{api}?op=loadthesaurusterms&code=#{code}&list=#{list}&flatten=#{flatten}&depth=#{depth}&from=#{from}&filters=#{filters}',
    ThesaurusIndexUrl : '#{api}?op=loadthesaurusterms&code=#{code}&list=#{list}&flatten=#{flatten}&depth=#{depth}&from=#{from}&filters=#{filters}',
    DataLinkUrl : '#{api}?op=loadlinknodeitems&domain=#{domain}&code=#{code}&list=#{list}&filters=#{filters}',
    AttributeListUrl : '/#{api}?op=loadattributes&idnode=#{idnode}',
    Required : '<img class="icon wrequired" src="#{ImagesPath}/icons/required.gif" title="Campo obligatorio incompleto" alt="*" />',
    Upload : '<img src="#{ImagesPath}/s.gif" class="#{cls}" alt="#{message}" title="#{message}"><span>#{message}</span>',
    FileOnlineMenu : '<div class="onlinemenu"><a class="download" alt="Descargar documento" title="Descargar documento">Descargar documento</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>',
    ImageOnlineMenu : '<div class="onlinemenu"><a class="download" alt="Descargar imagen" title="Descargar imagen">Descargar imagen</a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>',
    LinkOnlineMenu : '<div class="onlinemenu">Ir al elemento: <a class="hiperlink"></a>&nbsp;-&nbsp;<a class="clearvalue" alt="Borrar" title="Borrar">Borrar</a></div>'
  }

};

WidgetObserverTemplate = new Template(Lang.Widget.Templates.Observer);
WidgetLoadingTemplate = new Template(Lang.Widget.Templates.Loading);
WidgetListTableElementTemplate = new Template(Lang.Widget.Templates.ListTableElement);
WidgetListElementTemplate = new Template(Lang.Widget.Templates.ListElement);
WidgetSectionExtensibleOptionsTemplate = new Template(Lang.Widget.Templates.SectionExtensibleOptions);
WidgetSectionConditionalOptionsTemplate = new Template(Lang.Widget.Templates.SectionConditionalOptions);
WidgetLinkNodeBoxTemplate = new Template(Lang.Widget.Templates.LinkNodeBox);  

WidgetTemplateHistoryStoreUrl = new Template(Lang.Widget.Templates.HistoryStoreUrl);
WidgetTemplateThesaurusSourceUrl = new Template(Lang.Widget.Templates.ThesaurusSourceUrl);
WidgetTemplateThesaurusIndexUrl = new Template(Lang.Widget.Templates.ThesaurusIndexUrl);
WidgetTemplateDataLinkUrl = new Template(Lang.Widget.Templates.DataLinkUrl);
WidgetTemplateAttributeListUrl = new Template(Lang.Widget.Templates.AttributeListUrl);

WidgetOptionsTemplate = new Template(Lang.Widget.Templates.WidgetOptions);
WidgetTemplateUpload = new Template(Lang.Widget.Templates.Upload);
WidgetFileOnlineMenuTemplate = new Template(Lang.Widget.Templates.FileOnlineMenu);
WidgetImageOnlineMenuTemplate = new Template(Lang.Widget.Templates.ImageOnlineMenu);
WidgetLinkOnlineMenuTemplate = new Template(Lang.Widget.Templates.LinkOnlineMenu);

Lang.Decorator = {

  NodeMark : { // Be careful, case sensitive
    recoveredfromtrash : "recuperado de la papelera",
    added : "añadido recientemente",
    copied : "agregado recientemente"
  }

};

