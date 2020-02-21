package org.spoofax.jsglr2.recovery;

import java.util.HashMap;
import java.util.Map;

public class RecoveryJob<StackNode> {

    public int offset;
    public int iteration;
    final int iterationsQuota;
    public Map<StackNode, Integer> quota;

    public RecoveryJob(int offset, int iterationsQuota) {
        this.offset = offset;
        this.iteration = -1;
        this.iterationsQuota = iterationsQuota;
        this.quota = new HashMap<>();
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

    void updateQuota(StackNode stack, int newQuota) {
        quota.merge(stack, newQuota, Math::max);
    }

}
