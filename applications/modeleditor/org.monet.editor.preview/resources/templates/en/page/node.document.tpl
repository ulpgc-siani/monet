@tabs
<script type="text/javascript">
  var tabs = new Array();
</script>  
<div class="body">
  <div class="content">
    <div id="::code::" class="tabs::hide| hide::">
      <div class="def default">::defaultTab::</div>
      ::tabsList::
    </div>
  </div>
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
  ::render(view.node)::
</div>
<script type="text/javascript">
  tabs.push({code:'::code::::codeView::', label:'::label::'});
</script>

@tab.signs
<div class="tab::class| *::" id="::code::signs">
  <div class="def label">Signatures</div>
  ::render(view.node)::
</div>
<script type="text/javascript">
  tabs.push({code:'::code::signs', label:'::label::'});
</script>
