function CategoryList() {
  this.base = List;
  this.base();
}

CategoryList.prototype = new List;
CategoryList.constructor = IndicatorList;

CategoryList.prototype.add = function(taxonomyId, id, label) {
  if (this.get(id, "") != null) return;
  var category = new Category(taxonomyId, id, label);
  this.addItem(category);
};

CategoryList.prototype.getCategoriesOfTaxonomy = function(taxonomyId) {
  var result = new Array();
  
  for (var i=0; i<this.items.length; i++) {
    var category = this.items[i];
    if (category.taxonomyId == taxonomyId)
      result.push(category);
  }
  
  return result;
};

CategoryList.prototype.deleteCategoriesOfTaxonomy = function(taxonomyId) {
  var itemsToDelete = this.getCategoriesOfTaxonomy(taxonomyId);
  for (var i=0; i<itemsToDelete.length; i++)
    this.deleteItem(itemsToDelete[i].id, "");
};

CategoryList.prototype.fromJson = function(jsonObject) {
  this.clear();
  for (var i=0; i<jsonObject.rows.length; i++) {
    var category = new Category();
    category.fromJson(jsonObject.rows[i]);
    this.addItem(category);
  }
};