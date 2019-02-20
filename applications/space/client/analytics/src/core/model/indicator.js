function Indicator(id) {
  this.id = id;
  this.value = "";
}

Indicator.prototype.toJson = function() {
  return "{\"id\":\"" + stringify(this.id) + "\"}";
};

Indicator.prototype.fromJson = function(jsonObject) {
  this.id = jsonObject.id;
};