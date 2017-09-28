package org.spoofax.jsglr2.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.metaborg.spoofax.nativebundle.NativeBundle;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public interface WithGrammar extends WithParseTable {
    
    default String grammarsPath() {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("grammars").getFile();
        
        return path;
    }
	
	default void setupParseTableFromDefFile(String grammarName) throws InterruptedException, IOException, ParseError, ParseTableReadException, InvalidParseTableException, URISyntaxException {
	    setupDefFile(grammarName);
	    
        String grammarDefPath     = grammarsPath() + "/" + grammarName + ".def",
               parseTableTermPath = grammarsPath() + "/" + grammarName + ".tbl";
        
        String sdf2tablePath = setupSdf2Table();
        
        String command = sdf2tablePath + " -i " + grammarDefPath + " -o " + parseTableTermPath + " -m " + grammarName + " -t";
        
        Process sdf2tableProcess = Runtime.getRuntime().exec(command);
        int sdf2tableExitCode = sdf2tableProcess.waitFor();
        
        if (sdf2tableExitCode != 0)
            throw new RuntimeException("sdf2table failed with exit code " + sdf2tableExitCode);
        
        setupParseTableByFilename("grammars/" + grammarName + ".tbl");
    }
	
	default String setupSdf2Table() throws URISyntaxException, IOException {
		String targetPath = new File(WithGrammar.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent(); // Path of the target directory
        
		// Create a separate directory for the native sdf2table file
        new File(targetPath + "/native").mkdirs();
		
        String pathInTargetDir = targetPath + "/native/" + NativeBundle.getSdf2TableName();
        
        // Copy the sdf2table executable to the target/native directory
        Files.copy(Sdf2Table.getNativeInputStream(), Paths.get(pathInTargetDir), StandardCopyOption.REPLACE_EXISTING);
        
        // Make it executable
        new File(pathInTargetDir).setExecutable(true);
        
        return pathInTargetDir;
	}
    
    default void setupDefFile(String grammarName) throws IOException {}
	
}
