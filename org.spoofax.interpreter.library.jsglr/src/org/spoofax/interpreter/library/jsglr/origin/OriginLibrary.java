package org.spoofax.interpreter.library.jsglr.origin;

import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class OriginLibrary extends AbstractStrategoOperatorRegistry {

    public static final String REGISTRY_NAME = "ORIGIN";

    public OriginLibrary() {
        add(new SSL_EXT_enable_origins());
        add(new SSL_EXT_enable_desugared_origins());
        add(new SSL_EXT_clone_and_set_parents());
        add(new SSL_EXT_get_parent());
		add(new OriginLocationPrimitive());
		add(new OriginOffsetPrimitive());
		add(new OriginStripPrimitive());
		add(new OriginTermPrimitive());
		add(new OriginTextPrimitive());
		add(new OriginEqualPrimitive());
		
		add(new OriginNonLayoutTokensPrimitive());
		add(new OriginTokensPrimitive());
		add(new OriginTokenStreamPrimitive());

		//origin term strategies
		add(new OriginSublistTermPrimitive());
		add(new OriginDesugaredTermPrimitive());
		add(new OriginTermFuzzyPrimitive());
		
		//layout strategies
		add(new OriginLayoutPrefixPrimitive());
		add(new OriginCommentsAfterPrimitive());
		add(new OriginCommentsBeforePrimitive());
		add(new OriginIndentationPrimitive());
		add(new OriginSeparationPrimitive());
		add(new OriginDeletionOffsetPrimitive());
		add(new OriginInsertBeforeOffsetPrimitive());
		add(new OriginInsertAtEndOffsetPrimitive());
		add(new OriginTextWithLayoutPrimitive());

    }

    public String getOperatorRegistryName() {
        return REGISTRY_NAME;
    }
}
