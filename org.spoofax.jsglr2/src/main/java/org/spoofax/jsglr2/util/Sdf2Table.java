package org.spoofax.jsglr2.util;

import java.io.InputStream;

import org.apache.commons.lang3.SystemUtils;
import org.metaborg.spoofax.nativebundle.NativeBundle;

public class Sdf2Table {
	
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
