ChartApi = new Object();
ChartApi.onChartClick = null;

ChartApi.chartInstance = function(type, DOMContainer) {
if (type == "tablechart") {
var chart = new DataTable();
return chart;
}
return null;
}

ChartApi.createChart = function(type, DOMContainer, datasourceUrl, options, onCreateCallback) {
$.ajax({url: datasourceUrl, async: true}).done(ChartApi.createChartCallback.bind(ChartApi, type, DOMContainer, options, onCreateCallback));
};

ChartApi.createChartCallback = function(type, DOMContainer, options, onCreateCallback, serializedData) {
var chartInfo;
var dataTable;

if (serializedData != "")
eval(serializedData);

var chart = ChartApi.chartInstance(type, DOMContainer);
chart.width = options.width-100;
chart.colors = options.colors;
chart.language = options.language;
chart.columns = dataTable.columns;
chart.rows = dataTable.rows;
chart.grouped = false;
chart.render($(DOMContainer));

ChartApi.atChartReady(dataTable, onCreateCallback);
};

ChartApi.atChartReady = function(chart, onCreateCallback) {
if (onCreateCallback) onCreateCallback(chart);
};

ChartApi.atChartClick = function(chartWrapper) {
};