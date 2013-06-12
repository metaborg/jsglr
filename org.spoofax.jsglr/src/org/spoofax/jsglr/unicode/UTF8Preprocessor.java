package org.spoofax.jsglr.unicode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UTF8Preprocessor {

	private static class Pair<X, Y> {
		X x;
		Y y;

		Pair(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	private static class SingleRange extends Pair<Integer, Integer> {
		SingleRange(Integer start, Integer end) {
			super(start, end);
		}

		SingleRange(Integer val) {
			this(val, val);
		}
	}

	private static class Range implements Iterable<SingleRange> {
		List<SingleRange> ranges;

		Range() {
			this.ranges = new LinkedList<SingleRange>();
		}

		public Range(SingleRange initial) {
			this();
			this.ranges.add(initial);
		}

		void add(SingleRange r) {
			ranges.add(r);
		}

		void unite(Range r) {
			ranges.addAll(r.ranges);
		}

		public Iterator<SingleRange> iterator() {
			return this.ranges.iterator();
		}

		public boolean isEmpty() {
			return ranges.isEmpty();
		}

	}

	private static class RangePair extends Pair<Range, Range> {

		RangePair() {
			this(new Range(), new Range());
		}

		RangePair(int start, int end) {
			this(null, null);
			if (start > end) {

			}
			if (UnicodeConverter.isTwoByteChar(end)) {
				this.x = new Range(new SingleRange(start, end));
				this.y = new Range();
			} else if (!UnicodeConverter.isTwoByteChar(start)) {
				this.y = new Range(new SingleRange(start, end));
				this.x = new Range();
			} else {
				this.x = new Range(new SingleRange(start,  UnicodeConverter.getMaxTwoByteChar()));
				this.y = new Range(new SingleRange(UnicodeConverter.getMaxTwoByteChar() + 1, end));
			}
		}

		RangePair(Range r2, Range r4) {
			super(r2, r4);
		}

		void unite(RangePair p) {
			this.x.unite(p.x);
			this.y.unite(p.y);
		}
	}

	private static final String UNICODE_COMMAND = "$Unicode";


	public static void preprocessFile(File inputFile) throws IOException {
		File outputFile = new File(inputFile.getParent(), inputFile.getName().replaceAll(".sdfu", ".sdf"));
		BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
		StringBuilder content = new StringBuilder();
		String temp;
		while ((temp = inputReader.readLine()) != null) {
			content.append(temp);
			content.append('\n');
		}
		inputReader.close();
		
		String output = preprocess(content.toString());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		writer.append(output);
		writer.flush();
		writer.close();
		
	}
	
	
	public static String preprocess(String sdfContent) {
		// Search all occurrences of the $Unicode command
		StringBuilder builder = new StringBuilder((int) (sdfContent.length() * 1.1));
		int startIndex = 0;
		int newStartIndex = 0;
		int argStart, argEnd;
		while ((newStartIndex = sdfContent.indexOf(UNICODE_COMMAND, startIndex)) != -1) {
			// Append unhandled content to the builder
			builder.append(sdfContent.substring(startIndex, newStartIndex));
			// Search for the arguments of the command
			argStart = sdfContent.indexOf("(", newStartIndex);
			if (argStart == -1) {

			}
			argEnd = sdfContent.indexOf(")", argStart);
			if (argEnd == -1) {

			}
			String argumentString = sdfContent.substring(argStart + 1, argEnd);
			System.out.println("Argument: "+ argumentString);
			String[] arguments = argumentString.split(",");
			if (arguments == null) {

			}
			// Now convert the argument
			RangePair r = new RangePair();
			for (String arg : arguments) {
				r.unite(convertArgument(arg));
			}
			// Append the converted ranges
			builder.append(rangePairToSDF(r));
			startIndex = argEnd + 1;
		}
		builder.append(sdfContent.substring(startIndex));
		return builder.toString();
	}

	private static RangePair convertArgument(String arg) {
		// Check for the range
		arg = arg.replace(" ", "");
		int separatorIndex = arg.indexOf("-");
		if (separatorIndex == -1) {
			// Single character
			int val = UnicodeConverter.convertSingleUnicodeAndForceEmpty(CharBuffer.wrap(arg));
			return new RangePair(val, val);
		} else {
			int start = UnicodeConverter.convertSingleUnicodeAndForceEmpty(CharBuffer.wrap(arg.substring(0, separatorIndex)));
			int end = UnicodeConverter.convertSingleUnicodeAndForceEmpty(CharBuffer.wrap(arg.substring(separatorIndex + 1, arg.length())));
			return new RangePair(start, end);
		}
	}

	private static String rangePairToSDF(RangePair pair) {
		StringBuilder builder = new StringBuilder();
		builder.append("[\\007]");
		if (!pair.x.isEmpty() && !pair.y.isEmpty()) {
			builder.append('(');
			builder.append(rangeToString(pair.x, 2));
			builder.append('|');
			builder.append(rangeToString(pair.y, 4));
			builder.append(')');
		} else if (pair.x.isEmpty()) {
			builder.append(rangeToString(pair.y, 4));
		} else {
			builder.append(rangeToString(pair.x, 2));
		}
		return builder.toString();
	}

	private static String rangeToString(Range range, int num) {
		StringBuilder builder = new StringBuilder();


		builder.append('(');
		for (SingleRange r : range) {
			for (int i = num - 1; i >= 0; i--) {
				builder.append('[');
				int start = Math.min(r.x, r.y);
				int end = Math.max(r.x, r.y);
				System.out.println("Range from " + start + " to " + end);
				builder.append("\\");
				builder.append((int) UnicodeConverter.getByte(i, start));
				if (start != end) {
					builder.append('-');
					builder.append("\\");
					builder.append((int) UnicodeConverter.getByte(i, end));
				}
				builder.append(']');
			//	builder.append(',');
			}
			builder.append('|');
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(')');
		return builder.toString();
	}

	public static void main(String[] args) {
		System.out.println(preprocess("$Unicode(Ø,∀) $Unicode(∀) $Unicode(∀-水,𝄞) $Unicode(𝄞)"));
	
		try {
			preprocessFile(new File("tests/grammars/basic/UTF8.sdfu"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
