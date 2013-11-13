package org.spoofax.jsglr.unicode;

import org.spoofax.jsglr.unicode.UnicodeConverter;
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

	private static void printString(String s) {
	/*	System.out.print(s + "," +s.length() +": ");
		for (char c : s.toCharArray()) {
			System.out.print((int)c + " - ");
		}
		System.out.println();*/
	}
	
	private static String decodeAsciiToUnicodeSafe(String s) {
		try {
			return UnicodeConverter.decodeAsciiToUnicode(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public UnicodeTermFactory(int storageType, ITermFactory baseFactory) {
		super(storageType, baseFactory);
	}

	@Override
	public StrategoConstructor makeConstructor(String name, int arity) {;
		return super.makeConstructor(decodeAsciiToUnicodeSafe(name), arity);
	}

	@Override
	public IStrategoString makeString(String s) {
		printString(s);
		return super.makeString(decodeAsciiToUnicodeSafe(s));
	}

	@Override
	public IStrategoString tryMakeUniqueString(String s) {
		return super.tryMakeUniqueString(decodeAsciiToUnicodeSafe(s));
	}

	@Override
	public IStrategoTerm parseFromString(String text) throws ParseError {
		return super.parseFromString(UnicodeConverter.encodeUnicodeToAscii(text));
	}

	public ITermFactory getFactoryWithStorageType(int arg0) {
		return this;
	}

}
