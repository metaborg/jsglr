package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetReader;

public class Main {
	
	private static String reportPath = "/path/to/measurements/";

	public static void main(String[] args) throws ParseTableReadException, IOException, ParseException {
		System.out.println("Starting measurments...");
		
		new File(reportPath).mkdirs();
		
		for (TestSet testSet : TestSet.all) {
			measure(testSet);
		}
		
		System.out.println("Done");
	}
	
	private static void measure(TestSet testSet) throws ParseTableReadException, IOException, ParseException {
		System.out.println(" - Testset '" + testSet.name + "'");
		
		TestSetReader testSetReader = new MeasureTestsetReader(testSet);
		
		IParseTable parseTable = ParseTableReader.read(testSetReader.getParseTableTerm());
		
		String filename = reportPath + testSet.name + ".csv";
		
		PrintWriter out = new PrintWriter(filename);
		
		csvHeader(out);
		
		for (TestSetReader.InputBatch inputBatch : testSetReader.getInputBatches()) {
			if (inputBatch.size != -1)
				System.out.println("   Batch '" + inputBatch.size + "'");
			
			@SuppressWarnings("unchecked")
			Parser<AbstractElkhoundStackNode<HybridParseForest>, HybridParseForest, ParseNode, Derivation> parser =
				(Parser<AbstractElkhoundStackNode<HybridParseForest>, HybridParseForest, ParseNode, Derivation>) JSGLR2Variants.getParser(parseTable, JSGLR2Variants.ParseForestRepresentation.Hybrid, JSGLR2Variants.StackRepresentation.BasicElkhound, JSGLR2Variants.Reducing.Elkhound);
			
			ParserMeasureObserver<HybridParseForest> measureObserver = new ParserMeasureObserver<HybridParseForest>();
			
			parser.attachObserver(measureObserver);
			
			for (Input input : inputBatch.inputs) {
				parser.parseUnsafe(input.content, input.filename);
			}
			
			csvRow(out, inputBatch, measureObserver);
		}
		
		out.close();
	}
	
	private static void csvHeader(PrintWriter out) {
		List<String> cells = new ArrayList<String>();
		
		for (Measurement measurement : measurements) {
			cells.add(measurement.name());
		}
		
		csvLine(out, cells);
	}
	
	private static void csvRow(PrintWriter out, TestSetReader.InputBatch inputBatch, ParserMeasureObserver<HybridParseForest> measureObserver) {
		List<String> cells = new ArrayList<String>();
		
		for (Measurement measurement : measurements) {
			switch (measurement) {
				case size:
					cells.add("" + inputBatch.size);
					break;
				case characters:
					cells.add("" + measureObserver.length);
					break;
				case stackNodes:
					cells.add("" + measureObserver.stackNodes.size());
					break;
				case stackLinks:
					cells.add("" + measureObserver.stackLinks.size());
					break;
				case stackLinksRejected:
					cells.add("" + measureObserver.stackLinksRejected.size());
					break;
				case deterministicDepthResets:
					cells.add("" + measureObserver.deterministicDepthResets);
					break;
				case parseNodes:
					cells.add("" + measureObserver.parseNodes.size());
					break;
				case parseNodesAmbiguous:
					int parseNodesAmbiguous = 0;
					
					for (ParseNode parseNode : measureObserver.parseNodes) {
						if (parseNode.isAmbiguous())
							parseNodesAmbiguous++;
					}
					
					cells.add("" + parseNodesAmbiguous);
					break;
				case parseNodesContextFree:
					int parseNodesContextFree = 0;
					
					for (ParseNode parseNode : measureObserver.parseNodes) {
						if (parseNode.production.isContextFree())
							parseNodesContextFree++;
					}
					
					cells.add("" + parseNodesContextFree);
					break;
				case parseNodesLexical:
					int parseNodesLexical = 0;
					
					for (ParseNode parseNode : measureObserver.parseNodes) {
						if (parseNode.production.isLexical() || parseNode.production.isLexicalRhs())
							parseNodesLexical++;
					}
					
					cells.add("" + parseNodesLexical);
					break;
				case parseNodesLayout:
					int parseNodesLayout = 0;
					
					for (ParseNode parseNode : measureObserver.parseNodes) {
						if (parseNode.production.isLayout())
							parseNodesLayout++;
					}
					
					cells.add("" + parseNodesLayout);
					break;
				case characterNodes:
					cells.add("" + measureObserver.characterNodes.size());
					break;
				case actors:
					cells.add("" + measureObserver.actors.size());
					break;
				case doReductions:
					cells.add("" + measureObserver.doReductions);
					break;
				case doLimitedReductions:
					cells.add("" + measureObserver.doLimitedReductions);
					break;
				case doReductionsLR:
					cells.add("" + measureObserver.doReductionsLR);
					break;
				case doReductionsDeterministicGLR:
					cells.add("" + measureObserver.doReductionsDeterministicGLR);
					break;
				case doReductionsNonDeterministicGLR:
					cells.add("" + measureObserver.doReductionsNonDeterministicGLR);
					break;
				case reducers:
					cells.add("" + measureObserver.reducers.size());
					break;
				case reducersElkhound:
					cells.add("" + measureObserver.reducersElkhound.size());
					break;
				default:
					break;
			}
		}
		
		csvLine(out, cells);
	}
	
	public enum Measurement {
		size,
		characters,
		stackNodes,
		stackLinks,
		stackLinksRejected,
		deterministicDepthResets,
		parseNodes,
		characterNodes,
		parseNodesAmbiguous,
		parseNodesContextFree,
		parseNodesLexical,
		parseNodesLayout,
		actors,
		doReductions,
		doLimitedReductions,
		doReductionsLR,
		doReductionsDeterministicGLR,
		doReductionsNonDeterministicGLR,
		reducers,
		reducersElkhound
	}
	
	public static Measurement[] measurements = new Measurement[] {
		Measurement.size,
		Measurement.characters,
		Measurement.stackNodes,
		Measurement.stackLinks,
		Measurement.stackLinksRejected,
		Measurement.deterministicDepthResets,
		Measurement.parseNodes,
		Measurement.characterNodes,
		Measurement.parseNodesAmbiguous,
		Measurement.parseNodesContextFree,
		Measurement.parseNodesLexical,
		Measurement.parseNodesLayout,
		Measurement.actors,
		Measurement.doReductions,
		Measurement.doLimitedReductions,
		Measurement.doReductionsLR,
		Measurement.doReductionsDeterministicGLR,
		Measurement.doReductionsNonDeterministicGLR,
		Measurement.reducers,
		Measurement.reducersElkhound
	};
	
	private static void csvLine(PrintWriter out, List<String> cells) {
		out.println(String.join(",", cells));
	}
	
}
