package org.spoofax.jsglr2.messages;

public enum Severity {
    WARNING(1), ERROR(2);

    public final int value;

    Severity(int value) {
        this.value = value;
    }
}
