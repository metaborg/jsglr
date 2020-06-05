package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parser.Position;

public abstract class JSGLR2Result<AbstractSyntaxTree> {

    public final Collection<Message> messages;

    JSGLR2Result(JSGLR2Request request, Collection<Message> messages) {
        this.messages = postProcessMessages(request, messages);
    }

    public abstract boolean isSuccess();

    private List<Message> postProcessMessages(JSGLR2Request request, Collection<Message> originalMessages) {
        List<Message> messages = new ArrayList<>();

        for(Message originalMessage : originalMessages) {
            Message message = originalMessage;

            // Make sure messages are not reported outside the input region
            if(message.region != null && message.region.endOffset > request.input.length() - 1) {
                SourceRegion region = null;

                if(request.input.length() > 0) {
                    Position end = Position.atOffset(request.input, request.input.length() - 1);

                    if(message.region.startOffset > request.input.length() - 1)
                        region = new SourceRegion(end, end);
                    else
                        region = new SourceRegion(message.region.position(), end);
                }

                message = message.atRegion(region);
            }

            messages.add(message);
        }

        return messages;
    }

}
