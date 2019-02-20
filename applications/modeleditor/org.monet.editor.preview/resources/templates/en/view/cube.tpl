@view
<div class="cube">
  ::content::
</div>

@view.undefined
<div class="node summary">
  <div class="error" style="clear:both;">No view with code '::codeView::' defined in definition '::labelDefinition::'</div>
</div>

@view.dynamic$content
::toolbar::
::body::

@view.dynamic$content$toolbar.report
<table class="toolbar">
  <tr>
    <td><a class="command button" href="javascript:void(null)">Save as...</a></td>
    <td><a class="command button" href="javascript:void(null)">Show saved reports</a></td>
    <td class="selector">
      <a class="button behaviour" href="javascript:void(null)">More actions</a>
      <ul class="options">
        <li class="option"><a class="command" href="javascript:void(null)">Swap axis</a></li>
        <li class="option"><a class="command" href="javascript:void(null)">Download in CSV format</a></li>
        <li class="option"><a class="command" href="javascript:void(null)">Download in XLS format</a></li>
      </ul>
    </td>
  </tr>
</table>

@view.dynamic$content$toolbar.reportList

@view.dynamic$content$body.report
<div class="system reports">Here you can see reports view</div>

@view.static$content
<div class="system view">Here you can see reports view</div>