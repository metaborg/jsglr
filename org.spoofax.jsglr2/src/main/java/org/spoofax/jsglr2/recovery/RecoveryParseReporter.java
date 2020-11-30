package org.spoofax.jsglr2.recovery;

import java.util.Collection;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IParseReporter;
import org.spoofax.jsglr2.parser.ParseReporterFactory;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryParseReporter
//@formatter:off
   <ParseForest          extends IParseForest,
    Derivation           extends IDerivation<ParseForest>,
    ParseNode            extends IParseNode<ParseForest, Derivation>,
    StackNode            extends IStackNode,
    InputStack           extends IInputStack,
    BacktrackChoicePoint extends IBacktrackChoicePoint<InputStack, StackNode>,
    ParseState           extends AbstractParseState<InputStack, StackNode> & IRecoveryParseState<InputStack, StackNode, BacktrackChoicePoint>>
//@formatter:on
    implements IParseReporter<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> {

    ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager;

    RecoveryParseReporter(
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager) {
        this.parseForestManager = parseForestManager;
    }

    public static
//@formatter:off
   <ParseForest_          extends IParseForest,
    Derivation_           extends IDerivation<ParseForest_>,
    ParseNode_            extends IParseNode<ParseForest_, Derivation_>,
    StackNode_            extends IStackNode,
    InputStack_           extends IInputStack,
    BacktrackChoicePoint_ extends IBacktrackChoicePoint<InputStack_, StackNode_>,
    ParseState_           extends AbstractParseState<InputStack_, StackNode_> & IRecoveryParseState<InputStack_, StackNode_, BacktrackChoicePoint_>>
//@formatter:on
    ParseReporterFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_> factory() {
        return RecoveryParseReporter::new;
    }

    @Override public void report(ParseState parseState, ParseForest parseForest, Collection<Message> messages) {
        if(parseState.appliedRecovery()) {
            RecoveryMessagesVisitor visitor =
                new RecoveryMessagesVisitor(messages, parseState.inputStack.inputString());

            parseForestManager.visit(parseState.request, parseForest, visitor);
        }
    }

    class RecoveryMessagesVisitor implements ParseNodeVisitor<ParseForest, Derivation, ParseNode> {

        private final Collection<Message> messages;
        private final String inputString;

        RecoveryMessagesVisitor(Collection<Message> messages, String inputString) {
            this.messages = messages;
            this.inputString = inputString;
        }

        @Override public boolean preVisit(ParseNode parseNode, Position startPosition) {
            return !isRecovery(parseNode);
        }

        @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
            if(isRecovery(parseNode)) {
                SourceRegion region = ParseNodeVisiting.visitRegion(inputString, startPosition, endPosition);

                messages.add(RecoveryMessages.get(parseNode.production(), region));
            }
        }

        private boolean isRecovery(ParseNode parseNode) {
            return parseNode.production().isRecovery()
                || (parseNode.production().sort() != null && parseNode.production().sort().contains("WATER"));
        }

    }
}
