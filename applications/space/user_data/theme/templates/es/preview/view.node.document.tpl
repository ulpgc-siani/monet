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
<div class="command onload">previewnode(::id::,::idLayer::)</div>
<div id="::idLayer::" style="color:\#15429E;margin-top:20px;">Cargando vista previa. Por favor, espere...</div>

@content.signs
<div class="command onload">rendersignaturelist(::id::,::id::_signlist,::id::_signlist_options)</div>
<div id="::id::_signlist" class="signlist"></div>
<div id="::id::_signlist_options" style="display:none;">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::signTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen firmas&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} firmas";
</div>

@signTemplate:client-side
<div class='signing'>firmando...</div>
<div class='header'>
  <span class='flag \#\{state\}'>\#\{stateLabel\}</span>
  <span class='label' title='\#\{label\}'>\#\{label_short\}</span>
</div>
<div class='body'>
  <div class='roles invisible\#\{roles_length\} restricted\#\{restricted\}'><span>Puede ser firmado por:</span>\#\{roles\}</div>
  <div class='users invisible\#{users_count}'><span>Solo puede ser firmado por:</span>\#{users}</div>
  <div class='contact invisible\#\{contact_length\}' title='\#\{contact\}'><span>Firma realizada por:</span>\#\{contact\}</div>
  <div class='reason invisible\#\{reason_length\}' title='\#\{reason\}'><span>Motivo:</span>\#\{reason_short\}</div>
  <div class='date invisible\#\{date_length\}' title='\#\{date\}'><span>Fecha/hora:</span>\#\{date\}</div>
  <div class='message delayed' title='Esta firma requiere que se realize antes \#\{label\}'>Esta firma requiere que se realize antes \#\{label_short\}</div>
  <div class='message signed' title='Firma'>\#\{details_short\}</div>
  <div class='action pending invisible\#{users_count}' title='Haga click para firmar'>Haga click para iniciar el proceso de firma</div>
</div>