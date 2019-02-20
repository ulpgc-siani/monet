::view::

@view
<div class="field readonly ::code:: nid::idNode:: f::type:: ::super| *::::required| *::::extended| *::::multiple| *::::tableView| tableview::::conditional| *::::extensible| *::::allowOthers| *::">
  ::label::
  <input type="hidden" name="::code::" class="data" value="::value::"></input>
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
<span class="option">Si</span>

@label$conditioned.no
<span class="option">No</span>

@field.multiple
<div id="::id::" class="widget wlist::extended| *::::conditioned| *::">
  <ul class="list">
    ::emptyElement::
    ::elements::
  </ul>
</div>

@field.multiple$element
<li class="element">
  <div class="elementwidget">
    ::fieldSingle::
  </div>
</li>

@field.multiple.table
<div id="::id::" class="widget wtable::extended| *::::conditioned| *::::tableView| tableview::">
  <table width="100%">
    <tr><td><input class="filter"></input><span class="filterempty">Introduzca aquí los términos para filtrar</span><a class="filterclear" href="javascript:void(null)" alt="borrar"></a></td></tr>
    <tr>::tableView::</tr>
    <tr style="display:none;"><td>&nbsp;</td></tr>
    <tr style="display:none;"><td style="display:none !important;"><div class="toolbar item"><a class="hidelist" href="javascript:void(null)">ocultar sección</a><table class="paging"><tr><td><a class="previous" href="javascript:void(null)"></a></td><td><span class="current"></span></td><td><a class="next" href="javascript:void(null)"></a></td></tr></table></div></td></tr>
    <tr><td style="display:none !important;"><ul class="list"></ul></td></tr>
  </table>
</div>

@field.multiple.table$tableView
<td>
  <div class="table_envelope">
    <ul class="table">
      <li class="header"><table width="100%"><tr><th class="flag"><div>&nbsp;</div></th>::headerColumns::</tr></table></li>
      ::elements::
    </ul>
  </div>
</td>

@field.multiple.table$tableView$emptyLabel
Sin etiqueta

@field.multiple.table$tableView$headerColumn
<th width="::width::%"><div>::label::</div></th>

@field.multiple.table$tableView$element
<li class="element readonly">
  <table width="100%" height="100%">
    <tr class="reference">
      <td class="flag" width='26px' class="check"><div>&nbsp;</div></td>
      <td width="100%">
        <a class="command link" href="loadnodefieldcompositeitem(::id::,::code::,::position::)">
          <table width="100%">
            <tr>::columns::</tr>
          </table>
        </a>
      </td>
    </tr>
    <tr class="box" style="display:none;">
      <td class="flag" width='26px'>
        <div>&nbsp;</div>
        <a class="command close" href="loadnodefieldcompositeitem(::id::,::code::,::position::)" alt="Haga click para abrir el elemento">&nbsp;</a>
      </td>
      <td width="100%">
        <div class="content"></div>
      </td>
    </tr>
  </table>
</li>

@field.multiple.table$tableView$element$column
<td width="::width::%"><div class="value">::value::</div></td>

@field.multiple.table$element
<li class="element readonly">
  <div class="elementwidget">
    ::fieldSingle::
  </div>
</li>

@field.single
<div style='::compositeHidden|display:none;::' class="widget w::type::::memo| long::::extended| *::::conditioned| *::">
  ::super|<div class="def super">*</div>::
  ::body::
</div>

@field.composite
<input type="text" class="component hidden" id="::id::" readonly="yes" value="::value::"></input>
<div class="fieldsbox">::fields::</div>

@field.composite$field
::render(view.field)::

@field.text
<a type="text" class="component text" id="::id::" name="::code::">::value::</a>

@field.memo
<a class="component memo" id="::id::" >::value::</a>

@field.boolean
<div class="capsule">
  ::checked::
  <a class="component boolean">::label::</a>
</div>

@field.boolean$label
::unchecked|No ::::declarationLabel::

@field.boolean$checked
<img alt="Marcado" title="Marcado" class="checker" src="::themeSource::&path=_images/icons/check_disabled.gif" />

@field.boolean$unchecked
<img alt="Desmarcado" title="Desmarcado" class="checker" src="::themeSource::&path=_images/icons/uncheck_disabled.gif" />

@field.number
<a type="text" class="component number" id="::id::">::value:: ::metricLabel::</a>

@field.number$range

@field.date
<a type="text" class="component date" id="::id::">::value::</a>
<script type="text/javascript">
	var _date = Date.parseDate("::internal::", DATE_FORMAT_INTERNAL);
	if (_date != null) {
		document.getElementById("::id::").innerHTML = _date.format(Date.getPattern("::format::"));
	}
</script>

@field.file
<a type="text" class="component file" alt="Descargar documento" title="Descargar documento" id="::id::" href="::action::">::label::</a>

@field.file.empty
<a type="text" class="component file" id="::id::"></a>

@field.picture
<a type="text" class="picture" href="::photoLink::" title="::title::" id="::id::" rel="::lightBox::" alt="Ampliar">
  <div class="pictureframe">
    <img class="thumbnail" src="::thumbPhotoLink::"/>
  </div>
</a>
<script type="text/javascript">
  myLightbox.updateImageList();
</script>

@field.picture$title
&lt;a href=&quot;::photoLink::&quot;&gt;Descargar&lt;/a&gt;

@field.picture.noLightBox
<div class="pictureframe">
  <img class="thumbnail" src="::thumbPhotoLink::"/>
</div>
<script type="text/javascript">
  myLightbox.updateImageList();
</script>

@field.select
<a type="text" class="component select" id="::id::" name="::code::">::value::</a>

@field.select.embedded
<a type="text" class="component select" id="::id::" name="::code::">::value::</a>

@field.select.other
<a type="text" class="component select ::other| other::" id="::id::" name="::code::">::value::::other| (otro)::</a>

@field.select$declarationTerms

@field.select$declarationTerms.inline

@field.select$declarationTerms.inline$term

@field.link
<a type="text" class="component link command" id="::id::" name="::code::" alt="::value| Ir al elemento *" title="::value| Ir al elemento *" href="showlinknode(::idNode::,::code::)">::value::</a>
<div class="nodebox"></div>

@field.link.empty
<a type="text" class="component link" id="::id::" name="::code::">::value::</a>
<div class="nodebox"></div>

@field.check
<ul class="checklist">::items::</ul>

@field.check$item
<li class="check ancestorlevel_::ancestorLevel::">::checked::<a type="text" class="check">::label::</a></li>

@field.check$item.category
<li class="check ancestorlevel_::ancestorLevel:: category"><label for="::id::">::label::</label></li>

@field.check$item.super
<li class="check ancestorlevel_::ancestorLevel:: superterm">::checked::<label for="::id::">::label::</label></li>

@field.check$item$checked
<img alt="Marcado" title="Marcado" class="checker" src="::themeSource::&path=_images/icons/check_disabled.gif" />

@field.check$item$unchecked
<img alt="Desmarcado" title="Desmarcado" class="checker" src="::themeSource::&path=_images/icons/uncheck_disabled.gif" />

@field.node
<div class="nodebox">::node::</div>

@field.node$node
::render(view.node)::

@field.node$node.document
<a class="command" href="shownode(::id::)" alt="::label::" style="float\:left;"><img style="border\:1px solid \#666;" src="::previewImageSource::" title="::label::"/></a>
<div class="toolbar" style="float:left;border-bottom:0px;clear:both;margin: 10px 0px;padding-bottom:10px;"><a class="command button" href="shownode(::id::)" style="width:60px;display:inline;text-align:center;margin:0px;">abrir</a>\&nbsp;\&nbsp;\&nbsp;<a class="command button" href="downloadnode(::id::)" style="width:60px;display:inline;text-align:center;margin:0px;">descargar</a></div>
  
@field.node$node.empty
<div style="color:\#666;font-style:italic;">No hay elemento</div>

@field.serial
<a type="hidden" class="component serial" id="::id::" name="::code::">::value::</a>

@field.location
<a type="hidden" class="component location" id="::id::" name="::code::">::value::</a>

@field.summation
<input type="text" class="component" readonly="true" id="::id::" value="::value::"/>
<ul class="summationlist" style="display:none;">
  ::items::
</ul>
<a class="close">&nbsp;</a>

@field.summation$item.composite
<li class="summationitem subtotal ::evenOdd::::type| *::::multiple| *::::negative| *::">
  <table>
    <tr>
      <td width="80%">::label::</td>
      <td width="20%"><a type="hidden" class="value summationitem readonly">::value::</a></td>
    </tr>
  </table>
  <ul>::items::</ul>
</li>

@field.summation$item.simple
<li class="summationitem ::evenOdd::::type| *::::multiple| *::::negative| *::">
  <table::multiple| class="multiple"::>
    <tr>
      <td width="80%">::label::</td>
      <td width="20%"><a type="hidden" class="value summationitem readonly">::value::</a></td>
    </tr>
    </tr>
  </table>
</li>

@field.summation$item.simple$label.single
<div class="tabbed label"><img src="::themeSource::&path=_images/s.gif" alt="::label::" class="none"/><span>::label::</span></div>

@field.summation$item.simple$label.single$expandable
<a class="tabbed label"><img src="::themeSource::&path=_images/s.gif" alt="::label::"/><span>::label::</span></a>

@field.summation$item.simple$label.multiple
<div class="tabbed label"><img src="::themeSource::&path=_images/s.gif" alt="::label::" class="none"/><span>::label::</span></div>

@field.unknown
<div class="compiler error">Tipo de campo desconocido</div>