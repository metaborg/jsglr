package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import java.io.IOException;
import java.net.URISyntaxException;

import org.openjdk.jmh.annotations.Setup;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.actions.IActionsFactory;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.states.IStateFactory;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;
import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.characterclasses.ICharacterClassFactory;
import org.metaborg.parsetable.IParseTable;

public abstract class JSGLR2DataStructureBenchmark extends BaseBenchmark {

    protected IParser<BasicParseForest, BasicStackNode<BasicParseForest>> parser;

    protected JSGLR2DataStructureBenchmark(TestSet testSet) {
        super(testSet);
    }

    @SuppressWarnings("unchecked")
    @Setup
    public void parserSetup() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        IParseTable parseTable = readParseTable(testSetReader.getParseTableTerm());

        parser = (IParser<BasicParseForest, BasicStackNode<BasicParseForest>>) JSGLR2Variants.getParser(parseTable,
            new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic,
                Reducing.Basic));

        postParserSetup();

        try {
            for(Input input : inputs)
                parser.parseUnsafe(input.content, input.filename, null);
        } catch(ParseException e) {
            throw new IllegalStateException("setup of benchmark should not fail");
        }
    }

    protected IParseTable readParseTable(IStrategoTerm parseTableTerm) throws ParseTableReadException {
    	IStateFactory stateFactory = new StateFactory(StateFactory.defaultActionsForCharacterRepresentation,
                StateFactory.defaultProductionToGotoRepresentation);
        IActionsFactory actionsFactory = new ActionsFactory(true);
        ICharacterClassFactory characterClassFactory = new CharacterClassFactory(true, true);
        
        return new ParseTableReader(characterClassFactory, actionsFactory, stateFactory).read(testSetReader.getParseTableTerm());
    }

    abstract protected void postParserSetup();

}
