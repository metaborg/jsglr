package org.spoofax.jsglr2.imploder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

public abstract class IterativeTreeTokenizer<Tree> extends TreeTokenizer<Tree> {

    @Override public Tree tokenize(Tokens tokens, TreeImploder.SubTree<Tree> rootTree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), rootTree.tree);

        // SubTree res = tokenizeInternal(tokens, tree, new Position(0, 1, 1));
        Stack<LinkedList<TreeImploder.SubTree<Tree>>> inputStack = new Stack<>();
        Stack<Position> positionStack = new Stack<>();
        Stack<LinkedList<SubTree>> outputStack = new Stack<>();

        inputStack.add(new LinkedList<>(Collections.singletonList(rootTree)));
        positionStack.add(new Position(0, 1, 1));
        outputStack.add(new LinkedList<>());

        while(true) {
            LinkedList<TreeImploder.SubTree<Tree>> currentIn = inputStack.peek();
            Position currentPos = positionStack.peek();
            LinkedList<SubTree> currentOut = outputStack.peek();
            if(currentIn.isEmpty()) { // If we're finished with the current children
                inputStack.pop(); // That means it's done, so remove it from the stack
                if(inputStack.isEmpty()) // If it was the last stack node, we're done
                    break;
                positionStack.pop(); // Also remove `currentPos` from stack
                outputStack.pop(); // Also remove `currentOut` from stack

                // Process current output in the way we're used to
                TreeImploder.SubTree<Tree> tree = inputStack.peek().removeFirst();
                IToken leftToken = null;
                IToken rightToken = null;
                Position pivotPosition = currentPos;
                for(SubTree subTree : currentOut) {
                    // If child tree had tokens that were not yet bound, bind them
                    if(tree.tree == null) {
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

                    // If tree production == null, that means it's an "amb" node; in that case, position is not advanced
                    if(tree.production != null)
                        pivotPosition = subTree.endPosition;
                }
                // Add processed output to the list that is on top of the stack
                positionStack.pop();
                positionStack.push(pivotPosition);
                outputStack.peek().add(new SubTree(tree, leftToken, rightToken, pivotPosition));
            } else {
                TreeImploder.SubTree<Tree> tree = currentIn.getFirst(); // Process the next input
                if(tree.children.size() == 0) {
                    if(tree.width > 0) {
                        Position endPosition = currentPos.step(tokens.getInput(), tree.width);
                        IToken token = tokens.makeToken(currentPos, endPosition, tree.production);
                        tokenTreeBinding(token, tree.tree);
                        currentOut.add(new SubTree(tree, token, token, endPosition));
                        positionStack.pop();
                        positionStack.push(endPosition);
                    } else {
                        currentOut.add(new SubTree(tree, null, null, currentPos));
                    }
                    currentIn.removeFirst();
                } else {
                    inputStack.add(new LinkedList<>(tree.children));
                    positionStack.add(currentPos);
                    outputStack.add(new LinkedList<>());
                }
            }
        }

        SubTree res = outputStack.pop().getFirst();

        tokens.makeEndToken(new Position(res.endPosition.offset, res.endPosition.line, res.endPosition.column));
        tokenTreeBinding(tokens.endToken(), res.tree);

        return res.tree;
    }

}
