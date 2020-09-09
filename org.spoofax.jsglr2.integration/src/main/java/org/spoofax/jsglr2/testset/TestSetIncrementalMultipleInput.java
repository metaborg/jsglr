package org.spoofax.jsglr2.testset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public class TestSetIncrementalMultipleInput extends TestSetMultipleInputs<String[], IncrementalStringInput> {

    public final int[] versions;

    public static TestSetIncrementalMultipleInput fromArgsIncremental(Map<String, String> args) {
        String sourcePath = args.get("sourcePath");
        String extension = args.get("extension");
        int[] versions = args.containsKey("iterations")
            ? versionsFromIteration(Integer.parseInt(args.get("iterations"))) : args.containsKey("versions")
                ? Arrays.stream(args.get("versions").split(",")).mapToInt(Integer::parseInt).toArray() : null;
        return new TestSetIncrementalMultipleInput(sourcePath, extension, versions);
    }

    private static int[] versionsFromIteration(int iteration) {
        if(iteration == 0)
            return new int[] { 0 };
        if(iteration > 0)
            return new int[] { iteration - 1, iteration };
        return null;
    }

    public TestSetIncrementalMultipleInput(String path, String extension, int[] versions) {
        super(path, extension);
        this.versions = versions == null ? Arrays.stream(new File(path).listFiles(File::isDirectory))
            .mapToInt(f -> Integer.parseInt(f.getName())).sorted().toArray() : versions;
    }

    public TestSetIncrementalMultipleInput(String path, String extension) {
        this(path, extension, null);
    }

    @Override public List<IncrementalStringInput> getInputs() {
        List<IncrementalStringInput> inputs = new ArrayList<>();

        Set<String> files = filesInPath(new File(path)).stream().map(File::getName)
            .filter(n -> n.endsWith("." + extension)).collect(Collectors.toSet());
        for(String filename : files) {
            String[] versionInputs = new String[versions.length];
            int k = 0;
            for(int i : versions) {
                try {
                    String input = inputStreamAsString(new FileInputStream(
                        path + (path.endsWith(File.separator) ? "" : File.separator) + i + File.separator + filename));
                    versionInputs[k++] = input;
                } catch(NullPointerException | IOException ignored) {
                    versionInputs[k] = k > 0 ? versionInputs[k - 1] : "";
                    k++;
                }
            }
            inputs.add(getInput(filename, versionInputs));
        }

        return inputs;
    }

    @Override protected IncrementalStringInput getInput(String filename, String[] input) {
        return new IncrementalStringInput(filename, input);
    }
}
