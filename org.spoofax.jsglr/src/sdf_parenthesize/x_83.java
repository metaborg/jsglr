package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class x_83 extends Strategy 
{ 
  public static final x_83 instance = new x_83();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy z_83)
  { 
    Fail60:
    { 
      lifted1 lifted10 = new lifted1();
      lifted10.z_83 = z_83;
      term = SRTS_all.instance.invoke(context, term, lifted10);
      if(term == null)
        break Fail60;
      term = z_83.invoke(context, term);
      if(term == null)
        break Fail60;
      if(true)
        return term;
    }
    return null;
  }
}