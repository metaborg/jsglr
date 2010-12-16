package org.spoofax.jsglr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileTools {

	public static String loadFileAsString(String fn) {
		// FIXME (KTK) static allocation of 15MB must be replaced with something dynamic  
		char[] cbuf = new char[1024*1024*15];
		try {
			BufferedReader br = new BufferedReader(new FileReader(fn));
			int len = br.read(cbuf);
			return new String(cbuf, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
