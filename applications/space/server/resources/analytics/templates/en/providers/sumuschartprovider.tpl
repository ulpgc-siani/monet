dataTable = {};
dataTable.columns = new Array();
::columns::
dataTable.rows = new Array();
::rows::

@column
dataTable.columns.push({ 'type': 'number', 'label': '::label::', 'unit': '::metric:: ::scale::', children: []});

@column.date
dataTable.columns.push({ 'type': 'string', 'label': 'Date'});

@column.time
dataTable.columns.push({ 'type': 'string', 'label': 'Date'});

@row
dataTable.rows.push([::indicators::]);

@row$indicator
{'value'\:::value::,'formatted':'::formattedValue::'}::comma|,::

@row$value
"::value::"

@row$value.date
new Date(::year::,::month::,::day::)

@row$value.date.working_day
"::value::"

@row$value.date.day_week
"::value::"

@row$value.time
new Date(::year::,::month::,::day::,::hour::,::minute::,::second::)

@row$formatted
"::value::"

@row$formatted.date
"::formattedDay::, ::day:: of ::formattedMonth:: ::year::"

@row$formatted.date.year
"::year::"

@row$formatted.date.month
"::formattedMonth::"

@row$formatted.date.month.full
"::formattedMonth:: ::year::"

@row$formatted.date.day
"::day::"

@row$formatted.date.day.full
"::formattedDay::, ::day:: of ::formattedMonth:: ::year::"

@row$formatted.date.hour
"::hour::\:00\:00"

@row$formatted.date.hour.full
"::formattedDay::, ::day:: of ::formattedMonth:: ::year:: at ::hour::\:00\:00"

@row$formatted.date.minute
"::hour::\:::minute::\:00"

@row$formatted.date.minute.full
"::formattedDay::, ::day:: of ::formattedMonth:: ::year:: at ::hour::\:::minute::\:00"

@row$formatted.date.second
"::hour::\:::minute::\:::second::"

@row$formatted.date.second.full
"::formattedDay::, ::day:: of ::formattedMonth:: ::year:: at ::hour::\:::minute::\:::second::"