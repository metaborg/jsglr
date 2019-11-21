package org.spoofax.jsglr2.benchmark.antlr4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.Tree;

public class ANTLRJavaBenchmark extends ANTLRBenchmark<JavaLexer, JavaParser> {

    public ANTLRJavaBenchmark() {
        super();
    }

    protected JavaLexer lexer(CharStream charStream) {
        return new JavaLexer(charStream);
    }

    protected JavaParser parser(TokenStream tokens) {
        return new JavaParser(tokens);
    }

    protected Tree result(JavaParser parser) {
        return parser.compilationUnit();
    }

}
