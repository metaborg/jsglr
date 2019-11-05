package org.spoofax.jsglr2.stack.collections;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.HashMap;
import java.util.Map;

public class ActiveStacksArrayListHashMap
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<StackNode>>
//@formatter:on
    extends ActiveStacksArrayList<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    protected Map<Integer, StackNode> activeStacksMap;

    public ActiveStacksArrayListHashMap(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        super(observing);
        this.activeStacksMap = new HashMap<>();
    }

    @Override public void add(StackNode stack) {
        super.add(stack);

        activeStacksMap.put(stack.state().id(), stack);
    }

    @Override public StackNode findWithState(IState state) {
        observing.notify(observer -> observer.findActiveStackWithState(state));

        return activeStacksMap.get(state.id());
    }

    @Override public void clear() {
        super.clear();

        activeStacksMap.clear();
    }

}
