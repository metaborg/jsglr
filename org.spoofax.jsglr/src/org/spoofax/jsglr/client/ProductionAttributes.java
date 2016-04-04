/*
 * Created on 16.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import static org.spoofax.jsglr.client.ProductionType.AVOID;
import static org.spoofax.jsglr.client.ProductionType.PREFER;

import java.io.Serializable;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class ProductionAttributes implements Serializable {

    private static final long serialVersionUID = 556855017447626835L;

    private final int type;

    private final boolean isRecover;
    private final boolean isBracket;
    private final boolean isCompletion;
    private final boolean isPlaceholderInsertion;
    private final boolean isLiteralCompletion;

    private final boolean isIgnoreLayout;
    private final IStrategoTerm layoutConstraint;
    private final boolean isNewlineEnforced;
    private final boolean isLongestMatch;


    private final transient IStrategoTerm abstractCtor;

    ProductionAttributes(IStrategoTerm ctor, int type, boolean isRecover, boolean isBracket, boolean isCompletion, boolean isPlaceholderInsertion, boolean isLiteralCompletion, boolean isIgnoreIndent, IStrategoTerm layoutConstraint, boolean isNewlineEnforced, boolean isLongestMatch) {
        this.type = type;
        this.abstractCtor = ctor;
        this.isRecover = isRecover;
        this.isBracket = isBracket;
        this.isCompletion = isCompletion;
        this.isPlaceholderInsertion = isPlaceholderInsertion;
        this.isLiteralCompletion = isLiteralCompletion;

        this.isIgnoreLayout = isIgnoreIndent;
        this.layoutConstraint = layoutConstraint;
        this.isNewlineEnforced = isNewlineEnforced;
        this.isLongestMatch = isLongestMatch;
    }

    public final int getType() {
        return type;
    }

    public final IStrategoTerm getTerm() {
        return abstractCtor;
    }

    public boolean isRecoverProduction() {
        return isRecover;
    }
    
    public boolean isIgnoreLayout() {
      return isIgnoreLayout;
    }
    
    public IStrategoTerm getLayoutConstraint() {
      return layoutConstraint;
    }
    
    public boolean isNewlineEnforced() {
      return isNewlineEnforced;
    }

    public boolean isCompletionProduction() {
        return isCompletion;
    }
    
    public boolean isPlaceholderInsertionProduction() {
        return isPlaceholderInsertion;
    }
    

    public boolean isMoreEager(ProductionAttributes other) {
        return type != other.type && (type == PREFER || other.type == AVOID);
    }

    public boolean isLongestMatch() {
      return isLongestMatch;
    }

    public boolean isLiteralCompletionProduction() {
        // TODO Auto-generated method stub
        return isLiteralCompletion;
    }

    public boolean isBracket() {
        return isBracket;
    }
}
