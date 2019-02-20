TaskSearchRequest = new Object();

TaskSearchRequest.ANY_TYPE = "AnyType";
TaskSearchRequest.SPECIFIC_TYPE = "SpecificType";

TaskSearchRequest.ANY_SITUATION = "AnySituation";
TaskSearchRequest.SITUATIONS = { ANY_SITUATION: TaskSearchRequest.ANY_SITUATION,
  DOABLE_SITUATION: "doable",
  DELAYED_SITUATION: "delayed",
  JOB_ORDERED_SITUATION: "jobordered",
  FINISHED_SITUATION: "finished",
  ABORTED_SITUATION: "aborted" };

TaskSearchRequest.ANY_STATE = "AnyState";
TaskSearchRequest.SPECIFIC_STATE = "SpecificState";
