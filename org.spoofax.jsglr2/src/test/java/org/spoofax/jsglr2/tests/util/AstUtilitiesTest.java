package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.util.AstUtilities;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public class AstUtilitiesTest {

    private AstUtilities astUtilities;
    private TermFactory termFactory;
    private TermReader termReader;

    public AstUtilitiesTest() {
        this.astUtilities = new AstUtilities();

        this.termFactory = new TermFactory();
        this.termReader = new TermReader(termFactory);
    }

    @Test
    public void testExpansionListExpansion() throws ParseError, ParseTableReadException, IOException {
        List<List<String>> elements = Arrays.asList(Arrays.asList("a"), Arrays.asList("b1", "b2"), Arrays.asList("c"));

        List<List<String>> expectedExpansion =
            Arrays.asList(Arrays.asList("a", "b1", "c"), Arrays.asList("a", "b2", "c"));

        List<List<String>> actualExpansion = astUtilities.expand(elements);

        assertEquals(expectedExpansion, actualExpansion);
    }

    @Test
    public void testExpansionAmbApplTermExpansion() throws ParseError, ParseTableReadException, IOException {
        IStrategoTerm ambiguousTem = termReader.parseFromString("Cons(\"a\",amb([\"b1\",\"b2\"]),\"c\")");

        List<IStrategoTerm> actualExpansion = astUtilities.expand(ambiguousTem);

        String expectedExpansion = "[Cons(\"a\",\"b1\",\"c\"), Cons(\"a\",\"b2\",\"c\")]";

        assertEquals(expectedExpansion, actualExpansion.toString());
    }

    @Test
    public void testExpansionAmbListTermExpansion() throws ParseError, ParseTableReadException, IOException {
        IStrategoTerm ambiguousTem = termReader.parseFromString("[\"a\",amb([\"b1\",\"b2\"]),\"c\"]");

        List<IStrategoTerm> actualExpansion = astUtilities.expand(ambiguousTem);

        String expectedExpansion = "[[\"a\",\"b1\",\"c\"], [\"a\",\"b2\",\"c\"]]";

        assertEquals(expectedExpansion, actualExpansion.toString());
    }

    @Test
    public void testExpansionAmbTupleTermExpansion() throws ParseError, ParseTableReadException, IOException {
        IStrategoTerm ambiguousTem = termReader.parseFromString("(\"a\",amb([\"b1\",\"b2\"]),\"c\")");

        List<IStrategoTerm> actualExpansion = astUtilities.expand(ambiguousTem);

        String expectedExpansion = "[(\"a\",\"b1\",\"c\"), (\"a\",\"b2\",\"c\")]";

        assertEquals(expectedExpansion, actualExpansion.toString());
    }

    @Test
    public void testExpansionAmbNested() throws ParseError, ParseTableReadException, IOException {
        IStrategoTerm ambiguousTem = termReader.parseFromString("amb([amb([\"a\",\"b\"]),\"c\"])");

        List<IStrategoTerm> actualExpansion = astUtilities.expand(ambiguousTem);

        String expectedExpansion = "[\"a\", \"b\", \"c\"]";

        assertEquals(expectedExpansion, actualExpansion.toString());
    }

    @Test
    public void testCountAmb() throws ParseError, ParseTableReadException, IOException {
        IStrategoTerm ambiguousTem = termReader.parseFromString("amb([amb([\"a\",\"b\"]),\"c\"])");

        assertEquals(2, astUtilities.ambCount(ambiguousTem));
    }

    @Test
    public void testCountAmbShared() throws ParseError, ParseTableReadException, IOException {
        IStrategoTerm termA = termFactory.makeString("A");
        IStrategoTerm termB = termFactory.makeString("B");

        IStrategoTerm term1 = termFactory.makeInt(1);
        IStrategoTerm term2 = termFactory.makeInt(2);
        IStrategoTerm termAmb1or2 = termFactory.makeAppl(termFactory.makeConstructor("amb", 1),
            new IStrategoTerm[] { termFactory.makeList(new IStrategoTerm[] { term1, term2 }) });

        IStrategoTerm termTupleA1or2 = termFactory.makeTuple(new IStrategoTerm[] { termA, termAmb1or2 });
        IStrategoTerm termTuple1or2B = termFactory.makeTuple(new IStrategoTerm[] { termAmb1or2, termB });

        IStrategoTerm termAmbTop = termFactory.makeAppl(termFactory.makeConstructor("amb", 1),
            new IStrategoTerm[] { termFactory.makeList(new IStrategoTerm[] { termTupleA1or2, termTuple1or2B }) });

        List<IStrategoTerm> actualExpansion = astUtilities.expand(termAmbTop);

        String expectedExpansion = "[(\"A\",1), (\"A\",2), (1,\"B\"), (2,\"B\")]";

        assertEquals(expectedExpansion, actualExpansion.toString());

        assertEquals(3, astUtilities.ambCount(termAmbTop));
        assertEquals(2, astUtilities.ambCountShared(termAmbTop));
    }

    @Test
    public void testFlattenAmb() throws ParseError, ParseTableReadException, IOException {
        IStrategoTerm termA = termFactory.makeString("A");

        IStrategoTerm term1 = termFactory.makeInt(1);
        IStrategoTerm term2 = termFactory.makeInt(2);

        IStrategoTerm termAmb1or2 = termFactory.makeAppl(termFactory.makeConstructor("amb", 1),
            new IStrategoTerm[] { termFactory.makeList(new IStrategoTerm[] { term1, term2 }) });
        IStrategoTerm termAmbTop = termFactory.makeAppl(termFactory.makeConstructor("amb", 1),
            new IStrategoTerm[] { termFactory.makeList(new IStrategoTerm[] { termA, termAmb1or2 }) });

        List<IStrategoTerm> termAmbTopExpansion = astUtilities.expand(termAmbTop);

        String expectedExpansion = "[\"A\", 1, 2]";

        assertEquals("amb([\"A\",amb([1,2])])", termAmbTop.toString());
        assertEquals(expectedExpansion, termAmbTopExpansion.toString());

        IStrategoTerm termAmbTopFlatten = astUtilities.ambFlatten(termAmbTop);
        List<IStrategoTerm> termAmbTopFlattenExpansion = astUtilities.expand(termAmbTopFlatten);

        assertEquals("amb([\"A\",1,2])", termAmbTopFlatten.toString());
        assertEquals(expectedExpansion, termAmbTopFlattenExpansion.toString());
    }

}
