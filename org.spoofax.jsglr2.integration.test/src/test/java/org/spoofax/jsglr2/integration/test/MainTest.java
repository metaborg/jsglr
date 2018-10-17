package org.spoofax.jsglr2.integration.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.junit.Test;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.project.ISimpleProjectService;
import org.metaborg.core.project.SimpleProjectService;
import org.metaborg.meta.core.project.ILanguageSpec;
import org.metaborg.spoofax.core.Spoofax;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.meta.core.SpoofaxExtensionModule;
import org.metaborg.spoofax.meta.core.SpoofaxMeta;
import org.metaborg.util.concurrent.IClosableLock;
import org.spoofax.interpreter.terms.IStrategoTerm;
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
    public void test() throws MetaborgException, IOException {
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
        
        System.out.println("parsed: " +  parseResult.ast());
        
        try(IClosableLock lock = context.read()) {
            final HybridInterpreter runtime = spoofax.strategoRuntimeService.runtime(sdf3Component, context, false);
            final IStrategoTerm resultTerm = spoofax.strategoCommon.invoke(runtime, parseResult.ast(), "module-to-pp");
            System.out.println("after transform: " + resultTerm);
        }
        
        assertEquals(true, true);
    }
    
    private String getTestResourcePath(String resource) {
    	return getClass().getClassLoader().getResource(resource).getPath();
    }

}