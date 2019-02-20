package org.monet.space.office.presentation.user.renders;


public class ServiceViewRender extends ProcessViewRender {

	public ServiceViewRender() {
		super();
	}

/*
  protected String initStateViewFormLock(FormLock lock) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String idForm = lock.getFormId();
    Node form;
    OfficeRender render;
    String renderResult = "";

    if ((idForm != null) && (!idForm.isEmpty())) {
      form = this.renderLink.loadNode(idForm);
      render = this.rendersFactory.get(form, "edit.html", this.renderLink);
      render.setParameter("view", lock.getDefinition().getUse().getWithView().getValue());
      renderResult = render.getOutput();
    }

    map.put("label", lock.getLabel(Language.getCurrent()));
    map.put("render(node.form)", renderResult);
    map.put("idTask", this.task.getId());
    map.put("idLock", lock.getId());

    return block("content.state$formLock", map);
  }

  protected String initStateViewOutsourcing() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    StringBuilder outsourcingsBuilder = new StringBuilder();
    Process process = this.task.getProcess();

    for (ServiceProviderInstance serviceProviderInstance : process.getOutsourcingServiceProviderInstances()) {
      String stateMessage = "";

      String requestKey = this.dictionary.getServiceProviderDefinition(serviceProviderInstance.getServiceProviderCode()).getRequest().getValue();
      String requestDefinitionLabel = this.dictionary.getDocumentDefinition(requestKey).getLabelString();
      String responseKey = this.dictionary.getServiceProviderDefinition(serviceProviderInstance.getServiceProviderCode()).getResponse().getValue();
      String responseDefinitionLabel = this.dictionary.getDocumentDefinition(responseKey).getLabelString();

      Node requestDocument = serviceProviderInstance.getRequestDocumentId() != null ? this.renderLink.loadNode(serviceProviderInstance.getRequestDocumentId()) : null;
      Node responseDocument = serviceProviderInstance.getResponseDocumentId() != null ? this.renderLink.loadNode(serviceProviderInstance.getResponseDocumentId()) : null;

      switch (serviceProviderInstance.getState()) {
        case ServiceProviderInstance.INITIAL:
          map.put("requestLabel", requestDefinitionLabel);
          stateMessage = block("content.state$Outsourcing$initial", map);
          break;
        case ServiceProviderInstance.REQUEST_BUILT:
          map.put("requestLabel", requestDocument.getLabel());
          stateMessage = block("content.state$Outsourcing$built", map);
          break;
        case ServiceProviderInstance.REQUEST_CONSOLIDATED:
          map.put("requestLabel", requestDocument.getLabel());
          stateMessage = block("content.state$Outsourcing$consolidated", map);
          break;
        case ServiceProviderInstance.REQUEST_SENT:
          map.put("requestId", requestDocument.getId());
          map.put("requestLabel", requestDocument.getLabel());
          map.put("providerLabel", serviceProviderInstance.getProviderLabel());
          map.put("createDate", serviceProviderInstance.getLastTryDate());
          map.put("responseLabel", responseDefinitionLabel);
          stateMessage = block("content.state$Outsourcing$sent", map);
          break;
        case ServiceProviderInstance.ERROR_BUILDING_REQUEST:
          map.put("requestLabel", requestDefinitionLabel);
          map.put("createDate", serviceProviderInstance.getLastTryDate());
          stateMessage = block("content.state$Outsourcing$failure_building", map);
          break;
        case ServiceProviderInstance.ERROR_CONSOLIDATING_REQUEST:
          map.put("requestLabel", requestDocument.getLabel());
          map.put("createDate", serviceProviderInstance.getLastTryDate());
          stateMessage = block("content.state$Outsourcing$failure_consolidating", map);
          break;
        case ServiceProviderInstance.ERROR_SENDING_REQUEST:
          map.put("requestId", requestDocument.getId());
          map.put("requestLabel", requestDocument.getLabel());
          map.put("providerLabel", serviceProviderInstance.getProviderLabel());
          map.put("createDate", serviceProviderInstance.getLastTryDate());
          stateMessage = block("content.state$Outsourcing$failure_sending", map);
          break;
        case ServiceProviderInstance.COMPLETED:
          map.put("requestId", requestDocument.getId());
          map.put("requestLabel", requestDocument.getLabel());
          map.put("providerLabel", serviceProviderInstance.getProviderLabel());
          map.put("responseId", responseDocument.getId());
          map.put("responseLabel", responseDocument.getLabel());
          stateMessage = block("content.state$Outsourcing$completed", map);
          break;
      }

      map.put("taskId", this.task.getId());

      String retry = "";
      if (serviceProviderInstance.getState() < 0) {
        map.put("code", serviceProviderInstance.getOutsourcingCode());
        retry = block("content.state$Outsourcing$action.retry", map);
      }

      map.put("outsourcingLabel", this.language.getModelResource(this.task.getProcess().getWorkmap().getOutsource(serviceProviderInstance.getOutsourcingCode()).getLabel()));

      if (serviceProviderInstance.getProviderId() == null) {
        map.put("message", "");
        StringBuilder serviceProviders = new StringBuilder();
        for (ServiceProvider provider : this.renderLink.loadServiceProviders(serviceProviderInstance.getServiceProviderCode())) {
          map.put("id", provider.getId());
          map.put("label", provider.getLabel());
          map.put("providerInstanceId", serviceProviderInstance.getId());
          serviceProviders.append(block("content.state$Outsourcing$startedAndRetryOrSeveralProviders.other$retry$serviceLink", map));
        }
        map.put("serviceProviders", serviceProviders.toString());
        map.put("requestLabel", requestDefinitionLabel);

        outsourcingsBuilder.append(block("content.state$Outsourcing$startedAndRetryOrSeveralProviders.other$retry", map));
      } else {
        map.put("message", stateMessage);
        map.put("retry", retry);
        outsourcingsBuilder.append(block("content.state$Outsourcing$startedAndRetryOrSeveralProviders.other", map));
      }
    }

    map.put("startedAndRetryOrSeveralProviders", outsourcingsBuilder.toString());

    return block("content.state$Outsourcing", map);
  }

  protected String initStateViewServiceLock(ServiceLock lock) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    StringBuilder waitingOutsourcings = new StringBuilder();

    WorkmapProperty workmapDefinition = this.task.getDefinition().getWorkmap();

    for (WaitProperty waitDefinition : lock.getDefinition().getWaitList()) {
      map.put("outsourceLabel", this.language.getModelResource(workmapDefinition.getOutsource(waitDefinition.getFor().getValue()).getLabel()));
      waitingOutsourcings.append(block("content.state$serviceLock$waiting_outsource", map));
    }

    map.put("waiting_outsourcings", waitingOutsourcings.toString());

    return block("content.state$serviceLock", map);
  }

  protected String initStateViewTimerLock(TimerLock lock) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String actions = "";

    if (lock.canExtend()) {
      map.put("taskId", this.task.getId());
      map.put("id", lock.getId());
      actions += block("content.state$timerLock$action.extend", map);
      map.clear();
    }

    map.put("timeLeft", String.valueOf(lock.getTimeLeft()));
    map.put("actions", actions);

    return block("content.state$timerLock", map);
  }

  protected String initStateViewSyncLock(SyncLock lock) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<LockAction> actionsList = lock.getActions(Language.getCurrent());
    String actions = "";

    for (LockAction action : actionsList) {
      map.put("parameters", action.getParameters());
      map.put("label", action.getLabel());
      actions += block("content.state$syncLock$action", map);
      map.clear();
    }

    map.put("count", String.valueOf(actionsList.size()));
    map.put("actions", actions);

    return block("content.state$syncLock", map);
  }

  protected String initStateViewDecisionLock(DecisionLock lock) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<LockAction> actionsList = lock.getActions(Language.getCurrent());
    String actions = "";

    for (LockAction action : actionsList) {
      map.put("taskId", this.task.getId());
      map.put("lockId", lock.getId());
      map.put("parameters", action.getParameters());
      map.put("label", action.getLabel());
      actions += block("content.state$decisionLock$action", map);
      map.clear();
    }

    map.put("actions", actions);

    return block("content.state$decisionLock", map);
  }

  protected String initStateView() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String locks = "";
    String date = "";
    String history = "", currentState = "";
    String state = this.task.getState();

    String currentLabel = null;
    map.put("idTask", this.task.getId());
    map.put("state", state);

    ServiceInstance serviceInstance = null;
    if (this.task.getDefinition().getExecution() != null) {
      serviceInstance = this.task.getServiceInstance();
    }

    map.put("locks", "");
    map.put("retryFinish", "");
    if (!this.task.isFinished() && !this.task.isAborted()) {
      map.put("outsourcing", this.initStateViewOutsourcing());

      for (WorkLock<?> lock : this.task.getProcess().getLocks()) {
        if (lock.isForm())
          locks += this.initStateViewFormLock((FormLock) lock);
        else if (lock.isService())
          locks += this.initStateViewServiceLock((ServiceLock) lock);
        else if (lock.isTimer())
          locks += this.initStateViewTimerLock((TimerLock) lock);
        else if (lock.isSync())
          locks += this.initStateViewSyncLock((SyncLock) lock);
        else if (lock.isDecision())
          locks += this.initStateViewDecisionLock((DecisionLock) lock);
        date = LibraryDate.getDateAndTimeString(lock.getInternalCreateDate(), Language.getCurrent(), LibraryDate.Format.INTERNAL, true, "/");
      }
      map.put("locks", locks);
      currentLabel = this.definition.getWorkmapDeclarationLabel(this.task.getProcess().getCurrentPosition());
      date = LibraryDate.getDateAndTimeString(this.task.getInternalUpdateDate(), Language.getCurrent(), LibraryDate.Format.INTERNAL, true, "/");
    } else if (serviceInstance != null && serviceInstance.getResponseState() == State.Error) {
      String retryFinish = this.initStateViewRetryFinish(serviceInstance);
      map.put("retryFinish", retryFinish);
      currentLabel = block("content.state$label.error", new HashMap<String, Object>());
      date = LibraryDate.getDateAndTimeString(this.task.getInternalUpdateDate(), Language.getCurrent(), LibraryDate.Format.INTERNAL, true, "/");
    } else {
      currentLabel = "";
      date = "";
    }

    map.put("stateLabel", this.getTaskStateLabel(state));

    if (!currentLabel.isEmpty()) {
      map.put("currentLabel", currentLabel);
      map.put("date", date);
      currentState = block("content.state$current", map);
    } else
      currentState = block("content.state$current.empty", map);

    history = block("content.state$history", map);

    map.clear();
    map.put("current", currentState);
    map.put("history", history);

    return block("content.state", map);
  }

  protected String initStateViewRetryFinish(ServiceInstance serviceInstance) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("createDate", serviceInstance.getResponseSentDate());
    map.put("taskId", this.task.getId());

    return block("content.state$retryFinish", map);
  }
*/

/*
  protected String initTeamView() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String actions = "", users = "";
    UserList userList = this.renderLink.loadUsers(this.task.getEnrolmentsIdUsers());

    if (!this.task.isFinished()) {
      map.put("taskId", this.task.getId());
      actions += block("content.team$actions", map);
    }

    map.put("actions", actions);

    for (User user : userList.get().values()) {
      HashMap<String, Object> localMap = new HashMap<String, Object>();
      localMap.put("id", user.getId());
      localMap.put("fullname", user.getInfo().getFullname());
      users += block("content.team$user", localMap);
      localMap.clear();
    }
    map.put("userList", users);

    return block("content.team", map);
  }

  protected String initHistoryView() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    TaskFactPage taskHistoryPage = (TaskFactPage) this.getParameter("historyPage");
    WorkmapProperty workmapDefinition = this.definition.getWorkmap();
    String facts = "";

    for (Fact fact : taskHistoryPage.getEntries()) {
      WorklineProperty worklineDefinition = workmapDefinition.getWorkline(fact.getWorklineCode());
      WorkstopProperty workstopDefinition = workmapDefinition.getWorkstop(fact.getWorkstopCode());
      String workstopLabel = null;
      String workplaceLabel = null;
      String extraDataContent = "";

      if (workstopDefinition != null) {
        WorkplaceProperty workplaceDeclaration = workmapDefinition.getWorkplace(workstopDefinition.getWorkplace().getValue());
        workstopLabel = workstopDefinition.getLabelString();
        workplaceLabel = workplaceDeclaration.getLabelString();
      }

      if (fact.getType().equals(WorkLock.TYPE_SERVICE)) {
        ServiceFact extraData = (ServiceFact) fact.getExtraData();
        if (extraData.getResponseDocumentId() != null) {
          map.put("requestDocumentId", extraData.getRequestDocumentId());
          map.put("requestDocumentLabel", extraData.getRequestDocument().getLabel());
          map.put("requestProvider", extraData.getProvider());
          map.put("requestDocumentCreateDate", LibraryDate.getDateAndTimeString(extraData.getRequestDocument().getReference().getCreateDate().getValue(), Language.getCurrent(), LibraryDate.Format.INTERNAL, true, "/"));
        }
        String response;
        if (extraData.getResponseDocumentId() != null) {
          map.put("responseDocumentId", extraData.getResponseDocumentId());
          map.put("responseDocumentLabel", extraData.getResponseDocument().getLabel());
          map.put("responseDocumentCreateDate", LibraryDate.getDateAndTimeString(extraData.getResponseDocument().getReference().getCreateDate().getValue(), Language.getCurrent(), LibraryDate.Format.INTERNAL, true, "/"));
          response = block("content.history$fact$extraData.serviceUse.response", map);
        } else {
          response = "";
        }
        map.put("response", response);
        extraDataContent = block("content.history$fact$extraData.serviceUse", map);
        map.clear();
      } else if (fact.getType().equals(WorkLock.TYPE_FORM)) {
        FormFact extraData = (FormFact) fact.getExtraData();
        map.put("formContent", extraData.getFormContent());
        extraDataContent = block("content.history$fact$extraData.form", map);
        map.clear();
      }

      map.put("createDate", fact.getCreateDate());
      map.put("workplaceLabel", workplaceLabel);
      map.put("worklineResult", worklineDefinition != null ? worklineDefinition.getResultString() : null);
      map.put("workstopLabel", (workstopLabel != null) ? workstopLabel : "");

      if (fact.getType().equals(WorkLock.TYPE_NONE)) {
        String linkBlock = "";
        CustomFactExtraData extraData = (CustomFactExtraData) fact.getExtraData();
        map.put("worklineResult", extraData.getTitle());
        map.put("messageText", extraData.getText());

        Link link = extraData.getLink();
        if (link != null) {
          map.put("linkTargetId", link.getId());

          switch (link.getType()) {
            case NODE:
              map.put("linkTarget", block("content.history$fact$extraData.custom.link.showNode", map));
              break;
            case TASK:
              map.put("linkTarget", block("content.history$fact$extraData.custom.link.showTask", map));
              break;
          }
          map.put("linkLabel", link.getLabel());
          linkBlock = block("content.history$fact$extraData.custom.link", map);
        }
        map.put("link", linkBlock);

        extraDataContent = block("content.history$fact$extraData.custom", map);
      }

      map.put("extraData", extraDataContent);

      facts += block("content.history$fact", map);
      map.clear();
    }

    map.put("facts", facts);

    return block("content.history", map);
  }
*/

	@Override
	protected void init() {
		loadCanvas("view.task.service");
		super.init();
	}

}
