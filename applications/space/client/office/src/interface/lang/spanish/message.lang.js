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
