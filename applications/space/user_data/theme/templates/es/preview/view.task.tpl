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
<div class="task summary"><div class="error" style="clear:both;">No se ha definido la vista '::codeView::' en la definición '::labelDefinition::'</div></div>

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
¿Está seguro que desea continuar?

@content.state$current$action.delegation
<div class="delegation">
  <div class="message">::message::</div>
  ::roleList|<ul>*</ul>::
  ::setup::
</div>

@content.state$current$action.delegation$message
Selecciona el cliente al cual enviar el encargo\:

@content.state$current$action.delegation$message.failure
Intentando enviar el encargo. El último intento de envío fue realizado <span class="function">formatdatetime(::date::)</span>.

@content.state$current$action.delegation$message.internal
Esta tarea no soporta delegar el encargo a un usuario de la unidad de negocio

@content.state$current$action.delegation$message.external
Esta tarea no soporta delegar el encargo a un usuario externo

@content.state$current$action.delegation$message.unknown
No se ha definido ningún proveedor externo/interno. Contacte con el ingeniero de sistemas que ha analizado la aplicación.

@content.state$current$action.delegation$message.empty
No se ha encontrado ningún role que permita realizar esta acción. Vaya a la vista de roles y asigne el role ::role:: a algún usuario.

@content.state$current$action.delegation$message.setup
Configura el encargo de ::role::

@content.state$current$action.delegation$role
<li><a class='command' href='selecttaskdelegationrole(::taskId::,::roleId::,::requireConfirmation::)'>::name::</a></li>

@content.state$current$action.delegation$setup
::render(node.form)::
<div class="toolbar" style="border-bottom: 0px;padding-bottom: 0px;"> 
  <table>
    <tr>
      <td><a style="margin: 10px 0 0;" class="command button" href="setuptaskdelegation(::idTask::,::requireConfirmation::)">Continuar</a></td>
    </tr>
  </table>
</div>  

@content.state$current$action.delegation.waiting
<div class="delegation">
  <div class="message">Enviando al proveedor seleccionado...</div>
</div>

@content.state$current$action.sendjob
<div class="sendjob">
  <div class="message">::message::</div>
  ::roleList|<ul>*</ul>::
  ::setup::
</div>

@content.state$current$action.sendjob$message
Selecciona el usuario al cual enviar el trabajo\:

@content.state$current$action.sendjob$message.setup
Configura el trabajo que se enviará a ::role::

@content.state$current$action.sendjob$message.setup.unassign
Configura el trabajo

@content.state$current$action.sendjob$role
<li><a class='command' href='selecttasksendjobrole(::taskId::,::roleId::,::requireConfirmation::)'>::name::</a></li>

@content.state$current$action.sendjob$role.unassign
<li><a class='command' href='selecttasksendjobrole(::taskId::,null,::requireConfirmation::)'>Sin asignar</a></li>

@content.state$current$action.sendjob$setup
::render(node.form)::
<div class="toolbar" style="border-bottom: 0px;padding-bottom: 0px;"> 
  <table>
    <tr>
      <td><a style="margin: 10px 0 0;" class="command button" href="setuptasksendjob(::idTask::,::requireConfirmation::)">Continuar</a></td>
    </tr>
  </table>
</div>  

@content.state$current$action.sendjob.waiting
<div class="delegation">
  <div class="message">Enviando al trabajador seleccionado...</div>
</div>

@content.state$current$action.line
<div class="line">
  ::timeout::
  <ul>::stopList::</ul>
</div>

@content.state$current$action.line$timeout
<div class="hint">Se seleccionará ::option:: si no se elije una opción antes de <span class="function">formatdatetime(::timeoutDate::)</span>.</div>

@content.state$current$action.line$stop
<li><a class="command" href="solvetaskline(::taskId::,::placeCode::,::stopCode::,::requireConfirmation::)">::label::</a></li>

@content.state$current$action.edition
<div class="edition">
  ::render(node.form)::
  <div class="toolbar" style="border-bottom: 0px;padding-bottom: 0px;"> 
    <table>
      <tr>
        <td><a style="margin: 10px 0 0;" class="command button" href="solvetaskedition(::idTask::,::requireConfirmation::)">Continuar</a></td>
      </tr>
    </table>
  </div>  
</div>

@content.state$current$action.enroll
<div class="enroll">
  <div class="message">Selecciona el concurso al cual enviar la solicitud</div>
  <ul>::contests::</ul>
</div>

@content.state$current$action.enroll$contest
<li><a class="command" href="setuptaskenroll(::idTask::,::idContest::,::requireConfirmation::)">::label::</a></li>

@content.state$current$action.wait.pending
<div class="wait">
  <ul>
    <li><a class="command" href="setuptaskwait(::idTask::,hour,plus,::requireConfirmation::)">una hora</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,day,plus,::requireConfirmation::)">un día</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,month,plus,::requireConfirmation::)">un mes</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,year,plus,::requireConfirmation::)">un año</a></li>
  </ul>
</div>

@content.state$current$action.wait.waiting
<div class="wait">
  <div class="message">Esperando hasta <span class="function">formatdatetime(::date::)</span>...</div>
  <ul>
    <li><a class="command" href="setuptaskwait(::idTask::,hour,plus,::requireConfirmation::)">aumentar una hora</a> / <a class="command" href="setuptaskwait(::idTask::,hour,minus,::requireConfirmation::)">decrementar una hora</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,day,plus,::requireConfirmation::)">aumentar un día</a> / <a class="command" href="setuptaskwait(::idTask::,day,minus,::requireConfirmation::)">decrementar un día</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,month,plus,::requireConfirmation::)">aumentar un mes</a> / <a class="command" href="setuptaskwait(::idTask::,month,minus,::requireConfirmation::)">decrementar un mes</a></li>
    <li><a class="command" href="setuptaskwait(::idTask::,year,plus,::requireConfirmation::)">aumentar un año</a> / <a class="command" href="setuptaskwait(::idTask::,year,minus,::requireConfirmation::)">decrementar un año</a></li>
  </ul>
</div>

@content.state$current$action.sendRequest$failure
<div class="sendrequest">
  <div class="message">Hubo un intento de envío de una solicitud que falló.</div>
  <a class="command button" href="sendtaskrequest(::idTask::)">Reintentar...</a>
</div>

@content.state$current$action.sendResponse$failure
<div class="sendresponse">
  <div class="message">Hubo un intento de envío de una respuesta que falló.</div>
  <a class="command button" href="sendtaskresponse(::idTask::)">Reintentar...</a>
</div>

@content.state$history
<div class="command onload">loadmoretaskhistory(::idTask::)</div>
<div class="history">
  <div class="title"></div>
  <div class="switch"></div>
  <div class="historyitems"></div>
  <div class="historymore"><a class="command button loadmoretaskhistory" href="loadmoretaskhistory(::idTask::)">ver más</a></div>
</div>

@content.state$label.error
Finalizado sin enviar el resultado

@content.state$label.finished
Finalizado

@content.orders
<div class="command onload">rendertaskorderlist(::id::,orderlist_::id::,orderlist_::id::_options)</div>
<div id="orderlist_::id::" class="orderlist"></div>
<div style="display:none;" id="orderlist_::id::_options">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::orderTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen encargos&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} encargos";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  Options.SortByList.Items["label"] = {"Code": "label", "Label": "Título"};
  Options.SortByList.Items["create_date"] = {"Code": "create_date", "Label": "Fecha de creación"};
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
  <div>Encargo realizado \#\{createDate\}</span></div>
  <div class='closed'>El encargo ha sido cerrado. Fue creado \#\{createDate\}</div>
  <div class='newmessages \#\{newMessagesClass\}'>Hay \#\{newMessagesCount\} mensajes nuevos</div>
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
      <td><a class="command button" href="showtasktarget(::idTask::)">Ver</a></td>
    </tr>
  </table>
</div>
::render(node.view)::

@content.shortcut
<div class="toolbar">
  <table>
    <tr>
      <td><a class="command button" href="showtaskshortcut(::idTask::,::idNode::)">Ver</a></td>
    </tr>
  </table>
</div>
::render(node.view)::

@content.shortcut.document
<div class="toolbar">
	<table>
		<tr>
			<td><a class="command button" href="downloadnode(::idNode::)">Descargar</a></td>
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
<span> por ::user::</span>

@content.history$link
<li class="link"><a class="command button shownode" href="::target::">::label::</a></li>

@content.history$link.showNode
showtasknode(::targetId::)

@content.history$link.showTask
showtask(::targetId::)
