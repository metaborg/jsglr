package org.spoofax.jssglr.client.services;

import org.spoofax.terms.StrategoConstructor;
import org.spoofax.terms.TermFactory;

import com.google.gwt.core.client.JavaScriptObject;

public class NativeTermFactory extends TermFactory {
	JavaScriptObject constructorCache;

	public NativeTermFactory() {
		super();
	}

	public StrategoConstructor makeConstructor(String name, int arity) {
        return nativeMakeConstructor(name, arity);
    }

	public StrategoConstructor makeNewConstructor(String name, int arity) {
        return new StrategoConstructor(name, arity);
    }

	public native StrategoConstructor nativeMakeConstructor(String name, int arity)  /*-{
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
    }-*/;

	public native JavaScriptObject exposeFactory(TermFactory factory) /*-{
		var self = this;
		self.@org.spoofax.jssglr.client.services.NativeTermFactory::constructorCache = {};
		var factory = {};
		factory.makeConstructor = function(name, arity) {
			return self.@org.spoofax.jssglr.client.services.NativeTermFactory::nativeMakeConstructor(Ljava/lang/String;I)(name, arity);
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
		factory.result = null;
		return factory;
	}-*/;
}
