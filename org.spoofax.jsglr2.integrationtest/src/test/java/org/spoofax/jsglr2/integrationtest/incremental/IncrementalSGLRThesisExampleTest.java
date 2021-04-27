package org.spoofax.jsglr2.integrationtest.incremental;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.metaborg.parsetable.ParseTableVariant;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.states.IState;
import org.metaborg.sdf2table.parsetable.LRItem;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.metaborg.sdf2table.parsetable.State;
import org.metaborg.util.iterators.Iterables2;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.imploder.incremental.IncrementalStrategoTermImploder;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.diff.JGitHistogramDiff;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForestManager;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.integrationtest.BaseTest;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.ParseNodeDescriptor;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.terms.util.TermUtils;

public class IncrementalSGLRThesisExampleTest extends BaseTestWithSdf3ParseTables {

    private static final String identifier1 = "ab =42 * 42";
    private static final String identifier2 = "ans =42 * 42";

    private static final String operatorLayout1 = "a = x + y + z";
    private static final String operatorLayout2 = "a = x + y * z";

    private static final String listLayout1 = "x = 3 y = 4 z = 5";
    private static final String listLayout2 = "x = 3 y = 4 z = 7";

    private static final String return1 = "return ab";
    private static final String return2 = "return ans";

    public IncrementalSGLRThesisExampleTest() {
        super("isglr-thesis-example.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testIdentifierChange() {
        return testIncrementalSuccessByExpansions(new String[] { identifier1, identifier2 }, new String[] { //
            "[Assign(\"ab\",Mul(Num(\"42\"),Num(\"42\")))]", //
            "[Assign(\"ans\",Mul(Num(\"42\"),Num(\"42\")))]" });
    }

    @TestFactory public Stream<DynamicTest> testIdentifierChangeReuse() {
        return testParseNodeReuse(identifier1, identifier2, //
            new ParseNodeDescriptor(0, 0, "LAYOUT", null), //
            new ParseNodeDescriptor(2, 1, "LAYOUT", null), //
            new ParseNodeDescriptor(3, 1, "\"=\"", null), //
            new ParseNodeDescriptor(4, 0, "LAYOUT", null), //
            new ParseNodeDescriptor(4, 7, "Exp", "Mul"));
    }

    @TestFactory public Stream<DynamicTest> testOperatorLayout() {
        return testIncrementalSuccessByExpansions(new String[] { operatorLayout1, operatorLayout2, "a = x * y * z" },
            new String[] { //
                "[Assign(\"a\",Add(Add(Var(\"x\"),Var(\"y\")),Var(\"z\")))]",
                "[Assign(\"a\",Add(Var(\"x\"),Mul(Var(\"y\"),Var(\"z\"))))]",
                "[Assign(\"a\",Mul(Mul(Var(\"x\"),Var(\"y\")),Var(\"z\")))]", });
    }

    @TestFactory public Stream<DynamicTest> testOperatorLayoutReuse() {
        return testParseNodeReuse(operatorLayout1, operatorLayout2, //
            new ParseNodeDescriptor(0, 0, "LAYOUT", null), //
            new ParseNodeDescriptor(0, 1, "ID", null), //
            new ParseNodeDescriptor(1, 1, "LAYOUT", null), //
            new ParseNodeDescriptor(2, 1, "\"=\"", null), //
            new ParseNodeDescriptor(3, 1, "LAYOUT", null), //
            new ParseNodeDescriptor(4, 1, "Exp", "Var"), //
            new ParseNodeDescriptor(6, 1, "\"+\"", null), //
            new ParseNodeDescriptor(7, 1, "LAYOUT", null), //
            new ParseNodeDescriptor(8, 1, "Exp", "Var"));
    }

    @TestFactory public Stream<DynamicTest> testListLayout() {
        return testIncrementalSuccessByExpansions(new String[] { listLayout1, listLayout2 }, new String[] { //
            "[Assign(\"x\",Num(\"3\")),Assign(\"y\",Num(\"4\")),Assign(\"z\",Num(\"5\"))]", //
            "[Assign(\"x\",Num(\"3\")),Assign(\"y\",Num(\"4\")),Assign(\"z\",Num(\"7\"))]", });
    }

    @TestFactory public Stream<DynamicTest> testListLayoutReuse() {
        return testParseNodeReuse(listLayout1, listLayout2, //
            new ParseNodeDescriptor(0, 0, "LAYOUT", null), //
            new ParseNodeDescriptor(0, 1, "ID", null), //
            new ParseNodeDescriptor(1, 1, "LAYOUT", null), //
            new ParseNodeDescriptor(2, 1, "\"=\"", null), //
            new ParseNodeDescriptor(3, 1, "LAYOUT", null), //
            new ParseNodeDescriptor(4, 1, "Exp", "Num"), //
            new ParseNodeDescriptor(7, 1, "LAYOUT", null), //
            new ParseNodeDescriptor(8, 1, "\"=\"", null), //
            new ParseNodeDescriptor(9, 0, "LAYOUT", null), //
            new ParseNodeDescriptor(10, 1, "Exp", "Num"), //
            new ParseNodeDescriptor(13, 0, "LAYOUT", null), //
            new ParseNodeDescriptor(14, 1, "\"=\"", null));
    }

    @TestFactory public Stream<DynamicTest> testReturn() {
        return testIncrementalSuccessByExpansions(new String[] { return1, return2 },
            new String[] { "[Return(Var(\"ab\"))]", "[Return(Var(\"ans\"))]", });
    }

    @TestFactory public Stream<DynamicTest> testReturnReuse() {
        return testParseNodeReuse(return1, return2, //
            new ParseNodeDescriptor(0, 0, "LAYOUT", null), //
            new ParseNodeDescriptor(6, 1, "LAYOUT", null));
    }

    public static void main(String[] args) throws Exception {
        IncrementalSGLRThesisExampleTest test = new IncrementalSGLRThesisExampleTest();

        // §3.1: First example
        printParseForestAndAST();
        logIncrementalParse(test, identifier1, identifier2);

        // §3.2.1: Example With Layout Between Operators
        logIncrementalParse(test, operatorLayout1, operatorLayout2);
        // §3.2.2: Example With Layout Between List Items
        logIncrementalParse(test, listLayout1, listLayout2);
        // §3.2.3: Example With Identifier Versus Keyword
        logIncrementalParse(test, return1, return2);
    }

    static void printParseForestAndAST() throws Exception {
        BaseTestWithSdf3ParseTables.setup();
        IParser<? extends IParseForest> parser = getParser(new IncrementalSGLRThesisExampleTest());

        System.out.println("# Parse Forest of input `" + identifier1 + "`:");
        ParseResult<? extends IParseForest> parse = parser.parse(identifier1);
        assert parse.isSuccess();
        IParseForest parseForest = ((ParseSuccess<?>) parse).parseResult;
        printLaTeX(parseForest);
        System.out.println();

        System.out.println("# AST of input `" + identifier1 + "`:");
        IncrementalStrategoTermImploder<IParseForest, ?, ?> imploder = new IncrementalStrategoTermImploder<>();
        ImplodeResult<TreeImploder.SubTree<IStrategoTerm>, ?, IStrategoTerm> ast =
            imploder.implode(identifier1, null, parseForest, null);
        printLaTeX(ast.ast());
        System.out.println();

        System.out.println("# List of edits between input `" + identifier1 + "` and `" + identifier2 + "`:");
        @SuppressWarnings({ "unchecked", "rawtypes" }) ProcessUpdates<?, ?> processUpdates =
            new ProcessUpdates<>(new IncrementalParseForestManager<>(((IObservableParser) parser).observing(), null));
        List<EditorUpdate> diff = new JGitHistogramDiff().diff(identifier1, identifier2);
        System.out.println(diff);
        System.out.println();

        System.out.println("# Parse Forest with processed updates:");
        IncrementalParseForest processedPF =
            processUpdates.processUpdates(identifier1, (IncrementalParseForest) parseForest, diff);
        printLaTeX(parseForest, processedPF);
        System.out.println();
    }

    // #### PRINT PARSE FOREST ####

    private static void printLaTeX(IParseForest parseForest) {
        System.out.print("\\");
        Map<IParseForest, Integer> ids = getIds(parseForest);
        printLaTeX(parseForest, ids, ids.size(), 0);
        System.out.println(";");
    }

    private static void printLaTeX(IParseForest parseForest, Map<IParseForest, Integer> ids, int originalSize,
        int indent) {
        Integer id = ids.get(parseForest);
        if(parseForest instanceof IParseNode) {
            IParseNode<?, ?> parseNode = (IParseNode<?, ?>) parseForest;
            IProduction production = parseNode.production();
            System.out.print("node" + (id > originalSize ? "[new]" : "") + " (" + id + ") {"
                + (production == null ? ""
                    : ((ParseTableProduction) production).getProduction().leftHand() + (production.constructor() == null
                        ? "" : "\\sdfoperator .\\sdfconstructor{" + production.constructor() + "}"))
                + "}");
            IParseForest[] children = getChildren(parseForest);
            if(children.length > 0)
                System.out.println();
            else
                System.out.print(" ");
            for(IParseForest child : children) {
                printIndent(indent);
                String opt = isExp(child) ? "[level distance=6em]"
                    : (isExp(parseForest)) ? "[level distance=3em]" : id == 50 ? "[sibling distance=4em]" : "";
                System.out.print("child" + opt + " { ");
                printLaTeX(child, ids, originalSize, indent + 8);
                System.out.println("}");
            }
            if(children.length > 0)
                printIndent(indent - 4);
        } else {
            System.out.print(
                "node[lex" + (id > originalSize ? ",new" : "") + "] (" + id + ") {" + parseForest.descriptor() + "} ");
        }
    }

    // #### PRINT AST ####

    private static void printLaTeX(IStrategoTerm ast) {
        System.out.print("\\");
        idx = 1;
        printLaTeX(ast, 0);
        System.out.println(";");
    }

    static int idx = 1;

    private static void printLaTeX(IStrategoTerm ast, int indent) {
        if(TermUtils.isAppl(ast) || TermUtils.isList(ast)) {
            if(TermUtils.isAppl(ast))
                System.out.print("node (" + idx++ + ") {" + TermUtils.toAppl(ast).getName() + "}");
            if(TermUtils.isList(ast))
                System.out.print("node (" + idx++ + ") {\\texttt{\\atermlist{[} \\ldots{} \\atermlist{]}}}");
            IStrategoTerm[] children = ast.getAllSubterms();
            if(children.length > 0)
                System.out.println();
            else
                System.out.print(" ");
            for(IStrategoTerm child : children) {
                printIndent(indent);
                System.out.print("child { ");
                printLaTeX(child, indent + 8);
                System.out.println("}");
            }
            if(children.length > 0)
                printIndent(indent - 4);
        } else {
            System.out.print("node[lex] (" + idx++ + ") {\\sdfstring{" + ast + "}} ");
        }
    }

    // #### PRINT UPDATED PARSE FOREST ####

    private static void printLaTeX(IParseForest parseForest, IParseForest parseForest2) {
        System.out.print("\\");
        Map<IParseForest, Integer> ids = getIds(parseForest);
        int originalSize = ids.size();
        updateIds(ids, parseForest2);
        printLaTeX(parseForest2, ids, originalSize, 0);
        System.out.println(";");
    }

    private static void updateIds(Map<IParseForest, Integer> ids, IParseForest parseForest) {
        Stack<IParseForest> stack = new Stack<>();
        Queue<IParseForest> queue = new LinkedList<>();
        queue.add(parseForest);
        while(!queue.isEmpty()) {
            IParseForest current = queue.poll();
            IParseForest[] children = getChildren(current);
            if(!ids.containsKey(current))
                stack.push(current);
            if(!ids.containsKey(current) && Arrays.stream(children).allMatch(c -> c instanceof ICharacterNode)) {
                for(int i = children.length - 1; i >= 0; i--) {
                    if(!ids.containsKey(children[i]))
                        stack.push(children[i]);
                }
            } else {
                queue.addAll(Arrays.asList(children));
            }
        }
        while(!stack.isEmpty()) {
            ids.put(stack.pop(), ids.size() + 1);
        }
    }

    // #### UTIL ####

    static IParser<? extends IParseForest> getParser(BaseTest test) throws Exception {
        return new ParserVariant(ActiveStacksRepresentation.standard(), ForActorStacksRepresentation.standard(),
            ParseForestRepresentation.Incremental, ParseForestConstruction.Full, StackRepresentation.Hybrid,
            Reducing.Incremental, false).getParser(test.getParseTable(new ParseTableVariant()).parseTable);
    }

    private static Map<IParseForest, Integer> getIds(IParseForest parseForest) {
        Map<IParseForest, Integer> ids = new HashMap<>();
        Stack<IParseForest> stack = new Stack<>();
        stack.add(parseForest);
        while(!stack.isEmpty()) {
            IParseForest current = stack.pop();
            ids.put(current, ids.size() + 1);
            IParseForest[] children = getChildren(current);
            for(int i = children.length - 1; i >= 0; i--) {
                stack.push(children[i]);
            }
        }
        return ids;
    }

    private static boolean isExp(IParseForest parseForest) {
        if(!(parseForest instanceof IParseNode))
            return false;
        IParseNode<?, ?> node = (IParseNode<?, ?>) parseForest;
        return node.production() != null && node.production().lhs().descriptor().contains("Exp")
            && parseForest.width() == 7;
    }

    private static void printIndent(int indent) {
        for(int i = 0; i < indent; i++)
            System.out.print(' ');
    }

    // #### LOG INCREMENTAL PARSE ####

    static void logIncrementalParse(BaseTestWithSdf3ParseTables test, String input1, String input2) {
        try {
            BaseTestWithSdf3ParseTables.setup();
            System.out.println("# Normalized grammar:");
            System.out.println(test.getNormalizedGrammar(false));
            System.out.println();

            logIncrementalParse((BaseTest) test, input1, input2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void logIncrementalParse(BaseTest test, String input1, String input2) throws Exception {
        @SuppressWarnings("unchecked") IParser<IParseForest> parser = (IParser<IParseForest>) getParser(test);
        // noinspection rawtypes,unchecked
        ((IObservableParser) parser).observing().attachObserver(new ShiftReduceBreakdownObserver());

        System.out.println("# Batch parse with input `" + input1 + "`:");
        ParseResult<IParseForest> parse = parser.parse(input1);
        assert parse.isSuccess();
        IParseForest parseForest = ((ParseSuccess<?>) parse).parseResult;
        System.out.println();

        System.out.println("# Incremental parse with input `" + input2 + "`:");
        parser.parse(input2, input1, parseForest);
        System.out.println();
        System.out.println();
    }

    @SuppressWarnings("rawtypes")
    static class ShiftReduceBreakdownObserver implements IParserObserver {
        @Override public void forActorStacks(IForActorStacks forActorStacks) {
            System.out.println("Starting parse round with stacks\n[" + Iterables2.stream(forActorStacks)
                .map(e -> stateToString(((IStackNode) e).state())).collect(Collectors.joining(", ")) + "]");
        }

        @Override public void actor(IStackNode stack, AbstractParseState parseState, Iterable applicableActions) {
            System.out.println("Actions for stack with state " + stateToString(stack.state()) + "\n["
                + Iterables2.stream(applicableActions).map(Object::toString).collect(Collectors.joining(", ")) + "]");
        }

        @Override public void shifter(IParseForest termNode, Queue forShifter) {
            System.out.println("Shifting");
            System.out.println(termNode);
        }

        @Override public void shift(AbstractParseState parseState, IStackNode originStack, IStackNode gotoStack) {
            IState state = originStack.state();
            System.out
                .println("Shifting from state " + stateToString(state) + " to " + stateToString(gotoStack.state()));
        }

        @Override public void reducer(AbstractParseState parseState, IStackNode activeStack, IStackNode originStack,
            IReduce reduce, IParseForest[] parseNodes, IStackNode gotoStack) {
            System.out.println("Reducing to " + reduce.production() + " from state "
                + stateToString(activeStack.state()) + ", origin state " + stateToString(originStack.state())
                + ", goto state " + stateToString(gotoStack.state()));
            System.out.println(Arrays.toString(parseNodes));
        }

        @Override public void breakDown(IIncrementalInputStack inputStack, BreakdownReason reason) {
            System.out.println("Breaking down because " + reason.message + " (" + reason + ")");
            System.out.println(inputStack.getNode());
        }
    }

    private static String stateToString(IState state) {
        if(state instanceof State)
            return state.id() + "("
                + ((State) state).getKernel().stream().map(LRItem::toString).collect(Collectors.joining(",")) + ")";
        else
            return "" + state.id();
    }
}
