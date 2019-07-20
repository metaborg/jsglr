package org.spoofax.jsglr2.imploder;

import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.symbols.IMetaVarSymbol;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public abstract class AbstractTreeImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>,
    Tree>
//@formatter:on
    implements IImploder<ParseForest, Tree> {

    protected ParseNode implodeInjection(ParseNode parseNode) {
        for(Derivation derivation : parseNode.getDerivations()) {
            if(derivation.parseForests().length == 1 && (derivation.parseForests()[0] instanceof IParseNode)) {
                ParseNode injectedParseNode = (ParseNode) derivation.parseForests()[0];

                // Meta variables are injected:
                // https://github.com/metaborg/strategoxt/blob/master/strategoxt/stratego-libraries/sglr/lib/stratego/asfix/implode/injection.str#L68-L69
                if(injectedParseNode.production().lhs() instanceof IMetaVarSymbol) {
                    return injectedParseNode;
                }
            }
        }

        return parseNode;
    }

    protected List<Derivation> applyDisambiguationFilters(ParseNode parseNode) {
        if(!parseNode.isAmbiguous())
            return Collections.singletonList(parseNode.getFirstDerivation());

        List<Derivation> result;
        // TODO always filter longest-match?
        if(parseNode instanceof LayoutSensitiveParseNode) {
            ((LayoutSensitiveParseNode) parseNode).filterLongestMatchDerivations();
        }
        // TODO always filter prefer/avoid?
        result = parseNode.getPreferredAvoidedDerivations();

        return result;
    }

}
