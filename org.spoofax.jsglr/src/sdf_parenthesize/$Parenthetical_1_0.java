package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $Parenthetical_1_0 extends Strategy 
{ 
  public static $Parenthetical_1_0 instance = new $Parenthetical_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy b_2)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("Parenthetical_1_0");
    Fail30:
    { 
      IStrategoTerm a_80 = null;
      IStrategoTerm z_79 = null;
      if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consParenthetical_1 != ((IStrategoAppl)term).getConstructor())
        break Fail30;
      z_79 = term.getSubterm(0);
      IStrategoList annos0 = term.getAnnotations();
      a_80 = annos0;
      term = b_2.invoke(context, z_79);
      if(term == null)
        break Fail30;
      term = termFactory.annotateTerm(termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{term}), checkListAnnos(termFactory, a_80));
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}