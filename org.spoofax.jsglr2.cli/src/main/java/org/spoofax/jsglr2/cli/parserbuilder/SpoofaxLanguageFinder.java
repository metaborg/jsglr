package org.spoofax.jsglr2.cli.parserbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.commons.io.comparator.NameFileComparator;
import org.spoofax.jsglr2.cli.WrappedException;

@SuppressWarnings("WeakerAccess")
public final class SpoofaxLanguageFinder {
    /**
     * This only works when the repoName is formatted like "path/to/group/id/artifactId[/version]". Providing a version
     * is optional; note however that the version you provide must already be installed. Example: for the groupId
     * "org.metaborg" and the artifactId "lang.java", you would be able to find this language using
     * "org/metaborg/lang.java".
     *
     * @param repoName
     *            The identifier of the language in the repository, in the format "[groupId]/[artifactId]/[version]",
     *            where the groupId is slash-separated, the artifactId is period-separated, and the version is optional.
     *            Example: "org/metaborg/lang.java/1.1.0-SNAPSHOT"
     * @return A *.spoofax-language file.
     */
    public static File getSpoofaxLanguage(String repoName) throws WrappedException {
        File repoFolder = new File(getLocalRepository()).toPath().resolve(repoName).toFile();
        File[] versionDirs = repoFolder.listFiles(File::isDirectory);

        // If we find version directories, the version is not provided. We choose the latest version.
        if(versionDirs != null && versionDirs.length > 0) {
            Arrays.sort(versionDirs, NameFileComparator.NAME_REVERSE);
            repoFolder = versionDirs[0];
        }

        // If no version directories are found, either the version is already provided or there's nothing
        File[] languageFiles = repoFolder.listFiles((f, n) -> n.endsWith(".spoofax-language"));
        if(languageFiles == null || languageFiles.length == 0)
            // In this case, there's nothing :(
            throw new WrappedException("Language not found in local Maven repository: " + repoName);

        // There can be multiple *.spoofax-language files; take the one with the latest version name
        Arrays.sort(languageFiles, NameFileComparator.NAME_REVERSE);
        return languageFiles[0];
    }

    private static String getLocalRepository() throws WrappedException {
        try {
            return new BufferedReader(new InputStreamReader(Runtime.getRuntime()
                .exec("mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout").getInputStream()))
                    .readLine();
        } catch(Exception e) {
            throw new WrappedException("Could not locate local Maven repository", e);
        }
    }
}
