package org.spoofax.jsglr2.parseforest;

import java.util.Stack;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parser.Position;

public interface ParseNodeVisiting
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
{

    default void visit(JSGLR2Request request, ParseForest root,
        ParseNodeVisitor<ParseForest, Derivation, ParseNode> visitor) {
        Stack<Position> positionStack = new Stack<>(); // Start positions of parse nodes
        Stack<Object> inputStack = new Stack<>(); // Pending parse nodes and derivations
        Stack<Visit<ParseNode>> outputStack = new Stack<>(); // Parse node and derivations with remaining children

        Position pivotPosition = Position.START_POSITION;
        positionStack.push(Position.START_POSITION);
        inputStack.push(root);
        outputStack.push(new Visit<>());

        while(!inputStack.isEmpty() || !outputStack.isEmpty()) {
            if(!outputStack.isEmpty() && outputStack.peek().done()) { // Finish derivation
                outputStack.pop();

                if(outputStack.isEmpty())
                    break;

                outputStack.peek().remainingChildren--;

                if(!outputStack.isEmpty() && outputStack.peek().done()) { // Visit parse node
                    ParseNode parseNode = outputStack.pop().parseNode;

                    visitor.postVisit(parseNode, positionStack.pop(), pivotPosition);

                    outputStack.peek().remainingChildren--;
                }
            } else if(inputStack.peek() instanceof IDerivation) { // Consume derivation
                Derivation derivation = (Derivation) inputStack.pop();

                outputStack.push(new Visit<>(derivation));

                ParseForest[] children = derivation.parseForests();

                for(int i = children.length - 1; i >= 0; i--)
                    inputStack.push(children[i]);

                pivotPosition = positionStack.peek();
            } else if(inputStack.peek() instanceof ICharacterNode) { // Consume character node
                pivotPosition = pivotPosition.step(request.input, ((ICharacterNode) inputStack.pop()).width());

                outputStack.peek().remainingChildren--;
            } else if(inputStack.peek() instanceof IParseNode) { // Consume (skipped) parse node
                ParseNode parseNode = (ParseNode) inputStack.pop();
                positionStack.push(pivotPosition);

                boolean visitChildren = visitor.preVisit(parseNode, pivotPosition);

                if(visitChildren && parseNode.hasDerivations()) { // Parse node with derivation(s)
                    int derivations = 0;

                    for(Derivation derivation : parseNode.getDerivations()) {
                        inputStack.push(derivation);
                        derivations++;

                        if (derivations >= 1 && !visitor.visitAmbiguousDerivations())
                            break;
                    }

                    outputStack.push(new Visit<>(derivations, parseNode));
                } else { // Skipped parse node (without derivations)
                    pivotPosition = pivotPosition.step(request.input, parseNode.width());

                    visitor.postVisit(parseNode, positionStack.pop(), pivotPosition);

                    outputStack.peek().remainingChildren--;
                }
            }
        }
    }

    class Visit<ParseNode> {
        int remainingChildren;
        ParseNode parseNode;

        Visit() {
            this.remainingChildren = 1;
            this.parseNode = null;
        }

        Visit(IDerivation<?> derivation) {
            this.remainingChildren = derivation.parseForests().length;
            this.parseNode = null;
        }

        Visit(int remainingChildren, ParseNode parseNode) {
            this.remainingChildren = remainingChildren;
            this.parseNode = parseNode;
        }

        boolean done() {
            return remainingChildren == 0;
        }
    }

    public static SourceRegion visitRegion(String inputString, Position startPosition, Position endPosition) {
        if(endPosition.offset > startPosition.offset)
            endPosition = endPosition.previous(inputString);

        return new SourceRegion(startPosition, endPosition);
    }

}
