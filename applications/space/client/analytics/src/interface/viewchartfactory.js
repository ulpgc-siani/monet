ViewChartFactory = new Object();

ViewChartFactory.get = function(type) {
  var view = null;
  
  if (type == ViewChart.LINE_CHART) view = new ViewLineChart();
  else if (type == ViewChart.BAR_CHART) view = new ViewBarChart();
  else if (type == ViewChart.MAP_CHART) view = new ViewMapChart();
  else if (type == ViewChart.BUBBLE_CHART) view = new ViewBubbleChart();
  else if (type == ViewChart.TABLE_CHART) view = new ViewTableChart();
  
  return view;
};