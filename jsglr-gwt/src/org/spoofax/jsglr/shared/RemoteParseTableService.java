package org.spoofax.jsglr.shared;

import org.spoofax.jsglr.shared.terms.ATerm;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parsetable")
public interface RemoteParseTableService extends RemoteService {

	ATerm fetchParseTable(String resourceName);

	ATerm readTermFromFile(String string);

	String fetchText(String string);
}
