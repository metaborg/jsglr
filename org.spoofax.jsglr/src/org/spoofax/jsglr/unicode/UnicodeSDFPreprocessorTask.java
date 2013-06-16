package org.spoofax.jsglr.unicode;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * The {@link UnicodeSDFPreprocessorTask} integrates
 * {@link UnicodeSDFPreprocessor} to Ant.<br>
 * The task has to attributes: onlysdfufiles, which specifies, whether onlt
 * files with the sdfu extension are processed (default true), and encoding,
 * which specifies the encoding of the file (default UTF-8). File are added with
 * an nested fileset.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeSDFPreprocessorTask extends Task {

	private List<FileSet> inputs;
	private boolean onlySDFUFiles = true;
	private String encoding = "UTF-8";

	public UnicodeSDFPreprocessorTask() {
		this.inputs = new LinkedList<FileSet>();
	}

	public void setOnlySDFUFiles(boolean onlySDFUFiles) {
		this.onlySDFUFiles = onlySDFUFiles;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void addFileSet(FileSet s) {
		this.inputs.add(s);
	}

	@Override
	public String getTaskName() {
		return "unicodesdf";
	}

	@Override
	public void execute() throws BuildException {
		// Iterate through all input files
		for (FileSet input : this.inputs) {
			for (Object o : new IteratorWrapper<Object>(input.iterator())) {
				if (o instanceof FileResource) {
					FileResource fileRes = (FileResource) o;
					File file = fileRes.getFile();
					// Preprocess the file
					if (!this.onlySDFUFiles || file.getName().endsWith(".sdfu")) {
						try {
							System.out.println("Unicode Preprocessing file: " + file);
							UnicodeSDFPreprocessor.preprocessFile(file, encoding);
						} catch (Exception e) {
							throw new BuildException(e);
						}
					}

				}
			}
		}
	}

	private static class IteratorWrapper<T> implements Iterable<T> {

		private Iterator<T> iterator;

		public IteratorWrapper(Iterator<T> i) {
			this.iterator = i;
		}

		public Iterator<T> iterator() {
			return this.iterator;
		}

	}

}
