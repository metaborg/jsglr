package org.spoofax.jsglr.unicode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.text.ParseException;

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
	 * 				the encoding of the input file
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
	public static String preprocess(String sdfContent) throws ParseException{
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

			// Split the arguments by ,
			String[] arguments = argumentString.split(",");
			if (arguments == null) {
				throw new ParseException("Missing arguments for $Unicode call.", argEnd);
			}
			// Now convert the argument
			UnicodeRangePair r = parseArguments(arguments);

			// Append the converted ranges
			builder.append(r.toString());
			startIndex = argEnd + 1;
		}
		builder.append(sdfContent.substring(startIndex));
		return builder.toString();
	}

	/**
	 * Parses the given arguments of a Unicode call to a RangePair.
	 * 
	 * @param arguments
	 *            the arguments of a Unicode call
	 * @return the resulting RangePair
	 */
	private static UnicodeRangePair parseArguments(String[] arguments) {
		// Unite the ranges of all arguments
		UnicodeRangePair r = new UnicodeRangePair();
		for (String arg : arguments) {
			r.unite(parseArgument(arg));
		}
		return r;
	}

	/**
	 * Parses the given argument to a RangePair.
	 * 
	 * @param arg
	 *            the argument to parse
	 * @return the resulting RangePair
	 */
	private static UnicodeRangePair parseArgument(String arg) {
		// Check for the range
		arg = arg.replace(" ", "");
		int separatorIndex = arg.indexOf("-");
		if (separatorIndex == -1) {
			// Single character
			int val = UnicodeConverter.extractFirstUnicodeCharacterAndForceEmptyBuffer(CharBuffer.wrap(arg));
			return new UnicodeRangePair(val, val);
		} else {
			// Character range
			int start = UnicodeConverter.extractFirstUnicodeCharacterAndForceEmptyBuffer(CharBuffer.wrap(arg.substring(
					0, separatorIndex)));
			int end = UnicodeConverter.extractFirstUnicodeCharacterAndForceEmptyBuffer(CharBuffer.wrap(arg.substring(
					separatorIndex + 1, arg.length())));
			return new UnicodeRangePair(start, end);
		}
	}

	public static void main(String[] args)  throws ParseException {
		System.out.println(preprocess("$Unicode(Ø,∀) $Unicode(∀) $Unicode(∀-水,𝄞) $Unicode(𝄞)"));

		try {
			preprocessFile(new File("tests/grammars/basic/UTF8.sdfu"), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
