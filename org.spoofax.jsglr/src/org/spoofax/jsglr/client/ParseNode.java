/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.TopdownTreeBuilder;
import org.spoofax.terms.TermFactory;

public class ParseNode extends AbstractParseNode {

    private int label;

    AbstractParseNode[] kids;
    
    private boolean isParseProductionChain; //should be set only after parsing 

    private boolean isSetPPC;
    
    private int nodeType;

    private int cachedHashCode; //should be set only after parsing 

    public int getLabel() {
    	if(isAmbNode())
    		throw new NotImplementedException();
    	return label;
    }

    public ParseNode(int label, AbstractParseNode[] kids, int type) {
    	setFields(label, kids, type);
    	if(type == AbstractParseNode.AMBIGUITY){
        	this.isParseProductionChain=false;
        	this.isSetPPC=true;
        	this.label = -1;
    	}
    }
    
    public static ParseNode createAmbNode(AbstractParseNode[] kids){
    	ParseNode amb = new ParseNode(-1, kids, AbstractParseNode.AMBIGUITY);
    	return amb;
    }

	private void setFields(int label, AbstractParseNode[] kids, int type) {
		assert (type!=AbstractParseNode.AMBIGUITY || (label==-1));
		this.nodeType = type;
        this.label = label;
        this.kids = kids;
        this.isParseProductionChain = false;
        this.isSetPPC=false;
 	}
	
	public void toAmbiguity(AbstractParseNode pn){
		if(this == pn)
			return;
		ParseNode left = new ParseNode(this.label, this.kids, this.nodeType); 
		assert(this.cachedHashCode == NO_HASH_CODE) : "Hashcode should not be cached during parsing because descendant nodes may change";
		assert(!this.isParseProductionChain) : "PPC is not set to true during parsing because descendents may change";
		if(pn instanceof ParseNode)
			((ParseNode) pn).replaceDescendant(this, left); //prevent cycles
		setFields(-1, new AbstractParseNode[] { left, pn }, AbstractParseNode.AMBIGUITY);
	}
	
	private void replaceDescendant(ParseNode before, ParseNode after){ //only reductions for current char (right chain) are inspected
		if(kids.length > 0 ){ 
			replaceDescendantAt(before, after, kids.length-1);			
		}
		if(isAmbNode() && kids.length >1){
			replaceDescendantAt(before, after, kids.length-2);			
		}
	}

	private void replaceDescendantAt(ParseNode before, ParseNode after, int index) {
		AbstractParseNode kid = kids[index];
		if(kid==before){
			kids[index]=after; 
			return; //no further inspection needed since cycles should not occur
		}
		else if(kid instanceof ParseNode){
			((ParseNode)kid).replaceDescendant(before, after);
		}
	}
	    
    @Override
	public boolean isParseProductionChain() {
    	//REMARK: works because PPC property is not set during parsing, so descendants will not change
		// assert isParseProductionChain == calculateIsParseProdChain(kids);
    	if(!isSetPPC)
    		deepSetIsParseProdChain();
		return isParseProductionChain;
	}
    
	private void deepSetIsParseProdChain(){
		if(!isSetPPC){
			for (int i = 0; i < kids.length; i++) {
				if(kids[i].isParseNode() || kids[i].isAmbNode())
					((ParseNode)kids[i]).deepSetIsParseProdChain();
			}
			this.isParseProductionChain=false;
			if (kids.length == 2 && !isAmbNode()) {
	    		this.isParseProductionChain =
	    			kids[0] instanceof ParseProductionNode /*kids[0].isParseProductionChain()*/
	    			&& kids[1].isParseProductionChain();
	        }
			else if (kids.length == 1) {
	    		this.isParseProductionChain = kids[0].isParseProductionChain();
	        } 
			isSetPPC=true;
		}
	}

	@Override 
    public Object toTreeTopdown(TopdownTreeBuilder builder) {
    	if(isAmbNode())
        	return builder.buildTreeAmb(this);
    	return builder.buildTreeNode(this);
    }

    //TODO: refactor
    @Override 
	public Object toTreeBottomup(BottomupTreeBuilder builder) {
    	if(isAmbNode()){
    		return toTreeBottomupAmb(builder);
    	}
    	builder.visitLabel(label);
        ArrayList<Object> subtrees = new ArrayList<Object>(kids.length);
        for (int i = 0; i < kids.length; i++) {
        	subtrees.add(kids[i].toTreeBottomup(builder));
        }

        Object result = builder.buildNode(label, subtrees);
        builder.endVisitLabel(label);
		return result;
    }
    
	
	public Object toTreeBottomupAmb(BottomupTreeBuilder builder) {
    	ArrayList<Object> collect = new ArrayList<Object>();
    	addToTreeAmb(builder, collect);
    	return builder.buildAmb(collect);
    }
        
    private void addToTreeAmb(BottomupTreeBuilder builder, List<Object> collect) {
    	for (int i = kids.length - 1; i >= 0; i--) {
    		AbstractParseNode alt = kids[i];
    		if (alt.isAmbNode()) {
    			((ParseNode) alt).addToTreeAmb(builder, collect);
    		} else {
    			collect.add(alt.toTreeBottomup(builder));
    		}
    	}
    }

    /**
     * todo: stolen from TAFReader; move elsewhere
     */
    public static IStrategoList makeList(TermFactory factory, List<IStrategoTerm> terms) {
        IStrategoList result = factory.makeList();
        for (int i = terms.size() - 1; i >= 0; i--) {
        	result = factory.makeListCons(terms.get(i), result);
        }
        return result;
    }

    @Override
    public String toString() {
    	switch (nodeType) {
		case AbstractParseNode.AMBIGUITY:
    		return "amb(" + Arrays.toString(kids) + ")";
		case AbstractParseNode.PARSENODE:
	        return "regular(aprod(" + label + ")," + Arrays.toString(kids) + ")";
		case AbstractParseNode.AVOID:
			return "avoid(" + getLabel() + "," + kids + ")";
		case AbstractParseNode.PREFER:
			return "prefer(" + getLabel() + "," + kids + ")";
		case AbstractParseNode.REJECT:
		    return "reject(" + getLabel() + "," + kids + ")";
		default:
			throw new NotImplementedException();
		}
    }

	@Override
	public int getNodeType() {
		return nodeType;
	}

    @Override
    public AbstractParseNode[] getChildren() {
		return kids;
	}
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ParseNode))
            return false;
        if (obj == this)
            return true;
        final ParseNode o = (ParseNode)obj;
        if (getNodeType() != o.getNodeType() || 
        	label != o.label || 
        	kids.length != o.kids.length || 
        	hashCode() != o.hashCode())
            return false;
        for(int i = 0; i < kids.length; i++) {
            if(!kids[i].equals(o.kids[i]))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (cachedHashCode != NO_HASH_CODE)
            return cachedHashCode;
        final int prime = 31;
        int result = prime * label;
        for(AbstractParseNode n : kids)
            result += (prime * n.hashCode());
        cachedHashCode = result; //Assumption is that hashcode is not set during parsing
        return result;
    }

    @Override
    public String toStringShallow() {
    	if (isAmbNode())
    		return "Amb";
        return "regular*(" + label + ", {" +  kids.length + "})";
    }
	
	/*
	private void log(){
		System.out.println(this.toStringShallow());
		for (int i = 0; i < kids.length; i++) {
			if(kids[i].isParseNode() || kids[i].isAmbNode())
				((ParseNode)kids[i]).log();			
		}
	}*/

}
