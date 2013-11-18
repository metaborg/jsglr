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
    varScope.addSVar("io_core_sdf_parenthesize_0_0", new InteropSDefT(io_core_sdf_parenthesize_0_0.instance, context));
    varScope.addSVar("parenthesize_Sdf2_0_0", new InteropSDefT(parenthesize_$Sdf2_0_0.instance, context));
    varScope.addSVar("Sdf2Parenthesize_0_0", new InteropSDefT($Sdf2$Parenthesize_0_0.instance, context));
    varScope.addSVar("Parenthetical_1_0", new InteropSDefT($Parenthetical_1_0.instance, context));
  }

  private void registerLazy(org.spoofax.interpreter.core.IContext context, Context compiledContext, ClassLoader classLoader, org.spoofax.interpreter.core.VarScope varScope)
  { 
    compiledContext.registerComponent("sdf_parenthesize");
    sdf_parenthesize.init(compiledContext);
    varScope.addSVar("io_core_sdf_parenthesize_0_0", new InteropSDefT(classLoader, "sdf_parenthesize.io_core_sdf_parenthesize_0_0", context));
    varScope.addSVar("parenthesize_Sdf2_0_0", new InteropSDefT(classLoader, "sdf_parenthesize.parenthesize_$Sdf2_0_0", context));
    varScope.addSVar("Sdf2Parenthesize_0_0", new InteropSDefT(classLoader, "sdf_parenthesize.$Sdf2$Parenthesize_0_0", context));
    varScope.addSVar("Parenthetical_1_0", new InteropSDefT(classLoader, "sdf_parenthesize.$Parenthetical_1_0", context));
  }
}