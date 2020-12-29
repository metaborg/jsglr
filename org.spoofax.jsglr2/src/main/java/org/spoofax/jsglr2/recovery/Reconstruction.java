package org.spoofax.jsglr2.recovery;

import java.util.Collections;

import org.metaborg.parsetable.symbols.ILiteralSymbol;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseNodeVisitor;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public class Reconstruction {

    static public String reconstruct(IParser<?> parser, ParseSuccess<?> success) {
        ReconstructParseNodeVisitor<?, ?, ?> visitor =
            new ReconstructParseNodeVisitor<>(success.parseState.inputStack.inputString());

        parser.visit(success, visitor);

        return visitor.reconstruction.toString();
    }

    static class ReconstructParseNodeVisitor
    //@formatter:off
       <ParseForest extends IParseForest,
        Derivation  extends IDerivation<ParseForest>,
        ParseNode   extends IParseNode<ParseForest, Derivation>>
    //@formatter:on
        implements ParseNodeVisitor<ParseForest, Derivation, ParseNode> {

        String inputString;
        StringBuilder reconstruction = new StringBuilder();

        public ReconstructParseNodeVisitor(String inputString) {
            this.inputString = inputString;
        }

        @Override public boolean preVisit(ParseNode parseNode, Position startPosition) {
            return !isBoundary(parseNode);
        }

        private boolean isBoundary(ParseNode parseNode) {
            return parseNode.production().isRecovery() || parseNode.production().isLexical()
                || parseNode.production().isLiteral();
        }

        @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
            if(isBoundary(parseNode)) {
                if(parseNode.production().isWater())
                    reconstruction.append(String.join("", Collections.nCopies(parseNode.width(), " ")));
                else if(parseNode.production().isInsertion()) {
                    if(parseNode.production().isLiteral())
                        reconstruction.append(((ILiteralSymbol) parseNode.production().lhs()).literal());
                    else
                        throw new IllegalStateException("cannot reconstruct non-literal insertion");
                } else
                    reconstruction.append(inputString, startPosition.offset, endPosition.offset);
            }
        }
    }

}
