function ProviderDefinitionTreeNodeAdapter() {
};

//--------------------------------------------------------------------------------
ProviderDefinitionTreeNodeAdapter.prototype.toNode = function(providerDefinition) {	       
  var node = new Ext.tree.TreeNode({     
    text: providerDefinition.label,
    leaf: true      
  });
     
//  node.attributes.label = providerDefinition.label;
//  node.attributes.code = providerDefinition.code;  
  node.attributes.type = providerDefinition.type;
  node.attributes.isInstanced = providerDefinition.isInstanced;
  return node;
};

//--------------------------------------------------------------------------------
ProviderDefinitionTreeNodeAdapter.prototype.toProviderDefinition = function(node) {
  var providerDefinition = new ProviderDefinition();  
  providerDefinition.label = node.text;  
  providerDefinition.code = node.id;
  providerDefinition.type = node.attributes.type;
  providerDefinition.isInstanced = node.attributes.isInstanced;
  
  return providerDefinition;
};
