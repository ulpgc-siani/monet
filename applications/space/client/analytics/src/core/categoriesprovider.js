function CategoriesProvider(dashboardName, taxonomyId) {
  this.dashboardName = dashboardName;
  this.taxonomyId = taxonomyId;
};

CategoriesProvider.prototype.load = function(parentCategoryId, callback) {
  
  if (this.dashboardName == null) return;
  if (this.taxonomyId == null) return;
  
  var datasourceUrl = Kernel.getLoadCategoriesUrl(this.dashboardName, this.taxonomyId, parentCategoryId);
  $.ajax({url: datasourceUrl, async:true}).done(CategoriesProvider.prototype.loadCallback.bind(this, callback));
};

CategoriesProvider.prototype.loadCallback = function(callback, data) {
  callback(eval("(" + data + ")"));
};