package org.spoofax.jsglr2.recovery;

import java.util.Collection;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IParseReporter;
import org.spoofax.jsglr2.parser.ParseReporterFactory;
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

    @Override public Collection<Message> getMessages(ParseState parseState, ParseForest parseForest) {
        RecoveryMessagesParseForestVisitor<ParseForest, Derivation, ParseNode> parseForestVisitor =
            new RecoveryMessagesParseForestVisitor<>();

        parseForestManager.visit(parseState.request, parseForest, parseForestVisitor);

        return parseForestVisitor.messages;
    }
}
