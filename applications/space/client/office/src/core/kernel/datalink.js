function CGDataLink() {
};

CGDataLink.prototype.load = function (sLink, aParameters) {
  var request = new Request(sLink);

  for (var Index in aParameters) {
    if (isFunction(aParameters[Index])) continue;
    request.Add(Index, aParameters[Index]);
  }

  return request.Post();
};

CGDataLink.prototype.loadDefinition = function (aParameters) {
  return this.load(Kernel.getBusinessModelDefinitionLink(), aParameters);
};

CGDataLink.prototype.loadFile = function (aParameters) {
  return this.load(Kernel.getBusinessModelFileLink(), aParameters);
};