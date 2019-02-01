package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public abstract class ParseForestManager<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> {

    abstract public ParseNode createParseNode(AbstractParse<ParseForest, ?> parse, Position beginPosition,
        IProduction production, Derivation firstDerivation);

    abstract public Derivation createDerivation(AbstractParse<ParseForest, ?> parse, Position beginPosition,
        IProduction production, ProductionType productionType, ParseForest[] parseForests);

    abstract public void addDerivation(AbstractParse<ParseForest, ?> parse, ParseNode parseNode, Derivation derivation);

    abstract public ParseForest createCharacterNode(AbstractParse<ParseForest, ?> parse);

    abstract public ParseForest[] parseForestsArray(int length);

    abstract public ParseForest filterStartSymbol(ParseForest parseForest, String startSymbol, AbstractParse<ParseForest, ?> parse);

}
