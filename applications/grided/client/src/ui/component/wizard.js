
function Wizard() {
  this.currentPage = 0;
  this.pagesCount = 0;
  this.pages = [];
}

//-----------------------------------------------------------------------------------
Wizard.prototype.addPage = function(page) {
  this.pages[this.pagesCount++] = page;
};

//-----------------------------------------------------------------------------------
Wizard.prototype.isInFirstPage = function() {
  return this.currentPage === 0;
};

//-----------------------------------------------------------------------------------
Wizard.prototype.isInLastPage = function() {
  return this.currentPage + 1 === this.pagesCount;
};

//-----------------------------------------------------------------------------------
Wizard.prototype.isCurrentPageCompleted = function() {
  return this.pages[this.currentPage].isCompleted();
};

//-----------------------------------------------------------------------------------
Wizard.prototype.getCurrentPage = function() {
  return this.pages[this.currentPage];
};

//-----------------------------------------------------------------------------------
Wizard.prototype.start = function() {
  this.currentPage = 0;
  this._hideAllPages();
  this.pages[this.currentPage].show();
};

//-----------------------------------------------------------------------------------
Wizard.prototype.next = function() {
  if (this.currentPage >= this.pagesCount) return;
  this.pages[this.currentPage].hide();
  this.currentPage += 1;
  this.pages[this.currentPage].show();
};

//-----------------------------------------------------------------------------------
Wizard.prototype.previous = function() {
  if (this.currentPage == 0) return;
  this.pages[this.currentPage].hide();
  this.currentPage -= 1;
  this.pages[this.currentPage].show();
};

//-----------------------------------------------------------------------------------
Wizard.prototype._hideAllPages = function() {
  for (var i=0 , l = this.pages.length; i < l; i++) {
    var page = this.pages[i];
    page.hide();
  }
};

//-----------------------------------------------------------------------------------
// Wizard Page
//-----------------------------------------------------------------------------------

function Page(composite) {
  this.composite = composite;    
}

//-----------------------------------------------------------------------------------
Page.prototype.show = function() {
  this.composite.show();
};

//-----------------------------------------------------------------------------------
Page.prototype.hide = function() {
  this.composite.hide();
};
