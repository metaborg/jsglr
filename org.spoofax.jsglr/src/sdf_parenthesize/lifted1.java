package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class lifted1 extends Strategy 
{ 
  Strategy z_83;

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail61:
    { 
      term = x_83.instance.invoke(context, term, z_83);
      if(term == null)
        break Fail61;
      if(true)
        return term;
    }
    return null;
  }
}