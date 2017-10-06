package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestRepresentation;
import org.spoofax.jsglr2.JSGLR2Variants.Reducing;
import org.spoofax.jsglr2.JSGLR2Variants.StackRepresentation;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.benchmark.BenchmarkParserObserver;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2CharacterClassBenchmark extends BaseBenchmark {

	IParser<?, ?> parser;
	ActorObserver actorObserver;
	
	protected JSGLR2CharacterClassBenchmark(TestSet testSet) {
		super(testSet);
	}

	@Setup
    public void parserSetup() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException, URISyntaxException {
		IParseTable parseTable = ParseTableReader.read(testSetReader.getParseTableTerm());

        parser = JSGLR2Variants.getParser(parseTable, ParseForestRepresentation.Basic, StackRepresentation.Basic, Reducing.Basic);
		
		actorObserver = new ActorObserver();
		
		parser.attachObserver(actorObserver);

		try {
			for (Input input : inputs)
				parser.parseUnsafe(
				    input.content,
				    input.filename
				);
		} catch (ParseException e) {
			throw new IllegalStateException("setup of benchmark should not fail");
		}
	}
	
	class Actor {
		
		final public IState state;
		final public int character;
		
		public Actor(IState state, int character) {
			this.state = state;
			this.character = character;
		}
		
	}
	
	class ActorObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> extends BenchmarkParserObserver<StackNode, ParseForest> {
		
		public List<Actor> actors = new ArrayList<Actor>(); 
		
		public void actor(StackNode stack, int currentChar, Iterable<IAction> applicableActions) {
			actors.add(new Actor(stack.state, currentChar));
		}
		
	}
	
	@Benchmark
    public void benchmark(Blackhole bh) throws ParseException {
		for (Actor actor : ((ActorObserver<?, ?>) actorObserver).actors)
    			bh.consume(actor.state.applicableActions(actor.character));
    }
	
}
