@view
<div class="node::clec| *::::type| *::">
  ::toolbar::
  ::content::
</div>

@view.undefined
<div class="node summary"><div class="error" style="clear:both;">No se ha definido la vista '::codeView::' en la definición '::labelDefinition::'</div></div>

@toolbar

@toolbar.systemView
<script type="text/javascript">
  viewToolbarButtonsList["::id::"] = new Array();
  viewToolbarButtonsList["::id::"]["ADD"] = new Array();
  viewToolbarButtonsList["::id::"]["DOWNLOAD"] = new Array();
  viewToolbarButtonsList["::id::"]["TOOL"] = new Array();
  viewSidebarButtonsList["::id::"] = new Array();
  viewSidebarButtonsList["::id::"]["ADD"] = new Array();
  viewSidebarButtonsList["::id::"]["DOWNLOAD"] = new Array();
  viewSidebarButtonsList["::id::"]["TOOL"] = new Array();
  ::operations::
  viewToolbarButtonsList["::id::"]["TOOL"].push("<td><a class='command button' href='javascript:void(null)'>Actualizar</a></td>");
</script>

@toolbar.systemView.attachments
<script type="text/javascript">
  viewToolbarButtonsList["::id::"] = new Array();
  viewSidebarButtonsList["::id::"] = new Array();
  ::operations::
  viewToolbarButtonsList["::id::"]["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Descargar</a>");
  viewToolbarButtonsList["::id::"]["TOOL"].push("<a class='command button' href='javascript:void(null)'>Eliminar</a>");
  viewSidebarButtonsList["::id::"]["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Descargar</a>");
  viewSidebarButtonsList["::id::"]["TOOL"].push("<a class='command button' href='javascript:void(null)'>Eliminar</a>");
</script>

@toolbar.systemView.attachments$addNode
viewToolbarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::</a>");
viewSidebarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::</a>");

@toolbar.systemView.attachments$addFile
viewToolbarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::...</a>");
viewSidebarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::...</a>");

@content.linksIn
<div class="systemview">Listado de vínculos de sistema de tipo linksIn</div>

@content.linksOut
<div class="systemview">Listado de vínculos de sistema de tipo linksOut</div>

@content.attachments
<div class="documentlistviewer">
  <div class="header" style="display: block;">
    <table>
      <tr>
        <td width="1%"><a title="anterior" class="previous disabled"></a></td>
        <td width="98%">
          <select class="documents">::options::</select>
        </td>
        <td width="1%"><a title="siguiente" class="next disabled"></a></td>
      </tr>
    </table>
  </div>
  <div class="container">
    <div class="pageItem">
      <div class="pageItemImage" style="height:750px;width:535px;background:none;">
        <img src="::imagesPath::preview.png" style="height:750px;width:535px;">
      </div>
    </div>  
  </div>
</div>

@content.attachments$option
<option value="::code::" class="document">::label::</option>

@content.location
<div class="systemview">Vista de localización</div>

@content.notes
<ul class="notes">::notes::</ul>

@content.notes$empty
<li class="note">No se han definido notas</li>

@content.tasks
<div class="tasklist" style="padding:0px;"><div class="listviewer"><ul class="items">::tasks::</ul></div></div>

@content.tasks$empty
<li class="task">Este elemento no es usado por ninguna tarea</li>

@content.revisions

@content.prototypes
<div class="systemview">Listado de prototipos</div>

@sortByList$item.default
Options.SortByList.Items["label"] = {"Code": "label", "Label": "Título"};