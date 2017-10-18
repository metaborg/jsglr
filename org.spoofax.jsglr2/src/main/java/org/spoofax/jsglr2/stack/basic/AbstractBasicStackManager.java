package org.spoofax.jsglr2.stack.basic;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public abstract class AbstractBasicStackManager<StackNode extends AbstractBasicStackNode<ParseForest>,ParseForest extends AbstractParseForest> extends StackManager<AbstractBasicStackNode<ParseForest>, ParseForest> {
    
	protected abstract StackNode createStackNode(int stackNumber, IState state);
	
    public AbstractBasicStackNode<ParseForest> createInitialStackNode(Parse<AbstractBasicStackNode<ParseForest>, ParseForest> parse, IState state) {
        AbstractBasicStackNode<ParseForest> newStackNode = createStackNode(parse.stackNodeCount++, state);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public AbstractBasicStackNode<ParseForest> createStackNode(Parse<AbstractBasicStackNode<ParseForest>, ParseForest> parse, IState state) {
        AbstractBasicStackNode<ParseForest> newStackNode = createStackNode(parse.stackNodeCount++, state);
        
        parse.notify(observer -> observer.createStackNode(newStackNode));
                
        return newStackNode;
    }
    
    public StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> createStackLink(Parse<AbstractBasicStackNode<ParseForest>, ParseForest> parse, AbstractBasicStackNode<ParseForest> from, AbstractBasicStackNode<ParseForest> to, ParseForest parseNode) {
        StackLink<AbstractBasicStackNode<ParseForest>, ParseForest> link = from.addOutLink(parse.stackLinkCount++, to, parseNode);
        
        parse.notify(observer -> observer.createStackLink(link));
        
        return link;
    }
    
    protected Iterable<StackLink<AbstractBasicStackNode<ParseForest>, ParseForest>> stackLinksOut(AbstractBasicStackNode<ParseForest> stack) {
        return stack.getLinksOut();
    }
    
}
