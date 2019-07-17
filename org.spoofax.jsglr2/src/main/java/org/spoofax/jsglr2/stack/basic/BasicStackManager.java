package org.spoofax.jsglr2.stack.basic;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.StackManager;

public class BasicStackManager
//@formatter:off
   <ParseForest extends IParseForest,
    Parse       extends AbstractParse<ParseForest, BasicStackNode<ParseForest>>>
//@formatter:on
    extends StackManager<ParseForest, BasicStackNode<ParseForest>, Parse> {

    @Override protected BasicStackNode<ParseForest> createStackNode(IState state, Position position, boolean isRoot) {
        return new BasicStackNode<>(state, position);
    }

}
