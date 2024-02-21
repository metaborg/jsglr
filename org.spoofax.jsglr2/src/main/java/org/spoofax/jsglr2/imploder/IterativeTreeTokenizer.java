package org.spoofax.jsglr2.imploder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

import jsglr.shared.IToken;

public abstract class IterativeTreeTokenizer<Tree> extends TreeTokenizer<Tree> {

    @Override public void tokenize(Tokens tokens, TreeImploder.SubTree<Tree> rootTree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), rootTree.tree);

        Stack<LinkedList<TreeImploder.SubTree<Tree>>> inputStack = new Stack<>();
        Stack<Position> pivotPositionStack = new Stack<>();
        Stack<Position> startPositionStack = new Stack<>();
        Stack<LinkedList<SubTree>> outputStack = new Stack<>();

        inputStack.add(new LinkedList<>(Collections.singletonList(rootTree)));
        pivotPositionStack.add(Position.START_POSITION);
        startPositionStack.add(Position.START_POSITION);
        outputStack.add(new LinkedList<>());

        while(true) {
            LinkedList<TreeImploder.SubTree<Tree>> currentIn = inputStack.peek();
            Position currentPos = pivotPositionStack.peek();
            Position currentStart = startPositionStack.peek();
            LinkedList<SubTree> currentOut = outputStack.peek();
            if(currentIn.isEmpty()) { // If we're finished with the current children
                inputStack.pop(); // That means it's done, so remove it from the stack
                if(inputStack.isEmpty()) // If it was the last stack node, we're done
                    break;
                pivotPositionStack.pop(); // Also remove `currentPos` from stack
                startPositionStack.pop(); // Also remove `currentStart` from stack
                outputStack.pop(); // Also remove `currentOut` from stack

                // Process current output in the way we're used to
                TreeImploder.SubTree<Tree> tree = inputStack.peek().removeFirst();
                IToken leftToken = null;
                IToken rightToken = null;
                Position pivotPosition = currentPos;
                for(SubTree subTree : currentOut) {
                    // If child tree had tokens that were not yet bound, bind them
                    if(subTree.tree == null) {
                        if(subTree.leftToken != null)
                            tokenTreeBinding(subTree.leftToken, tree.tree);

                        if(subTree.rightToken != null)
                            tokenTreeBinding(subTree.rightToken, tree.tree);
                    }

                    // The left-most token of this tree is the first non-null leftToken of a subTree
                    if(leftToken == null)
                        leftToken = subTree.leftToken;

                    // The right-most token of this tree is the last non-null rightToken of a subTree
                    if(subTree.rightToken != null)
                        rightToken = subTree.rightToken;

                    pivotPosition = subTree.endPosition;
                }

                // If there is no token, this means that this AST has no characters in the input.
                // In this case, create an empty token to associate with this AST node.
                if(leftToken == null) {
                    assert rightToken == null;
                    leftToken = rightToken = tokens.makeToken(currentStart, pivotPosition, tree.production);
                    tokenTreeBinding(leftToken, tree.tree);
                }

                // In the case when we're dealing with an ambiguous tree node, position is not advanced
                // noinspection ConstantConditions (peek can never return `null`, as we check size > 1)
                if(inputStack.size() > 1 && !inputStack.get(inputStack.size() - 2).peek().isAmbiguous) {
                    pivotPositionStack.pop();
                    pivotPositionStack.push(pivotPosition);
                }
                // Add processed output to the list that is on top of the stack
                outputStack.peek().add(new SubTree(tree, leftToken, rightToken, pivotPosition));
            } else {
                TreeImploder.SubTree<Tree> tree = currentIn.getFirst(); // Process the next input
                if(tree.production != null && !tree.production.isContextFree() || tree.isCharacterTerminal) {
                    if(tree.width > 0 || tree.tree != null) {
                        Position endPosition = currentPos.step(tokens.getInput(), tree.width);
                        IToken token = tokens.makeToken(currentPos, endPosition, tree.production);
                        tokenTreeBinding(token, tree.tree);

                        currentOut.add(new SubTree(tree, token, token, endPosition));
                        pivotPositionStack.pop();
                        pivotPositionStack.push(endPosition);
                    } else
                        currentOut.add(new SubTree(tree, null, null, currentPos));
                    currentIn.removeFirst();
                } else {
                    inputStack.add(new LinkedList<>(tree.children));
                    pivotPositionStack.add(currentPos);
                    startPositionStack.add(currentPos);
                    outputStack.add(new LinkedList<>());
                }
            }
        }

        SubTree res = outputStack.pop().getFirst();

        tokens.makeEndToken(res.endPosition);
        tokenTreeBinding(tokens.endToken(), res.tree);
    }

}
