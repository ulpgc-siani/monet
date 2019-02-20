<div class="view trash">::view::</div>

@view
<div class="command onload">rendertrashlist(::id::trashlistviewer,::id::trashlistviewer_options)</div>
<div class="toolbar" style="margin-bottom: 10px;"> 
  <table>
    <tr>
      <td><a class="command button" href="emptytrash()">Empty the trash</a></td>
      <td><a class="command button" href="recovernodesfromtrash()">Recover the selected elements</a></td>
    </tr>
  </table>
</div>
<div id="::id::trashlistviewer"></div>
<div id="::id::trashlistviewer_options" style="display:none;">
  var Options = new Object();
  Options.Editable = true;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::itemTemplate::";
  Options.Templates.ShowItemCommand = "recovernodefromtrash(\#\{id\})";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;There are not elements&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} elements";
</div>

@view$itemTemplate:client-side
<div class='label' title='\#\{label\}'>\#\{label_short\}</div>
<div class='body'>
  <div title='\#\{description\}'>\#\{description_short\}</div>
  <div class='createdate'>Delete on \#\{delete_date\}</div>
</div>
