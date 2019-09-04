package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.datadependent.DataDependentParseForestManager;
import org.spoofax.jsglr2.elkhound.BasicElkhoundStackManager;
import org.spoofax.jsglr2.elkhound.ElkhoundParser;
import org.spoofax.jsglr2.elkhound.HybridElkhoundStackManager;
import org.spoofax.jsglr2.incremental.IncrementalParse;
import org.spoofax.jsglr2.incremental.IncrementalParser;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForestManager;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseForestManager;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForestManager;
import org.spoofax.jsglr2.parseforest.empty.NullParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForestManager;
import org.spoofax.jsglr2.parser.failure.DefaultParseFailureHandler;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.recovery.IRecoveryState;
import org.spoofax.jsglr2.recovery.RecoveryParseFailureHandler;
import org.spoofax.jsglr2.recovery.RecoveryParserObserver;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
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
        // Elkhound reducing requires Elkhound stack, and the other way around (bi-implication)
        boolean validElkhound =
            (reducing == Reducing.Elkhound) == (stackRepresentation == StackRepresentation.BasicElkhound
                || stackRepresentation == StackRepresentation.HybridElkhound);
        // PFR Null requires PFC Full (the implication N -> F is written as !N || F)
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
        // Recovery and incremental parsing not simultaneously supported
        boolean validRecoveryIncremental =
            !(parseForestRepresentation == ParseForestRepresentation.Incremental && recovery);

        return validElkhound && validParseForest && validIncremental && validLayoutSensitive && validDataDependent
            && validRecoveryIncremental;
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
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode> & IRecoveryState<ParseForest, StackNode>>
//@formatter:on
    IParser<? extends IParseForest> withRecovery(IObservableParser<ParseForest, StackNode, ParseState> parser) {
        parser.observing().attachObserver(new RecoveryParserObserver<>());

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
                            return withRecovery(new ElkhoundParser<>(Parse.factory(this), parseTable, new BasicElkhoundStackManager<>(),  new BasicParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new ElkhoundParser<>(Parse.factory(this), parseTable, new BasicElkhoundStackManager<>(),  new BasicParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case HybridElkhound:
                        if (this.recovery)
                            return withRecovery(new ElkhoundParser<>(Parse.factory(this), parseTable, new HybridElkhoundStackManager<>(), new BasicParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new ElkhoundParser<>(Parse.factory(this), parseTable, new HybridElkhoundStackManager<>(), new BasicParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new BasicParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new BasicParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case Hybrid:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new BasicParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new BasicParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with basic parse forest representation");
            }

            case Null: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return withRecovery(new ElkhoundParser<>(Parse.factory(this), parseTable, new BasicElkhoundStackManager<>(),  new NullParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new ElkhoundParser<>(Parse.factory(this), parseTable, new BasicElkhoundStackManager<>(),  new NullParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case HybridElkhound:
                        if (this.recovery)
                            return withRecovery(new ElkhoundParser<>(Parse.factory(this), parseTable, new HybridElkhoundStackManager<>(), new NullParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new ElkhoundParser<>(Parse.factory(this), parseTable, new HybridElkhoundStackManager<>(), new NullParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new NullParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new NullParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case Hybrid:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new NullParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new NullParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with empty parse forest representation");
            }

            case Hybrid: switch(this.reducing) {
                case Elkhound: switch(this.stackRepresentation) {
                    case BasicElkhound:
                        if (this.recovery)
                            return withRecovery(new ElkhoundParser<>(Parse.factory(this), parseTable, new BasicElkhoundStackManager<>(),  new HybridParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new ElkhoundParser<>(Parse.factory(this), parseTable, new BasicElkhoundStackManager<>(),  new HybridParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case HybridElkhound:
                        if (this.recovery)
                            return withRecovery(new ElkhoundParser<>(Parse.factory(this), parseTable, new HybridElkhoundStackManager<>(), new HybridParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new ElkhoundParser<>(Parse.factory(this), parseTable, new HybridElkhoundStackManager<>(), new HybridParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new HybridParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new HybridParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case Hybrid:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new HybridParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new HybridParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(this), new DefaultParseFailureHandler<>());
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
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new DataDependentParseForestManager<>(), ReduceManagerFactory.dataDependentReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new DataDependentParseForestManager<>(), ReduceManagerFactory.dataDependentReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case Hybrid:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new DataDependentParseForestManager<>(), ReduceManagerFactory.dataDependentReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new DataDependentParseForestManager<>(), ReduceManagerFactory.dataDependentReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException();
                }

            case LayoutSensitive:
                if(this.reducing != Reducing.LayoutSensitive)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new LayoutSensitiveParseForestManager<>(), ReduceManagerFactory.layoutSensitiveReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new BasicStackManager<>(),  new LayoutSensitiveParseForestManager<>(), ReduceManagerFactory.layoutSensitiveReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case Hybrid:
                        if (this.recovery)
                            return withRecovery(new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new LayoutSensitiveParseForestManager<>(), ReduceManagerFactory.layoutSensitiveReduceManagerFactory(this), new RecoveryParseFailureHandler<>()));
                        else
                            return              new Parser<>(Parse.factory(this), parseTable, new HybridStackManager<>(), new LayoutSensitiveParseForestManager<>(), ReduceManagerFactory.layoutSensitiveReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException();
                }

            case Incremental:
                if(this.reducing != Reducing.Incremental || this.recovery)
                    throw new IllegalStateException();

                switch(this.stackRepresentation) {
                    case Basic:  return new IncrementalParser<>(IncrementalParse.factory(this), IncrementalParse.incrementalFactory(this), parseTable, new BasicStackManager<>(),  new IncrementalParseForestManager<>(), ReduceManagerFactory.incrementalReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    case Hybrid: return new IncrementalParser<>(IncrementalParse.factory(this), IncrementalParse.incrementalFactory(this), parseTable, new HybridStackManager<>(), new IncrementalParseForestManager<>(), ReduceManagerFactory.incrementalReduceManagerFactory(this), new DefaultParseFailureHandler<>());
                    default: throw new IllegalStateException();
                }
        }
        // @formatter:on
    }
}
