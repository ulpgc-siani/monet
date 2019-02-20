@content
::view::

@toolbar
<script type="text/javascript">
  viewToolbarButtonsList["::id::"] = new Array();
  viewToolbarButtonsList["::id::"]["PRINT"] = new Array();
  viewSidebarButtonsList["::id::"] = new Array();
  viewSidebarButtonsList["::id::"]["PRINT"] = new Array();
  viewToolbarButtonsList["::id::"]["PRINT"].push("<a class='command button' href='javascript:void(null)'>Download in PDF</a>");
</script>

@view.index
<div id="listviewer::code::::codeView::"></div>
<script type="text/javascript">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::indexTemplate::";
  Options.Templates.ShowItemCommand = "javascript:showPage('\#\{id\}')";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No ::labelReference::&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} ::labelReference::";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["label"];
  Options.SortByList.Items = new Object();
  ::sortByList::
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
  ::groupByList::

  var listviewer::code::::codeView:: = new CGListViewer(Options,'::language::');
  listviewer::code::::codeView::.setBaseUrl("::path::data/::name::.::codeView::.json");
  listviewer::code::::codeView::.setWizardLayer("listviewerwizard");
  listviewer::code::::codeView::.render("listviewer::code::::codeView::");
</script>

@indexTemplate:client-side
<div class='reference'>
  <table width='100%'>
    <tr>
      ::picture|<td width='1px'>*</td>::
      <td>
        ::label::
        ::lead|<div class='lead attributes'>*</div>::
        <div class='body'>
          <table width='100%'>
            <tr>
              <td width='80%'>
                ::line|<div class='line attributes'>*</div>::
                ::line_below|<div class='line_below attributes'>*</div>::
                ::highlight|<div class='highlight attributes'>*</div>::
              </td>
              <td>
                <div class='icons attributes'>
                  <a class='attribute georef val0\#\{georeferenced\}' title='This element is not georeferenced' href='javascript:void(null)'></a>
                  <a class='attribute georef val1\#\{georeferenced\}' title='This element is georeferenced' href='javascript:void(null)'></a>
                  ::icon::
                </div>
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    ::footer|<tr><td colspan='2'><div class='footer attributes'>*</div></td></tr>::
  </table>
</div>

@indexTemplate$label
<div class='label' title='\#\{::code::\}'>\#\{::code::\}</div>

@indexTemplate$noLabel
<div class='label'>No label</div>

@indexTemplate$attribute
<span class='attribute invisible\#\{::length::\}'>
  <span class='title'>::label::\:</span>&nbsp;
  <span class='value' title='\#\{::code::\}'>\#\{::code::\}</span>&nbsp;&nbsp;&nbsp;
</span>

@indexTemplate$attribute.picture
<span class='attribute'><img src='::imagesUrl::' alt='::label::' class='picture attribute'/></span>

@indexTemplate$attribute.lead
<span class='attribute invisible\#\{::length::\}'>\#\{::code::\}</span>&nbsp;&nbsp;&nbsp;</span>

@indexTemplate$attribute.highlight
<span class='attribute invisible\#\{::length::\}'>\#\{::code::\}</span>&nbsp;&nbsp;&nbsp;</span>

@sortByList$item
Options.SortByList.Items["::code::"] = {"Code": "::code::", "Label": "::label::"};

@groupByList$item$typeLabel
Class

@groupByList$item
Options.GroupByList.Items["::code::"] = new Object();
Options.GroupByList.Items["::code::"].Code = "::code::";
Options.GroupByList.Items["::code::"].Label = "::label::";
Options.GroupByList.Items["::code::"].Options = new Array();
::options::

@groupByList$item$option
Options.GroupByList.Items["::code::"].Options[::position::] = {Code: '::optionCode::', Label: '::optionLabel::'};

@view.locationsMap
<div class="command onload">rendermaplayer(::idNode::,::codeDefinition::,::codeView::,mapviewer_::idNode::_::codeView::,mapviewer_::idNode::_::codeView::_options)</div>
<div id="mapviewer_::idNode::_::codeView::" style="height: 400px; width: 100%;"></div>
<div style="display:none;" id="mapviewer_::idNode::_::codeView::_options">
  var Options = {
    editable : false,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    place: "::place::",
    infoTemplate: "::infoTemplate::"
  };
</div>

@view.locationsMap$infoTemplate:client-side
<div class='infowindow'>
  <table width='100%'>
    <tr>
      ::picture|<td width='1px'>*</td>::
      <td>
        <div class='label'><a href=\\"javascript:CommandDispatcher.dispatch(\'shownode(\{id\})\');\\">\{label\}</a></div>
        ::lead|<div class='lead attributes'>*</div>::
        <div class='body'>
          <table width='100%'>
            <tr>
              <td width='80%'>
                ::line|<div class='line attributes'>*</div>::
                ::line_below|<div class='line_below attributes'>*</div>::
                ::highlight|<div class='highlight attributes'>*</div>::
              </td>
              <td>
                <div class='icons attributes'>
                  ::icon::
                </div>
              </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    ::footer|<tr><td colspan='2'><div class='footer attributes'>*</div></td></tr>::
  </table>
</div>

@view.locationsMap$infoTemplate$label
\{::code::\}

@view.locationsMap$infoTemplate$noLabel
No label

@view.locationsMap$infoTemplate$attribute
<span class='attribute invisible\{::length::\}'>
  <span class='title'>::label::\:</span>&nbsp;
  <span class='value'>\{::code::\}</span>&nbsp;&nbsp;&nbsp;
</span>

@view.locationsMap$infoTemplate$attribute.picture
<span class='attribute'><img src='::imagesUrl::' alt='::label::' class='picture attribute'/></span>

@view.locationsMap$infoTemplate$attribute.lead
<span class='attribute invisible\{::length::\}'>\{::code::\}</span>&nbsp;&nbsp;&nbsp;</span>

@view.locationsMap$infoTemplate$attribute.highlight
<span class='attribute invisible\{::length::\}'>\{::code::\}</span>&nbsp;&nbsp;&nbsp;</span>

@view.summary
::views::

@view.summary$view
<ul>
  <a class="command" href="shownodeview(::idMainNode::,::idNode::,::code::)">::label::</a><span class="count"> (::count::)</span>
  ::list::
</ul>

@view.summary$view$list
<label>::label::</label>
<ul>::items::</ul>

@view.summary$view$list$definitionLabel
Type

@view.summary$view$list$noItems
<li style="font-style:italic;">No elements</li>

@view.summary$view$list$item
<li><a class="command" href="shownodeview(::idMainNode::,::idNode::,::codeView::,::code::,::value::)">::label::</a><span class="count"> (::count::)</span></li>