package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.Term.intAt;
import static org.spoofax.terms.Term.isTermInt;
import static org.spoofax.terms.Term.javaInt;
import static org.spoofax.terms.Term.termAt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.actions.Accept;
import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.Goto;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.Reduce;
import org.spoofax.jsglr2.actions.ReduceLookahead;
import org.spoofax.jsglr2.actions.Shift;
import org.spoofax.jsglr2.characters.ICharacterClassFactory;
import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.states.IState;
import org.spoofax.jsglr2.states.IStateFactory;
import org.spoofax.jsglr2.states.State;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

import io.usethesource.capsule.Set;
import io.usethesource.capsule.util.stream.CapsuleCollectors;

public class ParseTableReader {

    ICharacterClassFactory characterClassFactory;
    IStateFactory stateFactory;

    public ParseTableReader() {
        this.characterClassFactory = ICharacters.factory();
        this.stateFactory = new StateFactory();
    }

    public ParseTableReader(ICharacterClassFactory characterClassFactory) {
        this.characterClassFactory = characterClassFactory;
        this.stateFactory = new StateFactory();
    }

    public ParseTableReader(IStateFactory stateFactory) {
        this.characterClassFactory = ICharacters.factory();
        this.stateFactory = stateFactory;
    }

    public ParseTableReader(ICharacterClassFactory characterClassFactory, IStateFactory stateFactory) {
        this.characterClassFactory = characterClassFactory;
        this.stateFactory = stateFactory;
    }

    /*
     * Reads a parse table from a term. The format consists of a tuple of 5: (1) version number (not used), (2) start
     * state number, (3) list of productions (i.e. labels), (4) list of states and (5) list of priorities (not used
     * since priorities are now encoded in the parse table itself during parser generation and do not have to be
     * implemented separately during parsing).
     */
    public IParseTable read(IStrategoTerm pt) throws ParseTableReadException {
        int startStateNumber = intAt(pt, 1);
        IStrategoList productionsTermList = termAt(pt, 2);
        IStrategoList statesTermList = termAt(termAt(pt, 3), 0);

        IProduction[] productions = readProductions(productionsTermList);
        IState[] states = readStates(statesTermList, productions);

        markRejectableStates(states);

        return new ParseTable(states, startStateNumber);
    }

    public IParseTable read(InputStream inputStream) throws ParseTableReadException, ParseError, IOException {
        TermReader termReader = new TermReader(new TermFactory());

        IStrategoTerm parseTableTerm = termReader.parseFromStream(inputStream);

        return read(parseTableTerm);
    }

    private Production[] readProductions(IStrategoList productionsTermList) throws ParseTableReadException {
        int productionsCount = productionsTermList.getSubtermCount();

        Production[] productions = new Production[257 + productionsCount];

        for(IStrategoTerm numberedProductionTerm : productionsTermList) {
            Production production = ProductionReader.read(numberedProductionTerm);

            productions[production.productionNumber()] = production;
        }

        return productions;
    }

    private IState[] readStates(IStrategoList statesTermList, IProduction[] productions)
        throws ParseTableReadException {
        int stateCount = statesTermList.getSubtermCount();

        IState[] states = new IState[stateCount];

        for(IStrategoTerm stateTerm : statesTermList) {
            IStrategoNamed stateTermNamed = (IStrategoNamed) stateTerm;

            int stateNumber = intAt(stateTermNamed, 0);

            IStrategoList gotosTermList = termAt(stateTermNamed, 1);
            IStrategoList actionsTermList = termAt(stateTermNamed, 2);

            IGoto[] gotos = readGotos(gotosTermList);
            ActionsPerCharacterClass[] actions = readActions(actionsTermList, productions);

            states[stateNumber] = stateFactory.from(stateNumber, gotos, actions);
        }

        return states;
    }

    private IGoto[] readGotos(IStrategoList gotosTermList) {
        int gotoCount = gotosTermList.size();

        IGoto[] gotos = new IGoto[gotoCount];

        for(int i = 0; i < gotoCount; i++) {
            IStrategoNamed gotoTermNamed = (IStrategoNamed) gotosTermList.getSubterm(i);

            IStrategoList productionsTermList = termAt(gotoTermNamed, 0);
            int[] productionNumbers = readGotoProductions(productionsTermList);

            int gotoStateNumber = intAt(gotoTermNamed, 1);

            gotos[i] = new Goto(productionNumbers, gotoStateNumber);
        }

        return gotos;
    }

    private int[] readGotoProductions(IStrategoList productionsTermList) {
        int productionNumbersCount = productionsTermList.size();

        int[] productionNumbers = new int[productionNumbersCount];

        for(int i = 0; i < productionNumbersCount; i++) {
            IStrategoTerm productionNumbersTerm = productionsTermList.getSubterm(i);

            if(isTermInt(productionNumbersTerm)) {
                int productionNumber = javaInt(productionNumbersTerm);

                productionNumbers[i] = productionNumber;
            }

            // productionNumbersTerm can also be a range representing character classes. That is a remainder of parse
            // table generation (representing transitions between states). We can ignore them here since parsing only
            // looks up gotos after a reduction and than uses the production that is used for the reduction the retrieve
            // the goto action, not a character.
        }

        return productionNumbers;
    }

    private ActionsPerCharacterClass[] readActions(IStrategoList characterActionsTermList, IProduction[] productions)
        throws ParseTableReadException {
        int characterClassesWithActionsCount = characterActionsTermList.size();

        List<ActionsPerCharacterClass> actionsForCharacterClasses =
            new ArrayList<ActionsPerCharacterClass>(characterClassesWithActionsCount);

        for(IStrategoTerm charactersActionsTerm : characterActionsTermList) {
            IStrategoNamed charactersActionsTermNamed = (IStrategoNamed) charactersActionsTerm;

            IStrategoList charactersTermList = (IStrategoList) termAt(charactersActionsTermNamed, 0);
            IStrategoList actionsTermList = (IStrategoList) termAt(charactersActionsTermNamed, 1);

            ICharacters characters = readCharacters(charactersTermList);
            IAction[] actions = readActionsForCharacters(actionsTermList, productions);

            actionsForCharacterClasses.add(new ActionsPerCharacterClass(characters, actions));
        }

        return actionsForCharacterClasses.toArray(new ActionsPerCharacterClass[actionsForCharacterClasses.size()]);
    }

    private ICharacters readCharacters(IStrategoList charactersTermList) {
        ICharacters characters = null;

        for(IStrategoTerm charactersTerm : charactersTermList) {
            ICharacters charactersForTerm = null;

            if(isTermInt(charactersTerm)) {
                charactersForTerm = characterClassFactory.fromSingle(javaInt(charactersTerm));
            } else {
                int from = intAt(charactersTerm, 0);
                int to = intAt(charactersTerm, 1);

                charactersForTerm = characterClassFactory.fromRange(from, to);
            }

            if(characters == null)
                characters = charactersForTerm;
            else if(charactersForTerm != null)
                characters = characterClassFactory.union(characters, charactersForTerm);
        }

        return characterClassFactory.finalize(characters);
    }

    private ICharacters[] readReduceLookaheadCharacters(IStrategoTerm followRestrictionTerm)
        throws ParseTableReadException {
        if("follow-restriction".equals(((IStrategoNamed) followRestrictionTerm).getName())) {
            IStrategoList followRestrictionCharactersList = (IStrategoList) termAt(followRestrictionTerm, 0);

            int lookaheadLength = followRestrictionCharactersList.size();

            // The length of this list equals the length of the lookahead
            ICharacters[] followRestrictionCharacters = new ICharacters[lookaheadLength];

            for(int i = 0; i < lookaheadLength; i++) {
                IStrategoTerm charactersTerm = followRestrictionCharactersList.getSubterm(i);

                followRestrictionCharacters[i] = readCharacters((IStrategoList) charactersTerm.getSubterm(0));
            }

            return followRestrictionCharacters;
        } else
            throw new ParseTableReadException("invalid follow restriction");
    }

    private IAction[] readActionsForCharacters(IStrategoList actionsTermList, IProduction[] productions)
        throws ParseTableReadException {
        int actionCount = actionsTermList.size();

        IAction[] actions = new IAction[actionCount];

        for(int i = 0; i < actionCount; i++) {
            IStrategoAppl actionTermAppl = (IStrategoAppl) actionsTermList.getSubterm(i);
            IAction action = null;

            if(actionTermAppl.getName().equals("reduce")) { // Reduce
                int arity = intAt(actionTermAppl, 0);
                int productionNumber = intAt(actionTermAppl, 1);
                ProductionType productionType = Production.typeFromInt(intAt(actionTermAppl, 2));

                IProduction production = productions[productionNumber];

                if(actionTermAppl.getConstructor().getArity() == 3) { // Reduce without lookahead
                    action = new Reduce(production, productionType, arity);
                } else if(actionTermAppl.getConstructor().getArity() == 4) { // Reduce with lookahead
                    ICharacters[] followRestriction =
                        readReduceLookaheadCharacters(termAt((IStrategoList) termAt(actionTermAppl, 3), 0));

                    action = new ReduceLookahead(production, productionType, arity, followRestriction);
                }
            } else if(actionTermAppl.getName().equals("accept")) { // Accept
                action = Accept.SINGLETON;
            } else if(actionTermAppl.getName().equals("shift")) { // Shift
                int shiftState = intAt(actionTermAppl, 0);

                action = new Shift(shiftState);
            } else {
                throw new IllegalStateException("invalid action type");
            }

            actions[i] = action;
        }

        return actions;
    }

    // Mark states that are reachable by a reject production as rejectable. "Reachable" means the parser could
    // transition into such state by means of a goto action after reducing a production.
    private void markRejectableStates(IState[] states) {
        final Set.Immutable<Integer> rejectProductionIdentifiers = Stream.of(states)
            .flatMap(state -> Stream.of(((State) state).actions())).filter(IAction::typeMatchesReduceOrReduceLookahead)
            .map(IReduce.class::cast).map(IReduce::production).filter(IProduction::typeMatchesReject)
            .map(IProduction::productionNumber).collect(CapsuleCollectors.toSet());

        final Set.Immutable<Integer> rejectableStateIdentifiers = Stream.of(states)
            .flatMap(state -> rejectProductionIdentifiers.stream().filter(rejectProductionIdentifier -> {
                return ((State) state).hasGoto(rejectProductionIdentifier);
            }).map(state::getGotoId)).collect(CapsuleCollectors.toSet());

        // A state is marked as rejectable if it is reachable by at least one reject production.
        rejectableStateIdentifiers.forEach(gotoId -> ((State) states[gotoId]).markRejectable());
    }

}
