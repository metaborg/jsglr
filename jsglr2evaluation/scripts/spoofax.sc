import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._
import $file.spoofaxDeps

import $ivy.`org.metaborg:org.spoofax.jsglr2.integration:2.6.0-SNAPSHOT`

import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

import org.metaborg.parsetable.IParseTable
import org.metaborg.parsetable.ParseTableReadException

import org.spoofax.jsglr2.integration.IntegrationVariant

import java.io.InputStream
import org.spoofax.jsglr.client.{SGLR => JSGLR1};
import org.spoofax.jsglr.client.imploder.{TermTreeFactory => JSGLR1TermTreeFactory};
import org.spoofax.jsglr.client.imploder.{TreeBuilder => JSGLR1TreeBuilder};
import org.spoofax.jsglr.client.{ParseTable => JSGLR1ParseTable};

import org.metaborg.sdf2table.io.ParseTableIO;

val termFactory = new TermFactory()
val termReader = new TermReader(termFactory)

def readParseTableTerm(inputStream: InputStream) = termReader.parseFromStream(inputStream)

def getJSGLR2ParseTable(variant: IntegrationVariant, language: Language): IParseTable =
    if (language.dynamicParseTableGeneration)
        new ParseTableIO(language.parseTableStream).getParseTable()
    else {
        val parseTableTerm = readParseTableTerm(language.parseTableStream)

        variant.parseTable.parseTableReader().read(parseTableTerm)
    }

val jsglr2ParseTables = scala.collection.mutable.Map[Language, IParseTable]()

def getJSGLR2(variant: IntegrationVariant, language: Language) = {
    if (!jsglr2ParseTables.contains(language))
        jsglr2ParseTables.addOne((language, getJSGLR2ParseTable(variant, language)))

    val parseTable = jsglr2ParseTables(language)

    variant.jsglr2.getJSGLR2(parseTable)
}


def getJSGLR1ParseTable(language: Language): JSGLR1ParseTable = {
    language.parseTable match {
        case parseTable @ GitSpoofax(_, _, dynamic) =>
            val parseTableTerm = readParseTableTerm(parseTable.term(language))
            val persistedTable = parseTable.bin(language)

            new JSGLR1ParseTable(parseTableTerm, termFactory, persistedTable, new ParseTableIO(persistedTable))
        case _ =>
            val parseTableTerm = readParseTableTerm(language.parseTableStream)
            
            new JSGLR1ParseTable(parseTableTerm, termFactory)
    }
}

val jsglr1ParseTables = scala.collection.mutable.Map[Language, JSGLR1ParseTable]()

def getJSGLR1(language: Language) = {
    if (!jsglr1ParseTables.contains(language))
        jsglr1ParseTables.addOne((language, getJSGLR1ParseTable(language)))
    
    val parseTable = jsglr1ParseTables(language)
    val treeBuilder = new JSGLR1TreeBuilder(new JSGLR1TermTreeFactory(termFactory))

    new JSGLR1(treeBuilder, parseTable)
}