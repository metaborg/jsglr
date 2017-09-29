package org.spoofax.jsglr2.tests.util;

import static java.util.Collections.sort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.imploder.hybrid.HStrategoImploder;
import org.spoofax.jsglr2.imploder.symbolrule.SRStrategoImploder;
import org.spoofax.jsglr2.parseforest.hybrid.HParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HParseForestManager;
import org.spoofax.jsglr2.parseforest.symbolrule.RuleNode;
import org.spoofax.jsglr2.parseforest.symbolrule.SRParseForest;
import org.spoofax.jsglr2.parseforest.symbolrule.SRParseForestManager;
import org.spoofax.jsglr2.parseforest.symbolrule.SymbolNode;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseResult;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.Reducer;
import org.spoofax.jsglr2.parser.ReducerElkhound;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.elkhound.StandardElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.ElkhoundStackNode;
import org.spoofax.jsglr2.stack.standard.StandardStackManager;
import org.spoofax.jsglr2.util.AstUtilities;
import org.spoofax.jsglr2.util.Sdf2Table;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class BaseTest {

    private TermReader termReader;
    private IStrategoTerm parseTableTerm;
    
	protected AstUtilities astUtilities;
    
    protected BaseTest() {
        TermFactory termFactory = new TermFactory();
        this.termReader = new TermReader(termFactory);
        
        this.astUtilities = new AstUtilities();
    }
    
    @BeforeClass
    public static void setUpNativeSdf2Table() throws URISyntaxException, IOException {
    		Sdf2Table.setupSdf2TableInTargetDir();
    }
    
    public TermReader getTermReader() {
        return termReader;
    }
    
    public void setParseTableTerm(IStrategoTerm parseTableTerm) {
        this.parseTableTerm = parseTableTerm;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }
    
    private IParseTable getParseTable() {
        try {
            return ParseTableReader.read(getParseTableTerm());
        } catch(ParseTableReadException e) {
            e.printStackTrace();
            
            fail();
            
            return null;
        }
    }
    
    private IParser<?, SRParseForest> srParser() {
        return new Parser(getParseTable(), new StandardStackManager<SRParseForest>(), new SRParseForestManager());
    }
    
    private IParser<?, SRParseForest> srElkhoundParser() {
        IParseTable parseTable = getParseTable();
        StackManager<ElkhoundStackNode<SRParseForest>, SRParseForest> stackManager = new StandardElkhoundStackManager<SRParseForest>();
        SRParseForestManager parseForestManager = new SRParseForestManager();
        
        Reducer<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode> elkhoundReducer = new ReducerElkhound<SRParseForest, SymbolNode, RuleNode>(parseTable, stackManager, parseForestManager);
        
        return new Parser(parseTable, stackManager, parseForestManager, elkhoundReducer);
    }
    
    private IParser<?, HParseForest> hParser() {
        return new Parser(getParseTable(), new StandardElkhoundStackManager<SRParseForest>(), new HParseForestManager());
    }

	public void testParseFailure(String inputString) {
        ParseResult<SRParseForest> srParseResult = srParser().parse(inputString);
        ParseResult<HParseForest> hParseResult = hParser().parse(inputString);

        assertEquals(false, srParseResult.isSuccess);
        assertEquals(false, hParseResult.isSuccess);
	}

    protected IStrategoTerm testSRParseSuccess(String inputString) {
        ParseResult<SRParseForest> srParseResult = srParser().parse(inputString);

        assertEquals(true, srParseResult.isSuccess);

        IImploder<SRParseForest, IStrategoTerm> srImploder = new SRStrategoImploder();
        
        ImplodeResult<IStrategoTerm> implodeResult = srImploder.implode(srParseResult.parse, ((ParseSuccess<SRParseForest>) srParseResult).parseResult);
        
        assertEquals(true, implodeResult.isSuccess);

        return implodeResult.ast;
    }

    protected IStrategoTerm testHParseSuccess(String inputString) {
        ParseResult<HParseForest> hParseResult = hParser().parse(inputString);

        assertEquals(true, hParseResult.isSuccess);

        IImploder<HParseForest, IStrategoTerm> hImploder = new HStrategoImploder();
        
        ImplodeResult<IStrategoTerm> implodeResult = hImploder.implode(hParseResult.parse, ((ParseSuccess<HParseForest>) hParseResult).parseResult);
        
        assertEquals(true, implodeResult.isSuccess);

        return implodeResult.ast;
    }

    protected IStrategoTerm testSRElkhoundParseSuccess(String inputString) {
        ParseResult<SRParseForest> srElkhoundParseResult = srElkhoundParser().parse(inputString);

        assertEquals(true, srElkhoundParseResult.isSuccess);

        IImploder<SRParseForest, IStrategoTerm> srImploder = new SRStrategoImploder();
        
        ImplodeResult<IStrategoTerm> implodeResult = srImploder.implode(srElkhoundParseResult.parse, ((ParseSuccess<SRParseForest>) srElkhoundParseResult).parseResult);
        
        assertEquals(true, implodeResult.isSuccess);

        return implodeResult.ast;
    }
	
	protected void testParseSuccessByAstString(String inputString, String expectedOutputAstString) {
		testParseSuccess(inputString, expectedOutputAstString, false);
	}
	
	protected void testParseSuccessByExpansions(String inputString, String expectedOutputAstString) {
		testParseSuccess(inputString, expectedOutputAstString, true);
	}
	
	private void testParseSuccess(String inputString, String expectedOutputAstString, boolean equalityByExpansions) {
        IStrategoTerm srActualOutputAst = testSRParseSuccess(inputString);
        IStrategoTerm hActualOutputAst = testHParseSuccess(inputString);
        IStrategoTerm srElkhoundActualOutputAst = testSRElkhoundParseSuccess(inputString);
		
		if (equalityByExpansions) {
			IStrategoTerm expectedOutputAst = termReader.parseFromString(expectedOutputAstString);

            assertEqualTermExpansions(expectedOutputAst, srActualOutputAst);
            assertEqualTermExpansions(expectedOutputAst, hActualOutputAst);
            assertEqualTermExpansions(expectedOutputAst, srElkhoundActualOutputAst);
		} else {
            assertEquals(expectedOutputAstString, srActualOutputAst.toString());
            assertEquals(expectedOutputAstString, hActualOutputAst.toString());
            assertEquals(expectedOutputAstString, srElkhoundActualOutputAst.toString());
		}
	}
	
	protected void assertEqualTermExpansions(IStrategoTerm expected, IStrategoTerm actual) {
		List<String> expectedExpansion = toSortedStringList(this.astUtilities.expand(expected));
		List<String> actualExpansion = toSortedStringList(this.astUtilities.expand(actual));
		
		assertEquals(expectedExpansion, actualExpansion);
		
	}
	
	private List<String> toSortedStringList(List<IStrategoTerm> astExpansion) {
		List<String> result = new ArrayList<String>(astExpansion.size());
		
		for (IStrategoTerm ast : astExpansion) {
			result.add(ast.toString());
		}
		
		sort(result);
		
		return result;
	}

    protected String getFileAsString(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        byte[] encoded = Files.readAllBytes(Paths.get(classLoader.getResource("samples/" + filename).getPath()));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    protected IStrategoTerm getFileAsAST(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = new FileInputStream(classLoader.getResource("samples/" + filename).getFile());
        return this.termReader.parseFromStream(inputStream);
    }
	
}
