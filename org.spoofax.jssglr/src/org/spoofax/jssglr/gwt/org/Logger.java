package org;

public class Logger
{
    public native static void Log(String s) /*-{
		console.log(s);
    }-*/;
    
    public native static void Debugger(Object s) /*-{
    	var x = s;
    	//JS Debugger
    	debugger;
    	console.log(x);
    }-*/;
    
    public native static void LogDataToPost(String str) /*-{
       	var DATACOLLECTINGSERVERURL = "http://127.0.0.1:8889";
    	var xmlhttpreq=undefined;
		if (xmlhttpreq === undefined)
		{
			try 
			{
				//Required when running from CLI (on V8)
				xmlhttpreq  = require("./XMLHttpRequest.js").XMLHttpRequest;
			} catch (err) {
				xmlhttpreq = XMLHttpRequest;
			}
		}
		try
		{
			xmlhttp=new xmlhttpreq();
			xmlhttp.open("POST", DATACOLLECTINGSERVERURL, false);
			xmlhttp.setRequestHeader(str, 'hack');//chrome Access-Control-Allow-Origin hack. Just want to send a string over. Let server handle retrieval
			xmlhttp.send("x", false);
		} catch (err)
		{
			console.log("Error while posting statistics: " + err.message);
		}
		return;
    }-*/;
}