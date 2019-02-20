var DecoratorFactory = new Object();

DecoratorFactory.getInstance = function(type) {
  if (type == "google")
    return new GoogleDecorator();
};

