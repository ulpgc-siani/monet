var BusinessModel = Model.extend({
  init : function() {
    this._super();    
  }	
});

BusinessModel.ID    = 'id';
BusinessModel.NAME  = 'name';
BusinessModel.LABEL = 'label';
BusinessModel.LOGO  = 'logo';
BusinessModel.VERSIONS = 'versions';
BusinessModel.LATEST_VERSION = 'latest_version';

//---------------------------------------------------------------------------------------------
BusinessModel.prototype.isLatestVersion = function(version) {
  return this.getLatestVersion(version) === version;  
};

//---------------------------------------------------------------------------------------------
BusinessModel.prototype.getLatestVersion = function(version) {
  var latestVersion = null;
  var versions = this.get(BusinessModel.VERSIONS);
  
  for (var i=0, l = versions.size(); i < l; i++) {
    var version = versions.get(i);
    if (latestVersion == null || version.get(ModelVersion.DATE) > latestVersion.get(ModelVersion.DATE)) {
       latestVersion = version;
    }
  }
  return latestVersion;
};

//---------------------------------------------------------------------------------------------
BusinessModel.prototype.getNextVersion = function(version) {
  var minDiff     = version.get(ModelVersion.DATE);
  var nextVersion = this.get(BusinessModel.LATEST_VERSION);
  var versions    = this.get(BusinessModel.VERSIONS);
  
  for (i=0, l = versions.size(); i < l; i++) {
    var v = versions.get(i);
    var diff = v.get(ModelVersion.DATE) - version.get(ModelVersion.DATE);
    if (diff > 0 && diff < minDiff) {
      minDiff = diff;
      nextVersion = v;
    }
  }
  return nextVersion;
};

//---------------------------------------------------------------------------------------------
var ModelVersion = Model.extend({
  init : function() {
    this._super();    
  }
});

ModelVersion.ID = 'id';
ModelVersion.LABEL = 'label';
ModelVersion.DATE = 'date';
ModelVersion.METAMODEL = 'metamodel';