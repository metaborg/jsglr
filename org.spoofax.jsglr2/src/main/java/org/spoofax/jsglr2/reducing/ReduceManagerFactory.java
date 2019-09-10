package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.datadependent.DataDependentReduceManager;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.elkhound.ElkhoundReduceManager;
import org.spoofax.jsglr2.elkhound.ElkhoundStackManager;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.IncrementalReduceManager;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseForest;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveReduceManager;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;

// Implementation note: in theory, this factory is an unnecessary bypass for the constructors of ReduceManagers.
// It is unnecessary, because the factory is only used once after it has been created and passed to the Parser.
// However, using this factory prevents the duplication of the new StackManager in JSGLR2Variants.getParser.
// This duplication would either mean
// - having to extract the new StackManager as a variable and having to explicitly write all type parameters, or
// - constructing two new StackManagers instead.
// Having a ReduceManagerFactory was preferred over these other two options.
public interface ReduceManagerFactory
//@formatter:off
   <ParseForest   extends IParseForest,
    ParseNode     extends ParseForest,
    Derivation    extends IDerivation<ParseForest>,
    StackNode     extends IStackNode,
    ParseState    extends AbstractParseState<ParseForest, StackNode>,
    StackManager  extends AbstractStackManager<ParseForest, StackNode, ParseState>,
    ReduceManager extends org.spoofax.jsglr2.reducing.ReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>>
//@formatter:on
{
    ReduceManager get(IParseTable parseTable, StackManager stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager);

    static
    //@formatter:off
       <ParseForest  extends IParseForest,
        ParseNode    extends ParseForest,
        Derivation   extends IDerivation<ParseForest>,
        StackNode    extends IStackNode,
        ParseState   extends AbstractParseState<ParseForest, StackNode>,
        StackManager extends AbstractStackManager<ParseForest, StackNode, ParseState>>
    //@formatter:on
    ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, StackManager, org.spoofax.jsglr2.reducing.ReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>>
        reduceManagerFactory(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new org.spoofax.jsglr2.reducing.ReduceManager<>(
            parseTable, stackManager, parseForestManager, parserVariant.parseForestConstruction);
    }

    static
    //@formatter:off
       <ParseForest  extends IParseForest,
        ParseNode    extends ParseForest,
        Derivation   extends IDerivation<ParseForest>,
        StackNode    extends AbstractElkhoundStackNode<ParseForest>,
        ParseState   extends AbstractParseState<ParseForest, StackNode>,
        StackManager extends ElkhoundStackManager<ParseForest, StackNode, ParseState>>
    //@formatter:on
    ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, StackManager, ElkhoundReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>>
        elkhoundReduceManagerFactory(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new ElkhoundReduceManager<>(parseTable, stackManager,
            parseForestManager, parserVariant.parseForestConstruction);
    }

    static
    //@formatter:off
       <ParseForest  extends IParseForest,
        ParseNode    extends ParseForest,
        Derivation   extends IDerivation<ParseForest>,
        StackNode    extends IStackNode,
        ParseState   extends AbstractParseState<ParseForest, StackNode>,
        StackManager extends AbstractStackManager<ParseForest, StackNode, ParseState>>
    //@formatter:on
    ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, StackManager, DataDependentReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>>
        dataDependentReduceManagerFactory(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new DataDependentReduceManager<>(parseTable,
            stackManager, parseForestManager, parserVariant.parseForestConstruction);
    }

    static
    //@formatter:off
       <ParseForest  extends LayoutSensitiveParseForest,
        ParseNode    extends ParseForest,
        Derivation   extends IDerivation<ParseForest>,
        StackNode    extends IStackNode,
        ParseState   extends AbstractParseState<ParseForest, StackNode>,
        StackManager extends AbstractStackManager<ParseForest, StackNode, ParseState>>
    //@formatter:on
    ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, StackManager, LayoutSensitiveReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>>
        layoutSensitiveReduceManagerFactory(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new LayoutSensitiveReduceManager<>(parseTable,
            stackManager, parseForestManager, parserVariant.parseForestConstruction);
    }

    static
    //@formatter:off
       <ParseForest  extends IncrementalParseForest,
        ParseNode    extends ParseForest,
        Derivation   extends IDerivation<ParseForest>,
        StackNode    extends IStackNode,
        ParseState   extends AbstractParseState<ParseForest, StackNode> & IIncrementalParseState,
        StackManager extends AbstractStackManager<ParseForest, StackNode, ParseState>>
    //@formatter:on
    ReduceManagerFactory<ParseForest, ParseNode, Derivation, StackNode, ParseState, StackManager, IncrementalReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>>
        incrementalReduceManagerFactory(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new IncrementalReduceManager<>(parseTable,
            stackManager, parseForestManager, parserVariant.parseForestConstruction);
    }

}
