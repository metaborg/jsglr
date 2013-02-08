package org.spoofax.jssglr.client.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.findLeftMostTokenOnSameLine;
import static org.spoofax.jsglr.client.imploder.AbstractTokenizer.findRightMostTokenOnSameLine;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
//import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.RegionRecovery;
import org.spoofax.jsglr.client.SGLR;
//import org.spoofax.jsglr.client.imploder.MemoryRecordingTreeBuilder;
import org.spoofax.jsglr.client.imploder.Token;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.jssglr.client.STRJSNativeTermFactory;

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

	private static final int LARGE_REGION_SIZE = 8;

	private static final String LARGE_REGION_START =
		"Region could not be parsed because of subsequent syntax error(s) indicated below";

	private final ITermFactory af;
	private ParseTable parseTable;
	private ITreeBuilder treeBuilder;
	private SGLR sglr;
	private boolean tableLoaded = false;
	private Set<String> incrementalSorts;
	private IStrategoTerm lastResult;
	
	private HashMap<String, SemanticError> _tokenTable = new HashMap<String, SemanticError>(); 

	public Parser(ITermFactory termFactory) {
		af = termFactory;

		incrementalSorts = new HashSet<String>();
		for (String s : HACK_DEFAULT_INCREMENTAL_SORTS)
			incrementalSorts.add(s);
	}

	public JavaScriptObject asyncInitializeFromURL(final String parseTableURL) {
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, parseTableURL);
		try {
			builder.sendRequest( null,  new RequestCallback() {
				@Override
				public void onError(Request request, Throwable exception)
				{
					tableLoaded = true;
					GWT.log( "error", exception );
					logToConsole("Could not load parse table at: '" + parseTableURL + "' due to: " + exception.getMessage());
				}
				@Override
				public void onResponseReceived(Request request, Response response) {
					tableLoaded = true;
					if(response.getStatusCode() == 200 || response.getStatusCode() == 304) {
						initializeFromString(response.getText());
						parseTableLoaded();
					} else {
						logToConsole("Could not load parse table at: " + parseTableURL);
					}
				}
			});
		} catch (final RequestException e) {
			GWT.log( "error", e);
			tableLoaded = true;
			logToConsole("Could not build parsetable request for: '" + parseTableURL + "' due to: " + e.getMessage());
		}

		return exposeParser(this);
	}

	public JavaScriptObject initializeFromTable(IStrategoTerm tableTerm) {
		initialize(tableTerm);
		return exposeParser(this);
	}

	private void initializeFromString(String tableContents) {
		// TODO: share table across multiple Parser instances
		IStrategoTerm tableTerm = af.parseFromString(tableContents);
		initialize(tableTerm);
	}

	private void initialize(IStrategoTerm tableTerm) {
		
		

		try {
			parseTable = new ParseTable(tableTerm, af);
		} catch (InvalidParseTableException e) {
			GWT.log("error", e);
			logToConsole("Could not load parsetable due to: " + e.getMessage());
			return;
		}
		//TermTreeFactory factory = new TermTreeFactory(af);
		
		
		{//Normal mode (to be used with str2js)
			treeBuilder = new TreeBuilder(new STRJSNativeTermFactory(), false);
			sglr = new SGLR(treeBuilder, parseTable);
			sglr.setUseStructureRecovery(true);
		}
		//Measure mode:
		/*
		{
			treeBuilder = new MemoryRecordingTreeBuilder(new Asfix2TreeBuilder());
			sglr = new SGLR(treeBuilder, parseTable);
		}
		*/
	}

	public boolean isReady() {
		return sglr != null;
	}

	public boolean isTableLoaded() {
		return tableLoaded;
	}

	public boolean loadFailed() {
		return tableLoaded && sglr == null;
	}

	private native JavaScriptObject exposeParser (Parser parser) /*-{
		var self = this;
		var parser = {};
		parser.parse = function (text) {
			return self.@org.spoofax.jssglr.client.services.Parser::parse(Ljava/lang/String;)(text);
		};
		parser.Tokenize = function (ast, lines)  {
			return self.@org.spoofax.jssglr.client.services.Parser::Tokenize(Ljava/lang/Object;I) (ast, lines);
		};		
		//parser.parseAndTokenize = function (lineCount, text) {
		//	return self.@org.spoofax.jssglr.client.services.Parser::parseAndTokenize(ILjava/lang/String;)(lineCount, text);
		//};
		parser.loadFailed = function() {
			return self.@org.spoofax.jssglr.client.services.Parser::loadFailed()();
		}
		parser.isTableLoaded = function() {
			return self.@org.spoofax.jssglr.client.services.Parser::isTableLoaded()();
		}
		parser.isReady = function() {
			return self.@org.spoofax.jssglr.client.services.Parser::isReady()();
		};
		parser.initTokenTable = function()
		{
			self.@org.spoofax.jssglr.client.services.Parser::initTokenTable()();
		}
		parser.addTokenToTable = function(token, type, text, value)
		{
			self.@org.spoofax.jssglr.client.services.Parser::storeErrorToken(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;) (token, type, text, value);
		}
		return parser;
	}-*/;
	
	public void initTokenTable()
	{
		_tokenTable = new HashMap<String, SemanticError>();
		//log("Initialized token table. Count=" + _tokenTable.size());
	}
	
	public void storeErrorToken(Object token, String type, String text, String value)
	{
		String key = getTokenCompareString((IToken) token);
		_tokenTable.put(key, new SemanticError(type, text, value ));
		//log("added token to table. Size=" + _tokenTable.size());
	}
	
	private String getTokenCompareString(IToken token)
	{
		String key = "TI."+token.getLine()+"."+token.getColumn(); 
		return key;
	}	
	
	

	private int findRightMostWithSameError(IToken token, String prefix) {
		String expectedError = token.getError();
		ITokenizer tokenizer = token.getTokenizer();
		int i = token.getIndex();
		for (int max = tokenizer.getTokenCount(); i + 1 < max; i++) {
			String error = tokenizer.getTokenAt(i + 1).getError();
			if (error != expectedError
					&& (error == null || prefix == null || !error.startsWith(prefix)))
				break;
		}
		return i;
	}

	private void reportErrorNearOffset(JsArray<JavaScriptObject> jserrors, ITokenizer tokenizer, int offset, String message) {
		IToken errorToken = tokenizer.getErrorTokenOrAdjunct(offset);
		reportErrorAtTokens(jserrors, errorToken, errorToken, message);
	}

	private void reportErrorAtTokens(JsArray<JavaScriptObject> jserrors, final IToken left, final IToken right, String message) {
		if (left.getStartOffset() > right.getEndOffset()) {
			reportErrorNearOffset(jserrors, left.getTokenizer(), left.getStartOffset(), message);
		} else {
			jserrors.push(createWarningToken(left.getLine() - 1, left.getColumn(), message, false));
		}
	}

	private void reportWarningAtTokens(JsArray<JavaScriptObject> jserrors, final IToken left, final IToken right, final String message) {
		jserrors.push(createWarningToken(left.getLine() - 1, left.getColumn(), message, true));
	}

	private static List<BadTokenException> getCollectedErrorsInRegion(SGLR parser, IToken left, IToken right, boolean alsoOutside) {
		List<BadTokenException> results = new ArrayList<BadTokenException>();
		int line = left.getLine();
		int endLine = right.getLine() + (alsoOutside ? RegionRecovery.NR_OF_LINES_TILL_SUCCESS : 0);
		for (BadTokenException e : parser.getCollectedErrors()) {
			if (e.getLineNumber() >= line && e.getLineNumber() <= endLine)
				results.add(e);
		}
		return results;
	}

	private static IToken findNextNonEmptyToken(IToken token) {
		ITokenizer tokenizer = token.getTokenizer();
		IToken result = null;
		for (int i = token.getIndex(), max = tokenizer.getTokenCount(); i < max; i++) {
			result = tokenizer.getTokenAt(i);
			if (result.getLength() != 0 && !Token.isWhiteSpace(result)) break;
		}
		return result;
	}

	private void reportBadToken(JsArray<JavaScriptObject> jserrors, ITokenizer tokenizer, BadTokenException exception) {
		String message;
		if (exception.isEOFToken() || tokenizer.getTokenCount() <= 1) {
			message = exception.getShortMessage();
		} else {
			IToken token = tokenizer.getTokenAtOffset(exception.getOffset());
			token = findNextNonEmptyToken(token);
			message = ITokenizer.ERROR_WATER_PREFIX + ": " + token.toString().trim();
		}
		reportErrorNearOffset(jserrors, tokenizer, exception.getOffset(), message);
	}


	private void reportSkippedRegion(JsArray<JavaScriptObject> jserrors, SGLR parser, IToken left, IToken right) {
		// Find a parse failure(s) in the given token range
		int line = left.getLine();
		int reportedLine = -1;
		for (BadTokenException e : getCollectedErrorsInRegion(parser, left, right, true)) {
			reportBadToken(jserrors, left.getTokenizer(), e);
			if (reportedLine == -1)
				reportedLine = e.getLineNumber();
		}

		if (reportedLine == -1) {
			// Report entire region
			reportErrorAtTokens(jserrors, left, right, ITokenizer.ERROR_SKIPPED_REGION);
		} else if (reportedLine - line >= LARGE_REGION_SIZE) {
			// Warn at start of region
			reportErrorAtTokens(jserrors, findLeftMostTokenOnSameLine(left),
					findRightMostTokenOnSameLine(left), LARGE_REGION_START);
		}
	}
	
	private native void log(String o) /*-{
		StrategoJS.trace("JSGLR PARSER LOG: " + o);
	}-*/;
	
	
	private native void debug(Object o) /*-{
		var x = o;
		//Parser Debugger
		debugger; 
	}-*/;
	
	private native IToken getTopLeftToken(Object o)  /*-{
		if (o.editortoken.tokenLeft !== null)
			return o.editortoken.tokenLeft;
		return o.getSubterm(0).editortoken.tokenLeft;
	}-*/;
	

	
	/*
	 * This method is created to couple ACE with the JSSGLR parser and Stratego-JS-Backend programs.
	 * Since The Stratego-JS-Backend requires a SJSB ast in order to perform the transformations a suitable AST (generated with STRJSNativeTermFactory) is applied to a required strategy.
	 * 
	 * The strategy which must be implemented should be called main, and take as parameter a list of two items. 
	 * 
	 * 
	Function returns a {tokens, errors}
	*/
	
	
		


	@SuppressWarnings("unchecked")
	public JavaScriptObject Tokenize(Object ast, int lines) {
//		int ttsize = _tokenTable.size();
		//debug(ttsize);
		
		
		final JsArray<JavaScriptObject>[] attrs = new JsArray[lines];
		for(int i = 0; i < lines; i++) {
			attrs[i] = (JsArray<JavaScriptObject>) JavaScriptObject.createArray();
		}
		//final ISimpleTerm o = parse(text);
		final ISimpleTerm o = (ISimpleTerm)ast; 
		
		JsArray<JavaScriptObject> jserrors = (JsArray<JavaScriptObject>) JavaScriptObject.createArray();
		if(o == null) {
			return makeParseResult(makeJsArray(attrs), jserrors);
		}
		final IToken t = getTopLeftToken(o);
		
		if(t == null) {
			return makeParseResult(makeJsArray(attrs), jserrors);
		}
		final ITokenizer tok = t.getTokenizer();
		
		//log("Tokencount: "  + tok.getTokenCount());
		for(int i = 0; i < tok.getTokenCount(); i++) {
			final IToken x = tok.getTokenAt(i);
			int line = x.getLine();// - 1;
			
			String tokentype = convertTokenType(x.getKind());
			String tokenKey = getTokenCompareString(x);
			//log("Looking for tokenkey B: " + tokenKey);
			if (_tokenTable.containsKey(tokenKey))
			{
				tokentype = _tokenTable.get(tokenKey).type;
				log("Marking error at: " + _tokenTable.get(tokenKey).text);
				
				//_tokenTable.get(tokenKey).text
				jserrors.push(createWarningToken(line, x.getColumn(), _tokenTable.get(tokenKey).text, false));
			}
			//debugToken(x);
			final int start = x.getColumn();
			final int end = x.getEndOffset() - x.getStartOffset() + start + 1;
			
			attrs[line].push(createBespinToken(x.toString(), tokentype, start, end, x.getLine()));
		}
		
		// https://svn.strategoxt.org/repos/StrategoXT/spoofax-imp/trunk/org.strategoxt.imp.runtime/src/org/strategoxt/imp/runtime/parser/ParseErrorHandler.java
		for(int i = 0; i < tok.getTokenCount(); i++) {
			final IToken x = tok.getTokenAt(i);
			String error = x.getError();

			if (error != null) {
				if (error == ITokenizer.ERROR_SKIPPED_REGION) {
					i = findRightMostWithSameError(x, null);
					reportSkippedRegion(jserrors, sglr, x, tok.getTokenAt(i));
				} else if (error.startsWith(ITokenizer.ERROR_WARNING_PREFIX)) {
					i = findRightMostWithSameError(x, null);
					reportWarningAtTokens(jserrors, x, tok.getTokenAt(i), error);
				} else if (error.startsWith(ITokenizer.ERROR_WATER_PREFIX)) {
					i = findRightMostWithSameError(x, ITokenizer.ERROR_WATER_PREFIX);
					reportErrorAtTokens(jserrors, x, tok.getTokenAt(i), error);
				} else {
					i = findRightMostWithSameError(x, null);
					// UNDONE: won't work for multi-token errors (as seen in SugarJ)
					reportErrorAtTokens(jserrors, x, tok.getTokenAt(i), error);
				}
			}
			
		}
		

		return makeParseResult(makeJsArray(attrs), jserrors);
	}	
	
	
	/*

	@SuppressWarnings("unchecked")
	public JavaScriptObject parseAndTokenize(int lines, String text) {
		final JsArray<JavaScriptObject>[] attrs = new JsArray[lines];
		for(int i = 0; i < lines; i++) {
			attrs[i] = (JsArray<JavaScriptObject>) JavaScriptObject.createArray();
		}
		final ISimpleTerm o = parse(text);
		//Now we have the ast;
		
		
		debug("W00t");
		
		
		JsArray<JavaScriptObject> jserrors = (JsArray<JavaScriptObject>) JavaScriptObject.createArray();
		if(o == null) {
			return makeParseResult(makeJsArray(attrs), jserrors);
		}
		//final IToken t = ImploderAttachment.get(o).getLeftToken();
		final IToken t = getTopLeftToken(o);
		
		if(t == null) {
			return makeParseResult(makeJsArray(attrs), jserrors);
		}
		final ITokenizer tok = t.getTokenizer();

		
		for(int i = 0; i < tok.getTokenCount(); i++) {
			final IToken x = tok.getTokenAt(i);
			int line = x.getLine() - 1;
			//debugToken(x);

			final int start = x.getColumn();
			final int end = x.getEndOffset() - x.getStartOffset() + start + 1;
			final String tokentype = convertTokenType(x.getKind());
			attrs[line].push(createBespinToken(x.toString(), tokentype, start, end, x.getLine()));
		}
		// https://svn.strategoxt.org/repos/StrategoXT/spoofax-imp/trunk/org.strategoxt.imp.runtime/src/org/strategoxt/imp/runtime/parser/ParseErrorHandler.java
		for(int i = 0; i < tok.getTokenCount(); i++) {
			final IToken x = tok.getTokenAt(i);
			final String error = x.getError();
			
			if (error != null) {
				if (error == ITokenizer.ERROR_SKIPPED_REGION) {
					i = findRightMostWithSameError(x, null);
					reportSkippedRegion(jserrors, sglr, x, tok.getTokenAt(i));
				} else if (error.startsWith(ITokenizer.ERROR_WARNING_PREFIX)) {
					i = findRightMostWithSameError(x, null);
					reportWarningAtTokens(jserrors, x, tok.getTokenAt(i), error);
				} else if (error.startsWith(ITokenizer.ERROR_WATER_PREFIX)) {
					i = findRightMostWithSameError(x, ITokenizer.ERROR_WATER_PREFIX);
					reportErrorAtTokens(jserrors, x, tok.getTokenAt(i), error);
				} else {
					i = findRightMostWithSameError(x, null);
					// UNDONE: won't work for multi-token errors (as seen in SugarJ)
					reportErrorAtTokens(jserrors, x, tok.getTokenAt(i), error);
				}
			}
		}
		

		return makeParseResult(makeJsArray(attrs), jserrors, o);
	}
	*/

	public native static void logToConsole(String message) /*-{
		$self.sender.emit("log", message);
	}-*/;

	public native static void parseTableLoaded() /*-{
		$self.sender.emit("loaded", "");
	}-*/;

	public native static JavaScriptObject createWarningToken(int row, int column, String text, boolean isWarning) /*-{
		return {
			row: row,
			column: column,
			text: text,
			type: isWarning ? "warning" : "error"
		};
	}-*/;

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

	private native static JavaScriptObject makeParseResult(
			JsArray<JavaScriptObject> tokens, JsArray<JavaScriptObject> errors) /*-{
		return {
			tokens: tokens,
			errors: errors
		};
	}-*/;

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
		case IToken.TK_NUMBER: return "constant";
		case IToken.TK_OPERATOR: return "keyword.operator";
		case IToken.TK_KEYWORD: return "keyword";
		case IToken.TK_STRING: return "string";
		case IToken.TK_IDENTIFIER: return "variable";
		case IToken.TK_ERROR_KEYWORD: return "invalid.illegal";
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

class SemanticError
{
	public final String type;
	public final String text;
	public final String value;
	
	public SemanticError(String pType, String pText, String pValue)
	{
		type = pType;
		text = pText;
		value = pValue;
	}
}
