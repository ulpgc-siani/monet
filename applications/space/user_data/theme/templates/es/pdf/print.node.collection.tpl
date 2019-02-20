@content
<fo:block font-size="20pt">::label::</fo:block>
<fo:block font-size="10pt" margin-bottom="2mm" color="\#666666">::date::</fo:block>
::filters::
<fo:block margin-bottom="6mm"></fo:block>
<fo:table table-layout="fixed">
	::referencesColumns::
	<fo:table-header>::referencesHeader::</fo:table-header>
	<fo:table-body>::references::</fo:table-body>
</fo:table>

@content.empty
<fo:block font-size="20pt">::label::</fo:block>
<fo:block font-size="10pt" margin-bottom="3mm" color="\#666666">::date::</fo:block>
::filters::
<fo:block>No existen elementos</fo:block>

@content$filters
<fo:block background-color="\#FAEDC3" font-size="12pt" margin-bottom="2mm" padding="1mm">
	Filtros
	::filters::
</fo:block>

@content$filter
<fo:block font-size="9pt" margin-left="4mm">::label::\: ::values::</fo:block>

@content$filter.lt
<fo:block font-size="9pt" margin-left="4mm">::label:: anterior a ::values::, inclusive</fo:block>

@content$filter.gt
<fo:block font-size="9pt" margin-left="4mm">::label:: posterior a ::values::, inclusive</fo:block>

@content$filter.condition
<fo:block font-size="9pt" margin-left="4mm">Condición\: ::value::</fo:block>

@content$filters.empty

@content$reference.column
<fo:table-column column-width="::width::%"/>

@content$reference.header
<fo:table-row background-color="\#EFF7FF" margin-bottom="2mm" border-top="0.2mm solid \#5E93B1" border-bottom="0.1mm solid \#5E93B1">
	::attributes::
</fo:table-row>

@content$reference.header$attribute
<fo:table-cell padding-left="0.5mm" padding-top="2mm" padding-bottom="2mm" wrap-option="wrap">
	<fo:block font-size="10pt" font-weight="bold">::label::</fo:block>
</fo:table-cell>

@content$reference.header$attribute.emptyLabel

@content$reference
<fo:table-row::odd| background-color="\#ddd"::>
	::attributes::
</fo:table-row>

@content$reference$attribute
<fo:table-cell padding-left="0.5mm" font-size="8pt" padding-top="2mm" padding-bottom="2mm"><fo:block>::value::</fo:block></fo:table-cell>

@content$reference$attribute.picture
<fo:table-cell padding-left="0.5mm" font-size="8pt" padding-top="2mm" padding-bottom="2mm"><fo:block><fo:external-graphic src="url('::value::')" content-height="scale-to-fit" height="1.00in" content-width="1.00in" scaling="non-uniform"/></fo:block></fo:table-cell>

@attribute$label.code
Clase