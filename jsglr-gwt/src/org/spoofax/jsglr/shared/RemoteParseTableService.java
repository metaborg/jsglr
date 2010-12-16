package org.spoofax.jsglr.shared;

import org.spoofax.jsglr.shared.terms.ATerm;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parsetable")
public interface RemoteParseTableService {

	ATerm findParseTable(String resourceName);
}
