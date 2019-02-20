@content
::links::
::shows::

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

@link.simple
::entities::

@link.multiple
<div class="_link">
  <div class="title">::entityLabel::</div>
  <div class="description">::entityDescription::</div>
  <div class="entities">::entities::</div>
</div>

@link.entity$notFound
<div class="_link">
  <div class="title">¡No se ha encontrado el elemento! Comprueba que haya sido definido como singleton</div>
</div>

@link.entity
<div class="_link ::entityCode:: ::entityId::">
  <div class="title"><a class="command" href="shownode(::entityId::,preview.html?mode=page)" title="::entityLabel::">::entityLabel::</a></div>
  <div class="description">::entityDescription::</div>
</div>

@link.entity.source
<div class="_link ::entityCode::">
  <div class="title"><a class="command" href="showsource(::entityId::,preview.html?mode=page)" title="::entityLabel::">::entityLabel::</a></div>
  <div class="description">::entityDescription::</div>
</div>

@link.dashboard
<div class="_link">
  <div class="title"><a class="command" href="showdashboard(::code::,::view::)">::label::</a></div>
  <div class="description">::description::</div>
</div>

@show.dashboard
::render(view.dashboard)::