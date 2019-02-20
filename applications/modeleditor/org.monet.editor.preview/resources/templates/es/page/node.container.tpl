@tabs
<script type="text/javascript">
  var tabs = new Array();
</script>  
<div id="::code::" class="tabs::hide| hide::">
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
<div class="items-container tab" id="::code::::codeView::">
  <div class="def label">::label::</div>
  ::render(view.node)::
</div>
<script type="text/javascript">
  tabs.push({code:'::code::::codeView::', label:'::label::'});
</script>