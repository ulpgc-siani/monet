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

@content
<div class="command onload">editnodedocument(::id::,::idLayer::)</div>
<div id="::idLayer::" class="wizarddocument" style="margin-top:10px;color:\#15429E;"></div>

<div class="command onload">previewnode(::id::,::idLayer::_preview)</div>
<div id="::idLayer::_preview" style="color:\#15429E;margin-top:20px;">Cargando vista previa. Por favor, espere...</div>

@content.form
<a class="command" href="shownode(::id::)" alt="::label::" style="float\:left;"><img style="border\:1px solid \#666;" src="::previewImageSource::" title="::label::"/></a>
<div class="toolbar" style="float:left;border-bottom:0px;clear:both;margin: 10px 0px;padding-bottom:10px;"><a class="command button" href="shownode(::id::)" style="width:60px;display:inline;text-align:center;margin:0px;">abrir</a>\&nbsp;\&nbsp;\&nbsp;<a class="command button" href="downloadnode(::id::)" style="width:60px;display:inline;text-align:center;margin:0px;">descargar</a></div>