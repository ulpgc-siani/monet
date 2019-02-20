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
