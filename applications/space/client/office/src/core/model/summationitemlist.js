CGSummationItemList = function () {
  this.Items = new Array();
};

CGSummationItemList.prototype.serialize = function () {
  var sResult = "<itemlist>";
  for (var i = 0; i < this.Items.length; i++) sResult += this.Items[i].serialize();
  sResult += "</itemlist>";
  return sResult;
};