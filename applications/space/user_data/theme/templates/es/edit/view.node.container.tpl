@content
::shows::

@show
<div class="item-container">::render(view.node)::</div>

@toolbar
<div class="def configuration">
  var configuration = new Object();
  configuration.toolbar = new Object();
  configuration.toolbar.operationSet = new Array();
  configuration.sidebar = new Object();
  configuration.sidebar.operationSet = new Array();
  configuration.sidebar.operationSet.push({type:"HELP",parameters:{}});
  ::operations::
</div>