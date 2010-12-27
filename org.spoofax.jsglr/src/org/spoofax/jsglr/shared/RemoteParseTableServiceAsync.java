package org.spoofax.jsglr.shared;

import org.spoofax.jsglr.shared.terms.ATerm;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface RemoteParseTableServiceAsync {

	void fetchParseTable(String resourcePath, AsyncCallback<ATerm> callback);

	void readTermFromFile(String string, AsyncCallback<ATerm> callback);

	void fetchText(String string, AsyncCallback<String> callback);

}
