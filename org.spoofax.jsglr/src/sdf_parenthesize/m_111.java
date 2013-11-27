package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class m_111 extends Strategy 
{ 
  public static final m_111 instance = new m_111();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy q_111)
  { 
    Fail36:
    { 
      lifted1 lifted10 = new lifted1();
      lifted10.q_111 = q_111;
      term = SRTS_all.instance.invoke(context, term, lifted10);
      if(term == null)
        break Fail36;
      term = q_111.invoke(context, term);
      if(term == null)
        break Fail36;
      if(true)
        return term;
    }
    return null;
  }
}