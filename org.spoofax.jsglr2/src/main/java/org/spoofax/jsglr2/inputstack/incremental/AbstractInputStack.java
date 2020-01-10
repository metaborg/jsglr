package org.spoofax.jsglr2.inputstack.incremental;

import static org.metaborg.parsetable.characterclasses.CharacterClassFactory.EOF_INT;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.inputstack.IInputStack;

public abstract class AbstractInputStack implements IInputStack {
    protected final String inputString;
    protected final FileObject resource;
    protected final String fileName;
    protected final int inputLength;
    protected int currentOffset = 0;

    public AbstractInputStack(String inputString, @Nullable FileObject resource) {
        this.inputString = inputString;
        this.inputLength = inputString.length();
        this.resource = resource;
        this.fileName = resource != null ? resource.getName().getURI() : "";
    }

    @Override public abstract IInputStack clone();

    @Override public String inputString() {
        return inputString;
    }

    @Override public FileObject resource() {
        return resource;
    }

    @Override public String fileName() {
        return fileName;
    }

    @Override public int offset() {
        return currentOffset;
    }

    @Override public int length() {
        return inputLength;
    }

    @Override public int actionQueryCharacter() {
        if(currentOffset < inputLength)
            return inputString.charAt(currentOffset);
        if(currentOffset == inputLength)
            return EOF_INT;
        else
            return -1;
    }

    @Override public String actionQueryLookahead(int length) {
        return inputString.substring(currentOffset + 1, Math.min(currentOffset + 1 + length, inputLength))
            + (currentOffset + 1 + length > inputLength ? (char) EOF_INT : "");
    }

    @Override public int getChar(int offset) {
        if(offset < inputLength) {
            char c = inputString.charAt(offset);

            if(c > 255)
                throw new IllegalStateException("Unicode not supported");

            return c;
        } else
            return EOF_INT;
    }
}
