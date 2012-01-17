package org.spoofax.interpreter.library.jsglr;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class TestJSGLRLibrary {
	
	@Test
	public void testPresenceOfStrategies() throws Exception {
		InputStream ctree = JSGLRLibrary.class.getClassLoader().getResourceAsStream("share/libstratego-jsglr.ctree");
		assertNotNull(ctree);
	}

}
