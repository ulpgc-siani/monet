function IndicatorList() {
  this.base = List;
  this.base();
}

IndicatorList.prototype = new List;
IndicatorList.constructor = IndicatorList;

IndicatorList.prototype.add = function(id) {
  if (this.get(id, "") != null) return;
  var indicator = new Indicator(id);
  this.addItem(indicator);
};

IndicatorList.prototype.fromJson = function(jsonObject) {
  this.clear();
  for (var i=0; i<jsonObject.rows.length; i++) {
    var indicator = new Indicator();
    indicator.fromJson(jsonObject.rows[i]);
    this.addItem(indicator);
  }
};