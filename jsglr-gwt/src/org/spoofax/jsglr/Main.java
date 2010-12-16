/*
 * Created on 03.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.Tools;
import org.spoofax.jsglr.shared.terms.ATermFactory;

public class Main {

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
			} else {
				System.err.println("Unknown option: " + args[i]);
				System.exit(1);
			}
		}

		if(parseTableFile == null) {
			usage();
		}

		final ATermFactory factory = new ATermFactory();
		long tableLoadingTime = System.currentTimeMillis();
		final ParseTable pt = new ParseTable(factory.parseFromString(FileTools.loadFileAsString(parseTableFile)));
		final SGLR sglr = new SGLR(factory, pt);

		tableLoadingTime = System.currentTimeMillis() - tableLoadingTime;

		Tools.setDebug(debugging);
		Tools.setLogging(logging);
		sglr.getDisambiguator().setFilterCycles(detectCycles);
		sglr.getDisambiguator().setFilterAny(filter);
		sglr.getDisambiguator().setHeuristicFilters(heuristicFilters);
		sglr.setBuildParseTree(buildParseTree);

		if(waitForProfiler) {
			System.err.println("Hit enter to start profiling...");
			System.in.read();
		}

		for(int i = 0; i < profilingRuns - 1; i++) {
			parseFile(input, null, sglr, startSymbol);
		}

		final long parsingTime = parseFile(input, output, sglr, startSymbol);

		if(timing) {
			System.err.println("Parse table loading time : " + tableLoadingTime + "ms");
			System.err.println("Parsing time             : " + parsingTime + "ms");
		}
	}

	public static long parseFile(String input, String output, SGLR sglr, String startSymbol)
	throws FileNotFoundException, IOException {
		InputStream fis = null;
		if(input == null) {
			fis = System.in;
		} else {
			fis = new BufferedInputStream(new FileInputStream(input));
		}
		OutputStream ous = null;
		if(output != null && !"-".equals(output)) {
			ous = new FileOutputStream(output);
		} else {
			ous = System.out;
		}

		long parsingTime = 0;
		Object t = null;
		try {
			parsingTime = System.currentTimeMillis();
			t = sglr.parse(FileTools.loadFileAsString(input), startSymbol);
			parsingTime = System.currentTimeMillis() - parsingTime;
		} catch(final BadTokenException e) {
			System.err.println("Parsing failed : " + e.getMessage());
		} catch(final SGLRException e) {
			// Detailed message for other exceptions
			System.err.println("Parsing failed : " + e);
		}
		if(t != null && !"-".equals(output)){
			final String outputString = t.toString();
			ous.write(outputString.getBytes());
		}
		return parsingTime;
	}

	private static void usage() {
		System.out.println("Usage: org.spoofax.jsglr.Main [-f -d -v] -p <parsetable.tbl> -i <inputfile>");
		System.exit(-1);
	}
}
