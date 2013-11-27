package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("unused") public class InteropRegisterer extends org.strategoxt.lang.InteropRegisterer 
{ 
  @Override public void register(org.spoofax.interpreter.core.IContext context, Context compiledContext)
  { 
    register(context, compiledContext, context.getVarScope());
  }

  @Override public void registerLazy(org.spoofax.interpreter.core.IContext context, Context compiledContext, ClassLoader classLoader)
  { 
    registerLazy(context, compiledContext, classLoader, context.getVarScope());
  }

  private void register(org.spoofax.interpreter.core.IContext context, Context compiledContext, org.spoofax.interpreter.core.VarScope varScope)
  { 
    compiledContext.registerComponent("sdf_parenthesize");
    sdf_parenthesize.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(main_0_0.instance, context));
    varScope.addSVar("parenthesize_Sdf2_0_0", new InteropSDefT(parenthesize_$Sdf2_0_0.instance, context));
    varScope.addSVar("Parenthesize_List_1_0", new InteropSDefT($Parenthesize_$List_1_0.instance, context));
  }

  private void registerLazy(org.spoofax.interpreter.core.IContext context, Context compiledContext, ClassLoader classLoader, org.spoofax.interpreter.core.VarScope varScope)
  { 
    compiledContext.registerComponent("sdf_parenthesize");
    sdf_parenthesize.init(compiledContext);
    varScope.addSVar("main_0_0", new InteropSDefT(classLoader, "sdf_parenthesize.main_0_0", context));
    varScope.addSVar("parenthesize_Sdf2_0_0", new InteropSDefT(classLoader, "sdf_parenthesize.parenthesize_$Sdf2_0_0", context));
    varScope.addSVar("Parenthesize_List_1_0", new InteropSDefT(classLoader, "sdf_parenthesize.$Parenthesize_$List_1_0", context));
  }
}