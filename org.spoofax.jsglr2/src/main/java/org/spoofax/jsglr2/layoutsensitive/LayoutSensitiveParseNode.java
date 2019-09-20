package org.spoofax.jsglr2.layoutsensitive;

import com.google.common.collect.Lists;
import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;

import java.util.List;

public class LayoutSensitiveParseNode
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest,
    Derivation  extends ILayoutSensitiveDerivation<ParseForest>>
//@formatter:on
    extends BasicParseNode<ParseForest, Derivation> implements ILayoutSensitiveParseNode<ParseForest, Derivation> {

    private final Position startPosition, endPosition;

    public List<PositionInterval> longestMatchPos = null;
    boolean filteredLongestMatch = false;

    public LayoutSensitiveParseNode(Position startPosition, Position endPosition, IProduction production) {
        super(production);

        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    @Override public void filterLongestMatchDerivations() {
        filteredLongestMatch = true;
        longestMatchPos = getFirstDerivation().getLongestMatchPositions();
        if(derivations.size() <= 1) {
            return;
        }

        List<List<PositionInterval>> longestMatchNodes = Lists.newArrayList();

        for(Derivation derivation : derivations) {
            longestMatchNodes.add(derivation.getLongestMatchPositions());
        }

        int size = -1;
        int currentLongestDerivation = 0;
        boolean disambiguatedLongestMatch = false;

        for(int i = 1; i < longestMatchNodes.size(); i++) {
            List<PositionInterval> list = longestMatchNodes.get(i);

            // FIXME: list of longest-match nodes should be the same?
            if(size == -1) {
                size = list.size();
            } else if(size != list.size()) {
                System.out.println("Number of longest match nodes differ");
            }

            for(int j = 0; j < list.size(); j++) {
                Boolean secondNodeExpandsLonger =
                    expandsLonger(longestMatchNodes.get(currentLongestDerivation).get(j), list.get(j));
                if(secondNodeExpandsLonger == null) {
                    continue;
                } else if(secondNodeExpandsLonger) {
                    currentLongestDerivation = i;
                    disambiguatedLongestMatch = true;
                    break;
                } else {
                    disambiguatedLongestMatch = true;
                    break;
                }
            }
        }

        if(disambiguatedLongestMatch) {
            Derivation longestDerivation = derivations.get(currentLongestDerivation);
            derivations.clear();
            derivations.add(longestDerivation);
        }

        longestMatchPos = longestMatchNodes.get(currentLongestDerivation);
    }

    private Boolean expandsLonger(PositionInterval pos1, PositionInterval pos2) {
        assert (pos1.getStart().equals(pos2.getStart()));

        if(pos2.getEnd().line > pos1.getEnd().line
            || (pos2.getEnd().line == pos1.getEnd().line && pos2.getEnd().column > pos1.getEnd().column)) {
            return true;
        } else if(pos1.getEnd().line > pos2.getEnd().line
            || (pos1.getEnd().line == pos2.getEnd().line && pos1.getEnd().column > pos2.getEnd().column)) {
            return false;
        }

        return null;
    }

    @Override public List<PositionInterval> getLongestMatchPositions() {
        if(longestMatchPos == null && !filteredLongestMatch) {
            filterLongestMatchDerivations();

            return longestMatchPos;
        } else {
            return longestMatchPos;
        }
    }

}
