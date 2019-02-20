chartInfo = {
isEmpty \: ::isEmpty::,
VerticalAxis \: [
::verticalAxis::
],
IndicatorsAxisMember \: [
::indicatorsAxisMember::
]
};

dataTable = new google.visualization.DataTable();
::columns::
dataTable.addRows([::rows::]);

@axis
{ measureUnit\: "::measureUnit::", label\: "::measureUnitLabel:: ::scale|\(*\)::"::minValue|,minValue\:*::::maxValue|,maxValue\:*::::useBaseLine|,useBaseLine\:*:: }::comma|,::

@indicatorAxisMember
{ indicator\: "::indicator::", axisPosition\: ::axisMemberPosition::, columnPosition\: ::columnPosition:: }::comma|,::

@column
dataTable.addColumn('number', '::label::');

@column.tooltip
dataTable.addColumn({type:'string',role:'tooltip','p':{'html': true}});

@column.date
dataTable.addColumn('string', 'Fecha');

@column.time
dataTable.addColumn('string', 'Fecha');

@column.id
dataTable.addColumn('string', 'ID');

@row
[::indicators::]::comma|,::

@row$indicator
::value::::tooltip|,'*'::::comma|,::

@row$indicator.id
''

@row$value
"::value::"

@row$value.date
new Date(::year::,::month::,::day::)

@row$value.time
new Date(::year::,::month::,::day::,::hour::,::minute::,::second::)

@row$formatted
"::value::"

@row$formatted.date
"::formattedDay::, ::day:: de ::formattedMonth:: de ::year::"

@row$formatted.date.year
"::year::"

@row$formatted.date.month
"::formattedMonth::"

@row$formatted.date.month.full
"::formattedMonth:: de ::year::"

@row$formatted.date.day
"::day::"

@row$formatted.date.day.full
"::formattedDay::, ::day:: de ::formattedMonth:: de ::year::"

@row$formatted.date.hour
"::hour::\:00\:00"

@row$formatted.date.hour.full
"::formattedDay::, ::day:: de ::formattedMonth:: de ::year:: ::hour::\:00\:00"

@row$formatted.date.minute
"::hour::\:::minute::\:00"

@row$formatted.date.minute.full
"::formattedDay::, ::day:: de ::formattedMonth:: de ::year:: ::hour::\:::minute::\:00"

@row$formatted.date.second
"::hour::\:::minute::\:::second::"

@row$formatted.date.second.full
"::formattedDay::, ::day:: de ::formattedMonth:: de ::year:: ::hour::\:::minute::\:::second::"

@row$tooltip
<div style="padding:10px;">
  <div style="font-size:14px;"><b>::value:: ::scale::</b></div>
  <div>::date::</div>
</div>

@row$tooltip.drill
<div style="padding:10px;">
  <div style="font-size:14px;"><b>::value:: ::scale::</b></div>
  <div>::date::</div>
  <div style="font-style:italic;">::indicator\:::::member::</div>
</div>