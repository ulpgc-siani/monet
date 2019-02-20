CGDecoratorFieldText = function () {
};

CGDecoratorFieldText.prototype = new CGDecorator;

CGDecoratorFieldText.prototype.execute = function (DOMField) {

  DOMField.getHistoryStore = function () {
    return this.getStore(CSS_FIELD_DEF_HISTORY_STORE);
  };

  DOMField.getTextEdition = function () {
    var extField = Ext.get(this);
    var extEdition = extField.select(CSS_FIELD_DEF_TEXT_EDITION).first();
    return (extEdition) ? extEdition.dom.innerHTML : null;
  };

  DOMField.getLength = function () {
    return {"min": this.getMinLength(), "max": this.getMaxLength()};
  };

  DOMField.getMinLength = function () {
    var extField = Ext.get(this);
    var extLength = extField.select(CSS_FIELD_DEF_MIN_LENGTH).first();
    return (extLength) ? extLength.dom.innerHTML : 0;
  };

  DOMField.getMaxLength = function () {
    var extField = Ext.get(this);
    var extLength = extField.select(CSS_FIELD_DEF_MAX_LENGTH).first();
    return (extLength) ? extLength.dom.innerHTML : 0;
  };

  DOMField.getPatterns = function () {
    var extField = Ext.get(this);
    var aResult = new Array();
    var extPatternList = extField.select(CSS_FIELD_DEF_PATTERN);

    extPatternList.each(function (extPattern) {
      var Pattern = new Object();
      Pattern.Expression = new RegExp(extPattern.dom.value);
      Pattern.aCodes = extPattern.dom.name.split(WIDGET_PATTERN_CODES_SEPARATOR);
      Pattern.data = extPattern.dom.value;
      aResult.push(Pattern);
    }, this);

    return aResult;
  };

  DOMField.format = function (sValue) {
    var TextEdition = this.getTextEdition();
    var iLength = this.getMaxLength();

    if (TextEdition != null) {
      if (TextEdition == TEXT_EDITION_LOWERCASE) {
        sValue = sValue.toLowerCase();
      }
      else if (TextEdition == TEXT_EDITION_UPPERCASE) {
        sValue = sValue.toUpperCase();
      }
      else if (TextEdition == TEXT_EDITION_SENTENCE) {
        sValue = capitalizeSentence(sValue);
      }
      else if (TextEdition == TEXT_EDITION_TITLE) {
        sValue = capitalizeTitle(sValue);
      }
    }

    if (iLength != 0) {
      sValue = sValue.substring(0, iLength);
    }

    return sValue;
  };

};