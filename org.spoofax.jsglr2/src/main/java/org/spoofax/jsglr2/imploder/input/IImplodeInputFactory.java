package org.spoofax.jsglr2.imploder.input;

public interface IImplodeInputFactory<Input extends ImplodeInput> {

    Input get(String inputString);

}
