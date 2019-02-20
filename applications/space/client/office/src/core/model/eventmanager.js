EventManager = new Object;
EventManager.aListeners = new Array();
EventManager.bDoNotifications = true;

EventManager.CREATE_NODE = "createnode";
EventManager.CREATE_TASK = "createtask";
EventManager.BEFORE_OPEN_NODE = "beforeopennode";
EventManager.OPEN_NODE = "opennode";
EventManager.FOCUS_NODE = "focusnode";
EventManager.BLUR_NODE = "blurnode";
EventManager.FOCUS_NODE_VIEW = "focusnodeview";
EventManager.OPEN_TASK = "opentask";
EventManager.FOCUS_TASK_VIEW = "focustaskview";
EventManager.CLOSE_NODE = "closenode";
EventManager.CLOSE_TASK = "closetask";
EventManager.SAVE_NODE = "savenode";
EventManager.SAVE_TASK = "savetask";
EventManager.POST_EXECUTE_NODE_COMMAND = "postexecutenodecommand";
EventManager.FOCUS_FIELD = "focusfield";
EventManager.BLUR_FIELD = "blurfield";
EventManager.CHANGE_FIELD = "changefield";
EventManager.LINK_FIELD = "linkfield";
EventManager.ADD_FIELD = "addfield";
EventManager.REMOVE_FIELD = "removefield";

EventManager.addListener = function (Listener) {
  EventManager.aListeners.push(Listener);
};

EventManager.enableNotifications = function () {
  EventManager.bDoNotifications = true;
};

EventManager.disableNotifications = function () {
  EventManager.bDoNotifications = false;
};

EventManager.notify = function (EventType, Sender) {

  if (!EventManager.bDoNotifications) return;

  for (var i = 0; i < EventManager.aListeners.length; i++) {
    if (EventType == EventManager.CREATE_NODE && EventManager.aListeners[i].nodeCreated) EventManager.aListeners[i].nodeCreated(Sender);
    else if (EventType == EventManager.BEFORE_OPEN_NODE && EventManager.aListeners[i].nodeBeforeOpened) EventManager.aListeners[i].nodeBeforeOpened(Sender);
    else if (EventType == EventManager.OPEN_NODE && EventManager.aListeners[i].nodeOpened) EventManager.aListeners[i].nodeOpened(Sender);
    else if (EventType == EventManager.FOCUS_NODE && EventManager.aListeners[i].nodeFocused) EventManager.aListeners[i].nodeFocused(Sender);
    else if (EventType == EventManager.FOCUS_NODE_VIEW && EventManager.aListeners[i].nodeViewFocused) EventManager.aListeners[i].nodeViewFocused(Sender);
    else if (EventType == EventManager.CLOSE_NODE && EventManager.aListeners[i].nodeClosed) EventManager.aListeners[i].nodeClosed(Sender);
    else if (EventType == EventManager.SAVE_NODE && EventManager.aListeners[i].nodeSaved) EventManager.aListeners[i].nodeSaved(Sender);
    else if (EventType == EventManager.FOCUS_FIELD && EventManager.aListeners[i].nodeFieldFocused) EventManager.aListeners[i].nodeFieldFocused(Sender);
    else if (EventType == EventManager.BLUR_FIELD && EventManager.aListeners[i].nodeFieldBlur) EventManager.aListeners[i].nodeFieldBlur(Sender);
    else if (EventType == EventManager.CHANGE_FIELD && EventManager.aListeners[i].nodeFieldChanged) EventManager.aListeners[i].nodeFieldChanged(Sender);
    else if (EventType == EventManager.POST_EXECUTE_NODE_COMMAND && EventManager.aListeners[i].nodeExecutePostCommand) EventManager.aListeners[i].nodeExecutePostCommand(Sender);
    else if (EventType == EventManager.CREATE_TASK && EventManager.aListeners[i].taskCreated) EventManager.aListeners[i].taskCreated(Sender);
    else if (EventType == EventManager.OPEN_TASK && EventManager.aListeners[i].taskOpened) EventManager.aListeners[i].taskOpened(Sender);
    else if (EventType == EventManager.FOCUS_TASK_VIEW && EventManager.aListeners[i].taskViewFocused) EventManager.aListeners[i].taskViewFocused(Sender);
    else if (EventType == EventManager.CLOSE_TASK && EventManager.aListeners[i].taskClosed) EventManager.aListeners[i].taskClosed(Sender);
    else if (EventType == EventManager.SAVE_TASK && EventManager.aListeners[i].taskSaved) EventManager.aListeners[i].taskSaved(Sender);
  }

};