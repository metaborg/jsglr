package org.spoofax.jsglr2.incremental.diff;

import java.util.List;

import org.spoofax.jsglr2.incremental.EditorUpdate;

public interface IStringDiff {
    List<EditorUpdate> diff(String oldString, String newString);
}
