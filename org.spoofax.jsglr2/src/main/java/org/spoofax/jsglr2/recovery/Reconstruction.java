package org.spoofax.jsglr2.recovery;

import java.util.Collections;

import org.metaborg.parsetable.symbols.ILiteralSymbol;
import org.metaborg.parsetable.symbols.SyntaxContext;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseNodeVisitor;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public class Reconstruction {

    static public Reconstructed reconstruct(IParser<?> parser, ParseSuccess<?> success) {
        ReconstructParseNodeVisitor<?, ?, ?> visitor =
            new ReconstructParseNodeVisitor<>(success.parseState.inputStack.inputString());

        parser.visit(success, visitor);

        return visitor.getReconstructed();
    }

    public static class Reconstructed {
        final public String inputString;
        final public int insertions;
        final public int deletions;

        public Reconstructed(String inputString, int insertions, int deletions) {
            this.inputString = inputString;
            this.insertions = insertions;
            this.deletions = deletions;
        }
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
        int insertions = 0;
        int deletions = 0;

        public ReconstructParseNodeVisitor(String inputString) {
            this.inputString = inputString;
        }

        @Override public boolean preVisit(ParseNode parseNode, Position startPosition) {
            return !isBoundary(parseNode);
        }

        private boolean isBoundary(ParseNode parseNode) {
            return parseNode.production().isRecovery() || parseNode.production().isLexical()
                || parseNode.production().isLiteral()
                || (parseNode.production().lhs().syntaxContext() == SyntaxContext.Lexical
                    && parseNode.production().isLayout());
        }

        @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
            if(isBoundary(parseNode)) {
                if(parseNode.production().isWater()) {
                    reconstruction.append(String.join("", Collections.nCopies(parseNode.width(), " ")));

                    deletions += parseNode.width();
                } else if(parseNode.production().isInsertion()) {
                    if(parseNode.production().isLiteral()) {
                        String literal = ((ILiteralSymbol) parseNode.production().lhs()).literal();

                        reconstruction.append(literal);

                        insertions += literal.length();
                    } else
                        throw new IllegalStateException("cannot reconstruct non-literal insertion");
                } else
                    reconstruction.append(inputString, startPosition.offset, endPosition.offset);
            }
        }

        Reconstructed getReconstructed() {
            return new Reconstructed(reconstruction.toString(), insertions, deletions);
        }
    }

}
