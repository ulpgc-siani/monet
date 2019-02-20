function CommandInfo(link) {

  this.operation = null;
  this.parameters = new Array();

  if (link != null) this.proceslink(link);
};

//---------------------------------------------------------------------
CommandInfo.prototype.proceslink = function(link) {

  link = link.replace(/%20/g, "");
  link = link.replace(/%28/g, "(");
  link = link.replace(/%29/g, ")");
  
  var leftBracketPos = link.indexOf("(");
  var rightBracketPos = link.indexOf(")");

  if (leftBracketPos == -1) {
    this.operation = link;
    return true;
  }
  
  var parameters = link.substring(leftBracketPos+1,rightBracketPos);
  parameters = parameters.replace(/\//g, "@bar45@");
  link = link.substring(0,leftBracketPos+1) + parameters + link.substring(rightBracketPos);

  while ((iPos=link.indexOf("/")) != -1) {
    link = link.substring(iPos+1, link.length);
  }

  if ((iPos=link.indexOf("(")) == -1) {
    this.operation = link;
    return true;
  }

  if (link.substring(link.length-1,link.length) != ")") return false;

  this.operation = link.substring(0,iPos);

  link = link.substring(iPos+1,link.length-1);
  link = link.replace(/@bar45@/g, "/");
  var result = link.split(',');
  this.parameters = new Array();
  for (var iPos=0; iPos<result.length; iPos++) {
    this.parameters.push(unescape(result[iPos]));
  }
};

//---------------------------------------------------------------------
CommandInfo.prototype.getOperation = function() { 
	return this.operation; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.setOperation = function(operation) { 
  this.operation = operation; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.getParameters = function() { 
  return this.parameters; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.addParameter = function(parameter) { 
  this.parameters.push(parameter); 
};

//---------------------------------------------------------------------
CommandInfo.prototype.setParameters = function(parameters) { 
  this.parameters = parameters; 
};

//---------------------------------------------------------------------
CommandInfo.prototype.getLink = function(operation, parameters) { 
  var result = operation + "(";
  var parameters = "";

  parameters.each(function(parameter) {
    parameters += parameter + ",";
  }, this);

  if (parameters != "") result += parameters.substring(0, parameters.length-1);

  return result + ")";
};