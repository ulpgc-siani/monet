@page
<div class="node ::type::">
  ::configuration::
  <div class="header info">::header::</div>
  ::tabs::
</div>

@header.desktop
<table width="100%">
  <tr>
    <td class="content">
      <div class="breadcrumbs"><span>Inicio</span></div>
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
      <div class="breadcrumbs"><a class="command" href="javascript:showHome()">Inicio</a>::breadcrumbs::</div>
      <div class="title"><span class="descriptor" title='::label::'>::shortLabel::</span></div>
      <div class="subtitle" title='::description::'>::shortDescription::</div>
      <div class="toolbar"></div>
    </td>
    <td class="flags">::flags::</td>
  </tr>
</table>

@breadcrumb
&nbsp;-&nbsp;<a class="command" href="javascript:showPage('::name::')" title="::label::">::shortLabel::</a>

@operation
<script type="text/javascript">
  toolbarButtonsList["TOOL"].push("<a class='command button' href='javascript:void(null)'>::label::</a>");
  sidebarButtonsList["TOOL"].push("<a class='command button' href='javascript:void(null)'>::label::</a>");
</script>

@operation.editable
<script type="text/javascript">
  toolbarButtonsList["EDIT"].push("<a class='command button' href='javascript:void(null)'>Editar</a>");
</script>

@operation.download
<script type="text/javascript">
  toolbarButtonsList["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Descargar</a>");
  sidebarButtonsList["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Descargar</a>");
  sidebarButtonsList["PRINT"].push("<a class='command button' href='javascript:void(null)'>Descargar en PDF</a>");
  sidebarButtonsList["PRINT"].push("<a class='command button' href='javascript:void(null)'>Descargar en CSV</a>");
</script>

@operation.revisions
<script type="text/javascript">
  toolbarButtonsList["CONTEXT"].push("<a class='command button' href='javascript:void(null)'>Versiones</a>");
</script>

@operation.closerevisions
<script type="text/javascript">
  toolbarButtonsList["NAVIGATION"].push("<a class='command button' href='javascript:void(null)'>Cerrar</a>");
</script>

@operation.restorerevision
<script type="text/javascript">
  toolbarButtonsList["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Restaurar</a>");
</script>

@operation.copy
<script type="text/javascript">
  toolbarButtonsList["COPY"].push("<a class='command button' href='javascript:void(null)'>Duplicar</a>");
</script>

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
  <div class="node">
    <div class="body loading">Cargando...</div>
  </div>
</div>
<script type="text/javascript">
  tabs.push({code:'::code::', label:'::label::'});
</script>  

@tab$emptyLabel
Sin etiqueta

@previewLabel
Vista previa

@signaturesLabel
Firmas

@label.location
Localización