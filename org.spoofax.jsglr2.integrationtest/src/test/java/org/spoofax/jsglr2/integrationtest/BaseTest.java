package org.spoofax.jsglr2.integrationtest;

import static java.util.Collections.sort;
import static org.junit.jupiter.api.Assertions.*;
import static org.spoofax.terms.util.TermUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.metaborg.parsetable.ParseTableVariant;
import mb.util.vfs2.resource.ResourceUtils;
import org.opentest4j.AssertionFailedError;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import mb.jsglr.shared.IToken;
import mb.jsglr.shared.ITokens;
import mb.jsglr.shared.ImploderAttachment;
import org.spoofax.jsglr2.*;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.WithParseTable;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.recovery.Reconstruction;
import org.spoofax.jsglr2.util.AstUtilities;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class BaseTest implements WithParseTable {

    private static TermReader termReader = new TermReader(new TermFactory());
    private static AstUtilities astUtilities = new AstUtilities();

    protected BaseTest() {
    }

    public TermReader getTermReader() {
        return termReader;
    }

    protected Iterable<ParseTableWithOrigin> getParseTablesOrFailOnException(ParseTableVariant variant) {
        try {
            return getParseTables(variant);
        } catch(Exception e) {
            e.printStackTrace();

            fail("Exception during reading parse table: " + e.getMessage());

            return null;
        }
    }

    protected static class TestVariant {

        public final IntegrationVariant variant;
        public final ParseTableWithOrigin parseTableWithOrigin;

        TestVariant(IntegrationVariant variant, ParseTableWithOrigin parseTableWithOrigin) {
            this.variant = variant;
            this.parseTableWithOrigin = parseTableWithOrigin;
        }

        String name() {
            return variant.name() + "(parseTableOrigin:" + parseTableWithOrigin.origin + ")";
        }

        IParser<? extends IParseForest> parser() {
            return variant.parser.getParser(parseTableWithOrigin.parseTable);
        }

        JSGLR2<IStrategoTerm> jsglr2() {
            return variant.jsglr2.getJSGLR2(parseTableWithOrigin.parseTable);
        }

    }

    protected Stream<TestVariant> getTestVariants(Predicate<TestVariant> filter) {
        List<TestVariant> testVariants = new ArrayList<>();

        for(IntegrationVariant variant : IntegrationVariant.testVariants()) {
            for(ParseTableWithOrigin parseTableWithOrigin : getParseTablesOrFailOnException(variant.parseTable)) {
                TestVariant testVariant = new TestVariant(variant, parseTableWithOrigin);

                // data-dependent, layout-sensitive, and composite parsers are incompatible with Aterm parse table
                if((variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.DataDependent)
                    || variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.LayoutSensitive)
                    || variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.Composite))
                    && parseTableWithOrigin.origin.equals(ParseTableOrigin.ATerm)) {
                    continue;
                }

                if(filter.test(testVariant))
                    testVariants.add(testVariant);
            }
        }

        return testVariants.stream();
    }

    protected Stream<TestVariant> getTestVariants() {
        return getTestVariants(testVariant -> true);
    }

    protected Predicate<TestVariant> isNonOptimizedParseForestVariant =
        variant -> variant.variant.parser.parseForestConstruction == ParseForestConstruction.Full;

    protected Stream<DynamicTest> testPerVariant(Stream<TestVariant> variants, Function<TestVariant, Executable> body) {
        return variants.map(variant -> DynamicTest.dynamicTest(variant.name(), body.apply(variant)));
    }

    protected Stream<DynamicTest> testParseSuccess(String inputString) {
        return testParseSuccess(inputString, getTestVariants());
    }

    protected JSGLR2Request getRequest(String inputString) {
        return configureRequest(new JSGLR2Request(inputString, "", null));
    }

    protected JSGLR2Request getRequest(String inputString, String filename, String startSymbol) {
        return configureRequest(new JSGLR2Request(inputString, filename, startSymbol));
    }

    protected JSGLR2Request configureRequest(JSGLR2Request request) {
        return request;
    }

    protected Stream<DynamicTest> testParseSuccess(String inputString, Stream<TestVariant> variants) {
        return testPerVariant(variants, variant -> () -> {
            ParseResult<?> parseResult = variant.parser().parse(getRequest(inputString));

            assertEquals(true, parseResult.isSuccess(), parseResult instanceof ParseFailure
                ? "Parsing failed: " + ((ParseFailure<?>) parseResult).failureCause.causeMessage() : "Parsing failed");
        });
    }

    protected Stream<DynamicTest> testParseFailure(String inputString) {
        return testParseFailure(inputString, getTestVariants());
    }

    protected Stream<DynamicTest> testParseFailure(String inputString, Stream<TestVariant> variants) {
        return testPerVariant(variants, variant -> () -> {
            ParseResult<?> parseResult = variant.parser().parse(getRequest(inputString));

            assertEquals(false, parseResult.isSuccess(), "Parsing should fail");
        });
    }

    protected Stream<DynamicTest> testSuccessByAstString(String inputString, String expectedOutputAstString, boolean serializeNormGrammar) {
        return testSuccess(inputString, expectedOutputAstString, null, false, serializeNormGrammar);
    }

    protected Stream<DynamicTest> testSuccessByAstString(String inputString, String expectedOutputAstString) {
        return testSuccess(inputString, expectedOutputAstString, null, false, false);
    }

    protected Stream<DynamicTest> testSuccessByExpansions(String inputString, String expectedOutputAstString) {
        return testSuccess(inputString, expectedOutputAstString, null, true, false);
    }

    protected Stream<DynamicTest> testIncrementalSuccessByExpansions(String[] inputStrings,
        String[] expectedOutputAstStrings) {
        return testIncrementalSuccess(inputStrings, expectedOutputAstStrings, null, true);
    }

    protected Stream<DynamicTest> testSuccessByAstString(String startSymbol, String inputString,
        String expectedOutputAstString) {
        return testSuccess(inputString, expectedOutputAstString, startSymbol, false, false);
    }

    protected Stream<DynamicTest> testSuccessByExpansions(String startSymbol, String inputString,
        String expectedOutputAstString) {
        return testSuccess(inputString, expectedOutputAstString, startSymbol, true, false);
    }

    private Stream<DynamicTest> testSuccess(String inputString, String expectedOutputAstString, String startSymbol,
        boolean equalityByExpansions, boolean serializeNormGrammar) {
        List<IStrategoTerm> previous = new ArrayList<>(1); // Variable used in lambda should be effectively final
        return testPerVariant(getTestVariants(), variant -> () -> {
            BaseTest.TestVariant variant2 = variant;
            IStrategoTerm actualOutputAst = testSuccess(variant, startSymbol, inputString);

//            if(previous.isEmpty())
//                previous.add(actualOutputAst);
//            else {
//                if(serializeNormGrammar) {
//                    variant.parseTableWithOrigin.parseTable.serializeNormGrammar(variant.name());
//                }
//                try {
//                    assertEqualAST("Variant '" + variant.name() + "' does not have the same AST as the first variant",
//                            previous.get(0), actualOutputAst);
//                } catch(AssertionFailedError e) {
//                    throw e;
//                }
//            }

            assertEqualAST("Incorrect AST", expectedOutputAstString, actualOutputAst, equalityByExpansions);
        });
    }

    protected Stream<DynamicTest> testAmbiguous(String inputString, boolean expectAmbiguous) {
        return testPerVariant(getTestVariants(), variant -> () -> {
            JSGLR2Result<IStrategoTerm> result = variant.jsglr2().parseResult(getRequest(inputString));

            assertTrue(result.isSuccess(), "Succeeding parse expected");

            JSGLR2Success<IStrategoTerm> success = (JSGLR2Success<IStrategoTerm>) result;

            if(expectAmbiguous)
                assertTrue(success.isAmbiguous(), "Result is not ambiguous");
            else
                assertFalse(success.isAmbiguous(), "Result is ambiguous");
        });
    }

    protected IStrategoTerm testSuccess(TestVariant variant, String startSymbol, String inputString) {
        return testSuccess("Parsing failed", "Imploding failed", variant.jsglr2(), "", startSymbol, inputString);
    }

    private IStrategoTerm testSuccess(String parseFailMessage, String implodeFailMessage, JSGLR2<IStrategoTerm> jsglr2,
        String fileName, String startSymbol, String inputString) {
        try {
            IStrategoTerm result = jsglr2.parseUnsafe(getRequest(inputString, fileName, startSymbol));

            // Fail here if imploding or tokenization failed
            assertNotNull(result, implodeFailMessage);

            return result;
        } catch(ParseException e) {
            // Fail here if parsing failed
            fail(parseFailMessage + ": " + e.getMessage());
        }
        return null;
    }

    protected Stream<DynamicTest> testReconstruction(String inputString, String expectedReconstruction,
        int expectedInsertions, int expectedDeletions) {
        return testPerVariant(getTestVariants(isNonOptimizedParseForestVariant), variant -> () -> {
            ParseResult<?> result = variant.jsglr2().parser().parse(getRequest(inputString));

            assertTrue(result.isSuccess(), "Succeeding parse expected");

            ParseSuccess<?> success = (ParseSuccess<?>) result;

            assertReconstruction(variant.parser(), success, expectedReconstruction, expectedInsertions,
                expectedDeletions);
        });
    }

    protected void assertReconstruction(IParser<?> parser, ParseSuccess<?> parseSuccess, String expectedReconstruction,
        int expectedInsertions, int expectedDeletions) {
        Reconstruction.Reconstructed reconstruction = Reconstruction.reconstruct(parser, parseSuccess);

        assertEquals(expectedReconstruction, reconstruction.inputString, "Incorrect reconstruction");
        assertEquals(expectedInsertions, reconstruction.insertions, "Incorrect #insertions");
        assertEquals(expectedDeletions, reconstruction.deletions, "Incorrect #deletions");
    }

    protected Predicate<TestVariant> isIncrementalVariant =
        testVariant -> testVariant.variant.parser.parseForestRepresentation == ParseForestRepresentation.Incremental;

    private Stream<DynamicTest> testIncrementalSuccess(String[] inputStrings, String[] expectedOutputAstStrings,
        String startSymbol, boolean equalityByExpansions) {
        return testIncrementalSuccess(inputStrings, expectedOutputAstStrings, startSymbol, equalityByExpansions,
            getTestVariants(isIncrementalVariant));
    }

    private Stream<DynamicTest> testIncrementalSuccess(String[] inputStrings, String[] expectedOutputAstStrings,
        String startSymbol, boolean equalityByExpansions, Stream<TestVariant> variants) {
        return testPerVariant(variants, variant -> () -> {
            JSGLR2<IStrategoTerm> jsglr2 = variant.jsglr2();
            IStrategoTerm actualOutputAst;
            String fileName = "" + System.nanoTime(); // To ensure the results will be cached
            for(int i = 0; i < expectedOutputAstStrings.length; i++) {
                String inputString = inputStrings[i];
                actualOutputAst = testSuccess("Parsing failed at update " + i, "Imploding failed at update " + i + ": ",
                    jsglr2, fileName, startSymbol, inputString);
                assertEqualAST("Incorrect AST at update " + i + ": ", expectedOutputAstStrings[i], actualOutputAst,
                    equalityByExpansions);
            }
        });
    }

    protected Stream<DynamicTest> testIncrementalSuccessByBatch(String... inputs) throws ParseError {
        IntegrationVariant batchVariant =
            new IntegrationVariant(new ParseTableVariant(), JSGLR2Variant.Preset.standard.variant);
        JSGLR2<IStrategoTerm> batchJSGLR2 =
            new TestVariant(batchVariant, getParseTablesOrFailOnException(batchVariant.parseTable).iterator().next())
                .jsglr2();

        String[] expectedOutputAstStrings = new String[inputs.length];
        for(int i = 0; i < inputs.length; i++) {
            JSGLR2Result<IStrategoTerm> result = batchJSGLR2.parseResult(inputs[i]);
            int finalI = i;
            assertTrue(result.isSuccess(), () -> "Batch parse for version " + finalI + " failed:\n"
                + ((JSGLR2Failure<IStrategoTerm>) result).parseFailure.exception());
            expectedOutputAstStrings[i] = ((JSGLR2Success<IStrategoTerm>) result).ast.toString();
        }

        return testIncrementalSuccessByExpansions(inputs, expectedOutputAstStrings);
    }

    protected void assertEqualAST(String message, IStrategoTerm expected, IStrategoTerm actual) {
        assertEqualAST(message, expected, actual, expected, actual);
    }

    private void assertEqualAST(String message, IStrategoTerm expected, IStrategoTerm actual, IStrategoTerm e,
        IStrategoTerm a) {
        compareAttachments(message, e, a);

        IStrategoTerm[] subTermsE = e.getAllSubterms();
        IStrategoTerm[] subTermsA = a.getAllSubterms();
        if(isAppl(e) && toAppl(e).getName().equals("amb") && isAppl(a) && toAppl(a).getName().equals("amb")) {
            // Check the list term that is the first argument of the amb()
            compareAttachments(message, subTermsE[0], subTermsA[0]);

            IStrategoTerm[] ambTermsE = subTermsE[0].getAllSubterms();
            IStrategoTerm[] ambTermsA = subTermsA[0].getAllSubterms();
            // Sort the sub terms of the list term
            Arrays.sort(ambTermsE, Comparator.comparing(Object::toString));
            Arrays.sort(ambTermsA, Comparator.comparing(Object::toString));

            compareSubTerms(message, expected, actual, ambTermsE, ambTermsA);
        } else {
            compareSubTerms(message, expected, actual, subTermsE, subTermsA);
        }
    }

    private void compareSubTerms(String message, IStrategoTerm expected, IStrategoTerm actual,
        IStrategoTerm[] subTermsE, IStrategoTerm[] subTermsA) {
        if(subTermsA.length != subTermsE.length)
            fail(message + "\nExpected: " + expected + "\n  Actual: " + actual);
        for(int i = 0; i < subTermsA.length; i++) {
            assertEqualAST(message, expected, actual, subTermsE[i], subTermsA[i]);
        }
    }

    private void compareAttachments(String message, IStrategoTerm e, IStrategoTerm a) {
        if(!Objects.equals(e.getAnnotations(), a.getAnnotations()))
            fail(message + "\nExpected annotations: " + e.getAnnotations() + "\n  Actual annotations: "
                + a.getAnnotations() + "\n On tree: " + a);

        ImploderAttachment expectedAttachment = e.getAttachment(ImploderAttachment.TYPE);
        ImploderAttachment actualAttachment = a.getAttachment(ImploderAttachment.TYPE);
        if(!equalAttachment(expectedAttachment, actualAttachment)) {
            fail(message + "\nExpected attachment: " + expectedAttachment + " "
                + (expectedAttachment == null ? "null"
                    : printToken(expectedAttachment.getLeftToken()) + " - "
                        + printToken(expectedAttachment.getRightToken()))
                + "\n  Actual attachment: " + actualAttachment + " "
                + (actualAttachment == null ? "null" : printToken(actualAttachment.getLeftToken()) + " - "
                    + printToken(actualAttachment.getRightToken()))
                + "\nOn tree: " + a);
        }
    }

    private String printToken(IToken token) {
        if(token == null)
            return "null";
        return "<" + token.toString() + ";" + token.getKind() + ";o:" + token.getStartOffset() + " l:" + token.getLine()
            + " c:" + token.getColumn() + ";o:" + token.getEndOffset() + " l:" + token.getEndLine() + " c:"
            + token.getEndColumn() + ">";
    }

    private boolean equalAttachment(ImploderAttachment expectedAttachment, ImploderAttachment actualAttachment) {
        if(expectedAttachment == null)
            return actualAttachment == null;
        if(actualAttachment == null)
            return false;
        IToken expectedLeft = expectedAttachment.getLeftToken();
        IToken expectedRight = expectedAttachment.getRightToken();
        IToken actualLeft = actualAttachment.getLeftToken();
        IToken actualRight = actualAttachment.getRightToken();
        return Objects.equals(expectedAttachment.getSort(), actualAttachment.getSort())
            && expectedLeft.getKind() == actualLeft.getKind() && expectedRight.getKind() == actualRight.getKind()
            && expectedLeft.getStartOffset() == actualLeft.getStartOffset()
            && expectedRight.getStartOffset() == actualRight.getStartOffset()
            && expectedLeft.getEndOffset() == actualLeft.getEndOffset()
            && expectedRight.getEndOffset() == actualRight.getEndOffset()
            && expectedLeft.getLine() == actualLeft.getLine() && expectedRight.getLine() == actualRight.getLine()
            && expectedLeft.getEndLine() == actualLeft.getEndLine()
            && expectedRight.getEndLine() == actualRight.getEndLine()
            && expectedLeft.getColumn() == actualLeft.getColumn()
            && expectedRight.getColumn() == actualRight.getColumn()
            && expectedLeft.getEndColumn() == actualLeft.getEndColumn()
            && expectedRight.getEndColumn() == actualRight.getEndColumn();
    }

    protected Stream<DynamicTest> testParseNodeReuse(String inputString1, String inputString2,
        ParseNodeDescriptor... parseNodeDescriptors) {
        return testPerVariant(getTestVariants(isIncrementalVariant), variant -> () -> {
            ParseNodeDescriptor[] filteredDescriptors = Arrays.stream(parseNodeDescriptors)
                .filter(parseNodeDescriptor -> parseNodeDescriptor.onlyForFullForest == null
                    || parseNodeDescriptor.onlyForFullForest == isNonOptimizedParseForestVariant.test(variant))
                .toArray(ParseNodeDescriptor[]::new);

            @SuppressWarnings("unchecked") IParser<IParseForest> parser = (IParser<IParseForest>) variant.parser();
            ParseResult<IParseForest> parse1 = parser.parse(inputString1);
            assertTrue(parse1.isSuccess(), "Parse 1 of " + inputString1 + " failed!");
            IParseForest parseForest1 = ((ParseSuccess<?>) parse1).parseResult;
            ParseResult<IParseForest> parse2 = parser.parse(inputString2, inputString1, parseForest1);
            assertTrue(parse2.isSuccess(), "Parse 2 of " + inputString2 + " failed!");
            IParseForest parseForest2 = ((ParseSuccess<?>) parse2).parseResult;

            Map<IParseForest, IParseNode<?, ?>> cache = populateCache(parseForest1);
            Map<IParseForest, Integer> offsets = calculateOffsets(parseForest1);
            List<IParseNode<?, ?>> reused = checkReuse(cache, parseForest2);
            assertEquals(filteredDescriptors.length, reused.size(),
                "Length of reused nodes not equal! Reused: [\n" + reused.stream()
                    .map(n -> "  offset " + offsets.get(n) + ", width " + n.width() + ", symbol "
                        + n.production().lhs().descriptor() + "." + n.production().constructor() + ":\n    "
                        + n.toString().replaceAll("\n", "\n    "))
                    .collect(Collectors.joining("\n")) + "\n]");
            for(int i = 0; i < filteredDescriptors.length; i++) {
                IParseNode<?, ?> node = reused.get(i);
                assertEquals(filteredDescriptors[i].offset, (int) offsets.get(reused.get(i)),
                    "Offsets do not match for " + filteredDescriptors[i]);
                assertEquals(filteredDescriptors[i].width, node.width(),
                    "Width does not match for " + filteredDescriptors[i]);
                assertEquals(filteredDescriptors[i].sort, node.production().lhs().descriptor(),
                    "Sort does not match for " + filteredDescriptors[i]);
                assertEquals(filteredDescriptors[i].cons, node.production().constructor(),
                    "Cons does not match for " + filteredDescriptors[i]);
            }
        });
    }

    private Map<IParseForest, IParseNode<?, ?>> populateCache(IParseForest parseForest) {
        Map<IParseForest, IParseNode<?, ?>> cache = new IdentityHashMap<>();
        Queue<IParseForest> queue = new LinkedList<>();
        queue.add(parseForest);
        while(!queue.isEmpty()) {
            IParseForest current = queue.poll();
            if(current instanceof IParseNode) {
                cache.put(current, (IParseNode<?, ?>) current);
            }
            queue.addAll(Arrays.asList(getChildren(current)));
        }
        return cache;
    }

    private Map<IParseForest, Integer> calculateOffsets(IParseForest parseForest) {
        Map<IParseForest, Integer> offsets = new IdentityHashMap<>();
        offsets.put(parseForest, 0);
        Queue<IParseForest> queue = new LinkedList<>();
        queue.add(parseForest);
        while(!queue.isEmpty()) {
            IParseForest current = queue.poll();
            int offset = offsets.get(current);
            for(IParseForest child : getChildren(current)) {
                offsets.put(child, offset);
                offset += child.width();
                queue.add(child);
            }
        }
        return offsets;
    }

    private List<IParseNode<?, ?>> checkReuse(Map<IParseForest, IParseNode<?, ?>> cache, IParseForest parseForest) {
        List<IParseNode<?, ?>> reused = new ArrayList<>();
        Stack<IParseForest> stack = new Stack<>();
        stack.add(parseForest);
        while(!stack.isEmpty()) {
            IParseForest current = stack.pop();
            if(current instanceof IParseNode) {
                if(cache.containsKey(current)) {
                    reused.add((IParseNode<?, ?>) current);
                    continue;
                }
            }
            IParseForest[] children = getChildren(current);
            ArrayUtils.reverse(children);
            stack.addAll(Arrays.asList(children));
        }
        return reused;
    }

    private static IParseForest[] EMPTY_CHILDREN = new IParseForest[0];

    protected static IParseForest[] getChildren(IParseForest parseForest) {
        if(parseForest instanceof IParseNode)
            return getChildren(((IParseNode<?, ?>) parseForest));
        return EMPTY_CHILDREN;
    }

    private static IParseForest[] getChildren(IParseNode<?, ?> parseNode) {
        if(!parseNode.getDerivations().iterator().hasNext())
            return EMPTY_CHILDREN;
        return parseNode.getFirstDerivation().parseForests();
    }

    protected void assertEqualAST(String message, String expectedOutputAstString, IStrategoTerm actualOutputAst,
        boolean equalityByExpansions) {
        if(equalityByExpansions) {
            IStrategoTerm expectedOutputAst = termReader.parseFromString(expectedOutputAstString);

            assertEqualTermExpansions(message, expectedOutputAst, actualOutputAst);
        } else {
            assertEquals(expectedOutputAstString, actualOutputAst.toString(), message);
        }
    }

    protected static void assertEqualTermExpansions(IStrategoTerm expected, IStrategoTerm actual) {
        assertEqualTermExpansions(null, expected, actual);
    }

    protected static void assertEqualTermExpansions(String message, IStrategoTerm expected, IStrategoTerm actual) {
        List<String> expectedExpansion = toSortedStringList(astUtilities.expand(expected));
        List<String> actualExpansion = toSortedStringList(astUtilities.expand(actual));

        assertEquals(expectedExpansion, actualExpansion, message);
    }

    private static List<String> toSortedStringList(List<IStrategoTerm> astExpansion) {
        List<String> result = new ArrayList<>(astExpansion.size());

        for(IStrategoTerm ast : astExpansion) {
            result.add(ast.toString());
        }

        sort(result);

        return result;
    }

    protected Stream<DynamicTest> testTokens(String inputString, List<TokenDescriptor> expectedTokens) {
        return testTokens(inputString, expectedTokens, getTestVariants());
    }

    protected Stream<DynamicTest> testTokens(String inputString, List<TokenDescriptor> expectedTokens,
        List<TokenDescriptor> expectedAmbiguousTokens) {
        return testTokens(inputString, expectedTokens, expectedAmbiguousTokens, getTestVariants());
    }

    protected Stream<DynamicTest> testTokens(String inputString, List<TokenDescriptor> expectedTokens,
        Stream<TestVariant> testVariants) {
        return testTokens(inputString, expectedTokens, expectedTokens, testVariants);
    }

    protected Stream<DynamicTest> testTokens(String inputString, List<TokenDescriptor> expectedTokens,
        List<TokenDescriptor> expectedAllTokens, Stream<TestVariant> variants) {
        return testPerVariant(variants, variant -> () -> {
            JSGLR2Result<IStrategoTerm> jsglr2Result = variant.jsglr2().parseResult(getRequest(inputString));

            assertTrue(jsglr2Result.isSuccess(), "Parsing failed");

            JSGLR2Success<IStrategoTerm> jsglr2Success = (JSGLR2Success<IStrategoTerm>) jsglr2Result;

            IStrategoTerm rootAst = jsglr2Success.ast;
            String rootCons = isAppl(rootAst) ? toAppl(rootAst).getName() : isList(rootAst) ? "[]" : null;

            ITokens actualTokens = jsglr2Success.tokens;
            testTokens(inputString, expectedTokens, actualTokens, "regular", rootCons);

            if(expectedTokens != expectedAllTokens)
                testTokens(inputString, expectedAllTokens, actualTokens.allTokens(), "all (incl. ambiguous/empty)",
                    rootCons);

            testTokenAtOffset(inputString, expectedAllTokens, actualTokens);

            testTokenAfterBefore(inputString, expectedTokens, actualTokens);
        });
    }

    private void testTokenAtOffset(String inputString, List<TokenDescriptor> expectedAllTokens, ITokens actualTokens) {
        assertEquals(IToken.Kind.TK_RESERVED, actualTokens.getTokenAtOffset(0).getKind());
        for(int i = 1; i < inputString.length(); i++) {
            TokenDescriptor expectedToken = null;
            for(TokenDescriptor t : expectedAllTokens) {
                if(t.offset == i)
                    expectedToken = t;
                if(t.offset >= i)
                    break;
            }
            if(expectedToken == null)
                continue;
            assertEquals(expectedToken, TokenDescriptor.from(inputString, actualTokens.getTokenAtOffset(i)),
                "Token at offset " + i);
        }
        if(inputString.length() > 0)
            assertEquals(IToken.Kind.TK_EOF, actualTokens.getTokenAtOffset(inputString.length()).getKind());
    }

    private void testTokenAfterBefore(String inputString, List<TokenDescriptor> expectedTokens, ITokens actualTokens) {
        IToken actualToken = actualTokens.getTokenAtOffset(0); // start token
        for(TokenDescriptor expectedToken : expectedTokens) {
            actualToken = actualToken.getTokenAfter();
            assertNotNull(actualToken, "Token " + expectedToken + " is null");
            assertEquals(expectedToken, TokenDescriptor.from(inputString, actualToken), "TokenAfter");
        }
        actualToken = actualToken.getTokenAfter();
        assertEquals(IToken.Kind.TK_EOF, actualToken.getKind());
        assertNull(actualToken.getTokenAfter());
        for(int i = expectedTokens.size() - 1; i >= 0; i--) {
            TokenDescriptor expectedToken = expectedTokens.get(i);
            actualToken = actualToken.getTokenBefore();
            assertNotNull(actualToken, "Token " + expectedToken + " is null");
            assertEquals(expectedToken, TokenDescriptor.from(inputString, actualToken), "TokenBefore");
        }
        actualToken = actualToken.getTokenBefore();
        assertEquals(IToken.Kind.TK_RESERVED, actualToken.getKind());
        assertNull(actualToken.getTokenBefore());
    }

    protected void testTokens(String inputString, List<TokenDescriptor> expectedTokens, Iterable<IToken> tokens,
        String type, String rootCons) {
        String messageSuffix = " for " + type + " tokens";

        List<TokenDescriptor> actualTokens = new ArrayList<>();
        for(IToken token : tokens) {
            actualTokens.add(TokenDescriptor.from(inputString, token));
        }

        TokenDescriptor expectedStartToken = new TokenDescriptor("", IToken.Kind.TK_RESERVED, 0, 1, 1, null, rootCons);
        TokenDescriptor actualStartToken = actualTokens.get(0);

        assertEquals(expectedStartToken, actualStartToken, "Start token incorrect" + messageSuffix);

        Position endPosition = Position.atEnd(inputString);

        int endLine = endPosition.line;
        int endColumn = endPosition.column;

        TokenDescriptor expectedEndToken =
            new TokenDescriptor("", IToken.Kind.TK_EOF, inputString.length(), endLine, endColumn, null, rootCons);
        TokenDescriptor actualEndToken = actualTokens.get(actualTokens.size() - 1);

        List<TokenDescriptor> actualTokensWithoutStartAndEnd = actualTokens.subList(1, actualTokens.size() - 1);

        assertIterableEquals(expectedTokens, actualTokensWithoutStartAndEnd, "Token lists don't match" + messageSuffix);

        assertEquals(expectedEndToken, actualEndToken, "End token incorrect" + messageSuffix);
    }

    protected Stream<DynamicTest> testOrigins(String inputString, List<OriginDescriptor> expectedOrigins) {
        return testOrigins(inputString, expectedOrigins, getTestVariants());
    }

    protected Stream<DynamicTest> testOrigins(String inputString, List<OriginDescriptor> expectedOrigins,
        Stream<TestVariant> variants) {
        return testPerVariant(variants, variant -> () -> {
            JSGLR2Result<IStrategoTerm> jsglr2Result = variant.jsglr2().parseResult(getRequest(inputString));

            assertTrue(jsglr2Result.isSuccess(), "Parsing failed");

            IStrategoTerm ast = ((JSGLR2Success<IStrategoTerm>) jsglr2Result).ast;

            List<OriginDescriptor> actualOrigins = new ArrayList<>();

            Stack<IStrategoTerm> terms = new Stack<>();

            terms.push(ast);

            while(!terms.isEmpty()) {
                IStrategoTerm term = terms.pop();

                for(int i = term.getSubtermCount() - 1; i >= 0; i--)
                    terms.push(term.getSubterm(i));

                if(term instanceof IStrategoAppl) {
                    actualOrigins.add(OriginDescriptor.from(term));
                }
            }

            assertIterableEquals(expectedOrigins, actualOrigins, "Origin lists don't match");
        });
    }

    protected Stream<DynamicTest> testMessages(String inputString, List<MessageDescriptor> expectedMessages) {
        return testMessages(inputString, expectedMessages, getTestVariants(), null);
    }

    protected Stream<DynamicTest> testMessages(String inputString, List<MessageDescriptor> expectedMessages,
        Stream<TestVariant> variants) {
        return testMessages(inputString, expectedMessages, variants, null);
    }

    protected Stream<DynamicTest> testMessages(String inputString, List<MessageDescriptor> expectedMessages,
        String startSymbol) {
        return testMessages(inputString, expectedMessages, getTestVariants(), startSymbol);
    }

    protected Stream<DynamicTest> testMessages(String inputString, List<MessageDescriptor> expectedMessages,
        Stream<TestVariant> variants, String startSymbol) {
        return testPerVariant(variants, variant -> () -> {
            JSGLR2Result<?> jsglr2Result = variant.jsglr2().parseResult(getRequest(inputString, "", startSymbol));

            List<MessageDescriptor> actualMessages = new ArrayList<>();

            for(Message message : jsglr2Result.messages) {
                actualMessages.add(MessageDescriptor.from(message));
            }

            assertIterableEquals(expectedMessages, actualMessages, "Message lists don't match");
        });
    }

    protected String getFileAsString(String filename) throws IOException {
        return ResourceUtils.readJavaResource("samples/" + filename, StandardCharsets.UTF_8);
    }

    protected IStrategoTerm getFileAsAST(String filename) throws IOException {
        InputStream inputStream = ResourceUtils.resolveJavaResource("samples/" + filename).openStream();

        return termReader.parseFromStream(inputStream);
    }

    protected Stream<DynamicTest> concat(Stream<DynamicTest>... testStreams) {
        return Stream.of(testStreams).flatMap(Function.identity());
    }

}
