import $file.spoofaxDeps

import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._
import $ivy.`org.metaborg:org.spoofax.jsglr2.integration:2.6.0-SNAPSHOT`

import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

import org.metaborg.parsetable.IParseTable
import org.metaborg.parsetable.ParseTableReadException

import org.spoofax.jsglr2.integration.IntegrationVariant

val termReader = new TermReader(new TermFactory())

def readParseTableTerm(path: Path) = termReader.parseFromString(read! path)

def getJSGLR2(variant: IntegrationVariant, parseTablePath: Path) = {
    val parseTableTerm = readParseTableTerm(parseTablePath)
    val parseTable = variant.parseTable.parseTableReader().read(parseTableTerm)
    
    variant.jsglr2.getJSGLR2(parseTable)
}