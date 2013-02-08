package org.spoofax.jssglr.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.Asfix2TreeBuilder;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.MemoryRecordingTreeBuilder;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;


public class JSMain {
	
	///To be able to call jssglr from CLI with a (stipped version of) the usual cli parameters
	
	public static native void JsPrintErr(String str) /*-{
		console.error(str);
	}-*/;
	
	public static native void JsPrintln(String str) /*-{
		console.log(str);
	}-*/;
	
	
	private static final String NO_OUTPUT = "-";
	
	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidParseTableException
	{
		String output = null;
		String parseTableFile = null;
		String inputFile = null;
		String startSymbol = null;
		boolean timing = false;
		boolean measureMemory = false;
		boolean implode = false;
		int profilingRuns = 1;
		int warmup = 0;
		
		//JsPrintln("Len="+args.length);
		//for (int i=0;i<args.length;i++)
			//JsPrintln("Arg[" + i + "] = " + args[i]);
			
		
		if(args.length < 4) {
			JsPrintln("Usage: node thisfile.js -p <parsetable.tbl> -i <inputfile>\n\nOptional parameters: \n--timing\n--measure-memory\n--warmup");
			return;
		}
		
		for(int i=0;i<args.length;i++) {
			if(args[i].equals("-p")) {
				parseTableFile = args[++i];
			} else if(args[i].equals("-i")) {
				inputFile = args[++i];
			} else if(args[i].equals("--timing")) {
				timing = true;
			} else if(args[i].equals("--measure-memory")) { 
				measureMemory = true;
			} else if(args[i].equals("--warmup")) {
				warmup = Integer.parseInt(args[++i]);
			} else if(args[i].equals("-s")) {
				startSymbol = args[++i];
			} else if(args[i].equals("-o")) {
				output = args[++i];				
			} else if(args[i].equals("--implode")) {
				implode = true;
			}
			
		}
		
		
		ITreeBuilder treeBuilder = new Asfix2TreeBuilder(); 
		final ITermFactory factory = new TermFactory();
		long tableLoadingTime = System.currentTimeMillis();
		final IStrategoTerm tableTerm = new TermReader(factory).parseFromFile(parseTableFile);
		final ParseTable pt = new ParseTable(tableTerm, factory);
		final SGLR sglr = new SGLR(treeBuilder, pt);
		
		//sglr.setUseStructureRecovery(recover);
		
		
		if (implode)
			treeBuilder = new TreeBuilder(new TermTreeFactory(new TermFactory()), true);		
		

		tableLoadingTime = System.currentTimeMillis() - tableLoadingTime;
		MemoryRecordingTreeBuilder memory = null;
		if(measureMemory) {
			memory = new MemoryRecordingTreeBuilder(treeBuilder);
			sglr.setTreeBuilder(memory);
		} else {
			sglr.setTreeBuilder(treeBuilder);
		}
		
		String input = File2String.GetContentsOf(inputFile);//  FileTools.loadFileAsString(new BufferedReader(new FileReader(inputFile)));
		
		if (timing) System.gc();

		if (warmup > 0)
			warmup(sglr, inputFile, input, startSymbol, warmup);

		long parsingTime = 0;
		for(int i = 0; i < profilingRuns - 1; i++) {
			parsingTime += parseFile(input, inputFile, NO_OUTPUT, sglr, startSymbol);
			System.gc();
		}

		parsingTime += parseFile(input, inputFile, output, sglr, startSymbol);
		if(timing) {
			JsPrintErr("Parse table loading time : " + tableLoadingTime + "ms");
			JsPrintErr("Parsing time             : " + (parsingTime / profilingRuns) + "ms");
		}
		
		if(measureMemory) {
			JsPrintErr("Total memory (min/max)   : " + memory.getMinTotal() + " / " + memory.getMaxTotal());
			JsPrintErr("Used memory (min/max)    : " + memory.getMinUsed() + " / " + memory.getMaxUsed());
			JsPrintErr("# of memory measurements : " + memory.getMeasureCount());
		}
	}


	private static void warmup(final SGLR sglr, String inputFile, String input,
			String startSymbol, int warmup) throws FileNotFoundException,
			IOException {
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() < time + warmup * 1000) {
			parseFile(input, inputFile, NO_OUTPUT, sglr, startSymbol);
		}
		System.gc();
	}


public static long parseFile(String input, String inputFile, String output, SGLR sglr, String startSymbol)
		throws FileNotFoundException, IOException {

			long parsingTime = 0;
			Object t = null;
			try {
				parsingTime = System.currentTimeMillis();
				t = sglr.parse(input, inputFile, startSymbol);
				parsingTime = System.currentTimeMillis() - parsingTime;
			} catch(final BadTokenException e) {
				JsPrintErr("Parsing failed: " + e.getMessage());
				//System.exit(1);
			} catch(final SGLRException e) {
				// Detailed message for other exceptions dfef``
				JsPrintErr("Parsing failed: " + e);
				//System.exit(1);
			}
			if (!NO_OUTPUT.equals(output) && !output.equals("/dev/null"))
				JsPrintln(t.toString());
			return parsingTime;
		}
}
