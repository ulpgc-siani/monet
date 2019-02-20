<div class="rolelist">
  ::view::
</div>

@view
<div class="command onload">renderrolelist(::id::rolelistview,::id::rolelistviewdefinitions_options,::id::rolelistview_options)</div>

<div id="::id::rolelistview"></div>

<div id="::id::rolelistviewdefinitions_options" style="display:none;">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::roleDefinitionTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen roles&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} roles";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
</div>

<div id="::id::rolelistview_options" style="display:none;">
  var Options = new Object();
  Options.Editable = false;
  Options.ShowFilterBox = true;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::roleTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;Este rol no ha sido asignado a nadie&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} usuarios";
  Options.Operations = new Array();
  Options.Operations.push({name: 'adduser', label: 'añadir usuario', visible: true});
  Options.Operations.push({name: 'addservice', label: 'añadir proveedor de servicio', visible: true});
  Options.Operations.push({name: 'addfeeder', label: 'añadir proveedor de fuente de datos', visible: true});
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
</div>

@view$roleDefinitionTemplate:client-side
<div class='roledefinition'>
  <div class='label'>\#\{label\}</div>
</div>

@view$roleTemplate:client-side
<div class='role \#\{view\} expires_\#\{expires\} expired_\#\{expired\} began_\#\{began\}'>
  <div class='label grouped'>\#\{label\}<span class='subtitle'>\#\{subtitle\}</span></div>
  <div class='label active'>Activo</div>
  <div class='label pending'>Pendiente</div>
  <div class='label inactive'>Caducado</div>
  <div class='date pending'>Se activa \#\{formattedBeginDate\}</div>
  <div class='date active'>Activo desde \#\{formattedBeginDate\}</div>
  <div class='date noexpires'>No caduca</div>
  <div class='date expires'>Caduca \#\{formattedExpireDate\}</div>
  <div class='date expired'>Estuvo activo desde \#\{formattedBeginDate\}</div>
  <div class='date expired'>Caducó \#\{formattedExpireDate\}</div>
</div>
