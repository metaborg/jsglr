package org.spoofax.jsglr.unicode.preprocessor;

import java.io.IOException;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.spoofax.terms.ParseError;
import org.spoofax.terms.Term;

public class AnotationPrettyPrinter {

	private static final String ANNOTATION_BEGIN = "{default(";
	private static final String ANNOTATION_END = "}";
	
	private static ParseTable ANNOTATION_PARSE_TABLE;
	
	static {
	
	ParseTableManager manager = new ParseTableManager();
	try {
		ANNOTATION_PARSE_TABLE =  manager.loadFromStream(AnotationPrettyPrinter.class.getResourceAsStream("Annotations.tbl"));
	} catch (ParseError e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidParseTableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	public String prettyPrintAnnotations(String fileContent) {
		// Search for annotations
		int startIndex = 0;
		int foundIndex;
		int foundEndIndex = 0;
		StringBuilder builder = new StringBuilder();
		while ((foundIndex = fileContent.indexOf(ANNOTATION_BEGIN, startIndex)) != -1) {
			builder.append(fileContent, startIndex, foundIndex+1);
			foundEndIndex = fileContent.indexOf(ANNOTATION_END, foundIndex);
			if (foundEndIndex != -1) {
				String annotation = fileContent.substring(foundIndex + 1, foundEndIndex);
				try {
				//	System.out.println(annotation);
					IStrategoTerm annotationAST  = (IStrategoTerm) new SGLR(new TreeBuilder(), ANNOTATION_PARSE_TABLE).parse(annotation, null, null);
				//	System.out.println(annotationAST);
					prettyPrint(annotationAST, builder);
				} catch (Exception e) {
					// Found something wrong, append it
					builder.append(fileContent, foundIndex+1, foundEndIndex);
					System.err.println(annotation);
					e.printStackTrace();
				}
			} else {
				// Something wrong, add the rest and return
				builder.append(fileContent, foundIndex, fileContent.length());
			}
			startIndex = foundEndIndex;
		}
		if (foundEndIndex != -1) {
			builder.append(fileContent, foundEndIndex, fileContent.length());
		}
		return builder.toString();
	}
	
	private void prettyPrint(IStrategoTerm t, StringBuilder b) {
		if (Term.tryGetName(t).equals("annoseq")) {
			prettyPrint(t.getSubterm(0), b);
			b.append(',');
			prettyPrint(t.getSubterm(1), b);
		} else if (Term.tryGetName(t).equals("default")) {
			prettyPrint(t.getSubterm(0), b);
		} else if (Term.tryGetName(t).equals("appl")) {
			prettyPrint(t.getSubterm(0), b);
			b.append('(');
			prettyPrint(t.getSubterm(1), b);
			b.append(')');
		} else if (Term.tryGetName(t).equals("fun")) {
			prettyPrint(t.getSubterm(0), b);
		} else if (Term.tryGetName(t).equals("unquoted")) {
			b.append(Term.asJavaString(t.getSubterm(0)));
		} else if (Term.tryGetName(t).equals("quoted")) {
			b.append('\"');
			b.append(Term.asJavaString(t.getSubterm(0)));
			b.append('\"');
		} else if (Term.tryGetName(t).equals("const")) {
			b.append(Term.asJavaString(t.getSubterm(0)));
		} else {
			throw new RuntimeException("Unexpected term: "  + t);
		}
	}

}
