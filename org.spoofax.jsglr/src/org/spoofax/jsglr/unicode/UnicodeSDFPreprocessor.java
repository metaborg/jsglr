package org.spoofax.jsglr.unicode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.text.ParseException;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.terms.Term;
import org.spoofax.terms.attachments.ParentTermFactory;

/**
 * The UnicodeSDFPreprocessor allows to define unicode characters and ranges in
 * SDF. You define unicode characters by $Unicode(...). Where ... is a list of
 * unicode intervals separated by "," and a unicode interval is [unicode
 * character] - [unicode character] or a single [unicode character]. So for
 * example:<br>
 * $Unicode(Ø,∀) $Unicode(∀) $Unicode(∀-水,𝄞) $Unicode(𝄞).<br>
 * This is no valid SDF, the $ is used not to mix up with existing SDF
 * identifiers.<br>
 * The UnicodeSDFPreprocessor turns this in valid SDF code again by encoding the
 * unicode characters in ascii as described in {@link UnicodeConverter}. So the
 * result of the examples is:<br>
 * [\7]([\0][\216]|[\34][\0]) [\7]([\34][\0])
 * [\7](([\34-\108][\0-\52])|([\216][\52][\221][\30]))
 * [\7]([\216][\52][\221][\30])<br>
 * which now is valid SDF Code again, but code nobody wants to write by hand.<br>
 * The idea for the preprocessor is to store the Unicode extended SDF in files
 * with file extension ".sdfu", the preprocessor then creates the corresponding
 * ".sdf" file.<br>
 * The proprecessor, for simplicity, does not work on ASTs but on strings.
 * Correct $Unicode notations are preprocessed correctly, wrong may be
 * preprocessed too without an exception to probably wrong SDF code.
 * Additionally it does not check that the <b> $Unicode calls are only in
 * lexical syntax</b> because no layout between unicode bytes is allowed.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeSDFPreprocessor {

	private static final String UNICODE_COMMAND = "$Unicode";

	/**
	 * Preprocesses the given SDF Code for Unicode character commands. All
	 * $Unicode commands are converted to the corresponding character ranges.
	 * The result is written back. When the input file is xxx.sdfu, the result
	 * is written to xxx.sdf, otherwise .sdf is appended to the file name. The
	 * resulting file now is valid SDF Code.
	 * 
	 * @param inputFile
	 *            the input SDF file containing Unicode characters
	 * @param encoding
	 *            the encoding of the input file
	 * @throws IOException
	 *             problems with reading and writing files
	 */
	public static void preprocessFile(File inputFile, String encoding) throws IOException, ParseException {
		StringBuilder content = new StringBuilder();

		// === Read the content of the file ===
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), encoding));
		try {
			String temp;
			while ((temp = inputReader.readLine()) != null) {
				content.append(temp);
				content.append('\n');
			}
		} catch (IOException e) {
			// Catch the exception and retrow it, but this allows closing the
			// stream in finally block
			throw e;
		} finally {
			inputReader.close();
		}

		// === Preprocess the content
		String output = preprocess(content.toString());

		// === Write the File
		String outputFileName = inputFile.getName();
		if (outputFileName.endsWith(".sdfu")) {
			outputFileName = outputFileName.substring(0, outputFileName.length() - 1);
		} else {
			outputFileName += ".sdf";
		}
		File outputFile = new File(inputFile.getParent(), outputFileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		try {
			writer.append(output);
			writer.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			writer.close();
		}
	}

	/**
	 * Preprocesses the given SDF Code for Unicode characters. It replaces all
	 * $Unicode calls in the given String with the correct character ranges for
	 * parsing.
	 * 
	 * @param sdfContent
	 *            the SDF Code to preprocess
	 * @return the preprocessed SDF Code which is now valid SDF Code
	 */
	public static String preprocess(String sdfContent) throws ParseException {
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
				throw new ParseException("Cannot parse the arguments for $Unicode call. Missing (", newStartIndex);
			}
			argEnd = sdfContent.indexOf(")", argStart);
			if (argEnd == -1) {
				throw new ParseException("Cannot parse the arguments for $Unicode call. Missing )", argStart);
			}
			String argumentString = sdfContent.substring(argStart + 1, argEnd);

			UnicodeRangePair r;
			try {
				r = buildUnicodeRange(parseUnicodeRangeArgument(argumentString));
				r.normalize();
				 builder.append(r.toString());
			} catch (Exception e) {

				e.printStackTrace();
			}

			// Append the converted ranges
			startIndex = argEnd + 1;
		}
		builder.append(sdfContent.substring(startIndex));
		return builder.toString();
	}

	private static ParseTable unicodeTable;

	static {
		try {
			unicodeTable = new ParseTableManager().loadFromStream(UnicodeSDFPreprocessor.class
					.getResourceAsStream("/org/spoofax/jsglr/unicode/Unicode.tbl"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static IStrategoTerm parseUnicodeRangeArgument(String argument) throws Exception {
		SGLR sglr = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(unicodeTable.getFactory())),
				true), unicodeTable);
		return (IStrategoTerm) sglr.parse(argument, null, null, true);
	}

	private static UnicodeRangePair buildUnicodeRange(IStrategoTerm term) {
		String cons = Term.tryGetConstructor(term).getName();
		if (cons.equals("Range")) {
			IStrategoList list = (IStrategoList) term.getSubterm(0);
			UnicodeRangePair pair = new UnicodeRangePair();
			for (int i = 0; i < list.getSubtermCount(); i++) {
				pair.unite(buildUnicodeRangeElem(list.getSubterm(i)));
			}
			return pair;
		} else if (cons.equals("AllRange")) {
			return new UnicodeRangePair(UnicodeConverter.FIRST_UNICODE, UnicodeConverter.LAST_UNICODE);
		} else {
			UnicodeRangePair left = buildUnicodeRange(term.getSubterm(0));
			UnicodeRangePair right = buildUnicodeRange(term.getSubterm(1));
			if (cons.equals("Union")) {
				left.unite(right);
			} else if (cons.equals("Intersection")) {
				left.intersect(right);
			} else if (cons.equals("Difference")) {
				left.diff(right);
			} else {
				throw new RuntimeException("Unsupported Unicode Constructor");
			}
			return left;
		}
	}
	
	private static UnicodeRangePair buildUnicodeRangeElem(IStrategoTerm term) {
		String cons = Term.tryGetConstructor(term).getName();
		if (cons.equals("Interval")) {
			int startVal = buildUnicodeVal(term.getSubterm(0));
			int endVal = buildUnicodeVal(term.getSubterm(1));
			return new UnicodeRangePair(startVal, endVal);
		} else {
			int val = buildUnicodeVal(term);
			return new UnicodeRangePair(val, val);
		}
	}
	
	private static int buildUnicodeVal(IStrategoTerm term) {
		String cons = Term.tryGetConstructor(term).getName();
		String val = Term.asJavaString(term.getSubterm(0));
		if (cons.equals("Char")) {
			return UnicodeConverter.toUnicodeCharacter(val);
		} else if (cons.equals("Val")) {
			if (val.startsWith("\\u")) {
				return Integer.parseInt(val.substring(2), 16);
			} else {
				throw new RuntimeException("Invalid Unicode value");
			}
		}
		return 0;
	}

}
