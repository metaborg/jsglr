package org.spoofax.interpreter.library.jsglr;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.Disambiguator;
import org.spoofax.jsglr.client.ITreeBuilder;

/**
 * Extends the JSGLR_parse_string_pt primitive with support
 * for filter settings.
 *
 * @see jsglr_parser_compat
 *
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class JSGLR_parse_string_pt_compat extends STRSGLR_parse_string_pt {

	public static final String NAME = "JSGLR_parse_string_pt_compat";

	protected JSGLR_parse_string_pt_compat(Disambiguator filterSettings, AtomicBoolean recoveryEnabled) {
		super(NAME, filterSettings, recoveryEnabled);
	}

	protected ITreeBuilder createTreeBuilder(IContext env) {
		return new Asfix2TreeBuilder(env.getFactory());
	}

}
