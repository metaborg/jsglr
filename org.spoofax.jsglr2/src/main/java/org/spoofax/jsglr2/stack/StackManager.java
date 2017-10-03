package org.spoofax.jsglr2.stack;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IState;

public abstract class StackManager<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> {
    
    public abstract StackNode createInitialStackNode(Parse<StackNode, ParseForest> parse, IState state);
    
    public abstract StackNode createStackNode(Parse<StackNode, ParseForest> parse, IState state);
    
    public abstract StackLink<StackNode, ParseForest> createStackLink(Parse<StackNode, ParseForest> parse, StackNode from, StackNode to, ParseForest parseNode);
    
    public void rejectStackLink(Parse<StackNode, ParseForest> parse, StackLink<StackNode, ParseForest> link) {
        link.reject();
        
        parse.notify(observer -> observer.rejectStackLink(link));
    }
    
    public StackNode findActiveStackWithState(Parse<StackNode, ParseForest> parse, IState state) {
        for (StackNode activeStack : parse.activeStacks) {
            if (activeStack.state.equals(state))
                return activeStack;
        }
        
        return null;
    }
    
    public StackLink<StackNode, ParseForest> findDirectLink(StackNode from, StackNode to) {
        for (StackLink<StackNode, ParseForest> link : stackLinksOut(from)) {
            if (link.to == to)
                return link;
        }
        
        return null;
    }
    
    public List<StackPath<StackNode, ParseForest>> findAllPathsOfLength(StackNode stack, int length) {
        List<StackPath<StackNode, ParseForest>> paths = new ArrayList<StackPath<StackNode, ParseForest>>();
    	
    		StackPath<StackNode, ParseForest> pathsOrigin = new EmptyStackPath<StackNode, ParseForest>(stack);
    		
    		findAllPathsOfLength(pathsOrigin, length, paths);
    		
    		return paths;
    }
    
    private void findAllPathsOfLength(StackPath<StackNode, ParseForest> path, int length, List<StackPath<StackNode, ParseForest>> paths) {
    		if (length == 0)
    			paths.add(path);
    		else {
    			StackNode lastStackNode = path.head();
    			
    			for (StackLink<StackNode, ParseForest> linkOut : stackLinksOut(lastStackNode)) {
    				StackPath<StackNode, ParseForest> extendedPath = new NonEmptyStackPath<StackNode, ParseForest>(linkOut, path);
    				
    				findAllPathsOfLength(extendedPath, length - 1, paths);
    			}
    		}
    }
    
    protected abstract Iterable<StackLink<StackNode, ParseForest>> stackLinksOut(StackNode stack);
    
    public ParseForest[] getParseForests(ParseForestManager<ParseForest, ?, ?> parseForestManager, StackPath<StackNode, ParseForest> pathBegin) {
		ParseForest[] res = parseForestManager.parseForestsArray(pathBegin.length);
		
		StackPath<StackNode, ParseForest> path = pathBegin;
		
		for (int i = 0; i < pathBegin.length; i++) {
			NonEmptyStackPath<StackNode, ParseForest> nonEmptyPath = (NonEmptyStackPath<StackNode, ParseForest>) path;
			
			res[i] = nonEmptyPath.link.parseForest;
			
			path = nonEmptyPath.tail;
		}
		
		return res;
    }
    
}
