package org.spoofax.interpreter.library.jsglr;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.jsglr.client.Disambiguator;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;

/**
 * Extends the JSGLR_parse_string_pt primitive with support
 * for filter settings and tree imploding.
 *
 * @see JSGLR_parse_string_pt_compat
 *
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class JSGLR_parse_string_compat extends STRSGLR_parse_string_pt {

	public static final String NAME = "JSGLR_parse_string_compat";

	protected JSGLR_parse_string_compat(Disambiguator filterSettings, AtomicBoolean recoveryEnabled) {
		super(NAME, filterSettings, recoveryEnabled);
	}

	@Override
	protected ITreeBuilder createTreeBuilder(IContext env) {
		return new TreeBuilder(new TermTreeFactory(env.getFactory()));
	}

}
