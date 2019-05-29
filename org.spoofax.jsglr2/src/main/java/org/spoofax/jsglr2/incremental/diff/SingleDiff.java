package org.spoofax.jsglr2.incremental.diff;

import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.incremental.EditorUpdate;

/**
 * Always returns a single EditorUpdate between the first and last change.
 */
public class SingleDiff implements IStringDiff {

    @Override public List<EditorUpdate> diff(String oldString, String newString) {
        int begin = 0, endOld = oldString.length() - 1, endNew = newString.length() - 1;
        while(begin <= endOld && begin <= endNew && oldString.charAt(begin) == newString.charAt(begin))
            begin++;
        while(endOld > begin && endNew > begin && oldString.charAt(endOld) == newString.charAt(endNew)) {
            endOld--;
            endNew--;
        }
        return Collections.singletonList(new EditorUpdate(begin, endOld + 1, newString.substring(begin, endNew + 1)));
    }

}
