@page
<div class="cube">
  ::header::
  <div class="content">
    ::tabs::
  </div>
</div>

@header
<div class="properties">
  <div class="breadcrumbs">
    <a class="command" href="javascript:showHome()">Inicio</a>&nbsp;-&nbsp;<span>::label::</span>
  </div>
  <div class="info">
    <table width="100%">
      <tr>
        <td class="content">
          <div class="title"><span class="descriptor">::label::</span></div>
          <div class="subtitle">::description::</div>
        </td>
        <td class="flags">&nbsp;</td>
      </tr>
    </table>
  </div>
</div>

@tabs
<script type="text/javascript">
  var tabs = new Array();
</script>  
<div id="::code::" class="tabs">
  <div class="def default">::defaultTab::</div>
  ::tabsList::
</div>
<script type="text/javascript">
  var extTabs = new Ext.TabPanel('::code::');
  for (var i=0; i<tabs.length; i++) {
    var tab = tabs[i]; 
    var extTab = extTabs.addTab(tab.code, tab.label);
    extTab.on("activate", atTabActivated);
  }
  disableNotifications();
  extTabs.activate("::defaultTab::");
  enableNotifications();
</script>  

@tab
<div class="tab" id="::code::::codeView::">
  <div class="def label">::label::</div>
  ::render(view.cube)::
</div>
<script type="text/javascript">
  tabs.push({code:'::code::::codeView::', label:'::label::'});
</script>  

@tab.loading
<div class="tab" id="::code::">
  <div class="def label">::label::</div>
  <div class="cube">
    <div class="body loading">Cargando...</div>
  </div>
</div>