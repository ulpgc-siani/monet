
var Router = {
  routes  : {},
  patterns : [],

  getPattern : function(queryString) {
    var result = null; 
      for (var i=0; i < this.patterns.length; i++) {
        var pattern = this.patterns[i];
        if (queryString.match(pattern)) {result = pattern; break;}
      }
     return result;
  },
 
  addRoute : function(pattern, placename) {
    this.patterns.push(pattern);
    this.routes[pattern] = placename;
  },

  get : function(queryString) {
    var pattern = this.getPattern(queryString);
    if (!pattern) return null;
    return this.routes[pattern];
  }     
};

