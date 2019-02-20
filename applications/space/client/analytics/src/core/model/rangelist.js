function RangeList() {
  this.base = List;
  this.base();
}

RangeList.prototype = new List;
RangeList.constructor = RangeList;

RangeList.prototype.add = function(measureUnit, mode, min, max) {
  if (this.get(measureUnit, "") != null) return;
  var range = new Range(measureUnit, mode, min, max);
  this.addItem(range);
};

RangeList.prototype.addItem = function(item) {
  this.items.push(item);
  this.positions[item.measureUnit] = this.items.length-1;
};

RangeList.prototype.get = function(measureUnit) {
  return this.items[this.positions[measureUnit]];
};

RangeList.prototype.fromJson = function(jsonObject) {
  this.clear();
  for (var i=0; i<jsonObject.rows.length; i++) {
    var range = new Range();
    range.fromJson(jsonObject.rows[i]);
    this.addItem(range);
  }
};