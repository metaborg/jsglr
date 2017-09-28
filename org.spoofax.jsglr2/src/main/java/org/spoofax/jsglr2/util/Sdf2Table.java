package org.spoofax.jsglr2.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.SystemUtils;
import org.metaborg.spoofax.nativebundle.NativeBundle;

public class Sdf2Table {
	
	private static boolean isSetUp = false;
	private static String pathInTargetDir;
	
	public static String getPathInTargetDir() throws URISyntaxException, IOException {
		if (!isSetUp) {
			setupSdf2TableInTargetDir();
			
			isSetUp = true;
		}
		
		return pathInTargetDir;
	}
	
	public static void setupSdf2TableInTargetDir() throws URISyntaxException, IOException {
		String targetPath = new File(WithGrammar.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent(); // Path of the target directory
        
		// Create a separate directory for the native sdf2table file
        new File(targetPath + "/native").mkdirs();
		
        pathInTargetDir = targetPath + "/native/" + NativeBundle.getSdf2TableName();
        
        if (!new File(pathInTargetDir).exists()) { // Only copy sdf2table to the target directory once
        		// Copy the sdf2table executable to the target/native directory
	        Files.copy(Sdf2Table.getNativeInputStream(), Paths.get(pathInTargetDir), StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Make it executable
        new File(pathInTargetDir).setExecutable(true);
	}
	
	public static InputStream getNativeInputStream() {
		return getResourceAsStream(getNativeDirectory() + NativeBundle.getSdf2TableName());
	}

	public static String getNativeDirectory() {
        if(SystemUtils.IS_OS_WINDOWS) {
            return "native/cygwin/";
        } else if(SystemUtils.IS_OS_MAC_OSX) {
            return "native/macosx/";
        } else if(SystemUtils.IS_OS_LINUX) {
            return "native/linux/";
        } else {
            throw new UnsupportedOperationException("Unsupported platform " + SystemUtils.OS_NAME);
        }
    }
	
	private static InputStream getResourceAsStream(String name) {
        final InputStream inputStream = NativeBundle.class.getResourceAsStream(name);
        
        if(inputStream == null)
            throw new IllegalStateException("Resource " + name + " cannot be found in the native bundle");

        return inputStream;
    }
	
}
