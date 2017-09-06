package org.spoofax.jsglr2.util;

import java.io.IOException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public interface WithGrammar extends WithParseTable {
	
	default void setupParseTableFromDefFile(String grammarName) throws InterruptedException, IOException, ParseError, ParseTableReadException, InvalidParseTableException {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("grammars").getFile();
        
        String grammarDefPath     = path + "/" + grammarName + ".def",
               parseTableTermPath = path + "/" + grammarName + ".tbl";
        
        String command = "sdf2table -i " + grammarDefPath + " -o " + parseTableTermPath + " -m " + grammarName + " -t";
        
        Process sdf2tableProcess = Runtime.getRuntime().exec(command);
        int sdf2tableExitCode = sdf2tableProcess.waitFor();
        
        if (sdf2tableExitCode != 0)
            throw new RuntimeException("sdf2table failed with exit code " + sdf2tableExitCode);
        
        setupParseTableByFilename("grammars/" + grammarName + ".tbl");
    }
	
}
