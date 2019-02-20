::view::

@view
<div class="command onload">rendersource(::id::,::label::,::id::sourceview,::id::sourceviewterms_options)</div>

<div id="::id::sourceview"></div>

<div id="::id::sourceviewterms_options" style="display:none;">
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

@view$termTemplate:client-side
<div class='term'>
  <div class='label type\#\{type\} enable_\#\{enable\}'>\#\{label\}&nbsp;<span class='code'>(\#\{code\})</span></div>
  <div class='tags'>\#\{formattedTags\}</div>
</div>