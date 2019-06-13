package org.spoofax.jsglr2.testset;

import java.util.Collections;
import java.util.List;

public abstract class TestSetSizedInput<Input> extends TestSetInput<Input> {

    public interface InputForSize {
        String get(int n);
    }

    private final InputForSize inputForSize;
    public final int[] sizes;

    public TestSetSizedInput(InputForSize inputForSize, int... sizes) {
        super(Type.SIZED);

        this.inputForSize = inputForSize;
        this.sizes = sizes;
    }

    public String get(int n) {
        return inputForSize.get(n);
    }

    @Override public List<Input> getInputs() {
        throw new IllegalStateException("Invalid input type (sized test set input should get a size)");
    }

    public List<Input> getInputs(int n) {
        return Collections.singletonList(getInput("", get(n)));
    }

    static class StringInputSet extends TestSetSizedInput<StringInput> {
        public StringInputSet(InputForSize inputForSize, int... sizes) {
            super(inputForSize, sizes);
        }

        @Override protected StringInput getInput(String filename, String input) {
            return new StringInput(filename, input);
        }
    }

}
