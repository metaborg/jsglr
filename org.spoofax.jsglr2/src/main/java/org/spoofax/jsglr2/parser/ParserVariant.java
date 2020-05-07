package org.spoofax.jsglr2.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.composite.CompositeParseForestManager;
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
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.inputstack.InputStack;
import org.spoofax.jsglr2.inputstack.InputStackFactory;
import org.spoofax.jsglr2.inputstack.LayoutSensitiveInputStack;
import org.spoofax.jsglr2.inputstack.incremental.EagerIncrementalInputStack;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.inputstack.incremental.IncrementalInputStackFactory;
import org.spoofax.jsglr2.inputstack.incremental.LinkedIncrementalInputStack;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseForestManager;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveReduceManager;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForestManager;
import org.spoofax.jsglr2.parseforest.empty.NullParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForestManager;
import org.spoofax.jsglr2.parser.failure.DefaultParseFailureHandler;
import org.spoofax.jsglr2.recovery.*;
import org.spoofax.jsglr2.recoveryincremental.RecoveryIncrementalParseState;
import org.spoofax.jsglr2.reducing.*;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.collections.*;
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
        return validate().count() == 0;
    }

    public Stream<String> validate() {
        // Implication N -> F is written as !N || F
        // Bi-implication N <-> F is written as N == F

        Map<String, Boolean> constraints = new HashMap<>();

        constraints.put("both Reducing and StackRepresentation should use Elkhound",
            (reducing == Reducing.Elkhound) == (stackRepresentation == StackRepresentation.BasicElkhound
                || stackRepresentation == StackRepresentation.HybridElkhound));

        constraints.put("null parse forest requires Full parse forest construction",
            parseForestRepresentation != ParseForestRepresentation.Null
                || parseForestConstruction == ParseForestConstruction.Full);

        constraints.put("both Reducing and ParseForestRepresentation should use Incremental",
            (parseForestRepresentation == ParseForestRepresentation.Incremental) == (reducing == Reducing.Incremental));

        constraints.put("both Reducing and ParseForestRepresentation should use LayoutSensitive",
            (parseForestRepresentation == ParseForestRepresentation.LayoutSensitive) == (reducing == Reducing.LayoutSensitive));

        constraints.put("both Reducing and ParseForestRepresentation should use DataDependent",
            (parseForestRepresentation == ParseForestRepresentation.DataDependent) == (reducing == Reducing.DataDependent));

        constraints.put("both Reducing and ParseForestRepresentation should use Composite",
            (parseForestRepresentation == ParseForestRepresentation.Composite) == (reducing == Reducing.Composite));

        constraints.put("recovery and layout-sensitive parsing not simultaneously supported",
            !(parseForestRepresentation == ParseForestRepresentation.LayoutSensitive && recovery));

        constraints.put("recovery and composite parsing not simultaneously supported",
            !(parseForestRepresentation == ParseForestRepresentation.Composite && recovery));

        return constraints.entrySet().stream().filter(constraint -> !constraint.getValue()).map(Map.Entry::getKey);
    }

    public String name() {
        //@formatter:off
        return  (activeStacksRepresentation == ActiveStacksRepresentation.standard() ? "_" : "ActiveStacksRepresentation:" + activeStacksRepresentation)
        + "/" + (forActorStacksRepresentation == ForActorStacksRepresentation.standard() ? "_" : "ForActorStacksRepresentation:" + forActorStacksRepresentation)
        + "/" + (parseForestRepresentation == ParseForestRepresentation.standard() ? "_" : "ParseForestRepresentation:" + parseForestRepresentation)
        + "/" + (parseForestConstruction == ParseForestConstruction.standard() ? "_" : "ParseForestConstruction:" + parseForestConstruction)
        + "/" + (stackRepresentation == StackRepresentation.standard() ? "_" : "StackRepresentation:" + stackRepresentation)
        + "/" + (reducing == Reducing.standard() ? "_" : "Reducing:" + reducing)
        + "/Recovery:" + recovery;
        //@formatter:on
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
    InputStack           extends IInputStack,
    BacktrackChoicePoint extends IBacktrackChoicePoint<InputStack, StackNode>,
    ParseState           extends AbstractParseState<InputStack, StackNode> & IRecoveryParseState<InputStack, StackNode, BacktrackChoicePoint>>
//@formatter:on
    IParser<? extends IParseForest>
        withRecovery(Parser<ParseForest, ?, ?, StackNode, InputStack, ParseState, ?, ?> parser) {
        parser.reduceManager.addFilter(new RecoveryReduceActionFilter<>());
        parser.observing.attachObserver(new RecoveryObserver<>());

        return parser;
    }

    private static
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    IParser<? extends IParseForest> withoutRecovery(Parser<ParseForest, ?, ?, StackNode, ?, ParseState, ?, ?> parser) {
        parser.reduceManager.addFilter(ReduceActionFilter.ignoreRecoveryAndCompletion());

        return parser;
    }

    public IParser<? extends IParseForest> getParser(IParseTable parseTable) {
        return getParser(parseTable, new ActiveStacksFactory(activeStacksRepresentation),
            new ForActorStacksFactory(forActorStacksRepresentation));
    }

    private
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    ReducerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState> reducerFactory() {
        if(parseForestConstruction == ParseForestConstruction.Optimized)
            return ReducerOptimized.factoryOptimized();
        else
            return Reducer.factory();
   }

    public IParser<? extends IParseForest> getParser(IParseTable parseTable, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory) {
        if(!this.isValid())
            throw new IllegalStateException("Invalid parser variant: " + validate().collect(Collectors.joining(", ")));

        InputStackFactory<IInputStack> inputStackFactory = InputStack::new;
        InputStackFactory<LayoutSensitiveInputStack> inputStackFactoryLS = LayoutSensitiveInputStack::new;
        IncrementalInputStackFactory<IIncrementalInputStack> incrementalInputStackFactory =
            EagerIncrementalInputStack::new; // TODO switch between Eager, Lazy, and Linked?
        IncrementalInputStackFactory<IIncrementalInputStack> incrementalRecoveryInputStackFactory =
            LinkedIncrementalInputStack::new;

        // @formatter:off
        switch(this.parseForestRepresentation) {
            default:
            case Basic: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicElkhoundStackManager.factory(),  BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicElkhoundStackManager.factory(),  BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case HybridElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridElkhoundStackManager.factory(), BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridElkhoundStackManager.factory(), BasicParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  BasicParseForestManager.factory(), ReduceManager.factory(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicStackManager.factory(),  BasicParseForestManager.factory(), ReduceManager.factory(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), BasicParseForestManager.factory(), ReduceManager.factory(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridStackManager.factory(), BasicParseForestManager.factory(), ReduceManager.factory(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with basic parse forest representation");
            }

            case Null: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicElkhoundStackManager.factory(),  NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicElkhoundStackManager.factory(),  NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case HybridElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridElkhoundStackManager.factory(), NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridElkhoundStackManager.factory(), NullParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  NullParseForestManager.factory(), ReduceManager.factory(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicStackManager.factory(),  NullParseForestManager.factory(), ReduceManager.factory(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), NullParseForestManager.factory(), ReduceManager.factory(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridStackManager.factory(), NullParseForestManager.factory(), ReduceManager.factory(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with empty parse forest representation");
            }

            case Hybrid: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicElkhoundStackManager.factory(),  HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicElkhoundStackManager.factory(),  HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case HybridElkhound:
                        if (this.recovery)
                            return    withRecovery(new ElkhoundParser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridElkhoundStackManager.factory(), HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new ElkhoundParser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridElkhoundStackManager.factory(), HybridParseForestManager.factory(), ElkhoundReduceManager.factoryElkhound(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  HybridParseForestManager.factory(), ReduceManager.factory(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicStackManager.factory(),  HybridParseForestManager.factory(), ReduceManager.factory(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), HybridParseForestManager.factory(), ReduceManager.factory(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridStackManager.factory(), HybridParseForestManager.factory(), ReduceManager.factory(reducerFactory()), DefaultParseFailureHandler.factory()));
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
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicStackManager.factory(),  DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new Parser<>(inputStackFactory, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new Parser<>(inputStackFactory, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridStackManager.factory(), DataDependentParseForestManager.factory(), DataDependentReduceManager.factoryDataDependent(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }

            case LayoutSensitive:
                if(this.reducing != Reducing.LayoutSensitive || this.recovery)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(inputStackFactoryLS, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(inputStackFactoryLS, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicStackManager.factory(),  LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(inputStackFactoryLS, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(inputStackFactoryLS, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridStackManager.factory(), LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }

            case Composite:
                if(this.reducing != Reducing.Composite || this.recovery)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(inputStackFactoryLS, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(inputStackFactoryLS, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, BasicStackManager.factory(),  CompositeParseForestManager.factory(), CompositeReduceManager.factoryComposite(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        //if (this.recovery)
                        //    return    withRecovery(new Parser<>(inputStackFactoryLS, RecoveryParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), LayoutSensitiveParseForestManager.factory(), LayoutSensitiveReduceManager.factoryLayoutSensitive(this), RecoveryParseFailureHandler.factory()));
                        //else
                            return withoutRecovery(new Parser<>(inputStackFactoryLS, ParseState.factory(activeStacksFactory, forActorStacksFactory),         parseTable, HybridStackManager.factory(), CompositeParseForestManager.factory(), CompositeReduceManager.factoryComposite(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }

            case Incremental:
                if(this.reducing != Reducing.Incremental)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return    withRecovery(new IncrementalParser<>(incrementalRecoveryInputStackFactory, RecoveryIncrementalParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new IncrementalParser<>(        incrementalInputStackFactory,         IncrementalParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, BasicStackManager.factory(),  IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(reducerFactory()), DefaultParseFailureHandler.factory()));
                    case Hybrid:
                        if (this.recovery)
                            return    withRecovery(new IncrementalParser<>(incrementalRecoveryInputStackFactory, RecoveryIncrementalParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(reducerFactory()), RecoveryParseFailureHandler.factory()));
                        else
                            return withoutRecovery(new IncrementalParser<>(        incrementalInputStackFactory,         IncrementalParseState.factory(activeStacksFactory, forActorStacksFactory), parseTable, HybridStackManager.factory(), IncrementalParseForestManager.factory(), IncrementalReduceManager.factoryIncremental(reducerFactory()), DefaultParseFailureHandler.factory()));
                    default: throw new IllegalStateException();
                }
        }
        // @formatter:on
    }
}
