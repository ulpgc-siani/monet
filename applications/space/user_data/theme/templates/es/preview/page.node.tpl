<div class="node page ::type:: ::id:: ::mode::">
  <div class="controlinfo">
    <div class="idnode">::id::</div>
    <div class="code">::code::</div>
    <div class="type">::type::</div>
    <div class="tpl refresh ::id::">preview.html?mode=page::from|&from=*::</div>
    <div class="ancestors">::ancestorsIds::</div>
    <div class="mode">edition</div>
    <div class="labeleditable">::labelEditable::</div>
    <div class="timestamp">::timestamp::</div>
    <div class="state">::state::</div>
    ::children::
    ::addList::
  </div>
  
  <div class="def configuration">
    var configuration = new Object();
    configuration.toolbar = new Object();
    configuration.toolbar.operationSet = new Array();
    configuration.sidebar = new Object();
    configuration.sidebar.operationSet = new Array();
    ::operations::
  </div>
    
  <div class="header info">::header::</div>
  
  ::messages::
  ::tabs::
  
</div>

@operation
configuration.toolbar.operationSet.push({type:"TOOL", visible: ::visible::, label: "::label::", parameters: {id:"::id::", code:"::code::", name:"::name::"}});
configuration.sidebar.operationSet.push({type:"TOOL", visible: ::visible::, label: "::label::", parameters: {id:"::id::", code:"::code::", name:"::name::"}});

@operation.editable
configuration.toolbar.operationSet.push({type:"EDIT", label: "Editar", parameters: {id:"::idNode::",mode:"edit.html?mode=page::from|&from=*::"}});

@operation.download
configuration.toolbar.operationSet.push({type:"DOWNLOAD", label: "Descargar", parameters: {id:"::idNode::"}});
configuration.sidebar.operationSet.push({type:"DOWNLOAD", label: "Descargar", parameters: {id:"::idNode::"}});

@operation.revisions
configuration.toolbar.operationSet.push({type:"CONTEXT", label: "Versiones", parameters: {command:"shownoderevisions(::idNode::,preview.html?mode=page)"}});

@operation.closerevisions
configuration.toolbar.operationSet.push({type:"NAVIGATION", label: "Cerrar", parameters: {command:"closenoderevisions(::idNode::,preview.html?mode=page)"}});

@operation.restorerevision
configuration.toolbar.operationSet.push({type:"DOWNLOAD", label: "Restaurar", parameters: {command:"restorenoderevision(::idRevision::,::idNode::,preview.html?mode=page)"}});

@operation.copy
configuration.toolbar.operationSet.push({type:"COPY", label: "Duplicar", parameters: {id:"::idNode::",mode:"edit.html?mode=page::from|&from=*::",parent:"::idParent::"}});

@header.desktop
<table width="100%">
  <tr>
    <td class="content">
      <div class="breadcrumbs"><span>::label::</span></div>
      <div class="title">::label::</div>
      <div class="subtitle">::description::</div>
      <div class="toolbar"></div>
    </td>
    <td class="flags">::flags::</td>
  </tr>
</table>

@header.other
<table width="100%">
  <tr>
    <td class="content">
      <div class="breadcrumbs"><a class="command" href="showhome()">Inicio</a>::breadcrumbs::&nbsp;-&nbsp;::breadcrumbsRevisions::</div>
      <div class="title"><span class="descriptor" title='::label::'>::shortLabel::</span></div>
      ::revision::
      <div class="toolbar"></div>
    </td>
    <td class="flags">::flags::</td>
  </tr>
</table>

@breadcrumbs$revisions
<a class='command' title='::label::' href='shownode(::id::,preview.html?mode=page)'>::shortLabel::</a>&nbsp;-&nbsp;<span title='Versiones'>Versiones</span>

@breadcrumbs$noRevisions
<span title='::label::'>::shortLabel::</span>

@noRevision
<div class="subtitle" title='::description::'>::shortDescription::</div>
<div class="description">Creado <span class="function">formatdatetime(::createDate::)</span>::owner| por *::::prototype| *::</div>

@revision
<span class="revision"><span class="function">formatdatetime(::revisionDate::)</span></span>
<div class="subtitle" title='::description::'>::shortDescription::</div>

@revision.current
<span class="revision">Actual</span>
<div class="subtitle" title='::description::'>::shortDescription::</div>

@prototype
con la plantilla <a class='command' href="shownode(::prototypeId::)" title="::prototypeLabel::">::shortPrototypeLabel::</a>

@breadcrumb
&nbsp;-&nbsp;<a class="command" href="shownode(::id::)" title="::label::">::shortLabel::</a>

@flag.locked
<a class="locked" alt="Este elemento no es editable" title="Este elemento no es editable">&nbsp;</a>

@child
<div class="child"><p class="code">::code::</p><p class="value">::value::</p></div>

@messages

@tabs.empty
<div class="body">
  <div class="content">
    ::render(view.node)::
  </div>
</div>

@tab$template
preview

@tab$template.readonly
preview

@tab.loading
<div class="tab" id="::code::">
  <div class="def label">::label::</div>
  <div class="def visible">::visible::</div>
  <div class="node">
    <div class="controlinfo">
      <div class="idnode">::idNode::</div>
      ::idRevision|<div class="idrevision">*</div>::
      <div class="tpl refresh ::idNode::">preview.html?view=::codeView::::from|&from=*::</div>
      <div class="mode">edition</div>
    </div>
    <div class="body loading">Cargando...</div>
  </div>
</div>

@tab$emptyLabel
Sin etiqueta

# Pendiente de implementar. Funcionalidad de MiCV
@title.editable
<a class="behaviour descriptor" title="Haga click para modificar el título" href="changedescriptor(::id::,label)">::label::</a>