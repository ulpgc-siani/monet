::view::

@view
<div class="field ::code:: nid::idNode:: f::type:: ::super| *::::required| *::::extended| *::::multiple| *::::tableView| tableview::::conditional| *::::extensible| *::::allowOthers| *::::lock| *::">
  ::label::
  ::declarations::
  <input type="hidden" class="data::root| *::" name="::code::" class="data" value="::value::"></input>
  ::field::
</div>

@label
<label>::label::</label>

@label.composite
<label>::label::</label>
::conditioned::

@label.empty
<label>No se ha definido etiqueta</label>

@label$conditioned

@label$conditioned.no

@declarations
<div class="def ititle">::description::</div>
::default|<p class="def default">*</p>::
::messageIfEmpty|*::
::messageIfEditing|*::
::messageIfRequired|*::
::concreteDeclarations::

@declarations$messageIfEmpty
<div class="def msgwhenempty">::message::</div>

@declarations$messageIfEditing
<div class="def ihelp">::message::</div>

@declarations$messageIfRequired
<div class="def msgwhenrequired">::message::</div>

@declarations$default.multiple
<attribute code="::code::">
::attributes::
</attribute>

@declarations$default.single
<attribute code="::code::">
::codeIndicator|<indicator code="code">*</indicator>::
::checkedIndicator|<indicator code="checked">*</indicator>::
::valueIndicator|<indicator code="value">*</indicator>::
</attribute>

@field.multiple
<div id="::id::" class="widget wlist::extended| *::::conditioned| *::">
  <ul class="list">::elements::</ul>
  <div class="template">::template::</div>
  <a class="behaviour more button" href="more()">Añadir</a>
</div>

@field.multiple$element
<li class="element">
  <table width="100%">
    <tr>
      <td width="24px">
        <div class="elementoptions">
          <a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar"></a>
        </div>
      </td>
      <td>
        <div class="elementwidget">
          <a class="delete" alt="borrar" title="borrar">&nbsp;</a>
          ::fieldSingle::
        </div>
      </td>
    </tr>
  </table>
</li>

@field.multiple.table
<div id="::id::" class="widget wtable::extended| *::::conditioned| *::::tableView| tableview::">
  <table width="100%">
    <tr><td><input class="filter"></input><span class="filterempty">Introduzca aquí los términos para filtrar</span><a class="filterclear" href="javascript:void(null)" alt="borrar"></a></td></tr>
    <tr>::tableView::</tr>
    <tr>
      <td>
        <div class="toolbar">
          <a class="behaviour more button" href="more()">Añadir</a>&nbsp;&nbsp;
          <a class="behaviour remove" href="remove()" style="display:none;"><img class="trigger" alt="Eliminar selección" title="Eliminar selección" src="::themeSource::&path=_images/s.gif"></img>Eliminar selección...</a>
        </div>
      </td>
    </tr>
    <tr style="display:none;"><td><div class="toolbar item"><a class="hidelist" href="javascript:void(null)">ocultar sección</a><table class="paging"><tr><td><a class="previous" href="javascript:void(null)"></a></td><td><span class="current"></span></td><td><a class="next" href="javascript:void(null)"></a></td></tr></table></div></td></tr>
    <tr><td><ul class="list"><li class="element">::elements::</li></ul></td></tr>
  </table>
</div>

@field.multiple.table$tableView
<td>
  <div class="table_envelope">
    <ul class="table">
      <li class="header">
        <table width="100%">
          <tr>
            <th class="flag"><div>&nbsp;</div></th>
            ::headerColumns::
            <th width="3%"></th>
          </tr>
        </table>
      </li>
      ::elements::
    </ul>
  </div>
  <div class="rowtemplate" style="display:none;">
    <li class="element" id="\#\{id\}">
      <table width="100%" height="100%">
        <tr>
          <td class="flag">
            <input type="checkbox" class="check"/>
            <a class="close" alt="Haga click para abrir el elemento" style="display:none;">&nbsp;</a>
          </td>
          ::templateColumns::
          <td class="flag right" width="3%">
            <a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar">&nbsp;</a>
          </td>
        </tr>
      </table>
    </li>
  </div>
</td>

@field.multiple.table$tableView$emptyLabel
Sin etiqueta

@field.multiple.table$tableView$headerColumn
<th width="::width::%"><div>::label::</div></th>

@field.multiple.table$tableView$templateColumn
<td width="::width::%"><div class="::link|link ::value ::code::">\#\{::code::\}&nbsp;</div></td>

@field.multiple.table$tableView$element
<li class="element">
  <table width="100%" height="100%">
    <tr>
      <td class="flag">
        <input type="checkbox" class="check"/>
        <a class="close" alt="Haga click para abrir el elemento" style="display:none;">&nbsp;</a>
      </td>
      ::columns::
      <td class="flag right" width="3%">
        <a class="move" alt="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar" title="Mantenga pulsado botón izquierdo del ratón para arrastrar y soltar">&nbsp;</a>
      </td>
    </tr>
  </table>
</li>

@field.multiple.table$tableView$element$column
<td width="::width::%"><div class="::link|link ::value ::code::">::value::</div></td>

@field.multiple.table$element
<div class="elementwidget">
  ::fieldSingle::
</div>

@field.single
<div style='::compositeHidden|display:none;::' class="widget w::type::::memo| long::::extended| *::::conditioned| *::">
  ::super|<div class="def super">*</div>::
  ::body::
</div>

@field.composite
<input type="text" class="component hidden" id="::id::" value="::value::"></input>
<div class="fieldsbox">::fields::</div>

@field.composite$field
::render(view.field)::

@field.composite$concreteDeclarations
::labelField|<p class="def labelfield">*</p>::

@field.text
<input type="text" class="component" id="::id::" name="::code::" value="::value::"/>

@field.text$pattern
<textarea class="def pattern" name="::indicators::">::regexp::</textarea>

@field.text$pattern$indicator
::comma|,::::indicator::

@field.text$concreteDeclarations
::allowHistory|<p class="def history store">*</p>::
::edition|<p class="def text edition">*</p>::
::length|<p class="def length">*</p>::
::patterns::

@field.memo
<textarea class="component" id="::id::">::value::</textarea>

@field.memo$concreteDeclarations
::allowHistory|<p class="def history store">*</p>::
::edition|<p class="def text edition">*</p>::
::length|<p class="def length">*</p>::

@field.boolean
::checked::<label for="::id::" class="box">::label::</label>

@field.boolean$label
Sí/No

@field.boolean$checked
<input id="::id::" type="checkbox" class="component" checked/>

@field.boolean$unchecked
<input id="::id::" type="checkbox" class="component"/>

@field.number
::metrics::
<input type="text" class="component" id="::id::" value="::value::"/>

@field.number$concreteDeclarations
::range::
::format|<p class="def format">*</p>::
::metrics::

@field.number$metrics
<select class="metrics">::items::</select>

@field.number$metrics.declaration
<select class="def metrics">::items::</select>

@field.number$metrics$item
<option value="::code::"::isSelected| selected::::equivalence| class="*"::>::label::</option>

@field.number$metrics$item.declaration
<option value="::code::"::isDefault| selected::::equivalence| class="*"::>::label::</option>

@field.number$range
<p class="def range">::min::,::max::,1</p>

@field.date
<input type="text" class="component" id="::id::" value="::internal::" />

@field.date$concreteDeclarations
::format|<p class="def format">*</p>::
::edition|<p class="def edition">*</p>::

@field.file
<input type="text" class="component" value="::label::" />
<input type="hidden" class="component_hidden" id="::id::" value="::value::" />

@field.file.empty
<input type="text" class="component" />
<input type="hidden" class="component_hidden" id="::id::" />

@field.file$concreteDeclarations
::limit|<p class="def limit">*</p>::

@field.picture
<input type="text" class="component" id="::id::" value="::value::" />
<div class="pictureframe">
  <img class="thumbnail" src="::thumbPhotoLink::"/>
</div>

@field.picture$concreteDeclarations
::size|<p class="def size">*</p>::
::limit|<p class="def limit">*</p>::

@field.picture$size
::width::,::height::

@field.picture$title
&lt;a href=&quot;::photoLink::&quot;&gt;Descargar&lt;/a&gt;

@field.picture.noLightBox
<input type="text" class="component" id="::id::" value="::value::" />
<div class="pictureframe">
  <img class="thumbnail" src="::thumbPhotoLink::"/>
</div>

@field.select
<input type="text" class="component" id="::id::" value="::value::" name="::code::" alt="::source::"/>

@field.select.embedded
<input type="hidden" class="component" value="::value::" name="::code::" alt="::source::"/>
<ul class="radiolist component">::items::</ul>

@field.select.embedded$item
<li id="::id::" class="radio ancestorlevel_::ancestorLevel::"><input type="radio" class="::code::" name="::id::" value="::label::" ::checked| checked::><label>::allowCode::::label::</label></li>

@field.select.embedded$item.category
<li id="::id::" class="radio ancestorlevel_::ancestorLevel:: category"><label>::allowCode::::label::</label></li>

@field.select.embedded$item.super
<li id="::id::" class="radio ancestorlevel_::ancestorLevel:: superterm"><input type="radio" class="::code::" name="::id::" value="::label::" ::checked| checked::><label>::allowCode::::label::</label></li>

@field.select.other
<input type="text" class="component other" id="::id::" value="::other::" name="::code::" alt="::source::"/>

@field.select$concreteDeclarations
<select class="def sources">::declarationSources::</select>
::declarationTerms::
::allowHistory|<p class="def history store::::allowCode| showcode::::allowHistory|">*</p>::
::allowSearch|<p class="def index store::::allowCode| showcode::::allowSearch|">*</p>::

@field.select$declarationSource
<option value="::id::" class="::partner::">::label::</option>

@field.select$declarationTerms
<p class="def source store::allowCode| showcode::">::source::</p>
<p class="def store filters">::filters::</p>
::flatten|<p class="def store flatten">*</p>::
::depth|<p class="def store depth">*</p>::
::from|<p class="def store from">*</p>::
::partnerContext|<p class="def store partnercontext">*</p>::

@field.select$declarationTerms.inline
<select class="def source store::allowCode| showcode::">::terms::</select>

@field.select$allowCode
::code::\&nbsp;\&nbsp;-\&nbsp;\&nbsp;

@field.select$declarationTerms.inline$term
<option value="::code::" class="ancestorlevel_::ancestorLevel::">::allowCode::::label::</option>

@field.select$declarationTerms.inline$term.category
<div class="ancestorlevel_::ancestorLevel:: category">::allowCode::::label::</div>

@field.select$declarationTerms.inline$term.super
<option value="::code::" class="ancestorlevel_::ancestorLevel:: superterm">::allowCode::::label::</option>

@field.link
<input type="text" class="component" id="::id::" value="::value::" name="::code::" />
<div class="nodebox"></div>

@field.link.empty
<input type="text" class="component" id="::id::" value="::value::" name="::code::" />
<div class="nodebox"></div>

@field.link$concreteDeclarations
<p class="def data domain">::source::</p>
<p class="def data link">::source::</p>
::allowLocations|<p class="def data link_locations">*</p>::
<p class="def store filters">::filters::</p>
<p class="def nodetypes">::nodeTypes::</p>
<p class="def tpl view">preview.html?mode=page</p>
<p class="def tpl edit">edit.html?mode=page</p>
::collection|<p class="def field_collection">*</p>::
::allowOther|<p class="def other">*</p>::
::allowHistory|<p class="def history store">*</p>::
::allowSearch|<p class="def index store">*</p>::
::header::

@field.link$concreteDeclarations$nodeType
::comma|,::::nodeType::

@field.link$concreteDeclarations$header
<select class="def header">::attributes::</select>

@field.link$concreteDeclarations$header$attribute
<option value="::code::"::class| class="*"::>::label::</option>

@field.check
<ul class="checklist component" type="::source::">::items::</ul>

@field.check$allowCode
::code::\&nbsp;\&nbsp;-\&nbsp;\&nbsp;

@field.check$item
<li class="check ancestorlevel_::ancestorLevel::::parent| parent_*::">::checked::<label for="::id::">::allowCode::::label::</label></li>

@field.check$item.category
<li class="check ancestorlevel_::ancestorLevel::::parent| parent_*:: category">
  ::checked::<label for="::id::">::allowCode::::label::</label>
  <span style="float:left; margin-top: 2px; margin-left: 13px; display:none;">
    <a class="behaviour checkall">todos</a>&nbsp;|&nbsp;
    <a class="behaviour uncheckall">ninguno</a>
  </span>
</li>

@field.check$item.super
<li class="check ancestorlevel_::ancestorLevel:: superterm">::checked::<label for="::id::">::allowCode::::label::</label></li>

@field.check$item$checked
<input id="::id::" name="::code::" value="::label::" type="checkbox" class="checker" checked/>

@field.check$item$unchecked
<input id="::id::" name="::code::" value="::label::" type="checkbox" class="checker"/>

@field.check$concreteDeclarations
<select class="def sources">::declarationSources::</select>
<p class="def node">::nodeId::</p>
::from|<p class="def store from">*</p>::

@field.check$declarationSource
<option value="::id::" class="::partner::">::label::</option>

@field.node
<input type="hidden" class="component" id="::id::" value="::value::" name="::code::"/>
<div class="nodebox">::node::</div>
<a class="button add" alt="Crear elemento" title="Crear elemento" style="display:none;width:30px;margin-top:5px;cursor:pointer;">Crear</a>

@field.node$concreteDeclarations
::nodeTypes|<p class="def nodetypes">*</p>::
<p class="def tpl view">preview.html?view=::codeView::</p>
<p class="def tpl edit">edit.html?view=::codeView::</p>
<p class="def allowadd">::allowAdd::</p>

@field.node$concreteDeclarations.document
::nodeTypes|<p class="def nodetypes">*</p>::
<p class="def tpl view">preview.html?view=form</p>
<p class="def tpl edit">edit.html?view=form</p>
<p class="def allowadd">::allowAdd::</p>

@field.node$node

@field.node$node.document
  
@field.node$node.empty

@field.serial
<input type="text" class="component" readonly="true" id="::id::" value="::value::" name="::code::"/>

@field.serial$concreteDeclarations
::format|<p class="def format">*</p>::

@field.location
<input type="text" class="component" readonly="true" id="::id::" value="::value::" name="::code::"/>

@field.summation
<input type="text" class="component" readonly="true" id="::id::" value="::value::"/>
<ul class="summationlist" style="display:none;">
  ::items::
</ul>
<a class="close">&nbsp;</a>
<div class="template simple" style="display:none;">
  <li class="summationitem simple multiple">
    <table>
      <tr>
        <td width="80%">
          <div class="widget wselect tabbed">
            <img src="::themeSource::&path=_images/s.gif" alt="::label::" class="none"/>
            <input type="text" class="component tabbed label"/>
          </div>      
        </td>
        <td width="20%">
          <div class="widget wnumber">
            <input type="text" class="component" value="0"></input>
          </div>
        </td>
      </tr>
    </table>
  </li>
</div>

@field.summation$item.composite
<li class="summationitem subtotal ::evenOdd::::type| *::::multiple| *::::negative| *::">
  <table>
    <tr>
      <td width="80%">::label::</td>
      <td width="20%"><input type="text" class="value readonly" value="::value::" readonly="true"></input></td>
    </tr>
  </table>
  <ul>::items::</ul>
</li>

@field.summation$item.simple
<li class="summationitem ::evenOdd::::type| *::::multiple| *::::negative| *::">
  <table::multiple| class="multiple"::>
    <tr>
      <td width="80%">::label::</td>
      <td width="20%">
        <div class="widget wnumber">
          <input type="text" class="component" value="::value::"></input>
        </div>
      </td>
    </tr>
    </tr>
  </table>
</li>

@field.summation$item.simple$label.single
<div class="tabbed label"><img src="::themeSource::&path=_images/s.gif" alt="::label::" class="none"/><span>::label::</span></div>

@field.summation$item.simple$label.single$expandable
<a class="tabbed label"><img src="::themeSource::&path=_images/s.gif" alt="::label::"/><span>::label::</span></a>

@field.summation$item.simple$label.multiple
<div class="widget wselect tabbed">
  <img src="::themeSource::&path=_images/s.gif" alt="::label::" class="none"/>
  <input type="text" class="component tabbed label" value="::label::"/>
</div>

@field.summation$concreteDeclarations
<p class="def source store::allowCode| showcode::">::source::</p>
::flatten|<p class="def store flatten">*</p>::
::depth|<p class="def store depth">*</p>::
::from|<p class="def store from">*</p>::
::format|<p class="def format">*</p>::

@field.unknown
<div class="compiler error">Tipo de campo desconocido</div>