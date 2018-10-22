package org.spoofax.jsglr2.integration.test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.junit.Test;
import org.metaborg.core.context.IContext;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.project.ISimpleProjectService;
import org.metaborg.core.project.SimpleProjectService;
import org.metaborg.meta.core.project.ILanguageSpec;
import org.metaborg.parsetable.IParseTable;
import org.metaborg.sdf2table.grammar.NormGrammar;
import org.metaborg.sdf2table.io.GrammarReader;
import org.metaborg.sdf2table.parsetable.ParseTable;
import org.metaborg.spoofax.core.Spoofax;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.meta.core.SpoofaxExtensionModule;
import org.metaborg.spoofax.meta.core.SpoofaxMeta;
import org.metaborg.util.concurrent.IClosableLock;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.strategoxt.HybridInterpreter;

import com.google.common.collect.Iterables;
import com.google.inject.Singleton;

public class MainTest {

    public static class Module extends SpoofaxModule {
        @Override protected void bindProject() {
            bind(SimpleProjectService.class).in(Singleton.class);
            bind(IProjectService.class).to(SimpleProjectService.class);
        }
    }

    @Test
    public void test() throws Exception {
    	final Spoofax spoofax = new Spoofax(new Module(), new SpoofaxExtensionModule());
    	final SpoofaxMeta spoofaxMeta = new SpoofaxMeta(spoofax);

    	final FileObject sdf3Location = spoofax.resolve("zip://" + getTestResourcePath("sdf3.spoofax-language"));
        
        final Set<ILanguageImpl> languageImpls = spoofax.scanLanguagesInDirectory(sdf3Location);

        final ILanguageImpl sdf3Impl = Iterables.get(languageImpls, 0);
        final ILanguageComponent sdf3Component = Iterables.get(sdf3Impl.components(), 0);
        
        final FileObject sdf3File = spoofax.resourceService.resolve(getTestResourcePath("test.sdf3"));
        final String sdf3Text = spoofax.sourceTextService.text(sdf3File);
        
        ISpoofaxInputUnit inputUnit = spoofax.unitService.inputUnit(sdf3File, sdf3Text, sdf3Impl, null);
        
        final ISpoofaxParseUnit parseResult = spoofax.syntaxService.parse(inputUnit);

        final FileObject directory = sdf3File.getParent();
        final IProject project = ((ISimpleProjectService) spoofax.projectService).create(directory);
        final ILanguageSpec languageSpec = spoofaxMeta.languageSpecService.get(project);
        final IContext context = spoofax.contextService.get(directory, languageSpec, sdf3Impl);
        
        final IStrategoTerm sdf3Module = parseResult.ast();
        
        try(IClosableLock lock = context.read()) {
            final HybridInterpreter runtime = spoofax.strategoRuntimeService.runtime(sdf3Component, context, false);
            final IStrategoTerm sdf3ModuleNormalized = spoofax.strategoCommon.invoke(runtime, sdf3Module, "module-to-normal-form");
            
            NormGrammar grammar = new GrammarReader().readGrammar(sdf3ModuleNormalized);
            
            IParseTable parseTable = new ParseTable(grammar, false, false, true);
            
            JSGLR2<HybridParseForest, IStrategoTerm> jsglr2 = JSGLR2.standard(parseTable);
            
            IStrategoTerm result = jsglr2.parse("1+2");
            
            assertEquals(result.toString(), "Add(Int(\"1\"),Int(\"2\"))");
        }
    }
    
    private String getTestResourcePath(String resource) {
    	return getClass().getClassLoader().getResource(resource).getPath();
    }

}