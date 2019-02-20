ViewerHelperChat = new Object;
ViewerHelperChat.extLayer = null;
ViewerHelperChat.itemsPerPage = 40;
ViewerHelperChat.currentPage = 1;

ViewerHelperChat.init = function (extLayer) {
  ViewerHelperChat.extLayer = extLayer;
  ViewerHelperChat.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperChat, Lang.ViewerHelperChat);
  ViewerHelperChat.extLayer.select("textarea").first().on("keyup", ViewerHelperChat.atChange.bind(ViewerHelperChat));
  ViewerHelperChat.extLayer.select(".sendmessage").first().on("click", ViewerHelperChat.atSendMessage.bind(ViewerHelperChat));
};

ViewerHelperChat.show = function () {
  ViewerHelperChat.extLayer.dom.style.display = "block";
};

ViewerHelperChat.hide = function () {
  ViewerHelperChat.extLayer.dom.style.display = "none";
};

ViewerHelperChat.addItem = function (item) {
  var template = AppTemplate.ViewerHelperChatItem;
  var extList = ViewerHelperChat.extLayer.select(".items").first();
  var extItem = Ext.get(new Insertion.Bottom(extList.dom, template).element.immediateDescendants().last());

  extItem.dom.id = "chatitem_" + item.id;
  extItem.select(".date .value").first().dom.innerHTML = getFormattedDateTime(parseServerDate(item.date), Context.Config.Language, false);

  if (item.sent)
    extItem.select(".state").first().addClass("sent");
  else
    extItem.select(".state").first().removeClass("sent");

  extItem.select(".message").first().dom.innerHTML = item.message;
  extItem.addClass(item.type);

  return extItem;
};

ViewerHelperChat.load = function (clearItems) {
  var iStart = (ViewerHelperChat.currentPage * ViewerHelperChat.itemsPerPage) - ViewerHelperChat.itemsPerPage;
  Ext.Ajax.request({
    url: ViewerHelperChat.target.loader,
    params: "start=" + iStart + "&limit=-1",
    method: "GET",
    callback: function (sOptions, bSuccess, Response) {
      eval("var data = " + Response.responseText);
      ViewerHelperChat.update(data, clearItems);
    },
    scope: this
  });
};

ViewerHelperChat.update = function (data, clearItems) {
  var extList = ViewerHelperChat.extLayer.select(".items").first();

  if (clearItems) {
    if (data.rows.length == 0) extList.dom.innerHTML = translate(AppTemplate.ViewerHelperChatNoItems, Lang.ViewerHelperChat);
    else extList.dom.innerHTML = "";
  }

  for (var i = 0; i < data.rows.length; i++) {
    var item = data.rows[i];
    this.addItem(item);
  }

  extList.scroll("bottom", (extList.down("li").getHeight() * data.nrows) + 10);
};

ViewerHelperChat.refresh = function () {
  if (ViewerHelperChat.target == null) return;
  if (ViewerHelperChat.target.loader == null) return;

  var extToolbar = ViewerHelperChat.extLayer.select(".toolbar").first();
  if (this.target.closed) extToolbar.hide();
  else extToolbar.show();

  ViewerHelperChat.load(true);
};

ViewerHelperChat.sendMessage = function (message) {
  CommandListener.throwCommand("sendtaskorderchatmessage(" + this.target.idTask + "," + this.target.idOrder + "," + escape(message) + ")");
};

ViewerHelperChat.atChange = function (event) {
  var extTextArea = ViewerHelperChat.extLayer.select("textarea").first();
  var codeKey = event.getKey();
  var message = extTextArea.dom.value;

  if (codeKey == event.ENTER && !event.shiftKey && message != "") {
    ViewerHelperChat.sendMessage(message);
    extTextArea.dom.value = "";
  }

  Event.stop(event);
  return false;
};

ViewerHelperChat.atSendMessage = function () {
  var extTextArea = ViewerHelperChat.extLayer.select("textarea").first();
  var message = extTextArea.dom.value;
  if (message == "") return;
  ViewerHelperChat.sendMessage(message);
  extTextArea.dom.value = "";
};