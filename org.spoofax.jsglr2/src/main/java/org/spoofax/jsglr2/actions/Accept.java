package org.spoofax.jsglr2.actions;

import org.metaborg.parsetable.actions.IAccept;

public class Accept implements IAccept {

    public static final Accept SINGLETON = new Accept();

    public Accept() {
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() != o.getClass());
    }

}
