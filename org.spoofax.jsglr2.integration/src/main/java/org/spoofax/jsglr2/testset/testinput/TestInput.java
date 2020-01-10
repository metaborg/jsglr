package org.spoofax.jsglr2.testset.testinput;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.util.LocalFileObject;

public abstract class TestInput<ContentType> {

    public String filename;
    public FileObject resource;
    public ContentType content;

    public TestInput(String filename, ContentType content) {
        this.filename = filename;
        this.resource = LocalFileObject.get(filename);
        this.content = content;
    }

}
