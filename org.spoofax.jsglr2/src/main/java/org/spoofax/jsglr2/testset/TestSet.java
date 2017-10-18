package org.spoofax.jsglr2.testset;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestSet {

	public final String name;
	public final TestSetParseTable parseTable;
	public final TestSetInput input;
	
	public TestSet(String name, TestSetParseTable parseTable, TestSetInput input) {
		this.name = name;
		this.parseTable = parseTable;
		this.input = input;
	}
	
	public static TestSet lexical = new TestSet(
		"lexical",
		new TestSetParseTableFromGrammarDef("lexical-id"),
		new TestSetSizedInput(n -> {
			return String.join("", Collections.nCopies(n, "a"));
		}, 10000, 50000, 100000)
	);
	
	public static TestSet sumAmbiguous = new TestSet(
		"sumAmbiguous",
		new TestSetParseTableFromGrammarDef("sum-ambiguous"),
		new TestSetSizedInput(n -> {
			return String.join("+", Collections.nCopies(n, "x"));
		}, 20, 40, 60, 80)
	);
	
	public static TestSet sumNonAmbiguous = new TestSet(
		"sumNonAmbiguous",
		new TestSetParseTableFromGrammarDef("sum-nonambiguous"),
		new TestSetSizedInput(n -> {
			return String.join("+", Collections.nCopies(n, "x"));
		}, 4000, 8000, 16000, 32000, 64000)
	);
	
	public static TestSet csv = new TestSet(
		"csv",
		new TestSetParseTableFromGrammarDef("csv"),
		new TestSetSizedInput(n -> {
			return String.join("\n", Collections.nCopies(n, "1234567890,\"abcdefghij\",1234567890,\"abcdefghij\",1234567890,\"abcdefghij\",1234567890,\"abcdefghij\",1234567890,\"abcdefghij\""));
		}, 1000, 2000, 4000)
	);
	
	public static TestSet java8 = new TestSet(
		"java8",
		new TestSetParseTableFromATerm("Java8"),
		new TestSetMultipleInputs("/Users/Jasper/git/spoofax-releng/mb-rep/org.spoofax.terms", "java")
	);
	
	public static TestSet greenMarl = new TestSet(
		"greenmarl",
		new TestSetParseTableFromATerm("GreenMarl"),
		new TestSetSingleInput("GreenMarl/infomap.gm")
	);
	
	public static TestSet webDSL = new TestSet(
		"webdsl",
		new TestSetParseTableFromATerm("WebDSL"),
		new TestSetSingleInput("WebDSL/built-in.app")
	);
	
	public static List<TestSet> all = Arrays.asList(lexical, sumAmbiguous, sumNonAmbiguous, csv, java8, greenMarl, webDSL);
	
}
