package org.spoofax.jsglr2.parsetable;

public interface IProduction {

    int productionNumber();
    
    String sort();
    
    String constructor();
    
    String descriptor();
    
    boolean isContextFree();
    
    boolean isLayout();
    
    boolean isLiteral();
    
    boolean isLexical();
    
    boolean isList();
    
    boolean isOptional();
    
    boolean isCompletionOrRecovery();
    
    boolean isOperator();
    
}
