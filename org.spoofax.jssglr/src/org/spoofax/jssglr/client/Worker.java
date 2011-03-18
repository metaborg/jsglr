package org.spoofax.jssglr.client;

import com.google.gwt.webworker.client.DedicatedWorkerEntryPoint;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;

import org.spoofax.jssglr.client.services.*;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author Karl Trygve Kalleberg &lt;karltk@spoofax.org&gt;
 *
 * Licensed under MPL 1.1/GPL 2.0/LGPL 2.1
 */
public class Worker extends DedicatedWorkerEntryPoint {

	private Parser parser;

	private native void nativeOnWorkerLoad() /*-{
		var oneself = this;

		self.spoofax = {};
		self.spoofax.createParser = function(lang) {
			return oneself.@org.spoofax.jssglr.client.Worker::createParser(Ljava/lang/String;)(lang);
		}
	}-*/;

	@Override
	public void onWorkerLoad() {
		nativeOnWorkerLoad();
	}

	private JavaScriptObject createParser(String grammarUrl) {
		parser = new Parser();
		return parser.asyncInitializeFromURL(grammarUrl);
	}
}
