package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;

import com.google.common.collect.Lists;

public interface ILayoutSensitiveParseNode
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest,
    Derivation  extends ILayoutSensitiveDerivation<ParseForest>>
//@formatter:on
    extends IBasicParseNode<ParseForest, Derivation>, ILayoutSensitiveParseForest {

    boolean filteredLongestMatch();

    void setFilteredLongestMatch();

    default void filterLongestMatchDerivations() {
        setFilteredLongestMatch();

        if(getDerivations().size() <= 1) {
            setLongestMatchPositions(getFirstDerivation().getLongestMatchPositions());

            return;
        }

        List<List<PositionInterval>> longestMatchNodes = Lists.newArrayList();

        for(Derivation derivation : getDerivations()) {
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
            Derivation longestDerivation = getDerivations().get(currentLongestDerivation);
            getDerivations().clear();
            getDerivations().add(longestDerivation);
        }

        setLongestMatchPositions(longestMatchNodes.get(currentLongestDerivation));
    }

    default Boolean expandsLonger(PositionInterval pos1, PositionInterval pos2) {
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

    void setLongestMatchPositions(List<PositionInterval> longestMatchPositions);

    List<PositionInterval> longestMatchPositions();

    default List<PositionInterval> getLongestMatchPositions() {
        if(longestMatchPositions() == null && !filteredLongestMatch())
            filterLongestMatchDerivations();

        return longestMatchPositions();
    }

}
