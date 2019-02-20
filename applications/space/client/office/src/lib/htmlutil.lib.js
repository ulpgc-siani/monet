HtmlUtil = {

  getEncodingTranslationTable: function () {
    var entities = {}, hash_map = {}, decimal = 0, symbol = '';

    entities['38'] = '&amp;';
    entities['60'] = '&lt;';
    entities['62'] = '&gt;';

    for (decimal in entities) {
      symbol = String.fromCharCode(decimal);
      hash_map[symbol] = entities[decimal];
    }

    return hash_map;
  },

  encode: function (string) {
    var hash_map = {}, symbol = '', tmp_str = '', entity = '';
    tmp_str = string.toString();

    hash_map = HtmlUtil.getEncodingTranslationTable();

    for (symbol in hash_map) {
      entity = hash_map[symbol];
      tmp_str = tmp_str.split(symbol).join(entity);
    }

    return tmp_str;
  },

  decode: function (string) {
    var hash_map = {}, symbol = '', tmp_str = '', entity = '';

    if (string == null) return null;

    tmp_str = string.toString();

    return tmp_str;
  }
};