package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class sdf_parenthesize  
{ 
  protected static final boolean TRACES_ENABLED = true;

  protected static ITermFactory constantFactory;

  private static WeakReference<Context> initedContext;

  private static boolean isIniting;

  protected static IStrategoTerm constNil0;

  public static IStrategoConstructor _consConc_2;

  public static IStrategoConstructor _consNone_0;

  public static IStrategoConstructor _consSome_1;

  protected static IStrategoConstructor _consconc_grammars_2;

  protected static IStrategoConstructor _conswith_arguments_2;

  protected static IStrategoConstructor _consconc_2;

  protected static IStrategoConstructor _consnon_transitive_1;

  protected static IStrategoConstructor _consseq_2;

  protected static IStrategoConstructor _consopt_1;

  protected static IStrategoConstructor _consiter_star_1;

  protected static IStrategoConstructor _consalt_2;

  protected static IStrategoConstructor _conslabel_2;

  protected static IStrategoConstructor _consiter_1;

  protected static IStrategoConstructor _consdiff_2;

  protected static IStrategoConstructor _consisect_2;

  protected static IStrategoConstructor _consunion_2;

  protected static IStrategoConstructor _conscomp_1;

  protected static IStrategoConstructor _consprod_3;

  public static IStrategoConstructor _consParenthetical_1;

  public static Context init(Context context)
  { 
    synchronized(sdf_parenthesize.class)
    { 
      if(isIniting)
        return null;
      try
      { 
        isIniting = true;
        ITermFactory termFactory = context.getFactory();
        if(constantFactory == null)
        { 
          initConstructors(termFactory);
          initConstants(termFactory);
        }
        if(initedContext == null || initedContext.get() != context)
        { 
          org.strategoxt.stratego_lib.Main.init(context);
          context.registerComponent("sdf_parenthesize");
        }
        initedContext = new WeakReference<Context>(context);
        constantFactory = termFactory;
      }
      finally
      { 
        isIniting = false;
      }
      return context;
    }
  }

  public static Context init()
  { 
    return init(new Context());
  }

  public static void main(String args[])
  { 
    Context context = init();
    context.setStandAlone(true);
    try
    { 
      IStrategoTerm result;
      try
      { 
        result = context.invokeStrategyCLI(main_0_0.instance, "sdf_parenthesize", args);
      }
      finally
      { 
        context.getIOAgent().closeAllFiles();
      }
      if(result == null)
      { 
        System.err.println("sdf_parenthesize" + (TRACES_ENABLED ? ": rewriting failed, trace:" : ": rewriting failed"));
        context.printStackTrace();
        context.setStandAlone(false);
        System.exit(1);
      }
      else
      { 
        System.out.println(result);
        context.setStandAlone(false);
        System.exit(0);
      }
    }
    catch(StrategoExit exit)
    { 
      context.setStandAlone(false);
      System.exit(exit.getValue());
    }
  }

  public static IStrategoTerm mainNoExit(String ... args) throws StrategoExit
  { 
    return mainNoExit(new Context(), args);
  }

  public static IStrategoTerm mainNoExit(Context context, String ... args) throws StrategoExit
  { 
    try
    { 
      init(context);
      return context.invokeStrategyCLI(main_0_0.instance, "sdf_parenthesize", args);
    }
    finally
    { 
      context.getIOAgent().closeAllFiles();
    }
  }

  public static Strategy getMainStrategy()
  { 
    return main_0_0.instance;
  }

  public static void initConstructors(ITermFactory termFactory)
  { 
    _consConc_2 = termFactory.makeConstructor("Conc", 2);
    _consNone_0 = termFactory.makeConstructor("None", 0);
    _consSome_1 = termFactory.makeConstructor("Some", 1);
    _consconc_grammars_2 = termFactory.makeConstructor("conc-grammars", 2);
    _conswith_arguments_2 = termFactory.makeConstructor("with-arguments", 2);
    _consconc_2 = termFactory.makeConstructor("conc", 2);
    _consnon_transitive_1 = termFactory.makeConstructor("non-transitive", 1);
    _consseq_2 = termFactory.makeConstructor("seq", 2);
    _consopt_1 = termFactory.makeConstructor("opt", 1);
    _consiter_star_1 = termFactory.makeConstructor("iter-star", 1);
    _consalt_2 = termFactory.makeConstructor("alt", 2);
    _conslabel_2 = termFactory.makeConstructor("label", 2);
    _consiter_1 = termFactory.makeConstructor("iter", 1);
    _consdiff_2 = termFactory.makeConstructor("diff", 2);
    _consisect_2 = termFactory.makeConstructor("isect", 2);
    _consunion_2 = termFactory.makeConstructor("union", 2);
    _conscomp_1 = termFactory.makeConstructor("comp", 1);
    _consprod_3 = termFactory.makeConstructor("prod", 3);
    _consParenthetical_1 = termFactory.makeConstructor("Parenthetical", 1);
  }

  public static void initConstants(ITermFactory termFactory)
  { 
    constNil0 = (IStrategoTerm)termFactory.makeList();
  }

  public static void registerInterop(org.spoofax.interpreter.core.IContext context, Context compiledContext)
  { 
    new InteropRegisterer().registerLazy(context, compiledContext, InteropRegisterer.class.getClassLoader());
  }
}