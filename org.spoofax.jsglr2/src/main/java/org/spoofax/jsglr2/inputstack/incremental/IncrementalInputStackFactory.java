package org.spoofax.jsglr2.inputstack.incremental;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

public interface IncrementalInputStackFactory<InputStack extends IIncrementalInputStack> {

    InputStack get(IncrementalParseForest updatedTree, String inputString, @Nullable FileObject resource);

}
