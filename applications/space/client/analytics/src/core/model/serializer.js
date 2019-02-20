var Serializer = new Object();
Serializer.ITEM_SEPARATOR      = "#";
Serializer.ITEM_CODE_SEPARATOR = "=";

Serializer.serializeAccount = function(account) {
  var result = "\"id\":\"" + account.id + "\",";
  result += "\"user\":" + Serializer.serializeAccountUser(account.user);
  return "{" + result + "}";
};

Serializer.serializeAccountUser = function(user) {
  var result = "\"id\":\"" + user.id + "\",";
  result += "\"language\":\"" + user.language + "\",";
  result += "\"info\":" + Serializer.serializeAccountUserInfo(user.info);
  return "{" + result + "}";
};

Serializer.serializeAccountUserInfo = function(info) {
  var result = "\"photo\":\"" + info.photo + "\",";
  result += "\"fullname\":\"" + info.fullname + "\",";
  result += "\"preferences\":\"" + info.preferences + "\",";
  result += "\"email\":\"" + info.email + "\",";
  result += "\"occupations\":\"" + info.occupations + "\"";
  return "{" + result + "}";
};

Serializer.serializeMap = function(map) {
  var result = "";
  
  if (map == null) return "";
  
  for (var key in map) {
    if (isFunction(map[key])) continue;
    var value = map[key];
    result += key + Serializer.ITEM_CODE_SEPARATOR + value + Serializer.ITEM_SEPARATOR;
  }
  
  if (result.length > 0)
    result = result.substring(0, result.length-1);
  
  return result;
};

Serializer.deserializeMap = function(data) {
  var result = new Array();
  
  if (data == null || data == "") return result;
  
  var itemsArray = data.split(Serializer.ITEM_SEPARATOR);
  for (var i=0; i<itemsArray.length; i++) {
    var itemArray = itemsArray[i].split(Serializer.ITEM_CODE_SEPARATOR);
    if (itemArray.length == 0)
      continue;
    result[itemArray[0]] = (itemArray.length > 1) ? itemArray[1] : "";
  }
  
  return result;
};
