chartInfo = {
	isEmpty \: ::isEmpty::,
	center\: ::center::,
	markerIcon\: '::markerIcon::'
};

dataTable = [::rows::];

@chartInfo$center
{ latitude: ::latitude::, longitude: ::longitude::, zoom: ::zoom:: }

@chartInfo$center.default
{ latitude: 28.139740, longitude: -15.448251, zoom: 13 }

@chartInfo$zoom.default
13

@row
::indicators::::comma|,::

@row$indicator
	::value::::tooltip|,'*'::::comma|,::

@row$indicator.geo
{ position\: new google.maps.LatLng(::x::,::y::), relevance\: ::value::, label\: '::tooltip::', colorIndex\: ::colorIndex:: }::comma|,::

@row$value
"::value::"

@row$value.date
new Date(::year::,::month::,::day::)

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

@row$tooltip
::date::

@row$tooltip.drill
::member:: on ::date::