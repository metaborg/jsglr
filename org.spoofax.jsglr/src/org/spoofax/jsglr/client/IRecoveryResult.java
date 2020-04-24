package org.spoofax.jsglr.client;

import java.util.Map;

public interface IRecoveryResult {
    String getResult();
    Map<Integer, char[]> getSuggestions();
}
