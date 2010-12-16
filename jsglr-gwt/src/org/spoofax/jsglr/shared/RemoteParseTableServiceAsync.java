package org.spoofax.jsglr.shared;

import org.spoofax.jsglr.shared.terms.ATerm;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface RemoteParseTableServiceAsync {

	void findParseTable(String resourcePath, AsyncCallback<ATerm> asyncCallback);

}
