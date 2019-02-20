::view::

@view
<div class="command onload">rendersourcelist(::id::sourcelistview,::id::sourcelistview_options,::id::sourcelistviewterms_options)</div>

<div id="::id::sourcelistview"></div>

<div id="::id::sourcelistview_options" style="display:none;">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::sourceTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen tesauros&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} fuentes";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
</div>

<div id="::id::sourcelistviewterms_options" style="display:none;">
  var Options = new Object();
  Options.Editable = false;
  Options.ShowFilterBox = true;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::termTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen términos&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} términos";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
</div>

@view$sourceTemplate:client-side
<div class='source'>
  <div class='label'>\#\{label\}</div>
</div>

@view$termTemplate:client-side
<div class='term'>
  <div class='label type\#\{type\} enable_\#\{enable\}'>\#\{label\}&nbsp;<span class='code'>(\#\{code\})</span></div>
  <div class='tags'>\#\{formattedTags\}</div>
</div>
