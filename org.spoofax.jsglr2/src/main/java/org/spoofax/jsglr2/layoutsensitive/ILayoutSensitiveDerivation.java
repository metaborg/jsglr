package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.spoofax.jsglr2.parseforest.basic.IBasicDerivation;
import org.spoofax.jsglr2.parser.Position;

import com.google.common.collect.Lists;

public interface ILayoutSensitiveDerivation
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest>
//@formatter:on
    extends IBasicDerivation<ParseForest> {

    Position getStartPosition();

    Position getEndPosition();

    Position getLeftPosition();

    Position getRightPosition();

    default List<PositionInterval> getLongestMatchPositions() {
        List<PositionInterval> result = Lists.newArrayList();
        if(production().isLongestMatch()) {
            result.add(new PositionInterval(getStartPosition(), getEndPosition()));
        }
        for(ParseForest pf : parseForests()) {
            if(pf instanceof ILayoutSensitiveParseNode) {
                result.addAll(((ILayoutSensitiveParseNode<ParseForest, ILayoutSensitiveDerivation<ParseForest>>) pf)
                    .getLongestMatchPositions());
            }
        }
        return result;
    }

}
