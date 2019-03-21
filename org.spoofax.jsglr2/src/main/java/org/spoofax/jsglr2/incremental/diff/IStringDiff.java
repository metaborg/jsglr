package org.spoofax.jsglr2.incremental.diff;

import org.spoofax.jsglr2.incremental.EditorUpdate;

import java.util.List;

public interface IStringDiff {
    List<EditorUpdate> diff(String oldString, String newString);
}
