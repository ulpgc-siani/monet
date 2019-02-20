@view
<div class="object task::idTask| *::">

    <div class="controlinfo">
        <div class="idtask">::idTask::</div>
        <div class="tpl refresh ::idTask::">preview.html?view=::codeView::</div>
        <div class="mode">edition</div>
    </div>

::content::
</div>

@content.undefined
<div class="task summary"><div class="error" style="clear:both;">It has not defined the view '::codeView::' in the definition '::labelDefinition::'</div></div>

@content.state
::current::
::history::

@content.state$current
	<div class="state">
		<div class="date"><div class="function">formatdatetime(::date::)</div></div>
		<div class="flag ::state::">::stateLabel::</div>
		<div class="title">::label::</div>
	::action|<div class="view action">*</div>::
</div>

@content.state$current.empty
<div class="state">
  <div class="flag ::state::">::stateLabel::</div>
</div>

@content.state$current$action$confirmation.default
¿Are you sure you want continue with task?

@content.state$current$action.delegation
<div class="delegation">
  <div class="message">::message::</div>
  ::roleList|<ul>*</ul>::
  ::setup::
</div>

@content.state$current$action.delegation$message
Select a client to whom send order\:

@content.state$current$action.delegation$message.failure
Trying to send order. Last sent made <span class="function">formatdatetime(::date::)</span>.

@content.state$current$action.delegation$message.internal
Delegation is not supported in this task for business unit users

@content.state$current$action.delegation$message.external
Delegation is not supported in this task for external users

@content.state$current$action.delegation$message.unknown
There are neither external or internal provider defined. You may contact with engineer who develops application.

@content.state$current$action.delegation$message.empty
There are no roles to do this action. Open roles view and assign the role ::role:: to somebody.

@content.state$current$action.delegation$message.setup
Setup order for ::role::

@content.state$current$action.delegation$role
<li><a class='command' href='selecttaskdelegationrole(::taskId::,::roleId::,::requireConfirmation::)'>::name::</a></li>

@content.state$current$action.delegation$setup
::render(node.form)::
<div class="toolbar" style="border-bottom: 0px;padding-bottom: 0px;"> 
  <table>
    <tr>
      <td><a style="margin: 10px 0 0;" class="command button" href="setuptaskdelegation(::idTask::,::requireConfirmation::)">Continue</a></td>
    </tr>
  </table>
</div>  

@content.state$current$action.delegation.waiting
<div class="delegation">
  <div class="message">Sending to selected provider...</div>
</div>

@content.state$current$action.sendjob
<div class="sendjob">
  <div class="message">::message::</div>
  ::roleList|<ul>*</ul>::
  ::setup::
</div>

@content.state$current$action.sendjob$message
Select a client to whom send job\:

@content.state$current$action.sendjob$message.setup
Setup order which will be send to ::role::

@content.state$current$action.sendjob$message.setup.unassign
Setup order

@content.state$current$action.sendjob$role
<li><a class='command' href='selecttasksendjobrole(::taskId::,::roleId::,::requireConfirmation::)'>::name::</a></li>

@content.state$current$action.sendjob$role.unassign
<li><a class='command' href='selecttasksendjobrole(::taskId::,null,::requireConfirmation::)'>Unassigned</a></li>

@content.state$current$action.sendjob$setup
::render(node.form)::
<div class="toolbar" style="border-bottom: 0px;padding-bottom: 0px;"> 
  <table>
    <tr>
      <td><a style="margin: 10px 0 0;" class="command button" href="setuptasksendjob(::idTask::,::requireConfirmation::)">Continue</a></td>
    </tr>
  </table>
</div>  

@content.state$current$action.sendjob.waiting
<div class="delegation">
  <div class="message">Sending to selected worker...</div>
</div>

@content.state$current$action.line
<div class="line">
  ::timeout::
  <ul>::stopList::</ul>
</div>

@content.state$current$action.line$timeout
<div class="hint">::option:: will be selected if no option is checked before <span class="function">formatdatetime(::timeoutDate::)</span>.</div>

@content.state$current$action.line$stop
<li><a class="command" href="solvetaskline(::taskId::,::placeCode::,::stopCode::,::requireConfirmation::)">::label::</a></li>

@content.state$current$action.edition
<div class="edition">
  ::render(node.form)::
  <div class="toolbar" style="border-bottom: 0px;padding-bottom: 0px;"> 
    <table>
      <tr>
        <td><a style="margin: 10px 0 0;" class="command button" href="solvetaskedition(::idTask::,::requireConfirmation::)">Continue</a></td>
      </tr>
    </table>
  </div>  
</div>

@content.state$current$action.enroll
<div class="enroll">
  <div class="message">Select the contest which resolves this enrollment</div>
  <ul>::contests::</ul>
</div>

@content.state$current$action.enroll$contest
<li><a class="command" href="setuptaskenroll(::idTask::,::idContest::,::requireConfirmation::)">::label::</a></li>

@content.state$current$action.wait.pending
<div class="wait">
  <ul>
    <li><a class="command" href="setuptaskwait(::idTask::,hour,plus,::requireConfirmation::)">one hour</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,day,plus,::requireConfirmation::)">one day</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,month,plus,::requireConfirmation::)">one month</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,year,plus,::requireConfirmation::)">one year</a></li>
  </ul>
</div>

@content.state$current$action.wait.waiting
<div class="wait">
  
  <div class="message">Waiting until <span class="function">formatdatetime(::date::)</span>...</div>
  
  <ul>
    <li><a class="command" href="setuptaskwait(::idTask::,hour,plus,::requireConfirmation::)">add one hour</a> / <a class="command" href="setuptaskwait(::idTask::,hour,minus,::requireConfirmation::)">subtract one hour</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,day,plus,::requireConfirmation::)">add one day</a> / <a class="command" href="setuptaskwait(::idTask::,day,minus,::requireConfirmation::)">subtract one day</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,month,plus,::requireConfirmation::)">add one month</a> / <a class="command" href="setuptaskwait(::idTask::,month,minus,::requireConfirmation::)">subtract one month</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,year,plus,::requireConfirmation::)">add one year</a> / <a class="command" href="setuptaskwait(::idTask::,year,minus,::requireConfirmation::)">subtract one year</a></li>
  </ul>
</div>

@content.state$current$failure.sendRequest
<div class="sendrequest">
  <div class="message">It had a try to send the request but it failed.</div>
  <a class="command button" href="sendtaskrequest(::idTask::)">Retry...</a>
</div>

@content.state$current$failure.sendResponse
<div class="sendresponse">
  <div class="message">It had a try to send the response but it failed.</div>
  <a class="command button" href="sendtaskresponse(::idTask::)">Retry...</a>
</div>

@content.state$history
<div class="command onload">loadmoretaskhistory(::idTask::)</div>
<div class="history">
  <div class="title"></div>
  <div class="switch"></div>
  <div class="historyitems"></div>
  <div class="historymore"><a class="command button loadmoretaskhistory" href="loadmoretaskhistory(::idTask::)">show more</a></div>
</div>

@content.state$label.error
Finished without send the result

@content.state$label.finished
Finished

@content.orders
<div class="command onload">rendertaskorderlist(::id::,orderlist_::id::,orderlist_::id::_options)</div>
<div id="orderlist_::id::"></div>
<div style="display:none;" id="orderlist_::id::_options">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::orderTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No orders&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} orders";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  Options.SortByList.Items["label"] = {"Code": "label", "Label": "Title"};
  Options.SortByList.Items["create_date"] = {"Code": "create_date", "Label": "Create date"};
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
  ::groupByList::
</div>

@content.orders$orderTemplate:client-side
<div class='header closed_\#{closed}'>
  <div class='label' title='\#\{label\}'>\#\{label_short\}</div>
</div>
<div class='body closed_\#{closed}'>
  <div>Order created \#\{createDate\}</span></div>
  <div class='closed'>Order is closed. It was created at \#\{createDate\}</div>
  <div class='newmessages \#\{newMessagesClass\}'>There are \#\{newMessagesCount\} new messages</div>
</div>

@content.orders$groupByList$item
Options.GroupByList.Items["::code::"] = new Object();
Options.GroupByList.Items["::code::"].Code = "::code::";
Options.GroupByList.Items["::code::"].Label = "::label::";
Options.GroupByList.Items["::code::"].Options = new Array();
::options::

@content.orders$groupByList$item$roleLabel
Cliente / Proveedor

@content.orders$groupByList$item$option
Options.GroupByList.Items["::code::"].Options[::position::] = {Code: '::optionCode::', Label: '::optionLabel::'};

@content.target
<div class="toolbar">
  <table>
    <tr>
      <td><a class="command button" href="showtasktarget(::idTask::)">Show</a></td>
    </tr>
  </table>
</div>
::render(node.view)::

@content.shortcut
<div class="toolbar">
  <table>
    <tr>
      <td><a class="command button" href="showtaskshortcut(::idTask::,::idNode::)">Show</a></td>
    </tr>
  </table>
</div>
::render(node.view)::

@content.shortcut.document
<div class="toolbar">
	<table>
		<tr>
			<td><a class="command button" href="downloadnode(::idNode::)">Download</a></td>
		</tr>
	</table>
</div>
::render(node.view)::

@content.history
::facts::

@content.history$fact
<div class="fact">
  <div class="date"><span class="function">formatdatetime(::createDate::)</span>::user::</div>
  <div class="title">::title::</div>
  ::subTitle|<div class="subtitle">*</div>::
  ::links|<ul>*</ul>::
</div>

@content.history$fact$user
<span> by ::user::</span>

@content.history$link
<li class="link"><a class="command button shownode" href="::target::">::label::</a></li>

@content.history$link.showNode
showtasknode(::targetId::)

@content.history$link.showTask
showtask(::targetId::)
