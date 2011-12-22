define('ace/jssglr', ['require', 'exports', 'module' ], function(require, exports, module) {

$stats = function(){};
$self = self;
$sessionId = null;
$wnd = {};
$doc = {}; //firefox fix
$wnd.XMLHttpRequest = XMLHttpRequest;
$wnd.setTimeout = function() { setTimeout.apply(self, arguments) };
$wnd.clearTimeout = function() { clearTimeout.apply(self, arguments) };
$wnd.clearInterval = function() { clearInterval.apply(self, arguments) };
$wnd.alert = function(x) { }; //chrome fix

