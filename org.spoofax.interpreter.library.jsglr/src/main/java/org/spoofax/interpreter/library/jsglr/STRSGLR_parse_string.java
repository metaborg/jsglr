/*
 * Copyright (c) 2005-2011, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.jsglr.client.Disambiguator;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;

public class STRSGLR_parse_string extends STRSGLR_parse_string_pt {

	public final static String NAME = "STRSGLR_parse_string";

	protected STRSGLR_parse_string(String name, Disambiguator filterSettings, AtomicBoolean recoveryEnabled) {
		super(name, filterSettings, recoveryEnabled);
	}

	protected STRSGLR_parse_string(Disambiguator filterSettings, AtomicBoolean recoveryEnabled) {
		this(NAME, filterSettings, recoveryEnabled);
	}

	@Override
	protected ITreeBuilder createTreeBuilder(IContext env) {
		return new TreeBuilder(new TermTreeFactory(env.getFactory()));
	}
}
