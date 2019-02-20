@view
<div class="node::clec| *::::type| *::">
  ::toolbar::
  ::content::
</div>

@view.undefined
<div class="node summary"><div class="error" style="clear:both;">No view defined with code '::codeView::' in definition '::labelDefinition::'</div></div>

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
  viewToolbarButtonsList["::id::"]["TOOL"].push("<td><a class='command button' href='javascript:void(null)'>Refresh</a></td>");
</script>

@toolbar.systemView.attachments
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
  viewToolbarButtonsList["::id::"]["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Download</a>");
  viewToolbarButtonsList["::id::"]["TOOL"].push("<a class='command button' href='javascript:void(null)'>Delete</a>");
  viewSidebarButtonsList["::id::"]["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Download</a>");
  viewSidebarButtonsList["::id::"]["TOOL"].push("<a class='command button' href='javascript:void(null)'>Delete</a>");
</script>

@toolbar.systemView.attachments$addNode
viewToolbarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::</a>");
viewSidebarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::</a>");

@toolbar.systemView.attachments$addFile
viewToolbarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::...</a>");
viewSidebarButtonsList["::id::"]["ADD"].push("<a class='command button' href='javascript:void(null)'>::label::...</a>");

@content.linksIn
<div class="systemview">Here you can see the links in</div>

@content.linksOut
<div class="systemview">Here you can see the links out</div>

@content.attachments
<div class="documentlistviewer">
  <div class="header" style="display: block;">
    <table>
      <tr>
        <td width="1%"><a title="previous" class="previous disabled"></a></td>
        <td width="98%">
          <select class="documents">::options::</select>
        </td>
        <td width="1%"><a title="next" class="next disabled"></a></td>
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
<div class="systemview">Location view</div>

@content.notes
<ul class="notes">::notes::</ul>

@content.notes$empty
<li class="note">No notes defined</li>

@content.tasks
<div class="tasklist" style="padding:0px;"><div class="listviewer"><ul class="items">::tasks::</ul></div></div>

@content.tasks$empty
<li class="task">This element is not referenced by tasks</li>

@content.revisions

@content.prototypes
<div class="systemview">Prototypes list</div>

@sortByList$item.default
Options.SortByList.Items["label"] = {"Code": "label", "Label": "Title"};