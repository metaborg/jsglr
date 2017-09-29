package org.spoofax.jsglr2.stack.basic;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public class BasicStackManager<ParseForest extends AbstractParseForest> extends StackManager<BasicStackNode<ParseForest>, ParseForest> {
    
    public BasicStackNode<ParseForest> createInitialStackNode(Parse<BasicStackNode<ParseForest>, ParseForest> parse, IState state) {
        BasicStackNode<ParseForest> newStackNode = new BasicStackNode<ParseForest>(parse.stackNodeCount++, state);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public BasicStackNode<ParseForest> createStackNode(Parse<BasicStackNode<ParseForest>, ParseForest> parse, IState state) {
        BasicStackNode<ParseForest> newStackNode = new BasicStackNode<ParseForest>(parse.stackNodeCount++, state);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public StackLink<BasicStackNode<ParseForest>, ParseForest> createStackLink(Parse<BasicStackNode<ParseForest>, ParseForest> parse, BasicStackNode<ParseForest> from, BasicStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<BasicStackNode<ParseForest>, ParseForest> link = from.addLink(parse.stackLinkCount++, to, parseNode);
        
        parse.notify(observer -> observer.createStackLink(link.linkNumber, from, to, parseNode));
        
        return link;
    }
    
    protected Iterable<StackLink<BasicStackNode<ParseForest>, ParseForest>> stackLinksOut(BasicStackNode<ParseForest> stack) {
        return stack.getLinksOut();
    }
    
}
