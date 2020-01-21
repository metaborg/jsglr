package org.spoofax.jsglr2.inputstack;

public interface InputStackFactory<InputStack> {

    InputStack get(String inputString, String fileName);

}
