<div class="object tasklist">

	<div class="def configuration">
	  var configuration = new Object();
	  configuration.toolbar = new Object();
	  configuration.toolbar.operationSet = new Array();
	  configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Update", parameters: {command:"refreshtasklist()"}});
	  configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Assign tasks...", parameters: {command:"settasksowner()"}});
	  configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Unassign tasks...", parameters: {command:"unsettasksowner()"}});
	  configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Download in PDF", parameters: {command:"printtasklist(pdf,::inbox::)"}});
	  configuration.sidebar = new Object();
	  configuration.sidebar.operationSet = new Array();
	  configuration.sidebar.operationSet.push({type:"NAVIGATION", label: "Update", parameters: {command:"refreshtasklist()"}});
	  configuration.sidebar.operationSet.push({type:"NAVIGATION", label: "Assign tasks...", parameters: {command:"settasksowner()"}});
 	  configuration.sidebar.operationSet.push({type:"NAVIGATION", label: "Unassign tasks...", parameters: {command:"unsettasksowner()"}});
	  configuration.sidebar.operationSet.push({type:"PRINT", label: "Download in PDF", parameters: {command:"printtasklist(pdf,::inbox::)"}});
	  configuration.sidebar.operationSet.push({type:"PRINT", label: "Download in CSV", parameters: {command:"printtasklist(csv,::inbox::)"}});
	  configuration.sidebar.operationSet.push({type:"PRINT", label: "Download in Excel", parameters: {command:"printtasklist(xls,::inbox::)"}});
	</div>
	
  ::view::
</div>

@view
<div class="command onload">rendertasklist(::view::,::id::tasklistviewer_::type::,::id::tasklistviewer_::type::_options)</div>
<div id="::id::tasklistviewer_::type::"></div>
<div id="::id::tasklistviewer_::type::_options" style="display:none;">
  var Options = new Object();
  Options.Editable = true;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::taskTemplate::";
  Options.Templates.ShowItemCommand = "showtask(\#\{id\},preview.html?mode=page&from=::from::)";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;There are not tasks&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} tasks";
  Options.FolderList = new Object();
  Options.FolderList.Items = new Object();
  Options.FolderList.Selection = "::defaultFolder::";
  Options.FolderList.Items["all"] = {"Code": "all", "Label": "all"};
  Options.FolderList.Items["alive"] = {"Code": "alive", "Label": "executing"};
  Options.FolderList.Items["active"] = {"Code": "active", "Label": "active"};
  Options.FolderList.Items["pending"] = {"Code": "pending", "Label": "pending"};
  Options.FolderList.Items["finished"] = {"Code": "finished", "Label":"finished"};
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["create_date"];
  Options.SortByList.Items = new Object();
  Options.SortByList.Items["create_date"] = {"Code": "create_date", "Label": "Create date", "DefaultMode": "descendant"};
  Options.SortByList.Items["update_date"] = {"Code": "update_date", "Label": "Update date", "DefaultMode": "descendant"};
  Options.SortByList.Items["label"] = {"Code": "label", "Label": "Nombre", "DefaultMode": "descendant"};
  Options.SortByList.Items["urgency"] = {"Code": "urgent", "Label": "Urgency"};
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = ::groupByListSelection::;
  Options.GroupByList.Items = new Object();
  ::groupByListRoles::
  Options.GroupByList.Items["urgency"] = new Object();
  Options.GroupByList.Items["urgency"].Code = "urgent";
  Options.GroupByList.Items["urgency"].Label = "Urgency";
  Options.GroupByList.Items["urgency"].Options = new Array();
  Options.GroupByList.Items["urgency"].Options[0] = {Code: '1', Label: 'Urgents'};
  Options.GroupByList.Items["urgency"].Options[1] = {Code: '0', Label: 'Not urgents'};
  Options.GroupByList.Items["background"] = new Object();
  Options.GroupByList.Items["background"].Code = "background";
  Options.GroupByList.Items["background"].Label = "Nature";
  Options.GroupByList.Items["background"].Options = new Array();
  Options.GroupByList.Items["background"].Options[0] = {Code: '1', Label: 'Only system tasks'};
  Options.GroupByList.Items["background"].Options[1] = {Code: '2', Label: 'All tasks'};
  ::groupByListTypes::
  ::groupByListOwners::
  ::groupByListSenders::
</div>

@view.custom
<div class="tasklist" style="padding:0px;">
  <div class="listviewer">
    <ul class="items">::tasks::</ul>
  </div>
</div>

@view.custom$item
<li class="item" style="padding-top:5px;">
  <a class="command content" href="showtask(::id::)">
    <table class='task ::state:: ::read::' style='width:100%'>
      <tr>
        <td class='header'>
          <div class='label' style='margin-top\:3px;' title='::label::'><img src='::themeSource::&path=_images/s.gif' alt='::label::' class='type ::type::'/><span>::label::</span></div>
          <div class='description' title='::definitionLabel::. ::description::'>::definitionLabel::.&nbsp;::description::</div>
          <div class='dates'>Created&nbsp;::createDate::. Updated&nbsp;::updateDate::.</div>
          <div class='newmessages ::newMessagesClass::'>There are ::newMessagesCount:: new messages</div>
        </td>
        <td class='charts'>
          <img src='::chartSource::&id=::id::&random=::random::'/>
        </td>
        <td class='flags'>
          <a class='command urgent ::urgentStyle::' title='Set task priority: urgent/not urgent' href='toggletaskurgency(::id::)'></a>
          <a class='command hover comments visible_::hasComments::' href='loadtaskcomments(::id::)'>&nbsp;</a>
        </td>
      </tr>
    </table>
  </a>
</li>

@view$tasktray$groupByListSelection
[]

@view$taskboard$groupByListSelection
[]

@view$tasktray$template:client-side
<table class='task \#\{state\} \#\{read}' style='width:100%'>
  <tr>
    <td class='header'>
      <div class='label' style='margin-top\:3px;' title='\#\{label\}'><img src='::themeSource::&path=_images/s.gif' alt='\#\{typeLabel\}' class='type \#\{type\}'/><span>\#\{label_short\}</span></div>
      <div class='description' title='\#\{definitionLabel\}. \#\{description\}'>\#\{definitionLabel\}.&nbsp;\#\{description_short\}</div>
      <div class='sender \#{senderStyle}'><span class='assigned'>\#{senderFullname} assigned it to me</span><a class='command' title='Unassign task' href='unsettaskowner(\#{id},sender)'></a></div>
      <div class='senderempty \#{senderEmptyStyle}'><span class='assigned'>Assigned it to me</span><a class='command' title='Unassign task' href='unsettaskowner(\#{id},senderempty)'></a></div>
      <div class='dates'>Created&nbsp;\#\{createDate\}. Updated&nbsp;\#\{updateDate\}.</div>
    </td>
    <td class='charts'>
      <img src='::chartSource::&id=\#\{id\}&random=\#\{random\}'/>
    </td>
    <td class='flags'>
      <a class='command urgent \#{urgentStyle}' title='Set task priority: urgent/not urgent' href='toggletaskurgency(\#{id})'></a>
      <a class='command hover comments visible_\#{hasComments}' href='loadtaskcomments(\#{id})'>&nbsp;</a>
    </td>
  </tr>
</table>

@view$taskboard$template:client-side
<table class='task \#\{state\} \#\{read}' style='width:100%'>
  <tr>
    <td class='header'>
      <div class='label' style='margin-top\:3px;' title='\#\{label\}'><img src='::themeSource::&path=_images/s.gif' alt='\#\{typeLabel\}' class='type \#\{type\}'/><span>\#\{label_short\}</span></div>
      <div class='description' title='\#\{definitionLabel\}. \#\{description\}'>\#\{definitionLabel\}.&nbsp;\#\{description_short\}</div>
      <div class='owner \#{ownerStyle}'><span class='assigned'>Assigned to \#{ownerFullname}</span><a class='command' title='Unassign task' href='unsettaskowner(\#{id},owner)'></a></div>
      <div class='sender \#{senderStyle}'><span class='assigned'>\#{senderFullname} assigned it to me</span><a class='command' title='Unassign task' href='unsettaskowner(\#{id},sender)'></a></div>
      <div class='senderempty \#{senderEmptyStyle}'><span class='assigned'>Assigned it to me</span><a class='command' title='Unassign task' href='unsettaskowner(\#{id},senderempty)'></a></div>
      <div class='dates'>Created&nbsp;\#\{createDate\}. Updated&nbsp;\#\{updateDate\}.</div>
      <div class='newmessages \#\{newMessagesClass\}'>There are \#\{newMessagesCount\} new messages</div>
    </td>
    <td class='charts'>
      <img src='::chartSource::&id=\#\{id\}&random=\#\{random\}'/>
    </td>
    <td class='flags'>
      <a class='command urgent \#{urgentStyle}' title='Set task priority: urgent/not urgent' href='toggletaskurgency(\#{id})'></a>
      <a class='command hover comments visible_\#{hasComments}' href='loadtaskcomments(\#{id})'>&nbsp;</a>
    </td>
  </tr>
</table>

@view$groupByList$item
Options.GroupByList.Items["::code::"] = new Object();
Options.GroupByList.Items["::code::"].Code = "::code::";
Options.GroupByList.Items["::code::"].Label = "::label::";
Options.GroupByList.Items["::code::"].Options = new Array();
::options::

@view$groupByList$item$roleLabel
Role

@view$groupByList$item$typeLabel
Clase

@view$groupByList$item$ownerLabel
Users with assignments

@view$groupByList$item$senderLabel
Users that made me assignments

@view$groupByList$item$unassignedLabel
No assignment

@view$groupByList$item$option
Options.GroupByList.Items["::code::"].Options[::position::] = {Code: '::optionCode::', Label: '::optionLabel::'};