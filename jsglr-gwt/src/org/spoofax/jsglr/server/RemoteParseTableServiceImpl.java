package org.spoofax.jsglr.server;

import org.spoofax.jsglr.shared.RemoteParseTableService;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RemoteParseTableServiceImpl extends RemoteServiceServlet implements RemoteParseTableService {

	public ATerm findParseTable(String resourceName) {
		return	new ATermFactory().makeInt(0);
	}
}
