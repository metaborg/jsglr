package org.spoofax.jsglr2;

import java.util.Objects;
import java.util.Optional;

public class JSGLR2Request {

    public final String input;
    public final String fileName;
    public final String startSymbol;
    public final int recoveryIterationsQuota;
    public final int succeedingRecoveryOffset;
    public final int recoveryTimeout;
    public final Optional<Integer> completionCursorOffset;
    public final boolean reportAmbiguities;

    static public final int DEFAULT_RECOVERY_ITERATIONS_QUOTA = 5;
    static public final int DEFAULT_SUCCEEDING_RECOVERY_OFFSET = 10;
    static public final int DEFAULT_RECOVERY_TIMEOUT = 1000; // in milliseconds

    public JSGLR2Request(String input) {
        this(input, "");
    }

    public JSGLR2Request(String input, String fileName) {
        this(input, fileName, null);
    }

    public JSGLR2Request(String input, String fileName, String startSymbol) {
        this(input, fileName, startSymbol, DEFAULT_RECOVERY_ITERATIONS_QUOTA, DEFAULT_SUCCEEDING_RECOVERY_OFFSET,
            DEFAULT_RECOVERY_TIMEOUT, Optional.empty(), false);
    }

    public JSGLR2Request(String input, String fileName, String startSymbol, int recoveryIterationsQuota,
        int succeedingRecoveryOffset, int recoveryTimeout, Optional<Integer> completionCursorOffset,
        boolean reportAmbiguities) {
        this.input = input;
        this.fileName = fileName;
        this.startSymbol = startSymbol;
        this.recoveryIterationsQuota = recoveryIterationsQuota;
        this.succeedingRecoveryOffset = succeedingRecoveryOffset;
        this.recoveryTimeout = recoveryTimeout;
        this.completionCursorOffset = completionCursorOffset;
        this.reportAmbiguities = reportAmbiguities;
    }

    public JSGLR2Request(String input, String fileName, String startSymbol, int recoveryIterationsQuota,
        int succeedingRecoveryOffset, int recoveryTimeout, Optional<Integer> completionCursorOffset) {
        this(input, fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset, recoveryTimeout,
            completionCursorOffset, false);
    }

    public boolean isCacheable() {
        return !"".equals(fileName);
    }

    public boolean isCompletion() {
        return completionCursorOffset.isPresent();
    }

    public JSGLR2Request withCompletion(Optional<Integer> completionCursorOffset) {
        return new JSGLR2Request(input, fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset,
            recoveryTimeout, completionCursorOffset, reportAmbiguities);
    }

    public JSGLR2Request withAmbiguitiesReporting(boolean reportAmbiguities) {
        return new JSGLR2Request(input, fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset,
            recoveryTimeout, Optional.empty(), reportAmbiguities);
    }

    public CachingKey cachingKey() {
        if(!isCacheable())
            throw new IllegalStateException("This JSGLR2Request is not cacheable");
        return new CachingKey(fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset,
            recoveryTimeout);
    }

    public static class CachingKey {

        final String fileName;
        final String startSymbol;
        final int recoveryIterationsQuota;
        final int succeedingRecoveryOffset;
        final int recoveryTimeout;

        CachingKey(String fileName, String startSymbol, int recoveryIterationsQuota, int succeedingRecoveryOffset,
            int recoveryTimeout) {
            this.fileName = fileName;
            this.startSymbol = startSymbol;
            this.recoveryIterationsQuota = recoveryIterationsQuota;
            this.succeedingRecoveryOffset = succeedingRecoveryOffset;
            this.recoveryTimeout = recoveryTimeout;
        }

        @Override public boolean equals(Object o) {
            if(o == null || getClass() != o.getClass())
                return false;
            CachingKey that = (CachingKey) o;
            return Objects.equals(fileName, that.fileName) && Objects.equals(startSymbol, that.startSymbol)
                && recoveryIterationsQuota == that.recoveryIterationsQuota
                && succeedingRecoveryOffset == that.succeedingRecoveryOffset && recoveryTimeout == that.recoveryTimeout;
        }

        @Override public int hashCode() {
            return Objects.hash(fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset,
                recoveryTimeout);
        }

    }

    @Override public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass())
            return false;
        JSGLR2Request that = (JSGLR2Request) o;
        return Objects.equals(input, that.input) && Objects.equals(fileName, that.fileName)
            && Objects.equals(startSymbol, that.startSymbol) && recoveryIterationsQuota == that.recoveryIterationsQuota
            && succeedingRecoveryOffset == that.succeedingRecoveryOffset && recoveryTimeout == that.recoveryTimeout
            && completionCursorOffset.equals(that.completionCursorOffset)
            && reportAmbiguities == that.reportAmbiguities;
    }

    @Override public int hashCode() {
        return Objects.hash(input, fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset,
            recoveryTimeout, completionCursorOffset, reportAmbiguities);
    }

}
