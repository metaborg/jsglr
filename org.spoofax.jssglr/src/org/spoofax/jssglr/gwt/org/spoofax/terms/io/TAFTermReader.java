/*
 * Created on 27. jan.. 2007
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.terms.io;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.javaoverride.lang.Character2;

import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.AbstractTermFactory;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.StringTermReader;
import org.spoofax.terms.io.binary.TermReader;

/**
 * @see TermReader  An extension of this class that also supports binary ATerms.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TAFTermReader extends StringTermReader {
	
	private final StringBuilder sharedBuilder = new StringBuilder();
	
    public TAFTermReader(ITermFactory factory) {
    	super(factory);
    }
    
    public IStrategoTerm parseFromFile(String path) throws IOException, ParseError {
        InputStream stream = new FileInputStream(path);
        System.err.println("parseFromFile");
        try {
            return parseFromStream(stream);
        } finally {
            stream.close();
        }
    }

    public IStrategoTerm parseFromStream(InputStream inputStream) throws IOException, ParseError {
    	System.err.println("parseFromStream");

    	try {
	        if (!(inputStream instanceof BufferedInputStream))
	            inputStream = new BufferedInputStream(inputStream);
	        PushbackInputStream bis = new PushbackInputStream(inputStream);
	        
	        return parseFromStream(bis);
    	} finally {
    		inputStream.close();
    	}
    }

    protected IStrategoTerm parseFromStream(PushbackInputStream bis) throws IOException, ParseError {
        parseSkip(bis);
        final int ch = bis.read();
        switch(ch) {
        case '[': return parseAnno(bis, parseList(bis));
        case '(': return parseAnno(bis, parseTuple(bis));
        case '"': return parseAnno(bis, parseString(bis));
        case '<': return parsePlaceholder(bis);
        case '!': throw new ParseError("Unsupported ATerm format: TAF");
        default:
            bis.unread(ch);
            if (Character2.isLetter(ch)) {
                return parseAnno(bis, parseAppl(bis));
            }
            else if (Character2.isDigit(ch) || ch == '-')
                return parseAnno(bis, parseNumber(bis));
        }
        throw new ParseError("Invalid term: '" + (char)ch + "'");
    }
    
    private IStrategoTerm parseAnno(PushbackInputStream bis, IStrategoTerm term) throws IOException, ParseError {
        parseSkip(bis);
        final int ch = bis.read();
        if (ch == '{') {
            List<IStrategoTerm> annos = parseTermSequence(bis, '}');
            return factory.annotateTerm(term, factory.makeList(annos));
        } else {
            bis.unread(ch);
            return term;
        }
    }

    private IStrategoTerm parseString(PushbackInputStream bis) throws IOException, ParseError {
        int ch = bis.read();
        if(ch == '"')
            return factory.makeString("");
        StringBuilder sb = sharedBuilder;
        sb.setLength(0);
        boolean escaped = false;
        do {
            escaped = false;
            if(ch == '\\') {
                escaped = true;
                ch = bis.read();
            }
            if(escaped) {
                switch(ch) {
                case 'n':
                    sb.append('\n');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case 'b':
                    sb.append('\b');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case 'r':
                    sb.append('\r');
                    break;
                case '\\':
                    sb.append('\\');
                    break;
                case '\'':
                    sb.append('\'');
                    break;
                case '\"':
                    sb.append('\"');
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    throw new NotImplementedException();
                default:
                    sb.append("\\" + (char)ch); 
                }
                ch = bis.read();
            } else if(ch != '\"') {
                if (ch == -1)
                    throw new ParseError("Unterminated string: " + sb);
                sb.append((char)ch);
                ch = bis.read();
            }
        } while(escaped || ch != '\"');
        return factory.makeString(sb.toString());
    }

    private IStrategoTerm parseAppl(PushbackInputStream bis) throws IOException, ParseError {
        //System.err.println("appl");
        StringBuilder sb = sharedBuilder;
        sb.setLength(0);
        int ch;
        
        ch = bis.read();
        do {
            sb.append((char)ch);
            ch = bis.read();
        } // TODO: use a switch for this
          while(Character2.isLetterOrDigit(ch) || ch == '_' || ch == '-'
            || ch == '+' || ch == '*' || ch == 'd');

        String constructor = sb.toString();
        
        //System.err.println(" - " + sb.toString());
        
        bis.unread(ch);
        parseSkip(bis);
        ch = bis.read();

        if(ch == '(') {
            List<IStrategoTerm> l = parseTermSequence(bis, ')');
            IStrategoConstructor c = factory.makeConstructor(constructor, l.size());
            return factory.makeAppl(c, l.toArray(new IStrategoTerm[l.size()]));
        } else {
            bis.unread(ch);
            IStrategoConstructor c = factory.makeConstructor(constructor, 0);
            return factory.makeAppl(c, AbstractTermFactory.EMPTY);
        }
    }
    
    private IStrategoTerm parsePlaceholder(PushbackInputStream bis) throws IOException, ParseError {
        IStrategoTerm template = parseFromStream(bis);
        parseSkip(bis);
        if (bis.read() != '>')
            throw new ParseError("Expected: '>'");
        return factory.makePlaceholder(template);
    }

    private IStrategoTerm parseTuple(PushbackInputStream bis) throws IOException, ParseError {
        //System.err.println("tuple");
        return factory.makeTuple(parseTermSequence(bis, ')').toArray(AbstractTermFactory.EMPTY));
    }

    private List<IStrategoTerm> parseTermSequence(PushbackInputStream bis, char endChar) throws IOException, ParseError {
        //System.err.println("sequence");
        List<IStrategoTerm> els = Collections.emptyList();
        parseSkip(bis);
        int ch = bis.read();
        if(ch == endChar)
            return els;
        els = new ArrayList<IStrategoTerm>();
        bis.unread(ch);
        do {
            els.add(parseFromStream(bis));
            parseSkip(bis);
            ch = bis.read();
        } while(ch == ',');
        
        if (ch != endChar) {
            bis.unread(ch);
            parseSkip(bis);
            ch = bis.read();
        }

        if(ch != endChar)
        	throw new ParseError("Sequence must end with '" + endChar + "', saw '" + (char)ch + "' '" + (char) bis.read() + "' after items " + els);
        
        return els;
    }

    private IStrategoTerm parseList(PushbackInputStream bis) throws IOException, ParseError {
        //System.err.println("list");
        return factory.makeList(parseTermSequence(bis, ']'));
    }

    private IStrategoTerm parseNumber(PushbackInputStream bis) throws IOException {
        //System.err.println("number");
        String whole = parseDigitSequence(bis);
        
        int ch = bis.read();
        if(ch == '.') {
            String frac = parseDigitSequence(bis);
            ch = bis.read();
            if(ch == 'e' || ch == 'E') {
                String exp = parseDigitSequence(bis);
                double d = Double.parseDouble(whole + "." + frac + "e" + exp);
                return factory.makeReal(d);
            }
            bis.unread(ch);
            double d = Double.parseDouble(whole + "." + frac);
            return factory.makeReal(d);
        }
        bis.unread(ch);
        return factory.makeInt(Integer.parseInt(whole));
    }

    private String parseDigitSequence(PushbackInputStream bis) throws IOException {
        StringBuilder sb = sharedBuilder;
        sb.setLength(0);
        int ch = bis.read();
        do {
            sb.append((char)ch);
            ch = bis.read();
        } while(Character2.isDigit(ch));
        bis.unread(ch);
        return sb.toString(); 
    }
    
    private void parseSkip(PushbackInputStream input) throws IOException {
        for (;;) {
            int b = input.read();
            switch (b) {
                case ' ': case '\t': case '\n':
                    continue;
                default:
                    input.unread(b);
                    return;
            }
        }
    }
    

    public void unparseToFile(IStrategoTerm t, OutputStream ous) throws IOException {
    	/*
        Writer out = new BufferedWriter(new OutputStreamWriter(ous));
        unparseToFile(t, out);
        out.flush();
        */
    }

    public void unparseToFile(IStrategoTerm t, Writer out) throws IOException {
        t.writeAsString(out, Integer.MAX_VALUE);
    }

}
