package java.io;

import java.io.*;

import org.Logger;

import com.google.gwt.core.client.JavaScriptObject;
public class FileInputStream extends InputStream
{
	private static JavaScriptObject fs;
	private final String fileName;
	private int fileSize;
	private JavaScriptObject fileHandle;
	private int pos = 0;
	 
	
	public FileInputStream(String s) throws FileNotFoundException
	{
		fileName = s;
		init();
	}
	
	public FileInputStream(File f)
	{
		fileName = f.getName();
		init();
	}	
	
	
	private native void init() /*-{
		@java.io.FileInputStream::fs = require('fs');
		var fn = this.@java.io.FileInputStream::fileName;
		this.@java.io.FileInputStream::fileHandle = @java.io.FileInputStream::fs.openSync(fn, 'r');
		var size = @java.io.FileInputStream::fs.statSync(fn).size;
		this.@java.io.FileInputStream::fileSize = size;
		
		//console.log(fn + " " + size);
	}-*/;
	
	public native void close() throws IOException /*-{
		//console.log("close()");
		@java.io.FileInputStream::fs.closeSync(this.@java.io.FileInputStream::fileHandle);
	}-*/;

	public native int available() throws IOException /*-{
		return this.@java.io.FileInputStream::fileSize - this.@java.io.FileInputStream::pos;
	}-*/;
	
	public int read() throws IOException 
	{
		return read(0);
	}
	
	public native int read(byte b[]) throws IOException /*-{
		//console.log("read(byte b[])");
		return 122;
	}-*/;
	
	public int read(byte b[], int off, int len) throws IOException {
		return readBytes(b, off, len);
	}
	
 	
	private native int read(int offset) throws IOException /*-{
		var file = this.@java.io.FileInputStream::fileHandle;
		var buf = new Buffer(1);
		buf[0] = -1;
		var readBytes = @java.io.FileInputStream::fs.readSync(file, buf, offset, 1, this.@java.io.FileInputStream::pos);
		this.@java.io.FileInputStream::pos += readBytes;
		if (readBytes === 0)
		{
			return -1;
		}
		return buf[0];
	}-*/;

	private int readBytes(byte b[], int off, int len) throws IOException 
	{
		int read = 0;
		for (int i = 0; i < len; i++)
		{
			int val = read(off);
			if (val == -1)
				return read;
			b[i] = (byte)val;
			read++;
		}
		return read;
	}

}