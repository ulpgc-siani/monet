//----------------------------------------------------------------------
// Show News
//----------------------------------------------------------------------
function CGActionShowNews() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowNews.prototype = new CGAction;
CGActionShowNews.constructor = CGActionShowNews;
CommandFactory.register(CGActionShowNews, null, true);

CGActionShowNews.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadSystemTemplate(this, "news");
};

CGActionShowNews.prototype.step_2 = function () {

  Desktop.Main.Center.Body.activateNews();

  ViewPageNews.setContent(this.data);
  ViewPageNews.refresh();
  ViewPageNews.show();

  this.setActiveFurniture(Furniture.NEWS);

  var Action = new CGActionNotificationsReadAll();
  Action.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Render News
//----------------------------------------------------------------------
function CGActionRenderNews() {
  this.base = CGAction;
  this.base(1);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderNews.prototype = new CGAction;
CGActionRenderNews.constructor = CGActionRenderNews;
CommandFactory.register(CGActionRenderNews, { IdDOMViewerLayer: 0 }, false);

CGActionRenderNews.prototype.step_1 = function () {
  ViewNews.init(this.IdDOMViewerLayer);
  ViewNews.refresh();
};

//----------------------------------------------------------------------
// Refresh News
//----------------------------------------------------------------------
function CGActionRefreshNews() {
  this.base = CGAction;
  this.base(1);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRefreshNews.prototype = new CGAction;
CGActionRefreshNews.constructor = CGActionRefreshNews;
CommandFactory.register(CGActionRefreshNews, null, false);

CGActionRefreshNews.prototype.step_1 = function () {
  ViewNews.refresh();
};

//----------------------------------------------------------------------
// Add Comment To News
//----------------------------------------------------------------------
function CGActionAddCommentToPost() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionAddCommentToPost.prototype = new CGAction;
CGActionAddCommentToPost.constructor = CGActionAddCommentToPost;
CommandFactory.register(CGActionAddCommentToPost, { PostId: 0 }, false);

CGActionAddCommentToPost.prototype.step_1 = function () {
  if (this.PostId == null) {
    this.terminate();
    return;
  }

  this.DOMCommentTextArea = Ext.get(this.DOMItem).getPrevSibling();
  var comment = this.DOMCommentTextArea.up("li");
  if (comment.hasClassName("disable")) {
    this.terminate();
    return;
  }

  var text = this.DOMCommentTextArea.value;
  if (text == EMPTY) {
    this.terminate();
    return;
  }
  this.DOMCommentTextArea.value = this.DOMCommentTextArea.title;
  this.DOMCommentTextArea.blur();
  comment.addClassName("disable");

  Kernel.addCommentToPost(this, this.PostId, text);
};

CGActionAddCommentToPost.prototype.step_2 = function () {
  var eComment = this.DOMCommentTextArea.up("li");
  var tmpDivHolder = document.createElement('div');
  tmpDivHolder.innerHTML = this.data;
  var html = Ext.get(tmpDivHolder).query(".comment")[0].parentNode.innerHTML;
  new Insertion.Before(eComment, html);
};


//----------------------------------------------------------------------
// Add Post To News
//----------------------------------------------------------------------
function CGActionAddPost() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionAddPost.prototype = new CGAction;
CGActionAddPost.constructor = CGActionAddPost;
CommandFactory.register(CGActionAddPost, { PostTextArea: 0, IdDOMLayer: 1}, false);

CGActionAddPost.prototype.step_1 = function () {
  if ((this.PostTextArea == null) || (this.IdDOMLayer == null)) {
    this.terminate();
    return;
  }

  var sNewText = ViewNews.getNewPostAndDisable(this.PostTextArea);
  if (!sNewText || sNewText == EMPTY) {
    this.terminate();
    return;
  }

  Kernel.addPost(this, sNewText);
};

CGActionAddPost.prototype.step_2 = function () {
  ViewNews.addPost(this.data);
  ViewNews.refreshHandlers();
};

//----------------------------------------------------------------------
//Load News Next Page
//----------------------------------------------------------------------
function CGActionLoadNewsPage() {
  this.base = CGAction;
  this.base(2);
};

CGActionLoadNewsPage.prototype = new CGAction;
CGActionLoadNewsPage.constructor = CGActionLoadNewsPage;
CommandFactory.register(CGActionLoadNewsPage, { Start: 0, Limit: 1}, false);

CGActionLoadNewsPage.prototype.step_1 = function () {
  if ((this.Start == null) || (this.Limit == null)) {
    this.terminate();
    return;
  }

  Kernel.loadNewsNextPage(this, this.Start, this.Limit);
};

CGActionLoadNewsPage.prototype.step_2 = function () {
  ViewNews.addNewsPage(this.data, this.Clean);
};

//----------------------------------------------------------------------
// Add Filter To News
//----------------------------------------------------------------------
function CGActionAddFilter() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionAddFilter.prototype = new CGAction;
CGActionAddFilter.constructor = CGActionAddFilter;
CommandFactory.register(CGActionAddFilter, { IdPostDOMLayer: 0, PostId: 1, Filter: 2}, false);

CGActionAddFilter.prototype.step_1 = function () {
  if ((this.IdPostDOMLayer == null) || (this.PostId == null) || (this.Filter == null)) {
    this.terminate();
    return;
  }

  Kernel.addFilter(this, this.PostId, this.Filter);
};

CGActionAddFilter.prototype.step_2 = function () {

};

//----------------------------------------------------------------------
// Update news
//----------------------------------------------------------------------
function CGActionUpdateNews() {
  this.base = CGAction;
  this.base(1);
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionUpdateNews.prototype = new CGAction;
CGActionUpdateNews.constructor = CGActionUpdateNews;
CommandFactory.register(CGActionUpdateNews, { }, false);

CGActionUpdateNews.prototype.step_1 = function () {
  ViewNews.invalidate();

  this.terminate();
};
