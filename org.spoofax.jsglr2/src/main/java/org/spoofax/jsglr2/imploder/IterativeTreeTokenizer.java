package org.spoofax.jsglr2.imploder;

import java.util.*;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.messages.IMessage;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

public abstract class IterativeTreeTokenizer<Tree> extends TreeTokenizer<Tree> {

    @Override public SubTree tokenize(FileObject resource, Tokens tokens, TreeImploder.SubTree<Tree> rootTree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), rootTree.tree);

        // SubTree res = tokenizeInternal(tokens, tree, new Position(0, 1, 1));
        Stack<LinkedList<TreeImploder.SubTree<Tree>>> inputStack = new Stack<>();
        Stack<Position> pivotPositionStack = new Stack<>();
        Stack<Position> startPositionStack = new Stack<>();
        Stack<LinkedList<SubTree>> outputStack = new Stack<>();

        inputStack.add(new LinkedList<>(Collections.singletonList(rootTree)));
        pivotPositionStack.add(new Position(0, 1, 1));
        startPositionStack.add(new Position(0, 1, 1));
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
                Collection<IMessage> messages = null;
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

                    // If tree production == null, that means it's an "amb" node; in that case, position is not advanced
                    if(tree.production != null)
                        pivotPosition = subTree.endPosition;

                    if(subTree.messages != null) {
                        if(messages == null)
                            messages = new ArrayList<>();

                        messages.addAll(subTree.messages);
                    }
                }

                if(tree.production != null && tree.production.isRecovery()) {
                    if(messages == null)
                        messages = new ArrayList<>();

                    messages.add(parseErrorMessage(resource, currentStart, pivotPosition));
                }

                // Add processed output to the list that is on top of the stack
                pivotPositionStack.pop();
                pivotPositionStack.push(pivotPosition);
                outputStack.peek().add(new SubTree(tree, leftToken, rightToken, pivotPosition, messages));
            } else {
                TreeImploder.SubTree<Tree> tree = currentIn.getFirst(); // Process the next input
                if(tree.children.size() == 0) {
                    if(tree.width > 0) {
                        Position endPosition = currentPos.step(tokens.getInput(), tree.width);
                        IToken token = tokens.makeToken(currentPos, endPosition, tree.production);
                        tokenTreeBinding(token, tree.tree);
                        currentOut.add(new SubTree(tree, token, token, endPosition, Collections.emptyList()));
                        pivotPositionStack.pop();
                        pivotPositionStack.push(endPosition);
                    } else {
                        currentOut.add(new SubTree(tree, null, null, currentPos, Collections.emptyList()));
                    }
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

        tokens.makeEndToken(new Position(res.endPosition.offset, res.endPosition.line, res.endPosition.column));
        tokenTreeBinding(tokens.endToken(), res.tree);

        return res;
    }

}
