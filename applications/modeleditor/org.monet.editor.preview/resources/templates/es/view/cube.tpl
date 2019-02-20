@view
<div class="cube">
  ::content::
</div>

@view.undefined
<div class="node summary">
  <div class="error" style="clear:both;">No se ha definido la vista '::codeView::' en la definición '::labelDefinition::'</div>
</div>

@view.dynamic$content
::toolbar::
::body::

@view.dynamic$content$toolbar.report
<table class="toolbar">
  <tr>
    <td><a class="command button" href="javascript:void(null)">Guardar como...</a></td>
    <td><a class="command button" href="javascript:void(null)">Ver informes guardados</a></td>
    <td class="selector">
      <a class="button behaviour" href="javascript:void(null)">Más acciones</a>
      <ul class="options">
        <li class="option"><a class="command" href="javascript:void(null)">Intercambiar ejes</a></li>
        <li class="option"><a class="command" href="javascript:void(null)">Descargar en formato CSV</a></li>
        <li class="option"><a class="command" href="javascript:void(null)">Descargar en formato XLS</a></li>
      </ul>
    </td>
  </tr>
</table>

@view.dynamic$content$toolbar.reportList

@view.dynamic$content$body.report
<div class="system reports">Aquí se mostrará la vista de informes</div>

@view.static$content
<div class="system view">Aquí se mostrará la vista de informes</div>