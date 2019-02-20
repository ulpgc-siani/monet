@content
<div class="form view">
  <div class="nodeFields">
    ::shows::
  </div>
</div>

@show.field
::render(field)::

@show.layout
::render(layout)::

@toolbar
<div class="def configuration">
  var configuration = new Object();
  configuration.toolbar = new Object();
  configuration.toolbar.operationSet = new Array();
  configuration.sidebar = new Object();
  configuration.sidebar.operationSet = new Array();
  configuration.sidebar.operationSet.push({type:"OBSERVER",parameters:{}});
  configuration.sidebar.operationSet.push({type:"HELP",parameters:{}});
  ::operations::
</div>
