package org.spoofax.jsglr2.layoutsensitive;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;

import com.google.common.collect.Lists;

public class LayoutSensitiveParseNode extends LayoutSensitiveParseForest
    implements IBasicParseNode<LayoutSensitiveParseForest, LayoutSensitiveDerivation> {

    public final IProduction production; // left hand side non-terminal
    private final List<LayoutSensitiveDerivation> derivations;

    public List<PositionInterval> longestMatchPos = null;
    boolean filteredLongestMatch = false;

    public LayoutSensitiveParseNode(Position startPosition, Position endPosition, IProduction production) {
        super(startPosition, endPosition);
        this.production = production;
        this.derivations = new ArrayList<>();
    }

    @Override public IProduction production() {
        return production;
    }

    public List<LayoutSensitiveDerivation> getDerivations() {
        return derivations;
    }

    @Override public String descriptor() {
        return production.descriptor();
    }

    public void filterLongestMatchDerivations() {
        filteredLongestMatch = true;
        longestMatchPos = getFirstDerivation().getLongestMatchPositions();
        if(derivations.size() <= 1) {
            return;
        }

        List<List<PositionInterval>> longestMatchNodes = Lists.newArrayList();

        for(LayoutSensitiveDerivation derivation : derivations) {
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
        //
        if(disambiguatedLongestMatch) {
            LayoutSensitiveDerivation longestDerivation = derivations.get(currentLongestDerivation);
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

    public List<PositionInterval> getLongestMatchPositions() {
        // System.out.println("getting positions for " + this);
        if(longestMatchPos == null && !filteredLongestMatch) {
            filterLongestMatchDerivations();
            return longestMatchPos;
        } else {
            return longestMatchPos;
        }

    }

    // @Override public String toString() {
    // if(derivations.size() == 1) {
    // return derivations.get(0).toString();
    // }
    // String buf = "amb(";
    // int i = 0;
    // for(LayoutSensitiveDerivation der : derivations) {
    // if(der == null)
    // continue;
    // if(i != 0)
    // buf += ", ";
    // buf += der.toString();
    // i++;
    // }
    // buf += ")";
    //
    // return buf;
    // }

}
