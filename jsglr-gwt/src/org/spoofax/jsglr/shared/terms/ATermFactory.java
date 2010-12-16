package org.spoofax.jsglr.shared.terms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.spoofax.jsglr.client.NotImplementedException;
import org.spoofax.jsglr.client.PushbackStringIterator;

public class ATermFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	protected static final ATerm[] EMPTY = new ATerm[0];

	private final HashMap<AFun, AFun> afunCache = new HashMap<AFun, AFun>();

	public AFun makeAFun(String ctorName, int arity, boolean quoted) {
		final AFun candidate = new AFun(ctorName, arity, quoted);
		AFun cached = afunCache.get(candidate);
		if (cached == null) {
			cached = afunCache.put/*IfAbsent*/(candidate, candidate);
			return cached == null ? candidate : cached;
		} else {
			return cached;
		}
	}

	public ATermPlaceholder makePlaceholder(ATerm template) {
		return new ATermPlaceholder(this, makeAFun("<>", 1), template);
	}

	public ATermList makeList() {
		return new ATermList(this, null, null);
	}

	public ATermList makeList(ATerm... elements) {
		ATermList l = new ATermList(this, null, null);
		for(int i = elements.length - 1; i >= 0; i--) {
			l = l.prepend(elements[i]);
		}
		return l;
	}

	//	public ATermList makeList(List<ATerm> elements) {
	//		return new ATermList(this, elements.toArray(new ATerm[0]));
	//	}

	public ATerm parse(String text) {
		return parseFromString(text);
	}

	public ATermAppl makeAppl(AFun afun, ATermList kids) {
		return new ATermAppl(this, afun, kids);
	}

	public ATermAppl makeAppl(AFun afun, ATerm... kids) {
		return new ATermAppl(this, afun, kids);
	}

	public ATermAppl makeAppl(AFun afun, ATerm[] kids, boolean m) {
		if (m) {
			throw new UnsupportedOperationException("Use makeString() to make strings");
		}
		return new ATermAppl(this, afun, kids);
	}

	public ATermInt makeInt(int i) {
		return new ATermInt(this, i);
	}


	protected ATerm parseFromStream(PushbackStringIterator bis) {
		parseSkip(bis);
		final int ch = bis.read();
		switch(ch) {
		case '[': return parseAnno(bis, parseList(bis));
		case '(': return parseAnno(bis, parseTuple(bis));
		case '"': return parseAnno(bis, parseString(bis));
		case '<': return parsePlaceholder(bis);
		default:
			bis.unread(ch);
			if (Character.isLetter((char)ch)) {
				return parseAnno(bis, parseAppl(bis));
			}
			else if(Character.isDigit((char)ch)) {
				return parseAnno(bis, parseNumber(bis));
			}
		}
		throw new ParseError("Invalid term: '" + (char)ch + "'");
	}

	private ATerm parseAnno(PushbackStringIterator bis, ATerm term) {
		parseSkip(bis);
		final int ch = bis.read();
		if (ch == '{') {
			final List<ATerm> annos = parseTermSequence(bis, '}');
			return annotateTerm(term, makeList(annos.toArray(new ATerm[annos.size()])));
		} else {
			bis.unread(ch);
			return term;
		}
	}

	private ATerm parseString(PushbackStringIterator bis) {
		int ch = bis.read();
		if(ch == '"') {
			return makeString("");
		}
		final StringBuilder sb = new StringBuilder();
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
				if (ch == -1) {
					throw new ParseError("Unterminated string: " + sb);
				}
				sb.append((char)ch);
				ch = bis.read();
			}
		} while(escaped || ch != '\"');
		return makeString(sb.toString());
	}

	public ATermString makeString(String string) {
		return new ATermString(this, string);
	}

	private ATerm parseAppl(PushbackStringIterator bis) {
		//System.err.println("appl");
		// TODO: share stringbuilder instances?
		final StringBuilder sb = new StringBuilder();
		int ch = bis.read();
		do {
			sb.append((char)ch);
			ch = bis.read();
		} // TODO: use a switch for this
		while(Character.isLetterOrDigit((char)ch) || ch == '_' || ch == '-'
			|| ch == '+' || ch == '*' || ch == '$');

		//System.err.println(" - " + sb.toString());

		bis.unread(ch);
		parseSkip(bis);
		ch = bis.read();

		if(ch == '(') {
			final List<ATerm> l = parseTermSequence(bis, ')');
			final AFun c = makeAFun(sb.toString(), l.size());
			return makeAppl(c, l.toArray(EMPTY));
		} else {
			bis.unread(ch);
			final AFun c = makeAFun(sb.toString(), 0);
			return makeAppl(c, EMPTY);
		}
	}


	private AFun makeAFun(String ctorName, int arity) {
		return makeAFun(ctorName, arity, false);
	}

	private ATerm parsePlaceholder(PushbackStringIterator bis) {
		final ATerm template = parseFromStream(bis);
		parseSkip(bis);
		if (bis.read() != '>') {
			throw new ParseError("Expected: '>'");
		}
		return makePlaceholder(template);
	}

	private ATerm parseTuple(PushbackStringIterator bis) {
		//System.err.println("tuple");
		return makeTuple(parseTermSequence(bis, ')').toArray(EMPTY));
	}

	public ATermTuple makeTuple(ATerm[] elements) {
		return new ATermTuple(this, elements);
	}

	private List<ATerm> parseTermSequence(PushbackStringIterator bis, char endChar) {
		//System.err.println("sequence");
		final int pos = bis.getOffset();
		List<ATerm> els = Collections.emptyList();
		parseSkip(bis);
		int ch = bis.read();
		if(ch == endChar) {
			return els;
		}
		els = new ArrayList<ATerm>();
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

		if(ch != endChar) {
			throw new ParseError("Sequence starting at offset " + pos + " must end with '" + endChar + "', saw '" + (char)ch + "'");
		}

		return els;
	}

	private ATerm parseList(PushbackStringIterator bis) {
		//System.err.println("list");
		return makeList(parseTermSequence(bis, ']').toArray(new ATerm[0]));
	}

	private ATerm parseNumber(PushbackStringIterator bis) {
		//System.err.println("number");
		final String whole = parseDigitSequence(bis);

		int ch = bis.read();
		if(ch == '.') {
			final String frac = parseDigitSequence(bis);
			ch = bis.read();
			if(ch == 'e' || ch == 'E') {
				final String exp = parseDigitSequence(bis);
				final double d = Double.parseDouble(whole + "." + frac + "e" + exp);
				return makeReal(d);
			}
			bis.unread(ch);
			final double d = Double.parseDouble(whole + "." + frac);
			return makeReal(d);
		}
		bis.unread(ch);
		return makeInt(Integer.parseInt(whole));
	}

	public ATerm makeReal(double d) {
		throw new NotImplementedException();
	}

	private String parseDigitSequence(PushbackStringIterator bis) {
		final StringBuilder sb = new StringBuilder();
		int ch = bis.read();
		do {
			sb.append((char)ch);
			ch = bis.read();
		} while(Character.isDigit((char)ch));
		bis.unread(ch);
		return sb.toString();
	}

	public ATerm parseFromString(String text) {
		return parseFromStream(new PushbackStringIterator(text));
	}

	private void parseSkip(PushbackStringIterator input){
		for (;;) {
			final int b = input.read();
			switch (b) {
			case ' ': case '\t': case '\n':
				continue;
			default:
				input.unread(b);
				return;
			}
		}
	}

	public ATerm annotateTerm(ATerm term, ATerm annotations) {
		throw new NotImplementedException();
	}

}
