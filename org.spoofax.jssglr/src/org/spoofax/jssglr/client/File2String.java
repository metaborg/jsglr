package org.spoofax.jssglr.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class File2String 
{
	public static String GetContentsOf(String file)
	{
		StringBuffer strContent = new StringBuffer("");
		try {
			FileInputStream fis = new FileInputStream(file);
			int ch;
			while ((ch = fis.read()) != -1) 
				strContent.append((char)ch);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return strContent.toString();
	}

}
