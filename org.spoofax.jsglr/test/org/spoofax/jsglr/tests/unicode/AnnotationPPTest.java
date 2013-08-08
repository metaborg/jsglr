package org.spoofax.jsglr.tests.unicode;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spoofax.jsglr.unicode.preprocessor.AnotationPrettyPrinter;

public class AnnotationPPTest {

	@Test
	public void testAnnoPP() {
		AnotationPrettyPrinter pp = new AnotationPrettyPrinter();
		String ppContent = pp.prettyPrintAnnotations("{default(appl(unquoted(\"cons\"),[[fun(quoted(\"\\\"Identifier\\\"\"))]]))}");
		assertEquals("{cons(\"Identifier\")}", ppContent);
	}

}
