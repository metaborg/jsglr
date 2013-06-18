package org.spoofax.jsglr.unicode;

import static org.spoofax.jsglr.unicode.UnicodeConverter.decodeAsciiToUnicode;

import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.StrategoConstructor;
import org.spoofax.terms.TermFactory;

/**
 * THe {@link UnicodeTermFactory} decodes all String parameters from ascii encoded
 * unicode to usual unicode and delegates the rest to the {@link TermFactory}.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeTermFactory extends TermFactory {

	public StrategoConstructor makeConstructor(String name, int arity) {;
		return super.makeConstructor(decodeAsciiToUnicode(name), arity);
	}

	public IStrategoString makeString(String s) {
		return super.makeString(decodeAsciiToUnicode(s));
	}

	public IStrategoString tryMakeUniqueString(String s) {
		return super.tryMakeUniqueString(decodeAsciiToUnicode(s));
	}

	@Override
	public IStrategoTerm parseFromString(String text) throws ParseError {
		return super.parseFromString(decodeAsciiToUnicode(text));
	}

}
