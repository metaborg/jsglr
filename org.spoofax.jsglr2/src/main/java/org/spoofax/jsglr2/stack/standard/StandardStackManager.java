package org.spoofax.jsglr2.stack.standard;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public class StandardStackManager<ParseForest extends AbstractParseForest> extends StackManager<StandardStackNode<ParseForest>, ParseForest> {
    
    public StandardStackNode<ParseForest> createInitialStackNode(Parse<StandardStackNode<ParseForest>, ParseForest> parse, IState state) {
        StandardStackNode<ParseForest> newStackNode = new StandardStackNode<ParseForest>(parse.stackNodeCount++, state);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public StandardStackNode<ParseForest> createStackNode(Parse<StandardStackNode<ParseForest>, ParseForest> parse, IState state) {
        StandardStackNode<ParseForest> newStackNode = new StandardStackNode<ParseForest>(parse.stackNodeCount++, state);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public StackLink<StandardStackNode<ParseForest>, ParseForest> createStackLink(Parse<StandardStackNode<ParseForest>, ParseForest> parse, StandardStackNode<ParseForest> from, StandardStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<StandardStackNode<ParseForest>, ParseForest> link = from.addLink(parse.stackLinkCount++, to, parseNode);
        
        parse.notify(observer -> observer.createStackLink(link.linkNumber, from, to, parseNode));
        
        return link;
    }
    
    protected Iterable<StackLink<StandardStackNode<ParseForest>, ParseForest>> stackLinksOut(StandardStackNode<ParseForest> stack) {
        return stack.getLinksOut();
    }
    
}
