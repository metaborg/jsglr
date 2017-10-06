package org.spoofax.jsglr2.testset;

public class TestSetSizedInput extends TestSetInput {

	final InputForSize inputForSize;
	
	public TestSetSizedInput(InputForSize inputForSize) {
		super(Type.SIZED);
		
		this.inputForSize = inputForSize;
	}
	
	public interface InputForSize {
		String get(int n);
	}
	
	public Input get(int n) {
		return new Input("", inputForSize.get(n));
	}
	
}
