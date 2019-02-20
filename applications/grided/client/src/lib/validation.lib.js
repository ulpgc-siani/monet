
var ValidationErrors = {
  Required : 'required'		
};

Validators = {};

//-------------------------------------------------------------------------
Validators.Required = {	
  propertyName : '',
  
  validate : function(propertyName, value) {
	this.propertyName = propertyName;
		
	if (value == undefined || value == "" || value == null) return false;
    return true;
  },
  getError : function() {
	  return {propertyName: this.propertyName,  message: ValidationErrors.Required};
  }
};			

