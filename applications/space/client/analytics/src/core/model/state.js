State = new Object();
State.account = null;
State.compareTaxonomy = null;
State.indicatorList = new IndicatorList();
State.compareList = new CategoryList();
State.filterList = new CategoryList();
State.timeLapse = { from: null, to: null, scale: 5 };
State.lastCommand = new Object();
State.chartType = null;
State.chartLayer = null;
State.rangeList = new RangeList();

State.Mode = {
  EMBED : "embed",
  PRINT : "print"
};