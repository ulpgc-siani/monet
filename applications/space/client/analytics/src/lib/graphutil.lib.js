GraphUtil = function() {
};

GraphUtil.colors = ["#A52A2A","#4169E1","#DC143C","#008B8B","#696969","#006400","#8B008B","#FF8C00","#E8653A","#6F916F","#483D8B","#1E90FF","#DAA520","#BA55D3","#F7E120","#8B4513"];
GraphUtil.templates = new Object();
GraphUtil.templates.percentage = "<div class='bar' style='display:block;height:100%;float:left;'></div><div class='percentage' style='display:block;position:absolute;color:black;margin-right:5px;margin-top:5px;right:0;'></div>";
GraphUtil.templates.differences = "<table width='100%'><tr><td style='width:49.9%;vertical-align:middle;'><div class='leftbar' style='float:right;height:100%;color:white;text-align:right;'></div></td><td class='separator'></td><td style='width:49.9%;'><div style='position:relative;'><div class='rightbar' style='position:absolute;margin-left:-1px;height:100%;color:white;text-align:left;'></div></div></td></tr></table>";
  
GraphUtil.getColors = function(){
  return GraphUtil.colors;
};

GraphUtil.getColor = function(pos){
  while (pos > GraphUtil.colors.length) pos = pos-GraphUtil.colors.length;
  if (pos < 0) pos = 0;
  if (pos == GraphUtil.colors.length) pos = GraphUtil.colors.length-1;
  return GraphUtil.colors[pos];
};

GraphUtil.drawPercentage = function(DOMLayer, percentage, colorPos, animated) {
  var jLayer = $(DOMLayer);
  
  if (!animated) jLayer.html(GraphUtil.templates.percentage);
  
  var jBar = jLayer.find(".bar");
  jBar.css("background-color", GraphUtil.getColor(colorPos));
  
  if (animated) jBar.animate({width:jLayer.width()*(percentage/100)},400);
  else jBar.css("width", jLayer.width()*(percentage/100));
  
  jLayer.find(".percentage").html(Math.round(percentage) + "%");
};

GraphUtil.drawDifferences = function(DOMLayer, value, difference, config, animated) {
  var jLayer = $(DOMLayer);
  var absDifference = Math.abs(difference);
  var total = Math.abs(value) + absDifference;
  var height = jLayer.height();

  if (!animated) jLayer.html(GraphUtil.templates.differences);
  
  var jLeftBar = jLayer.find(".leftbar");
  var jRightBar = jLayer.find(".rightbar");
  var jSeparator = jLayer.find(".separator");
  var percentage = 100*absDifference/total;
  
  jLeftBar.css("background-color", config.negative);
  jRightBar.css("background-color", config.positive);
  jLeftBar.css("height", height*0.50);
  jSeparator.css("height", height);
  jRightBar.css("height", height*0.50);
  
  if (difference == 0) {
    jSeparator.css("border-left", "1px solid black");
    if (animated) { 
      jLeftBar.animate({width:0},400);
      jRightBar.animate({width:0},400);
    }
    else {
      jLeftBar.css("width", 0);
      jRightBar.css("width", 0);
    }
  }
  else { 
    jSeparator.css("border-left", "1px solid black");
    
    if (difference < 0) {
      if (animated) jLeftBar.animate({width:jLayer.width()*(percentage/200)},400); 
      else jLeftBar.css("width", jLayer.width()*(percentage/200));
      
      if (config.showPercentage) jLeftBar.html(Math.round(percentage) + "%&nbsp;");
      jRightBar.css("width", 0);
    }
    else {
      jLeftBar.css("width", 0);
      
      if (animated) jRightBar.animate({width:jLayer.width()*(percentage/200)},400); 
      else jRightBar.css("width", jLayer.width()*(percentage/200));
      
      jRightBar.css("margin-top", "-" + (Math.round(height)/4+1));
      if (config.showPercentage) jRightBar.html("&nbsp;" + Math.round(percentage) + "%");
    }
  }
};