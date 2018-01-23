package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.Term.intAt;
import static org.spoofax.terms.Term.isTermInt;
import static org.spoofax.terms.Term.javaInt;
import static org.spoofax.terms.Term.termAt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.characterclasses.ICharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.ProductionType;
import org.metaborg.parsetable.actions.ActionType;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.characterclasses.ICharacterClass;
import org.metaborg.sdf2table.io.ParseTableGenerator;
import org.metaborg.sdf2table.parsetable.query.ActionsPerCharacterClass;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.actions.Goto;
import org.spoofax.jsglr2.actions.IActionsFactory;
import org.spoofax.jsglr2.states.IStateFactory;
import org.spoofax.jsglr2.states.State;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public class ParseTableReader {

    final ICharacterClassFactory characterClassFactory;
    final IActionsFactory actionsFactory;
    final IStateFactory stateFactory;

    public ParseTableReader(ICharacterClassFactory characterClassFactory, IActionsFactory actionsFactory,
        IStateFactory stateFactory) {
        this.characterClassFactory = characterClassFactory;
        this.actionsFactory = actionsFactory;
        this.stateFactory = stateFactory;
    }

    /*
     * Reads a parse table from a term. The format consists of a tuple of 5: (1) version number (not used), (2) start
     * state id, (3) list of productions (i.e. labels), (4) list of states and (5) list of priorities (not used since
     * priorities are now encoded in the parse table itself during parser generation and do not have to be implemented
     * separately during parsing).
     */
    public IParseTable read(IStrategoTerm pt) throws ParseTableReadException {
        int startStateId = intAt(pt, 1);
        IStrategoList productionsTermList = termAt(pt, 2);
        IStrategoList statesTermList = termAt(termAt(pt, 3), 0);

        IProduction[] productions = readProductions(productionsTermList);
        IState[] states = readStates(statesTermList, productions);

        markRejectableStates(states);

        return new ParseTable(states, startStateId);
    }

    public IParseTable read(IStrategoTerm pt, FileObject persistedTable) throws Exception {

        org.metaborg.sdf2table.parsetable.ParseTable parseTableFromSerializable;

        ParseTableGenerator ptg = new ParseTableGenerator(persistedTable);

        // read persisted normalized grammar
        parseTableFromSerializable = ptg.getParseTable();

        // TODO: markRejectableStates(states);

        return parseTableFromSerializable;
    }

    public IParseTable read(InputStream inputStream) throws ParseTableReadException, ParseError, IOException {
        TermReader termReader = new TermReader(new TermFactory());

        IStrategoTerm parseTableTerm = termReader.parseFromStream(inputStream);

        return read(parseTableTerm);
    }

    private Production[] readProductions(IStrategoList productionsTermList) throws ParseTableReadException {
        int productionCount = productionsTermList.getSubtermCount();

        Production[] productions = new Production[257 + productionCount];

        for(IStrategoTerm productionWithIdTerm : productionsTermList) {
            Production production = ProductionReader.read(productionWithIdTerm);

            productions[production.id()] = production;
        }

        return productions;
    }

    private IState[] readStates(IStrategoList statesTermList, IProduction[] productions)
        throws ParseTableReadException {
        int stateCount = statesTermList.getSubtermCount();

        IState[] states = new IState[stateCount];

        for(IStrategoTerm stateTerm : statesTermList) {
            IStrategoNamed stateTermNamed = (IStrategoNamed) stateTerm;

            int stateId = intAt(stateTermNamed, 0);

            IStrategoList gotosTermList = termAt(stateTermNamed, 1);
            IStrategoList actionsTermList = termAt(stateTermNamed, 2);

            IGoto[] gotos = readGotos(gotosTermList);
            ActionsPerCharacterClass[] actions = readActions(actionsTermList, productions);

            IState state = stateFactory.from(stateId, gotos, actions);

            states[state.id()] = state;
        }

        return states;
    }

    private IGoto[] readGotos(IStrategoList gotosTermList) {
        int gotoCount = gotosTermList.size();

        IGoto[] gotos = new IGoto[gotoCount];

        for(int i = 0; i < gotoCount; i++) {
            IStrategoNamed gotoTermNamed = (IStrategoNamed) gotosTermList.getSubterm(i);

            IStrategoList productionsTermList = termAt(gotoTermNamed, 0);
            int[] productionIds = readGotoProductions(productionsTermList);

            int gotoStateId = intAt(gotoTermNamed, 1);

            gotos[i] = new Goto(productionIds, gotoStateId);
        }

        return gotos;
    }

    private int[] readGotoProductions(IStrategoList productionsTermList) {
        int productionCount = productionsTermList.size();

        int[] productionIds = new int[productionCount];

        for(int i = 0; i < productionCount; i++) {
            IStrategoTerm productionIdsTerm = productionsTermList.getSubterm(i);

            if(isTermInt(productionIdsTerm)) {
                int productionId = javaInt(productionIdsTerm);

                productionIds[i] = productionId;
            }

            // productionIdsTerm can also be a range representing character classes. That is a remainder of parse
            // table generation (representing transitions between states). We can ignore them here since parsing only
            // looks up gotos after a reduction and than uses the production that is used for the reduction the retrieve
            // the goto action, not a character.
        }

        return productionIds;
    }

    private ActionsPerCharacterClass[] readActions(IStrategoList characterClassActionsTermList,
        IProduction[] productions) throws ParseTableReadException {
        int characterClassesWithActionsCount = characterClassActionsTermList.size();

        List<ActionsPerCharacterClass> actionsPerCharacterClasses =
            new ArrayList<ActionsPerCharacterClass>(characterClassesWithActionsCount);

        for(IStrategoTerm characterClassActionsTerm : characterClassActionsTermList) {
            IStrategoNamed characterClassActionsTermNamed = (IStrategoNamed) characterClassActionsTerm;

            IStrategoList characterClassTermList = (IStrategoList) termAt(characterClassActionsTermNamed, 0);
            IStrategoList actionsTermList = (IStrategoList) termAt(characterClassActionsTermNamed, 1);

            ICharacterClass characterClass = readCharacterClass(characterClassTermList);
            IAction[] actions = readActionsForCharacterClass(actionsTermList, productions);

            actionsPerCharacterClasses.add(new ActionsPerCharacterClass(characterClass, actions));
        }

        return actionsPerCharacterClasses.toArray(new ActionsPerCharacterClass[actionsPerCharacterClasses.size()]);
    }

    private IAction[] readActionsForCharacterClass(IStrategoList actionsTermList, IProduction[] productions)
        throws ParseTableReadException {
        int actionCount = actionsTermList.size();

        IAction[] actions = new IAction[actionCount];

        for(int i = 0; i < actionCount; i++) {
            IStrategoAppl actionTermAppl = (IStrategoAppl) actionsTermList.getSubterm(i);
            IAction action = null;

            if(actionTermAppl.getName().equals("reduce")) { // Reduce
                int arity = intAt(actionTermAppl, 0);
                int productionId = intAt(actionTermAppl, 1);
                ProductionType productionType = Production.typeFromInt(intAt(actionTermAppl, 2));

                IProduction production = productions[productionId];

                if(actionTermAppl.getConstructor().getArity() == 3) { // Reduce without lookahead
                    action = actionsFactory.getReduce(production, productionType, arity);
                } else if(actionTermAppl.getConstructor().getArity() == 4) { // Reduce with lookahead
                    ICharacterClass[] followRestriction =
                        readReduceLookaheadFollowRestriction(termAt((IStrategoList) termAt(actionTermAppl, 3), 0));

                    action = actionsFactory.getReduceLookahead(production, productionType, arity, followRestriction);
                }
            } else if(actionTermAppl.getName().equals("accept")) { // Accept
                action = actionsFactory.getAccept();
            } else if(actionTermAppl.getName().equals("shift")) { // Shift
                int shiftStateId = intAt(actionTermAppl, 0);

                action = actionsFactory.getShift(shiftStateId);
            } else {
                throw new IllegalStateException("invalid action type");
            }

            actions[i] = action;
        }

        return actions;
    }

    private ICharacterClass readCharacterClass(IStrategoList characterClassTermList) {
        ICharacterClass characterClass = null;

        for(IStrategoTerm characterClassTerm : characterClassTermList) {
            ICharacterClass characterClassForTerm = null;

            if(isTermInt(characterClassTerm)) {
                characterClassForTerm = characterClassFactory.fromSingle(javaInt(characterClassTerm));
            } else {
                int from = intAt(characterClassTerm, 0);
                int to = intAt(characterClassTerm, 1);

                characterClassForTerm = characterClassFactory.fromRange(from, to);
            }

            if(characterClass == null)
                characterClass = characterClassForTerm;
            else if(characterClassForTerm != null)
                characterClass = characterClassFactory.union(characterClass, characterClassForTerm);
        }

        return characterClassFactory.finalize(characterClass);
    }

    private ICharacterClass[] readReduceLookaheadFollowRestriction(IStrategoTerm followRestrictionTerm)
        throws ParseTableReadException {
        if("follow-restriction".equals(((IStrategoNamed) followRestrictionTerm).getName())) {
            IStrategoList followRestrictionCharacterClassList = (IStrategoList) termAt(followRestrictionTerm, 0);

            int lookaheadLength = followRestrictionCharacterClassList.size();

            // The length of this list equals the length of the lookahead
            ICharacterClass[] followRestrictionCharacterClasses = new ICharacterClass[lookaheadLength];

            for(int i = 0; i < lookaheadLength; i++) {
                IStrategoTerm characterClassTerm = followRestrictionCharacterClassList.getSubterm(i);

                followRestrictionCharacterClasses[i] =
                    readCharacterClass((IStrategoList) characterClassTerm.getSubterm(0));
            }

            return followRestrictionCharacterClasses;
        } else
            throw new ParseTableReadException("invalid follow restriction");
    }

    // Mark states that are reachable by a reject production as rejectable. "Reachable" means the parser could
    // transition into such state by means of a goto action after reducing a production.
    private void markRejectableStates(IState[] states) {
        final Set<Integer> rejectProductionIds =
            Stream.of(states).flatMap(state -> Stream.of(((State) state).actions()))
                .filter(this::isReduceOrReduceLookahead).map(IReduce.class::cast).filter(IReduce::isRejectProduction)
                .map(IReduce::production).map(IProduction::id).collect(Collectors.toSet());

        final Set<Integer> rejectableStateIds =
            Stream.of(states).flatMap(state -> rejectProductionIds.stream().filter(rejectProductionId -> {
                return ((State) state).hasGoto(rejectProductionId);
            }).map(state::getGotoId)).collect(Collectors.toSet());

        // A state is marked as rejectable if it is reachable by at least one reject production.
        rejectableStateIds.forEach(gotoId -> ((State) states[gotoId]).markRejectable());
    }

    private boolean isReduceOrReduceLookahead(IAction action) {
        return action.actionType() == ActionType.REDUCE || action.actionType() == ActionType.REDUCE_LOOKAHEAD;
    }

}
