package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.composite.CompositeParseForestManager;
import org.spoofax.jsglr2.composite.CompositeParseState;
import org.spoofax.jsglr2.composite.CompositeReduceManager;
import org.spoofax.jsglr2.datadependent.DataDependentParseForestManager;
import org.spoofax.jsglr2.datadependent.DataDependentReduceManager;
import org.spoofax.jsglr2.elkhound.BasicElkhoundStackManager;
import org.spoofax.jsglr2.elkhound.ElkhoundParser;
import org.spoofax.jsglr2.elkhound.ElkhoundReduceManager;
import org.spoofax.jsglr2.elkhound.HybridElkhoundStackManager;
import org.spoofax.jsglr2.incremental.IncrementalParseState;
import org.spoofax.jsglr2.incremental.IncrementalParser;
import org.spoofax.jsglr2.incremental.IncrementalReduceManager;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForestManager;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseForestManager;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseState;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveReduceManager;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForestManager;
import org.spoofax.jsglr2.parseforest.empty.NullParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForestManager;
import org.spoofax.jsglr2.parser.failure.DefaultParseFailureHandler;
import org.spoofax.jsglr2.recovery.*;
import org.spoofax.jsglr2.recoveryincremental.RecoveryIncrementalParseState;
import org.spoofax.jsglr2.reducing.ReduceActionFilter;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.stack.hybrid.HybridStackManager;

public class ParserVariant {
    public final ActiveStacksRepresentation activeStacksRepresentation;
    public final ForActorStacksRepresentation forActorStacksRepresentation;
    public final ParseForestRepresentation parseForestRepresentation;
    public final ParseForestConstruction parseForestConstruction;
    public final StackRepresentation stackRepresentation;
    public final Reducing reducing;
    public final boolean recovery;

    public ParserVariant(ActiveStacksRepresentation activeStacksRepresentation,
        ForActorStacksRepresentation forActorStacksRepresentation, ParseForestRepresentation parseForestRepresentation,
        ParseForestConstruction parseForestConstruction, StackRepresentation stackRepresentation, Reducing reducing,
        boolean recovery) {
        this.activeStacksRepresentation = activeStacksRepresentation;
        this.forActorStacksRepresentation = forActorStacksRepresentation;
        this.parseForestRepresentation = parseForestRepresentation;
        this.parseForestConstruction = parseForestConstruction;
        this.stackRepresentation = stackRepresentation;
        this.reducing = reducing;
        this.recovery = recovery;
    }

    public boolean isValid() {
        // The implication N -> F is written as !N || F

        // Elkhound reducing requires Elkhound stack, and the other way around (bi-implication)
        boolean validElkhound =
            (reducing == Reducing.Elkhound) == (stackRepresentation == StackRepresentation.BasicElkhound
                || stackRepresentation == StackRepresentation.HybridElkhound);

        boolean validParseForest = parseForestRepresentation != ParseForestRepresentation.Null
            || parseForestConstruction == ParseForestConstruction.Full;

        boolean validIncremental =
            (parseForestRepresentation == ParseForestRepresentation.Incremental) == (reducing == Reducing.Incremental)
                // Incremental parsing requires a full parse forest
                && (reducing != Reducing.Incremental || parseForestConstruction == ParseForestConstruction.Full);

        boolean validLayoutSensitive =
            (parseForestRepresentation == ParseForestRepresentation.LayoutSensitive) == (reducing == Reducing.LayoutSensitive);

        boolean validDataDependent =
            (parseForestRepresentation == ParseForestRepresentation.DataDependent) == (reducing == Reducing.DataDependent);

        // Recovery and layout-sensitive parsing not simultaneously supported
        boolean validRecoveryLayoutSensitive =
            !(parseForestRepresentation == ParseForestRepresentation.LayoutSensitive && recovery);

        boolean validComposite =
            (parseForestRepresentation == ParseForestRepresentation.Composite) == (reducing == Reducing.Composite);

        return validElkhound && validParseForest && validIncremental && validLayoutSensitive && validDataDependent
            && validRecoveryLayoutSensitive && validComposite;
    }

    public String name() {
        return "ActiveStacksRepresentation:" + activeStacksRepresentation + "/ForActorStacksRepresentation:"
            + forActorStacksRepresentation + "/ParseForestRepresentation:" + parseForestRepresentation
            + "/ParseForestConstruction:" + parseForestConstruction + "/StackRepresentation:" + stackRepresentation
            + "/Reducing:" + reducing + "/Recovery:" + recovery;
    }

    @Override public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;

        ParserVariant that = (ParserVariant) o;

        return activeStacksRepresentation == that.activeStacksRepresentation
            && forActorStacksRepresentation == that.forActorStacksRepresentation
            && parseForestRepresentation == that.parseForestRepresentation
            && parseForestConstruction == that.parseForestConstruction
            && stackRepresentation == that.stackRepresentation && reducing == that.reducing
            && recovery == that.recovery;
    }

    private static
//@formatter:off
   <ParseForest          extends IParseForest,
    StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<StackNode>,
    ParseState           extends AbstractParseState<ParseForest, StackNode> & IRecoveryParseState<StackNode, BacktrackChoicePoint>>
//@formatter:on
    IParser<? extends IParseForest> withRecovery(Parser<ParseForest, ?, ?, StackNode, ParseState, ?, ?> parser) {
        parser.observing().attachObserver(new RecoveryParserObserver<>());
        parser.reduceManager.addFilter(new RecoveryReduceActionFilter<>());

        return parser;
    }

    private static
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    IParser<? extends IParseForest> withoutRecovery(Parser<ParseForest, ?, ?, StackNode, ParseState, ?, ?> parser) {
        parser.reduceManager.addFilter(ReduceActionFilter.ignoreRecoveryAndCompletion());

        return parser;
    }

    public IParser<? extends IParseForest> getParser(IParseTable parseTable) {
        if(!this.isValid())
            throw new IllegalStateException("Invalid parser variant");

        // @formatter:off
        switch(this.parseForestRepresentation) {
            default:
            case Basic: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(RecoveryParseState.factory(this), parseTable, BasicElkhoundStackManager.factory(),  BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(ParseState.factory(this),         parseTable, BasicElkhoundStackManager.factory(),  BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), DefaultParseFailureHandler.factory()));
                    case HybridElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(RecoveryParseState.factory(this), parseTable, HybridElkhoundStackManager.factory(), BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(ParseState.factory(this),         parseTable, HybridElkhoundStackManager.factory(), BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, BasicStackManager.factory(),  BasicParseForestManager.factory(), ReduceManager.factory(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, BasicStackManager.factory(),  BasicParseForestManager.factory(), ReduceManager.factory(this), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, HybridStackManager.factory(), BasicParseForestManager.factory(), ReduceManager.factory(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, HybridStackManager.factory(), BasicParseForestManager.factory(), ReduceManager.factory(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with basic parse forest representation");
            }

            case Null: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(RecoveryParseState.factory(this), parseTable, BasicElkhoundStackManager.factory(),  NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(ParseState.factory(this),         parseTable, BasicElkhoundStackManager.factory(),  NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), DefaultParseFailureHandler.factory()));
                    case HybridElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(RecoveryParseState.factory(this), parseTable, HybridElkhoundStackManager.factory(), NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(ParseState.factory(this),         parseTable, HybridElkhoundStackManager.factory(), NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, BasicStackManager.factory(),  NullParseForestManager.factory(), ReduceManager.factory(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, BasicStackManager.factory(),  NullParseForestManager.factory(), ReduceManager.factory(this), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, HybridStackManager.factory(), NullParseForestManager.factory(), ReduceManager.factory(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, HybridStackManager.factory(), NullParseForestManager.factory(), ReduceManager.factory(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with empty parse forest representation");
            }

            case Hybrid: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(RecoveryParseState.factory(this), parseTable, BasicElkhoundStackManager.factory(),  HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(ParseState.factory(this),         parseTable, BasicElkhoundStackManager.factory(),  HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), DefaultParseFailureHandler.factory()));
                    case HybridElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(RecoveryParseState.factory(this), parseTable, HybridElkhoundStackManager.factory(), HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(ParseState.factory(this),         parseTable, HybridElkhoundStackManager.factory(), HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, BasicStackManager.factory(),  HybridParseForestManager.factory(), ReduceManager.factory(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, BasicStackManager.factory(),  HybridParseForestManager.factory(), ReduceManager.factory(this), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, HybridStackManager.factory(), HybridParseForestManager.factory(), ReduceManager.factory(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, HybridStackManager.factory(), HybridParseForestManager.factory(), ReduceManager.factory(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with hybrid parse forest representation");
            }

            case DataDependent:
                if(this.reducing != Reducing.DataDependent)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, BasicStackManager.factory(),  DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, BasicStackManager.factory(),  DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(this), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(RecoveryParseState.factory(this), parseTable, HybridStackManager.factory(), DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(ParseState.factory(this),         parseTable, HybridStackManager.factory(), DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }

            case LayoutSensitive:
                if(this.reducing != Reducing.LayoutSensitive || this.recovery)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(LayoutSensitiveRecoveryParseState.factory(this), parseTable, BasicStackManager.factory(),  LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(LayoutSensitiveParseState.factory(this),         parseTable, BasicStackManager.factory(),  LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(LayoutSensitiveRecoveryParseState.factory(this), parseTable, HybridStackManager.factory(), LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(LayoutSensitiveParseState.factory(this),         parseTable, HybridStackManager.factory(), LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }

            case Composite:
                if(this.reducing != Reducing.Composite || this.recovery)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(LayoutSensitiveRecoveryParseState.factory(this), parseTable, BasicStackManager.factory(),  LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(CompositeParseState.factory(this),         parseTable, BasicStackManager.factory(),  CompositeParseForestManager.factory(), CompositeReduceManager.factoryComposite(this), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(LayoutSensitiveRecoveryParseState.factory(this), parseTable, HybridStackManager.factory(), LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(CompositeParseState.factory(this),         parseTable, HybridStackManager.factory(), CompositeParseForestManager.factory(), CompositeReduceManager.factoryComposite(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }

            case Incremental:
                if(this.reducing != Reducing.Incremental)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new IncrementalParser<>(RecoveryIncrementalParseState.factory(this), parseTable, BasicStackManager.factory(),  IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new IncrementalParser<>(        IncrementalParseState.factory(this), parseTable, BasicStackManager.factory(),  IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(this), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new IncrementalParser<>(RecoveryIncrementalParseState.factory(this), parseTable, HybridStackManager.factory(), IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(this), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new IncrementalParser<>(        IncrementalParseState.factory(this), parseTable, HybridStackManager.factory(), IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(this), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }
        }
        // @formatter:on
    }
}
