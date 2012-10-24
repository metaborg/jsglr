package org.spoofax.jsglr.client.imploder;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.ParseTable;

public class MemoryRecordingTreeBuilder implements ITreeBuilder {

	private final ITreeBuilder actualBuilder;
	private long maxTotalMemory = Long.MIN_VALUE;
	private long maxUsedMemory = Long.MIN_VALUE;
	private long minTotalMemory = Long.MAX_VALUE;
	private long minUsedMemory = Long.MAX_VALUE;
	private int measureCount = 0;
	
	public MemoryRecordingTreeBuilder(ITreeBuilder actualBuilder) {
		measure();
		this.actualBuilder = actualBuilder;
	}
	
	private void measure() {
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		long used = total - free;
		maxTotalMemory = Math.max(maxTotalMemory, total);
		maxUsedMemory = Math.max(maxUsedMemory, used);
		minTotalMemory = Math.min(minTotalMemory, total);
		minUsedMemory = Math.min(minUsedMemory, used);
		measureCount++;
	}
	
	public void initializeTable(ParseTable table, int productionCount,
			int labelStart, int labelCount) {
		measure();
		actualBuilder.initializeTable(table, productionCount, labelStart, labelCount);
	}

	public void initializeLabel(int labelNumber,
			IStrategoAppl parseTreeProduction) {
		measure();
		actualBuilder.initializeLabel(labelNumber, parseTreeProduction);
	}

	public void initializeInput(String input, String filename) {
		measure();
		actualBuilder.initializeInput(input, filename);
	}

	public Object buildTree(AbstractParseNode node) {
		measure();
		return actualBuilder.buildTree(node);
	}

	public Object buildTreeTop(Object subtree, int ambiguityCount) {
		measure();
		return actualBuilder.buildTreeTop(subtree, ambiguityCount);
	}

	public void reset() {
		measure();
		actualBuilder.reset();
	}
	
	public void reset(int startOffset) {
		// TODO Auto-generated method stub
	}

	public ITokenizer getTokenizer() {
		measure();
		return actualBuilder.getTokenizer();
	}

	public long getMaxTotal() {
		return maxTotalMemory;
	}

	public long getMaxUsed() {
		return maxUsedMemory;
	}

	public long getMinTotal() {
		return minTotalMemory;
	}

	public long getMinUsed() {
		return minUsedMemory;
	}

	public int getMeasureCount() {
		return measureCount;
	}
}
