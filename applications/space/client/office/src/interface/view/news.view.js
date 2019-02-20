var NEWS_PAGE_SIZE = 10;

ViewNews = new Object;

ViewNews.sLayerName = null;

//PUBLIC
ViewNews.init = function (sLayerName) {
  this.loaded = 0;
  this.initialized = true;

  this.DOMLayer = $(sLayerName);

  this.extNewsLayer = Ext.get(this.DOMLayer);
  this.extScrollParent = this.extNewsLayer.up(".content");
  this.extScrollParent.addListener("scroll", this.onScroll.bind(this));

  this.extNewNewsTicket = this.extNewsLayer.down(".update");
  this.extNewNewsTicket.enableDisplayMode();
  this.extNewNewsTicket.hide();

  this.refreshHandlers();
  var newPostBtn = this.extNewsLayer.down(".newpost textarea");
  newPostBtn.addKeyListener(27, this.onBlurNewPost, newPostBtn);
  newPostBtn.addListener("focus", this.onFocusNewPost.bind(newPostBtn));
  newPostBtn.addListener("blur", this.onNewPostBlurLuncher.bind(newPostBtn));
};

ViewNews.getDOM = function () {
  return this.DOMLayer;
};

ViewNews.show = function () {
  this.DOMLayer.show();
};

ViewNews.hide = function () {
  this.DOMLayer.hide();
};

ViewNews.setContent = function (sContent) {
};

ViewNews.refreshHandlers = function () {
  var buttons = this.extNewsLayer.query(".newcomment textarea");
  this.loaded = buttons.length;
  for (var i = 0; i < buttons.length; i++) {
    var button = Ext.get(buttons[i]);
    if (button.hasClass("active"))
      continue;
    button.addClass("active");
    button.addListener("focus", this.onFocus.bind(button));
    button.addListener("blur", this.onBlurLuncher.bind(button));
    button.addKeyListener(27, this.ViewNewsCommentAtBlur, button);
  }

  var contexts = this.extNewsLayer.query(".postlist .contextual");
  for (var i = 0; i < contexts.length; i++) {
    var contextBtn = Ext.get(contexts[i]);
    contextBtn.on("click", this.atContextBtnClick.bind(contextBtn));
    contextBtn.on("blur", this.atContextBtnBlur.bind(contextBtn));
  }

  CommandListener.capture(this.DOMLayer);
};

ViewNews.getNewPostAndDisable = function (newPostTextArea) {
  var eTxtArea = $(newPostTextArea);
  var newpost = eTxtArea.up(".newpost");
  if (newpost.hasClassName("disable"))
    return;

  var text = eTxtArea.value;
  eTxtArea.value = eTxtArea.title;
  eTxtArea.blur();
  newpost.addClassName("disable");

  return text;
};

ViewNews.addPost = function (data) {
  var ePostList = this.DOMLayer.down("ul");
  new Insertion.Top(ePostList, data);
};

ViewNews.addNewsPage = function (data, clean) {
  var ePostList = this.DOMLayer.down("ul");
  if (clean)
    ePostList.innerHTML = EMPTY;

  if (data && data != EMPTY) {
    new Insertion.Bottom(ePostList, data);

    this.refreshHandlers();
    this.endLoading();
  }
  this.extNewNewsTicket.hide();
};

ViewNews.atContextBtnClick = function () {
  this.down("ul").toggle();
};

ViewNews.atContextBtnBlur = function () {
  this.down("ul").hide();
};


ViewNews.endLoading = function () {
  this.loading = false;
};

ViewNews.invalidate = function () {
  if (!this.initialized) return;
  this.extNewNewsTicket.show();
};

ViewNews.refresh = function () {
  this.loading = true;
  var Action = new CGActionLoadNewsPage();
  Action.Start = 0;
  Action.Limit = NEWS_PAGE_SIZE;
  Action.Clean = true;
  Action.execute();
};

ViewNews.onScroll = function () {
  if (this.extScrollParent.dom.scrollHeight - this.extScrollParent.dom.clientHeight - this.extScrollParent.dom.scrollTop < 200 && !this.loading) {
    this.loading = true;
    var Action = new CGActionLoadNewsPage();
    Action.Start = this.loaded + 1;
    Action.Limit = NEWS_PAGE_SIZE;
    Action.execute();
  }
};

ViewNews.onFocus = function () {
  var txtArea = this;
  txtArea.dom.value = "";
  txtArea.up("li").removeClass("disable");
};

ViewNews.onFocusNewPost = function () {
  var txtArea = this;
  txtArea.dom.value = "";
  txtArea.up(".newpost").removeClass("disable");
};

ViewNews.onBlurLuncher = function () {
  setTimeout(ViewNewsCommentAtBlur.bind(this), 150);
};

ViewNews.onNewPostBlurLuncher = function () {
  setTimeout(ViewNewsNewPostAtBlur.bind(this), 150);
};

ViewNewsCommentAtBlur = function () {
  var eTxtArea = this;
  var comment = eTxtArea.up("li");
  if (comment.hasClass("disable"))
    return;
  eTxtArea.dom.value = eTxtArea.dom.title;
  comment.addClass("disable");
  eTxtArea.blur();
};

ViewNewsNewPostAtBlur = function () {
  var eTxtArea = this;
  var post = eTxtArea.up(".newpost");
  if (post.hasClass("disable"))
    return;
  eTxtArea.dom.value = eTxtArea.dom.title;
  post.addClass("disable");
  eTxtArea.blur();
};
