/* https://stackoverflow.com/questions/4176560/webdriver-get-elements-xpath */
getXPath = function(node) {
  if (node.id !== '') {
    return '//' + node.tagName.toLowerCase() + '[@id="' + node.id + '"]'
  }
  if (node === document.body) {
    return node.tagName.toLowerCase()
  }

  var nodeCount = 0;
  var childNodes = node.parentNode.childNodes;

  for (var i=0; i<childNodes.length; i++) {
    var currentNode = childNodes[i];
    if (currentNode === node) {
      return getXPath(node.parentNode) + '/' +
             node.tagName.toLowerCase() +  '[' + (nodeCount+1) + ']'
    }

    if (currentNode.nodeType === 1 &&
        currentNode.tagName.toLowerCase() === node.tagName.toLowerCase()) {
      nodeCount++
    }
  }
};
return getXPath(arguments[0]);