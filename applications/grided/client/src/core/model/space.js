var Space = Model.extend({
  init: function() {
    this._super();
    this.publicationService = new Collection();
    
    this.validations = {
      'name' : Validators.Required,
      'url' : Validators.Required,
    };    
  }
});

//Property names
Space.ID = 'id';
Space.NAME = 'name';
Space.LABEL = 'label';
Space.URL = 'url';
Space.LOGO = 'logo';
Space.FEDERATION = 'federation';
Space.SERVICES = 'services';
Space.DATAWAREHOUSE = 'datawarehouse';
Space.MODEL_VERSION = "model_version";
Space.STATE = "state";

//-------------------------------------------------------------------
Space.prototype.getService = function(id) {
  var collection = this.get(Space.SERVICES);
  return collection.getById(id);   
};

//-------------------------------------------------------------------
Space.prototype.addService = function(statement, options) {
  var collection = this.get(Space.SERVICES);	
  collection.add(statement, {silent : true});
  if (! options || !options.silent) this.fire({name: Events.ADDED, data: {model : this, collection: collection, item: statement}});
};

//-------------------------------------------------------------------
Space.prototype.removeService = function(ids, options) {
  var collection = this.get(Space.SERVICES);  	
  collection.remove(ids, {silent: true});
  if (! options || !options.silent) this.fire({name: Events.REMOVED, data: {model : this, collection: collection}});	
};


var PublicationService = Model.extend({
  init : function(name, type, published) {
    this._super();
    this.id = name;
    this.name = name;
    this.type = type;
    this.published = published;
  }
});

PublicationService.NAME = 'name';
PublicationService.TYPE = 'type';
PublicationService.PUBLISHED = 'published';
