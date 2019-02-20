var SerializerData = new Object();

SerializerData.ITEM_SEPARATOR = "#";
SerializerData.ITEM_CODE_SEPARATOR = "=";

SerializerData.deserialize = function (data) {
  var result = new Array();

  if (data == null || data == "") return result;

  var itemsArray = data.split(SerializerData.ITEM_SEPARATOR);
  for (var i = 0; i < itemsArray.length; i++) {
    var itemArray = itemsArray[i].split(SerializerData.ITEM_CODE_SEPARATOR);
    if (itemArray.length == 0)
      continue;
    result[itemArray[0]] = (itemArray.length > 1) ? itemArray[1] : "";
  }

  return result;
};

SerializerData.deserializeSet = function (data) {
  var result = new Array();

  if (data == null || data == "") return result;

  var itemsArray = data.split(SerializerData.ITEM_SEPARATOR);
  for (var i = 0; i < itemsArray.length; i++)
    result.push(itemsArray[i]);

  return result;
};

SerializerData.serialize = function (data) {
  var result = "";

  if (data == null) return "";

  for (var key in data) {
    if (isFunction(data[key])) continue;
    var value = data[key];
    result += key + SerializerData.ITEM_CODE_SEPARATOR + value + SerializerData.ITEM_SEPARATOR;
  }

  if (result.length > 0)
    result = result.substring(0, result.length - 1);

  return result;
};

SerializerData.serializeSet = function (data) {
  var result = "";

  if (data == null) return "";

  for (var key in data) {
    if (isFunction(data[key])) continue;
    result += data[key] + SerializerData.ITEM_SEPARATOR;
  }

  if (result.length > 0)
    result = result.substring(0, result.length - 1);

  return result;
};