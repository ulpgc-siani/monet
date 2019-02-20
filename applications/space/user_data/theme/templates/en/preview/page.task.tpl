<div class="task page ::state:: ::id::::finished| finished::::aborted| aborted::">
  <div class="controlinfo">
    <div class="idtask">::id::</div>
    <div class="code">::code::</div>
    <div class="tpl refresh ::id::">preview.html?mode=page::from|&from=*::</div>
    <div class="mode">edition</div>
    <div class="command onload">loadmoretaskhistory(::id::)</div>
  </div>
  
  <div class="def configuration">
    var configuration = new Object();
    configuration.toolbar = new Object();
    configuration.toolbar.operationSet = new Array();
    configuration.sidebar = new Object();
    configuration.sidebar.operationSet = new Array();
    ::operations::
  </div>

  ::header::
  <div class="content">::tabs::</div>
 
</div>

@type.activity
Activity

@type.service
Service

@type.job
Job

@operation.close
configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Close", parameters: {command:"closetask(::idTask::)"}});

@operation.assign
configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Assign...", parameters: {command:"settaskowner(::idTask::)"}});

@operation.refresh
configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Update", parameters: {command:"refreshtask(::idTask::)"}});

@operation$abort
configuration.toolbar.operationSet.push({type:"TOOL", label: "Abort...", parameters: {command:"aborttask(::idTask::)"}});

@header
<div class="properties">
  ::breadcrumbs::
  <div class="info">
    <table style="width:100%">
      <tr>
        <td class="header">
		      <div class='label' style='margin-top\:3px;'><img src='::themeSource::&path=_images/s.gif' alt='::typeLabel::' class='type ::type::'/><span>::label::</span></div>
		      <div class='description'>::definitionLabel::. ::description::</div>
		      ::owner::
		      ::sender::
		      <div class='dates'>Created&nbsp;<span class='function'>formatdatetime(::createDate::)</span>. Updated&nbsp;<span class='function'>formatdatetime(::updateDate::)</span></div>
          <table class="toolbar"><tr>::operations::</tr></table>
        </td>
		    <td class='charts'>
		      <img src='::chartSource::&id=::id::&random=::random::'/>
		    </td>
		    <td class='flags'>
		      <a class='command urgent ::urgentStyle| active::' title='Set task priority: urgent/not urgent' href='toggletaskurgency(::id::)'></a>
		    </td>
      </tr>
    </table>
  </div>
</div>

@header$breadcrumbs.tasktray
<div class="breadcrumbs"><a class="command" href="showhome()">Home</a>&nbsp;-&nbsp;<a class="command" href="showtasklist(tasktray)">My tasks</a>&nbsp;-&nbsp;<span>::label::</span></div>

@header$breadcrumbs.taskboard
<div class="breadcrumbs"><a class="command" href="showhome()">Home</a>&nbsp;-&nbsp;<a class="command" href="showtasklist(taskboard)">Task board</a>&nbsp;-&nbsp;<span>::label::</span></div>

@header$breadcrumbs.initializer
<div class="breadcrumbs"><span>::label::</span></div>

@header$owner
<div class='owner'><span>Assigned to ::ownerFullname::</span><a class='command' title='Unassign task' href='unsettaskowner(::idTask::,owner)'></a></div>

@header$sender
<div class='sender'><span>::senderFullname:: assigned it to me</span><a class='command' title='Unassign task' href='unsettaskowner(::idTask::,sender)'></a></div>

@header$sender.empty
<div class='sender'><span>Assigned to me</span><a class='command' title='Desasignar la tarea' href='unsettaskowner(::idTask::,sender)'></a></div>

# Pendiente de implementar. Funcionalidad de MiCV
@title.editable
<a class="behaviour descriptor" title="Click to modify the title" href="changedescriptor(::idTask::,label)">::label::</a>

@flag
<span class="::flag::"></span>

@tabs
<div class="items-desktop tabs">
  <div class="def default">::defaultTab::</div>
  ::tabsList::
</div>

@tab.default
state

@tab
<div id="::code::" class="tab ::code::">
  <div class="def label">::label::</div>
  ::render(view.task)::
</div>

@tab.state$label
State

@tab.target$label
Expedient

@tab.shortcut$label
Shortcut

@tab.orders$label
Chats