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
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;There are not roles&lt;/div&gt;";
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
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;This role is not assigned to anybody&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} users";
  Options.Operations = new Array();
  Options.Operations.push({name: 'adduser', label: 'add user', visible: true});
  Options.Operations.push({name: 'addservice', label: 'add service partner', visible: true});
  Options.Operations.push({name: 'addfeeder', label: 'add data source partner', visible: true});
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
  <div class='label active'>Active</div>
  <div class='label pending'>Pending</div>
  <div class='label inactive'>Expired</div>
  <div class='date pending'>It actives \#\{formattedBeginDate\}</div>
  <div class='date active'>Active from \#\{formattedBeginDate\}</div>
  <div class='date noexpires'>It does not expired</div>
  <div class='date expires'>It expires \#\{formattedExpireDate\}</div>
  <div class='date expired'>It was active from \#\{formattedBeginDate\}</div>
  <div class='date expired'>It expired \#\{formattedExpireDate\}</div>
</div>
