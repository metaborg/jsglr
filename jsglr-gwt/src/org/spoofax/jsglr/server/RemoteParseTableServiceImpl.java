package org.spoofax.jsglr.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.spoofax.jsglr.shared.RemoteParseTableService;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RemoteParseTableServiceImpl extends RemoteServiceServlet implements RemoteParseTableService {

	public ATerm fetchParseTable(String resourceName) {
		final ATermFactory f = new ATermFactory();
		try {
			final InputStream is = new FileInputStream(resourceName);
			System.out.println("Loading into buffer");
			final char[] buffer = new char[12*1024*1024]; // FIXME 12 MBs should be enough for everyone
			for(int i = 0; i < buffer.length; i++) {
				final int ch = is.read();
				if(ch == -1) {
					break;
				}
				buffer[i] = (char)ch;
			}
			System.out.println("Loading from buffer");
			final ATerm e = f.parse(new String(buffer));
			System.out.println("Loaded term, serializing");
			return e;
		} catch(final FileNotFoundException e) {
		} catch(final IOException e) {
		}
		System.err.println("Failed to load parse table " + resourceName);
		return null;
	}

	public ATerm readTermFromFile(String string) {
		return new ATermFactory().makeInt(0);
	}

	public String fetchText(String string) {
		return "abc";
	}
}
