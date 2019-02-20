function Range(measureUnit, mode, min, max) {
  this.measureUnit = measureUnit;
  this.mode = mode!=null?mode:Range.ABSOLUTE;
  this.min = min;
  this.max = max;
}

Range.ABSOLUTE = "absolute";
Range.RELATIVE = "relative";

Range.prototype.toJson = function() {
  var minValue = this.min;
  var maxValue = this.max;
  if (minValue == null) minValue = "";
  if (maxValue == null) maxValue = "";
  return "{\"measureUnit\":\"" + this.measureUnit + "\",\"mode\":\"" + this.mode + "\",\"min\":\"" + minValue + "\",\"max\":\"" + maxValue + "\"}";
};

Range.prototype.fromJson = function(jsonObject) {
  this.measureUnit = jsonObject.measureUnit;
  this.mode = jsonObject.mode;
  this.min = jsonObject.min;
  this.max = jsonObject.max;
};