package org.spoofax.jsglr2.testset;

public class TestSetSizedInput extends TestSetInput {

    final InputForSize inputForSize;
    public final int[] sizes;

    public TestSetSizedInput(InputForSize inputForSize) {
        super(Type.SIZED);

        this.inputForSize = inputForSize;
        this.sizes = null;
    }

    public TestSetSizedInput(InputForSize inputForSize, int... sizes) {
        super(Type.SIZED);

        this.inputForSize = inputForSize;
        this.sizes = sizes;
    }

    public interface InputForSize {
        String get(int n);
    }

    public String get(int n) {
        return inputForSize.get(n);
    }

}
