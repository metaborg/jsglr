package org.spoofax.jsglr2.parseforest;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public abstract class ParseForestManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
{

    protected final ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing;

    protected ParseForestManager(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        this.observing = observing;
    }

    /**
     * @param stack
     *            The parse node will be added to the link _to_ this stack node.
     */
    abstract public ParseNode createParseNode(ParseState parseState, IStackNode stack, IProduction production,
        Derivation firstDerivation);

    /**
     * @param stack
     *            The derivation will be added to the parse node on the link _to_ this stack node.
     */
    abstract public Derivation createDerivation(ParseState parseState, IStackNode stack, IProduction production,
        ProductionType productionType, ParseForest[] parseForests);

    abstract public void addDerivation(ParseState parseState, ParseNode parseNode, Derivation derivation);

    abstract public ParseNode createSkippedNode(ParseState parseState, IProduction production,
        ParseForest[] parseForests);

    abstract public ParseForest createCharacterNode(ParseState parseState);

    abstract public ParseForest[] parseForestsArray(int length);

    public ParseForest filterStartSymbol(ParseForest parseForest, String startSymbol, ParseState parseState) {
        ParseNode topNode = (ParseNode) parseForest;
        List<Derivation> derivationsWithStartSymbol = new ArrayList<>();

        for(Derivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production().startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                derivationsWithStartSymbol.add(derivation);
        }

        if(derivationsWithStartSymbol.isEmpty())
            return null;
        else
            return (ParseForest) filteredTopParseNode(topNode, derivationsWithStartSymbol);
    }

    abstract protected ParseNode filteredTopParseNode(ParseNode parseNode, List<Derivation> derivations);

    public void visit(JSGLR2Request request, ParseForest root,
        ParseForestVisitor<ParseForest, Derivation, ParseNode> visitor) {

        Stack<Position> positionStack = new Stack<>();

        Stack<Object> inputStack = new Stack<>();

        Stack<ParseNode> outputParseNodes = new Stack<>();
        Stack<VisitDerivation<Derivation>> outputDerivations = new Stack<>();

        Position pivotPosition = Position.START_POSITION;
        inputStack.push(root);
        outputDerivations.push(new VisitDerivation<>());

        while(!inputStack.isEmpty() || !outputParseNodes.isEmpty()) {
            if(!outputDerivations.isEmpty() && outputDerivations.peek().visitable()) { // Visit derivation
                VisitDerivation<Derivation> derivation = outputDerivations.pop();

                visitor.visitDerivation(derivation.derivation, positionStack.peek(), pivotPosition);
            } else if(!outputParseNodes.isEmpty()
                && !(!inputStack.isEmpty() && (inputStack.peek() instanceof IDerivation))) { // Visit parse node
                ParseNode parseNode = outputParseNodes.pop();

                visitor.visitParseNode(parseNode, positionStack.peek(), pivotPosition);

                outputDerivations.peek().remainingChildren--;
            } else if(inputStack.peek() instanceof IDerivation) { // Consume derivation
                Derivation derivation = (Derivation) inputStack.pop();
                outputDerivations.push(new VisitDerivation<>(derivation));

                ParseForest[] children = derivation.parseForests();

                for(int i = children.length - 1; i >= 0; i--)
                    inputStack.push(children[i]);

                pivotPosition = positionStack.peek();
            } else if(inputStack.peek() instanceof ICharacterNode) { // Consume character node
                pivotPosition = pivotPosition.step(request.input, ((ICharacterNode) inputStack.pop()).width());
                outputDerivations.peek().remainingChildren--;
            } else if(inputStack.peek() instanceof IParseNode) { // Consume (skipped) parse node
                ParseNode parseNode = (ParseNode) inputStack.pop();

                if(parseNode.hasDerivations()) { // Parse node with derivation
                    outputParseNodes.push(parseNode);
                    positionStack.push(pivotPosition);

                    for(Derivation derivation : parseNode.getDerivations())
                        inputStack.push(derivation);
                } else { // Skipped parse node
                    pivotPosition = pivotPosition.step(request.input, parseNode.width());

                    visitor.visitParseNode(parseNode, positionStack.peek(), pivotPosition);

                    outputDerivations.peek().remainingChildren--;
                }
            } else {
                throw new RuntimeException("invalid parse node");
            }
        }
    }

    static class VisitDerivation<Derivation extends IDerivation<?>> {
        int remainingChildren = 0;
        Derivation derivation;

        VisitDerivation() {
            this.remainingChildren = 1;
        }

        VisitDerivation(Derivation derivation) {
            this.remainingChildren = derivation.parseForests().length;
        }

        boolean visitable() {
            return remainingChildren == 0;
        }
    }

}
