@view
<div class="object node::clec| *::::type| *::::idNode| *::">
  ::toolbar::

  <div class="controlinfo">
    <div class="idnode">::idNode::</div>
    <div class="code">::codeNode::</div>
    <div class="tpl refresh ::idNode::">preview.html?view=::codeView::::page|&pag=*::::from|&from=*::</div>
    <div class="mode">edition</div>
    <div class="timestamp">::timestamp::</div>
    <div class="codeview">::codeView::</div>
    <div class="state">::state::</div>
  </div>

  ::content::
</div>

@view.undefined
<div class="node summary"><div class="error" style="clear:both;">No se ha definido la vista '::codeView::' en la definición '::labelDefinition::'</div></div>

@view.requirePartnerContext
<div class="node partnercontext">
  <table>
    <tr><td>
      <div class="title">No se ha definido el proveedor para este elemento</div>
    </td></tr>
  </table>
</div>

@view.requirePartnerContext.empty
<div class="node partnercontext">
  <table>
    <tr><td>
      <div class="title">Este elemento necesita que se indique el proveedor con el que se trabajará.</div>
    </td></tr>
  </table>
</div>

@view.requirePartnerContext$context

@toolbar

@toolbar.systemView
<div class="def configuration">
  var configuration = new Object();
  configuration.toolbar = new Object();
  configuration.toolbar.operationSet = new Array();
  configuration.sidebar = new Object();
  configuration.sidebar.operationSet = new Array();
  ::operations::
  configuration.toolbar.operationSet.push({type:"TOOL", label: "Actualizar", parameters: {command:"refreshnode(::idNode::)"}});
</div>

@toolbar.systemView.attachments
<div class="def configuration">
  var configuration = new Object();
  configuration.toolbar = new Object();
  configuration.toolbar.operationSet = new Array();
  configuration.sidebar = new Object();
  configuration.sidebar.operationSet = new Array();
  ::operations::
  configuration.toolbar.operationSet.push({type:"DOWNLOAD", label: "Descargar", parameters: {command:"downloadattachment(::idNode::)"}});
  configuration.sidebar.operationSet.push({type:"DOWNLOAD", label: "Descargar", parameters: {command:"downloadattachment(::idNode::)"}});
  configuration.sidebar.operationSet.push({type:"OBSERVER",parameters:{}});
</div>

@toolbar.systemView.attachments$addNode

@toolbar.systemView.attachments$addFile

@operation
configuration.toolbar.operationSet.push({type:"TOOL", visible: ::visible::, label: "::label::", parameters: {id:"::id::", code:"::code::", name:"::name::"}});
configuration.sidebar.operationSet.push({type:"TOOL", visible: ::visible::, label: "::label::", parameters: {id:"::id::", code:"::code::", name:"::name::"}});

@content.links$existsLinksToComponents
<div class="node summary"><div class="error" style="clear:both;">No puede definir vínculos a definiciones que son componente. Revise la definición de vínculos.</div></div>

@content.linksIn
<div class="command onload">rendernodecontent(::idNode::,linksin,::codeDefinition::,::codeView::,listviewer_::idNode::_::codeView::,listviewer_::idNode::_::codeView::_options)</div>
<div id="listviewer_::idNode::_::codeView::"></div>
<div style="display:none;" id="listviewer_::idNode::_::codeView::_options">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "&lt;div class='label' title='\#\{label\}'&gt;\#\{label_short\}&lt;/div&gt;&lt;div class='body'&gt;&lt;div title='\#\{description\}'&gt;\#\{description_short\}&lt;/div&gt;&lt;div class='typelabel'&gt;\#\{type_label\}&lt;/div&gt;&lt;/div&gt;";
  Options.Templates.ShowItemCommand = "shownode(\#\{id\},preview.html?mode=page)";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen relaciones&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} relaciones";
</div>

@content.linksOut
<div class="command onload">rendernodecontent(::idNode::,linksout,::codeDefinition::,::codeView::,listviewer_::idNode::_::codeView::,listviewer_::idNode::_::codeView::_options)</div>
<div id="listviewer_::idNode::_::codeView::"></div>
<div style="display:none;" id="listviewer_::idNode::_::codeView::_options">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "&lt;div class='label' title='\#\{label\}'&gt;\#\{label_short\}&lt;/div&gt;&lt;div class='body'&gt;&lt;div title='\#\{description\}'&gt;\#\{description_short\}&lt;/div&gt;&lt;div class='typelabel'&gt;\#\{type_label\}&lt;/div&gt;&lt;/div&gt;";
  Options.Templates.ShowItemCommand = "shownode(\#\{id\},preview.html?mode=page)";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen relaciones&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} relaciones";
</div>  

@content.attachments
<div class="command onload">rendernodeattachments(::idNode::,::codeDefinition::,::codeView::,attachmentviewer_::idNode::_::codeView::,attachmentviewer_::idNode::_::codeView::_options)</div>
<div id="attachmentviewer_::idNode::_::codeView::"></div>
<div style="display:none;" id="attachmentviewer_::idNode::_::codeView::_options">
  var Options = new Object();
  Options.Editable = false;
</div>  

@content.location
<div class="command onload">rendernodelocation(::idNode::,::codeDefinition::,::codeView::,mapviewer_::idNode::_::codeView::,mapviewer_::idNode::_::codeView::_options)</div>
<div id="mapviewer_::idNode::_::codeView::" style="float: left; height: 100%; width: 100%; margin-top: 10px;" class="maplocation"></div>
<div style="display:none;" id="mapviewer_::idNode::_::codeView::_options">
  var Options = {
    editable: false,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
</div>  

@content.notes
<div class="command onload">rendernodenotes(::idNode::,notesviewer_::idNode::_::codeView::,notesviewer_::idNode::_::codeView::_options)</div>
<div id="notesviewer_::idNode::_::codeView::"></div>
<div style="display:none;" id="notesviewer_::idNode::_::codeView::_options">
  var options = {};
  options.Editable = false;
</div>

@content.tasks
::render(view.tasklist)::

@content.tasks$empty
<div class="node summary"><div class="message" style="clear:both;">Este elemento no es usado por ninguna tarea</div></div>

@content.revisions

@content.prototypes
<div class="command onload">rendernodecontent(::idNode::,::type::,::codeDefinition::,::codeView::,listviewer_::idNode::_::codeView::,listviewer_::idNode::_::codeView::_options)</div>
<div id="listviewer_::idNode::_::codeView::"></div>
<div style="display:none;" id="listviewer_::idNode::_::codeView::_options">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "&lt;div class='label' title='\#\{label\}'&gt;\#\{label_short\}&lt;/div&gt;&lt;div class='body'&gt;&lt;div title='\#\{description\}'&gt;\#\{description_short\}&lt;/div&gt;&lt;div class='createdate'&gt;Creado el \#\{create_date\}&lt;/div&gt;&lt;/div&gt;";
  Options.Templates.ShowItemCommand = "shownode(\#\{id\},preview.html?mode=page)";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen elementos&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} elementos";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  ::sortByList::
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
  ::groupByList::
</div>

@sortByList$items.default
Options.SortByList.Items["label"] = {"Code": "label", "Label": "Título"};
Options.SortByList.Items["create_date"] = {"Code": "create_date", "Label": "Fecha de creación"};

@sortByList$item.default.code
label

@sortByList$item.default.mode
ascendant

@sortByList$item.mode.asc
ascendant

@sortByList$item.mode.desc
descendant