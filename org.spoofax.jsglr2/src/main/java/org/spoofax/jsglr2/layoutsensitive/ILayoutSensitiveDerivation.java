package org.spoofax.jsglr2.layoutsensitive;

import com.google.common.collect.Lists;
import org.spoofax.jsglr2.parseforest.basic.IBasicDerivation;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;

import java.util.List;

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
            if(pf instanceof ILayoutSensitiveDerivation) {
                result.addAll(((ILayoutSensitiveDerivation<ParseForest>) pf).getLongestMatchPositions());
            } else if(pf instanceof ILayoutSensitiveParseNode) {
                List<PositionInterval> positions =
                    ((ILayoutSensitiveParseNode<ParseForest, ILayoutSensitiveDerivation<ParseForest>>) pf)
                        .getLongestMatchPositions();

                result.addAll(positions);
            }
        }
        return result;
    }

}
