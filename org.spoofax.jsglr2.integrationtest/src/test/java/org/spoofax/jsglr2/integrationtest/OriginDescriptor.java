package org.spoofax.jsglr2.integrationtest;

import java.util.Objects;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import jsglr.shared.ImploderAttachment;

public final class OriginDescriptor {

    public final String constructor;
    public final int startOffset, endOffset;

    public OriginDescriptor(String constructor, int startOffset, int endOffset) {
        this.constructor = constructor;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public static OriginDescriptor from(IStrategoTerm term) {
        if(term instanceof IStrategoAppl) {
            return fromAppl((IStrategoAppl) term);
        }

        return null;
    }

    public static OriginDescriptor fromAppl(IStrategoAppl appl) {
        String constructor = appl.getConstructor().getName();

        ImploderAttachment attachment = appl.getAttachment(ImploderAttachment.TYPE);

        int startOffset = attachment.getLeftToken().getStartOffset();
        int endOffset = attachment.getRightToken().getEndOffset();

        return new OriginDescriptor(constructor, startOffset, endOffset);
    }

    @Override public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        OriginDescriptor other = (OriginDescriptor) obj;

        return Objects.equals(constructor, other.constructor) && startOffset == other.startOffset
            && endOffset == other.endOffset;
    }

    @Override public String toString() {
        return "<'" + constructor + "';" + startOffset + ";" + endOffset + ">";
    }

}
