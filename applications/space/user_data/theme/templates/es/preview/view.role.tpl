::view::

@view
<div class="command onload">renderrole(::code::,::label::,::id::roleview,::id::roleview_options)</div>

<div id="::id::roleview"></div>

<div id="::id::roleview_options" style="display:none;">
  var Options = new Object();
  Options.Editable = false;
  Options.ShowFilterBox = true;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::termTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;Este rol no ha sido asignado a nadie&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} usuarios";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
</div>

@view$roleTemplate:client-side
<div class='role \#\{view\} expires_\#\{expires\} expired_\#\{expired\} began_\#\{began\}'>
  <div class='label user'>\#\{username\}</div>
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