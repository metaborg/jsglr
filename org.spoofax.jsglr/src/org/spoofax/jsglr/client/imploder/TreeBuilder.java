package org.spoofax.jsglr.client.imploder;


import static java.lang.Math.max;
import static jsglr.shared.IToken.Kind.TK_EOF;
import static jsglr.shared.IToken.Kind.TK_ERROR_EOF_UNEXPECTED;
import static jsglr.shared.IToken.Kind.TK_UNKNOWN;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.CycleParseNode;
import org.spoofax.jsglr.client.ParseNode;
import org.spoofax.jsglr.client.ParseProductionNode;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.RecoveryConnector;
import org.spoofax.terms.util.PushbackStringIterator;

import jsglr.shared.AbstractTokenizer;
import jsglr.shared.IToken;
import jsglr.shared.ITokenizer;
import jsglr.shared.LabelInfo;
import jsglr.shared.NullTokenizer;
import jsglr.shared.ProductionAttributeReader;

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

	private AbstractTokenizer tokenizer;

	private String input;

	private PushbackStringIterator stringIterator;

	private ITreeFactory factory;

	private boolean initializeFactories;

	private ProductionAttributeReader prodReader;

	private ITermFactory termFactory;

	private LabelInfo[] labels;

	public LabelInfo[] getLabels() {
        return labels;
    }

    private int labelStart;

	public int getLabelStart() {
        return labelStart;
    }

    /** Character offset for the current implosion. */
	private int offset;

	private int nonMatchingOffset = NONE;

	private int nonMatchingChar, nonMatchingCharExpected;

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
		factory.setEnableTokens(!disableTokens);
	}

	public void initializeTable(ParseTable table, int labelStart, int labelCount) {
		this.table = table;
		this.termFactory = table.getFactory();
		if (initializeFactories) {
			factory = new TermTreeFactory(termFactory);
			factory.setEnableTokens(!disableTokens);
			initializeFactories = false;
		}
		// assert !(factory instanceof TermTreeFactory) || ((TermTreeFactory) factory).getOriginalTermFactory() == table.getFactory()
		// 	: "ITermFactory of ITreeFactory does not correspond to ITermFactory of ParseTable";
		assert !(tokenizer instanceof Tokenizer) || ((Tokenizer) tokenizer).getKeywordRecognizer() == table.getKeywordRecognizer();
		this.prodReader = new ProductionAttributeReader(termFactory);
		this.labels = new LabelInfo[labelCount - labelStart];
		this.labelStart = labelStart;
	}

	public void initializeLabel(int labelNumber, IStrategoAppl parseTreeProduction) {
		labels[labelNumber - labelStart] = new LabelInfo(prodReader, parseTreeProduction);
	}

	public void initializeInput(String input, String filename) {
		assert offset == 0 : "Tree builder offset was not reset, race condition?";
		setTokenizer(disableTokens
			? new NullTokenizer(input, filename)
			: new Tokenizer(input, filename, table.getKeywordRecognizer()));
		setInput(input);
		reset();
	}

	public final ITokenizer getTokenizer() {
		return tokenizer;
	}

	protected void setTokenizer(ITokenizer tokenizer) {
		this.tokenizer = (AbstractTokenizer) tokenizer;
	}

	protected void setInput(String input) {
		this.input = input;
		stringIterator = new PushbackStringIterator(input);
	}

	protected final int getOffset() {
		return offset;
	}

	protected final String getInput() {
		return input;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	protected ParseTable getParseTable() {
		return table;
	}

	public ITreeFactory getFactory() {
		if (factory == null && initializeFactories)
			throw new IllegalStateException("Not initialized yet");
		return factory;
	}

	@Override
	@Deprecated
	public Object buildTree(AbstractParseNode node) {
		assert tokenizer != null : "Tokenizer not initialized; initializeInput() not called?";
		return tryBuildAutoConcatListNode(super.buildTree(node));
	}

	@Override
	public void reset() {
		offset = 0;
		inLexicalContext = false;
		if (tokenizer != null && tokenizer.getStartOffset() > 0) {
			setTokenizer(disableTokens
				? new NullTokenizer(tokenizer.getInput(), tokenizer.getFilename())
				: new Tokenizer(tokenizer.getInput(), tokenizer.getFilename(), table.getKeywordRecognizer()));
		}
	}

	public void reset(int startOffset) {
		if(getTokenizer() instanceof Tokenizer){
			((Tokenizer)getTokenizer()).setPositions(0, 0, 0);
			reset();
			((Tokenizer)getTokenizer()).setPositions(0, startOffset, 0);
			setOffset(startOffset);
		}
		else{
			reset();
		}
	}


	@Override
	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		try {
			Object tree = tryBuildAutoConcatListNode(subtree);
			tree = recreateWithAllTokens(tree);
			tokenizer.makeToken(tokenizer.getStartOffset() - 1, TK_EOF, true);
			Object result = factory.createTop(tree, tokenizer.getFilename(), ambiguityCount);
			if (result instanceof ISimpleTerm)
				tokenizer.setAst((ISimpleTerm) result);
			return result;
		} finally {
			reset();
		}
	}

	/**
	 * Recreates a tree node, changing its begin and end token
	 * to the begin and end token of the entire token stream.
	 */
	protected Object recreateWithAllTokens(Object tree) {
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
		boolean isLayout = label.isLayout();
		boolean lexicalStart = false;
		boolean isCompletion = node.isProposal();
		boolean isNestedCompletion = node.isNestedProposal();
		boolean isSinglePlaceholderCompletion = node.isSinglePlaceholderInsertion();



		if (!inLexicalContext && label.isNonContextFree())
			inLexicalContext = lexicalStart = true;

		List<Object> children;

		if (isLayout) {
			// structure of layout does not matter; can simply iterate over all
			// production nodes
			children = null;

			ArrayDeque<AbstractParseNode> nodes = fillNodeStack(node);
			while (!nodes.isEmpty()) {
				AbstractParseNode current = nodes.pop();
				if (current.isParseProductionNode()){
					buildTreeProduction((ParseProductionNode) current);
				}
				if (0 <= current.getLabel() - labelStart && current.getLabel() - labelStart < labels.length) {
					LabelInfo mjlabel = labels[current.getLabel() - labelStart];
					tokenizer.markPossibleSyntaxError(mjlabel, prevToken, offset - 1,
							prodReader);
				}
			}
		} else if (isList) {
			children = inLexicalContext ? null : new AutoConcatList<Object>(
					label.getSort());

			ArrayDeque<AbstractParseNode> nodes = new ArrayDeque<AbstractParseNode>();
			nodes.push(node);

			while (!nodes.isEmpty()) {
				AbstractParseNode current = nodes.pop();

				LabelInfo currentLabel = current.isAmbNode()
						|| current.isParseProductionNode() ? null
						: labels[current.getLabel() - labelStart];

				if (currentLabel != null
						&& currentLabel.isList()
						&& (label.getSort() == null ? currentLabel.getSort() == null
								: label.getSort()
										.equals(currentLabel.getSort())))
					for (int i = current.getChildren().length - 1; i >= 0; i--)
						nodes.push(current.getChildren()[i]);
				else {
					Object child;
					if (inLexicalContext && current.isParseProductionChain())
						child = chainToTreeTopdown(current);
					else
						child = current.toTreeTopdown(this);

					// TODO: handle ambiguities in lexicals better (ignored now)
					if (inLexicalContext)
						child = null;
					if (child != null)
						children.add(child);
				}
			}

			if (!inLexicalContext && isList && children.isEmpty()) {
				IToken token = tokenizer.makeToken(
						tokenizer.getStartOffset() - 1, IToken.Kind.TK_LAYOUT, true);
				((AutoConcatList) children).setEmptyListToken(token);
			}
		} else {
			children = inLexicalContext ? null : new ArrayList<Object>(max(
					EXPECTED_NODE_CHILDREN, subnodes.length));

			// Recurse
			for (AbstractParseNode subnode : subnodes) {
				Object child;
				if (inLexicalContext && subnode.isParseProductionChain()) {
					child = chainToTreeTopdown(subnode);
				} else {
					child = subnode.toTreeTopdown(this);
				}
				// TODO: handle ambiguities in lexicals better (ignored now)
				if (inLexicalContext)
					child = null;
				if (child != null)
					children.add(tryBuildAutoConcatListNode(child));
			}
		}

		Object result;
		if (lexicalStart) {
			result = tryCreateStringTerminal(label, lastOffset);
			inLexicalContext = false;
		} else if (isLayout) {
			result = null;
		} else if (inLexicalContext) {
			tokenizer.tryMakeLayoutToken(offset - 1, lastOffset - 1, label);
			result = null; // don't create nodes inside lexical context; just
							// create one big token at the top
		} else if (isList) {
			result = children;
		} else {
			result = createNodeOrInjection(label, prevToken, children, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
		}
		tokenizer.markPossibleSyntaxError(label, prevToken, offset - 1,
				prodReader);
		return result;
	}

	private ArrayDeque<AbstractParseNode> fillNodeStack(AbstractParseNode node) {
		if (node.isAmbNode()) {
			return fillNodeStack(node.getChildren()[0]);
		}
		ArrayDeque<AbstractParseNode> nodes = new ArrayDeque<AbstractParseNode>();
		for (int i = 0; i < node.getChildren().length; i++){
			ArrayDeque<AbstractParseNode> nodesChild = fillNodeStack(node.getChildren()[i]);
			while(!nodesChild.isEmpty()){
				nodes.addLast(nodesChild.pop());
			}
		}
		nodes.addLast(node);
		return nodes;
	}

	/**
	 * Efficiently consume lexical chars in parse production chains.
	 * @see AbstractParseNode#isParseProductionChain()
	 */
	private Object chainToTreeTopdown(AbstractParseNode node) {
		assert node.isParseProductionChain();
		while (node.isParseNode()) {
			AbstractParseNode[] kids = ((ParseNode) node).getChildren();
			if (kids.length == 2) {
				buildTreeProduction((ParseProductionNode) kids[0]);
				node = kids[1];
			} else if (kids.length == 1) {
				// UNDONE: node = kids[0];
				return buildTreeNode((ParseNode) node);
			} else {
				throw new IllegalStateException("Unexpected node in parse production chain: " + node);
			}
		}
		buildTreeProduction((ParseProductionNode) node);
		return null;
	}

	@Override
	public Object buildTreeAmb(ParseNode a) {
		if (inLexicalContext) {
			// Ignore ambiguities in lexicals; can't show them in AST
			AbstractParseNode n = a.getChildren()[0];
			switch (n.getNodeType()) {
				case AbstractParseNode.CYCLE:
					return buildTreeCycle((CycleParseNode) n);
				case AbstractParseNode.PARSE_PRODUCTION_NODE:
					return buildTreeProduction((ParseProductionNode) n);
				case AbstractParseNode.AMBIGUITY:
					return buildTreeAmb((ParseNode) n);
				case AbstractParseNode.PARSENODE:
					return buildTreeNode((ParseNode) n);
				default:
					throw new IllegalStateException("Unkown node type");
			}
		}

		final int oldOffset = offset;
		final int oldBeginOffset = tokenizer.getStartOffset();
		final boolean oldLexicalContext = inLexicalContext;
		final AbstractParseNode[] subnodes = a.getChildren();
		final ArrayList<Object> children =
			new ArrayList<Object>(max(EXPECTED_NODE_CHILDREN, subnodes.length));
		tokenizer.setAmbiguous(true);


		// Recurse
		for (AbstractParseNode subnode : subnodes) {
			// Restore lexical state for each branch
			offset = oldOffset;
			tokenizer.setStartOffset(oldBeginOffset);
			inLexicalContext = oldLexicalContext;
			//if(subnode.isCompleted()){
			//    isCompletion = true;
			//}

			Object subtree;

			switch (subnode.getNodeType()) {
				case AbstractParseNode.CYCLE:
					subtree = buildTreeCycle((CycleParseNode) subnode);
					break;
				case AbstractParseNode.PARSE_PRODUCTION_NODE:
					subtree = buildTreeProduction((ParseProductionNode) subnode);
					break;
				case AbstractParseNode.AMBIGUITY:
					subtree = buildTreeAmb((ParseNode) subnode);
					break;
				case AbstractParseNode.PARSENODE:
					subtree = buildTreeNode((ParseNode) subnode);
					break;
				case AbstractParseNode.PREFER:
					subtree = buildTreeNode((ParseNode) subnode);
					break;
				case AbstractParseNode.AVOID:
					subtree = buildTreeNode((ParseNode) subnode);
					break;
				default:
					throw new IllegalStateException("Unkown node type");
			}
			Object child = tryBuildAutoConcatListNode(subtree);

			if (child != null) children.add(child);
		}
		IToken leftToken = null;
		IToken rightToken = null;
		if (children.size() > 0) {
			leftToken = factory.getLeftToken(children.get(0));
			rightToken = factory.getRightToken(children.get(children.size() - 1));
		} else {
			// No kids? Non-cf amb
			return null;
			// leftToken = rightToken = tokenizer.makeToken(offset - 1, TK_UNKNOWN, true);
		}



		return factory.createAmb(children, leftToken, rightToken, false, false, false);
	}

	@Override
	public Object buildTreeCycle(CycleParseNode node) {
		if (inLexicalContext)
			return null;
		IToken token = tokenizer.makeToken(offset - 1, TK_UNKNOWN, true);
		if (node.getTargetLabel() == -1) {
			return factory.createNonTerminal("cycle", "cycle", token, token, new ArrayList<Object>(), node.isProposal(), node.isNestedProposal(), node.isSinglePlaceholderInsertion());
		} else {
			LabelInfo label = labels[node.getTargetLabel() - labelStart];
			String cons = label.getSort();
			if (cons == null) cons = "cycle";
			Object child = factory.createNonTerminal(label.getSort(), cons, token, token, new ArrayList<Object>(), node.isProposal(), node.isNestedProposal(), node.isSinglePlaceholderInsertion());
			List<Object> children = new ArrayList<Object>();
			children.add(child);
			return factory.createNonTerminal("cycle", "cycle", token, token, children, node.isProposal(), node.isNestedProposal(), node.isSinglePlaceholderInsertion());
		}
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
		return inLexicalContext ? null : createIntTerminal(node, null);
	}


	private Object tryCreateStringTerminal(LabelInfo label, int lastOffset) {
		String sort = label.getSort();
		IToken rightToken = tokenizer.makeToken(offset - 1, label, sort != null);

		if (sort == null) return null; // just a literal

		// Don't use tokens here in case tokenizer is disabled
		IToken leftToken = rightToken.getStartOffset() == lastOffset ? rightToken : tokenizer.getTokenAtOffset(lastOffset);
		String contents = tokenizer.toString(lastOffset, offset - 1);
		assert disableTokens || tokenizer.isAmbiguous()
			|| (contents.equals(tokenizer.toString(leftToken, rightToken)) && lastOffset == leftToken.getStartOffset());

		Object result = factory.createStringTerminal(sort, leftToken, rightToken, getPaddedLexicalValue(label, contents, lastOffset), label.isCaseInsensitive());
		String constructor = label.getMetaVarConstructor();
		if (constructor != null) {
			ArrayList<Object> children = new ArrayList<Object>(1);
			children.add(result);
			result = factory.createNonTerminal(sort, constructor, leftToken, rightToken, children, false, false, false);
		}
		return result;
	}

	private Object createIntTerminal(ParseProductionNode node, LabelInfo label) {
		IToken token = tokenizer.makeToken(offset - 1, label, true);
		int value = node.prod;
		return factory.createIntTerminal(label == null ? null : label.getSort(), token, value);
	}

	private Object createNodeOrInjection(LabelInfo label, IToken prevToken, List<Object> children, boolean isCompletion, boolean isNestedCompletion, boolean isSinglePlaceholderCompletion) {

		String constructor = label.getConstructor();

		if (label.isList()) {
			throw new IllegalStateException("Illegal state: now handled by tryCreateAutoConcatListNode()");
			// return createNode(label, LIST_CONSTRUCTOR, prevToken, children);
		} else if (constructor != null) {
			// UNDONE: tokenizer.makeToken(offset, label); // TODO: why makeToken here??
			return createNode(label, constructor, prevToken, children, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
		} else if (label.getAstAttribute() != null) {
			return createAstNonTerminal(label, prevToken, children);
		} else if (label.isOptional()) {
			// TODO: Spoofax/295: JSGLR does not output correct AST for optional literals
			if (children == null || children.size() == 0) {
				return createNode(label, "None", prevToken, children, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
			} else {
				assert children.size() == 1;
				return createNode(label, "Some", prevToken, children, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
			}
		} else if (children.size() == 1) {
			// Injection
			// TODO: efficiently store injection sort for use by content completion?
			//       would be needed to distinguish FoldingRules and Sorts in "folding" sections...
			//       maybe only if the content proposer demands it?
			// TODO: also, avoid semantics for deprecated?

		    // adding brackets to origin of bracket nodes
		    IToken left = getStartToken(prevToken);
		    IToken right = tokenizer.currentToken();

		    return factory.createInjection(label.getSort(), left, right, children.get(0), isCompletion, isNestedCompletion, isSinglePlaceholderCompletion, label.isBracket());
		} else {
			// Constructor-less application (tuple)
			return createNode(label, TUPLE_CONSTRUCTOR, prevToken, children, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
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
			List<Object> children, boolean isCompletion, boolean isNestedCompletion, boolean isSinglePlaceholderCompletion) {

		IToken left = getStartToken(prevToken);
		// IToken right = getEndToken(left, tokenizer.currentToken());
		IToken right = tokenizer.currentToken();

		if (constructor == LIST_CONSTRUCTOR) {
			return factory.createList(label.getSort(), left, right, children);
		} else if (constructor == TUPLE_CONSTRUCTOR) {
			return factory.createTuple(label.getSort(), left, right, children);
		} else if (constructor == null && children.size() == 1 && factory.tryGetStringValue(children.get(0)) != null) {
			// Child node was a <string> node (rare case); unpack it and create a new terminal
			return factory.createStringTerminal(label.getSort(), left, right, factory.tryGetStringValue(children.get(0)), label.isCaseInsensitive());
		} else {
			return factory.createNonTerminal(label.getSort(), constructor, left, right, children, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
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
	private String getPaddedLexicalValue(LabelInfo label, String contents, int startOffset) {
		if (label.isIndentPaddingLexical()) {
			if (startOffset == 0) return contents;
			int lineStart = input.lastIndexOf('\n', startOffset - 1) + 1;
			StringBuilder result = new StringBuilder();
			result.append(input, lineStart, startOffset);
			for (int i = 0; i < result.length(); i++) {
				char c = result.charAt(i);
				if (c != ' ' && c != '\t') result.setCharAt(i, ' ');
			}
			result.append(contents);
			return result.toString();
		} else {
			return contents; // lazily load token string value
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
				// HACK: Assume TK_NO_TOKEN_KIND kind for empty tokens in AST nodes
				return tokenizer.makeToken(offset - 1, IToken.Kind.TK_NO_TOKEN_KIND, true);
			} else {
				return tokenizer.getTokenAt(index + 1);
			}
		}
	}

	/* Get the last no-layout token for an AST node.
	private IToken getEndToken(IToken currentToken, IToken lastToken) {
		if (lastToken.getEndOffset() == input.length() - 1)
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
	protected final void consumeLexicalChar(int parsedChar) {
		if (offset >= input.length()) {
			markUnexpectedEOF(parsedChar);
		} else {
			int inputChar = input.codePointAt(offset);

			if (parsedChar != inputChar) {
				if (RecoveryConnector.isLayoutCharacter(parsedChar)) {
					tokenizer.tryMakeSkippedRegionToken(offset);
					offset += Character.charCount(parsedChar);
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
				offset += Character.charCount(parsedChar);
			}
		}
	}

	private void markUnexpectedEOF(int character) {
		/*assert nonMatchingOffset == NONE :
				"Character in parse tree after end of input stream: " + (char) character
				+ " - may be caused by unexpected character in parse tree at position "
				+ nonMatchingChar 	+ ": " + nonMatchingChar + " instead of "
				+ nonMatchingCharExpected;*/
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
