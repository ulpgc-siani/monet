<?xml version='1.0' encoding='UTF-8'?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <fo:layout-master-set>
    <fo:simple-page-master master-name="cover" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="2cm" margin-left="2.5cm" margin-right="2.5cm">
      <fo:region-body margin-top="10cm" />
    </fo:simple-page-master>

    <fo:simple-page-master master-name="page-portrait" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="2cm" margin-left="2.5cm" margin-right="2.5cm">
      <fo:region-body margin-top="1cm" margin-bottom="1cm"/>
      <fo:region-before precedence="true" extent="1cm"/>
      <fo:region-after precedence="true" extent="0.5cm"/>
      <fo:region-start extent="1cm"/>
      <fo:region-end extent="1cm"/>
    </fo:simple-page-master>

    <fo:simple-page-master master-name="page-landscape" page-height="21cm" page-width="29.7cm" margin-top="1cm" margin-bottom="2cm" margin-left="1cm" margin-right="1cm">
      <fo:region-body margin-top="1cm" margin-bottom="1cm"/>
      <fo:region-before precedence="true" extent="1cm"/>
      <fo:region-after precedence="true" extent="0.5cm"/>
      <fo:region-start extent="1cm"/>
      <fo:region-end extent="1cm"/>
    </fo:simple-page-master>

    <fo:simple-page-master master-name="section" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="2cm" margin-left="2.5cm" margin-right="2.5cm">
      <fo:region-body margin-top="10cm" />
    </fo:simple-page-master>
  </fo:layout-master-set>

  <fo:page-sequence master-reference="page-::orientation::">
    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="end" font-size="10pt" font-family="serif" line-height="14pt" color="\#555"></fo:block>
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="10pt" font-family="serif" line-height="14pt" color="\#555" border-top="0.5pt solid \#555">::label:: - Página <fo:page-number/></fo:block>
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-start">
      <fo:block-container height="22.2cm" width="1cm" top="0cm" left="0cm" position="absolute">
        <fo:block text-align="start"></fo:block>
      </fo:block-container>
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-end">
      <fo:block-container height="22.2cm" width="1cm" top="0cm" left="0cm" position="absolute">
        <fo:block text-align="start"></fo:block>
      </fo:block-container>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">::content::</fo:flow>

  </fo:page-sequence>  
</fo:root>

@content
<fo:block font-size="20pt" margin-bottom="5mm" >::label::</fo:block>
<fo:block font-size="10pt" margin-bottom="6mm" color="\#666666">::date::</fo:block>
<fo:table table-layout="fixed">
	::columns::
	<fo:table-header>::header::</fo:table-header>
	<fo:table-body>::rows::</fo:table-body>
</fo:table>

@content.empty
<fo:block font-size="20pt" margin-bottom="5mm" >::label::</fo:block>
<fo:block font-size="10pt" margin-bottom="6mm" color="\#666666">::date::</fo:block>
<fo:block>No existen tareas</fo:block>

@content$label
Tareas

@content$column
<fo:table-column column-width="::width::%"/>

@content$header
<fo:table-row background-color="\#EFF7FF" margin-bottom="2mm" border-top="0.2mm solid \#5E93B1" border-bottom="0.1mm solid \#5E93B1">
	::attributes::
</fo:table-row>

@content$header$attribute
<fo:table-cell padding-left="0.5mm" padding-top="2mm" padding-bottom="2mm" wrap-option="wrap">
	<fo:block font-size="10pt" font-weight="bold">::label::</fo:block>
</fo:table-cell>

@content$row
<fo:table-row::odd| background-color="\#ddd"::>
	::attributes::
</fo:table-row>

@content$row$attribute
<fo:table-cell padding-left="0.5mm" font-size="8pt" padding-top="2mm" padding-bottom="2mm"><fo:block>::value::</fo:block></fo:table-cell>

@content$row$attribute.urgent.true
Sí

@content$row$attribute.urgent.false
No

@content$row$attribute.state.new
Nueva

@content$row$attribute.state.pending
Activa

@content$row$attribute.state.waiting
Pendiente

@content$row$attribute.state.expired
Expirada

@content$row$attribute.state.finished
Finalizada

@content$row$attribute.state.aborted
Abortada

@content$row$attribute.state.failure
Con error