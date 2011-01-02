package org.spoofax.jsglr.client.imploder;

import static java.lang.Math.max;
import static org.spoofax.jsglr.client.imploder.IToken.TK_EOF;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_EOF_UNEXPECTED;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.Amb;
import org.spoofax.jsglr.client.ParseNode;
import org.spoofax.jsglr.client.ParseProductionNode;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.RecoveryConnector;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
@SuppressWarnings({"rawtypes", "unchecked"}) // FIXME: class-wide {"rawtypes", "unchecked"}?!
public class TreeBuilder extends TopdownTreeBuilder {
	
	public static final char SKIPPED_CHAR = (char) -1; // TODO: sync with ParseErorHandler
	
	public static final char UNEXPECTED_EOF_CHAR = (char) -2; // TODO: sync with ParseErorHandler
	
	private static final int NONE = -1;

	private static final int EXPECTED_NODE_CHILDREN = 5;
	
	private static final String LIST_CONSTRUCTOR = new String("[]");
	
	private static final String TUPLE_CONSTRUCTOR = new String("");
	
	private final boolean disableTokens;
	
	private ParseTable table;
	
	private ITokenizer tokenizer;
	
	private ITreeFactory factory;
	
	private boolean initializeFactories;
	
	private ProductionAttributeReader prodReader;

	private ITermFactory termFactory;
	
	private LabelInfo[] labels;
	
	private int labelStart;
	
	/** Character offset for the current implosion. */ 
	private int offset;
	
	private int nonMatchingOffset = NONE;
	
	private char nonMatchingChar, nonMatchingCharExpected;
	
	private boolean inLexicalContext;
	
	public TreeBuilder() {
		this(false);
	}
	
	public TreeBuilder(boolean disableTokens) {
		this.disableTokens = disableTokens;
		this.initializeFactories = true;
	}
	
	public TreeBuilder(ITreeFactory treeFactory) {
		this(treeFactory, false);
	}
	
	public TreeBuilder(ITreeFactory treeFactory, boolean disableTokens) {
		this.factory = treeFactory;
		this.disableTokens = disableTokens;
		treeFactory.setEnableTokens(!disableTokens);
	}

	public void initializeTable(ParseTable table, int productionCount, int labelStart, int labelCount) {
		this.table = table;
		this.termFactory = table.getFactory();
		if (initializeFactories) {
			factory = new TermTreeFactory(termFactory);
		}
		assert !(factory instanceof TermTreeFactory) || ((TermTreeFactory) factory).getOriginalTermFactory() == table.getFactory(); 
		assert !(tokenizer instanceof Tokenizer) || ((Tokenizer) tokenizer).getKeywordRecognizer() == table.getKeywordRecognizer(); 
		this.prodReader = new ProductionAttributeReader(termFactory);
		this.labels = new LabelInfo[labelCount - labelStart];
		this.labelStart = labelStart;
	}

	public void initializeLabel(int labelNumber, IStrategoAppl parseTreeProduction) {
		labels[labelNumber - labelStart] = new LabelInfo(prodReader, parseTreeProduction);
	}
	
	public void initializeInput(String filename, String input) {
		assert offset == 0;
		tokenizer = disableTokens
			? new DummyTokenizer(filename, input)
			: new Tokenizer(table.getKeywordRecognizer(), filename, input);
	}
	
	public ITokenizer getTokenizer() {
		return tokenizer;
	}

	public ITreeFactory getFactory() {
		if (factory == null && initializeFactories)
			throw new IllegalStateException("Not initialized yet");
		return factory;
	}
	
	@Override
	@Deprecated
	public Object buildTree(AbstractParseNode node) {
		return tryBuildAutoConcatListNode(super.buildTree(node));
	}
	
	@Override
	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		try {
			Object tree = tryBuildAutoConcatListNode(subtree);
			tree = recreateWithAllTokens(tree);
			tokenizer.makeToken(tokenizer.getStartOffset() - 1, TK_EOF, true);
			return factory.createTop(tree, tokenizer.getFilename(), ambiguityCount);
		} finally {
			offset = 0;
			inLexicalContext = false;
		}
	}

	/**
	 * Recreates a tree node, changing its begin and end token
	 * to the begin and end token of the entire token stream.
	 */
	private Object recreateWithAllTokens(Object tree) {
		List<Object> children = new ArrayList<Object>();
		for (Object child : factory.getChildren(tree))
			children.add(child);
		tree = factory.recreateNode(tree, tokenizer.getTokenAt(0),
				tokenizer.currentToken(), children);
		return tree;
	}
	
	/**
	 * Given a {@link ParseNode}, builds a tree node using the
	 * {@link ITreeFactory}, or an intermediate {@link AutoConcatList}
	 * object.
	 */
	@Override
	public Object buildTreeNode(ParseNode node) {
		LabelInfo label = labels[node.getLabel() - labelStart];
		IToken prevToken = tokenizer.currentToken();
		int lastOffset = offset;
		AbstractParseNode[] subnodes = node.getChildren();
		boolean isList = label.isList();
		boolean lexicalStart = false;
		
		if (!inLexicalContext) {
			if (label.isNonContextFree()) {
				inLexicalContext = lexicalStart = true;
			} else if (subnodes.length > 0 && subnodes[0] instanceof ParseProductionNode
					&& label.isSortProduction()
					&& label.getLHS().getSubtermCount() == 1) {
				return createIntTerminal(label, subnodes);
			}
		}
		
		List<Object> children = null;
		if (!inLexicalContext) {
			if (isList) {
				children = new AutoConcatList<Object>(label.getSort());
			} else {
				children = new ArrayList<Object>(max(EXPECTED_NODE_CHILDREN, subnodes.length));
			}
		}

		// Recurse
		for (AbstractParseNode subnode : subnodes) {
			if (inLexicalContext && subnode.isParseProductionChain()) {
				chainToTreeTopdown(subnode);
			} else {
				// TODO: Optimize stack - inline toTreeTopdown case selection?
				Object child = subnode.toTreeTopdown(this);
				if (child != null) children.add(isList ? child : tryBuildAutoConcatListNode(child));
			}
		}
		
		if (!inLexicalContext && isList && children.isEmpty()) {
			IToken token = tokenizer.makeToken(tokenizer.getStartOffset() - 1, IToken.TK_LAYOUT, true);
			((AutoConcatList) children).setEmptyListToken(token);
		}
		
		if (lexicalStart) {
			return tryCreateStringTerminal(label);
		} else if (inLexicalContext) {
			tokenizer.makeLayoutToken(offset - 1, lastOffset - 1, label);
			return null; // don't create tokens inside lexical context; just create one big token at the top
		} else if (isList) {
			return children;
		} else {
			return createNodeOrInjection(label, prevToken, children);
		}
	}

	/**
	 * Efficiently consume lexical chars in parse production chains.
	 * @see AbstractParseNode#isParseProductionChain()
	 */
	private void chainToTreeTopdown(AbstractParseNode node) {
		assert node.isParseProductionChain();
		while (node instanceof ParseNode) {
			AbstractParseNode[] kids = ((ParseNode) node).getChildren();
			if (kids.length == 2) {
				buildTreeProduction((ParseProductionNode) kids[0]);
				node = kids[1];
			} else if (kids.length == 1) {
				node = kids[0];
			} else {
				throw new IllegalStateException("Unexpected node in parse production chain: " + node);
			}
		}
		buildTreeProduction((ParseProductionNode) node);
	}

	@Override
	public Object buildTreeAmb(Amb a) {
		final int oldOffset = offset;
		final int oldBeginOffset = tokenizer.getStartOffset();
		final boolean oldLexicalContext = inLexicalContext;
		final AbstractParseNode[] subnodes = a.getAlternatives();
		final ArrayList<Object> children =
			new ArrayList<Object>(max(EXPECTED_NODE_CHILDREN, subnodes.length));
		tokenizer.setAmbiguous(true);

		// Recurse
		for (AbstractParseNode subnode : subnodes) {
			// Restore lexical state for each branch
			offset = oldOffset;
			tokenizer.setStartOffset(oldBeginOffset);
			inLexicalContext = oldLexicalContext;
			
			Object child = subnode.toTreeTopdown(this);
			if (child != null) children.add(child);
		}
		return factory.createAmb(children);
	}

	private Object tryBuildAutoConcatListNode(Object node) {
		if (node instanceof AutoConcatList) {
			return buildAutoConcatListNode((AutoConcatList) node);
		} else {
			return node;
		}
	}
	
	/**
	 * Converts an {@link AutoConcatList} intermediate list
	 * representation to a proper tree node using the
	 * {@link ITreeFactory}.
	 */
	public Object buildAutoConcatListNode(AutoConcatList list) {
		IToken left = list.isEmpty() ? list.getEmptyListToken() : factory.getLeftToken(list.get(0));
		IToken right = list.isEmpty() ? list.getEmptyListToken() : factory.getRightToken(list.get(list.size() - 1));
		return factory.createList(list.getSort(), left, right, list);
	}

	@Override
	public Object buildTreeProduction(ParseProductionNode node) {
		int character = node.prod;
		consumeLexicalChar(character);
		return null;
	}


	private Object tryCreateStringTerminal(LabelInfo label) {
		inLexicalContext = false;
		String sort = label.getSort();
		IToken token = tokenizer.makeToken(offset - 1, label, sort != null);
		
		if (sort == null) return null;
		
		// Debug.log("Creating node ", sort, " from ", SGLRTokenizer.dumpToString(token));
		
		Object result = factory.createStringTerminal(sort, getPaddedLexicalValue(label, token), token);
		String constructor = label.getMetaVarConstructor();
		if (constructor != null) {
			ArrayList<Object> children = new ArrayList<Object>(1);
			children.add(result);
			result = factory.createNonTerminal(sort, constructor, token, token, children);
		}
		return result;
	}
	
	private Object createIntTerminal(LabelInfo label, AbstractParseNode[] contents) {
		IToken token = tokenizer.makeToken(offset - 1, label, true);
		int value = contents.length == 1 && contents[0] instanceof ParseProductionNode
			? ((ParseProductionNode) contents[0]).prod : -1;
		assert value != -1;
		return factory.createIntTerminal(label.getSort(), token, value);
	}

	private Object createNodeOrInjection(LabelInfo label, IToken prevToken, List<Object> children) {
		
		String constructor = label.getConstructor();
		
		if (label.isList()) {
			throw new IllegalStateException("Illegal state: now handled by tryCreateAutoConcatListNode()");
			// return createNode(label, LIST_CONSTRUCTOR, prevToken, children);
		} else if (constructor != null) {
			// UNDONE: tokenizer.makeToken(offset, label); // TODO: why makeToken here??
			return createNode(label, constructor, prevToken, children);
		} else if (label.getAstAttribute() != null) {
			return createAstNonTerminal(label, prevToken, children);
		} else if (label.isOptional()) {
			// TODO: Spoofax/295: JSGLR does not output correct AST for optional literals
			if (children.size() == 0) {
				return createNode(label, "None", prevToken, children);
			} else {
				assert children.size() == 1;
				return createNode(label, "Some", prevToken, children);
			}
		} else if (children.size() == 1) {
			// Injection
			// TODO: efficiently store injection sort for use by content completion?
			//       would be needed to distinguish FoldingRules and Sorts in "folding" sections...
			//       maybe only if the content proposer demands it?
			// TODO: also, avoid semantics for deprecated?
			return factory.createInjection(label.getSort(), children);
		} else {
			// Constructor-less application (tuple)
			return createNode(label, TUPLE_CONSTRUCTOR, prevToken, children);
		}
	}

	/**
	 * Create a context-free tree node.
	 * 
	 * @param constructor
	 *          The constructor to use, or {@link #LIST_CONSTRUCTOR} to construct a list,
	 *          or {@link #TUPLE_CONSTRUCTOR} to construct a tuple.
	 */
	private Object createNode(LabelInfo label, String constructor, IToken prevToken,
			List<Object> children) {
		
		IToken left = getStartToken(prevToken);
		// IToken right = getEndToken(left, tokenizer.currentToken());
		IToken right = tokenizer.currentToken();
		
		if (constructor == LIST_CONSTRUCTOR) {
			return factory.createList(label.getSort(), left, right, children);
		} else if (constructor == TUPLE_CONSTRUCTOR) {
			return factory.createTuple(label.getSort(), left, right, children);
		} else if (constructor == null && children.size() == 1 && factory.tryGetStringValue(children.get(0)) != null) {
			// Child node was a <string> node (rare case); unpack it and create a new terminal
			assert left == right;
			return factory.createStringTerminal(label.getSort(), getPaddedLexicalValue(label, left), left);
		} else {
			return factory.createNonTerminal(label.getSort(), constructor, left, right, children);
		}
	}

	/** Implode a context-free node with an {ast} annotation. */
	private Object createAstNonTerminal(LabelInfo label, IToken prevToken, List<Object> children) {
		IToken left = getStartToken(prevToken);
		// IToken right = getEndToken(left, tokenizer.currentToken());
		IToken right = tokenizer.currentToken();
		AstAnnoImploder imploder = new AstAnnoImploder<Object>(factory, termFactory, children, left, right);
		return imploder.implode(label.getAstAttribute(), label.getSort());
	}
	
	/**
	 * Gets the padded lexical value for {indentpadding} lexicals, or returns null.
	 */
	private String getPaddedLexicalValue(LabelInfo label, IToken startToken) {
		if (label.isIndentPaddingLexical()) {
			String input = tokenizer.getInput();
			int lineStart = startToken.getStartOffset() - 1;
			if (lineStart < 0) return null;
			while (lineStart >= 0) {
				char c = input.charAt(lineStart--);
				if (c == '\n' || c == '\r') {
					lineStart++;
					break;
				}
			}
			StringBuilder result = new StringBuilder();
			result.append(input, lineStart, startToken.getStartOffset() - lineStart - 1);
			for (int i = 0; i < result.length(); i++) {
				char c = result.charAt(i);
				if (c != ' ' && c != '\t') result.setCharAt(i, ' ');
			}
			result.append(startToken.toString());
			return result.toString();
		} else {
			return startToken.toString(); // lazily load token string value
		}
	}
	
	/** Get the token after the previous node's ending token, or null if N/A. */
	private IToken getStartToken(IToken prevToken) {
		if (prevToken == null) {
			return tokenizer.getTokenCount() == 0
				? null
			    : tokenizer.getTokenAt(0);
		} else {
			int index = prevToken.getIndex();
			
			if (tokenizer.getTokenCount() - index <= 1) {
				// Create new empty token
				// HACK: Assume TK_LAYOUT kind for empty tokens in AST nodes
				return tokenizer.makeToken(offset - 1, IToken.TK_LAYOUT, true);
			} else {
				return tokenizer.getTokenAt(index + 1); 
			}
		}
	}
	
	/* Get the last no-layout token for an AST node.
	private IToken getEndToken(IToken currentToken, IToken lastToken) {
		if (lastToken.getEndOffset() == tokenizer.getInput().length() - 1)
			return lastToken;

		int begin = currentToken.getIndex();
		
		for (int i = lastToken.getIndex(); i > begin; i--) {
			lastToken = tokenizer.getTokenAt(i);
			if (lastToken.getKind() != IToken.TK_LAYOUT
					|| lastToken.getStartOffset() == lastToken.getEndOffset()-1)
				break;
		}
		
		return lastToken;
	}
	*/
	
	/** Consume a character of a lexical terminal. */
	protected final void consumeLexicalChar(int character) {
		String input = tokenizer.getInput();
		if (offset >= input.length()) {
			markUnexpectedEOF(character);
		} else {
			char parsedChar = (char) character;
			char inputChar = input.charAt(offset);
			
			if (parsedChar != inputChar) {
				if (RecoveryConnector.isLayoutCharacter(parsedChar)) {
					tokenizer.makeErrorToken(offset);
					offset++;
				} else {
					// UNDONE: Strict lexical stream checking
					// throw new IllegalStateException("Character from asfix stream (" + parsedChar
					//	 	+ ") must be in lex stream (" + inputChar + ")");
				    // instead, we allow the non-matching character for now, and hope
				    // we can pick up the right track later
					// TODO: better way to report skipped fragments in the parser
					//       this isn't 100% reliable
					if (nonMatchingOffset == NONE) {
						nonMatchingOffset = offset;
						nonMatchingChar = parsedChar;
						nonMatchingCharExpected = inputChar;
					}
				}
			} else {
				offset++;
			}
		}
	}

	private void markUnexpectedEOF(int character) {
		String input = tokenizer.getInput();
		assert nonMatchingOffset == NONE :
				"Character in parse tree after end of input stream: " + (char) character
				+ " - may be caused by unexpected character in parse tree at position "
				+ nonMatchingChar 	+ ": " + nonMatchingChar + " instead of "
				+ nonMatchingCharExpected;
		// UNDONE: Strict lexical stream checking
		// throw new ImploderException("Character in parse tree after end of input stream: " + (char) character.getInt());
		// a forced reduction may have added some extra characters to the tree;
		if (tokenizer.currentToken().getKind() != TK_ERROR_EOF_UNEXPECTED) {
			if (tokenizer.getStartOffset() >= input.length())
				tokenizer.setStartOffset(max(input.length() - 1, 0));
			tokenizer.makeToken(input.length() - 1, TK_ERROR_EOF_UNEXPECTED, true);
		}
	}

}
