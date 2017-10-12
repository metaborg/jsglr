function stepForward() {
  if (step < actions.length)
    doStep(step++, gssNodes, parseForestNodes, gssEdges, parseForestEdges);
  else
    clearInterval(animationInterval);
}

function allForward() {
  var gssNodes = new vis.DataSet([]);
  var parseForestNodes = new vis.DataSet([]);

  var gssEdges = new vis.DataSet([]);
  var parseForestEdges = new vis.DataSet([]);
  
  while (step < actions.length) {
    doStep(step++, gssNodes, parseForestNodes, gssEdges, parseForestEdges);
  }
  
  gssNetwork.setData({
    nodes: gssNodes,
    edges: gssEdges
  });
  parseForestNetwork.setData({
    nodes: parseForestNodes,
    edges: parseForestEdges
  });

  clearInterval(animationInterval);
}

var animationInterval;

function play() {
  if (step < actions.length)
    animationInterval = setInterval(stepForward, 500);
}

function pause() {
  clearInterval(animationInterval);
}

function refresh() {
  pause();
  step = 0;
  
  gssData.nodes.clear();
  gssData.edges.clear();
  parseForestData.nodes.clear();
  parseForestData.edges.clear();
}

function setupVisualisation(actionsFromJson) {
  actions = actionsFromJson;
  step = 0;
}

function doStep(step, gssNodes, parseForestNodes, gssEdges, parseForestEdges) {
  var action = actions[step];
  
  doAction(action, gssNodes, parseForestNodes, gssEdges, parseForestEdges);
}

var lastDerivation = null;

function doAction(json, gssNodes, parseForestNodes, gssEdges, parseForestEdges) {
  switch (json.action) {
    case "start":
      var inputString = json.inputString;
    
      console.log("Starting parse for \"" + inputString + "\"");
      
      break;
    case "parseCharacter":
      var character = json.character;
      var activeStacks = json.activeStacks;
    
      console.log("Parse character '" + character + "' (active stacks: " + activeStacks + ")");
      
      break;
    case "createStackNode":
      var stackNumber = json.stackNumber;
      var stateNumber = json.stateNumber;
    
      console.log("Create stack " + stackNumber + " (state: " + stateNumber + ")");
      
      gssNodes.add({
        id: stackNumber,
        label: stackNumber + ": State " + stateNumber
      });
      
      break;
    case "createStackLink":
      var linkNumber = json.linkNumber;
      var fromStack = json.fromStack;
      var toStack = json.toStack;
      var parseNode = json.parseNode;
      var descriptor = json.descriptor;
    
      console.log("Create stack link " + linkNumber + " from stack " + fromStack + " to stack " + toStack + " (parse node: " + parseNode + " - " + descriptor + ")");
      
      gssEdges.add({
        id: linkNumber,
        from: fromStack,
        to: toStack,
        label: linkNumber + ": " + descriptor + "(" + parseNode + ")"
      });
      
      break;
    case "rejectStackLink":
      var linkNumber = json.linkNumber;
    
      var edge = gssEdges.get(linkNumber);
      
      edge.color = "#cc0000";
      
      gssEdges.update(edge);
    
      console.log("Reject stack link '" + linkNumber + "'");
      
      break;
    case "forActorStacks":
      var forActor = json.forActor;
      var forActorDelayed = json.forActorDelayed;
      
      console.log("forActor: " + forActor);
      console.log("forActorDelayed: " + forActorDelayed);
      
      break;
    case "actor":
      var stackNumber = json.stackNumber;
    
      console.log("Actor for stack " + stackNumber);
      
      break;
    case "skipRejectedStack":
      var stackNumber = json.stackNumber;
      
      var node = gssNodes.get(stackNumber);
      
      var red = {
        background: "#ff3300",
        border: "#cc0000"
      };
      
      node.color = red;
      node.color.highlight = red;
      node.color.hover = red;
      
      gssNodes.update(node);
    
      console.log("Skip rejected stack " + stackNumber + "");
      
      break;
    case "addForShifter":
      var stack = json.stack;
      var state = json.state;
    
      console.log("Add to shifter (stack: " + stack + ", state: " + state + ")");
      
      break;
    case "reduce":
      var parseNodes = json.parseNodes;
      var activeStackWithGotoState = json.activeStackWithGotoState;
      
      if (activeStackWithGotoState != -1)
        console.log("Reducing (parse forests: " + parseNodes.join(", ") + ") with existing active stack " + activeStackWithGotoState);
      else
        console.log("Reducing (parse forests: " + parseNodes.join(", ") + ") with no existing active stack");
      
      break;
    case "directLinkFound":
      var linkNumber = json.linkNumber;
      
      if (linkNumber != -1)
        console.log("Direct link found (link number: " + linkNumber + ")");
      else
        console.log("Direct link not found");
      
      break;
    case "acceptStackNode":
      var stackNumber = json.stackNumber;
      
      var node = gssNodes.get(stackNumber);
      
      var green = {
        background: "#33cc33",
        border: "#009933"
      };
      
      node.color = green;
      node.color.highlight = green;
      node.color.hover = green;
      
      gssNodes.update(node);
    
      console.log("Accept stack " + stackNumber + "");
      
      break;
    case "createParseNode":
      var nodeNumber = json.nodeNumber;
      var production = json.production;
      var term = json.term;
    
      console.log("Create parse symbol node " + nodeNumber + " (production: '" + production + "', term: '" + term + "')");
      
      createParseNode(nodeNumber, production, term, parseForestNodes);
      
      break;
    case "createDerivation":
      var nodeNumber = json.nodeNumber;
      var production = json.production;
      var term = json.term;
      var subTrees = json.subTrees;
    
      console.log("Create parse rule node " + nodeNumber + " (production: '" + production + "', term: '" + term + "', subTrees: [" + subTrees.join(", ") + "])");
      
      createDerivation(nodeNumber, production, term, subTrees, parseForestNodes, parseForestEdges);
      
      lastDerivation = nodeNumber;
      
      break;
    case "createCharacterNode":
      var nodeNumber = json.nodeNumber;
      var character = json.character;
      var startPosition = json.startPosition;
      var endPosition = json.endPosition;
    
      console.log("Create parse term node " + nodeNumber + " (character: '" + character + "')");
      
      createCharacterNode(nodeNumber, character, parseForestNodes, startPosition, endPosition);
      
      break;
    case "addDerivation":
      var parseNode = json.parseNode;
      var derivation = lastDerivation;
    
      console.log("Add parse node derivation (parse node: " + parseNode + ", derivation: " + derivation + ")");
      
      parseForestEdges.add({
        id: parseForestEdgeCount++,
        from: derivation,
        to: parseNode
      });
      
      break;
    case "shifter":
      var characterNode = json.characterNode;
      var elements = json.elements;
    
      console.log("Shifter (character node: " + characterNode + ", elements: [" + elements.map(function(forShifterElement) {
        return "(stack:" + forShifterElement.stack + ",state:" + forShifterElement.state + ")";
      }).join(",") + "])");
      
      break;
    case "success":
      console.log("Parse succeeded");
      
      break;
    default:
      console.error("Unknown action: " + json.action);
  }
}

function createParseNode(nodeNumber, production, term, parseForestNodes) {
  parseForestNodes.add({
    id: nodeNumber,
    label: "" + nodeNumber + " (p" + production + ")",
    shape: 'image',
    image: svhHtmlNode(term, 30)
  });
}

function createDerivation(nodeNumber, production, term, subTrees, parseForestNodes, parseForestEdges) {
  parseForestNodes.add({
    id: nodeNumber,
    label: "" + nodeNumber + " (p" + production + ")",
    shape: 'triangle'
  });
  
  subTrees.map(function(subTree) {
    parseForestEdges.add({
      id: parseForestEdgeCount++,
      from: subTree,
      to: nodeNumber
    });
  });
}

function createCharacterNode(nodeNumber, character, parseForestNodes, startPosition, endPosition) {
  parseForestNodes.add({
    id: nodeNumber,
    label: "" + nodeNumber + " (" + character + ") [" + startPosition + ", " + endPosition + "]",
    shape: 'text'
  });
}

function svhHtmlNode(html, lineHeight) {
  var content = `<div xmlns="http://www.w3.org/1999/xhtml" style="display: inline-block; font-family: Arial; font-size:` + (lineHeight * 0.75) + `px; line-height: ` + lineHeight + `px;">
    ` + html.replace(/</g, '&lt;').replace(/>/g, '&gt;') + `
  </div>`;
  
  var size = getSizeOfElement(content);
  
  var svgHtml = `
    <svg xmlns="http://www.w3.org/2000/svg" width="` + (size.width + 10) + `" height="` + size.height + `">
      <rect x="0" y="0" width="100%" height="100%" fill="#eeeeee" stroke-width="1" stroke="#000000"></rect>
      <foreignObject x="5" y="0" width="100%" height="100%">
        ` + content + `
      </foreignObject>
    </svg>
  `;
  
  return "data:image/svg+xml;charset=utf-8," + encodeURIComponent(svgHtml);
}

function getSizeOfElement(domElement) {
  var $element = $(domElement),
      $clone = $element.clone(),
      size = {
          width : 0,
          height : 0
      };

  $clone.addClass("sizingClone");

  $(document.documentElement).append($clone);

  size.width = $clone.width();
  size.height = $clone.height();
  
  $clone.remove();

  return size;
}