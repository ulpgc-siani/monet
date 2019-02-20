/* ProvidersTree
 *   Events : select(node)  
 * 
 */

function ProvidersTree(id, loaderUrl) {      
  this.loaderUrl  = loaderUrl;
  this.treeLoader = this._createTreeLoader(this.loaderUrl);
  this.tree       = this._createTree(id);
  this.selection = null;
     
  this.handlers = {
	fn : null,
	scope: null
  };
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype.getSelection = function() {
  if (!this.selection) return null;
  return this.selection;
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype.select = function(node) {
  this.tree.getSelectionModel().select(node);	
  this.selection = node;
  var handler = this.handlers['select'];
  if (handler) handler.fn.apply(handler.scope, [this.getSelection()]);
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};      
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype.un = function(eventName) {
  this.handlers[eventName] = null;	
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype.findChild = function(attribute) {
  return this.root.findChild(attribute);
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype._createTree = function(id) {
  var Tree = Ext.tree;
  
  var tree = new Tree.TreePanel(id, {
    animate: true,
    loader : this.treeLoader,
    containerScroll: true,    
    rootVisible : false
  });
  
  this.root = new Tree.TreeNode({id : 'providers'});    
  tree.setRootNode(this.root);
  tree.on('beforeclick', this._beforeClickTreeHandler, this);
  this._addRootChildren(this.root);  
  
  tree.render();
  this.root.expand();
  
  return tree;
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype._createTreeLoader = function(url) {  
  var loader = new Ext.tree.TreeLoader({
	dataUrl : url		
  });
  
  loader.on('beforeload', this._beforeLoadTreeHandler, this);
  loader.on('load', this._afterLoadTreeHandler, this);
  return loader;
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype._addRootChildren = function(root) {
  this.serviceNode   = new Ext.tree.AsyncTreeNode({id:0,text:Lang.ProvidersTree.service, leaf:false, loader: this.treeLoader});
  this.thesaurusNode = new Ext.tree.AsyncTreeNode({id:1,text:Lang.ProvidersTree.thesaurus, leaf:false, loader: this.treeLoader});
  this.mapNode       = new Ext.tree.AsyncTreeNode({id:2,text:Lang.ProvidersTree.map, leaf:false, loader: this.treeLoader});
  this.cubeNode      = new Ext.tree.AsyncTreeNode({id:2,text:Lang.ProvidersTree.cube, leaf:false, loader: this.treeLoader});
	
  this.serviceNode.attributes.type = ProviderDefinitionTypes.service;
  this.thesaurusNode.attributes.type = ProviderDefinitionTypes.thesaurus;
  this.mapNode.attributes.type = ProviderDefinitionTypes.map;
  this.cubeNode.attributes.type = ProviderDefinitionTypes.cube;  
    
  root.appendChild(this.serviceNode);
  root.appendChild(this.thesaurusNode);
  root.appendChild(this.mapNode);
  root.appendChild(this.cubeNode);
  
  this.serviceNode.reload();
    
  this.thesaurusNode.on('load', function(node) {
	console.log("se llama al onload de thesaurus: " + node);	      	  
  }, this);
	  
  this.serviceNode.on('beforechildrenrendered', function(node) {	 
    var firstChild = node.firstChild;    
    if (firstChild) {firstChild.render(); /*this.select(firstChild);*/};
  },this);
  
  this.serviceNode.on('beforeappend', function(tree, parent, child) {
	console.log("se llama al beforeload of serviceNode");
  }, this);  
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype._beforeLoadTreeHandler = function(treeLoader, node, callback) {
  if (! node.isLeaf()) this.treeLoader.baseParams.type = node.attributes.type;    
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype._afterLoadTreeHandler = function(treeLoader, node, response) {
  var handler = this.handlers['load'];
  var nodes = this._getChildNodes(node);
  if (handler) handler.fn.apply(handler.scope, [this, nodes]);
  // select node after call to loadHandler
  var firstChild = this.serviceNode.firstChild;
  if (firstChild) this.select(firstChild);  
};


//-----------------------------------------------------------------------------------
ProvidersTree.prototype._beforeClickTreeHandler = function(node, event) {      
  this.select(node);
};

//-----------------------------------------------------------------------------------
ProvidersTree.prototype._getChildNodes = function(node) {
   return node.childNodes;
};
