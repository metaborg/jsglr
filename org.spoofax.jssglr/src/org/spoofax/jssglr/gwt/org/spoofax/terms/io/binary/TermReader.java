package org.spoofax.terms.io.binary;

//import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.io.TAFTermReader;

/**
 * A term reader that supports both textual and binary ATerms.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TermReader extends TAFTermReader {
	
	public TermReader(ITermFactory factory) {
		super(factory);
	}
    
    @Override
	public IStrategoTerm parseFromStream(InputStream inputStream) throws IOException, ParseError {
    	try {
        	return SAFReader.readTermFromSAFStream(factory, inputStream);
    	} finally {
    	}
    }
    @Override
	public IStrategoTerm parseFromFile(String fileName) throws IOException, ParseError {
		return parseFromStream(new FileInputStream(fileName));
	}    

}
