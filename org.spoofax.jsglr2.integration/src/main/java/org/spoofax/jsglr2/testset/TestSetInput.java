package org.spoofax.jsglr2.testset;

public abstract class TestSetInput {

    public enum Type {
        SINGLE, // A single file from the org.spoofax.jsglr samples resources directory
        MULTIPLE, // All files with a certain extension from an absolute path
        SIZED // Custom input string of dynamic size based on given argument
    }

    public final Type type;

    protected TestSetInput(Type type) {
        this.type = type;
    }

}
