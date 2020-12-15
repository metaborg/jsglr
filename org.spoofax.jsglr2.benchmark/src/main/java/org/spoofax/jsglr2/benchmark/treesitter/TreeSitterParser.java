package org.spoofax.jsglr2.benchmark.treesitter;

import static com.github.mpsijm.javatreesitter.JavaTreeSitterLibrary.*;

import org.bridj.Pointer;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.diff.JGitHistogramDiff;

import com.github.mpsijm.javatreesitter.JavaTreeSitterLibrary;
import com.github.mpsijm.javatreesitter.TSInputEdit;
import com.github.mpsijm.javatreesitter.TSLanguage;

public class TreeSitterParser {
    private final Pointer<JavaTreeSitterLibrary.TSParser> parser;
    private final IStringDiff diff;

    public TreeSitterParser(Pointer<TSLanguage> language) {
        parser = ts_parser_new();
        ts_parser_set_language(parser, language);
        diff = new JGitHistogramDiff();
    }

    public Pointer<TSTree> parse(String input) {
        return parse(input, null, null);
    }

    public Pointer<TSTree> parse(String input, String previousInput, Pointer<TSTree> previousTree) {
        Pointer<TSTree> copyOfPrevious =
            previousInput != null && previousTree != null ? ts_tree_copy(previousTree) : null;

        if(copyOfPrevious != null) {
            for(EditorUpdate update : diff.diff(previousInput, input)) {
                Pointer<TSInputEdit> edit = Pointer.allocate(TSInputEdit.class);
                edit.get().start_byte(update.deletedStart).old_end_byte(update.deletedEnd)
                    .new_end_byte(update.deletedStart + update.inserted.length());

                ts_tree_edit(copyOfPrevious, edit);

                edit.release();
            }
        }

        return ts_parser_parse_string(parser, copyOfPrevious, Pointer.pointerToCString(input), input.length());
    }
}
