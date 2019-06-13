package org.spoofax.jsglr2.testset.testinput;

public abstract class TestInput<ContentType> {

    public String filename;
    public ContentType content;

    public TestInput(String filename, ContentType content) {
        this.filename = filename;
        this.content = content;
    }

}
