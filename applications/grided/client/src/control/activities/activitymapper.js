
ActivityMapper = {
  map: {},
  activities: {},
      
  get : function(placename) {
	var activity = this.activities[placename];
	if (! activity) {	
		var activityConstructor = this.map[placename];
		if (activityConstructor == null) throw new Error('Error mapping activity');
    
		activity = new activityConstructor();
		this.activities[placename] = activity;		
	}
	return activity;
  },
  
  register: function(placename, activity) {
    this.map[placename] = activity;	  
  }
};