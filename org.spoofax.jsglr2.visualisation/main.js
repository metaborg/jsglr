var gssNodes = new vis.DataSet([]);
var parseForestNodes = new vis.DataSet([]);

var gssEdges = new vis.DataSet([]);
var parseForestEdges = new vis.DataSet([]);

var gssContainer = document.getElementById('gss');
var parseForestContainer = document.getElementById('parseForest');

var gssData = {
  nodes: gssNodes,
  edges: gssEdges
};
var parseForestData = {
  nodes: parseForestNodes,
  edges: parseForestEdges
};

var options = {
  edges: {
    arrows: "to"
  },
  layout: {
    hierarchical: {
      direction: "DU",
      sortMethod: "directed",
      levelSeparation: 100,
      nodeSpacing: 200,
      parentCentralization: false
    }
  },
  interaction: {dragNodes: true},
  physics: {
    enabled: false
  }
};

var gssOptions = jQuery.extend({}, options);
var parseForestOptions = jQuery.extend({}, options);

gssOptions.nodes = {
  shape: 'box',
  size: 10
};

parseForestOptions.nodes = {
  shape: 'image',
  size: 10
};

var gssNetwork = new vis.Network(gssContainer, gssData, gssOptions);
var parseForestNetwork = new vis.Network(parseForestContainer, parseForestData, parseForestOptions);

var actions;
var step;
var parseForestEdgeCount = 0;

$(document).ready(function() {
  $.getJSON("input.json", function(data) {
    setupVisualisation(data);
    
    allForward();
  });
  
  $("#step-forward").click(stepForward);
  $("#play").click(play);
  $("#pause").click(pause);
  $("#all-forward").click(allForward);
  $("#refresh").click(refresh);
});