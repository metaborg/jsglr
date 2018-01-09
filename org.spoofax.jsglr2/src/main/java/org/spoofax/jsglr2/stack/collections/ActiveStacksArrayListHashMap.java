package org.spoofax.jsglr2.stack.collections;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.states.IState;

public class ActiveStacksArrayListHashMap<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    extends ActiveStacksArrayList<ParseForest, StackNode> {

    protected Map<Integer, StackNode> activeStacksMap;

    public ActiveStacksArrayListHashMap(ParserObserving<ParseForest, StackNode> observing) {
        super(observing);
        this.activeStacksMap = new HashMap<>();
    }

    @Override
    public void add(StackNode stack) {
        super.add(stack);

        activeStacksMap.put(stack.state.id(), stack);
    }

    @Override
    public StackNode findWithState(IState state) {
        observing.notify(observer -> observer.findActiveStackWithState(state));

        return activeStacksMap.get(state.id());
    }

    @Override
    public void clear() {
        super.clear();

        activeStacksMap.clear();
    }

}
