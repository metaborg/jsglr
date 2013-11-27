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
  Strategy q_111;

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    Fail37:
    { 
      term = m_111.instance.invoke(context, term, q_111);
      if(term == null)
        break Fail37;
      if(true)
        return term;
    }
    return null;
  }
}