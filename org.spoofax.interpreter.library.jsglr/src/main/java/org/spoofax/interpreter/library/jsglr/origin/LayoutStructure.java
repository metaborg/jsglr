package org.spoofax.interpreter.library.jsglr.origin;

import static mb.jsglr.shared.ImploderAttachment.getLeftToken;
import static mb.jsglr.shared.ImploderAttachment.getRightToken;
import static org.spoofax.terms.attachments.ParentAttachment.getParent;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.*;
import org.spoofax.terms.StrategoSubList;

import mb.jsglr.shared.IToken;
import mb.jsglr.shared.ITokens;
import mb.jsglr.shared.ImploderAttachment;
import mb.jsglr.shared.Token;

/**
 * Provides access to the layout structure (text fragments and offsets) surrounding a node
 * Offers support for text reconstruction algorithm
 * 
 * @author Maartje de Jonge
 */
public class LayoutStructure {
	
	private final ISimpleTerm node;
	private final ISimpleTerm listParent;
	private final ITokens tokens;
	
	//suffix data
	private IToken suffixStart; //possible empty (if node contains rightmost token)
	private IToken commentsAfterExclEnd; //possible empty (if node contains rightmost token)
	private IToken suffixSeparationExclEnd; //possible empty (if node contains rightmost token)
	
	//prefix data
	private IToken prefixEnd; //possible empty (if node contains leftmost token)
	private IToken commentsBeforeStart; //non-empty (first token if node contains leftmost token)
	private IToken prefixSeparationStart; //non-empty (first token if node contains leftmost token)

	public LayoutStructure(IStrategoTerm node) {
		this.node = ImploderAttachment.getImploderOrigin(node);
		listParent = getParentList(); //could be null
		assertImploderInfo();
		tokens = getLeftToken(node).getTokenizer();
		analyzeSuffix();
		analyzePrefix();
		//logAnalysisResults();
	}

	private void assertImploderInfo() {
		assert (node != null) : 
			"Error: Layout analysis can only be applied to nodes with associated origin term";
		if(listParent != null){
			assert (ImploderAttachment.hasImploderOrigin(listParent)) :
				"Unexpected Error: list-parent of imploder-origin must have imploder info";
			for (int i = 0; i < listParent.getSubtermCount(); i++) {
				assert (ImploderAttachment.hasImploderOrigin(listParent.getSubterm(i))) :
					"Unexpected Error: subterms of origin-list must have imploder info";
			}
		}
	}

	/**
	 * Comments preceding and associated with the node
	 */
	public String getCommentsBefore() {
		String layoutPrefix = getLayoutPrefix();
		return trimWsPreservingLastNewline(layoutPrefix);
	}

	/**
	 * Comments succeeding and associated with the node 
	 * whereby the suffix separator (if any) is changed by spaces
	 */
	public String getCommentsAfter() {
		String layoutSuffix = getLayoutSuffix();
		return trimWsPreservingLastNewline(layoutSuffix);
	}

	/**
	 * Text fragment preceeding the node that starts from comment(s) before
	 */
	public String getLayoutPrefix() {
		return layoutFragmentWithOutSeparator(this.commentsBeforeStart, this.prefixEnd);
		//assert: Separator not in fragment
	}

	/**
	 * Text fragment succeeding the node that ends with comment(s) after
	 * whereby the suffix separator (if any) is changed by spaces
	 */
	public String getLayoutSuffix() {
		return layoutFragmentWithOutSeparator(this.suffixStart, this.commentsAfterExclEnd.getTokenBefore());
	}

	/**
	 * Text fragment between comment(s) before start offset and comments after end offset,
	 * whereby the suffix separator (if any) is changed by spaces
	 */
	public String getTextWithLayout() {
		return getTokenString(commentsBeforeStart, getRightToken(node))+getLayoutSuffix();
	}

	/**
	 * Indentation of the start line of the node, consist of spaces and tabs.
	 */
	public String getIndentation() {
		String indent = getIndentString();
		assert(indent.replaceAll("[ \t]", "").equals(""));
		return indent;
	}

	/**
	 * Separation between first and second (TODO: average) list element:
	 * - consists of newlines, spaces, tabs and separator (if any)
	 * - null in case (parent) element is not an origin list, or is a list with 0 or 1 element
	 */
	public String getSeparation() {
		return getSeparationString();
	}

	/**
	 * For elements in the middle of a list: 
	 * - start offset of preceding comment (if any), or node (otherwise)
	 * - equal to "insert before offset/layout prefix/text with layout" start offset
	 * For last list element: 
	 * - min. start offset of node/comm before/prefix-separator + preceding ws
	 */
	public int getDeletionStartOffset() {
		if(isLastListElement()){
			assert(prefixSeparationStart != null);
			return prefixSeparationStart.getStartOffset();
		}
		return getInsertBeforeOffset();
	}

	/**
	 * For elements in the middle of a list: 
	 * - max. end offset of node/comment after/suffix-separator + succeeding ws
	 * For last list element: 
	 * - end offset comment after (or node)
	 * - equal to insert-at-end offset (minus \n for line comments)
	 * - do not delete \n of a line comment (this is used as vertical layout)
	 */
	public int getDeletionEndOffset() {
		if(isLastListElement()){
			int deletionEndOffset = getInsertAtEndOffset()-1;
			return keepNewlineOfLineComment(deletionEndOffset);
		}
		if(suffixSeparationExclEnd != null){
			int deletionEndOffset = suffixSeparationExclEnd.getStartOffset()-1;
			return deletionEndOffset;
		}
		return getRightToken(node).getEndOffset();
	}

	/**
	 * Start offset of preceding comment (if any) or node
	 */
	public int getInsertBeforeOffset() {
		int offset = this.commentsBeforeStart.getStartOffset();
		String input = tokens.getInput();
		while (offset < input.length() && (input.charAt(offset) == ' ' || input.charAt(offset) == '\t')) {
			offset++;
		}
		return offset;
	}

	/**
	 * End offset+1 of succeeding comment (if any) or node
	 */
	public int getInsertAtEndOffset() {
		IToken token = this.commentsAfterExclEnd;
		
		if(token != null) {
			if (node.isList() && node.getSubtermCount() == 0) {
				// insert a first list element at the first rather than the last valid offset
				IToken previousToken = token.getTokenBefore();
				while (isLayout(previousToken)) {
					token = previousToken;
				}
			}
			
			return token.getStartOffset();
		}

		return getRightToken(node).getEndOffset()+1;
	}	
	
	/**
	 * Layout fragment with separator replaced by spaces
	 * @param loStart token start layout fragment (inclusive)
	 * @param loEnd token end layout fragment (inclusive)
	 */
	private String layoutFragmentWithOutSeparator(IToken loStart, IToken loEnd){
		String result = "";
		for (IToken token = loStart; token != null && token.getStartOffset() <= loEnd.getStartOffset(); token = token.getTokenAfter()) {
			if(isLayout(token))
				result += getTokenString(token);
			else { //separator expected
				assert(isSeparatorToken(token));
				result += createSpaces(token.getLength());
			}
		}
		return result;
	}

	private String createSpaces(int length) {
		String spaces="";
		for (int i = 0; i < length; i++) {
			spaces +=" ";
		}
		return spaces;
	}

	private int keepNewlineOfLineComment(int deletionEndOffset) {
		while (Character.isWhitespace(getCharAt(deletionEndOffset))){ //newline at the end of line comment
			deletionEndOffset --;
		}
		return deletionEndOffset;
	}

	/**
	 * a) Always after separator, always after ast characters before node
	 * b) No empty line between end of comment string and start of node
	 * c) if comment is on the same line, then there is no preceding sibling on the same line except when (d)
	 * d) "separator" - "comment - node" on the same line, then comment associated to node
	 * e) if comment starts on same line as preceding ast token, then comment NOT associated to node 
	 * f) grouping???
	 */
	private void analyzePrefix() {
		prefixEnd = getLeftToken(node).getTokenBefore(); //possible empty token
		final IToken prefixStart = getPrefixStart(prefixEnd); //possible empty token
		commentsBeforeStart = getLeftToken(node);
		IToken token = commentsBeforeStart.getTokenBefore();
		int lastNonEmptyLine = getLeftToken(node).getLine();
		int preceedingAstLine = -1;
		IToken previousToken = prefixStart != null ? prefixStart.getTokenBefore() : null;
		if(previousToken != null){
			preceedingAstLine=previousToken.getEndLine();
		}
		final boolean sameLineSiblings = preceedingSibEndLine()  == getLeftToken(node).getLine();
		while(token != null && prefixStart != null && token.getStartOffset() >= prefixStart.getStartOffset()){
			if(isComment(token)){
				int commentEndLine = token.getEndLine();
				int commentStartLine = token.getLine();
				if(lastNonEmptyLine - commentEndLine > 1)
					break; //line between (b)
				else if ((preceedingAstLine == commentStartLine) && !sameLineSiblings) {
					break; //e comment associated to preceding astnode
				}
				else {
					commentsBeforeStart = token;
					lastNonEmptyLine = token.getLine();
				}
			}
			if(isSeparatorToken(token.getTokenBefore())){
				break; //a) only comments after separator are included
			}
			if(token == prefixStart && sameLineSiblings){
				commentsBeforeStart = getLeftToken(node); //(c,d) comments fall between siblings
				break;
			}
			token = token.getTokenBefore();
		}
		final IToken prefixSeparator = getSeparator(prefixStart, prefixEnd);//null in case not exists
		
		prefixSeparationStart = prefixSeparator == null ? commentsBeforeStart : prefixSeparator;
		while (isWhitespace(prefixSeparationStart.getTokenBefore())) {
			prefixSeparationStart = prefixSeparationStart.getTokenBefore(); //start of next node fragment
		}
	}

	/**
	 * a) Always on the same line as node
	 * b) If succeeding sibling on the same line, then: comments after separator are excluded
	 * c) If succeeding sibling on the same line, then: comments only included if they are in front of separator 
	 * d) If no succeeding sibling on the same line, then comment attaches to node (even when separator between node and comment) 
	 */
	private void analyzeSuffix() {
		suffixStart = getRightToken(node).getTokenAfter(); //possible null
		final IToken suffixEnd = getSuffixEnd(suffixStart); //possible null
		IToken nextToken = suffixEnd.getTokenAfter();
		final boolean sameLineSiblings = 
			tokensOnSameLine(suffixStart.getTokenBefore(), nextToken) &&
			!isLastListElement();
		commentsAfterExclEnd = suffixStart;
		IToken token = commentsAfterExclEnd;
		//sets comment after end index
		while(token != null && nextToken != null && token.getStartOffset() <= nextToken.getStartOffset()){
			if(token.getLine() != getRightToken(node).getEndLine()){
				break; //a) comment not on same line
			}
			if(sameLineSiblings && token == nextToken){
				//(c) same line, no separator: 
				//comments is not associated to preceeding or succeeding sibling
				commentsAfterExclEnd = suffixStart;
				break;
			}
			if(sameLineSiblings && isSeparatorToken(token)){
				break; //b) exclude comments after separator between siblings on same line
			}
			if(isComment(token)){
				commentsAfterExclEnd = token.getTokenAfter(); //d) consume comments
			}
			token = token.getTokenAfter();
		}
		final IToken suffixSeparator = getSeparator(suffixStart, suffixEnd);//null in case not exists
		suffixSeparationExclEnd = (suffixSeparator == null || commentsAfterExclEnd.getStartOffset() > suffixSeparator.getTokenAfter().getStartOffset()) ? commentsAfterExclEnd : suffixSeparator.getTokenAfter();
		while (isWhitespace(suffixSeparationExclEnd)) {
			suffixSeparationExclEnd = suffixSeparationExclEnd.getTokenAfter(); //start of next node fragment
		}
	}

	private boolean isFirstListElement() {
		if(listParent == null)
			return false;
		if(node instanceof StrategoSubList){
			return ((StrategoSubList)node).getIndexStart() == 0;
		}
		return listParent.getSubterm(0) == node;
	}
	
	private int preceedingSibEndLine() {
		if(isFirstListElement())
			return -1;
		if(listParent == null)
			return -1;
		int indexPreceedingSib = -1;
		if(node instanceof StrategoSubList){
			indexPreceedingSib = ((StrategoSubList)node).getIndexStart() - 1;
			assert(listParent == ((StrategoSubList)node).getCompleteList());
		}
		else{
			for (int i = 1; i < listParent.getSubtermCount(); i++) {
				if(listParent.getSubterm(i)==node)
					indexPreceedingSib = i-1;
			}
		}
		if(indexPreceedingSib == -1)
			return -1;
		assert(indexPreceedingSib >= 0 && indexPreceedingSib < listParent.getSubtermCount());
		ISimpleTerm preceedingNode = listParent.getSubterm(indexPreceedingSib);
		if(!ImploderAttachment.hasImploderOrigin(preceedingNode))
			return -1;
		return getRightToken(preceedingNode).getEndLine();
	}

	private boolean isLastListElement() {
		if(listParent == null)
			return false;
		if(node instanceof StrategoSubList){
			return ((StrategoSubList)node).getIndexEnd() == listParent.getSubtermCount()-1;
		}
		return listParent.getSubterm(listParent.getSubtermCount()-1) == node;
	}
	
	
	private boolean tokensOnSameLine(IToken i, IToken j) {
		if(i != null && j != null){
			return i.getLine() == j.getLine();
		}
		return false;
	}

	private char getCharAt(int offset) {
		assert(offset < tokens.getInput().length());
		return tokens.getInput().charAt(offset);
	}

	private IToken getSuffixEnd(IToken suffixStart) {
		IToken suffixEnd = suffixStart;
		while (suffixEnd != null && notAssociatedToAstNode(suffixEnd.getTokenAfter())) {
			suffixEnd = suffixEnd.getTokenAfter();
		}
		return suffixEnd;
	}

	private IToken getPrefixStart(IToken prefixEndToken) {
		IToken prefixStart = prefixEndToken;
		while (prefixStart != null && notAssociatedToAstNode(prefixStart.getTokenBefore())) {
			prefixStart = prefixStart.getTokenBefore();
		}
		return prefixStart;
	}

	private IToken getSeparator(IToken suffixStart, IToken suffixEnd) {
		for (IToken token = suffixEnd; token != null && token.getStartOffset() >= suffixStart.getStartOffset(); token = token.getTokenBefore()) {
			if(isSeparatorToken(token))
				return token;
		}
		return null;
	}


	private boolean isSeparatorToken(IToken token) {
		return isAssociatedToListParent(token) && !isLayout(token);
	}

	private boolean notAssociatedToAstNode(IToken token){
		return isLayout(token) || isAssociatedToListParent(token);
	}
	
	private boolean isWhitespace(IToken token) {
		return isLayout(token) && !isComment(token);
	}

	private boolean isComment(IToken token) {
		return
			isLayout(token) &&
			!Token.isWhiteSpace(token);
	}

	private boolean isLayout(IToken token) {
		return
			token != null &&
			token.getKind() == IToken.Kind.TK_LAYOUT;
	}

	private boolean isAssociatedToListParent(IToken token) {
		return
			token != null &&
			listParent != null && 
			token.getAstNode() == listParent;
	}


	private IStrategoTerm getParentList() {
		if(node instanceof StrategoSubList){
			return ((StrategoSubList)node).getCompleteList();
		}
		if (node.isList() && node.getSubtermCount() > 0)
			return (IStrategoTerm) node;
		if (getParent(node) == null)
			return null;
		return  getParent(node).isList()? getParent(node) : null;
	}
	
	private String trimWsPreservingLastNewline(String layoutSuffix) {
		String commentEnd = "";
		if(layoutSuffix.endsWith("\n"))
			commentEnd = "\n";
		return layoutSuffix.trim() + commentEnd; //preserve line comment end
	}

	private String getIndentString() {
		int offset = getLeftToken(node).getStartOffset();
		String input = tokens.getInput();
		String indentation = "";
		for (int i = offset-1; i >= 0; i--) {
			char character = input.charAt(i);
			if(character == '\n')
				return indentation;
			else if(character == ' ' || character == '\t')
				indentation = character + indentation;
			else
				indentation = ""; //node does not start on line
		}
		return indentation;
	}
	
	public String getSeparationString() { //TODO: find average separation?
		if(listParent != null && listParent.getSubtermCount() > 1){
			IToken startToken = getRightToken(listParent.getSubterm(0));
			IToken endToken = getLeftToken(listParent.getSubterm(1));
			return getSeparationString(startToken, endToken);
		}
		return null;
	}
	
	private String getSeparationString(IToken tokenStart, IToken tokenEnd) {
		String separation="";
		String layoutText="";
		boolean commentSeen = false;
		boolean commentLine = false;
		IToken token;
		for (token = tokenStart.getTokenAfter(); token != tokenEnd; token = token.getTokenAfter()) {
			String tokenText = getTokenString(token);
			if(!isComment(token)){
				if(!commentSeen)
					separation += tokenText;
				else
					layoutText += tokenText;
			}
			else { 
				commentSeen = true;
				layoutText = ""; //layout between comments is not part of separation
				if(tokenText.endsWith("\n"))
					layoutText = "\n";
				if(token.getLine() != tokenStart.getEndLine() && token.getEndLine() != tokenEnd.getLine())
					commentLine = true;
			}
		}
		if(commentLine){
			separation = separation.replaceFirst("\n[ \t]*", "");
		}
		return separation + layoutText;
	}

	private String getTokenString(IToken token){
		assert(token != null);
		int startOffset = token.getStartOffset();
		int endOffset = token.getEndOffset();
		return tokens.toString(startOffset, endOffset);
	}

	private String getTokenString(IToken tokenStart, IToken tokenEnd){
		assert(tokenStart != null);
		assert(tokenEnd != null);
		int startOffset = tokenStart.getStartOffset();
		int endOffset = tokenEnd.getEndOffset();
		return tokens.toString(startOffset, endOffset); 
	}
	
	/*
	private void logAnalysisResults() {
		System.out.println("comm before:" + this.getCommentsBefore() + "#");
		System.out.println("comm after:" + this.getCommentsAfter() + "#");
		System.out.println("lo prefix:" + this.getLayoutPrefix() + "#");
		System.out.println("lo suffix:" + this.getLayoutSuffix() + "#");
		System.out.println("full text:" + this.getTextWithLayout() + "#");

		System.out.println("indent:" + this.getIndentation() + "#");
		System.out.println("separation:" + this.getSeparation() + "#");

		System.out.println("del-start:" + this.getDeletionStartOffset());
		System.out.println("del-end:" + this.getDeletionEndOffset());
		System.out.println(tokens.toString(getDeletionStartOffset(), getDeletionEndOffset())+"#");
		System.out.println("insert-before:" + this.getInsertBeforeOffset());
		System.out.println("insert-at-end:" + this.getInsertAtEndOffset());
		System.out.println(tokens.toString(getInsertBeforeOffset(), getInsertAtEndOffset())+"#");
		System.out.println(tokens.toString(5,5) + "#--------------------");
	}
	*/

}

