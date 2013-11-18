package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class parenthesize_$Sdf2_0_0 extends Strategy 
{ 
  public static parenthesize_$Sdf2_0_0 instance = new parenthesize_$Sdf2_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    context.push("parenthesize_Sdf2_0_0");
    Fail1:
    { 
      term = x_83.instance.invoke(context, term, y_83.instance);
      if(term == null)
        break Fail1;
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}