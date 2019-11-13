package org.spoofax.jsglr2.testset;

import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public abstract class TestSetSizedInput<ContentType, Input extends TestInput<ContentType>>
    extends TestSetInput<ContentType, Input> {

    public interface InputForSize<ContentType> {
        ContentType get(int n);
    }

    private final InputForSize<ContentType> inputForSize;
    public final int[] sizes;

    public TestSetSizedInput(InputForSize<ContentType> inputForSize, int... sizes) {
        super(Type.SIZED);

        this.inputForSize = inputForSize;
        this.sizes = sizes;
    }

    public ContentType get(int n) {
        return inputForSize.get(n);
    }

    @Override public List<Input> getInputs() {
        throw new IllegalStateException("Invalid input type (sized test set input should get a size)");
    }

    public List<Input> getInputs(int n) {
        return Collections.singletonList(getInput("size-" + n, get(n)));
    }

    static class StringInputSet extends TestSetSizedInput<String, StringInput> {
        public StringInputSet(InputForSize<String> inputForSize, int... sizes) {
            super(inputForSize, sizes);
        }

        @Override protected StringInput getInput(String filename, String input) {
            return new StringInput(filename, input);
        }
    }

    static class IncrementalStringInputSet extends TestSetSizedInput<String[], IncrementalStringInput> {
        public IncrementalStringInputSet(InputForSize<String[]> inputForSize, int... sizes) {
            super(inputForSize, sizes);
        }

        @Override protected IncrementalStringInput getInput(String filename, String[] input) {
            return new IncrementalStringInput(filename, input);
        }
    }
}
