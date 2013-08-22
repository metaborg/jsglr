package org.spoofax.jsglr.unicode;

import static org.spoofax.jsglr.unicode.UnicodeConverter.decodeAsciiToUnicode;

import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.StrategoConstructor;
import org.spoofax.terms.attachments.AbstractWrappedTermFactory;

/**
 * THe {@link UnicodeTermFactory} decodes all String parameters from ascii encoded
 * unicode to usual unicode.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeTermFactory extends AbstractWrappedTermFactory {

	public UnicodeTermFactory(int storageType, ITermFactory baseFactory) {
		super(storageType, baseFactory);
	}

	@Override
	public StrategoConstructor makeConstructor(String name, int arity) {;
		return super.makeConstructor(decodeAsciiToUnicode(name), arity);
	}

	@Override
	public IStrategoString makeString(String s) {
		return super.makeString(decodeAsciiToUnicode(s));
	}

	@Override
	public IStrategoString tryMakeUniqueString(String s) {
		return super.tryMakeUniqueString(decodeAsciiToUnicode(s));
	}

	@Override
	public IStrategoTerm parseFromString(String text) throws ParseError {
		return super.parseFromString(decodeAsciiToUnicode(text));
	}

	public ITermFactory getFactoryWithStorageType(int arg0) {
		return this;
	}

}
