package org.spoofax.jsglr.unicode.preprocessor;

import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.StrategoExit;
import org.strategoxt.stratego_gpp.box2text_string_0_1;
import org.strategoxt.stratego_lib.parenthesize_2_0;
import org.strategoxt.stratego_lib.stratego_lib;
import org.strategoxt.stratego_sdf.pp_sdf_box_0_0;
import org.strategoxt.stratego_sdf.pp_sdf_string_0_0;
import org.strategoxt.stratego_sdf.sdf_ppfix_0_0;
import org.strategoxt.stratego_sdf.stratego_sdf;
import org.strategoxt.tools.sdf2parenthesize_0_0;
import org.strategoxt.tools.sdf_desugar_0_0;
import org.strategoxt.tools.tools;

//import sdf_parenthesize.sdf_parenthesize;

public class SDFPrettyPrinter {

	private static Context ctx = stratego_sdf.init();
	private static Context ctx_fix = tools.init();
	private static Context ctx_parenthesize = sdf_parenthesize.sdf_parenthesize.init();

	public static IStrategoTerm fixSDF(IStrategoTerm term) {
		if (term == null)
			return null;

		IStrategoTerm result = null;
		try {
			result = sdf_desugar_0_0.instance.invoke(ctx_fix, term);
			result = sdf_parenthesize.parenthesize_$Sdf2_0_0.instance.invoke(ctx_fix, result);
	//		result = sdf_ppfix_0_0.instance.invoke(ctx, result);
		} catch (StrategoExit e) {
			e.printStackTrace();
			if (e.getValue() != 0 || result == null)
				throw new RuntimeException("Sdf desugaring failed", e);
		}

		return result;
	}

	public String prettyPrintSDF(IStrategoTerm ast) {

		/*
		 * IStrategoTerm result = null; try { result =
		 * sdf_desugar_0_0.instance.invoke(interp.getCompiledContext(), ast); }
		 * catch (StrategoExit e) { if (e.getValue() != 0 || result == null)
		 * throw new RuntimeException("Sdf desugaring faild", e); } ast =
		 * result;
		 */
		//Context ctx = org.strategoxt.imp.editors.template.generated.generated.init();
		//return((IStrategoString) org.strategoxt.imp.editors.template.generated.pp_sdf_to_string_0_0.instance.invoke(ctx, ast )).stringValue();
		ast = fixSDF(ast);
		IStrategoTerm boxTerm = pp_sdf_box_0_0.instance.invoke(ctx, ast);
		
		if (boxTerm != null) {
			IStrategoTerm textTerm = box2text_string_0_1.instance
					.invoke(ctx, boxTerm, UnicodeUtils.factory.makeInt(80));
			if (textTerm != null)
				return ((IStrategoString) textTerm).stringValue();
		}
		throw new IllegalArgumentException("Given ast could not be printed.\n" + ast);
		// throw new ATermCommands.PrettyPrintError(ast,
		// "pretty printing SDF AST failed");
	}

}
