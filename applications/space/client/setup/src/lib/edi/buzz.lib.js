//--------------------------------------------------------------------------------

function isFunction(item) {
  return (typeof item == "function");
}

//--------------------------------------------------------------------------------

function generateTagAttributes(hAttributes) {
  if (!hAttributes) return EMPTY;

  var sResult = EMPTY;

  for (var key in hAttributes){
    if (isFunction(hAttributes[key])) continue;

    sResult += BLANK + ((key.charAt(0) == "_")? key.substr(1) : key );
    sResult += EQUAL + DOUBLE_QUOTE + hAttributes[key] + DOUBLE_QUOTE;
  }

  return sResult;
}

String.prototype.toTag = function(name, hAttributes) {
  var sResult = EMPTY;
  sResult = "<" + name + BLANK + generateTagAttributes(hAttributes) + ">";
  sResult += this;
  sResult += "</" + name + ">";
  return sResult;
};

String.prototype.toA = function(hAttributes){
  return this.toTag("a", hAttributes);
};

String.prototype.toLi = function(hAttributes){
  return this.toTag("li", hAttributes);
};

String.prototype.toImg = function(hAttributes){
  return "<img " + generateTagAttributes(hAttributes) + " src='" + this + "'>";
};

String.prototype.toShort = function(iLength){
  if (this.length <= iLength) return this;
  return this.substr(0, iLength) + DOT + DOT + DOT;
};

//--------------------------------------------------------------------------------
Array.prototype.getIndex = function(data, key) {
  if (!data && data != 0 && data != EMPTY) return -1;

  for (var i = 0; i<this.length; i++) {
    var item = this[i];
    var value = (key) ? item[key] : item;

    if (data == value) return i;
  }

  return -1;
};

Array.prototype.has = function(data) {
  return (this.getIndex(data) >= 0);
};

Array.prototype.tail = function(data) {
  this[this.length] = data;
};

Array.prototype.head = function() {
  if (this.length <= 0) return null;
  value = this[0];

  for (var i = 0; i < this.length - 1; i++ )
    this[i] = this[i + 1];

  this.length--;
  return value;
};

Array.prototype.remove = function(data, key){
  var i = this.getIndex(data, key);
  if (i < 0) return;

  for (; i < this.length - 1; i++) 
    this[i] = this[i+1];

  delete(this[i]);
  this.length--;
};

Array.prototype.copy = function(aData) {
  for (var i in aData) {
    this[i] = aData[i];
  }
};

Array.prototype.toString = function () {

  sResult = new String();
  for (iPos in this) {
    if (isFunction(this[iPos])) continue;
    sResult += this[iPos] + COMMA;
  }

  sResult = sResult.substr(0, sResult.length-1);

  return sResult;
};

Array.prototype.size = function() {
  var iLength = 0;
  for (var index in this) { 
    if (isFunction(this[index])) continue;
    iLength++; 
  }
  return iLength;
};

//--------------------------------------------------------------------------------

function extractFilename(sFullPath) {
  var iLast45 = sFullPath.lastIndexOf("/");
  var iLast135 = sFullPath.lastIndexOf("\\");
  var iLast = iLast45;

  if (iLast135 > iLast) iLast = iLast135;

  var sFileName = sFullPath.substring(iLast + 1 , sFullPath.length);

  return sFileName;
}

function extractFileExtension(sFileName) {
  sFileName = String(sFileName).toUpperCase();
  return sFileName.substring(sFileName.lastIndexOf(DOT) + 1);
}

var AT_IMAGE = "IMG";
var AT_EDC = "EDC";
var AT_HTML = "HTM";
var AT_DOC = "DOC";
var AT_COMPRESSED = "CMP";
var AT_SPREADSHEET = "SPS";
var AT_PRESENTATION = "PRE";
var AT_DATABASE = "DBA";
var AT_OTHER = "OTHER";

function getFileType(sFileName){
  var sAux = extractFileExtension(sFileName);
  if (["PNG", "BMP", "JPG", "JPEG", "GIF"].has(sAux)) return AT_IMAGE;
  else if (["PDF", "LIT"].has(sAux)) return AT_EDC;
  else if (["HTML", "HTML"].has(sAux)) return AT_HTML;
  else if (["DOC", "TXT", "RTF", "WRI", "SGL", "SVW", "STW", "SXW", "ODT", "SDW"].has(sAux)) return AT_DOC;
  else if (["ZIP", "RAR", "TAR", "GZ", "TGZ", "LZH", "LHA", "ARJ"].has(sAux)) return AT_COMPRESSED;
  else if (["XLS", "SXC", "ODS"].has(sAux)) return AT_SPREADSHEET;
  else if (["PPT", "PPS", "SDD"].has(sAux)) return AT_PRESENTATION;
  else if (["MDB", "ODB"].has(sAux)) return AT_DATABASE;
  else return AT_OTHER;
}

GetFileClass = function(FileName){
  var sFileType = getFileType(FileName);
  var aClasses = [AT_IMAGE, AT_EDC, AT_HTML, AT_DOC, AT_COMPRESSED, AT_SPREADSHEET, AT_PRESENTATION, AT_DATABASE, AT_OTHER];
  var aStyles = ["FILEIMAGE", "FILEEDOCUMENT", "FILEWEBPAGE", "FILEDOCUMENT", "FILECOMPRESSED", "FILESPREADSHEET", "FILEPRESENTATION", "FILEDATABASE", "FILEDEFAULT"];
  var i = aClasses.getIndex(sFileType);

  if (i < 0) return "FILEDEFAULT";

  return aStyles[i];
};