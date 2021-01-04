package org.spoofax.jsglr2.recovery;

import java.util.HashMap;
import java.util.Map;

public class RecoveryJob<StackNode> {

    public int offset;
    public int iteration;
    final int iterationsQuota;
    final long timeoutAt;
    public Map<StackNode, Integer> quota;
    public Map<StackNode, Integer> lastRecoveredOffset;

    public RecoveryJob(int offset, int iterationsQuota, int timeout) {
        this.offset = offset;
        this.iteration = -1;
        this.iterationsQuota = iterationsQuota;
        this.timeoutAt = System.currentTimeMillis() + timeout;
        this.quota = new HashMap<>();
        this.lastRecoveredOffset = new HashMap<>();
    }

    boolean hasNextIteration() {
        return iteration + 1 < iterationsQuota;
    }

    int nextIteration() {
        return ++iteration;
    }

    void initQuota(Iterable<StackNode> activeStacks) {
        int quotaPerStack = iteration + 1;

        quota.clear();
        activeStacks.forEach(stack -> quota.put(stack, quotaPerStack));
    }

    int getQuota(StackNode stack) {
        return quota.getOrDefault(stack, 0);
    }

    int lastRecoveredOffset(StackNode stack) {
        return lastRecoveredOffset.getOrDefault(stack, -1);
    }

    void updateQuota(StackNode stack, int newQuota) {
        quota.merge(stack, newQuota, Math::max);
    }

    void updateLastRecoveredOffset(StackNode stack, int offset) {
        if(offset != -1)
            lastRecoveredOffset.merge(stack, offset, Math::max);
    }

    boolean timeout() {
        return System.currentTimeMillis() >= timeoutAt;
    }

}
