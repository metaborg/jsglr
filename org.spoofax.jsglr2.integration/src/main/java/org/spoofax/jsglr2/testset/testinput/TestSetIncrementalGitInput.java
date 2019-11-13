package org.spoofax.jsglr2.testset.testinput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.spoofax.jsglr2.testset.TestSetInput;

public class TestSetIncrementalGitInput extends TestSetInput<String[], IncrementalStringInput> {
    private final String gitDirectory;
    private final String extension;
    private final int depth;

    public TestSetIncrementalGitInput(String gitDirectory, String extension, int depth) {
        super(Type.INCREMENTAL);
        this.gitDirectory = gitDirectory;
        this.extension = extension;
        this.depth = depth;
    }

    @Override protected IncrementalStringInput getInput(String filename, String[] input) {
        return new IncrementalStringInput(filename, input);
    }

    @Override public List<IncrementalStringInput> getInputs() throws IOException {
        Git git = Git.open(new File(gitDirectory));
        RevWalk revWalk = new RevWalk(git.getRepository());
        revWalk.markStart(revWalk.parseCommit(git.getRepository().getRefDatabase().findRef("master").getObjectId()));

        Map<String, List<String>> map = new HashMap<>();
        List<String> emptyList = Collections.nCopies(depth, "");

        try {
            int i = 0;
            for(RevCommit commit : revWalk) {
                // System.err.println(commit);
                git.checkout().setName(commit.getName()).call();

                for(File file : filesInPath(new File(gitDirectory))) {
                    String filename = file.getAbsolutePath();
                    if(filename.endsWith("." + extension)) {
                        if(!map.containsKey(filename)) {
                            map.put(filename, new ArrayList<>(emptyList));
                        }
                        map.get(filename).set(i, inputStreamAsString(new FileInputStream(file)));
                    }
                }
                i++;
                if(i >= depth)
                    break;
            }
            git.checkout().setName("master").call();
        } catch(GitAPIException e) {
            throw new IOException(e);
        }

        return map.entrySet().stream()
            .map(e -> new IncrementalStringInput(e.getKey(), e.getValue().toArray(new String[0])))
            .collect(Collectors.toList());
    }

    private Set<File> filesInPath(File path) {
        Set<File> acc = new HashSet<>();

        filesInPath(path, acc);

        return acc;
    }

    private void filesInPath(final File path, Set<File> acc) {
        File[] files = path.listFiles();
        if(files == null)
            throw new IllegalStateException("Directory " + path + " not found!");
        for(final File subPath : files) {
            if(subPath.isDirectory())
                filesInPath(subPath, acc);
            else
                acc.add(subPath);
        }
    }

}
