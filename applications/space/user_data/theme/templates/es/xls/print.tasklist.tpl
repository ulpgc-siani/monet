<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn\:schemas-microsoft-com\:office\:spreadsheet" xmlns\:o="urn\:schemas-microsoft-com\:office\:office" xmlns\:x="urn\:schemas-microsoft-com\:office\:excel" xmlns\:ss="urn\:schemas-microsoft-com\:office\:spreadsheet" xmlns\:html="http\://www.w3.org/TR/REC-html40" xmlns\:xalan="http\://xml.apache.org/xalan">
 <DocumentProperties xmlns="urn\:schemas-microsoft-com\:office\:office">
  <Author>Monet</Author>
  <LastAuthor>Monet</LastAuthor>
  <Created>2012-07-20T07\:24\:01Z</Created>
  <Company>Microsoft</Company>
  <Version>12.00</Version>
 </DocumentProperties>
 <ExcelWorkbook xmlns="urn\:schemas-microsoft-com\:office\:excel">
  <WindowHeight>9480</WindowHeight>
  <WindowWidth>22068</WindowWidth>
  <WindowTopX>480</WindowTopX>
  <WindowTopY>84</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss\:ID="Default" ss\:Name="Normal">
   <Alignment ss\:Vertical="Bottom"/>
   <Borders/>
   <Font ss\:FontName="Calibri" x\:Family="Swiss" ss\:Size="11" ss\:Color="\#000000"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss\:ID="s62">
   <Font ss\:FontName="Calibri" x\:Family="Swiss" ss\:Size="20" ss\:Color="\#000000"/>
  </Style>
  <Style ss\:ID="s63">
   <Alignment ss\:Horizontal="Center" ss\:Vertical="Bottom"/>
   <Font ss\:FontName="Verdana" x\:Family="Swiss" ss\:Size="9" ss\:Color="\#FFFFFF"/>
   <Interior ss\:Color="\#1F497D" ss\:Pattern="Solid"/>
  </Style>
  <Style ss\:ID="s64">
   <Borders/>
  </Style>
  <Style ss\:ID="s73">
   <Font ss\:FontName="Calibri" x\:Family="Swiss" ss\:Color="\#5A5A5A"/>
  </Style>
 </Styles>
 <Worksheet ss\:Name="Listado">
  ::content::
  <WorksheetOptions xmlns="urn\:schemas-microsoft-com\:office\:excel">
   <PageSetup>
    <Header x\:Margin="0.3"/>
    <Footer x\:Margin="0.3"/>
    <PageMargins x\:Bottom="0.75" x\:Left="0.7" x\:Right="0.7" x\:Top="0.75"/>
   </PageSetup>
   <Unsynced/>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>0</PaperSizeIndex>
    <HorizontalResolution>600</HorizontalResolution>
    <VerticalResolution>600</VerticalResolution>
   </Print>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>4</ActiveRow>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>

@content
<Table ss\:ExpandedColumnCount="::headerCount::" ss\:ExpandedRowCount="::rowCount::" x\:FullColumns="1" x\:FullRows="1" ss\:DefaultColumnWidth="62.400000000000006" ss\:DefaultRowHeight="14.55">
 <Column ss\:AutoFitWidth="0" ss\:Width="99.6"/>
 <Column ss\:AutoFitWidth="0" ss\:Width="57"/>
 <Row ss\:AutoFitHeight="0" ss\:Height="25.8">
  <Cell ss\:StyleID="s62"><Data ss\:Type="String">::label::</Data></Cell>
 </Row>
 <Row ss\:AutoFitHeight="0">
  <Cell ss\:StyleID="s73"><Data ss\:Type="String">::date::</Data></Cell>
 </Row>
 ::header::
 ::rows::
</Table>

@content$offsetColumnCount
1

@content$offsetRowCount
4

@content.empty
<Table ss\:ExpandedColumnCount="::headerCount::" ss\:ExpandedRowCount="::rowCount::" x\:FullColumns="1" x\:FullRows="1" ss\:DefaultColumnWidth="62.400000000000006" ss\:DefaultRowHeight="14.55">
 <Column ss\:AutoFitWidth="0" ss\:Width="99.6"/>
 <Column ss\:AutoFitWidth="0" ss\:Width="57"/>
 <Row ss\:AutoFitHeight="0" ss\:Height="25.8">
  <Cell ss\:StyleID="s62"><Data ss\:Type="String">::label::</Data></Cell>
 </Row>
 <Row ss\:AutoFitHeight="0">
  <Cell ss\:StyleID="s73"><Data ss\:Type="String">::date::</Data></Cell>
 </Row>
 ::header::
 <Row ss\:AutoFitHeight="0">
  <Cell ss\:StyleID="s64"><Data ss\:Type="String">No existen elementos</Data></Cell>
 </Row>
</Table>

@content.empty$offsetColumnCount
2

@content.empty$offsetRowCount
5

@content$header
<Row ss\:Index="4" ss\:AutoFitHeight="0">
 ::attributes::
</Row>

@content$header$attribute
<Cell ss\:StyleID="s63"><Data ss\:Type="String">::label::</Data></Cell>

@content$row
<Row ss\:AutoFitHeight="0">
 ::attributes::
</Row>

@content$row$attribute
<Cell ss\:StyleID="s64"><Data ss\:Type="String">::value::</Data></Cell>

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