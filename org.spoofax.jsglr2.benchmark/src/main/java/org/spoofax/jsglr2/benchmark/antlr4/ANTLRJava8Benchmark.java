package org.spoofax.jsglr2.benchmark.antlr4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.Tree;

public class ANTLRJava8Benchmark extends ANTLRBenchmark<Java8Lexer, Java8Parser> {

    public ANTLRJava8Benchmark() {
        super();
    }

    protected Java8Lexer lexer(CharStream charStream) {
        return new Java8Lexer(charStream);
    }

    protected Java8Parser parser(TokenStream tokens) {
        return new Java8Parser(tokens);
    }

    protected Tree result(Java8Parser parser) {
        return parser.compilationUnit();
    }

}
