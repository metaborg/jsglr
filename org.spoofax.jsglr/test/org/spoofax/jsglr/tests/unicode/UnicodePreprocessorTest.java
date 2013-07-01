package org.spoofax.jsglr.tests.unicode;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.spoofax.jsglr.unicode.preprocessor.UnicodeCleaner;

public class UnicodePreprocessorTest {

	@Test
	public void testUnicodeCleaner() throws IOException {
		File file = UnicodeCleaner.removeUnicode(new File("tests/grammars/basic/Unicode.sdf"));
		file.deleteOnExit();
		String text = TestUnicode.readFile(file, null);
		text = text.replaceAll("[\n,\t]+", " ");
		System.out.println(text);
		Assert.assertEquals("module Unicode " +
				"exports context-free syntax "+
				"Hallo \"sd\\u2f9f4dssdf\" Hallo2 -> Hallo "+
				"[a-\\u3c6] Word -> SpecialWord " +
				"lexical syntax "+
				"~[a-z] -> Word", text);
		
	}

}
