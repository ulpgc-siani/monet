SUMMATION_ITEM_SIMPLE = "SIMPLE";

CGSummationItem = function () {
  this.Label = "";
  this.Value = 0;
  this.Type = SUMMATION_ITEM_SIMPLE;
  this.IsMultiple = false;
  this.IsNegative = false;
  this.Children = new Array();
};

CGSummationItem.prototype.serialize = function () {
  var sResult = "<item label=\"" + this.Label + "\" value=\"" + this.Value + "\" type=\"" + this.Type + "\" multiple=\"" + this.IsMultiple + "\" negative=\"" + this.IsNegative + "\">";
  for (var i = 0; i < this.Children.length; i++) sResult += this.Children[i].serialize();
  sResult += "</item>";
  return sResult;
};