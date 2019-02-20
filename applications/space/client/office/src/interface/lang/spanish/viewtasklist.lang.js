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