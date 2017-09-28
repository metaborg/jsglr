package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public class ElkhoundStackManager<ParseForest extends AbstractParseForest> extends StackManager<ElkhoundStackNode<ParseForest>, ParseForest> {
    
    public ElkhoundStackNode<ParseForest> createInitialStackNode(Parse<ElkhoundStackNode<ParseForest>, ParseForest> parse, IState state) {
        ElkhoundStackNode<ParseForest> newStackNode = new ElkhoundStackNode<ParseForest>(parse.stackNodeCount++, state, 1);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public ElkhoundStackNode<ParseForest> createStackNode(Parse<ElkhoundStackNode<ParseForest>, ParseForest> parse, IState state) {
        ElkhoundStackNode<ParseForest> newStackNode = new ElkhoundStackNode<ParseForest>(parse.stackNodeCount++, state, 0);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public StackLink<ElkhoundStackNode<ParseForest>, ParseForest> createStackLink(Parse<ElkhoundStackNode<ParseForest>, ParseForest> parse, ElkhoundStackNode<ParseForest> from, ElkhoundStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<ElkhoundStackNode<ParseForest>, ParseForest> link = from.addLink(parse.stackLinkCount++, to, parseNode);
        
        parse.notify(observer -> observer.createStackLink(link.linkNumber, from, to, parseNode));
        
        return link;
    }
    
    protected Iterable<StackLink<ElkhoundStackNode<ParseForest>, ParseForest>> stackLinksOut(ElkhoundStackNode<ParseForest> stack) {
        return stack.getLinksOut();
    }
    
}
