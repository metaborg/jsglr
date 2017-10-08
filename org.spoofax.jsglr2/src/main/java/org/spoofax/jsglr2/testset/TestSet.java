package org.spoofax.jsglr2.testset;

import java.util.Collections;

public class TestSet {

	public final TestSetParseTable parseTable;
	public final TestSetInput input;
	
	public TestSet(TestSetParseTable parseTable, TestSetInput input) {
		this.parseTable = parseTable;
		this.input = input;
	}
	
	public static TestSet lexical = new TestSet(
		new TestSetParseTableFromGrammarDef("lexical-id"),
		new TestSetSizedInput(n -> {
			return String.join("", Collections.nCopies(n, "a"));
		})
	);
	
	public static TestSet sumAmbiguous = new TestSet(
		new TestSetParseTableFromGrammarDef("sum-ambiguous"),
		new TestSetSizedInput(n -> {
			return String.join("+", Collections.nCopies(n, "x"));
		})
	);
	
	public static TestSet sumNonAmbiguous = new TestSet(
		new TestSetParseTableFromGrammarDef("sum-nonambiguous"),
		new TestSetSizedInput(n -> {
			return String.join("+", Collections.nCopies(n, "x"));
		})
	);
	
	public static TestSet csv = new TestSet(
		new TestSetParseTableFromGrammarDef("csv"),
		new TestSetSizedInput(n -> {
			return String.join("\n", Collections.nCopies(n, "1234567890,\"abcdefghij\",1234567890,\"abcdefghij\",1234567890,\"abcdefghij\",1234567890,\"abcdefghij\",1234567890,\"abcdefghij\""));
		})
	);
	
	public static TestSet java8 = new TestSet(
		new TestSetParseTableFromATerm("Java8"),
		new TestSetMultipleInputs("/path/to/java/project", "java")
	);
	
	public static TestSet greenMarl = new TestSet(
		new TestSetParseTableFromATerm("GreenMarl"),
		new TestSetSingleInput("GreenMarl/infomap.gm")
	);
	
	public static TestSet webDSL = new TestSet(
		new TestSetParseTableFromATerm("WebDSL"),
		new TestSetSingleInput("WebDSL/built-in.app")
	);
	
}
