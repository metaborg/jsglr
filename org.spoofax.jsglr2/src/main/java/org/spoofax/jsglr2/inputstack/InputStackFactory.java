package org.spoofax.jsglr2.inputstack;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;

public interface InputStackFactory<InputStack> {

    InputStack get(String inputString, @Nullable FileObject resource);

}
