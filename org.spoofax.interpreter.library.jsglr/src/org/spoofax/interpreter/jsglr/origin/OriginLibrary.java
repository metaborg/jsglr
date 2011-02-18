package org.spoofax.interpreter.jsglr.origin;

import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class OriginLibrary extends AbstractStrategoOperatorRegistry {

    public static final String REGISTRY_NAME = "ORIGIN";

    public OriginLibrary() {
        add(new SSL_EXT_enable_origins());
    }

    public String getOperatorRegistryName() {
        return REGISTRY_NAME;
    }
}
