package org.spoofax.jsglr2.parsetable;

import static org.spoofax.terms.Term.intAt;
import static org.spoofax.terms.Term.isTermInt;
import static org.spoofax.terms.Term.javaInt;
import static org.spoofax.terms.Term.termAt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoNamed;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.actions.Accept;
import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.Goto;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.Reduce;
import org.spoofax.jsglr2.actions.ReduceLookahead;
import org.spoofax.jsglr2.actions.Shift;
import org.spoofax.jsglr2.characters.Characters;
import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.characters.RangesCharacterSet;
import org.spoofax.jsglr2.characters.SingleCharacter;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public class ParseTableReader {

	/*
	 * Reads a parse table from a term. The format consists of a tuple of 4:
	 *  - version number (not used)
	 *  - start state number
	 *  - list of productions (i.e. labels)
	 *  - list of states
	 *  - list of priorities (not used since priorities are now encoded in the parse table itself and do not have to be implemented separately during parsing)
	 */
	public static IParseTable read(IStrategoTerm pt) throws ParseTableReadException {
		int startStateNumber = intAt(pt, 1);
		IStrategoList productionsTermList = termAt(pt, 2);
        IStrategoNamed statesTerm = termAt(pt, 3);
        
        Production[] productions = readProductions(productionsTermList);
        State[] states = readStates(statesTerm, productions);

        markRejectableStates(states);
        
        return new ParseTable(productions, states, startStateNumber);
	}
	
	public static IParseTable read(InputStream inputStream) throws ParseTableReadException, ParseError, IOException {
		TermFactory factory = new TermFactory();
		TermReader termReader = new TermReader(factory);
		
		IStrategoTerm parseTableTerm = termReader.parseFromStream(inputStream);
		
		return read(parseTableTerm);
	}
	
	private static Production[] readProductions(IStrategoList productionsTermList) throws ParseTableReadException {
		int productionsCount = productionsTermList.getSubtermCount();
		
		Production[] productions = new Production[257 + productionsCount];
		
		for (IStrategoTerm numberedProductionTerm : productionsTermList) {
			Production production = ProductionReader.read((IStrategoNamed) numberedProductionTerm);
			
			productions[production.productionNumber()] = production;
		}
		
		return productions;
	}
	
	private static State[] readStates(IStrategoNamed statesTermNamed, IProduction[] productions) throws ParseTableReadException {
		IStrategoList statesTermList = termAt(statesTermNamed, 0);
		int stateCount = statesTermList.getSubtermCount();
		
		State[] states = new State[stateCount];
		
		for (IStrategoTerm stateTerm : statesTermList) {
			IStrategoNamed stateTermNamed = (IStrategoNamed) stateTerm;
			
			int stateNumber = intAt(stateTermNamed, 0);

			IStrategoList gotosTermList = (IStrategoList) termAt(stateTermNamed, 1);
			IStrategoList actionsTermList = (IStrategoList) termAt(stateTermNamed, 2);
			
			IGoto[] gotos = readGotos(gotosTermList);
			IAction[] actions = readActions(actionsTermList, productions);
			
			states[stateNumber] = new State(stateNumber, gotos, actions);
		}
		
		return states;
	}
	
	private static IGoto[] readGotos(IStrategoList gotosTermList) {
		int gotoCount = gotosTermList.getSubtermCount();
		
		IGoto[] gotos = new IGoto[gotoCount];
		
		int i = 0;
		
		for (IStrategoTerm gotoTerm : gotosTermList) {
			IStrategoNamed gotoTermNamed = (IStrategoNamed) gotoTerm;
			
			IStrategoList productionsTermList = termAt(gotoTermNamed, 0);
			int[] productionNumbers = readGotoProductions(productionsTermList);
			
			int gotoStateNumber = intAt(gotoTermNamed, 1);
			
			gotos[i++] = new Goto(productionNumbers, gotoStateNumber);
		}
		
		return gotos;
	}
	
	private static int[] readGotoProductions(IStrategoList productionsTermList) {
		ArrayList<Integer> productionNumbers = new ArrayList<Integer>();
		
		for (IStrategoTerm productionNumbersTerm : productionsTermList) {
			if (isTermInt(productionNumbersTerm)) {
				int productionNumber = javaInt(productionNumbersTerm);
				
				productionNumbers.add(productionNumber);
			} else {
				int productionNumberRangeFrom = intAt(productionNumbersTerm, 0);
				int productionNumberRangeTo = intAt(productionNumbersTerm, 1);
				
				for (int productionNumber = productionNumberRangeFrom; productionNumber <= productionNumberRangeTo; productionNumber++)
					productionNumbers.add(productionNumber);
			}
		}
		
		int[] res = new int[productionNumbers.size()];
		
		for (int i = 0; i < res.length; i++)
			res[i] = productionNumbers.get(i);
		
		return res;
	}
	
	private static IAction[] readActions(IStrategoList characterActionsTermList, IProduction[] productions) throws ParseTableReadException {
		int actionCount = characterActionsTermList.getSubtermCount();
		
		List<IAction> actions = new ArrayList<IAction>(actionCount);
		
		for (IStrategoTerm charactersActionsTerm : characterActionsTermList) {
			IStrategoNamed charactersActionsTermNamed = (IStrategoNamed) charactersActionsTerm;

			IStrategoList charactersTermList = (IStrategoList) termAt(charactersActionsTermNamed, 0);
			IStrategoList actionsTermList = (IStrategoList) termAt(charactersActionsTermNamed, 1);
			
			ICharacters characters = readCharacters(charactersTermList);
			
			List<IAction> actionsForCharacters = readActionsForCharacters(actionsTermList, characters, productions);
			
			actions.addAll(actionsForCharacters);
		}
		
		IAction[] res = new IAction[actions.size()];
		
		for (int i = 0; i < res.length; i++)
			res[i] = actions.get(i);
		
		return res;
	}
    
    private static ICharacters readCharacters(IStrategoList charactersTermList) {
        Characters characters = null;
        
        for (IStrategoTerm charactersTerm : charactersTermList) {
            Characters charactersForTerm;
            
            if (isTermInt(charactersTerm))
                charactersForTerm = new SingleCharacter(javaInt(charactersTerm));
            else
                charactersForTerm = new RangesCharacterSet(intAt(charactersTerm, 0), intAt(charactersTerm, 1));
            
            if (characters == null)
                characters = charactersForTerm;
            else
                characters = characters.union(charactersForTerm);
        }
        
        return characters;
    }
    
    private static ICharacters[] readReduceLookaheadCharacters(IStrategoList list) throws ParseTableReadException {
        List<ICharacters> followRestrictionCharacters = new ArrayList<ICharacters>(); // The length of this list equals the length of the lookahead
        
        for (IStrategoTerm term : list.getAllSubterms()) {
            IStrategoNamed termNamed = (IStrategoNamed) term;
            list = list.tail(); // TODO: check if this is required
            
            if ("follow-restriction".equals(termNamed.getName())) {
                IStrategoList listOfFollowCharRanges = (IStrategoList) termAt(termNamed, 0);
                
                for (IStrategoTerm charClass : listOfFollowCharRanges.getAllSubterms()) {
                    followRestrictionCharacters.add(readCharacters((IStrategoList) charClass.getSubterm(0)));
                }
            } else throw new ParseTableReadException("Invalid follow restriction for reduce");
        }
        
        return followRestrictionCharacters.toArray(new ICharacters[followRestrictionCharacters.size()]);
    }
	
	private static List<IAction> readActionsForCharacters(IStrategoList actionsTermList, ICharacters characters, IProduction[] productions) throws ParseTableReadException {
		int actionCount = actionsTermList.getSubtermCount();
		
		List<IAction> actions = new ArrayList<IAction>(actionCount);
		
		for (IStrategoTerm actionTerm : actionsTermList) {
			IStrategoAppl actionTermAppl = (IStrategoAppl) actionTerm;
			IAction action = null;
			
			if (actionTermAppl.getName().equals("reduce")) { // Reduce
				int arity = intAt(actionTermAppl, 0);
				int productionNumber = intAt(actionTermAppl, 1);
				ProductionType productionType = Production.typeFromInt(intAt(actionTermAppl, 2));
				
				IProduction production = productions[productionNumber];
				
				if (actionTermAppl.getConstructor().getArity() == 3) { // Reduce without lookahead
					action = new Reduce(characters, production, productionType, arity);
				} else if (actionTermAppl.getConstructor().getArity() == 4) { // Reduce with lookahead
					ICharacters[] followRestriction = readReduceLookaheadCharacters((IStrategoList) termAt(actionTermAppl, 3));
					
					action = new ReduceLookahead(characters, production, productionType, arity, followRestriction);
				}
			} else if (actionTermAppl.getName().equals("accept")) { // Accept
				action = new Accept();
			} else if (actionTermAppl.getName().equals("shift")) { // Shift
				int shiftState = intAt(actionTermAppl, 0);
				
				action = new Shift(characters, shiftState);
			}
			
			actions.add(action);
		}
		
		return actions;
	}
	
	// Mark states that are reachable by a reject production as rejectable
    // That means the parser transitions into such state by means of a goto action after there is reduced by the reject production
	private static void markRejectableStates(State[] states) {
	    List<IProduction> rejectProductions = new ArrayList<IProduction>();
        
        for (IState state : states) {
            for (IAction action : state.actions()) {
                if (action.actionType() == ActionType.REDUCE || action.actionType() == ActionType.REDUCE_LOOKAHEAD) {
                    IReduce reduce = (IReduce) action;
                    
                    if (reduce.productionType() == ProductionType.REJECT)
                        rejectProductions.add(reduce.production());
                }
            }
        }

        for (IState state : states) {
            for (IGoto gotoAction : state.gotos()) {
                boolean withRejectProduction = false;
                
                for (int production : gotoAction.productions()) {
                    for (IProduction rejectProduction : rejectProductions)
                        if (rejectProduction.productionNumber() == production)
                            withRejectProduction = true;
                }
                
                // The goto state for this goto action is marked rejectable if it is for at least one reject production
                if (withRejectProduction)
                    states[gotoAction.gotoState()].markRejectable();
            }
        }
	}
	
}