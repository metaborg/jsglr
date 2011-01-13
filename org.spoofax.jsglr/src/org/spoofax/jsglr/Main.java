/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.NullTreeBuilder;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.FileTools;
import org.spoofax.jsglr.io.SGLR;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public class Main {
	
	private static final String NO_OUTPUT = "-";

	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidParseTableException {

		if(args.length < 2) {
			usage();
		}

		String parseTableFile = null;
		String input = null;
		String output = null;
		String startSymbol = null;
		boolean debugging = false;
		boolean logging = false;
		boolean detectCycles = true;
		boolean filter = true;
		boolean waitForProfiler = false;
		boolean timing = false;
		boolean heuristicFilters = false;
		boolean buildParseTree = true;
		boolean implode = false;
		int profilingRuns = 0;

		for(int i=0;i<args.length;i++) {
			if(args[i].equals("-p")) {
				parseTableFile = args[++i];
			} else if(args[i].equals("-i")) {
				input = args[++i];
			} else if(args[i].equals("-o")) {
				output = args[++i];
			} else if(args[i].equals("-d")) {
				debugging = true;
			} else if(args[i].equals("-v")) {
				logging = true;
			} else if(args[i].equals("-f")) {
				filter = false;
			} else if(args[i].equals("-c")) {
				detectCycles = false;
			} else if(args[i].equals("-s")) {
				startSymbol = args[++i];
			} else if(args[i].equals("--heuristic-filters")) {
				heuristicFilters = args[++i].equals("on");
			} else if(args[i].equals("--wait-for-profiler")) {
				waitForProfiler = true;
			} else if(args[i].equals("--profiler-runs")) {
				profilingRuns = Integer.parseInt(args[++i]);
			} else if(args[i].equals("--timing")) {
				timing = true;
			} else if(args[i].equals("--no-tree-build")) {
				buildParseTree = false;
			} else if(args[i].equals("--implode")) {
				implode = true;
			} else {
				System.err.println("Unknown option: " + args[i]);
				System.exit(1);
			}
		}

		if(parseTableFile == null) {
			usage();
		}

		final TermFactory factory = new TermFactory();
		long tableLoadingTime = System.currentTimeMillis();
		final IStrategoTerm tableTerm = new TermReader(factory).parseFromFile(parseTableFile);
		final ParseTable pt = new ParseTable(tableTerm, factory);
		final SGLR sglr = new SGLR(new Asfix2TreeBuilder(), pt);

		tableLoadingTime = System.currentTimeMillis() - tableLoadingTime;

		Tools.setDebug(debugging);
		Tools.setLogging(logging);
		sglr.getDisambiguator().setFilterCycles(detectCycles);
		sglr.getDisambiguator().setFilterAny(filter);
		sglr.getDisambiguator().setHeuristicFilters(heuristicFilters);
		if (!buildParseTree)
			sglr.setTreeBuilder(new NullTreeBuilder());
		else if (implode)
			sglr.setTreeBuilder(new TreeBuilder(true));

		if(waitForProfiler) {
			System.err.println("Hit enter to start profiling...");
			System.in.read();
		}
		
		String inputFile = FileTools.loadFileAsString(new BufferedReader(new FileReader(input)));

		for(int i = 0; i < profilingRuns - 1; i++) {
			parseFile(inputFile, NO_OUTPUT, sglr, startSymbol);
		}

		final long parsingTime = parseFile(inputFile, output, sglr, startSymbol);

		if(timing) {
			System.err.println("Parse table loading time : " + tableLoadingTime + "ms");
			System.err.println("Parsing time             : " + parsingTime + "ms");
		}
	}

	public static long parseFile(String input, String output, SGLR sglr, String startSymbol)
	throws FileNotFoundException, IOException {
		/* TODO: support stdin input
		InputStream fis = null;
		if(input == null) {
			fis = new BufferedInputStream(System.in);
		} else {
			fis = new BufferedInputStream(new FileInputStream(input));
		}
		*/
		Writer out = null;
		if(output != null && !NO_OUTPUT.equals(output)) {
			out = new BufferedWriter(new FileWriter(output));
		} else {
			out = new BufferedWriter(new OutputStreamWriter(System.out));
		}

		long parsingTime = 0;
		Object t = null;
		try {
			parsingTime = System.currentTimeMillis();
			t = sglr.parse(input, startSymbol);
			parsingTime = System.currentTimeMillis() - parsingTime;
		} catch(final BadTokenException e) {
			System.err.println("Parsing failed : " + e.getMessage());
		} catch(final SGLRException e) {
			// Detailed message for other exceptions
			System.err.println("Parsing failed : " + e);
		}
		if(t != null && !NO_OUTPUT.equals(output)) {
			TermReader termIO = new TermReader(sglr.getParseTable().getFactory());
			termIO.unparseToFile((IStrategoTerm) t, out);
			out.close();
		}
		return parsingTime;
	}

	private static void usage() {
		System.out.println("Usage: org.spoofax.jsglr.Main [-f -d -v --no-tree-build --implode] -p <parsetable.tbl> -i <inputfile>");
		System.exit(-1);
	}
}
