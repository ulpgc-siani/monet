PublicationServicesSerializer = {

  serialize : function(services) {
  	  
  },
  
  unserialize : function(jsonServices) {
    var services = new Collection();
	  
	for (var i=0; i < jsonServices.length; i++) {
	  if (_.isFunction(jsonServices[i])) continue;
	  
	  var jsonService = jsonServices[i];
	  var service = new PublicationService();
	  service.id = jsonService.id;
	  service.set(PublicationService.NAME, jsonService.name, {silent: true});
	  service.set(PublicationService.TYPE, jsonService.type, {silent: true});
	  service.set(PublicationService.PUBLISHED, jsonService.published, {silent: true});
	  
	  services.add(service);
	}
	return services;
  }
};