CGDecoratorRouteMap = function () {
};

CGDecoratorRouteMap.prototype = new CGDecorator;

CGDecoratorRouteMap.prototype.execute = function (DOMRouteMap) {

  DOMRouteMap.aMemento = null;
  DOMRouteMap.aLinks = null;

  DOMRouteMap.init = function () {
    var extRouteMap = Ext.get(this);
    var extTask = extRouteMap.up(CSS_TASK);
    var aGoals = Extension.getDefinition(extTask.dom.getControlInfo().Type).Goals;

    this.aMemento = new Array();
    this.IndexMemento = -1;
  };

  DOMRouteMap.destroy = function () {
    this.aMemento = new Array();
  };

  DOMRouteMap.focus = function () {
  };

  DOMRouteMap.blur = function () {
  };

  DOMRouteMap.addMemento = function (Memento) {
    var iPos = this.IndexMemento + 1;
    while (iPos < this.aMemento.length) this.aMemento.pop();
    this.aMemento.push(Memento);
    this.IndexMemento = this.aMemento.length - 1;
  };

  DOMRouteMap.undo = function () {
    var Memento, CurrentMemento;

    this.IndexMemento--;

    if (this.IndexMemento < 0) {
      this.IndexMemento = 0;
      return;
    }

    if (this.aMemento.length == 0) return;

    CurrentMemento = this.aMemento[this.IndexMemento + 1];
    Memento = this.aMemento[this.IndexMemento];

    if (Memento == null) return;

    if ((CurrentMemento != null) && (CurrentMemento.id != Memento.id)) {
      this.undo();
      return;
    }
  };

  DOMRouteMap.redo = function () {
    var Memento, CurrentMemento;

    this.IndexMemento++;

    if (this.IndexMemento >= this.aMemento.length) {
      this.IndexMemento = this.aMemento.length - 1;
      return;
    }

    CurrentMemento = this.aMemento[this.IndexMemento - 1];
    Memento = this.aMemento[this.IndexMemento];

    if (Memento == null) return;

    if ((CurrentMemento != null) && (CurrentMemento.id != Memento.id)) {
      this.redo();
      return;
    }
  };

  // #############################################################################################################

};