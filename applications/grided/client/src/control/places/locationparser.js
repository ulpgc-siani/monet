
var LocationParser = {
  parse : function(location) {
    var dataPattern = /\d+$/;    
    var data  = location.match(dataPattern);     
    return (data)? data[0] : '';
  }
};
