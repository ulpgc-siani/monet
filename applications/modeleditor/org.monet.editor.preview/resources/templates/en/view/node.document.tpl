@content
<div style="color:\#15429E;"><img src="::imagesPath::preview.png" style="border: 1px solid black;margin:15px auto;text-align:center;"/></div>

@content.signs
<div id="listviewer::code::signs" class="signlist"></div>
<script type="text/javascript">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::signTemplate::";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No signatures&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} firmas";
  
  var listviewer::code::signs = new CGListViewer(Options,'::language::');
  listviewer::code::signs.setBaseUrl("::path::data/::name::.signs.json");
  listviewer::code::signs.setWizardLayer("listviewerwizard");
  listviewer::code::signs.render("listviewer::code::signs");
</script>

@signTemplate:client-side
<div class='header'>
  <span class='flag pending'>Pending</span>
  <span class='label' title='\#\{label\}'>\#\{label\}</span>
</div>
<div class='body'>
  <div class='roles invisible'><span>Can be signed by:</span>\#\{roles\}</div>
  <div class='message' style='display:block;'>\#\{precedence\}</div>
</div>