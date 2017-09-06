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
import org.spoofax.jsglr2.actions.Action;
import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.Goto;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.Reduce;
import org.spoofax.jsglr2.actions.ReduceLookahead;
import org.spoofax.jsglr2.actions.Shift;
import org.spoofax.jsglr2.characters.Characters;
import org.spoofax.jsglr2.characters.RangesCharacterSet;
import org.spoofax.jsglr2.characters.SingleCharacter;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public class ParseTableReader {
	
	public static IParseTable read(InputStream inputStream) throws ParseTableReadException, ParseError, IOException {
		TermFactory factory = new TermFactory();
		TermReader termReader = new TermReader(factory);
		IStrategoTerm parseTableTerm = termReader.parseFromStream(inputStream);
		
		return read(parseTableTerm);
	}

	public static IParseTable read(IStrategoTerm pt) throws ParseTableReadException {
		// int version = intAt(pt, 0); // Not used 
		
		int startStateNumber = intAt(pt, 1);
		
		IStrategoList productionsTermList = termAt(pt, 2);
        IStrategoNamed statesTerm = termAt(pt, 3);
        // IStrategoNamed prioritiesTerm = termAt(pt, 4); // Not used
        
        Production[] productions = readProductions(productionsTermList);
        State[] states = readStates(statesTerm, productions);

        markRejectableStates(states);
        
        return new ParseTable(productions, states, startStateNumber);
	}
	
	private static void markRejectableStates(State[] states) {
	    List<IProduction> rejectProductions = new ArrayList<IProduction>();
        
        for (State state : states) {
            for (IAction action : state.actions()) {
                if (action.actionType() == ActionType.REDUCE) {
                    IReduce reduce = (IReduce) action;
                    
                    if (reduce.productionType() == ProductionType.REJECT)
                        rejectProductions.add(reduce.production());
                }
            }
        }
        
        for (State state : states) {
            for (IGoto gotoAction : state.gotos()) {
                boolean withRejectProduction = false;
                
                for (int production : gotoAction.productions()) {
                    for (IProduction rejectProduction : rejectProductions)
                        if (rejectProduction.productionNumber() == production)
                            withRejectProduction = true;
                }
                
                if (withRejectProduction)
                    states[gotoAction.gotoState()].markRejectable();
            }
        }
	}
	
	private static Production[] readProductions(IStrategoList productionsTermList) throws ParseTableReadException {
		int productionsCount = productionsTermList.getSubtermCount();
		
		Production[] productions = new Production[257 + productionsCount];
		
		for (IStrategoTerm numberedProductionTerm : productionsTermList) {
			IStrategoNamed numberedProductionTermNamed = (IStrategoNamed) numberedProductionTerm;
			IStrategoAppl productionTerm = termAt(numberedProductionTermNamed, 0);
			IStrategoAppl attributesTerm = termAt(productionTerm, 2);
			
			int productionNumber = intAt(numberedProductionTermNamed, 1);
			ProductionAttributes productionAttributes = readProductionAttributes(attributesTerm);
			
			productions[productionNumber] = new Production(productionNumber, productionTerm, productionAttributes);
		}
		
		return productions;
	}
	
	private static ProductionAttributes readProductionAttributes(IStrategoAppl attributesTerm) throws ParseTableReadException {
		if (attributesTerm.getName().equals("attrs")) {
			ProductionType type = ProductionType.NO_TYPE;

            IStrategoTerm term = null;
            
			boolean isRecover = false;
			boolean isBracket = false;
			boolean isCompletion = false;
			boolean isPlaceholderInsertion = false;
			boolean isLiteralCompletion = false;
			boolean isIgnoreLayout = false;
			boolean isNewlineEnforced = false;
			boolean isLongestMatch = false;
            
            IStrategoList attributesTermsList = (IStrategoList) attributesTerm.getSubterm(0);
            
            for (IStrategoTerm attributeTerm : attributesTermsList) {
            	IStrategoNamed attributeTermNamed = (IStrategoNamed) attributeTerm;
            	
            	if (attributeTermNamed.getName().equals("reject")) {
            		type = ProductionType.REJECT;
            	} else if (attributeTermNamed.getName().equals("prefer")) {
            		type = ProductionType.PREFER;
            	} else if (attributeTermNamed.getName().equals("avoid")) {
            		type = ProductionType.AVOID;
            	} else if (attributeTermNamed.getName().equals("bracket")) {
            		type = ProductionType.BRACKET;
            		isBracket = true;
            	} else if (attributeTermNamed.getName().equals("assoc")) {
        			IStrategoNamed associativityTermNamed = (IStrategoNamed) attributeTermNamed.getSubterm(0);
        			
        			if (associativityTermNamed.getName().equals("left") || associativityTermNamed.getName().equals("assoc")) {
                        type = ProductionType.LEFT_ASSOCIATIVE;
                    } else if (associativityTermNamed.getName().equals("right")) {
                        type = ProductionType.RIGHT_ASSOCIATIVE;
                    }
        		} else if (attributeTermNamed.getName().equals("term") && attributeTermNamed.getSubtermCount() == 1) {
        			if (attributeTermNamed.getSubterm(0) instanceof IStrategoNamed) {
        				IStrategoNamed childTermNamed = (IStrategoNamed) attributeTermNamed.getSubterm(0);
        				
        				if (childTermNamed.getSubtermCount() == 1 && childTermNamed.getName().equals("cons")) {
                			term = childTermNamed.getSubterm(0);
                		} else if (childTermNamed.getSubtermCount() == 0 && childTermNamed.getName().equals("recover")) {
                		    isRecover = true;
                   		} else if (childTermNamed.getSubtermCount() == 0 && childTermNamed.getName().equals("completion")) {
                		    isCompletion = true;
                		} else if (childTermNamed.getSubtermCount() == 0 && childTermNamed.getName().equals("placeholder-insertion")) {
                            isPlaceholderInsertion = true;
                        } else if (childTermNamed.getSubtermCount() == 0 && childTermNamed.getName().equals("literal-completion")) {
                            isLiteralCompletion = true;
                        } else if (childTermNamed.getSubtermCount() == 0 && (childTermNamed.getName().equals("ignore-layout") || childTermNamed.getName().equals("ignore-indent"))) {
                            isIgnoreLayout = true;
                        } else if (childTermNamed.getSubtermCount() == 1 && childTermNamed.getName().equals("layout")) {
                        	throw new ParseTableReadException("'layout' production attributes not supported");
                        } else if (childTermNamed.getSubtermCount() == 0 && childTermNamed.getName().equals("enforce-newline")) {
                        	isNewlineEnforced = true;
                        } else if (childTermNamed.getSubtermCount() == 0 && childTermNamed.getName().equals("longest-match")) {
                        	isLongestMatch = true;
                        }
                    }
    			} else if (attributeTermNamed.getName().equals("id")) {
    				term = attributeTermNamed.getSubterm(0);
        		} else {
        			throw new ParseTableReadException("'layout' production attributes not supported");
        		}
            }
            
            return new ProductionAttributes(type, term, isRecover, isBracket, isCompletion, isPlaceholderInsertion, isLiteralCompletion, isIgnoreLayout, isNewlineEnforced, isLongestMatch);
		} else if (attributesTerm.getName().equals("no-attrs")) {
            return new ProductionAttributes(ProductionType.NO_TYPE, null, false, false, false, false, false, false, false, false);
        }
		
		throw new ParseTableReadException("unknown production attribute type: " + attributesTerm);
	}
	
	private static State[] readStates(IStrategoNamed statesTermNamed, Production[] productions) throws ParseTableReadException {
		IStrategoList statesTermList = termAt(statesTermNamed, 0);
		int stateCount = statesTermList.getSubtermCount();
		
		State[] states = new State[stateCount];
		
		for (IStrategoTerm stateTerm : statesTermList) {
			IStrategoNamed stateTermNamed = (IStrategoNamed) stateTerm;
			
			int stateNumber = intAt(stateTermNamed, 0);

			IStrategoList gotosTermList = (IStrategoList) termAt(stateTermNamed, 1);
			IStrategoList actionsTermList = (IStrategoList) termAt(stateTermNamed, 2);
			
			Goto[] gotos = readGotos(gotosTermList);
			Action[] actions = readActions(actionsTermList, productions);
			
			states[stateNumber] = new State(stateNumber, gotos, actions);
		}
		
		return states;
	}
	
	private static Goto[] readGotos(IStrategoList gotosTermList) {
		int gotoCount = gotosTermList.getSubtermCount();
		
		Goto[] gotos = new Goto[gotoCount];
		
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
	
	private static Action[] readActions(IStrategoList characterActionsTermList, Production[] productions) throws ParseTableReadException {
		int actionCount = characterActionsTermList.getSubtermCount();
		
		List<Action> actions = new ArrayList<Action>(actionCount);
		
		for (IStrategoTerm charactersActionsTerm : characterActionsTermList) {
			IStrategoNamed charactersActionsTermNamed = (IStrategoNamed) charactersActionsTerm;

			IStrategoList charactersTermList = (IStrategoList) termAt(charactersActionsTermNamed, 0);
			IStrategoList actionsTermList = (IStrategoList) termAt(charactersActionsTermNamed, 1);
			
			Characters characters = readCharacters(charactersTermList);
			
			List<Action> actionsForCharacters = readActionsForCharacters(actionsTermList, characters, productions);
			
			actions.addAll(actionsForCharacters);
		}
		
		Action[] res = new Action[actions.size()];
		
		for (int i = 0; i < res.length; i++)
			res[i] = actions.get(i);
		
		return res;
	}
    
    private static Characters readCharacters(IStrategoList charactersTermList) {
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
    
    private static Characters[] readReduceLookaheadCharacters(IStrategoList list) throws ParseTableReadException {
        List<Characters> followRestrictionCharacters = new ArrayList<Characters>(); // The length of this list equals the length of the lookahead, currently only 1 supported
        
        for (IStrategoTerm term : list.getAllSubterms()) {
            IStrategoNamed termNamed = (IStrategoNamed) term;
            list = list.tail();
            
            if ("follow-restriction".equals(termNamed.getName())) {
                IStrategoList listOfFollowCharRanges = (IStrategoList) termAt(termNamed, 0);
                
                for (IStrategoTerm charClass : listOfFollowCharRanges.getAllSubterms()) {
                    followRestrictionCharacters.add(readCharacters((IStrategoList) charClass.getSubterm(0)));
                }
            } else {
                throw new ParseTableReadException("invalid follow restriction for reduce");
            }
        }
        
        return followRestrictionCharacters.toArray(new Characters[followRestrictionCharacters.size()]);
    }
	
	private static List<Action> readActionsForCharacters(IStrategoList actionsTermList, Characters characters, Production[] productions) throws ParseTableReadException {
		int actionCount = actionsTermList.getSubtermCount();
		
		List<Action> actions = new ArrayList<Action>(actionCount);
		
		for (IStrategoTerm actionTerm : actionsTermList) {
			IStrategoAppl actionTermAppl = (IStrategoAppl) actionTerm;
			Action action = null;
			
			if (actionTermAppl.getName().equals("reduce")) { // Reduce
				int arity = intAt(actionTermAppl, 0);
				int productionNumber = intAt(actionTermAppl, 1);
				ProductionType productionType = Production.typeFromInt(intAt(actionTermAppl, 2));
				
				Production production = productions[productionNumber];
				
				if (actionTermAppl.getConstructor().getArity() == 3) { // Reduce without lookahead
					action = new Reduce(characters, production, productionType, arity);
				} else if (actionTermAppl.getConstructor().getArity() == 4) { // Reduce with lookahead
					Characters[] followRestriction = readReduceLookaheadCharacters((IStrategoList) termAt(actionTermAppl, 3));
					
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
	
}
