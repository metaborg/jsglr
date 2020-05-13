package org.spoofax.jsglr2.parseforest;

import java.util.Stack;

import org.spoofax.jsglr2.JSGLR2Request;
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
        Stack<ParseNode> outputParseNodes = new Stack<>(); // Parse nodes that are being processed
        Stack<VisitDerivation> outputDerivations = new Stack<>(); // Maintains remaining children for derivations

        Position pivotPosition = Position.START_POSITION;
        positionStack.push(Position.START_POSITION);
        inputStack.push(root);
        outputDerivations.push(new VisitDerivation());

        while(!inputStack.isEmpty() || !outputParseNodes.isEmpty()) {
            if(!outputDerivations.isEmpty() && outputDerivations.peek().done()) { // Finish derivation
                outputDerivations.pop();

                if(inputStack.isEmpty() || !(inputStack.peek() instanceof IDerivation)) { // Visit parse node
                    ParseNode parseNode = outputParseNodes.pop();

                    visitor.visit(parseNode, positionStack.pop(), pivotPosition);

                    outputDerivations.peek().remainingChildren--;
                }
            } else if(inputStack.peek() instanceof IDerivation) { // Consume derivation
                Derivation derivation = (Derivation) inputStack.pop();

                outputDerivations.push(new VisitDerivation(derivation));

                ParseForest[] children = derivation.parseForests();

                for(int i = children.length - 1; i >= 0; i--)
                    inputStack.push(children[i]);

                pivotPosition = positionStack.peek();
            } else if(inputStack.peek() instanceof ICharacterNode) { // Consume character node
                pivotPosition = pivotPosition.step(request.input, ((ICharacterNode) inputStack.pop()).width());
                outputDerivations.peek().remainingChildren--;
            } else if(inputStack.peek() instanceof IParseNode) { // Consume (skipped) parse node
                ParseNode parseNode = (ParseNode) inputStack.pop();
                positionStack.push(pivotPosition);

                if(parseNode.hasDerivations()) { // Parse node with derivation(s)
                    outputParseNodes.push(parseNode);

                    for(Derivation derivation : parseNode.getDerivations())
                        inputStack.push(derivation);
                } else { // Skipped parse node (without derivations)
                    pivotPosition = pivotPosition.step(request.input, parseNode.width());

                    visitor.visit(parseNode, positionStack.pop(), pivotPosition);

                    outputDerivations.peek().remainingChildren--;
                }
            }
        }
    }

    class VisitDerivation {
        int remainingChildren;

        VisitDerivation() {
            this.remainingChildren = 1;
        }

        VisitDerivation(IDerivation<?> derivation) {
            this.remainingChildren = derivation.parseForests().length;
        }

        boolean done() {
            return remainingChildren == 0;
        }
    }

}
