package org.spoofax.jsglr2.parser.observing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class ParserVisualisationObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends ParserObserver<ParseForest, StackNode> {

    List<String> jsonTrace = new ArrayList<>();

    @Override public void parseStart(AbstractParse<ParseForest, StackNode> parse) {
        super.parseStart(parse);
        jsonTrace.clear();
        trace("{\"action\":\"start\",\"inputString\":\"" + parse.inputString + "\"}");
    }

    @Override public void parseCharacter(AbstractParse<ParseForest, StackNode> parse,
        Iterable<StackNode> activeStacks) {
        trace("{\"action\":\"parseCharacter\",\"character\":\"" + CharacterClassFactory.intToString(parse.currentChar)
            + "\",\"activeStacks\":" + stackQueueToString(activeStacks) + "}");
    }

    @Override public void createStackNode(StackNode stack) {
        super.createStackNode(stack);

        trace("{\"action\":\"createStackNode\",\"stackNumber\":" + id(stack) + ",\"stateNumber\":" + stack.state().id()
            + "}");
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        super.createStackLink(link);

        trace("{\"action\":\"createStackLink\",\"linkNumber\":" + id(link) + ",\"fromStack\":" + id(link.from)
            + ",\"toStack\":" + id(link.to) + ",\"parseNode\":" + id(link.parseForest) + ",\"descriptor\":\""
            + escape(link.parseForest.descriptor()) + "\"}");
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
        trace("{\"action\":\"rejectStackLink\",\"linkNumber\":" + id(link) + "}");
    }

    @Override public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
        // TODO forActorStacks has no proper toString, luckily this is not needed for visualisation
        // trace("{\"action\":\"forActorStacks\",\"forActor\":" + forActorStacks + "}");
    }

    @Override public void actor(StackNode stack, AbstractParse<ParseForest, StackNode> parse,
        Iterable<IAction> applicableActions) {
        trace("{\"action\":\"actor\",\"stackNumber\":" + id(stack) + "}");
    }

    @Override public void skipRejectedStack(StackNode stack) {
        trace("{\"action\":\"skipRejectedStack\",\"stackNumber\":" + id(stack) + "}");
    }

    @Override public void addForShifter(ForShifterElement<StackNode> forShifterElement) {
        trace("{\"action\":\"addForShifter\",\"stack\":" + id(forShifterElement.stack) + ", \"state\":"
            + forShifterElement.state.id() + "}");
    }

    @Override public void doLimitedReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack,
        IReduce reduce, StackLink<ParseForest, StackNode> link) {
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
        trace("{\"action\":\"reduce\",\"parseNodes\":" + parseForestListToString(parseNodes)
            + ",\"activeStackWithGotoState\":" + (activeStackWithGotoState != null ? id(activeStackWithGotoState) : -1)
            + "}");
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        trace("{\"action\":\"reduce\",\"parseNodes\":" + parseForestListToString(parseNodes)
            + ",\"activeStackWithGotoState\":-1}");
    }

    @Override public void directLinkFound(AbstractParse<ParseForest, StackNode> parse,
        StackLink<ParseForest, StackNode> directLink) {
        trace("{\"action\":\"directLinkFound\",\"linkNumber\":" + (directLink != null ? id(directLink) : -1) + "}");
    }

    @Override public void accept(StackNode acceptingStack) {
        trace("{\"action\":\"acceptStackNode\",\"stackNumber\":" + id(acceptingStack) + "}");
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        super.createParseNode(parseNode, production);

        trace("{\"action\":\"createParseNode\",\"nodeNumber\":" + id(parseNode) + ",\"production\":" + production.id()
            + ",\"term\":\"" + escape(production.descriptor()) + "\"}");
    }

    @Override public void createDerivation(IDerivation<ParseForest> derivation, IProduction production,
        ParseForest[] parseNodes) {
        super.createDerivation(derivation, production, parseNodes);

        trace("{\"action\":\"createDerivation\",\"nodeNumber\":" + id(derivation) + ",\"production\":" + production.id()
            + ",\"term\":\"" + escape(production.descriptor()) + "\",\"subTrees\":"
            + parseForestListToString(parseNodes) + "}");
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        super.createCharacterNode(characterNode, character);

        trace("{\"action\":\"createCharacterNode\",\"nodeNumber\":" + id(characterNode) + ",\"character\":\""
            + CharacterClassFactory.intToString(character) + "\"" + "}");
    }

    @Override public void addDerivation(ParseForest parseNode) {
        trace("{\"action\":\"addDerivation\",\"parseNode\":" + id(parseNode) + "}");
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {
        trace("{\"action\":\"shifter\",\"characterNode\":" + id(termNode) + ",\"elements\":"
            + forShifterQueueToString(forShifter) + "}");
    }

    @Override public void remark(String remark) {
        trace("{\"action\":\"remark\",\"remark\":\"" + remark + "\"}");
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
        trace("{\"action\":\"success\"}");
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        trace("{\"action\":\"failure\"}");
    }

    private void trace(String json) {
        jsonTrace.add(json);
    }

    private String toJson() {
        String res = "";

        for(String action : jsonTrace) {
            if(res.isEmpty())
                res += "\n\t" + action;
            else
                res += ",\n\t" + action;
        }

        return "[" + res + "\n]";
    }

    public void toJsonFile(String filename) throws FileNotFoundException {
        try(PrintWriter out = new PrintWriter(filename)) {
            out.println(toJson());
        }
    }

    private String escape(String string) {
        return string.replaceAll("\"", Matcher.quoteReplacement("\\\""));
    }

}
