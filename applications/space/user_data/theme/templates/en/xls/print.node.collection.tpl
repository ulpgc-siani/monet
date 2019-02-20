@content
<Table ss\:ExpandedColumnCount="::referencesHeaderCount::" ss\:ExpandedRowCount="::referencesCount::" x\:FullColumns="1" x\:FullRows="1" ss\:DefaultColumnWidth="62.400000000000006" ss\:DefaultRowHeight="14.55">
 <Column ss\:AutoFitWidth="0" ss\:Width="99.6"/>
 <Column ss\:AutoFitWidth="0" ss\:Width="57"/>
 <Row ss\:AutoFitHeight="0" ss\:Height="25.8">
  <Cell ss\:StyleID="s62"><Data ss\:Type="String">::label::</Data></Cell>
 </Row>
 <Row ss\:AutoFitHeight="0">
  <Cell ss\:StyleID="s73"><Data ss\:Type="String">::date::</Data></Cell>
 </Row>
 ::referencesHeader::
 ::references::
</Table>

@content$offsetColumnCount
1

@content$offsetRowCount
4

@content.empty
<Table ss\:ExpandedColumnCount="2" ss\:ExpandedRowCount="3" x\:FullColumns="1" x\:FullRows="1" ss\:DefaultColumnWidth="62.400000000000006" ss\:DefaultRowHeight="14.55">
 <Column ss\:AutoFitWidth="0" ss\:Width="99.6"/>
 <Column ss\:AutoFitWidth="0" ss\:Width="57"/>
 <Row ss\:AutoFitHeight="0" ss\:Height="25.8">
  <Cell ss\:StyleID="s62"><Data ss\:Type="String">::label::</Data></Cell>
 </Row>
 <Row ss\:AutoFitHeight="0">
  <Cell ss\:StyleID="s73"><Data ss\:Type="String">::date::</Data></Cell>
 </Row>
 <Row ss\:AutoFitHeight="0">
   <Cell ss\:StyleID="s64"><Data ss\:Type="String">No existen elementos</Data></Cell>
 </Row>
</Table>

@content$reference.header
<Row ss\:Index="4" ss\:AutoFitHeight="0">
  ::attributes::
</Row>

@content$reference.header$attribute
<Cell ss\:StyleID="s63"><Data ss\:Type="String">::label::</Data></Cell>

@content$reference
<Row ss\:AutoFitHeight="0">
  ::attributes::
</Row>

@content$reference$attribute
<Cell ss\:StyleID="s64"><Data ss\:Type="String">::value::</Data></Cell>

@attribute$label.code
Clase