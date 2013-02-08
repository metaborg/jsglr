package org.spoofax.jssglr.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITreeFactory;
import com.google.gwt.core.client.JavaScriptObject;

@SuppressWarnings("rawtypes")
public class STRJSNativeTermFactory implements ITreeFactory
{
	private boolean _enableTokens = true;
	
	public STRJSNativeTermFactory() {
		super();
	}

	private native JavaScriptObject makeConstructor(String name, int arity ) /*-{
		return StrategoJS.Term.makeCtor(name, arity); 
	}-*/;
	
	private native JavaScriptObject makeList() /*-{
		return StrategoJS.Term.makeEmptyList(); 
	}-*/;
	
	private native JavaScriptObject makeList(Object ls) /*-{
		return StrategoJS.Term.makeList(ls); 
	}-*/;
	
	private native JavaScriptObject makeListCons(Object hd, Object tl) /*-{
		return StrategoJS.Term.makeListCons(hd, tl); 
	}-*/;

	private native JavaScriptObject makeAppl(Object ctor, Object args) /*-{
		return StrategoJS.Term.makeAppl(ctor, args); 
	}-*/;
	
	private native JavaScriptObject makeInt(int i) /*-{
		return StrategoJS.Term.makeInt(i); 
	}-*/;

	private native JavaScriptObject makeReal(double d) /*-{
		return StrategoJS.Term.makeReal(d); 
	}-*/;	
	
	private native JavaScriptObject makeString(String s) /*-{
		return StrategoJS.Term.makeString(s); 
	}-*/;
	
	private native JavaScriptObject makeTuple(Object args) /*-{
		return StrategoJS.Term.makeTuple(args); 
	}-*/;	
	
	private native JavaScriptObject recreateNodeNative(Object node, Object children) /*-{
		var constructor = node.getConstructor();
		
		var result = StrategoJS.Term.makeAppl(constructor, children);
		return result;
	}-*/;
	
	private native JavaScriptObject addToken(Object term, Object tokenLeft, Object tokenRight) /*-{
		var x = StrategoJS.Term.setEditorToken(term, {tokenLeft: tokenLeft, tokenRight: tokenRight});
		return x;
	}-*/;	
	
	private native IToken getLeftTokenFromTerm(Object term) /*-{
		var token = null;
		if (term.editorToken !== undefined) 
			token = term.editorToken.tokenLeft;
		return token;
	}-*/;
	
	private native IToken getRightTokenFromTerm(Object term) /*-{
		var token = null;
		if (term.editorToken !== undefined) 
			token = term.editorToken.tokenRight;
		return token;
	}-*/;	
	
	private native void debug(Object o) /*-{
		var x = o;
		debugger; 
	}-*/;
	
	private native JavaScriptObject[] getChildrenNative(Object term) /*-{
			var kids = new Array();
			var count = StrategoJS.Term.getSubtermCount(term);
			var i = 0;
			while (i < count)
			{
				var st = StrategoJS.Term.getSubterm(term, i);
				kids.push(st);
				i++;
			}
			return kids;
	}-*/;
	
	
	public native JavaScriptObject exposeFactory() /*-{
		var self = this;
		self.@org.spoofax.jssglr.client.services.NativeTermFactory::constructorCache = {};
		var factory = {};
		factory.makeConstructor = function(name, arity) {
			
			var self = this;
			var constructor_cache = self.@org.spoofax.jssglr.client.services.NativeTermFactory::constructorCache;
	        var cache = constructor_cache[name];
		    if(cache === undefined) {
		        constructor_cache[name] = cache = {};
		    }
		    var ctor = cache[arity];
		    if(ctor === undefined) {
		        cache[arity] = ctor = self.@org.spoofax.jssglr.client.services.NativeTermFactory::makeNewConstructor(Ljava/lang/String;I)(name, arity);
		    }
		    return ctor;	
		};
		factory.makeString = function(s) {
			return self.@org.spoofax.jssglr.client.services.NativeTermFactory::makeString(Ljava/lang/String;)(s);
		};
		factory.makeAppl = function (ctr, terms, annotations) {
			return self.@org.spoofax.jssglr.client.services.NativeTermFactory::makeAppl(Lorg/spoofax/interpreter/terms/IStrategoConstructor;[Lorg/spoofax/interpreter/terms/IStrategoTerm;Lorg/spoofax/interpreter/terms/IStrategoList;)(ctr, terms, annotations);
		};
		factory.makeInt = function(i) {
			return self.@org.spoofax.jssglr.client.services.NativeTermFactory::makeInt(I)(i);
		};
		factory.makeList = function (terms, outerAnnos) {
			return self.@org.spoofax.jssglr.client.services.NativeTermFactory::makeList([Lorg/spoofax/interpreter/terms/IStrategoTerm;Lorg/spoofax/interpreter/terms/IStrategoList;)(terms, outerAnnos);
		};
		
		factory.makeEmptyList = function() {
			return self.@org.spoofax.jssglr.client.services.NativeTermFactory::makeList([Lorg/spoofax/interpreter/terms/IStrategoTerm;Lorg/spoofax/interpreter/terms/IStrategoList;)
		};
		
		factory.result = null;
		return factory;
	}-*/;



	
	private native void log(String s) /*-{
			//StrategoJS.trace("STRJS: Treebuilder log: " + s); 
	}-*/;

	@Override
	public String tryGetStringValue(Object node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable getChildren(Object node) {
		log("getChildren");
		List<JavaScriptObject> x = new LinkedList<JavaScriptObject>();
		for (JavaScriptObject child : getChildrenNative(node))
			x.add(child);
		return x;
	}

	@Override
	public IToken getLeftToken(Object node) {
		return getLeftTokenFromTerm(node);
	}

	@Override
	public IToken getRightToken(Object node) {
		return getRightTokenFromTerm(node);
	}

	@Override
	public Iterable tryGetAmbChildren(Object node) {
		// TODO Auto-generated method stub
		log("try get amb children - UNIMPLEMENTED");
		return null;
		/*
		if ("amb".equals(node.name())) {
			return iterable((IStrategoList) node.getSubterm(0));
		} else {
			return null;
		}
		*/
	}

	@Override
	public Object createNonTerminal(String sort, String constructor,
			IToken leftToken, IToken rightToken, List children) {
		JavaScriptObject cons = makeConstructor(constructor, children.size());
		Object[] kids = new Object[children.size()];
		int i=0;
		for (Object o : children)
		{
			kids[i]= o;
			i++;
		}
		JavaScriptObject result = makeAppl(cons, kids);
		addTokens(result, leftToken, rightToken);
		return result;
	}

	@Override
	public Object createIntTerminal(String sort, IToken token, int value) {
		Object result = makeInt(value);
		result = addTokens(result, token, token);
		return result;
	}

	@Override
	public Object createRealTerminal(String sort, IToken token, double value) {
		Object result = makeReal(value);
		result = addTokens(result, token, token);
		return result;
	}

	@Override
	public Object createStringTerminal(String sort, IToken leftToken,
			IToken rightToken, String value) {
		log("createStringTerminal " + value);
		if (value == "UserERROR")
			debug(value);
		return addTokens(makeString(value), leftToken, rightToken);
	}

	@Override
	public Object createTuple(String elementSort, IToken leftToken,
			IToken rightToken, List children) {
		Object result = makeTuple(listToJsArray(children));
		result = addTokens(result, leftToken, rightToken);
		return result;
	}
	
	@Override
	public Object createList(String elementSort, IToken leftToken,
			IToken rightToken, List children) {
		log("createList. Sort: " + elementSort);
		Object[] kids = listToJsArray(children);
		return addTokens(makeList(kids), leftToken, rightToken);
	}

	@Override
	public Object createSublist(IStrategoList list, IStrategoTerm firstChild,
			IStrategoTerm lastChild) {
		// TODO Auto-generated method stub
		return "createsublist - UNIMPLEMENTED";
	}

	@Override
	public Object createTop(Object tree, String filename, int ambiguityCount) {
		return tree;
	}

	@Override
	public Object createAmb(List alternatives, IToken leftToken,
			IToken rightToken) {
		List<Object> alternativesInList = new ArrayList<Object>();
		alternativesInList.add(createList(null, leftToken, rightToken, alternatives));
		return createNonTerminal(null, "amb", leftToken, rightToken, alternativesInList);		
	}

	@Override
	public Object recreateNode(Object oldNode, IToken leftToken, IToken rightToken, List children) {
		log("recreateNode");
		Object[] kids = listToJsArray(children);
		JavaScriptObject newNode = recreateNodeNative(oldNode, kids);
		return addTokens(newNode, leftToken, rightToken);
	}

	@Override
	public Object createInjection(String sort, List children) {
		log("Create injection. Sort: "+ sort);
		if (children.size() == 1)
			return children.get(0);
		else
		{
			Object result = makeTuple(listToJsArray(children));
			IToken left = getLeftToken(children.get(0));
			IToken right = getRightToken(children.get(children.size() - 1));
			result = addTokens(result, left, right);
			return result;
		}
	}

	@Override
	public void setEnableTokens(boolean enableTokens) {
		_enableTokens = enableTokens;
	}	
	
	private Object addTokens(Object node, IToken leftToken, IToken rightToken)
	{
		if (_enableTokens)
			node = addToken(node, leftToken, rightToken);
		return node;
	}
	
	private Object[] listToJsArray(List list)
	{
		Object[] arr = new Object[list.size()];
		int i=0;
		for (Object o : list)
		{
			arr[i]= o;
			i++;
		}
		return arr;
	}	
}