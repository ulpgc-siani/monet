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
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;This role is not signed to anybody&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} users";
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
  <div class='label active'>Active</div>
  <div class='label pending'>Pending</div>
  <div class='label inactive'>Expired</div>
  <div class='date pending'>It actives \#\{formattedBeginDate\}</div>
  <div class='date active'>Active frome \#\{formattedBeginDate\}</div>
  <div class='date noexpires'>It does not expire</div>
  <div class='date expires'>Expire \#\{formattedExpireDate\}</div>
  <div class='date expired'>It was active from \#\{formattedBeginDate\}</div>
  <div class='date expired'>It expired \#\{formattedExpireDate\}</div>
</div>
