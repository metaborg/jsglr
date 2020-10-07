import $file.spoofaxDeps

import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._
import $ivy.`org.metaborg:org.spoofax.jsglr2.integration:2.6.0-SNAPSHOT`

import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

import org.metaborg.parsetable.IParseTable
import org.metaborg.parsetable.ParseTableReadException

import org.spoofax.jsglr2.integration.IntegrationVariant

import java.io.FileInputStream;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;

val termFactory = new TermFactory()
val termReader = new TermReader(termFactory)

def readParseTableTerm(path: Path) = termReader.parseFromStream(new FileInputStream(path.toString))

def getJSGLR2(variant: IntegrationVariant, parseTablePath: Path) = {
    val parseTableTerm = readParseTableTerm(parseTablePath)
    val parseTable = variant.parseTable.parseTableReader().read(parseTableTerm)
    
    variant.jsglr2.getJSGLR2(parseTable)
}

def getJSGLR1(parseTablePath: Path) = {
    val parseTableTerm = readParseTableTerm(parseTablePath)

    val jsglr1TreeBuilder = new TreeBuilder(new TermTreeFactory(termFactory));
    val jsglr1ParseTable = new ParseTable(parseTableTerm, termFactory);

    new SGLR(jsglr1TreeBuilder, jsglr1ParseTable)
}