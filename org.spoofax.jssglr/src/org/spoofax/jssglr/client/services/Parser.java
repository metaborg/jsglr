package org.spoofax.jssglr.client.services;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.terms.TermFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

/**
 * A parser for a specific editor instance.
 */
public class Parser {

	// TODO: get list of incremental sorts from some place
	private final String[] HACK_DEFAULT_INCREMENTAL_SORTS =
		{ "RuleDec", "SDec",
		  "MethodDec", "ClassBodyDec", "ClassMemberDec", "ConstrDec", "FieldDec" };

	private final ITermFactory af;
	private ParseTable parseTable;
	private TreeBuilder treeBuilder;
	private SGLR sglr;
	private Set<String> incrementalSorts;
	private IStrategoTerm lastResult;

	public Parser() {
		af = new TermFactory();

		incrementalSorts = new HashSet<String>();
		for (String s : HACK_DEFAULT_INCREMENTAL_SORTS)
			incrementalSorts.add(s);
	}

	public JavaScriptObject asyncInitializeFromURL(String parseTableURL) {
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, parseTableURL);
		try {
			builder.sendRequest( null,  new RequestCallback() {
				@Override
				public void onError(Request request, Throwable exception)
				{
					GWT.log( "error", exception );
				}
				@Override
				public void onResponseReceived(Request request, Response response) {
					if(response.getStatusCode() == 200 || response.getStatusCode() == 304) {
						initialize(response.getText());
					}
				}
			});
		} catch (final RequestException e) {
			GWT.log( "error", e);
		}

		return exposeParser(this);
	}

	private void initialize(String tableContents) {
		// TODO: share table across multiple Parser instances
		IStrategoTerm tableTerm = af.parseFromString(tableContents);
		try {
			parseTable = new ParseTable(tableTerm, af);
			TermTreeFactory factory = new TermTreeFactory(af);
			treeBuilder = new TreeBuilder(factory);
			sglr = new SGLR(treeBuilder, parseTable);
			sglr.setUseStructureRecovery(true);
//			sglr = new IncrementalSGLR<IStrategoTerm>(parser, C_STYLE, factory, incrementalSorts);
		} catch (InvalidParseTableException e) {
			GWT.log("error", e);
		}
	}

	public boolean isReady() {
		return sglr != null;
	}

	private native JavaScriptObject exposeParser (Parser parser) /*-{
		var self = this;
		var parser = {};
		parser.parse = function (text) {
			return self.@org.spoofax.jssglr.client.services.Parser::parse(Ljava/lang/String;)(text);
		};
		parser.parseAndTokenize = function (lineCount, text) {
			return self.@org.spoofax.jssglr.client.services.Parser::parseAndTokenize(ILjava/lang/String;)(lineCount, text);
		};
		parser.isReady = function() {
			return self.@org.spoofax.jssglr.client.services.Parser::isReady()();
		};
		return parser;
	}-*/;

	@SuppressWarnings("unchecked")
	public JavaScriptObject parseAndTokenize(int lines, String text) {
		final JsArray<JavaScriptObject>[] attrs = new JsArray[lines];
		for(int i = 0; i < lines; i++) {
			attrs[i] = (JsArray<JavaScriptObject>) JavaScriptObject.createArray();
		}
		final ISimpleTerm o = parse(text);
		if(o == null) {
			return makeJsArray(attrs);
		}
		final IToken t = ImploderAttachment.get(o).getLeftToken();
		if(t == null) {
			return makeJsArray(attrs);
		}
		final ITokenizer tok = t.getTokenizer();
		for(int i = 0; i < tok.getTokenCount(); i++) {
			final IToken x = tok.getTokenAt(i);
			//debugToken(x);

			final int start = x.getColumn();
			final int end = x.getEndOffset() - x.getStartOffset() + start + 1;
			final String tokentype = convertTokenType(x.getKind());
			attrs[x.getLine()-1].push(createBespinToken(x.toString(), tokentype, start, end, x.getLine()));
		}
		return makeJsArray(attrs);
	}

	public native static JavaScriptObject createBespinToken(String value, String tokentype, int startColumn, int endColumn, int lineNumber) /*-{
		return {
	            type: tokentype,
				value: value,
	            start: startColumn,
	            end: endColumn,
	            line: lineNumber
		       };
	}-*/;

	@SuppressWarnings("unchecked")
	private JsArray<JavaScriptObject> makeJsArray(JsArray<JavaScriptObject>[] attrs) {
		JsArray<JavaScriptObject> r = (JsArray<JavaScriptObject>) JavaScriptObject.createArray();
		for(JavaScriptObject o : attrs)
			r.push(o);
		return r;
	}

	private void debugToken(final IToken x) {
		System.out.println("line  = " + x.getLine());
		System.out.println("start = " + x.getColumn());
		System.out.println("end   = " + (x.getColumn() + x.getEndOffset() - x.getStartOffset() + 1));
		System.out.println("kind  = " + x.getKind());
		System.out.println("tag   = " + convertTokenType(x.getKind()));
		System.out.println("tok   = \"" + x.toString() + "\"");
	}

	private String convertTokenType(int kind) {
		switch(kind) {
		case IToken.TK_LAYOUT: return "comment";
		case IToken.TK_NUMBER: return "constant.numeric";
		case IToken.TK_OPERATOR: return "keyword.operator";
		case IToken.TK_KEYWORD: return "keyword";
		case IToken.TK_STRING: return "string";
		default: return "plain";
		}
	}

	public IStrategoTerm parse(String text) {
		try {
			IStrategoTerm result;
//			try {
//				result = sglr.parseIncremental(text, null, null);
//			} catch (IncrementalSGLRException e) {
				result = (IStrategoTerm)sglr.parse(text, null, null);
//			}
			System.out.println(result);
			return result;
		} catch (final TokenExpectedException e) {
			e.printStackTrace();
		} catch (final BadTokenException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		} catch (final SGLRException e) {
			e.printStackTrace();
		}
		return null;
	}
}
