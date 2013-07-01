package org.spoofax.jsglr.unicode.preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.spoofax.jsglr.unicode.UnicodeConverter;

public class UnicodeCleaner {
	
	public static File removeUnicode(File inputFile) throws IOException {
		File outputFile = createOutputFile(inputFile);
		
		BufferedReader inputReader = null;
		BufferedWriter outputWriter = null;
		
		try {
			inputReader = new BufferedReader(new FileReader(inputFile));
			outputWriter = new BufferedWriter(new FileWriter(outputFile));
			String temp;
			while ((temp = inputReader.readLine()) != null) {
				outputWriter.append(UnicodeConverter.encodeUnicodeToBacklashU(temp));
				outputWriter.newLine();
			}
			inputReader.close();
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			throw e;
		} finally {
			if (inputReader != null) {
				inputReader.close();
			}
			if (outputWriter != null) {
				outputWriter.close();
			}
		}
		
		return outputFile;
		
	}
	
	private static File createOutputFile(File inputFile) throws IOException{
		return File.createTempFile(inputFile.getName(), ".sdftmp");
	}
	

}
