function Category(taxonomyId, id, label) {
  this.taxonomyId = taxonomyId;
  this.id = id;
  this.label = label;
}

Category.prototype.toJson = function() {
  return "{\"taxonomy\":\"" + stringify(this.taxonomyId) + "\",\"id\":\"" + stringify(this.id) + "\",\"label\":\"" + stringify(this.label) + "\"}";
};

Category.prototype.fromJson = function(jsonObject) {
  this.id = jsonObject.id;
  this.label = jsonObject.label;
  this.taxonomyId = jsonObject.taxonomy;
};