package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parser.AbstractParse;

public abstract class ParseForestManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
{

    abstract public ParseNode createParseNode(AbstractParse<ParseForest, ?> parse, IProduction production,
        Derivation firstDerivation);

    abstract public Derivation createDerivation(AbstractParse<ParseForest, ?> parse, IProduction production,
        ProductionType productionType, ParseForest[] parseForests);

    abstract public void addDerivation(AbstractParse<ParseForest, ?> parse, ParseNode parseNode, Derivation derivation);

    abstract public ParseForest createCharacterNode(AbstractParse<ParseForest, ?> parse);

    abstract public ParseForest[] parseForestsArray(int length);

    abstract public ParseForest filterStartSymbol(ParseForest parseForest, String startSymbol,
        AbstractParse<ParseForest, ?> parse);

}
