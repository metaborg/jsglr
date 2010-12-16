package org.spoofax.client;

import java.io.IOException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class JSGLREntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new Label("Booting"));
		try {
			SGLR sglr = new SGLR(new ATermFactory(), new ParseTable(null));
			ATerm r = sglr.parse("foo bar baz");
			if(r != null)
				RootPanel.get().add(new Label(r.toString()));
			else {
				RootPanel.get().add(new Label("Parsing failed"));
			}
		} catch (InvalidParseTableException e) {
			wrap(e);
		} catch (TokenExpectedException e) {
			wrap(e);
		} catch (BadTokenException e) {
			wrap(e);
		} catch (ParseException e) {
			wrap(e);
		} catch (IOException e) {
			wrap(e);
		} catch (SGLRException e) {
			wrap(e);
		}

	}

	private static void wrap(Exception e) {
		throw new RuntimeException(e);
	}

}
