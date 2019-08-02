@content
::view::

@view.ancestor
<div class="ancestor-view">
  ::previousBlock::
  ::nextBlock::
</div>

@view.ancestor$previous.enabled
<a class="command label" href="shownodechild(::idNode::,edit.html?mode=page,::previous::,::count::)"><img src='::themeSource::&path=_images/icons/previous.png' alt="previous" title="previous"/></a>

@view.ancestor$previous.disabled
<img style='filter:grayscale(100%);-webkit-filter:grayscale(100%);' src='::themeSource::&path=_images/icons/previous.png' alt="previous" title="previous"/>

@view.ancestor$next.enabled
<a class="command label" href="shownodechild(::idNode::,edit.html?mode=page,::next::,::count::)"><img src='::themeSource::&path=_images/icons/next.png' alt="next" title="next"/></a>

@view.ancestor$next.disabled
<img style='filter:grayscale(100%);-webkit-filter:grayscale(100%);' src='::themeSource::&path=_images/icons/next.png' alt="next" title="next"/>

@toolbar
<div class="def configuration">
  var configuration = new Object();
  configuration.toolbar = new Object();
  configuration.toolbar.operationSet = new Array();
  configuration.toolbar.operationSet.push({type:"PRINT", label: "Download in PDF", parameters: {id:"::idNode::",format:"pdf",view:"::codeView::"}});
  configuration.sidebar = new Object();
  configuration.sidebar.operationSet = new Array();
  configuration.sidebar.operationSet.push({type:"PRINT", label: "Download in PDF", parameters: {id:"::idNode::",format:"pdf",view:"::codeView::"}});
  configuration.sidebar.operationSet.push({type:"PRINT", label: "Download in CSV", parameters: {id:"::idNode::",format:"csv",view:"::codeView::"}});
  configuration.sidebar.operationSet.push({type:"PRINT", label: "Download in Excel", parameters: {id:"::idNode::",format:"xls",view:"::codeView::"}});
  configuration.sidebar.operationSet.push({type:"HELP",parameters:{}});
</div>

@view.index
<div class="command onload">rendernodelist(::id::,::code::,::codeView::,listviewer_r_::id::_::codeView::,listviewer_r_::id::_::codeView::_options)</div>
<div id="listviewer_r_::id::_::codeView::"></div>

<div style="display:none;" id="listviewer_r_::id::_::codeView::_options">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.Templates.Item = "::indexTemplate::";
  Options.Templates.ShowItemCommand = "showsetitem(::id::,\#\{id\},edit.html?mode=page,\#\{index\},\#\{count\})";
  Options.Templates.NoItems = "&lt;div class='noitems'&gt;No existen ::labelReference::&lt;/div&gt;";
  Options.Templates.CountItems = "\#\{count\} ::labelReference::";
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["::defaultSortBy::"];
  Options.SortByList.Items = new Object();
  ::sortByList::
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
  ::groupByList::
</div>

@view.index$defaultLabelReference
items

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
                  <a class='attribute georef val0\#\{georeferenced\}' title='The element is not georeferenced' href='\#'></a>
                  <a class='attribute georef val1\#\{georeferenced\}' title='The element is not georeferenced' href='\#'></a>
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
<div class='label' title='\#\{::code::\}'>\#\{::code::_short\}</div>

@indexTemplate$noLabel
<div class='label'>Undefined</div>

@indexTemplate$attribute
<span class='attribute invisible\#\{::length::\}'>
  <span class='title'>::label::\:</span>&nbsp;
  <span class='value' title='\#\{::code::\}'>\#\{::code::_short\}</span>&nbsp;&nbsp;&nbsp;
</span>

@indexTemplate$attribute.picture
<span class='attribute'><img src='::imagesUrl::&nid=\#\{id\}&f=\#\{::code::\}&thumb=1' alt='::label::' class='picture attribute'/></span>

@indexTemplate$attribute.lead
<span class='attribute invisible\#\{::length::\}'>\#\{::code::\}</span>&nbsp;&nbsp;&nbsp;</span>

@indexTemplate$attribute.highlight
<span class='attribute invisible\#\{::length::\}'>\#\{::code::\}</span>&nbsp;&nbsp;&nbsp;</span>

@sortByList$item
Options.SortByList.Items["::code::"] = {"Code": "::code::", "Label": "::label::", "DefaultMode": "::mode::"};

@groupByList$item
Options.GroupByList.Items["::code::"] = new Object();
Options.GroupByList.Items["::code::"].Code = "::code::";
Options.GroupByList.Items["::code::"].Label = "::label::";
Options.GroupByList.Items["::code::"].Options = new Array();
::options::

@groupByList$item$typeLabel
Class

@groupByList$item$option
Options.GroupByList.Items["::code::"].Options[::position::] = {Code: '::optionCode::', Label: '::optionLabel::'};

@groupByList$item$option.standalone
Options[::position::] = {Code: '::optionCode::', Label: '::optionLabel::'};

@view.locationsMap
<div class="command onload">rendermaplayer(::idNode::,::codeDefinition::,::codeView::,mapviewer_::idNode::_::codeView::,mapviewer_::idNode::_::codeView::_options,filterswizard_::id::_::codeView::_options)</div>
<div id="mapviewer_::idNode::_::codeView::" style="height: 400px; width: 100%; margin-top: 10px;"></div>
<div style="display:none;" id="mapviewer_::idNode::_::codeView::_options">
  var Options = {
    editable : false,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    layer: "::layer::",
    place: "::place::",
    infoTemplate: "::infoTemplate::"
  };
</div>
<div style="display:none;" id="filterswizard_::id::_::codeView::_options">
  var Options = new Object();
  Options.Editable = false;
  Options.DataSource = new Object();
  Options.DataSource.Remote = true;
  Options.Templates = new Object();
  Options.SortByList = new Object();
  Options.SortByList.Selection = ["::defaultSortBy::"];
  Options.SortByList.Items = new Object();
  Options.GroupByList = new Object();
  Options.GroupByList.Selection = [];
  Options.GroupByList.Items = new Object();
  ::groupByList::
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
Undefined

@view.locationsMap$infoTemplate$attribute
<span class='attribute invisible\{::length::\}'>
  <span class='title'>::label::\:</span>&nbsp;
  <span class='value'>\{::code::\}</span>&nbsp;&nbsp;&nbsp;
</span>

@view.locationsMap$infoTemplate$attribute.picture
<span class='attribute'><img src='::imagesUrl::&nid=\{id\}&f=\{::code::\}&thumb=1' alt='::label::' class='picture attribute'/></span>

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
<li style="font-style:italic;">None element</li>

@view.summary$view$list$item
<li><a class="command" href="shownodeview(::idMainNode::,::idNode::,::codeView::,::code::,::value::)">::label::</a><span class="count"> (::count::)</span></li>
