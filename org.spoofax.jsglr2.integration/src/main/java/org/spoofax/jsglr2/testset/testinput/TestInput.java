package org.spoofax.jsglr2.testset.testinput;

public abstract class TestInput<ContentType> {

    public String fileName;
    public ContentType content;

    public TestInput(String fileName, ContentType content) {
        this.fileName = fileName;
        this.content = content;
    }

}
