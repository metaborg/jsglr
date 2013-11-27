package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $Parenthesize_$List_1_0 extends Strategy 
{ 
  public static $Parenthesize_$List_1_0 instance = new $Parenthesize_$List_1_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term, Strategy s_2)
  { 
    ITermFactory termFactory = context.getFactory();
    context.push("Parenthesize_List_1_0");
    Fail2:
    { 
      IStrategoTerm term29 = term;
      Success0:
      { 
        Fail3:
        { 
          if(term.getTermType() != IStrategoTerm.LIST || !((IStrategoList)term).isEmpty())
            break Fail3;
          term = s_2.invoke(context, sdf_parenthesize.constNil0);
          if(term == null)
            break Fail3;
          if(true)
            break Success0;
        }
        term = term29;
        IStrategoTerm term30 = term;
        Success1:
        { 
          Fail4:
          { 
            IStrategoTerm g_0 = null;
            IStrategoTerm h_0 = null;
            if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
              break Fail4;
            h_0 = ((IStrategoList)term).head();
            g_0 = ((IStrategoList)term).tail();
            term = h_0;
            IStrategoTerm term31 = term;
            IStrategoConstructor cons0 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
            Success2:
            { 
              if(cons0 == sdf_parenthesize._consalt_2)
              { 
                Fail5:
                { 
                  if(true)
                    break Success2;
                }
                term = term31;
              }
              if(cons0 == sdf_parenthesize._conslabel_2)
              { }
              else
              { 
                break Fail4;
              }
            }
            term = this.invoke(context, g_0, _Id.instance);
            if(term == null)
              break Fail4;
            term = (IStrategoTerm)termFactory.makeListCons(termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{h_0}), termFactory.makeListCons(term, (IStrategoList)sdf_parenthesize.constNil0));
            if(true)
              break Success1;
          }
          term = term30;
          IStrategoTerm b_0 = null;
          IStrategoTerm c_0 = null;
          if(term.getTermType() != IStrategoTerm.LIST || ((IStrategoList)term).isEmpty())
            break Fail2;
          b_0 = ((IStrategoList)term).head();
          c_0 = ((IStrategoList)term).tail();
          term = this.invoke(context, c_0, s_2);
          if(term == null)
            break Fail2;
          term = (IStrategoTerm)termFactory.makeListCons(b_0, termFactory.makeListCons(term, (IStrategoList)sdf_parenthesize.constNil0));
        }
      }
      context.popOnSuccess();
      if(true)
        return term;
    }
    context.popOnFailure();
    return null;
  }
}