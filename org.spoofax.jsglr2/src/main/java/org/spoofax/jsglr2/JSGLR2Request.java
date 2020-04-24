package org.spoofax.jsglr2;

import java.util.Objects;
import java.util.Optional;

public class JSGLR2Request {

    public final String input;
    public final String fileName;
    public final String startSymbol;
    public final int recoveryIterationsQuota;
    public final int succeedingRecoveryOffset;
    public final Optional<Integer> completionCursorOffset;

    static public final int DEFAULT_RECOVERY_ITERATIONS_QUOTA = 5;
    static public final int DEFAULT_SUCCEEDING_RECOVERY_OFFSET = 10;

    public JSGLR2Request(String input) {
        this(input, "");
    }

    public JSGLR2Request(String input, String fileName) {
        this(input, fileName, null);
    }

    public JSGLR2Request(String input, String fileName, String startSymbol) {
        this(input, fileName, startSymbol, DEFAULT_RECOVERY_ITERATIONS_QUOTA, DEFAULT_SUCCEEDING_RECOVERY_OFFSET,
            Optional.empty());
    }

    public JSGLR2Request(String input, String fileName, String startSymbol, int recoveryIterationsQuota,
        int succeedingRecoveryOffset, Optional<Integer> completionCursorOffset) {
        this.input = input;
        this.fileName = fileName;
        this.startSymbol = startSymbol;
        this.recoveryIterationsQuota = recoveryIterationsQuota;
        this.succeedingRecoveryOffset = succeedingRecoveryOffset;
        this.completionCursorOffset = completionCursorOffset;
    }

    public boolean hasFileName() {
        return !"".equals(fileName);
    }

    public int succeedingRecoveryOffset() {
        return succeedingRecoveryOffset;
    }

    public boolean isCompletion() {
        return completionCursorOffset.isPresent();
    }

    public CachingKey cachingKey() {
        return new CachingKey(fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset);
    }

    public static class CachingKey {

        final String fileName;
        final String startSymbol;
        final int recoveryIterationsQuota;
        final int succeedingRecoveryOffset;

        CachingKey(String fileName, String startSymbol, int recoveryIterationsQuota, int succeedingRecoveryOffset) {
            this.fileName = fileName;
            this.startSymbol = startSymbol;
            this.recoveryIterationsQuota = recoveryIterationsQuota;
            this.succeedingRecoveryOffset = succeedingRecoveryOffset;
        }

        @Override public boolean equals(Object o) {
            if(o == null || getClass() != o.getClass())
                return false;
            CachingKey that = (CachingKey) o;
            return Objects.equals(fileName, that.fileName) && Objects.equals(startSymbol, that.startSymbol)
                && recoveryIterationsQuota == that.recoveryIterationsQuota
                && succeedingRecoveryOffset == that.succeedingRecoveryOffset;
        }

        @Override public int hashCode() {
            return Objects.hash(fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset);
        }

    }

    @Override public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass())
            return false;
        JSGLR2Request that = (JSGLR2Request) o;
        return Objects.equals(input, that.input) && Objects.equals(fileName, that.fileName)
            && Objects.equals(startSymbol, that.startSymbol) && recoveryIterationsQuota == that.recoveryIterationsQuota
            && succeedingRecoveryOffset == that.succeedingRecoveryOffset
            && completionCursorOffset.equals(that.completionCursorOffset);
    }

    @Override public int hashCode() {
        return Objects.hash(input, fileName, startSymbol, recoveryIterationsQuota, succeedingRecoveryOffset,
            completionCursorOffset);
    }

}
