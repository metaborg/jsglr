package org.spoofax.jsglr2;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestRepresentation;
import org.spoofax.jsglr2.JSGLR2Variants.Reducing;
import org.spoofax.jsglr2.JSGLR2Variants.StackRepresentation;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.ParseFailure;
import org.spoofax.jsglr2.parser.ParseResult;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.elkhound.ElkhoundStackNode;

public class JSGLR2<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree> {

    public IParser<StackNode, ParseForest> parser;
    public IImploder<ParseForest, AbstractSyntaxTree> imploder;
    
    public static JSGLR2<ElkhoundStackNode<HParseForest>, HParseForest, IStrategoTerm> standard(IParseTable parseTable) throws ParseTableReadException {
        return (JSGLR2<ElkhoundStackNode<HParseForest>, HParseForest, IStrategoTerm>) JSGLR2Variants.getJSGLR2(parseTable, ParseForestRepresentation.Hybrid, StackRepresentation.Elkhound, Reducing.Elkhound);
    }
    
    public static JSGLR2<ElkhoundStackNode<HParseForest>, HParseForest, IStrategoTerm> standard(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        IParseTable parseTable = ParseTableReader.read(parseTableTerm);

        return standard(parseTable);
    }
    
    public JSGLR2(IParser<StackNode, ParseForest> parser, IImploder<ParseForest, AbstractSyntaxTree> imploder) {
        this.parser = parser;
        this.imploder = imploder;
    }
    
    public AbstractSyntaxTree parse(String input, String filename) {
        ParseResult<ParseForest> parseResult = parser.parse(input, filename);
        
        if (!parseResult.isSuccess)
            return null;
        
        ParseSuccess<ParseForest> parseSuccess = (ParseSuccess<ParseForest>) parseResult;
        
        ImplodeResult<AbstractSyntaxTree> implodeResult = imploder.implode(parseSuccess.parse, parseSuccess.parseResult);
        
        return implodeResult.ast;
    }
    
    public AbstractSyntaxTree parse(String input) {
        return parse(input, "");
    }
    
    public AbstractSyntaxTree parseUnsafe(String input, String filename) throws ParseException {
        ParseResult<ParseForest> result = parser.parse(input, filename);
        
        if (result.isSuccess) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) result;
        
            ImplodeResult<AbstractSyntaxTree> implodeResult = imploder.implode(success.parse, success.parseResult);
            
            return implodeResult.ast;
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) result;
            
            throw failure.parseException;
        }
    }
    
    public AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "");
    }
    
}
