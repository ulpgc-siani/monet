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
      <div class="breadcrumbs"><span>Home</span></div>
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
      <div class="breadcrumbs"><a class="command" href="javascript:showHome()">Home</a>::breadcrumbs::</div>
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
  toolbarButtonsList["EDIT"].push("<a class='command button' href='javascript:void(null)'>Edit</a>");
</script>

@operation.download
<script type="text/javascript">
  toolbarButtonsList["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Download</a>");
  sidebarButtonsList["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Download</a>");
  sidebarButtonsList["PRINT"].push("<a class='command button' href='javascript:void(null)'>Download in PDF</a>");
  sidebarButtonsList["PRINT"].push("<a class='command button' href='javascript:void(null)'>Download in CSV</a>");
</script>

@operation.revisions
<script type="text/javascript">
  toolbarButtonsList["CONTEXT"].push("<a class='command button' href='javascript:void(null)'>Revisions</a>");
</script>

@operation.closerevisions
<script type="text/javascript">
  toolbarButtonsList["NAVIGATION"].push("<a class='command button' href='javascript:void(null)'>Close</a>");
</script>

@operation.restorerevision
<script type="text/javascript">
  toolbarButtonsList["DOWNLOAD"].push("<a class='command button' href='javascript:void(null)'>Restore</a>");
</script>

@operation.copy
<script type="text/javascript">
  toolbarButtonsList["COPY"].push("<a class='command button' href='javascript:void(null)'>Copy</a>");
</script>

@flag.locked
<a class="locked" alt="This element is not editable" title="This element is not editable">&nbsp;</a>

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
    <div class="body loading">Loading...</div>
  </div>
</div>
<script type="text/javascript">
  tabs.push({code:'::code::', label:'::label::'});
</script>  

@tab$emptyLabel
No label

@previewLabel
Preview

@signaturesLabel
Signatures

@label.location
Location