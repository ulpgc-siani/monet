<div class="notificationlist">
  <div class="controlinfo">
    <div class="command onload">rendernotificationlist(notificationlistviewer,notificationlistviewer_options)</div>
  </div>
  
  <div class="info">
    <table width="100%">
      <tr>
        <td class="content">
          <div class="breadcrumbs"><a class="command" href="showhome()">Inicio</a>&nbsp;-&nbsp;<span>Avisos</span></div>
          <div class="title">Avisos</div>
        </td>
      </tr>
    </table>
  </div>
  
  <div class="content">
    <div id="notificationlistviewer"></div>

    <div id="notificationlistviewer_options" style="display:none;">
      var Options = new Object();
      Options.Editable = false;
      Options.DataSource = new Object();
      Options.DataSource.Remote = true;
      Options.Templates = new Object();
      Options.Templates.Item = "::notificationTemplate::";
      Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen avisos&lt;/div&gt;";
      Options.Templates.CountItems = "\#\{count\} avisos";
    </div>
  </div>
</div>

@notificationTemplate:client-side
<div class='\#\{unread\}'>
  <div class='label' title='\#\{label\}'>\#\{label_short\}</div>
  <div class='body'>
    <div class='dates'>\#\{createDate\}</div>
  </div>
</div>