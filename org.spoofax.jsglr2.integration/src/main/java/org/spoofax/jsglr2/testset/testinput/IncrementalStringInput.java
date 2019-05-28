package org.spoofax.jsglr2.testset.testinput;

public class IncrementalStringInput extends TestInput<String[]> {

    /**
     * @param content
     *            A string, separated with ASCII EOF characters (code 0x04).
     */
    public IncrementalStringInput(String filename, String content) {
        super(filename, content.split("\4"));
    }

    public IncrementalStringInput(String filename, String[] contents) {
        super(filename, contents);
    }

}
