@view
::elements::

@row
<table width="100%"><tr>::columns::</tr></table>

@column
<td style='width\:::width::;height\:::height::'>::element::</td>

@element.section
<div class="section::opened| *::">
	::label|<a class="behaviour toggle" href="javascript:void(null)"><label>*</label></a>::
	::elements::
</div>

@element.box
<div style='margin-right:5px;'>::render(field)::</div>

@element.box.composite
::elements::

@element.space
&nbsp;

@element
falta por implementar este tipo