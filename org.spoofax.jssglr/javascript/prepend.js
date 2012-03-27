$stats = function(){};
self = {};
$self = self;

navigator = {};
navigator.userAgent = 'node-js';

$sessionId = null;
$wnd = {};
window = {};
$doc = {}; //firefox fix
$wnd.setTimeout = function() { setTimeout.apply(self, arguments) };
$wnd.clearTimeout = function() { clearTimeout.apply(self, arguments) };
$wnd.clearInterval = function() { clearInterval.apply(self, arguments) };
$wnd.alert = function(x) { }; //chrome fix

JSSGLR = {};

JSSGLR._loaded = function() {

