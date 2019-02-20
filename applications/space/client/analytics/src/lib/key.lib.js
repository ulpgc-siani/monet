LibraryKey = new Object();

LibraryKey.generateKey = function(dimension, level) {
  var key = Context.Config.KeyTemplate;
  key = key.replace("%d", dimension);
  key = key.replace("%l", level);
  return key;
};

LibraryKey.getFirstCode = function(key) {
  var keyArray = key.split(Context.Config.KeySeparator);
  
  if (keyArray.length < 1)
    return null;

  return keyArray[0];
};

LibraryKey.getSecondCode = function(key) {
  var keyArray = key.split(Context.Config.KeySeparator);
  
  if (keyArray.length == 1)
    return key;

  if (keyArray.length < 2)
    return null;

  return keyArray[1];
};

LibraryKey.getDimension = function(key) {
  return LibraryKey.getFirstCode(key);
};

LibraryKey.getLevel = function(key) {
  return LibraryKey.getSecondCode(key);
};

LibraryKey.getHierarchy = function(key) {
  return LibraryKey.getLevel(key);
};

LibraryKey.getMeasure = function(key) {
  return LibraryKey.getFirstCode(key);
};

LibraryKey.getOperator = function(key) {
  return LibraryKey.getSecondCode(key);
};