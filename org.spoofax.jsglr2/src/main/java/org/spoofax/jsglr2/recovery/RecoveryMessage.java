package org.spoofax.jsglr2.recovery;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.spoofax.jsglr2.messages.Category;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;

public class RecoveryMessage extends Message {

    public final RecoveryType recoveryType;

    public RecoveryMessage(@Nonnull String message, @Nullable RecoveryType recoveryType,
        @Nullable SourceRegion region) {
        super(message, Category.RECOVERY, region);
        this.recoveryType = recoveryType;
    }

}
